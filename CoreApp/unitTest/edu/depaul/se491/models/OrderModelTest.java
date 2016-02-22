package edu.depaul.se491.models;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

public class OrderModelTest {
	private static ConnectionFactory connFactory;
	private static TestDAOFactory daoFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		daoFactory= new TestDAOFactory(connFactory);
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release and close resources
		dbBuilder = null;
		testDataGen = null;
		daoFactory = null;
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
	public void testOrderModelDAOFactoryCredentialsBean() {
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		assertNotNull(model);
	}

	@Test
	public void testCreate() {
		OrderItemBean[] items = new OrderItemBean[] {new OrderItemBean(new MenuItemBean(1, null, null, 1.99, null), 1, OrderItemStatus.NOT_READY)};
		
		OrderBean order = new OrderBean();
		order.setType(OrderType.PICKUP);
		order.setPayment(new PaymentBean(0, 1.99, PaymentType.CASH, null, null));
		order.setOrderItems(items);

		// as a manager test creating order with order status:
		// canceled, prepared, submitted		
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);

		order.setStatus(OrderStatus.CANCELED);
		OrderBean createdOrder = model.create(order);
		assertNotNull(createdOrder);

		order.setStatus(OrderStatus.PREPARED);
		createdOrder = model.create(order);
		assertNotNull(createdOrder);
		
		order.setStatus(OrderStatus.SUBMITTED);
		createdOrder = model.create(order);
		assertNotNull(createdOrder);

		
		// as employee, test creating order with order status:
		// canceled, prepared, submitted
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		
		order.setStatus(OrderStatus.CANCELED);
		createdOrder = model.create(order);
		assertNull(createdOrder);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		order.setStatus(OrderStatus.PREPARED);
		createdOrder = model.create(order);
		assertNull(createdOrder);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		order.setStatus(OrderStatus.SUBMITTED);
		createdOrder = model.create(order);
		assertNotNull(createdOrder);
		
		// as customer, test creating order with order status:
		// canceled, prepared, submitted
		credentials = new CredentialsBean("customerapp", "password");
		model = new OrderModel(daoFactory, credentials);

		order.setStatus(OrderStatus.CANCELED);
		createdOrder = model.create(order);
		assertNull(createdOrder);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		order.setStatus(OrderStatus.PREPARED);
		createdOrder = model.create(order);
		assertNull(createdOrder);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		order.setStatus(OrderStatus.SUBMITTED);
		createdOrder = model.create(order);
		assertNotNull(createdOrder);
	}

	@Test
	public void testUpdate() {
		long orderId = 1;
		
		// as a manager test updating order to order status:
		// canceled, prepared, submitted
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		OrderBean oldOrder = model.read(orderId);
		assertNotNull(oldOrder);
		
		oldOrder.setStatus(OrderStatus.CANCELED);
		Boolean updated = model.update(oldOrder);
		assertNotNull(updated);
		assertTrue(updated);
						
		oldOrder.setStatus(OrderStatus.PREPARED);
		updated = model.update(oldOrder);
		assertNotNull(updated);
		assertTrue(updated);
		
		oldOrder.setStatus(OrderStatus.SUBMITTED);
		updated = model.update(oldOrder);
		assertNotNull(updated);
		assertTrue(updated);
		
		// as employee test updating order
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		
		
		oldOrder.setStatus(OrderStatus.CANCELED);
		updated = model.update(oldOrder);
		assertNull(updated);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		oldOrder.setStatus(OrderStatus.SUBMITTED);
		oldOrder.getOrderItems()[0].setStatus(OrderItemStatus.READY);
		oldOrder.getOrderItems()[1].setStatus(OrderItemStatus.READY);
		updated = model.update(oldOrder);
		assertNotNull(updated);
		assertTrue(updated);
		
		// all items are ready so order status should be prepared
		OrderBean updatedOrder = model.read(orderId);
		assertNotNull(updatedOrder);
		assertEquals(OrderStatus.PREPARED, updatedOrder.getStatus());
	}

	@Test
	public void testDelete() {
		long orderId = 1;
		
		// as a manager test updating order to order status:
		// canceled, prepared, submitted
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		Boolean deleted = model.delete(orderId);
		assertNotNull(deleted);
		assertTrue(deleted);
		
		orderId = 2;
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		deleted = model.delete(orderId);
		assertNull(deleted);		
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		credentials = new CredentialsBean("customerapp", "password");
		model = new OrderModel(daoFactory, credentials);
		deleted = model.delete(orderId);
		assertNull(deleted);		
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
	}

	@Test
	public void testReadLong() {
		long orderId = 1;
		
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		OrderBean order = model.read(orderId);
		assertNotNull(order);
		
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		order = model.read(orderId);
		assertNotNull(order);
		
		credentials = new CredentialsBean("customerapp", "password");
		model = new OrderModel(daoFactory, credentials);
		order = model.read(orderId);
		assertNull(order);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
	}

	@Test
	public void testReadString() {
		String confirmation = "order-confirmation-123";
		
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		OrderBean order = model.read(confirmation);
		assertNotNull(order);
		
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		order = model.read(confirmation);
		assertNotNull(order);
		
		credentials = new CredentialsBean("customerapp", "password");
		model = new OrderModel(daoFactory, credentials);
		order = model.read(confirmation);
		assertNotNull(order);
	}

	@Test
	public void testReadAllOrderStatus() {
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		List<OrderBean> submittedOrders = model.readAll(OrderStatus.SUBMITTED);
		List<OrderBean> preparedOrders = model.readAll(OrderStatus.PREPARED);
		List<OrderBean> canceledOrders = model.readAll(OrderStatus.CANCELED);
		assertNotNull(submittedOrders);
		assertNotNull(preparedOrders);
		assertNotNull(canceledOrders);
		
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		submittedOrders = model.readAll(OrderStatus.SUBMITTED);
		preparedOrders = model.readAll(OrderStatus.PREPARED);
		canceledOrders = model.readAll(OrderStatus.CANCELED);
		assertNotNull(submittedOrders);
		assertNotNull(preparedOrders);
		assertNotNull(canceledOrders);
		
		credentials = new CredentialsBean("customerapp", "password");
		model = new OrderModel(daoFactory, credentials);
		submittedOrders = model.readAll(OrderStatus.SUBMITTED);
		assertNull(submittedOrders);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		preparedOrders = model.readAll(OrderStatus.PREPARED);
		assertNull(preparedOrders);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
		canceledOrders = model.readAll(OrderStatus.CANCELED);
		assertNull(canceledOrders);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
	}

	@Test
	public void testReadAllOrderType() {
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		List<OrderBean> pickupOrders = model.readAll(OrderType.PICKUP);
		List<OrderBean> deliveryOrders = model.readAll(OrderType.DELIVERY);
		assertNotNull(pickupOrders);
		assertNotNull(deliveryOrders);
		
		
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		pickupOrders = model.readAll(OrderType.PICKUP);
		deliveryOrders = model.readAll(OrderType.DELIVERY);
		assertNotNull(pickupOrders);
		assertNotNull(deliveryOrders);
		
		credentials = new CredentialsBean("customerapp", "password");
		model = new OrderModel(daoFactory, credentials);
		pickupOrders = model.readAll(OrderType.PICKUP);
		assertNull(pickupOrders);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());

		deliveryOrders = model.readAll(OrderType.DELIVERY);
		assertNull(deliveryOrders);
		assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		
	}

}
