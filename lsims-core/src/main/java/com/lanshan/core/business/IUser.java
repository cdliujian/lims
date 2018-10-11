package com.lanshan.core.business;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUser extends UserDetails {

	String getCname();
	String getEname();
	boolean isAdministrator();
	String getMobile();
	String getEmail();
	String getSalt() ;
	boolean isAccountNonLocked();
	String getPassword() ;
	Integer getDeptId();
	Integer getId();
}
