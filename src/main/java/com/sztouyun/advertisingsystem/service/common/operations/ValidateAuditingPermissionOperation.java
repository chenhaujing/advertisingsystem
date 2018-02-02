package com.sztouyun.advertisingsystem.service.common.operations;

import com.sztouyun.advertisingsystem.common.operation.INoDataOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class ValidateAuditingPermissionOperation implements INoDataOperation {
    @Override
    public void operate() {
        User user = AuthenticationService.getUser();
        if (user.isAdmin())
            return;

        RoleTypeEnum roleTypeEnum = user.getRoleTypeEnum();
        if (!roleTypeEnum.equals(RoleTypeEnum.ManagerialStaff))
            throw new BusinessException("权限不足！");
    }
}
