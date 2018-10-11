package com.lanshan.web.admin.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * 网关服务安全配置信息
 * 
 * @author qxs
 * @date 2017-06-09
 * **/
@Configuration
@EnableOAuth2Sso
@EnableResourceServer
@Order(value = 0)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	/**
	 * 登录action配置
	 * **/
	@Value("${security.oauth2.sso.loginPath}")
	private String securityOauth2SsoLoginPath = "/login";
	/**
	 * 认证中心跳转路径前缀
	 * **/
	@Value("${uaa.server.service.path}")
	private String uaaServerServicePath = "/uaa/**";

	/**
	 * 不走认证的url集合
	 * **/
	@Value("${http.authorize.matchers}")
	private String[] httpAuthorizeMatchers;

	@Autowired
	private ResourceServerTokenServices resourceServerTokenServices;

	@Bean
	@Primary
	public OAuth2ClientContextFilter dynamicOauth2ClientContextFilter() {
		return new DynamicOauth2ClientContextFilter();
	}

	private String[] getHttpAuthorizeMatchers() {
		List<String> matchers = new ArrayList<String>();
		matchers.add(uaaServerServicePath);
		matchers.add(securityOauth2SsoLoginPath);
		if (httpAuthorizeMatchers != null) {
			for (String httpAuthorizeMatcher : httpAuthorizeMatchers) {
				matchers.add(httpAuthorizeMatcher);
			}
		}

		return matchers.toArray(new String[matchers.size()]);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 不需要认证的路径
		http.authorizeRequests().antMatchers(getHttpAuthorizeMatchers())
				.permitAll().anyRequest().authenticated();

		http.addFilterAfter(oAuth2AuthenticationProcessingFilter(),
				AbstractPreAuthenticatedProcessingFilter.class);

		// 退出
		//http.logout().permitAll().logoutSuccessUrl("/");

		// 禁用csrf,csrf校验功能全部放到子系统
		http.csrf().disable();

		// 允许加载同源下的iframe页面
		http.headers().frameOptions().sameOrigin();

		

	}

	private OAuth2AuthenticationProcessingFilter oAuth2AuthenticationProcessingFilter() {
		OAuth2AuthenticationProcessingFilter oAuth2AuthenticationProcessingFilter = new OAuth2AuthenticationProcessingFilter();
		oAuth2AuthenticationProcessingFilter
				.setAuthenticationManager(oauthAuthenticationManager());
		oAuth2AuthenticationProcessingFilter.setStateless(false);

		return oAuth2AuthenticationProcessingFilter;
	}

	private AuthenticationManager oauthAuthenticationManager() {
		OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
		oAuth2AuthenticationManager.setResourceId("apigateway");
		oAuth2AuthenticationManager
				.setTokenServices(resourceServerTokenServices);
		oAuth2AuthenticationManager.setClientDetailsService(null);

		return oAuth2AuthenticationManager;
	}

	@Bean
	public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer(
			final SpringClientFactory springClientFactory) {
		return new UserInfoRestTemplateCustomizer() {
			@Override
			public void customize(OAuth2RestTemplate template) {
				ClientHttpRequestFactory clientHttpRequestFactory = new RibbonClientHttpRequestFactory(
						springClientFactory);

				List<AccessTokenProvider> accessTokenProviderList = new ArrayList<AccessTokenProvider>();

				accessTokenProviderList
						.add(new AuthorizationCodeAccessTokenProvider());
				accessTokenProviderList.add(new ImplicitAccessTokenProvider());
				accessTokenProviderList
						.add(new ResourceOwnerPasswordAccessTokenProvider());
				accessTokenProviderList
						.add(new ClientCredentialsAccessTokenProvider());
				for (AccessTokenProvider accessTokenProvider : accessTokenProviderList) {
					((OAuth2AccessTokenSupport) accessTokenProvider)
							.setRequestFactory(clientHttpRequestFactory);
				}

				AccessTokenProviderChain providerChain = new AccessTokenProviderChain(
						accessTokenProviderList);
				template.setAccessTokenProvider(providerChain);
			}
		};
	}

	// /**
	// * 解决同源策略问题的filter
	// * @return
	// */
	// @Bean
	// public FilterRegistrationBean corsFilter() {
	// UrlBasedCorsConfigurationSource source = new
	// UrlBasedCorsConfigurationSource();
	// CorsConfiguration config = new CorsConfiguration();
	// config.setAllowCredentials(true);
	// config.addAllowedOrigin("*");
	// config.addAllowedHeader("*");
	// config.addAllowedMethod("OPTIONS");
	// config.addAllowedMethod("HEAD");
	// config.addAllowedMethod("GET");
	// config.addAllowedMethod("PUT");
	// config.addAllowedMethod("POST");
	// config.addAllowedMethod("DELETE");
	// config.addAllowedMethod("PATCH");
	// source.registerCorsConfiguration("/**", config);
	// // return new CorsFilter(source);
	// final FilterRegistrationBean bean = new FilterRegistrationBean(new
	// CorsFilter(source));
	// bean.setOrder(0);
	// return bean;
	// }
}
