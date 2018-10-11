package com.lanshan.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lanshan.auth.service.IUserService;
import com.lanshan.core.business.IUser;
import com.lanshan.core.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		IUser user = userRepository.findUserByNameOrMobile(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户名 " + username
					+ " 不存在");
		}
		return user;
	}
	
	

}
