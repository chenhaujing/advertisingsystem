package com.sztouyun.advertisingsystem.service.account;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.*;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.repository.account.RoleRepository;
import com.sztouyun.advertisingsystem.repository.account.UserArchiveRepository;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.system.OrganizationRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.account.UserInfoCountViewModel;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@CacheConfig(cacheNames = "users")
public class UserService extends BaseService {
    public static String DEFAULT_PASSWORD = "992fed45712235bee217768818704d3f";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserArchiveRepository userArchiveRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContractRepository contractRepository;

    private final QUser qUser = QUser.user;

    private final QUserArchive qUserArchive = QUserArchive.userArchive;

    private final QCustomer qCustomer = QCustomer.customer;

    private final QContract qContract = QContract.contract;

    @Transactional
    public void modifyPassword(String password, String newPassword) {
        User user = getUser();
        if (!user.getPassword().equals(password))
            throw new BusinessException("密码错误");
        user.setPassword(newPassword);
        userRepository.save(user);

    }

    @Transactional
    public void createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new BusinessException("该电话号码已注册");
        if (userRepository.existsByNickname(user.getNickname()))
            throw new BusinessException("该用户名已注册");
        String organizationId = user.getOrganizationId();
        if (!StringUtils.isEmpty(organizationId) && !organizationRepository.exists(organizationId))
            throw new BusinessException("该组织编号不存在");
        user.setPassword(DEFAULT_PASSWORD);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId);
        if (null == user)
            throw new BusinessException("用户不存在");
        if (customerRepository.existsByOwnerId(userId))
            throw new BusinessException("用户是客户的负责人，不允许删除");
        if (organizationRepository.existsByOwnerId(userId))
            throw new BusinessException("用户是组织的负责人，不允许删除");
        if (contractRepository.existsByOwnerId(userId))
            throw new BusinessException("用户下存在合同，不允许删除");
        deleteUser(user);
    }

    @Caching(evict = {@CacheEvict(key = "#p0.getId()"), @CacheEvict(key = "#p0.getUsername()")})
    public void deleteUser(User user) {
        UserArchive userArchive = ApiBeanUtils.copyProperties(user, UserArchive.class);
        userArchiveRepository.save(userArchive);
        userRepository.delete(user.getId());
    }

    @Transactional
    public void updateUser(User user, String roleId) {
        String id = user.getId();
        User oldUser = userRepository.findById(id);
        if (null == oldUser)
            throw new BusinessException("该用户不存在");
        String newUserName = user.getUsername();
        if (userRepository.existsByIdNotAndUsername(id, newUserName))
            throw new BusinessException("该电话号码已存在");
        String newNickName = user.getNickname();
        if (userRepository.existsByIdNotAndNickname(id, newNickName))
            throw new BusinessException("该姓名已存在");
        if (!StringUtils.isEmpty(user.getOrganizationId()) && !organizationRepository.exists(user.getOrganizationId()))
            throw new BusinessException("该组织编号不存在");
        Role newRole = roleRepository.findOne(roleId);
        if (newRole == null)
            throw new BusinessException("角色ID不存在");
        if (oldUser.getRoleTypeEnum().equals(RoleTypeEnum.ManagerialStaff) && newRole.getRoleType().equals(RoleTypeEnum.SaleMan.getValue()) && organizationRepository.existsByOwnerId(id))
            throw new BusinessException("该区域管理人员负责一个组织机构，请重新指定该组织机构负责人，才能修改角色");
        oldUser.setRoleId(roleId);
        oldUser.setUpdatedTime(new Date());
        if (oldUser.getRoleTypeEnum().equals(RoleTypeEnum.SaleMan) && newRole.getRoleType().equals(RoleTypeEnum.ManagerialStaff.getValue())) {
            oldUser.setOrganizationId(null);
        } else {
            oldUser.setOrganizationId(user.getOrganizationId());
        }
        oldUser.setUsername(newUserName);
        oldUser.setNickname(newNickName);
        userRepository.save(oldUser);
    }

    public User getUserAndRoleByUsernameFromCache(String username) {
        if (StringUtils.isEmpty(username))
            return null;
        User user = getUserByUsernameFromCache(username);
        if (user == null)
            return null;
        if (!StringUtils.isEmpty(user.getRoleId())) {
            Role role = roleService.findRoleByIdFromCache(user.getRoleId());
            if (role != null) {
                user.setRole(role);
            }
        }
        return user;
    }

    public Page<User> queryUserList(String username, Pageable pageable) {
        Page<User> pages = userRepository.findAllAuthorized(qUser.nickname.contains(username), pageable,
                new JoinDescriptor().leftJoin(qUser.role).leftJoin(qUser.organization)
                , false);
        return pages;
    }

    public Page<User> findAllByOrganizationIdAndUsername(String organizationId, String username, Pageable pageable) {
        Organization organization = organizationRepository.findById(organizationId);
        if (null == organization)
            throw new BusinessException("组织编号不存在");
        BooleanBuilder predicate = GetPredicateByOrganizationIdOrUsername(organizationId, username);
        Page<User> pages = userRepository.findAllAuthorized(predicate, pageable, new JoinDescriptor().leftJoin(qUser.role).leftJoin(qUser.organization), false);
        return pages;
    }

    public User getUser(String id) {
        return getUser(id, null);
    }

    public User getUser(String id, JoinDescriptor joinDescriptor) {
        if (StringUtils.isEmpty(id))
            throw new BusinessException("用户ID不能为空！");
        User user = userRepository.findOne(qUser.id.eq(id), joinDescriptor);
        if (null == user)
            throw new BusinessException("该用户不存在");
        return user;
    }

    /**
     * 根据组织结构或者用户名查询用户并统计用户维护客户数量
     */
    public Page<User> getUserInfoByOrganizationIdOrUsername(String organizationId, String username, Pageable pageable) {
        BooleanBuilder predicate = GetPredicateByOrganizationIdOrUsername(organizationId, username);
        predicate.and(qUser.isAdmin.eq(false));
        Page<User> pages = userRepository.findAllAuthorized(predicate, pageable, true);
        if (pages.getContent().size() <= 0)
            return pages;

        List<User> userList = pages.getContent();
        List<String> ownerIds = Linq4j.asEnumerable(userList).select(user -> user.getId()).toList();
        //获取维护客户数量
        List<UserInfoCountViewModel> customerAmountList =
                customerRepository.findAll(q -> q
                        .select(Projections.bean(UserInfoCountViewModel.class, qCustomer.ownerId.as("ownerId"), qCustomer.count().as("customerCount")))
                        .from(qCustomer)
                        .where(qCustomer.ownerId.in(ownerIds))
                        .groupBy(qCustomer.ownerId));

        //获取合同签约的数量
        List<Integer> contractStatus = new ArrayList<Integer>();
        contractStatus.add(ContractStatusEnum.PendingSign.getValue());
        contractStatus.add(ContractStatusEnum.PendingExecution.getValue());
        contractStatus.add(ContractStatusEnum.AbruptlyTerminated.getValue());
        contractStatus.add(ContractStatusEnum.Finished.getValue());
        List<UserInfoCountViewModel> signedContractCountList = contractRepository.findAll(q -> q
                .select(Projections.bean(UserInfoCountViewModel.class, qContract.ownerId.as("ownerId"), qContract.count().as("signedContractCount")))
                .from(qContract)
                .where(qContract.ownerId.in(ownerIds).and(qContract.contractStatus.in(contractStatus)))
                .groupBy(qContract.ownerId));

        userList.forEach(user -> {
            UserInfoCountViewModel userInfoCount = Linq4j.asEnumerable(customerAmountList).firstOrDefault(userInfoCountViewModel -> (userInfoCountViewModel.getOwnerId()).equals(user.getId()));
            if (userInfoCount != null) {
                user.setCustomerCount(userInfoCount.getCustomerCount());
            }
            UserInfoCountViewModel signedContractCount = Linq4j.asEnumerable(signedContractCountList).firstOrDefault(userInfoCountViewModel -> (userInfoCountViewModel.getOwnerId()).equals(user.getId()));
            if (signedContractCount != null) {
                user.setSignedContractCount(signedContractCount.getSignedContractCount());
            }
        });
        return pages;
    }

    private BooleanBuilder GetPredicateByOrganizationIdOrUsername(String organizationId, String username) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(organizationId)) {
            predicate.and(qUser.organizationId.eq(organizationId));
        }
        if (!StringUtils.isEmpty(username)) {
            predicate.and(qUser.nickname.contains(username));
        }
        return predicate;
    }

    public Page<User> getOrganizationOwners(String username, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(qUser.organizationId.isNull());
        if (!StringUtils.isEmpty(username)) {
            booleanBuilder.and(qUser.nickname.contains(username));
        }
        BooleanBuilder roleFilter = new BooleanBuilder(qUser.role.roleType.eq(RoleTypeEnum.ManagerialStaff.getValue()));
        if (getUser().isAdmin()) {
            roleFilter.or(qUser.isAdmin.eq(true));
        }
        booleanBuilder.and(roleFilter);
        //查询负责人特殊处理，不过滤组织权限
        Page<User> pages = userRepository.findAll(booleanBuilder.and(userRepository.getStatusFilter()), pageable,
                new JoinDescriptor().leftJoin(qUser.role).leftJoin(qUser.organization));
        return pages;
    }

    @Transactional
    public void toggledEnable(String id) {
        User user = userRepository.findById(id);
        if (null == user)
            throw new BusinessException("用户不存在");
        boolean currentStatus = user.isEnable();
        user.setEnableFlag(!currentStatus);
        userRepository.save(user);
    }

    @Transactional
    public InvokeResult removeUserFromOrganization(String organizationId, String userId) {
        boolean organizationExists = organizationRepository.exists(organizationId);
        if (!organizationExists)
            throw new BusinessException("组织ID不存在");
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new BusinessException("用户ID不存在");
        user.setOrganizationId(null);
        userRepository.save(user);
        return InvokeResult.SuccessResult();
    }

    @Transactional
    public InvokeResult batchRemoveUserFromOrganization(String organizationId, List<String> list) {
        boolean organizationExists = organizationRepository.exists(organizationId);
        if (!organizationExists)
            throw new BusinessException("组织ID不存在");
        if (list.isEmpty())
            throw new BusinessException("用户ID列表不能为空");
        List<User> existsList = userRepository.findByOrganizationIdAndIdIn(organizationId, list);
        if (existsList.isEmpty() || existsList.size() != list.size())
            throw new BusinessException("组织ID和用户ID列表数据不准确");
        userRepository.updateByOrganizationIdAndIdIn(organizationId, list);
        return InvokeResult.SuccessResult();
    }

    @Cacheable(key = "#p0")
    public String getNicknameFromCache(String userId) {
        if (StringUtils.isEmpty(userId))
            return "";

        String nickname = userRepository.findOne(q -> q.select(qUser.nickname).from(qUser).where(qUser.id.eq(userId)));
        if (nickname == null) {
            nickname = userArchiveRepository.findOne(q -> q.select(qUserArchive.nickname).from(qUserArchive).where(qUserArchive.id.eq(userId)));
        }
        if (nickname == null)
            return "";

        return nickname;
    }

    @Cacheable(key = "#p0")
    public User getUserByUsernameFromCache(String username) {
        return userRepository.findByUsername(username);
    }

    public List<String> getAccessedUserIds() {
        return userRepository.findAllAuthorized(q -> q.select(qUser.id).from(qUser), false);
    }
}
