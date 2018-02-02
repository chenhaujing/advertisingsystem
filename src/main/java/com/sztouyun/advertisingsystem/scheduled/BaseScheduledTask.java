package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class BaseScheduledTask {

    @Autowired
    protected UserRepository userRepository;

    private final QUser qUser = QUser.user;

    protected void  setAdminLogin(){
        User adminUser  = userRepository.findFirstByIsAdmin(true);
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(adminUser, adminUser.getPassword(),adminUser.getAuthorities()));
    }
}
