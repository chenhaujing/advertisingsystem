package com.sztouyun.advertisingsystem.service.account;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.model.system.QOrganization;
import com.sztouyun.advertisingsystem.repository.system.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Service
public class AuthenticationService {

    @Autowired
    private OrganizationRepository repository;

    private static OrganizationRepository organizationRepository;

    @PostConstruct
    public void init() {
        organizationRepository = repository;
    }

    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取用户相关权限过滤条件
     *
     * @return
     */
    public static BooleanBuilder getUserAuthenticationFilter(StringPath userFiled) {
        User user = getUser();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (user.isAdmin())
            return booleanBuilder;

        switch (user.getRoleTypeEnum()) {
            case ManagerialStaff:
                QUser qUser = QUser.user;
                String userOrganizationCode = getUserOrganizationCode();
                BooleanBuilder myCreatedFilter = new BooleanBuilder(qUser.organizationId.isNull().and(qUser.creatorId.eq(user.getId())));
                //没有组织就查询自己的和自己创建并且没有组织的人
                if (StringUtils.isEmpty(userOrganizationCode))
                    return booleanBuilder.and(userFiled.eq(user.getId())
                            .or(userFiled.in(JPAExpressions.select(qUser.id).from(qUser).where(myCreatedFilter))));

                //查询自己组织下面的人和自己创建并且没有组织的人
                JPQLQuery<String> query = JPAExpressions.select(qUser.id).from(qUser).leftJoin(qUser.organization)
                        .where(getOrganizationCodeAuthentication(qUser.organization.code, userOrganizationCode)
                                .or(myCreatedFilter));
                booleanBuilder.and(userFiled.in(query));
                break;
            case SaleMan:
                booleanBuilder.and(userFiled.eq(user.getId()));
                break;
        }
        return booleanBuilder;
    }

    /**
     * 获取组织相关权限过滤
     *
     * @return
     */
    public static BooleanBuilder getOrganizationAuthenticationFilter() {
        StringPath organizationCodeFiled = QOrganization.organization.code;
        User user = getUser();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (user.isAdmin())
            return booleanBuilder;
        String userOrganizationCode = getUserOrganizationCode();
        if (StringUtils.isEmpty(userOrganizationCode))
            return booleanBuilder.and(organizationCodeFiled.isNull());

        return getOrganizationCodeAuthentication(organizationCodeFiled, userOrganizationCode);
    }

    private static String getUserOrganizationCode() {
        User user = getUser();
        if (StringUtils.isEmpty(user.getOrganizationId()))
            return null;

        Organization organization = organizationRepository.findById(user.getOrganizationId());
        if (organization == null)
            return null;
        return organization.getCode();
    }

    private static BooleanBuilder getOrganizationCodeAuthentication(StringPath organizationCodeFiled, String organizationCode) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        User user = getUser();
        switch (user.getRoleTypeEnum()) {
            case ManagerialStaff:
                booleanBuilder.and(organizationCodeFiled.startsWith(organizationCode));
                break;
            case SaleMan:
                booleanBuilder.and(organizationCodeFiled.eq(organizationCode));
                break;
        }
        return booleanBuilder;
    }
}
