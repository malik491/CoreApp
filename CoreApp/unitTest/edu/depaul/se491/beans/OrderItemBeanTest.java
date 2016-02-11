package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.enums.OrderItemStatus;

public class OrderItemBeanTest {
	OrderItemBean bean;

	@Before
	public void setUp() throws Exception {
		bean = new OrderItemBean();
				
	}

	@Test
	public void testOrderItemBean() {
		assertNotNull(bean);
		assertNull(bean.getMenuItem());
		assertNull(bean.getStatus());
		assertEquals(0, bean.getQuantity());
	}

	@Test
	public void testOrderItemBeanMenuItemBeanIntOrderItemStatus() {
		MenuItemBean menuItem = new MenuItemBean();
		int quantity = 1;
		OrderItemStatus orderstatus = OrderItemStatus.NOT_READY;
		OrderItemBean orderItem = new OrderItemBean(menuItem, quantity, orderstatus);
		assertNotNull(orderItem.getMenuItem());
		assertEquals(menuItem, orderItem.getMenuItem());
		assertEquals(quantity, orderItem.getQuantity());
		assertNotNull(orderItem.getStatus());
		assertEquals(orderstatus, orderItem.getStatus());
	}

	@Test
	public void testGetMenuItem() {
		assertNull(bean.getMenuItem());
		
		MenuItemBean menuItem = new MenuItemBean();
		bean.setMenuItem(menuItem);
		assertNotNull(bean.getMenuItem());
		assertEquals(menuItem, bean.getMenuItem());
	}

	@Test
	public void testSetMenuItem() {
		MenuItemBean menuItem = new MenuItemBean();
		bean.setMenuItem(menuItem);
		assertNotNull(bean.getMenuItem());
		assertEquals(menuItem, bean.getMenuItem());
		
		menuItem = null;
		bean.setMenuItem(menuItem);
		assertNull(bean.getMenuItem());
		assertEquals(menuItem, bean.getMenuItem());
	}

	@Test
	public void testGetQuantity() {
		assertEquals(0, bean.getQuantity());
		
		int quantity = 1;
		bean.setQuantity(quantity);
		assertEquals(quantity, bean.getQuantity());
	}

	@Test
	public void testSetQuantity() {
		int quantity = 1;
		bean.setQuantity(quantity);
		assertEquals(quantity, bean.getQuantity());
		
		quantity = 0;
		bean.setQuantity(quantity);
		assertEquals(quantity, bean.getQuantity());
		
		quantity = -1;
		bean.setQuantity(quantity);
		assertEquals(quantity, bean.getQuantity());
	}

	@Test
	public void testGetStatus() {
		assertNull(bean.getStatus());
		
		OrderItemStatus orderstatus = OrderItemStatus.NOT_READY;
		bean.setStatus(orderstatus);;
		assertNotNull(bean.getStatus());
		assertEquals(orderstatus, bean.getStatus());
	}

	@Test
	public void testSetStatus() {
		OrderItemStatus orderstatus = OrderItemStatus.NOT_READY;
		bean.setStatus(orderstatus);;
		assertNotNull(bean.getStatus());
		assertEquals(orderstatus, bean.getStatus());
		
		orderstatus = OrderItemStatus.READY;
		bean.setStatus(orderstatus);;
		assertNotNull(bean.getStatus());
		assertEquals(orderstatus, bean.getStatus());
	}

}
