package com.lanshan.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



/**
 * 认证微服务
 * @author liujian
 * @date 2018-08-20
 * **/
@SpringBootApplication(scanBasePackages=("com.lanshan"))
@EnableDiscoveryClient
@EnableWebSecurity
@EnableRedisHttpSession
@EnableOAuth2Client
@EnableAuthorizationServer
public class LsimsAuthApplication extends WebMvcConfigurerAdapter{

	public static void main(String[] args) {
		SpringApplication.run(LsimsAuthApplication.class, args);
	}
}
