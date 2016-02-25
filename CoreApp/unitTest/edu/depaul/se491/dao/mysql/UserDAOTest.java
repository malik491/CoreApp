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
import edu.depaul.se491.daos.BadConnection;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.ExceptionConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.daos.mysql.UserDAO;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;
import edu.depaul.se491.utils.ParamLengths;

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
	public void testGet() throws SQLException {
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
	public void testTransactionAdd() throws Exception {
		AddressBean address = new AddressBean(0L, "street address", null, "Chicago", AddressState.IL, "53123");
		UserBean user = new UserBean(0L, "first name", "last name", "myemail1@gmail.com", "0987654321", address);
		
		UserBean addedUser = null;
		Connection con = null;
		try {
			con = connFactory.getConnection();
			addedUser = userDAO.transactionAdd(con, user);
			
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
	public void testTransactionUpdate() throws SQLException {
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
	public void testTransactionDelete() throws SQLException {
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
		assertNull(userDAO.get(id));
	}
	
	
	@Test
	public void testExceptions() throws SQLException {
		UserDAO dao = new TestDAOFactory(new ExceptionConnectionFactory()).getUserDAO();
		
		try {
			dao.get(1L);
			fail("No Exception Thrown");
		} catch (SQLException e) {}
		
		try {
			dao.transactionAdd(new BadConnection(), new UserBean());
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.transactionDelete(new BadConnection(), new UserBean());
			fail("No Exception Thrown");
		} catch (SQLException e) {}

		try {
			dao.transactionUpdate(new BadConnection(), new UserBean());
			fail("No Exception Thrown");
		} catch (SQLException e) {}
		
		
		Connection conn = null;
		try {
			UserBean oldUser = userDAO.get(1L);
			
			StringBuilder sb = new StringBuilder();
			for (int i=0; i < ParamLengths.User.MAX_F_NAME + 1; i++)
				sb.append("x");
			
			oldUser.setFirstName(sb.toString());
			
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			
			userDAO.transactionUpdate(conn, oldUser);
			
			fail("No Exception Thrown");			
		} catch (SQLException e) {
			if (conn == null) {
				throw e;
			} else {
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException e1) {
					throw e1;
				}
			}
		}
		
		try {
			UserBean user = new UserBean(0L, "firstName", "lastName", "myemail@Email", "1234567890", new AddressBean(0L, "line 1", "line 2", "City", AddressState.IL, "12345"));
			
			StringBuilder sb = new StringBuilder();
			for (int i=0; i < ParamLengths.User.MAX_PHONE + 1; i++)
				sb.append("x");
			
			user.setPhone(sb.toString());
			
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			
			userDAO.transactionAdd(conn, user);
			
			fail("No Exception Thrown");			
		} catch (Exception e) {
			if (conn == null) {
				throw e;
			} else {
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException e1) {
					throw e1;
				}
			}
		}
		
		assertTrue(true);
	}

}
