package edu.depaul.se491.models;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.CreditCardBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.enums.MenuItemCategory;
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
		daoFactory = new TestDAOFactory(connFactory);
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
	public void testOrderModel() {
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		assertNotNull(model);
	}

	@Test
	public void testCreate() {
		OrderItemBean[] items = new OrderItemBean[] {new OrderItemBean(new MenuItemBean(1, null, null, 1.99, null), 1, OrderItemStatus.NOT_READY)};
		
		OrderBean order = new OrderBean();
		order.setType(OrderType.DELIVERY);
		order.setPayment(new PaymentBean(0, 1.99, PaymentType.CREDIT_CARD, new CreditCardBean("123456789012", "my name", 5, 2016), null));
		order.setOrderItems(items);
		order.setAddress(new AddressBean(0L, "line 1", "line2", "Chicago", AddressState.IL, "54321"));
		
		// manager role
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		for (OrderStatus status : OrderStatus.values()) {
			order.setStatus(status);
			OrderBean createdOrder = model.create(order);
			assertNotNull(createdOrder);
		}

		// admin role
		credentials = new CredentialsBean("admin", "password");
		model = new OrderModel(daoFactory, credentials);
		
		for (OrderStatus status : OrderStatus.values()) {
			order.setStatus(status);
			OrderBean createdOrder = model.create(order);
			assertNull(createdOrder);
			assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		}
		
		// employee & customerApp roles
		for (String username : new String[]{"employee1", "customerapp"}) {
			credentials = new CredentialsBean(username, "password");
			model = new OrderModel(daoFactory, credentials);
			
			for (OrderStatus status : OrderStatus.values()) {
				order.setStatus(status);
				OrderBean createdOrder = model.create(order);
				if (status == OrderStatus.SUBMITTED) {
					assertNotNull(createdOrder);					
				} else {
					assertNull(createdOrder);
					assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());		
				}
			}
		}		
		
		// test creating invalid order
		order.setType(null);
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			credentials = new CredentialsBean(username, "password");
			model = new OrderModel(daoFactory, credentials);
			OrderBean createdOrder = model.create(order);
			assertNull(createdOrder);
			assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
		}
	}

	@Test
	public void testUpdate() {
		long orderId = 1;
		
		// manager role
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		
		OrderBean oldOrder = model.read(orderId);
		assertNotNull(oldOrder);
		oldOrder.setAddress(null);
		oldOrder.setType(OrderType.PICKUP);
		
		Boolean updated = null;
		
		for (OrderStatus status : OrderStatus.values()) {
			oldOrder.setStatus(status);
			updated = model.update(oldOrder);
			assertNotNull(updated);
			assertTrue(updated);
		}
		
		
		// invalid order (null status)
		oldOrder.setStatus(null);
		updated = model.update(oldOrder);
		assertNull(updated);
		assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());

		oldOrder.setStatus(OrderStatus.SUBMITTED);

		// invalid order items (all have zero quantity)
		for (OrderItemBean item : oldOrder.getOrderItems()) {
			item.setQuantity(0);
		}
		updated = model.update(oldOrder);
		assertNull(updated);
		assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());

		
		// test all other role
		for (String username : new String[]{"admin", "employee1", "customerapp"}) {
			credentials = new CredentialsBean(username, "password");
			model = new OrderModel(daoFactory, credentials);
			updated = model.update(oldOrder);
			assertNull(updated);
			assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		}
	}
	
	@Test
	public void testEmployeeUpdate() {
		long orderId = 4;
		
		// manager role to read order
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		OrderModel model = new OrderModel(daoFactory, credentials);
		OrderBean oldOrder = model.read(orderId);

		assertNotNull(oldOrder);
		OrderItemBean[] orderItems = oldOrder.getOrderItems();
		assertNotNull(orderItems);
		assertEquals(4, orderItems.length);
		
		
		// test updating order items from different stations
		credentials = new CredentialsBean("employee1", "password");
		model = new OrderModel(daoFactory, credentials);
		Boolean updated = null;
		
		for (MenuItemCategory currentFoodStation : MenuItemCategory.values()) {
			OrderItemBean orderItem = null;

			// find order item for the current station
			for (OrderItemBean item : orderItems) {
				if (item.getMenuItem().getItemCategory() == currentFoodStation) {
					orderItem = item;
					break;
				}
			}
			
			assertNotNull(orderItem);
			
			// test updating order item status (ready and not_ready)
			for (OrderItemStatus status : OrderItemStatus.values()) {
				orderItem.setStatus(status);
				updated = model.update(oldOrder, currentFoodStation);
				assertNotNull(updated);
				assertTrue(updated);
			}
		}

		// test updating with invalid order item status
		OrderItemBean item = oldOrder.getOrderItems()[0];
		item.setStatus(null);
		updated = model.update(oldOrder, item.getMenuItem().getItemCategory());
		assertNull(updated);
		assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
		
		
		// test updating with invalid station
		item.setStatus(OrderItemStatus.READY);
		updated = model.update(oldOrder, null);
		assertNull(updated);
		assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
		
		// test all other role
		item.setStatus(OrderItemStatus.READY);
		for (String username : new String[]{"admin", "manager", "customerapp"}) {
			credentials = new CredentialsBean(username, "password");
			model = new OrderModel(daoFactory, credentials);
			updated = model.update(oldOrder, MenuItemCategory.MAIN);
			assertNull(updated);
			assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
		}
	}
	

	@Test
	public void testDelete() {
		long orderId = 1;
		
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			OrderModel model = new OrderModel(daoFactory, credentials);

			Boolean deleted = model.delete(orderId);
			
			// manager role
			if (username.equals("manager")) {
				assertNotNull(deleted);
				assertTrue(deleted);
				// next order
				orderId = 2;
			} else {
				assertNull(deleted);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());	
			}
		}
	}

	@Test
	public void testReadWithOrderId() {
		long orderId = 1;
		
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			OrderModel model = new OrderModel(daoFactory, credentials);
			OrderBean order = model.read(orderId);
			
			if (username.equals("manager") || username.equals("employee1")) {
				assertNotNull(order);
				
				// test invalid id
				assertNull(model.read(0L));
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());	

				// test valid id but no order found
				assertNull(model.read(100L));
				assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());	

			} else {
				assertNull(order);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());	
			}
		}
	}

	@Test
	public void testReadWithOrderConfirmation() {
		String confirmation = "order-confirmation-123";
		
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			OrderModel model = new OrderModel(daoFactory, credentials);
			OrderBean order = model.read(confirmation);
			
			if (username.equals("manager") || username.equals("customerapp")) {
				assertNotNull(order);
				
				// test invalid confirmation (empty string)
				assertNull(model.read(""));
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());	

				// test valid confirmation but no order is found
				assertNull(model.read("confirmation 000"));
				assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());	
				
			} else {
				assertNull(order);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());	
			}
		}
	}

	@Test
	public void testReadAllOrdersByStatus() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			OrderModel model = new OrderModel(daoFactory, credentials);
			
			for (OrderStatus status : OrderStatus.values()) {
				List<OrderBean> orders = model.readAll(status);
				if (username.equals("manager") || (username.equals("employee1") && status == OrderStatus.SUBMITTED)) {
					assertNotNull(orders);
				} else {
					assertNull(orders);
					assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());	
				}
			}
		}
	}

	@Test
	public void testReadAllOrderByType() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			OrderModel model = new OrderModel(daoFactory, credentials);
			
			for (OrderType type : OrderType.values()) {
				List<OrderBean> orders = model.readAll(type);
				if (username.equals("manager")) {
					assertNotNull(orders);
				} else {
					assertNull(orders);
					assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());	
				}
			}
		}
	}
	
	@Test
	public void testReadAll() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			OrderModel model = new OrderModel(daoFactory, credentials);
			
			for (OrderType type : OrderType.values()) {
				List<OrderBean> orders = model.readAll(type);
				if (username.equals("manager")) {
					assertNotNull(orders);
				} else {
					assertNull(orders);
					assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());	
				}
			}
		}
	}
}
