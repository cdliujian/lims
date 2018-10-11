package com.lanshan.web.admin;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.bstek.dorado.web.loader.DoradoLoader;
import com.bstek.dorado.web.servlet.DoradoServlet;
import com.lanshan.web.admin.config.DoradoApplicationContextInilializer;
import com.lanshan.web.admin.config.SessionSerializer;

@EnableDiscoveryClient
@EnableWebSecurity
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=99999999)
@EnableOAuth2Client
@SpringBootApplication(scanBasePackages=("com.lanshan") , exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		JpaRepositoriesAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})
public class LsimsAdminApplication {
	
	private final static String SESSION_SERIALIZATION_ID = "microservices4vaadin";

	public static void main(String[] args) {
		System.setProperty("doradoHome", "classpath:dorado-home/");
		SpringApplication app = new SpringApplication(LsimsAdminApplication.class);
		app.addInitializers(new DoradoApplicationContextInilializer());

		DoradoLoader doradoLoader = DoradoLoader.getInstance();
		try {
			doradoLoader.preload(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Set<String> sources = new LinkedHashSet<String>();
		sources.addAll(doradoLoader.getContextLocations(false));
		app.setSources(sources);

		app.run(args);
	}
	
	@Autowired
    private ApplicationContext appContext;
	
	@Bean
    public String overwriteSerializationId() {
        BeanFactory beanFactory = appContext.getAutowireCapableBeanFactory();
        ((DefaultListableBeanFactory) beanFactory).setSerializationId(SESSION_SERIALIZATION_ID);
        return "overwritten";
    }

    @Bean
    public CookieHttpSessionIdResolver httpSessionStrategy() {
      CookieHttpSessionIdResolver cookieHttpSessionStrategy = new CookieHttpSessionIdResolver();
      DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
      cookieSerializer.setCookieName("JSESSIONID");
      cookieSerializer.setCookiePath("/");
      cookieSerializer.setCookieMaxAge(48*60*60);
      cookieSerializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
      cookieHttpSessionStrategy.setCookieSerializer(cookieSerializer);
      return cookieHttpSessionStrategy;
    }

    @Bean
    public SessionSerializer springSessionDefaultRedisSerializer() {
        return new SessionSerializer();
    }
    
    @Bean
	public ServletRegistrationBean doradoServlet() {
		ServletRegistrationBean servlet = new ServletRegistrationBean(
				new DoradoServlet());
		servlet.setLoadOnStartup(1);
		servlet.addUrlMappings("*.d");
		servlet.addUrlMappings("*.c");
		servlet.addUrlMappings("*.dpkg");
		servlet.addUrlMappings("/dorado/*");
		return servlet;
	}
}
