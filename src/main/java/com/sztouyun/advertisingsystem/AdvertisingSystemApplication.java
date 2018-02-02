package com.sztouyun.advertisingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled=true)
@MapperScan("com.sztouyun.advertisingsystem.mapper")
public class AdvertisingSystemApplication {
	public static void main(String[] args) {
		SpringApplication springApplication =new SpringApplication(AdvertisingSystemApplication.class);
		springApplication.addListeners(new ApplicationStartup());
		springApplication.run(args);
	}

}
