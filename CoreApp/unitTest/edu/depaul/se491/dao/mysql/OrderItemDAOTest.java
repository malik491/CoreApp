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
import edu.depaul.se491.daos.ConnectionFactory;
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
	public void testGetOrderItems() {
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
	public void testTransactionAdd() {
		OrderBean order = new OrderBean();
		order.setId(1L);
		order.setOrderItems(new OrderItemBean[]{ new OrderItemBean(new MenuItemBean(3, null, null, 0, null), 10, OrderItemStatus.READY)});
		
		Connection con = null;
		boolean added = false;
		try {
			con = connFactory.getConnection();
			added = orderItemDAO.transactionAdd(con, order);
			
		} catch (Exception e) {
			fail("exception in testTransactionAdd()");
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		assertTrue(added);
		OrderItemBean[] orderItems = orderItemDAO.getOrderItems(1L);
		assertNotNull(orderItems);
		assertEquals(3, orderItems.length);
		assertEquals(3, orderItems[2].getMenuItem().getId());
		assertEquals(10, orderItems[2].getQuantity());
		assertEquals(OrderItemStatus.READY, orderItems[2].getStatus());
	}

	@Test
	public void testTransactionUpdate() {
		OrderItemBean[] oldItems = orderItemDAO.getOrderItems(1L);
		assertNotNull(oldItems);
		assertEquals(2, oldItems.length);
		
		oldItems[0].setQuantity(0);
		oldItems[1].setStatus(OrderItemStatus.READY);
		
		OrderBean order = new OrderBean();
		order.setId(1L);
		order.setOrderItems(oldItems);
		
		Connection con = null;
		boolean updated = false;
		try {
			con = connFactory.getConnection();
			updated = orderItemDAO.transactionUpdate(con, order);
			
		} catch (Exception e) {
			fail("exception in testTransactionAdd()");
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		assertTrue(updated);
		
		OrderItemBean[] updatedItems = orderItemDAO.getOrderItems(1L);
		assertNotNull(updatedItems);
		
		assertEquals(1, updatedItems.length);
		
		assertEquals(oldItems[1].getMenuItem().getId(), updatedItems[0].getMenuItem().getId());
		assertEquals(oldItems[1].getQuantity(), updatedItems[0].getQuantity());
		assertEquals(oldItems[1].getStatus(), updatedItems[0].getStatus());
	}

	@Test
	public void testTransactionDelete() {
		Connection con = null;
		boolean deleted = false;
		try {
			con = connFactory.getConnection();
			deleted = orderItemDAO.transactionDeleteAll(con, 1L, 2);
			
		} catch (Exception e) {
			fail("exception in testTransactionAdd()");
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		assertTrue(deleted);
		assertEquals(0, orderItemDAO.getOrderItems(1L).length);
	}

}
