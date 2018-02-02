package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;


/**
 * Created by szty on 2017/7/28.
 */
@ApiModel
public class PermissionViewModel extends CreatePermissionViewModel {

    @ApiModelProperty(value = "权限Id",required = true)
    @NotBlank(message = "权限Id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
