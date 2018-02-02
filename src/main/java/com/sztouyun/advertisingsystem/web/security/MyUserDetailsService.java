package com.sztouyun.advertisingsystem.web.security;

import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.service.account.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名不能空！");
        }

        User user = userService.getUserAndRoleByUsernameFromCache(username);
        if (user == null)
            throw new UsernameNotFoundException("用户不存在！");

        if (!user.isEnableFlag())
            throw new DisabledException("您已经被管理员禁用，无法登陆，请联系管理员解除！");

        return user;
    }
}
