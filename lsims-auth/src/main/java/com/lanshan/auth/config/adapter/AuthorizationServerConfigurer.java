package com.lanshan.auth.config.adapter;

import java.security.KeyPair;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.Assert;

/**
 * 证书及客户端配置
 * @author qxs
 * @date 2017-06-12
 * **/
@Configuration
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {
	
	/**
	 * 证书路径
	 * **/
	@Value("${jwt.access.token.converter.resource.location}")
	private Resource resourceLocation = new ClassPathResource("keystore.jks");
	/**
	 * 证书密码
	 * **/
	@Value("${jwt.access.token.converter.resource.password}")
	private String password;
	/**
	 * 证书密码
	 * **/
	@Value("${jwt.access.token.converter.resource.key-pair-alias}")
	private String keyPairAlias;
	
	@Autowired
	private DataSource dataSource;
//	@Autowired
//	private AuthenticationManager authenticationManager;
	
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
    	Assert.notNull(resourceLocation, "resourceLocations can not be null");
    	Assert.hasLength(password, "password can not be empty");
    	Assert.notNull(keyPairAlias, "keyPairAlias can not be empty");
    	
    	JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(resourceLocation, password.toCharArray()).getKeyPair(keyPairAlias);
        converter.setKeyPair(keyPair);
        return converter;
    }
    
	@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                 .withClient("acme")
               .secret("{noop}acmesecret")
               .authorizedGrantTypes("authorization_code", "refresh_token")
               .scopes("openid").autoApprove(true);
    	//clients.withClientDetails(clientDetails());
    }
    @Bean
	public ClientDetailsService clientDetails() {
		return new JdbcClientDetailsService(dataSource);
	}

//    @Bean
//	public TokenStore tokenStore() {
//		return new JdbcTokenStore(dataSource);
//	}
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//    	endpoints.authenticationManager(authenticationManager);
//		endpoints.tokenStore(tokenStore());
//
//		// 配置TokenServices参数
//		DefaultTokenServices tokenServices = new DefaultTokenServices();
//		tokenServices.setTokenStore(endpoints.getTokenStore());
//		tokenServices.setSupportRefreshToken(false);
//		tokenServices.setClientDetailsService(endpoints
//				.getClientDetailsService());
//		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
//		tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS
//				.toSeconds(30)); // 30天
//		endpoints.tokenServices(tokenServices);
    	
    	endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
    
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

}
