package com.sztouyun.advertisingsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by wenfeng on 2017/8/28.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private FontDownloadThread fontThread;
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        Thread thread = new Thread(fontThread);
        thread.start();
    }
}