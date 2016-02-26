package edu.depaul.se491.ws.clients;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * NOTE (Tomcat must be running for this to pass)
 * @author Malik
 */
public class AccountServiceClientTest {
	private String serviceBaseURL = "http://localhost/CoreApp/account";

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
	public void testAccountServiceClient() {
		assertNotNull(new AccountServiceClient(null, serviceBaseURL));
	}

	@Test
	public void testGet() {
		AccountServiceClient serviceClient = new AccountServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.get("admin"));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new AccountServiceClient(new CredentialsBean("employee1", "password"), serviceBaseURL);
		assertNull(serviceClient.get("manager"));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new AccountServiceClient(new CredentialsBean("admin", "password"), serviceBaseURL);
		assertNotNull(serviceClient.get("manager"));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testPost() {
		AccountServiceClient serviceClient = new AccountServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.post(new AccountBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new AccountServiceClient(new CredentialsBean("admin", "password"), serviceBaseURL);
		assertNull(serviceClient.post(new AccountBean()));
		assertNotNull(serviceClient.getResponseMessage());

		CredentialsBean credentials = new CredentialsBean("manager2", "password");
		AddressBean address = new AddressBean(0, "line 1", "line 2", "city", AddressState.CO, "1234567890");
		UserBean user = new UserBean(0, "first", "last", "memail@email.com", "1234567890", address);
		AccountBean account = new AccountBean(credentials, user, AccountRole.MANAGER);
		
		serviceClient = new AccountServiceClient(new CredentialsBean("admin", "password"), serviceBaseURL);
		assertNotNull(serviceClient.post(account));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testUpdate() {
		AccountServiceClient serviceClient = new AccountServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.update(new AccountBean()));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new AccountServiceClient(new CredentialsBean("admin", "password"), serviceBaseURL);
		assertNull(serviceClient.update(new AccountBean()));
		assertNotNull(serviceClient.getResponseMessage());

		CredentialsBean credentials = new CredentialsBean("employee1", "updatedPassword");
		AddressBean address = new AddressBean(3, "updated line 1", null, "city", AddressState.CO, "1234567890");
		UserBean user = new UserBean(3, "my first", "my last", "updatedemail@email.com", "1234567890", address);
		AccountBean account = new AccountBean(credentials, user, AccountRole.EMPLOYEE);
		
		serviceClient = new AccountServiceClient(new CredentialsBean("employee1", "password"), serviceBaseURL);
		assertNotNull(serviceClient.update(account));
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testDelete() {
		AccountServiceClient serviceClient = new AccountServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.delete("admin"));
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new AccountServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		assertNull(serviceClient.delete("admin"));
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new AccountServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		Boolean deleted = serviceClient.delete("employee2");
		assertNotNull(deleted);
		assertTrue(deleted);
		assertNull(serviceClient.getResponseMessage());
	}

	@Test
	public void testGetAll() {
		AccountServiceClient serviceClient = new AccountServiceClient(null, serviceBaseURL);
		assertNull(serviceClient.getAll());
		assertNotNull(serviceClient.getResponseMessage());
		
		serviceClient = new AccountServiceClient(new CredentialsBean("customerapps", "password"), serviceBaseURL);
		assertNull(serviceClient.getAll());
		assertNotNull(serviceClient.getResponseMessage());

		serviceClient = new AccountServiceClient(new CredentialsBean("manager", "password"), serviceBaseURL);
		AccountBean[] accounts = serviceClient.getAll();
		assertNotNull(accounts);
		// employee2 maybe deleted but not employee1 if testDelete() runs before this method.
		assertTrue(accounts.length == 1 || accounts.length == 2 );
		assertNull(serviceClient.getResponseMessage());
	}
	
	@Test
	public void testExceptions() {
		AccountServiceClient serviceClient = new AccountServiceClient(null, "wrongURL");
		assertNull(serviceClient.getAll());
		assertNull(serviceClient.get(null));
		assertNull(serviceClient.post(null));
		assertNull(serviceClient.update(null));
		assertNull(serviceClient.delete(null));
	}

}
