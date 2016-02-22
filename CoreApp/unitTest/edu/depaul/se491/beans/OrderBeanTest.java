package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;

public class OrderBeanTest {
	OrderBean bean;
	
	@Before
	public void setUp() throws Exception {
		bean = new OrderBean();
	}

	@Test
	public void testOrderBean() {
		assertNotNull(bean);
		assertNull(bean.getConfirmation());
		assertNull(bean.getOrderItems());
		assertNull(bean.getPayment());
		assertNull(bean.getStatus());
		assertNull(bean.getTimestamp());
		assertNull(bean.getType());
		assertNull(bean.getAddress());
		assertEquals(0, bean.getId());
	}

	@Test
	public void testGetId() {
		assertEquals(0, bean.getId());
		
		long id = 1;
		bean.setId(id);
		assertEquals(id, bean.getId());
		
			}

	@Test
	public void testSetId() {
	
		long id = 1;
		bean.setId(id);
		assertEquals(id, bean.getId());
		
		id = 0;
		bean.setId(id);
		assertEquals(id, bean.getId());
		
		id = -1;
		bean.setId(id);
		assertEquals(id, bean.getId());
	}

	@Test
	public void testGetTimestamp() {
		assertNull(bean.getTimestamp());
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		bean.setTimestamp(time);
		assertNotNull(bean.getTimestamp());
		assertEquals(time, bean.getTimestamp());
		
	}

	@Test
	public void testSetTimestamp() {
		assertNull(bean.getTimestamp());
		
		Timestamp time = new Timestamp(System.currentTimeMillis());

		bean.setTimestamp(time);
		assertNotNull(bean.getTimestamp());

		time = null;
		bean.setTimestamp(time);
		assertNull(bean.getTimestamp());
		assertEquals(time, bean.getTimestamp());
	}

	@Test
	public void testGetConfirmation() {
		assertNull(bean.getConfirmation());
		
		String confirm = "confirmation";
		bean.setConfirmation(confirm);
		assertNotNull(bean.getConfirmation());
		assertEquals(confirm, bean.getConfirmation());
	}

	@Test
	public void testSetConfirmation() {
		String confirm = "confirmation";
		bean.setConfirmation(confirm);
		assertNotNull(bean.getConfirmation());
		assertEquals(confirm, bean.getConfirmation());
		
		confirm = "";
		bean.setConfirmation(confirm);
		assertTrue(bean.getConfirmation().isEmpty());
		assertEquals(confirm, bean.getConfirmation());
		
		confirm = null;
		bean.setConfirmation(confirm);
		assertNull(bean.getConfirmation());
		assertEquals(confirm, bean.getConfirmation());
	}

	@Test
	public void testGetType() {
		assertNull(bean.getType());
		
		OrderType type = OrderType.PICKUP;
		bean.setType(type);
		assertNotNull(bean.getType());
		assertEquals(type, bean.getType());
	}

	@Test
	public void testSetType() {
		OrderType type = OrderType.PICKUP;
		bean.setType(type);
		assertNotNull(bean.getType());
		assertEquals(type, bean.getType());
		
		type = OrderType.DELIVERY;
		bean.setType(type);
		assertNotNull(bean.getType());
		assertEquals(type, bean.getType());
	}

	@Test
	public void testGetStatus() {
		assertNull(bean.getStatus());
		
		OrderStatus status = OrderStatus.SUBMITTED;
		bean.setStatus(status);
		assertNotNull(bean.getStatus());
		assertEquals(status, bean.getStatus());
	}

	@Test
	public void testSetStatus() {
		OrderStatus status = OrderStatus.SUBMITTED;
		bean.setStatus(status);
		assertNotNull(bean.getStatus());
		assertEquals(status, bean.getStatus());
		
		status = OrderStatus.PREPARED;
		bean.setStatus(status);
		assertNotNull(bean.getStatus());
		assertEquals(status, bean.getStatus());
		
		status = OrderStatus.CANCELED;
		bean.setStatus(status);
		assertNotNull(bean.getStatus());
		assertEquals(status, bean.getStatus());
		
		status = null;
		bean.setStatus(status);
		assertNull(bean.getStatus());
		assertEquals(status, bean.getStatus());
	}

	@Test
	public void testGetPayment() {
		assertNull(bean.getPayment());
		
		PaymentBean payment = new PaymentBean();
		bean.setPayment(payment);
		assertNotNull(bean.getPayment());
		assertEquals(payment, bean.getPayment());
	}

	@Test
	public void testSetPayment() {
		PaymentBean payment = new PaymentBean();
		bean.setPayment(payment);
		assertNotNull(bean.getPayment());
		assertEquals(payment, bean.getPayment());
		
		payment = null;
		bean.setPayment(payment);
		assertNull(bean.getPayment());
		assertEquals(payment, bean.getPayment());
	}

	@Test
	public void testGetItems() {
		assertNull(bean.getOrderItems());
		
		OrderItemBean[] items = new OrderItemBean[1];
		bean.setOrderItems(items);
		assertNotNull(bean.getOrderItems());
		assertEquals(items.length, bean.getOrderItems().length);
	}

	@Test
	public void testSetItems() {
		OrderItemBean[] items = new OrderItemBean[1];
		bean.setOrderItems(items);
		assertNotNull(bean.getOrderItems());
		assertEquals(items.length, bean.getOrderItems().length);
	}

	@Test
	public void testGetAddress() {
		assertNull(bean.getAddress());
		
		AddressBean address = new AddressBean();
		bean.setAddress(address);
		assertNotNull(bean.getAddress());
		assertEquals(address, bean.getAddress());
	}

	@Test
	public void testSetAddress() {
		AddressBean address = new AddressBean();
		bean.setAddress(address);
		assertNotNull(bean.getAddress());
		assertEquals(address, bean.getAddress());
		
		address = null;
		bean.setAddress(address);
		assertNull(bean.getAddress());
		assertEquals(address, bean.getAddress());
	}

}
