package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Timestamp;
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
import edu.depaul.se491.daos.ExceptionConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.daos.mysql.OrderDAO;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;
import edu.depaul.se491.utils.ParamLengths;

/**
 * 
 * @author Malik
 *
 */
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
		testDataGen.generateData();

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
		testDataGen.generateData();
	}

	@Test
	public void testOrderDAO() {
		assertNotNull(orderDAO);
	}

	@Test
	public void testGetAll() throws SQLException {
		List<OrderBean> orders = null;
		orders = orderDAO.getAll();
		assertFalse(orders.isEmpty());

		for (OrderBean order : orders)
			assertNotNull(order);
	}

	@Test
	public void testGetAllWithStatus() throws SQLException {
		List<OrderBean> orders = orderDAO.getAllWithStatus(OrderStatus.SUBMITTED);
		assertFalse(orders.isEmpty());
		for (OrderBean order : orders)
			assertNotNull(order);

		orders = orderDAO.getAllWithStatus(OrderStatus.PREPARED);
		assertFalse(orders.isEmpty());
		for (OrderBean order : orders)
			assertNotNull(order);

		orders = orderDAO.getAllWithStatus(OrderStatus.CANCELED);
		assertFalse(orders.isEmpty());
		for (OrderBean order : orders)
			assertNotNull(order);
	}

	@Test
	public void testGetAllWithType() throws SQLException {
		List<OrderBean> orders = orderDAO.getAllWithType(OrderType.DELIVERY);
		assertFalse(orders.isEmpty());
		for (OrderBean order : orders)
			assertNotNull(order);

		orders = orderDAO.getAllWithType(OrderType.PICKUP);
		assertFalse(orders.isEmpty());
		for (OrderBean order : orders)
			assertNotNull(order);
	}

	@Test
	public void testGetByID() throws SQLException {
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
		assertNotNull(order.getOrderItems());
		assertNotNull(order.getPayment());
		assertNull(order.getPayment().getCreditCard());
		assertNull(order.getPayment().getTransactionConfirmation());
		assertNull(order.getAddress());

		assertEquals(orderId, order.getId());
		assertEquals(confirmation, order.getConfirmation());
		assertEquals(OrderType.PICKUP, order.getType());
		assertEquals(OrderStatus.SUBMITTED, order.getStatus());
		assertEquals(expectedOrderItemsCount, order.getOrderItems().length);

		assertEquals(expectedPaymentId, order.getPayment().getId());
		assertEquals(PaymentType.CASH, order.getPayment().getType());
		

		// no order with this id
		assertNull(orderDAO.get(100));
	}

	@Test
	public void testGetByConfirmation() throws SQLException {
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
		assertNotNull(order.getOrderItems());
		assertNotNull(order.getPayment());
		assertNotNull(order.getPayment().getTransactionConfirmation());
		assertNotNull(order.getAddress());
		assertNull(order.getPayment().getCreditCard());

		assertEquals(confirmation, order.getConfirmation());
		assertEquals(expectedOrderId, order.getId());
		assertEquals(OrderType.DELIVERY, order.getType());
		assertEquals(OrderStatus.SUBMITTED, order.getStatus());
		assertEquals(expectedOrderItemsCount, order.getOrderItems().length);

		assertEquals(expectedPaymentId, order.getPayment().getId());
		assertEquals(PaymentType.CREDIT_CARD, order.getPayment().getType());
		assertEquals(expectedCcTransactionConf, order.getPayment().getTransactionConfirmation());

		assertEquals(expectedAddressId, order.getAddress().getId());
		
		
		// no order with this confirmation
		assertNull(orderDAO.get("invalid confirmation"));
		
	}

	@Test
	public void testAdd() throws SQLException {
		final long expectedOrderId = 5;
		final long expectedPaymentId = 3;
		final long expectedAddressId = 7;
		final String confirmation = "order-confirmation-111";
		final double total = 1.99;

		// soda menu item
		MenuItemBean menuItem = new MenuItemBean();
		menuItem.setId(1);

		OrderItemBean[] orderItems = new OrderItemBean[] { new OrderItemBean(menuItem, 1, OrderItemStatus.NOT_READY) };

		final OrderBean order = new OrderBean();
		order.setType(OrderType.DELIVERY);
		order.setStatus(OrderStatus.CANCELED);
		order.setTimestamp(new Timestamp(System.currentTimeMillis()));
		order.setConfirmation(confirmation);
		order.setPayment(new PaymentBean(0, total, PaymentType.CASH, null, null));
		order.setOrderItems(orderItems);
		order.setAddress(new AddressBean(0L, "line 1", "line 2", "City", AddressState.ID, "56789"));
		
		OrderBean addedOrder = orderDAO.add(order);

		assertNotNull(addedOrder);
		assertNotNull(addedOrder.getType());
		assertNotNull(addedOrder.getStatus());
		assertNotNull(addedOrder.getTimestamp());
		assertNotNull(addedOrder.getConfirmation());
		assertNotNull(addedOrder.getPayment());
		assertNotNull(addedOrder.getOrderItems());
		assertNotNull(addedOrder.getAddress());

		assertEquals(expectedOrderId, addedOrder.getId());
		assertEquals(order.getType(), addedOrder.getType());
		assertEquals(order.getStatus(), addedOrder.getStatus());
		assertEquals(order.getTimestamp(), addedOrder.getTimestamp());
		assertEquals(order.getConfirmation(), addedOrder.getConfirmation());
		assertEquals(order.getPayment().getType(), addedOrder.getPayment().getType());
		assertEquals(order.getOrderItems().length, addedOrder.getOrderItems().length);
		
		assertEquals(expectedPaymentId, addedOrder.getPayment().getId());
		assertEquals(0, Double.compare(order.getPayment().getTotal(), addedOrder.getPayment().getTotal()));
		assertEquals(order.getPayment().getType(), addedOrder.getPayment().getType());
		assertNull(addedOrder.getPayment().getCreditCard());
		assertNull(addedOrder.getPayment().getTransactionConfirmation());
		
		assertEquals(expectedAddressId, addedOrder.getAddress().getId());
		assertEquals(order.getAddress().getLine1(), addedOrder.getAddress().getLine1());
		assertEquals(order.getAddress().getLine2(), addedOrder.getAddress().getLine2());
		assertEquals(order.getAddress().getCity(), addedOrder.getAddress().getCity());
		assertEquals(order.getAddress().getState(), addedOrder.getAddress().getState());
		assertEquals(order.getAddress().getZipcode(), addedOrder.getAddress().getZipcode());
		
	}

	@Test
	public void testDelete() throws SQLException {
		final int orderCount = 4;
		final int expectedOrderCount = orderCount - 1;

		boolean deleted = orderDAO.delete(2);
		assertTrue(deleted);

		List<OrderBean> orders = orderDAO.getAll();
		assertNotNull(orders);
		assertEquals(expectedOrderCount, orders.size());
		

		// no order with id 100
		deleted = orderDAO.delete(100);
		assertFalse(deleted);
	}

	@Test
	public void testUpdate() throws SQLException {
		long pickupOrderId = 1;

		OrderBean oldPickupOrder = orderDAO.get(pickupOrderId);
		assertNotNull(oldPickupOrder);
		assertNotNull(oldPickupOrder.getType());
		assertNotNull(oldPickupOrder.getStatus());
		assertNotNull(oldPickupOrder.getTimestamp());
		assertNotNull(oldPickupOrder.getConfirmation());
		assertNotNull(oldPickupOrder.getOrderItems());
		assertNotNull(oldPickupOrder.getPayment());
		assertNull(oldPickupOrder.getAddress());
		assertEquals(OrderType.PICKUP, oldPickupOrder.getType());

		// update old pick order (everything except the payment & timestamp)
		oldPickupOrder.setType(OrderType.DELIVERY);
		oldPickupOrder.setStatus(OrderStatus.PREPARED);
		oldPickupOrder.setConfirmation("new confirmation");
		oldPickupOrder.setAddress(new AddressBean(0, "new address", null, "Chicago", AddressState.IL, "60601"));

		// update old pick order items
		OrderItemBean[] oldItems = oldPickupOrder.getOrderItems();
		oldItems[0].setQuantity(0);
		oldItems[1].setQuantity(2);

		boolean updated = orderDAO.update(oldPickupOrder);
		assertTrue(updated);

		OrderBean updatedOrder = orderDAO.get(pickupOrderId);
		assertNotNull(updatedOrder);
		assertNotNull(updatedOrder.getType());
		assertNotNull(updatedOrder.getStatus());
		assertNotNull(updatedOrder.getTimestamp());
		assertNotNull(updatedOrder.getConfirmation());
		assertNotNull(updatedOrder.getOrderItems());
		assertNotNull(updatedOrder.getPayment());
		assertNotNull(updatedOrder.getAddress());

		assertEquals(oldPickupOrder.getId(), updatedOrder.getId());
		assertEquals(oldPickupOrder.getType(), updatedOrder.getType());
		assertEquals(oldPickupOrder.getStatus(), updatedOrder.getStatus());
		assertEquals(oldPickupOrder.getConfirmation(), updatedOrder.getConfirmation());
		assertEquals(oldPickupOrder.getPayment().getId(), updatedOrder.getPayment().getId());
		assertEquals(oldPickupOrder.getPayment().getType(), updatedOrder.getPayment().getType());
		assertEquals(oldPickupOrder.getOrderItems().length - 1, updatedOrder.getOrderItems().length);

		long expectedAddressId = 7;
		assertEquals(expectedAddressId, updatedOrder.getAddress().getId());
		assertEquals(oldPickupOrder.getAddress().getLine1(), updatedOrder.getAddress().getLine1());
		assertNull(updatedOrder.getAddress().getLine2());
		assertEquals(oldPickupOrder.getAddress().getCity(), updatedOrder.getAddress().getCity());
		assertEquals(oldPickupOrder.getAddress().getState(), updatedOrder.getAddress().getState());
		assertEquals(oldPickupOrder.getAddress().getZipcode(), updatedOrder.getAddress().getZipcode());

		
		
		
		
		
		long deliveryOrderId = 4;

		OrderBean oldDeliveryOrder = orderDAO.get(deliveryOrderId);
		assertNotNull(oldDeliveryOrder);
		assertNotNull(oldDeliveryOrder.getType());
		assertNotNull(oldDeliveryOrder.getStatus());
		assertNotNull(oldDeliveryOrder.getTimestamp());
		assertNotNull(oldDeliveryOrder.getConfirmation());
		assertNotNull(oldDeliveryOrder.getOrderItems());
		assertNotNull(oldDeliveryOrder.getPayment());
		assertNotNull(oldDeliveryOrder.getAddress());
		assertEquals(OrderType.DELIVERY, oldDeliveryOrder.getType());

		
		// update type to pickup / removing old address
		oldDeliveryOrder.setType(OrderType.PICKUP);
		oldDeliveryOrder.setAddress(null);

		updated = orderDAO.update(oldDeliveryOrder);
		assertTrue(updated);

		updatedOrder = orderDAO.get(deliveryOrderId);
		assertNotNull(updatedOrder);
		assertNotNull(updatedOrder.getType());
		assertNull(updatedOrder.getAddress());

		assertEquals(oldDeliveryOrder.getId(), updatedOrder.getId());
		assertEquals(oldDeliveryOrder.getType(), updatedOrder.getType());
		
		
		
		
	}

	@Test
	public void testExceptions() {
		OrderDAO dao = new TestDAOFactory(new ExceptionConnectionFactory()).getOrderDAO();

		try {
			dao.get(1L);
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.get("confirmation");
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.delete(1L);
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.add(new OrderBean());
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.update(new OrderBean());
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.getAllWithStatus(OrderStatus.SUBMITTED);
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.getAllWithType(OrderType.DELIVERY);
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.getAll();
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			OrderBean order = new OrderBean();
			order.setPayment(new PaymentBean(0L, 100000000.00, PaymentType.CASH, null, null));
			orderDAO.add(order);
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			OrderBean oldOrder = orderDAO.get(1L);
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < ParamLengths.Order.MAX_CONFIRMATION + 1; i++)
				sb.append("x");
			
			oldOrder.setConfirmation(sb.toString());
			
			orderDAO.update(oldOrder);
			
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		assertTrue(true);
	}
}
