package edu.depaul.se491.ws;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.RequestBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * 
 * @author Malik
 *
 */
public class AccountServiceTest {
	private static ConnectionFactory connFactory;
	private static TestDAOFactory daoFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	private AccountService service;
	
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
		service = new AccountService(daoFactory);
		 
		// rebuild the DB before each method
		dbBuilder.rebuildAll();
			
		// generate test data
		testDataGen.generateData();
	}
	
	@Test
	public void testGet() {
		// invalid request
		Response response = service.get(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.get(new RequestBean<String>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.get(new RequestBean<String>(null, new String()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.get(new RequestBean<String>(new CredentialsBean(), new String()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.get(new RequestBean<String>(new CredentialsBean("manager", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// valid request
		response = service.get(new RequestBean<String>(new CredentialsBean("manager", "password"), "employee1"));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		AccountBean account = (AccountBean) response.getEntity();
		assertNotNull(account);
		
		// valid request no account found
		response = service.get(new RequestBean<String>(new CredentialsBean("manager", "password"), "randomAccount"));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());			
	}

	@Test
	public void testPost() {
		// invalid request
		Response response = service.post(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.post(new RequestBean<AccountBean>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.post(new RequestBean<AccountBean>(null, new AccountBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.post(new RequestBean<AccountBean>(new CredentialsBean(), new AccountBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.post(new RequestBean<AccountBean>(new CredentialsBean("admin", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		AddressBean address = new AddressBean(0L, "street", "apt 2", "LA", AddressState.CA, "12345");
		UserBean user = new UserBean(0L, "first", "last", "myemail@hotmail.com", "3123124567", address);
		AccountBean account = new AccountBean(new CredentialsBean("newManager", "password"), user, AccountRole.MANAGER);
		
		// valid request
		response = service.post(new RequestBean<AccountBean>(new CredentialsBean("admin", "password"), account));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		AccountBean createdAccount = (AccountBean) response.getEntity();
		assertNotNull(createdAccount);
		assertNotNull(createdAccount.getUser());
		assertNotNull(createdAccount.getUser().getAddress());
		assertNotEquals(0L, createdAccount.getUser().getId());
		assertNotEquals(0L, createdAccount.getUser().getAddress().getId());
		
		// valid request, bad account data
		response = service.post(new RequestBean<AccountBean>(new CredentialsBean("manager", "password"), new AccountBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());	
	}

	@Test
	public void testUpdate() {
		// invalid request
		Response response = service.update(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.update(new RequestBean<AccountBean>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.update(new RequestBean<AccountBean>(null, new AccountBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.update(new RequestBean<AccountBean>(new CredentialsBean(), new AccountBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.update(new RequestBean<AccountBean>(new CredentialsBean("admin", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		// valid request, bad account data
		response = service.update(new RequestBean<AccountBean>(new CredentialsBean("manager", "password"), new AccountBean()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		
		AccountBean oldAccount = (AccountBean) service.get(new RequestBean<String>(new CredentialsBean("admin", "password"), "manager")).getEntity();
		assertNotNull(oldAccount);
		oldAccount.getCredentials().setPassword("newPassword");
		oldAccount.getUser().setEmail("updatedEmail@email.com");
		
		// valid request
		response = service.update(new RequestBean<AccountBean>(new CredentialsBean("admin", "password"), oldAccount));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		Boolean updated = (Boolean) response.getEntity();
		assertNotNull(updated);
		assertTrue(updated);
		
		AccountBean updatedAccount = (AccountBean) service.get(new RequestBean<String>(new CredentialsBean("admin", "password"), "manager")).getEntity();
		assertNotNull(updatedAccount);
		
		assertEquals(oldAccount.getCredentials().getPassword(), updatedAccount.getCredentials().getPassword());
		assertEquals(oldAccount.getUser().getEmail(), updatedAccount.getUser().getEmail());
		
		// valid request, no account found
		updatedAccount.getCredentials().setUsername("random");
		response = service.update(new RequestBean<AccountBean>(new CredentialsBean("manager", "password"), updatedAccount));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

		// valid request, different user id and address id
		updatedAccount.getCredentials().setUsername("manager");
		updatedAccount.getUser().setId(10);
		updatedAccount.getUser().getAddress().setId(10);
		response = service.update(new RequestBean<AccountBean>(new CredentialsBean("admin", "password"), updatedAccount));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}

	@Test
	public void testDelete() {
		// invalid request
		Response response = service.delete(null);
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.delete(new RequestBean<String>(null, null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		// invalid request
		response = service.delete(new RequestBean<String>(null, new String()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		// invalid request
		response = service.delete(new RequestBean<String>(new CredentialsBean(), new String()));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());


		// invalid request
		response = service.delete(new RequestBean<String>(new CredentialsBean("admin", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

		
		// valid request, invalid
		response = service.delete(new RequestBean<String>(new CredentialsBean("manager", "password"), ""));
		assertNotNull(response);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		response = service.delete(new RequestBean<String>(new CredentialsBean("manager", "password"), "random"));
		assertNotNull(response);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		
		
		// valid request
		response = service.delete(new RequestBean<String>(new CredentialsBean("admin", "password"), "manager"));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		Boolean deleted = (Boolean) response.getEntity();
		assertNotNull(deleted);
		assertTrue(deleted);
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAll() {
		// invalid request
		Response response = service.get(null);
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

		// valid request, not authorized
		response = service.getAll(new RequestBean<Object>(new CredentialsBean("customerapp", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
				

		// valid request
		response = service.getAll(new RequestBean<Object>(new CredentialsBean("manager", "password"), null));
		assertNotNull(response);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		List<AccountBean> accounts = (ArrayList<AccountBean>) response.getEntity();
		assertNotNull(accounts);
		assertEquals(2, accounts.size());
		
		
		
	}

}
