package edu.sjsu.LPOS.util;

import edu.sjsu.LPOS.beans.UserBean;
import edu.sjsu.LPOS.domain.User;

public class UserBeanUtil {

	public static UserBean getUserBeanFromUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("Null user can not be converted to UserBean.");
		}
		UserBean bean = new UserBean();
		bean.setEmail(user.getEmail());
		bean.setUserId(user.getId());
		bean.setPhonenumber(user.getPhonenumber());
		bean.setUsername(user.getUsername());
		bean.setAddress(user.getAddress());
		return bean;
	}
}
