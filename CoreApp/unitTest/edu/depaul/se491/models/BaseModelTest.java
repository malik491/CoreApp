package edu.depaul.se491.models;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.ExceptionConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * @author Malik
 */
public class BaseModelTest {
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
		// rebuild the DB before each method
		dbBuilder.rebuildAll();
			
		// generate test data
		testDataGen.generateData();
	}
	
	/**
	 * Test method for {@link edu.depaul.se491.models.BaseModel#BaseModel(edu.depaul.se491.daos.DAOFactory, edu.depaul.se491.beans.CredentialsBean)}.
	 */
	@Test
	public void testBaseModel() {
		BaseModel model = new BaseModel(null, null);
		assertNotNull(model);
		assertNull(model.getResponseStatus());
		assertEquals("", model.getResponseMessage());
	}

	@Test
	public void testSetResponseMessageAndStatus() {
		BaseModel model = new BaseModel(null, null);
		
		model.setResponseMessage("message");
		assertNotNull(model.getResponseMessage());
		assertEquals("message", model.getResponseMessage());

		model.setResponseStatus(Response.Status.OK);
		assertNotNull(model.getResponseStatus());
		assertEquals(Response.Status.OK, model.getResponseStatus());

		model.setResponseAndMeessageForDBError(new SQLException("exception test"));
		assertNotNull(model.getResponseMessage());
		assertEquals(BaseModel.GENERIC_SERVER_ERR_MSG, model.getResponseMessage());
		assertNotNull(model.getResponseStatus());
		assertEquals(Response.Status.INTERNAL_SERVER_ERROR, model.getResponseStatus());

	}

	@Test
	public void testGetDAOFactory() {
		BaseModel model = new BaseModel(null, null);
		assertNull(model.getDAOFactory());
		
		model = new BaseModel(daoFactory, null);
		assertNotNull(model.getDAOFactory());
		assertSame(daoFactory, model.getDAOFactory());
	}

	@Test
	public void testGetLoggedinAccount() {
		BaseModel model = new BaseModel(daoFactory, null);
		assertNull(model.getLoggedinAccount());
		assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
		
		model = new BaseModel(daoFactory, new CredentialsBean());
		assertNull(model.getLoggedinAccount());
		assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
		
		model = new BaseModel(daoFactory, new CredentialsBean("randomUsername", "password"));
		assertNull(model.getLoggedinAccount());
		assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());

		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			model = new BaseModel(daoFactory, new CredentialsBean(username, "wrongPassword"));
			assertNull(model.getLoggedinAccount());
			assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());

			model = new BaseModel(daoFactory, new CredentialsBean(username, "password"));
			assertNotNull(model.getLoggedinAccount());
		}

	}

	@Test
	public void testHasPermission() {
		BaseModel model = new BaseModel(daoFactory, new CredentialsBean("admin", "password"));
		assertNotNull(model.getLoggedinAccount());
		
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			model = new BaseModel(daoFactory, new CredentialsBean(username, "password"));
			assertNotNull(model.getLoggedinAccount());
			
			assertFalse(model.hasPermission(new AccountRole[]{}));
			
			assertTrue(model.hasPermission(new AccountRole[]{model.getLoggedinAccount().getRole()}));
		}
	}
	
	@Test
	public void testExceptions() {
		BaseModel model = new BaseModel(new TestDAOFactory(new ExceptionConnectionFactory()), new CredentialsBean("admin", "password"));
		assertNull(model.getLoggedinAccount());
		assertEquals(Response.Status.INTERNAL_SERVER_ERROR, model.getResponseStatus());
	}

}
