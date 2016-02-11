/**
 * Base class for Delivery and Pickup order beans
 */
package edu.depaul.se491.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;

/**
 * @author Malik
 *
 */
@XmlRootElement
public class OrderBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private OrderStatus status;
	private OrderType type;
	private Timestamp timestamp;
	private String confirmation;
	private PaymentBean payment;
	private AddressBean address;
	private OrderItemBean[] orderItems;
	
	public OrderBean() {
	}
		
	/**
	 * return order id
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * set order id
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * return the order timestamp (data & time)
	 * @return
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * set the order timestamp (data & time)
	 * @param timestamp
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * return order confirmation number
	 * @return
	 */
	public String getConfirmation() {
		return confirmation;
	}

	/**
	 * set the order confirmation number
	 * @param confirmation
	 */
	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
	
	/**
	 * return order type
	 * @return
	 */
	public OrderType getType() {
		return type;
	}
	
	/**
	 * return order type
	 * @param type
	 */
	public void setType(OrderType type) {
		this.type = type;
	}
	
	/**
	 * return order status
	 * @return
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * set order status
	 * @param status
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public PaymentBean getPayment() {
		return payment;
	}

	public void setPayment(PaymentBean payment) {
		this.payment = payment;
	}
	
	/**
	 * return all order items for this order
	 * @return
	 */
	public OrderItemBean[] getOrderItems() {
		return orderItems;
	}
	
	/**
	 * set order items for this order
	 * @param items
	 */
	public void setOrderItems(OrderItemBean[] orderItems) {
		this.orderItems = orderItems;
	}

	public AddressBean getAddress() {
		return address;
	}

	public void setAddress(AddressBean address) {
		this.address = address;
	}
}
