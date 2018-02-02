package com.sztouyun.advertisingsystem.model.account;

import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
public class User extends BaseUser implements UserDetails {
    @Transient
    private Long customerCount;

    @Transient
    private Long signedContractCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    private Organization organization;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        if (StringUtils.isEmpty(organizationId)) {
            this.organizationId = null;
        } else {
            this.organizationId = organizationId;
        }

    }

    public Organization getOrganization() {
        return organization;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public RoleTypeEnum getRoleTypeEnum() {
        if (isAdmin())
            return RoleTypeEnum.Admin;

        Role role = getRole();
        if (role == null)
            return RoleTypeEnum.SaleMan;
        return EnumUtils.toEnum(role.getRoleType(), RoleTypeEnum.class);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + getRoleTypeEnum().toString()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnableFlag();
    }

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Long getSignedContractCount() {
        return signedContractCount;
    }

    public void setSignedContractCount(Long signedContractCount) {
        this.signedContractCount = signedContractCount;
    }
}
