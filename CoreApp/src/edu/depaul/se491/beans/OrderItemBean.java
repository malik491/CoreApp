package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.OrderItemStatus;

/**
 * OrderItem bean
 * 
 * @author Malik
 */
@XmlRootElement
public class OrderItemBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private MenuItemBean menuItem;
	private int quantity;
	private OrderItemStatus status;
	
	/**
	 * construct OrderItemBean
	 */
	public OrderItemBean() {
	}
	
	/**
	 * onstruct OrderItemBean
	 * @param menuItem
	 * @param quantity
	 * @param status
	 */
	public OrderItemBean(MenuItemBean menuItem, int quantity, OrderItemStatus status) {
		this.menuItem = menuItem;
		this.quantity = quantity;
		this.status = status;
	}
		
	/**
	 * return menuItem
	 * @return
	 */
	public MenuItemBean getMenuItem() {
		return menuItem;
	}

	/**
	 * set menuItem
	 * @param menuItem
	 */
	public void setMenuItem(MenuItemBean menuItem) {
		this.menuItem = menuItem;
	}
	
	/**
	 * return quantity
	 * @return
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * set quantity
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * return item status
	 * @return
	 */
	public OrderItemStatus getStatus() {
		return status;
	}

	/**
	 * set item status
	 * @param status
	 */
	public void setStatus(OrderItemStatus status) {
		this.status = status;
	}
}
