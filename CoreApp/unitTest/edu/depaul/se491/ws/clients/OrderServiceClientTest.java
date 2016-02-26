package edu.depaul.se491.ws.clients;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * NOTE (Tomcat must be running for this to pass)
 * @author Malik
 *
 */
public class OrderServiceClientTest {
	private String serviceBaseURL = "http://localhost/CoreApp/order";

	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		dbBuilder.rebuildAll();
		testDataGen.generateData();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbBuilder.rebuildAll();
		testDataGen.generateData();
		
		// release and close resources
		dbBuilder = null;
		testDataGen = null;
	
		// close connection data source (pool)
		connFactory.close();
	}
	@Test
	public void testOrderServiceClient() {
		assertNotNull(new OrderServiceClient(null, serviceBaseURL));
	}

	@Test
	public void testGetById() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.get(1));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.get(1));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNotNull(serviceClient.get(1));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetByConfirmation() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.get(""));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.get(""));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNotNull(serviceClient.get("order-confirmation-123"));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testPost() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.post(null));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.post(new OrderBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		OrderBean order = new OrderBean();
		order.setStatus(OrderStatus.SUBMITTED);
		order.setType(OrderType.PICKUP);
		order.setPayment(new PaymentBean(0, 1.99, PaymentType.CASH, null, null));
		order.setOrderItems(new OrderItemBean[] {new OrderItemBean(new MenuItemBean(1, null, null, 1.99, null), 1, OrderItemStatus.NOT_READY)});
		
		serviceClient = new OrderServiceClient(new CredentialsBean("employee1", "password"), serviceBaseURL);
		assertNotNull(serviceClient.post(order));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testUpdate() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.update(null));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.update(new OrderBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		OrderBean order = serviceClient.get(3);
		assertNotNull(order);
		assertNotNull(order.getOrderItems());
		assertEquals(2, order.getOrderItems().length);
		
		order.setStatus(OrderStatus.CANCELED);
		order.setType(OrderType.DELIVERY);
		order.setAddress(new AddressBean(0, "line 1", "line 2", "city", AddressState.MN, "123456"));
		order.getOrderItems()[0].setQuantity(0);
		order.getOrderItems()[1].setStatus(OrderItemStatus.READY);
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNotNull(serviceClient.update(order));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testMainStationUpdate() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.mainStationUpdate(null));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.mainStationUpdate(new OrderBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		OrderBean order = serviceClient.get(4);
		assertNotNull(order);
		assertNotNull(order.getOrderItems());
		for (OrderItemBean item: order.getOrderItems())
			item.setStatus(OrderItemStatus.READY);
		
		OrderBean canceledOrder = serviceClient.get(3);
		assertNotNull(canceledOrder);
		assertEquals(OrderStatus.CANCELED, canceledOrder.getStatus());
		assertNotNull(canceledOrder.getOrderItems());
		for (OrderItemBean item: canceledOrder.getOrderItems())
			item.setStatus(OrderItemStatus.READY);
		
		
		serviceClient = new OrderServiceClient(new CredentialsBean("employee1", "password"), serviceBaseURL);
		Boolean updated = serviceClient.mainStationUpdate(order);
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());

		updated = serviceClient.mainStationUpdate(canceledOrder);
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testSideStationUpdate() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.sideStationUpdate(null));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.sideStationUpdate(new OrderBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		OrderBean order = serviceClient.get(4);
		assertNotNull(order);
		assertNotNull(order.getOrderItems());
		for (OrderItemBean item: order.getOrderItems())
			item.setStatus(OrderItemStatus.READY);
		
		OrderBean canceledOrder = serviceClient.get(3);
		assertNotNull(canceledOrder);
		assertEquals(OrderStatus.CANCELED, canceledOrder.getStatus());
		assertNotNull(canceledOrder.getOrderItems());
		for (OrderItemBean item: canceledOrder.getOrderItems())
			item.setStatus(OrderItemStatus.READY);
		
		
		serviceClient = new OrderServiceClient(new CredentialsBean("employee1", "password"), serviceBaseURL);
		Boolean updated = serviceClient.sideStationUpdate(order);
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());

		updated = serviceClient.sideStationUpdate(canceledOrder);
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testBeverageStationUpdate() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.beverageStationUpdate(null));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.beverageStationUpdate(new OrderBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		OrderBean order = serviceClient.get(4);
		assertNotNull(order);
		assertNotNull(order.getOrderItems());
		for (OrderItemBean item: order.getOrderItems())
			item.setStatus(OrderItemStatus.READY);
		
		OrderBean canceledOrder = serviceClient.get(3);
		assertNotNull(canceledOrder);
		assertEquals(OrderStatus.CANCELED, canceledOrder.getStatus());
		assertNotNull(canceledOrder.getOrderItems());
		
		for (OrderItemBean item: canceledOrder.getOrderItems())
			item.setStatus(OrderItemStatus.READY);
		
		
		serviceClient = new OrderServiceClient(new CredentialsBean("employee1", "password"), serviceBaseURL);
		Boolean updated = serviceClient.beverageStationUpdate(order);
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());

		updated = serviceClient.beverageStationUpdate(canceledOrder);
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testDelete() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.delete(1));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.delete(1));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		Boolean deleted = serviceClient.delete(1);
		assertNotNull(deleted);
		assertTrue(deleted);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetAll() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.getAll());
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.getAll());
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		OrderBean[] orders = serviceClient.getAll();
		assertNotNull(orders);
		assertTrue(orders.length >= 3);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetAllWithStatus() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.getAllWithStatus(OrderStatus.CANCELED));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.getAllWithStatus(OrderStatus.CANCELED));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		OrderBean[] orders = serviceClient.getAllWithStatus(OrderStatus.CANCELED);
		assertNotNull(orders);
		assertTrue(orders.length == 1);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetAllWithType() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.getAllWithType(OrderType.PICKUP));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean(), serviceBaseURL);
		assertNull(serviceClient.getAllWithType(OrderType.PICKUP));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new OrderServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		OrderBean[] orders = serviceClient.getAllWithType(OrderType.DELIVERY);
		assertNotNull(orders);
		assertTrue(orders.length >= 1);
		assertNull(serviceClient.getResponseMessage());
	}
	
	@Test
	public void testExceptions() {
		OrderServiceClient serviceClient = new OrderServiceClient(null, "wrongURL");
		assertNull(serviceClient.get(0));
		assertNull(serviceClient.get(null));
		assertNull(serviceClient.delete(0));
		assertNull(serviceClient.post(null));
		assertNull(serviceClient.update(null));
		assertNull(serviceClient.mainStationUpdate(null));
		assertNull(serviceClient.sideStationUpdate(null));
		assertNull(serviceClient.beverageStationUpdate(null));
		assertNull(serviceClient.getAll());
		assertNull(serviceClient.getAllWithStatus(null));
		assertNull(serviceClient.getAllWithType(null));
	}

}
