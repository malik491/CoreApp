package edu.depaul.se491.builders;

import edu.depaul.se491.beans.AddressBean;

public class AddressBuilder {
	private AddressBean bean;
	
	public AddressBuilder() {
		this.bean = new AddressBean();
	}
	
	public AddressBuilder(final AddressBean bean) {
		this.bean = new AddressBean(bean.getId(), bean.getLine1(), bean.getLine2(), bean.getCity(), bean.getState(), bean.getZipcode());
	}
	
	public AddressBean build() {
		return bean;
	}
	
	public void clearAll() {
		bean = new AddressBean();
	}
}
