package com.sztouyun.advertisingsystem.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.config.EnvironmentConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailsService detailsService;

    @Autowired
    private MockUserAuthenticationFilter mockUserAuthenticationFilter;

    @Autowired
    private EnvironmentConfig environmentConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().disable();
        if(environmentConfig.isDebug()){
            http.addFilterBefore(mockUserAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/api/**").authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint())
            .and()
            .formLogin()
            .loginPage("/login")
            .successHandler(successHandler())
            .failureHandler(failureHandler())
            .permitAll()
            .and()
            .cors()
            .and()
            .rememberMe().tokenValiditySeconds(604800).alwaysRemember(true)//记住密码7天
            .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessHandler(logoutSuccessHandler())
            .permitAll()
            .and()
            .csrf()
            .disable();
}

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("123").roles("USER");
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/static/**","/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**");
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService);
    }

    private AuthenticationSuccessHandler successHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
            httpServletResponse.getWriter().append(new ObjectMapper().writeValueAsString(InvokeResult.SuccessResult()));
            httpServletResponse.setStatus(200);
        };
    }

    private AuthenticationFailureHandler failureHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            String message = e.getMessage();
            if(message.equals("Bad credentials")){
                message ="用户名或密码错误！";
            }
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
            httpServletResponse.getWriter().append(new ObjectMapper().writeValueAsString(InvokeResult.Fail(message)));
            httpServletResponse.setStatus(200);
        };
    }

    private LogoutSuccessHandler logoutSuccessHandler(){
        return (httpServletRequest, httpServletResponse, authentication)->{
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
            httpServletResponse.getWriter().append(new ObjectMapper().writeValueAsString(InvokeResult.SuccessResult()));
            httpServletResponse.setStatus(200);
        };
    }

    @Bean
    @Profile({"local","dev","test"})
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
