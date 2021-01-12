package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
public class RetryServiceImpl {

    @Autowired
    private RestTemplateConfig restTemplateConfig;

    @Autowired
    private RetryTemplateConfig retryTemplateConfig;

    @EventListener(ContextRefreshedEvent.class)
    public void run() {
        getHello();
        System.out.println("Here");
    }

    public void getHello() {
        try {
            retryTemplateConfig.retryTemplate().execute(context -> {
                System.out.println("inside retry method");
                String requestData = restTemplateConfig.customRestTemplate().getForObject("http://12.21.12.21:3131/test", String.class);
                System.out.println("Response ..."+ requestData);
                throw new IllegalStateException("Something went wrong");
            });
        } catch (ResourceAccessException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
