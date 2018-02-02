package com.sztouyun.advertisingsystem.repository.account;

import com.sztouyun.advertisingsystem.model.account.Role;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends BaseRepository<Role> {
    Page<Role> findByRoleNameContaining(String roleName, Pageable pageable);

    Role findById(String id);

    /**
     * 查询是否有其角色占用角色名称
     *
     * @param id
     * @param roleName
     * @return
     */
    @Query(value = "select 1 from  Role r where r.id<>?1 and r.roleName=?2")
    Integer findByRoleNameAndId(String id, String roleName);

    /**
     * 查询是否有其他人占用角色名
     *
     * @param id
     * @param roleName
     * @return
     */
    boolean existsByIdNotAndRoleName(String id, String roleName);

    /**
     * 查询是否有人占用角色名
     *
     * @param roleName
     * @return
     */
    boolean existsByRoleName(String roleName);

}
