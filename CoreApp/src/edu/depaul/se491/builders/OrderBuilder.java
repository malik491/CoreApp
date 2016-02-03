/**
 * 
 */
package edu.depaul.se491.builders;

import edu.depaul.se491.beans.OrderBean;

/**
 * @author Malik
 *
 */
public class OrderBuilder {
	private OrderBean bean;
	
	public OrderBuilder() {
		this.bean = new OrderBean();
	}
	
	public OrderBuilder(final OrderBean bean) {
		this.bean = new OrderBean();
		
		this.bean.setId(bean.getId());
		this.bean.setType(bean.getType());
		this.bean.setStatus(bean.getStatus());
		this.bean.setConfirmation(bean.getConfirmation());
		this.bean.setTimestamp(bean.getTimestamp());
		this.bean.setPayment(bean.getPayment());
		this.bean.setItems(bean.getItems());
		this.bean.setAddress(bean.getAddress());
	}
	
	public OrderBean build() {
		return bean;
	}
	
	public void clearAll() {
		bean = new OrderBean();
	}
}
