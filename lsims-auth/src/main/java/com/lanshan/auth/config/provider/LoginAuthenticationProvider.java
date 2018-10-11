package com.lanshan.auth.config.provider;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;

import com.lanshan.core.business.IUser;
import com.lanshan.core.util.Md5PasswordEncoder;


public class LoginAuthenticationProvider extends DaoAuthenticationProvider {
	
	private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
	private final static Logger logger = LoggerFactory.getLogger(LoginAuthenticationProvider.class);
	
	public LoginAuthenticationProvider(UserDetailsService userDetailsService) {
        super();
        // 这个地方一定要对userDetailsService赋值，不然userDetailsService是null (这个坑有点深)
        setUserDetailsService(userDetailsService);
    }

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		IUser user=(IUser)userDetails;
		String presentedPassword = authentication.getCredentials().toString();
		String enPwd=this.passwordEncoder.encodePassword(presentedPassword,user.getSalt());
		if(!enPwd.equals(user.getPassword()) ){
             throw new BadCredentialsException("密码错误");
		}else{
			
		}
	}
	
	
	
	
	
	

}
