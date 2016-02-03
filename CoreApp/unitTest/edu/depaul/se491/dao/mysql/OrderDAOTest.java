package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.daos.mysql.OrderDAO;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

public class OrderDAOTest {
	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	private static OrderDAO orderDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		
		orderDAO = new TestDAOFactory(connFactory).getOrderDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// rebuild to original state
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release resources
		dbBuilder = null;
		testDataGen = null;
		orderDAO = null;
		
		// close connection data source (pool)
		connFactory.close();
	}

	@Before
	public void setUp() throws Exception {
		// rebuild the DB before each method
		dbBuilder.rebuildAll();
			
		// generate test data
		testDataGen.generateStandardData();
	}

	@Test
	public void testOrderDAO() {
		assertNotNull(orderDAO);
	}

	@Test
	public void testGetAll() {
		List<OrderBean> orders = null;
		try {
			orders = orderDAO.getAll();
			assertFalse(orders.isEmpty());

			for (OrderBean order: orders)
				assertNotNull(order);
			
		} catch (DBException e) {
			fail("orderDAO.getAll() threw DBException: " + e.getMessage());
		}
	}

	@Test
	public void testGetAllWithStatus() {
		List<OrderBean> orders = null;
		try {
			orders = orderDAO.getAllWithStatus(OrderStatus.SUBMITTED);
			assertFalse(orders.isEmpty());
			for (OrderBean order: orders)
				assertNotNull(order);

			orders = orderDAO.getAllWithStatus(OrderStatus.PREPARED);
			assertFalse(orders.isEmpty());
			for (OrderBean order: orders)
				assertNotNull(order);

			
			orders = orderDAO.getAllWithStatus(OrderStatus.CANCELED);
			assertFalse(orders.isEmpty());
			for (OrderBean order: orders)
				assertNotNull(order);

			
		} catch (DBException e) {
			fail("orderDAO.getAllWithStatus() threw DBException: " + e.getMessage());
		}
	}

	@Test
	public void testGetAllWithType() {
		try {
			List<OrderBean> orders = orderDAO.getAllWithType(OrderType.DELIVERY);
			assertFalse(orders.isEmpty());
			for (OrderBean order: orders)
				assertNotNull(order);

			orders = orderDAO.getAllWithType(OrderType.PICKUP);
			assertFalse(orders.isEmpty());
			for (OrderBean order: orders)
				assertNotNull(order);
		} catch (DBException e) {
			fail("orderDAO.getAllWithStatus() threw DBException: " + e.getMessage());
		}
	}

	@Test
	public void testGetLong() {
		try {
			final long orderId = 1;
			final long expectedPaymentId = 1;
			final int expectedOrderItemsCount = 2;
			final String confirmation = "order-confirmation-123";
			
			OrderBean order = orderDAO.get(orderId);
			
			assertNotNull(order);
			assertNotNull(order.getType());
			assertNotNull(order.getStatus());
			assertNotNull(order.getTimestamp());
			assertNotNull(order.getConfirmation());
			assertNotNull(order.getItems());
			assertNotNull(order.getPayment());
			assertNull(order.getPayment().getCreditCard());
			assertNull(order.getPayment().getTransactionConfirmation());
			assertNull(order.getAddress());
			
			
			assertEquals(orderId, order.getId());
			assertEquals(confirmation, order.getConfirmation());
			assertEquals(OrderType.PICKUP, order.getType());
			assertEquals(OrderStatus.SUBMITTED, order.getStatus());
			assertEquals(expectedOrderItemsCount, order.getItems().size());

			assertEquals(expectedPaymentId, order.getPayment().getId());
			assertEquals(PaymentType.CASH, order.getPayment().getType());
			
			
			
		} catch (DBException e) {
			fail("orderDAO.get(long orderId) threw DBException: " + e.getMessage());
		}
	}

	@Test
	public void testGetString() {
		try {
			final String confirmation = "order-confirmation-101";
			
			final long expectedOrderId = 4;
			final long expectedPaymentId = 2;
			final long expectedAddressId = 6;
			final int expectedOrderItemsCount = 4;
			final String expectedCcTransactionConf = "cc-transaction-confirmation-123";
			
			OrderBean order = orderDAO.get(confirmation);
			
			assertNotNull(order);
			assertNotNull(order.getType());
			assertNotNull(order.getStatus());
			assertNotNull(order.getTimestamp());
			assertNotNull(order.getConfirmation());
			assertNotNull(order.getItems());
			assertNotNull(order.getPayment());
			assertNotNull(order.getPayment().getTransactionConfirmation());
			assertNotNull(order.getAddress());
			assertNull(order.getPayment().getCreditCard());
			
			assertEquals(confirmation, order.getConfirmation());
			assertEquals(expectedOrderId, order.getId());
			assertEquals(OrderType.DELIVERY, order.getType());
			assertEquals(OrderStatus.SUBMITTED, order.getStatus());
			assertEquals(expectedOrderItemsCount, order.getItems().size());

			assertEquals(expectedPaymentId, order.getPayment().getId());
			assertEquals(PaymentType.CREDIT_CARD, order.getPayment().getType());
			assertEquals(expectedCcTransactionConf, order.getPayment().getTransactionConfirmation());
			
			assertEquals(expectedAddressId, order.getAddress().getId());
			
		} catch (DBException e) {
			fail("orderDAO.get(String confirmation) threw DBException: " + e.getMessage());
		}
	}

	@Test
	public void testAdd() {
		final long expectedOrderId = 5;
		final long expectedPaymentId = 3;
		final String confirmation = "order-confirmation-111";
		final double total = 1.99;
		
		// soda menu item
		MenuItemBean menuItem = new MenuItemBean();
		menuItem.setId(1);
		
		List<OrderItemBean> orderItems = new ArrayList<>();
		OrderItemBean orderItem = new OrderItemBean(menuItem, 1, OrderItemStatus.NOT_READY);
		orderItems.add(orderItem);
		
		final OrderBean order = new OrderBean();
		order.setType(OrderType.PICKUP);
		order.setStatus(OrderStatus.CANCELED);
		order.setTimestamp(new Timestamp(System.currentTimeMillis()));
		order.setConfirmation(confirmation);
		order.setPayment( new PaymentBean(0, total, PaymentType.CASH, null, null));
		order.setItems(orderItems);
		
		OrderBean addedOrder = null;
		try {
			addedOrder = orderDAO.add(order);
		} catch (DBException e) {
			fail("orderDAO.add(OrderBean order) threw DBException: " + e.getMessage());
		}
		
		assertNotNull(addedOrder);
		assertNotNull(addedOrder.getType());
		assertNotNull(addedOrder.getStatus());
		assertNotNull(addedOrder.getTimestamp());
		assertNotNull(addedOrder.getConfirmation());
		assertNotNull(addedOrder.getPayment());
		assertNotNull(addedOrder.getItems());
		assertNull(addedOrder.getAddress());

		assertEquals(expectedOrderId, addedOrder.getId());
		assertEquals(order.getType(), addedOrder.getType());
		assertEquals(order.getStatus(), addedOrder.getStatus());
		assertEquals(order.getTimestamp(), addedOrder.getTimestamp());
		assertEquals(order.getConfirmation(), addedOrder.getConfirmation());
		assertEquals(order.getPayment().getType(), addedOrder.getPayment().getType());
		assertEquals(order.getItems().size(), addedOrder.getItems().size());
		assertEquals(expectedPaymentId, addedOrder.getPayment().getId());
		assertTrue(Double.compare(order.getPayment().getTotal(), addedOrder.getPayment().getTotal()) == 0);
	}

	@Test
	public void testDelete() {
		try {
			final int orderCount = 4;
			final int expectedOrderCount = orderCount - 1;
			
			boolean deleted = orderDAO.delete(2);
			assertTrue(deleted);
			
			List<OrderBean> orders = orderDAO.getAll();
			assertNotNull(orders);
			assertEquals(expectedOrderCount, orders.size());
		} catch (DBException e) {
			fail("orderDAO.delete(long orderId) threw DBException: " + e.getMessage());
		}
		
	}

	@Test
	public void testUpdate() {
		try {
			 long pickupOrderId = 1;
			 
			 OrderBean oldPickupOrder = orderDAO.get(pickupOrderId);
			 assertNotNull(oldPickupOrder);
			 assertNotNull(oldPickupOrder.getType());
			 assertNotNull(oldPickupOrder.getStatus());
			 assertNotNull(oldPickupOrder.getTimestamp());
			 assertNotNull(oldPickupOrder.getConfirmation());
			 assertNotNull(oldPickupOrder.getItems());
			 assertNotNull(oldPickupOrder.getPayment());
			 assertNull(oldPickupOrder.getAddress());
			 assertEquals(OrderType.PICKUP, oldPickupOrder.getType());
			 
			 
			 
			 // update old pick order (everything except the payment & timestamp)
			 oldPickupOrder.setType(OrderType.DELIVERY);
			 oldPickupOrder.setStatus(OrderStatus.PREPARED);
			 oldPickupOrder.setConfirmation("new confirmation");
			 oldPickupOrder.setAddress(new AddressBean(0, "new address", null, "Chicago", AddressState.IL, "60601"));
			 
			 // update old pick order items
			 List<OrderItemBean> oldItems = oldPickupOrder.getItems();
			 oldItems.get(0).setQuantity(0);
			 oldItems.get(1).setQuantity(2);
			 
			
			 boolean updated = orderDAO.update(oldPickupOrder);
			 assertTrue(updated);
			 
			 OrderBean updatedOrder = orderDAO.get(pickupOrderId);
			 assertNotNull(updatedOrder);
			 assertNotNull(updatedOrder.getType());
			 assertNotNull(updatedOrder.getStatus());
			 assertNotNull(updatedOrder.getTimestamp());
			 assertNotNull(updatedOrder.getConfirmation());
			 assertNotNull(updatedOrder.getItems());
			 assertNotNull(updatedOrder.getPayment());
			 assertNotNull(updatedOrder.getAddress());
			 
			 assertEquals(oldPickupOrder.getId(), updatedOrder.getId());
			 assertEquals(oldPickupOrder.getType(), updatedOrder.getType());
			 assertEquals(oldPickupOrder.getStatus(), updatedOrder.getStatus());
			 assertEquals(oldPickupOrder.getConfirmation(), updatedOrder.getConfirmation());
			 assertEquals(oldPickupOrder.getPayment().getId(), updatedOrder.getPayment().getId());
			 assertEquals(oldPickupOrder.getPayment().getType(), updatedOrder.getPayment().getType());
			 assertEquals(oldPickupOrder.getItems().size() - 1, updatedOrder.getItems().size());
			 
			 long expectedAddressId = 7;
			 assertEquals(expectedAddressId, updatedOrder.getAddress().getId());
			 assertEquals(oldPickupOrder.getAddress().getLine1(), updatedOrder.getAddress().getLine1());
			 assertNull(updatedOrder.getAddress().getLine2());
			 assertEquals(oldPickupOrder.getAddress().getCity(), updatedOrder.getAddress().getCity());
			 assertEquals(oldPickupOrder.getAddress().getState(), updatedOrder.getAddress().getState());
			 assertEquals(oldPickupOrder.getAddress().getZipcode(), updatedOrder.getAddress().getZipcode());
			 
		 } catch (DBException e) {
			 fail("orderDAO.get(orderId) or orderDAO.update(order) threw DBException: " + e.getMessage()); 
		 }
		 
		 try {
			 long deliveryOrderId = 4;
			 
			 OrderBean oldDeliveryOrder = orderDAO.get(deliveryOrderId);
			 assertNotNull(oldDeliveryOrder);
			 assertNotNull(oldDeliveryOrder.getType());
			 assertNotNull(oldDeliveryOrder.getStatus());
			 assertNotNull(oldDeliveryOrder.getTimestamp());
			 assertNotNull(oldDeliveryOrder.getConfirmation());
			 assertNotNull(oldDeliveryOrder.getItems());
			 assertNotNull(oldDeliveryOrder.getPayment());
			 assertNotNull(oldDeliveryOrder.getAddress());
			 assertEquals(OrderType.DELIVERY, oldDeliveryOrder.getType());
			 
			 
			 // update type to pickup / removing old address
			 oldDeliveryOrder.setType(OrderType.PICKUP);
			 oldDeliveryOrder.setAddress(null);
			 
		 	 boolean updated = orderDAO.update(oldDeliveryOrder);
			 assertTrue(updated);
			 
			 OrderBean updatedOrder = orderDAO.get(deliveryOrderId);
			 assertNotNull(updatedOrder);
			 assertNotNull(updatedOrder.getType());
			 assertNull(updatedOrder.getAddress());
			 
			 assertEquals(oldDeliveryOrder.getId(), updatedOrder.getId());
			 assertEquals(oldDeliveryOrder.getType(), updatedOrder.getType());
			 
		 } catch (DBException e) {
			 fail("orderDAO.update(order) or orderDAO.get(orderId) threw DBException: " + e.getMessage());
		 }
		 
		 
	}
}
