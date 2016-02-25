package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.daos.BadConnection;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.ExceptionConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.daos.mysql.OrderItemDAO;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * 
 * @author Malik
 *
 */
public class OrderItemDAOTest {
	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	private static OrderItemDAO orderItemDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		orderItemDAO = new TestDAOFactory(connFactory).getOrderItemDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// rebuild to original state
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release resources
		dbBuilder = null;
		testDataGen = null;
		orderItemDAO = null;
		
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
	public void testOrderItemDAO() {
		assertNotNull(orderItemDAO);
	}

	@Test
	public void testGetOrderItems() throws SQLException {
		OrderItemBean[] orderItems = null;
		orderItems = orderItemDAO.getOrderItems(1L);
		
		int expectedItemsCount = 2;
		
		assertNotNull(orderItems);
		assertEquals(expectedItemsCount, orderItems.length);
		
		for(int i=0; i < expectedItemsCount; i++) {
			assertNotNull(orderItems[i]);
			assertNotNull(orderItems[i].getMenuItem());
			assertEquals(i + 1, orderItems[i].getMenuItem().getId());
			assertEquals(1, orderItems[i].getQuantity());
			assertEquals(OrderItemStatus.NOT_READY, orderItems[i].getStatus());
		}
	}

	@Test
	public void testTransactionAdd() throws SQLException {
		long orderId = 1;
		OrderBean order = new OrderBean();
		order.setId(orderId);
		order.setOrderItems(
				new OrderItemBean[]{ 
						new OrderItemBean(new MenuItemBean(3, null, null, 0, null), 3, OrderItemStatus.NOT_READY),
						new OrderItemBean(new MenuItemBean(4, null, null, 0, null), 4, OrderItemStatus.NOT_READY)
				});
		
		Connection con = null;
		try {
			con = connFactory.getConnection();
			boolean added = orderItemDAO.transactionAdd(con, order);
			
			assertTrue(added);
			OrderItemBean[] orderItems = orderItemDAO.getOrderItems(orderId);
			assertNotNull(orderItems);
			assertEquals(4, orderItems.length);
			for (int i=2; i < 4; i++) {
				assertNotNull(orderItems[i].getMenuItem());
				assertEquals(i+1, orderItems[i].getMenuItem().getId());
				assertEquals(i+1, orderItems[i].getQuantity());
				assertEquals(OrderItemStatus.NOT_READY, orderItems[i].getStatus());
			}
			
			// test zero items
			order.setOrderItems(new OrderItemBean[0]);
			assertFalse(orderItemDAO.transactionAdd(con, order));
			
		} catch (SQLException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
	}

	@Test
	public void testTransactionUpdate() throws SQLException {
		OrderItemBean[] oldItems = orderItemDAO.getOrderItems(1L);
		assertNotNull(oldItems);
		assertEquals(2, oldItems.length);
		
		oldItems[0].setQuantity(0);
		oldItems[1].setStatus(OrderItemStatus.READY);
		
		OrderBean order = new OrderBean();
		order.setId(1L);
		order.setOrderItems(oldItems);
		
		Connection con = null;
		try {
			con = connFactory.getConnection();
			boolean updated = orderItemDAO.transactionUpdate(con, order);
			
			assertTrue(updated);
			
			OrderItemBean[] updatedItems = orderItemDAO.getOrderItems(1L);
			assertNotNull(updatedItems);
			
			assertEquals(1, updatedItems.length);
			
			assertEquals(oldItems[1].getMenuItem().getId(), updatedItems[0].getMenuItem().getId());
			assertEquals(oldItems[1].getQuantity(), updatedItems[0].getQuantity());
			assertEquals(oldItems[1].getStatus(), updatedItems[0].getStatus());
			
			// test zero items
			order.setOrderItems(new OrderItemBean[0]);
			assertFalse(orderItemDAO.transactionUpdate(con, order));
			
		} catch (SQLException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
	}

	@Test
	public void testTransactionDelete() throws SQLException {
		Connection con = null;
		boolean deleted = false;
		try {
			con = connFactory.getConnection();
			deleted = orderItemDAO.transactionDeleteAll(con, 1L, 2);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
		
		assertTrue(deleted);
		assertEquals(0, orderItemDAO.getOrderItems(1L).length);
	}
	
	@Test
	public void testExceptions() {
		OrderItemDAO dao = new TestDAOFactory(new ExceptionConnectionFactory()).getOrderItemDAO();
		OrderBean order = new OrderBean();
		order.setId(1L);
		order.setOrderItems(new OrderItemBean[]{new OrderItemBean(new MenuItemBean(1L, null, null, 0, null), 1, OrderItemStatus.READY)});
		
		try {
			dao.getOrderItems(order.getId());
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.transactionAdd(new BadConnection(),	order);
			fail("No Exception Thrown");
		} catch (SQLException e) {}
		
		try {
			dao.transactionUpdate(new BadConnection(), order);
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.transactionDeleteAll(new BadConnection(), order.getId(), 1);
			fail("No Exception Thrown");
		} catch (SQLException e) {}
				
		
		assertTrue(true);
	}

}
