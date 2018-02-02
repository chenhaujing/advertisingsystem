package com.sztouyun.advertisingsystem.controller;

import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected User getUser() {
        return AuthenticationService.getUser();
    }
}
