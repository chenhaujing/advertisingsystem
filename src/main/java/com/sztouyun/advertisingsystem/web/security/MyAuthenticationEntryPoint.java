package com.sztouyun.advertisingsystem.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        boolean isAjax = "XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"));
        boolean isApi =httpServletRequest.getRequestURL().toString().contains("/api/");
        if(isAjax || isApi){
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
            httpServletResponse.getWriter().append(new ObjectMapper().writeValueAsString(InvokeResult.Fail("请先登录！",401)));
            httpServletResponse.setStatus(200);
            return;
        }
        httpServletResponse.sendRedirect("/#/login");
    }
}
