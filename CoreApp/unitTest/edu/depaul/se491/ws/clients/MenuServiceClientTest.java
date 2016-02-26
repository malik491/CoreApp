package edu.depaul.se491.ws.clients;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * NOTE (Tomcat must be running for this to pass)
 * @author Malik
 *
 */
public class MenuServiceClientTest {
	private String serviceBaseURL = "http://localhost/CoreApp/menuItem";

	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release and close resources
		dbBuilder = null;
		testDataGen = null;
	
		// close connection data source (pool)
		connFactory.close();
	}
	
	@Test
	public void testMenuServiceClient() {
		assertNotNull(new MenuServiceClient(null, serviceBaseURL));
	}

	@Test
	public void testGet() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.get(0));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNull(serviceClient.get(10));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNotNull(serviceClient.get(1));
		assertNull(serviceClient.getResponseMessage());

	}

	@Test
	public void testPost() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.post(new MenuItemBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNull(serviceClient.post(new MenuItemBean()));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNotNull(serviceClient.post(new MenuItemBean(0, "name", "description", 1.50, MenuItemCategory.MAIN)));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testUpdate() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.update(new MenuItemBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNull(serviceClient.update(new MenuItemBean()));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		Boolean updated = serviceClient.update(new MenuItemBean(1, "updated name", "updated description", 1.50, MenuItemCategory.SIDE)); 
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testDelete() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.delete(1));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNull(serviceClient.delete(0));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		Boolean deleted = serviceClient.delete(1); 
		assertNotNull(deleted);
		assertFalse(deleted);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetAll() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.getAll());
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		MenuItemBean[] items = serviceClient.getAll(); 
		assertNotNull(items);
		assertTrue(items.length >= 6);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testExceptions() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, "wrongURL");
		assertNull(serviceClient.get(0));
		assertNull(serviceClient.delete(0));
		assertNull(serviceClient.post(null));
		assertNull(serviceClient.update(null));
		assertNull(serviceClient.getAll());
	}
}
