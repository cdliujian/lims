package com.lanshan.core.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lanshan.core.business.IUser;
import com.lanshan.core.entity.DefaultUser;

@Repository
public class UserRepository {
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
    public IUser findUserByNameOrMobile(String username) {
		List<IUser> users = (List<IUser>) jdbcTemplate.query("SELECT U.USERNAME_,U.CNAME_,U.ENAME_,U.ADMINISTRATOR_,U.BIRTHDAY_,U.EMAIL_,U.ENABLED_,U.MALE_,U.MOBILE_,U.PASSWORD_,U.SALT_,U.ADDRESS_,U.DEPT_ID,U.ID FROM SM_USER U WHERE (U.MOBILE_=? OR U.USERNAME_=? )", new Object[] { username,username }, new DefaultUserRowMapper());
		if (users.size() == 0) {
			return null;
		}
        return users.get(0);
    }
	
	class DefaultUserRowMapper implements RowMapper<IUser> {

		public IUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultUser user = new DefaultUser();
			user.setUsername(rs.getString("USERNAME_"));
			user.setCname(rs.getString("CNAME_"));
			user.setEname(rs.getString("ENAME_"));
			user.setAdministrator(rs.getBoolean("ADMINISTRATOR_"));
			user.setBirthday(rs.getDate("BIRTHDAY_"));
			
			user.setEmail(rs.getString("EMAIL_"));
			user.setEnabled(rs.getBoolean("ENABLED_"));
			user.setMale(rs.getBoolean("MALE_"));
			user.setMobile(rs.getString("MOBILE_"));
			user.setPassword(rs.getString("PASSWORD_"));
			user.setSalt(rs.getString("SALT_"));
			user.setAddress(rs.getString("ADDRESS_"));
			user.setDeptId(rs.getInt("DEPT_ID"));
			user.setId(rs.getInt("ID"));
			return user;
		}

	}
}
