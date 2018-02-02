package com.sztouyun.advertisingsystem.service.account;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.QRole;
import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.account.Role;
import com.sztouyun.advertisingsystem.model.system.Menu;
import com.sztouyun.advertisingsystem.model.system.QMenu;
import com.sztouyun.advertisingsystem.repository.account.RoleRepository;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.system.MenuRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.account.RoleMenuViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "roles")
public class RoleService extends BaseService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    private final QRole qRole = QRole.role;

    private final QUser qUser = QUser.user;

    private final QMenu qMenu = QMenu.menu;

    @Transactional
    public void createRole(Role roleDto) {
        if (roleRepository.existsByRoleName(roleDto.getRoleName()))
            throw new BusinessException("角色名称已存在");
        Role role = new Role(roleDto.getRoleName(), roleDto.getDescription(), roleDto.getRoleType());
        roleRepository.save(role);
    }

    @CacheEvict(key = "#p0")
    @Transactional
    public void deleteRole(String roleId) {
        Role role = roleRepository.findOne(roleId);
        if (null == role)
            throw new BusinessException("角色不存在");
        if (userRepository.exists(qUser.roleId.eq(roleId)))
            throw new BusinessException("角色已被用户使用不能删除！");
        roleRepository.delete(role);
    }

    @CacheEvict(key = "#p0.getId()")
    @Transactional
    public void updateRole(Role roleDto) {
        String id = roleDto.getId();
        String newRoleName = roleDto.getRoleName();
        Role role = roleRepository.findOne(id);
        if (null == role)
            throw new BusinessException("该角色不存在");
        if (roleRepository.existsByIdNotAndRoleName(id, newRoleName))
            throw new BusinessException("该角色名称已存在");
        role.setRoleName(roleDto.getRoleName());
        role.setDescription(roleDto.getDescription());
        role.setUpdatedTime(new Date());
        roleRepository.save(role);
    }

    @CacheEvict(key = "#p0")
    @Transactional
    public void toggledEnable(String id) {
        Role role = roleRepository.findOne(id);
        if (role == null)
            throw new BusinessException("该角色不存在");
        boolean currentStatus = role.isEnable();
        role.setEnableFlag(!currentStatus);
        roleRepository.save(role);

    }

    public Role getRole(String id) {
        if (StringUtils.isEmpty(id))
            throw new BusinessException("角色id不能为空");
        Role role = roleRepository.findOne(id);
        if (null == role)
            throw new BusinessException("该角色不存在");
        return role;
    }

    public Page<Role> queryRoleList(String roleName, Pageable pageable) {
        Page<Role> pages = roleRepository.findByRoleNameContaining(roleName, pageable);
        return pages;
    }

    @Transactional
    public void distributeMenu(RoleMenuViewModel roleMenuViewModel) {
        Role role = roleRepository.findOne(qRole.id.eq(roleMenuViewModel.getRoleId()));
        if (null == role)
            throw new BusinessException("该角色不存在");

        List<String> ids = roleMenuViewModel.getIds();
        Iterable<Menu> menuList = menuRepository.findAll(qMenu.id.in(ids));
        List<Menu> menus = new ArrayList<>();
        menuList.forEach(menu -> menus.add(menu));
        if (menus.size() == 0)
            throw new BusinessException("菜单分配失败");

        role.setMenus(menus);
        roleRepository.save(role);
    }

    public Iterable<Role> getAllRole() {
        Iterable<Role> list = roleRepository.findAll();
        return list;
    }

    @Cacheable(key = "#p0")
    public Role findRoleByIdFromCache(String roleId) {
        return roleRepository.findById(roleId);
    }
}
