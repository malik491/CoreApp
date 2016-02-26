package edu.depaul.se491.ws;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import edu.depaul.se491.beans.RequestBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * 
 * @author Malik
 */
public class OrderServiceTest {
	private static ConnectionFactory connFactory;
	private static TestDAOFactory daoFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	private OrderService service;
	
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
		testDataGen.generateData();
		
		// release and close resources
		dbBuilder = null;
		testDataGen = null;
		daoFactory = null;
		// close connection data source (pool)
		connFactory.close();
		
	}

	@Before
	public void setUp() throws Exception {
		// create menu service 
		service = new OrderService(daoFactory);
		 
		// rebuild the DB before each method
		dbBuilder.rebuildAll();
			
		// generate test data
		testDataGen.generateData();
	}
	
	@Test
	public void testGetById() {
		// invalid request
		Response response = service.getById(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getById(new RequestBean<Long>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getById(new RequestBean<Long>(null, new Long(1)));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.getById(new RequestBean<Long>(new CredentialsBean(), new Long(1)));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.getById(new RequestBean<Long>(new CredentialsBean("employee1", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request, invalid id
		response = service.getById(new RequestBean<Long>(new CredentialsBean("employee1", "password"), new Long(0)));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		// valid request
		response = service.getById(new RequestBean<Long>(new CredentialsBean("employee1", "password"), new Long(1)));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		OrderBean order = (OrderBean) response.getEntity();
		assertNotNull(order);
	}

	@Test
	public void testGetByConfirmation() {
		// invalid request
		Response response = service.getByConfirmation(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getByConfirmation(new RequestBean<String>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getByConfirmation(new RequestBean<String>(null, new String()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.getByConfirmation(new RequestBean<String>(new CredentialsBean(), new String()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.getByConfirmation(new RequestBean<String>(new CredentialsBean("customerapp", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request, invalid confirmation
		response = service.getByConfirmation(new RequestBean<String>(new CredentialsBean("customerapp", "password"), ""));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request, not found
		response = service.getByConfirmation(new RequestBean<String>(new CredentialsBean("customerapp", "password"), "random"));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

		
		// valid request
		response = service.getByConfirmation(new RequestBean<String>(new CredentialsBean("customerapp", "password"), "order-confirmation-123"));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		OrderBean order = (OrderBean) response.getEntity();
		assertNotNull(order);
	}

	@Test
	public void testPost() {
		// invalid request
		Response response = service.post(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.post(new RequestBean<OrderBean>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.post(new RequestBean<OrderBean>(null, new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.post(new RequestBean<OrderBean>(new CredentialsBean(), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.post(new RequestBean<OrderBean>(new CredentialsBean("customerapp", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request. bad order data
		response = service.post(new RequestBean<OrderBean>(new CredentialsBean("customerapp", "password"), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		OrderBean order = new OrderBean();
		order.setStatus(OrderStatus.SUBMITTED);
		order.setType(OrderType.PICKUP);
		order.setPayment(new PaymentBean(0, 1.99, PaymentType.CASH, null, null));
		order.setOrderItems(new OrderItemBean[] {new OrderItemBean(new MenuItemBean(1, null, null, 1.99, null), 1, OrderItemStatus.NOT_READY)});
		
		// valid request
		response = service.post(new RequestBean<OrderBean>(new CredentialsBean("customerapp", "password"), order));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		OrderBean createdOrder = (OrderBean) response.getEntity();
		assertNotNull(createdOrder);
		long expectedId = 5;
		assertEquals(expectedId, createdOrder.getId());
	}

	@Test
	public void testUpdate() {
		// invalid request
		Response response = service.update(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.update(new RequestBean<OrderBean>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.update(new RequestBean<OrderBean>(null, new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.update(new RequestBean<OrderBean>(new CredentialsBean(), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.update(new RequestBean<OrderBean>(new CredentialsBean("manager", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		OrderBean oldOrder = (OrderBean) (service.getById(new RequestBean<Long>(new CredentialsBean("manager", "password"), new Long(4L)))).getEntity();
		assertNotNull(oldOrder);
		oldOrder.setStatus(OrderStatus.PREPARED);
		oldOrder.getAddress().setLine2(null);
		oldOrder.getOrderItems()[1].setQuantity(0);
		
		// valid request
		response = service.update(new RequestBean<OrderBean>(new CredentialsBean("manager", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		Boolean updated = (Boolean) response.getEntity();
		assertNotNull(updated);
		assertTrue(updated);
		
		
		// valid request, invalid id
		oldOrder.setId(10);
		response = service.update(new RequestBean<OrderBean>(new CredentialsBean("manager", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		
	}

	@Test
	public void testMainStationUpdate() {
		// invalid request
		Response response = service.mainStationUpdate(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.mainStationUpdate(new RequestBean<OrderBean>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.mainStationUpdate(new RequestBean<OrderBean>(null, new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.mainStationUpdate(new RequestBean<OrderBean>(new CredentialsBean(), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.mainStationUpdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// valid request, bad order data
		response = service.mainStationUpdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
				
		
		OrderBean oldOrder = (OrderBean) (service.getById(new RequestBean<Long>(new CredentialsBean("employee1", "password"), new Long(4L)))).getEntity();
		assertNotNull(oldOrder);
		for (OrderItemBean item : oldOrder.getOrderItems()) {
			item.setStatus(OrderItemStatus.READY);
		}
		
		// valid request
		response = service.mainStationUpdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		Boolean updated = (Boolean) response.getEntity();
		assertNotNull(updated);
		assertTrue(updated);
		
		// valid request, invalid id
		oldOrder.setId(10);
		response = service.mainStationUpdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void testSideStationupdate() {
		// invalid request
		Response response = service.sideStationupdate(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.sideStationupdate(new RequestBean<OrderBean>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.sideStationupdate(new RequestBean<OrderBean>(null, new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.sideStationupdate(new RequestBean<OrderBean>(new CredentialsBean(), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.sideStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request, bad order data
		response = service.sideStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		OrderBean oldOrder = (OrderBean) (service.getById(new RequestBean<Long>(new CredentialsBean("employee1", "password"), new Long(4L)))).getEntity();
		assertNotNull(oldOrder);
		for (OrderItemBean item : oldOrder.getOrderItems()) {
			item.setStatus(OrderItemStatus.READY);
		}
		
		// valid request
		response = service.sideStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		Boolean updated = (Boolean) response.getEntity();
		assertNotNull(updated);
		assertTrue(updated);
		
		// valid request, invalid id
		oldOrder.setId(10);
		response = service.sideStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void testBeverageStationupdate() {
		// invalid request
		Response response = service.beverageStationupdate(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.beverageStationupdate(new RequestBean<OrderBean>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.beverageStationupdate(new RequestBean<OrderBean>(null, new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.beverageStationupdate(new RequestBean<OrderBean>(new CredentialsBean(), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.beverageStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		// valid request, bad order data
		response = service.beverageStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), new OrderBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		
		OrderBean oldOrder = (OrderBean) (service.getById(new RequestBean<Long>(new CredentialsBean("employee1", "password"), new Long(3L)))).getEntity();
		assertNotNull(oldOrder);
		for (OrderItemBean item : oldOrder.getOrderItems()) {
			item.setStatus(OrderItemStatus.READY);
		}
		
		// valid request
		response = service.beverageStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		Boolean updated = (Boolean) response.getEntity();
		assertNotNull(updated);
		assertTrue(updated);
		
		
		// valid request, invalid id
		oldOrder.setId(10);
		response = service.beverageStationupdate(new RequestBean<OrderBean>(new CredentialsBean("employee1", "password"), oldOrder));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}

	@Test
	public void testDelete() {
		// invalid request
		Response response = service.delete(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.delete(new RequestBean<Long>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.delete(new RequestBean<Long>(null, new Long(1L)));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.delete(new RequestBean<Long>(new CredentialsBean(), new Long(1L)));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.delete(new RequestBean<Long>(new CredentialsBean("manager", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request, invalid id
		response = service.delete(new RequestBean<Long>(new CredentialsBean("manager", "password"), new Long(0)));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		// valid request
		response = service.delete(new RequestBean<Long>(new CredentialsBean("manager", "password"), new Long(3L)));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		Boolean deleted = (Boolean) response.getEntity();
		assertNotNull(deleted);
		assertTrue(deleted);
		
		
		// valid request, no order with id
		response = service.delete(new RequestBean<Long>(new CredentialsBean("manager", "password"), new Long(10)));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		deleted = (Boolean) response.getEntity();
		assertNotNull(deleted);
		assertFalse(deleted);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAll() {
		// invalid request
		Response response = service.getAll(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getAll(new RequestBean<Object>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getAll(new RequestBean<Object>(new CredentialsBean(), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request but has no permission
		response = service.getAll(new RequestBean<Object>(new CredentialsBean("admin", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
				

		// valid request
		response = service.getAll(new RequestBean<Object>(new CredentialsBean("manager", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		List<OrderBean> orders = (ArrayList<OrderBean>) response.getEntity();
		assertNotNull(orders);
		assertEquals(4, orders.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllByType() {
		// invalid request
		Response response = service.getAllByType(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getAllByType(new RequestBean<OrderType>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getAllByType(new RequestBean<OrderType>(null, OrderType.PICKUP));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		
		// invalid request
		response = service.getAllByType(new RequestBean<OrderType>(new CredentialsBean(), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request but has no permission
		response = service.getAllByType(new RequestBean<OrderType>(new CredentialsBean("admin", "password"),  OrderType.PICKUP));
		assertNotNull(response);
		assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
		
		
		// valid request
		response = service.getAllByType(new RequestBean<OrderType>(new CredentialsBean("manager", "password"), OrderType.PICKUP));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		List<OrderBean> orders = (ArrayList<OrderBean>) response.getEntity();
		assertNotNull(orders);
		assertEquals(3, orders.size());
		
		// valid request
		response = service.getAllByType(new RequestBean<OrderType>(new CredentialsBean("manager", "password"), OrderType.DELIVERY));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		orders = (ArrayList<OrderBean>) response.getEntity();
		assertNotNull(orders);
		assertEquals(1, orders.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllByStatus() {
		// invalid request
		Response response = service.getAllByStatus(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getAllByStatus(new RequestBean<OrderStatus>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.getAllByStatus(new RequestBean<OrderStatus>(null, OrderStatus.CANCELED));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		
		// invalid request
		response = service.getAllByStatus(new RequestBean<OrderStatus>(new CredentialsBean(), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request but has no permission
		response = service.getAllByStatus(new RequestBean<OrderStatus>(new CredentialsBean("admin", "password"), OrderStatus.CANCELED));
		assertNotNull(response);
		assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
		
		// valid request
		response = service.getAllByStatus(new RequestBean<OrderStatus>(new CredentialsBean("manager", "password"), OrderStatus.CANCELED));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		List<OrderBean> orders = (ArrayList<OrderBean>) response.getEntity();
		assertNotNull(orders);
		assertEquals(1, orders.size());
	}
}
