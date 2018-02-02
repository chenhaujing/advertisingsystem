package com.sztouyun.advertisingsystem.service.system;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.Permission;
import com.sztouyun.advertisingsystem.model.system.QMenu;
import com.sztouyun.advertisingsystem.model.system.QPermission;
import com.sztouyun.advertisingsystem.repository.system.MenuRepository;
import com.sztouyun.advertisingsystem.repository.system.PermissionRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by szty on 2017/7/28.
 */
@Service
public class PermissionService extends BaseService{

    private static final String ROOT_PERMISSION_ID = "0";//表示顶级权限

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuRepository menuRepository;

    private final QPermission qPermission = QPermission.permission;

    private final QMenu qMenu = QMenu.menu;

    @Transactional
    public void createPermission(Permission permission){
        //权限名称、url不能重复
        boolean isExists = permissionRepository.exists(qPermission.permissionName.eq(permission.getPermissionName()).or(qPermission.apiUrl.eq(permission.getApiUrl())) );
        if(isExists)
            throw new BusinessException("权限已存在");

        if(!menuRepository.exists(qMenu.id.eq(permission.getMenuId())) )
            throw new BusinessException("菜单不存在");

        permissionRepository.save(permission);
    }

    @Transactional
    public void updatePermission(Permission permission){
        boolean isExists = permissionRepository.exists(qPermission.permissionName.eq(permission.getPermissionName()).or(qPermission.apiUrl.eq(permission.getApiUrl())) );
        if(isExists)
            throw new BusinessException("权限已存在");

        if(!menuRepository.exists(qMenu.id.eq(permission.getMenuId())) )
            throw new BusinessException("菜单不存在");

        permission.setUpdatedTime(new Date());
        permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(String id){
        boolean isExists = permissionRepository.exists(qPermission.id.eq(id));
        if(!isExists)
            throw new BusinessException("菜单不存在");

        permissionRepository.deleteById(id);
    }

    public List<Permission> getAllPermission(){
        List<Permission> permissionList = permissionRepository.findAll();
       return permissionList;
    }

    public Permission getPermission(String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("权限ID不能为空！");

        Permission permission= permissionRepository.findOne(qPermission.id.eq(id));
        if(null == permission)
            throw new BusinessException("权限不存在！");
        return permission;
    }

    public Page<Permission> findByPage(Pageable pageable){
        Page<Permission> pages= permissionRepository.findAll(pageable);
        return  pages;
    }
}
