package com.SzTouYun.advertisingsystem;

import com.sztouyun.advertisingsystem.AdvertisingSystemApplication;
import com.sztouyun.advertisingsystem.service.customer.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by szty on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdvertisingSystemApplication.class)
public class CustomerTest {
    @Autowired
    private CustomerService customerService;

    @Test
    public void save(){
    }

}
