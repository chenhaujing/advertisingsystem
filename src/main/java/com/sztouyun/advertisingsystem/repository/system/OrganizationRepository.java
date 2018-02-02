package com.sztouyun.advertisingsystem.repository.system;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

public interface OrganizationRepository extends BaseRepository<Organization> {
    Organization findById(String id);

    Organization findByName(String name);

    @Override
    default BooleanBuilder getAuthorizationFilter(){
        return AuthenticationService.getOrganizationAuthenticationFilter();
    }

    boolean existsByOwnerId(String ownerId);
}
