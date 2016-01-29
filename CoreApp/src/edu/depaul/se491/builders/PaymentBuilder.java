/**
 * 
 */
package edu.depaul.se491.builders;

import edu.depaul.se491.beans.PaymentBean;

/**
 * @author Malik
 *
 */
public class PaymentBuilder {
	private PaymentBean bean;
	
	public PaymentBuilder() {
		this.bean = new PaymentBean();
	}
	
	public PaymentBuilder(final PaymentBean bean) {
		this.bean = new PaymentBean(bean.getId(), bean.getTotal(), bean.getType(), bean.getCreditCard(), bean.getTransactionConfirmation());
	}

	public PaymentBean build() {
		return bean;
	}
	
	public void clearAll() {
		bean = new PaymentBean();
	}
}
