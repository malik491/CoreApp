/**
 * OrderItem bean
 */
package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.OrderItemStatus;

/**
 * @author Malik
 */
@XmlRootElement
public class OrderItemBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private MenuItemBean menuItem;
	private int quantity;
	private OrderItemStatus status;
	
	/**
	 * construct an empty order item
	 */
	public OrderItemBean() {
	}
	
	public OrderItemBean(MenuItemBean menuItem, int quantity, OrderItemStatus status) {
		this.menuItem = menuItem;
		this.quantity = quantity;
		this.status = status;
	}
		
	/**
	 * return the Menu Item associated with this Order Item
	 * @return
	 */
	public MenuItemBean getMenuItem() {
		return menuItem;
	}

	/**
	 * set the Menu Item associated with this Order Item
	 * @param menuItem
	 */
	public void setMenuItem(MenuItemBean menuItem) {
		this.menuItem = menuItem;
	}
	
	/**
	 * return quantity for the menu item
	 * @return
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * set the quantity for the menu item
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public OrderItemStatus getStatus() {
		return status;
	}

	public void setStatus(OrderItemStatus status) {
		this.status = status;
	}
}
