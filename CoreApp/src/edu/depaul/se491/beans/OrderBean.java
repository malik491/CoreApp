package edu.depaul.se491.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;

/**
 * Order Bean
 * 
 * @author Malik
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
	
	/**
	 * constrcut OrderBean
	 */
	public OrderBean() {
	}
		
	/**
	 * return id
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * set id
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * return timestamp
	 * @return
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * set timestamp
	 * @param timestamp
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * return confirmation
	 * @return
	 */
	public String getConfirmation() {
		return confirmation;
	}

	/**
	 * set confirmation
	 * @param confirmation
	 */
	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
	
	/**
	 * return type
	 * @return
	 */
	public OrderType getType() {
		return type;
	}
	
	/**
	 * return type
	 * @param type
	 */
	public void setType(OrderType type) {
		this.type = type;
	}
	
	/**
	 * return status
	 * @return
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * set status
	 * @param status
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	/**
	 * return payment
	 * @return
	 */
	public PaymentBean getPayment() {
		return payment;
	}

	/**
	 * set payment
	 * @param payment
	 */
	public void setPayment(PaymentBean payment) {
		this.payment = payment;
	}
	
	/**
	 * return order items
	 * @return
	 */
	public OrderItemBean[] getOrderItems() {
		return orderItems;
	}
	
	/**
	 * set order items
	 * @param items
	 */
	public void setOrderItems(OrderItemBean[] orderItems) {
		this.orderItems = orderItems;
	}

	/**
	 * return delivery address
	 * @return
	 */
	public AddressBean getAddress() {
		return address;
	}

	/**
	 * set delivery address
	 * @param address
	 */
	public void setAddress(AddressBean address) {
		this.address = address;
	}
}
