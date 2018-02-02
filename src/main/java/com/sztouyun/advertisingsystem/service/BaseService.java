package com.sztouyun.advertisingsystem.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    //实体管理者
    @Autowired
    private EntityManager entityManager;

    //查询工厂
    private  JPAQueryFactory queryFactory;

    //初始化查询工厂
    @PostConstruct
    public void init()
    {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    protected User getUser(){
        return AuthenticationService.getUser();
    }

    protected <T> Query createNativeQuery(String nativeSql,String resultClass){
        return entityManager.createNativeQuery(nativeSql,resultClass);
    }

    protected <T> Query createNativeQuery(String nativeSql){
        return entityManager.createNativeQuery(nativeSql);
    }
}
