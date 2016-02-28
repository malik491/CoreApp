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
	public void testHide() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.hideMenuItem(1L));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNull(serviceClient.hideMenuItem(0L));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		Boolean updated = serviceClient.hideMenuItem(1L); 
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
		
		// restore (unhide again so it doesn't affect other methods)
		updated = serviceClient.unhideMenuItem(1L); 
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
	}
	
	@Test
	public void testUnhide() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.unhideMenuItem(1L));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNull(serviceClient.unhideMenuItem(0L));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		Boolean updated = serviceClient.unhideMenuItem(7L); 
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
		
		// restore (hide again so it doesn't affect other methods)
		updated = serviceClient.hideMenuItem(7L); 
		assertNotNull(updated);
		assertTrue(updated);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetAllVisible() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.getAllVisible());
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		MenuItemBean[] items = serviceClient.getAllVisible(); 
		assertNotNull(items);
		// 6 or 7, depending or whether testPost() is called first or not
		assertTrue(items.length == 6 || items.length == 7);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetAllHidden() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.getAllHidden());
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new MenuServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		MenuItemBean[] items = serviceClient.getAllHidden(); 
		assertNotNull(items);
		assertEquals(1, items.length);
		assertNull(serviceClient.getResponseMessage());
	}
	
	@Test
	public void testExceptions() {
		MenuServiceClient serviceClient = new MenuServiceClient(null, "wrongURL");
		assertNull(serviceClient.get(0));
		assertNull(serviceClient.delete(0));
		assertNull(serviceClient.post(null));
		assertNull(serviceClient.update(null));
		assertNull(serviceClient.getAllVisible());
		assertNull(serviceClient.getAllHidden());
		assertNull(serviceClient.updateIsHidden(1, true));
		assertNull(serviceClient.updateIsHidden(1, false));
	}
}
