package com.lanshan.auth.config.adapter;



import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lanshan.auth.config.LoginSuccessHandler;
import com.lanshan.auth.config.provider.LoginAuthenticationProvider;
import com.lanshan.auth.service.IUserService;




@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    private final static String SESSION_SERIALIZATION_ID = "microservices4vaadin";
    
    
    @Autowired
    private ApplicationContext appContext;
    /**
	 * 不走认证的url集合
	 * **/
	@Value("${http.authorize.matchers}")
	private String[] httpAuthorizeMatchers;
	
	@Value("${http.login.path}")
	private String loginPath = "/login";
	
//	@Value("${csrf.request.matchers}")
	private AntPathRequestMatcher[] csrfRequestMatchers = new AntPathRequestMatcher[0];
	
	@Autowired
    private IUserService userService;
	
    @Autowired @Qualifier("dataSource")
    private DataSource dataSource;
    
	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests().antMatchers(httpAuthorizeMatchers).permitAll().anyRequest().authenticated();
    	
        http.formLogin().loginPage(loginPath).permitAll().successHandler(loginSuccessHandler());
        
        http.logout().permitAll();
        
        //http.rememberMe().tokenValiditySeconds(86400).tokenRepository(tokenRepository());
        http.csrf().requireCsrfProtectionMatcher(csrfRequestMatcher())
        .csrfTokenRepository(csrfTokenRepository());
      
        //解决不允许显示在iframe的问题
        http.headers().frameOptions().sameOrigin();
//      //session管理
//        //session失效后跳转
//        http.sessionManagement().invalidSessionUrl(loginPath);
//        //只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
//        http.sessionManagement().maximumSessions(1).expiredUrl(loginPath);
    }
 // Disable CSFR protection on the following urls:
   
    private RequestMatcher csrfRequestMatcher() {
        return new RequestMatcher() {
            private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|OPTIONS|TRACE)$");
            
            @Override
            public boolean matches(HttpServletRequest request) {
                if (allowedMethods.matcher(request.getMethod()).matches()) {
                    return false;
                }

                for (AntPathRequestMatcher matcher : csrfRequestMatchers) {
                    if (matcher.matches(request)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(CSRF_HEADER_NAME);
        return repository;
    }
    @Bean
    public JdbcTokenRepositoryImpl tokenRepository(){
        JdbcTokenRepositoryImpl jtr = new JdbcTokenRepositoryImpl();
        jtr.setDataSource(dataSource);
        return jtr;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(daoAuthenticationProvider());
        
        //不删除凭据，以便记住用户
        auth.eraseCredentials(false);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder;
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new LoginAuthenticationProvider(userService);
        
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        
        return daoAuthenticationProvider;
    }
    

	@Bean
	FilterRegistrationBean forwardedHeaderFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(new ForwardedHeaderFilter());
		filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegBean;
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
    public String overwriteSerializationId() {
        BeanFactory beanFactory = appContext.getAutowireCapableBeanFactory();
        ((DefaultListableBeanFactory) beanFactory).setSerializationId(SESSION_SERIALIZATION_ID);
        return "overwritten";
    }
     
}
