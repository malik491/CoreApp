package edu.depaul.se491.builders;

import edu.depaul.se491.beans.UserBean;

public class UserBuilder {
	private UserBean bean;
	
	public UserBuilder() {
		this.bean = new UserBean();
	}
	
	public UserBuilder(final UserBean bean) {
		this.bean = new UserBean(bean.getId(), bean.getFirstName(), bean.getLastName(), bean.getEmail(), bean.getPhone(), bean.getAddress());
	}
	
	public UserBean build() {
		return bean;
	}
	
	public void clearAll() {
		bean = new UserBean();
	}
}
