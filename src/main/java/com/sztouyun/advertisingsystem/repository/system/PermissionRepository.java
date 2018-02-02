package com.sztouyun.advertisingsystem.repository.system;

import com.sztouyun.advertisingsystem.model.system.Permission;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by szty on 2017/7/28.
 */
public interface PermissionRepository extends BaseRepository<Permission> {

    Permission findByPermissionName(String permissionName);

    Permission findById(String id);

    Integer deleteById(String id);

    List<Permission> findByMenuId(String menuId);

    @Query(value = "SELECT 1 FROM Permission t WHERE t.permissionName = :permissionName AND t.menuId <> :menuId AND t.id <> :id")
    String findRepeatPermission(@Param("permissionName") String permissionName,@Param("menuId") String menuId, @Param("id") String id);
}
