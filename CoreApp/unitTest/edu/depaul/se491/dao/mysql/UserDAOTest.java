package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.daos.mysql.UserDAO;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * 
 * @author Malik
 *
 */
public class UserDAOTest {
	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	private static UserDAO userDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		
		userDAO = new TestDAOFactory(connFactory).getUserDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// rebuild to original state
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release resources
		dbBuilder = null;
		testDataGen = null;
		userDAO = null;
		
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
	public void testUserDAO() {
		assertNotNull(userDAO);
	}

	@Test
	public void testGet() {
		UserBean user = userDAO.get(1L);
		assertNotNull(user);
		
		assertEquals(1L, user.getId());
		assertEquals("admin FName", user.getFirstName());
		assertEquals("admin LName", user.getLastName());
		assertEquals("admin@email.com", user.getEmail());
		assertEquals("1234567890", user.getPhone());
		assertNotNull(user.getAddress());
	}

	@Test
	public void testTransactionAdd() {
		AddressBean address = new AddressBean(0L, "street address", null, "Chicago", AddressState.IL, "53123");
		UserBean user = new UserBean(0L, "first name", "last name", "myemail1@gmail.com", "0987654321", address);
		
		UserBean addedUser = null;
		Connection con = null;
		try {
			con = connFactory.getConnection();
			addedUser = userDAO.transactionAdd(con, user);
			
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
		
		assertNotNull(addedUser);
		
		long expectedUserId = 6L;
		long expectedAddrId = 7L;
		
		assertEquals(expectedUserId, addedUser.getId());
		assertEquals(user.getFirstName(), addedUser.getFirstName());
		assertEquals(user.getLastName(), addedUser.getLastName());
		assertEquals(user.getEmail(), addedUser.getEmail());
		assertEquals(user.getPhone(), addedUser.getPhone());
		assertNotNull(user.getAddress());
		assertEquals(expectedAddrId, addedUser.getAddress().getId());
	}

	@Test
	public void testTransactionUpdate() {
		UserBean oldUser = userDAO.get(1L);
		oldUser.setFirstName("updated fname");
		oldUser.setLastName("updated lname");
		oldUser.setEmail("updatedemail@hotmail.com");
		oldUser.setPhone("3123121234");
		
		Connection con = null;
		boolean updated = false;
		try {
			con = connFactory.getConnection();
			updated = userDAO.transactionUpdate(con, oldUser);
			
		} catch (Exception e) {
			fail("exception in testTransactionUpdate()");
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

		UserBean updateduser = userDAO.get(1L);
		
		assertEquals(oldUser.getId(), updateduser.getId());
		assertEquals(oldUser.getFirstName(), updateduser.getFirstName());
		assertEquals(oldUser.getLastName(), updateduser.getLastName());
		assertEquals(oldUser.getEmail(), updateduser.getEmail());
		assertEquals(oldUser.getPhone(), updateduser.getPhone());
		assertNotNull(updateduser.getAddress());
		assertEquals(oldUser.getAddress().getId(), updateduser.getAddress().getId());
	}

	@Test
	public void testTransactionDelete() {
		long id = -1L;
		
		Connection con = null;
		boolean deleted = false;
		try {
			AddressBean address = new AddressBean(0L, "street address", null, "Chicago", AddressState.IL, "53123");
			UserBean user = new UserBean(0L, "first name", "last name", "myemail1@gmail.com", "0987654321", address);
			
			con = connFactory.getConnection();
			UserBean newUser = userDAO.transactionAdd(con, user);
			id = newUser.getId();
			deleted = userDAO.transactionDelete(con, newUser);
		} catch (Exception e) {
			fail("exception in testTransactionUpdate()");
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
		assertNull(userDAO.get(id));
	}

}
