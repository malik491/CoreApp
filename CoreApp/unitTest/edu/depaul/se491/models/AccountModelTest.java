package edu.depaul.se491.models;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

public class AccountModelTest {

	private static ConnectionFactory connFactory;
	private static TestDAOFactory daoFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		daoFactory= new TestDAOFactory(connFactory);
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



	@Test
	public void testAccountModelDAOFactoryCredentialsBean() {
		CredentialsBean credentials = new CredentialsBean("manager", "password");
		AccountModel model = new AccountModel(daoFactory, credentials);
		assertNotNull(model);
	}

	@Test
	public void testCreate() {
		AddressBean address = new AddressBean(0L, "line1", "line2", "city", AddressState.IL, "123456");
		UserBean user = new UserBean(0L, "first","last", "email1@gmail.com", "1234567890", address);
		
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			AccountModel model = new AccountModel(daoFactory, credentials);

			if (username.equals("admin")) {
				// valid accounts
				AccountBean newAdminAccount = new AccountBean(new CredentialsBean("admin100", "password"), user, AccountRole.ADMIN);
				AccountBean newManagerAccount = new AccountBean(new CredentialsBean("manager100", "password"), user, AccountRole.MANAGER);
				AccountBean newEmployeeAccount = new AccountBean(new CredentialsBean("employee100", "password"), user, AccountRole.EMPLOYEE);
				AccountBean newCustomerAccount = new AccountBean(new CredentialsBean("customerapp100", "password"), user, AccountRole.CUSTOMER_APP);
				
				
				assertNotNull(model.create(newManagerAccount));
				assertNotNull(model.create(newEmployeeAccount));
				assertNotNull(model.create(newCustomerAccount));
				
				// creating another admin account
				assertNull(model.create(newAdminAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
				// valid accounts, creating account with existing username
				AccountBean existingAccount = newManagerAccount;
				assertNull(model.create(existingAccount));
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
				
				// test invalid account
				assertNull(model.create(new AccountBean()));
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
				
			} else if (username.equals("manager")) {
				// valid accounts
				AccountBean newAdminAccount = new AccountBean(new CredentialsBean("admin200", "password"), user, AccountRole.ADMIN);
				AccountBean newManagerAccount = new AccountBean(new CredentialsBean("manager200", "password"), user, AccountRole.MANAGER);
				AccountBean newEmployeeAccount = new AccountBean(new CredentialsBean("employee200", "password"), user, AccountRole.EMPLOYEE);
				AccountBean newCustomerAccount = new AccountBean(new CredentialsBean("customerapp200", "password"), user, AccountRole.CUSTOMER_APP);
				
				// creating employee account
				assertNotNull(model.create(newEmployeeAccount));

				// creating admin, manager, customerApp account
				assertNull(model.create(newAdminAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
				assertNull(model.create(newManagerAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
				assertNull(model.create(newCustomerAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
								
				// creating account with existing username
				AccountBean existingAccount = newEmployeeAccount;
				assertNull(model.create(existingAccount));
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
				
				// test invalid account
				assertNull(model.create(new AccountBean()));
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
				
			} else {
				// valid accounts
				AccountBean newAdminAccount = new AccountBean(new CredentialsBean("admin200", "password"), user, AccountRole.ADMIN);
				AccountBean newManagerAccount = new AccountBean(new CredentialsBean("manager200", "password"), user, AccountRole.MANAGER);
				AccountBean newEmployeeAccount = new AccountBean(new CredentialsBean("employee200", "password"), user, AccountRole.EMPLOYEE);
				AccountBean newCustomerAccount = new AccountBean(new CredentialsBean("customerapp200", "password"), user, AccountRole.CUSTOMER_APP);
				
				assertNull(model.create(newAdminAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
				assertNull(model.create(newManagerAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
				assertNull(model.create(newEmployeeAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
				assertNull(model.create(newCustomerAccount));
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
			}
						
		}
	}

	@Test
	public void testUpdate() {
		AccountModel model = new AccountModel(daoFactory, new CredentialsBean("admin", "password"));

		AccountBean adminAccount = model.read("admin");
		AccountBean managerAccount = model.read("manager");
		AccountBean employeeAccount = model.read("employee1");
		AccountBean customerAppAccount = model.read("customerapp");
		assertNotNull(adminAccount);
		assertNotNull(managerAccount);
		assertNotNull(employeeAccount);
		assertNotNull(customerAppAccount);
		
		AddressBean address = new AddressBean(1L, "line1", "line2", "city", AddressState.IL, "123456");
		UserBean user = new UserBean(1L, "first","last", "email1@gmail.com", "1234567890", address);
		AccountBean noneExistingAccount = new AccountBean(new CredentialsBean("random", "password"), user, AccountRole.EMPLOYEE);
		
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			model = new AccountModel(daoFactory, credentials);

			AccountBean ownAccount = model.read(username);
			
			Boolean updated = null;
			if (username.equals("customerapp")) {
				assertNull(ownAccount);
				
				updated = model.update(customerAppAccount);
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			} else {
				assertNotNull(ownAccount);
				
				ownAccount.getCredentials().setPassword("updatedPassword");
				ownAccount.getUser().setEmail("updatedEmail@gmail.com");
				ownAccount.getUser().getAddress().setLine1("updated line 1");
			}
			
			
			if (username.equals("admin")) {
				// update own account
				updated = model.update(ownAccount);
				assertNotNull(updated);
				assertTrue(updated);
				
				adminAccount = ownAccount;
			
				managerAccount.getUser().setFirstName("updated");
				employeeAccount.getUser().setFirstName("updated");
				customerAppAccount.getUser().setFirstName("updated");
				
				updated = model.update(managerAccount);
				assertNotNull(updated);
				assertTrue(updated);
				
				updated = model.update(employeeAccount);
				assertNotNull(updated);
				assertTrue(updated);
				
				updated = model.update(customerAppAccount);
				assertNotNull(updated);
				assertTrue(updated);
				
				// invalid
				updated = model.update(new AccountBean());
				assertNull(updated);
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
				
				// try to update not found account
				updated = model.update(noneExistingAccount);
				assertNull(updated);
				assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());
				
			} else if (username.equals("manager")) {
				// update own account
				updated = model.update(ownAccount);
				assertNotNull(updated);
				assertTrue(updated);
				
				managerAccount = ownAccount;
				
				employeeAccount.getUser().setFirstName("updated");
				updated = model.update(employeeAccount);
				assertNotNull(updated);
				assertTrue(updated);
				
				// unauthorized
				updated = model.update(adminAccount);
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());

				updated = model.update(customerAppAccount);
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());

				// invalid
				updated = model.update(new AccountBean());
				assertNull(updated);
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
				
				// try to update not found account
				updated = model.update(noneExistingAccount);
				assertNull(updated);
				assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());
				
			} else if (username.equals("employee1")) {
				// update own account
				updated = model.update(ownAccount);
				assertNotNull(updated);
				assertTrue(updated);
				
				employeeAccount = ownAccount;
				
				// unauthorized
				updated = model.update(adminAccount);
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				
				updated = model.update(managerAccount);
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());

				updated = model.update(customerAppAccount);
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());

				// invalid
				updated = model.update(new AccountBean());
				assertNull(updated);
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());				
				
				// try to update not found account
				updated = model.update(noneExistingAccount);
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			}
						
		}
	
	}

	@Test
	public void testRead() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			AccountModel model = new AccountModel(daoFactory, new CredentialsBean(username, "password"));
			
			for (String anotherAccountUsername : new String[]{"admin", "manager", "employee1", "customerapp"}) {
				if (!username.equals("customerapp") && username.equals(anotherAccountUsername)) {
					// read own account
					assertNotNull(model.read(anotherAccountUsername));

					// wrong account username
					assertNull(model.read("random"));
					if (username.equals("admin") || username.equals("manager"))
						assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());
					else
						assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
				} else {
					// read other accounts
					if (username.equals("admin") || (username.equals("manager") && anotherAccountUsername.equals("employee1"))) {
						// admin can real all, manager can read employee.
						assertNotNull(model.read(anotherAccountUsername));
					} else {
						assertNull(model.read(anotherAccountUsername));
						assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
					}
				}
			}			
		
			// wrong account password
			model = new AccountModel(daoFactory, new CredentialsBean(username, "wrongPassword"));
			assertNull(model.read(username));
			assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());
		}
	}

	@Test
	public void testDelete() {
		for (String username : new String[]{"customerapp", "employee1", "manager", "admin"}) {
			AccountModel model = new AccountModel(daoFactory, new CredentialsBean(username, "password"));
			
			// delete own account
			assertNull(model.delete(username));
			assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			
			for (String anotherAccountUsername : new String[]{"customerapp", "employee1", "employee2", "manager", "admin"}) {
				if (!username.equals(anotherAccountUsername)) {
					// delete other accounts
					if (username.equals("admin")) {
						// employee1 will be deleted by manager (previous loop)
						if (!anotherAccountUsername.equals("employee1"))
							assertNotNull(model.delete(anotherAccountUsername));
					} else  if (username.equals("manager") && (anotherAccountUsername.equals("employee1") || anotherAccountUsername.equals("employee2"))){
						// don't delete employee2, leave it for the admin loop (deleting employee)
						if (anotherAccountUsername.equals("employee1"))
							assertNotNull(model.delete(anotherAccountUsername));
					} else {
						assertNull(model.delete(anotherAccountUsername));
						assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
					}
				}
			}			
		}
	}

	@Test
	public void testReadAll() {
		for (String username : new String[]{"customerapp", "employee1", "manager", "admin"}) {
			AccountModel model = new AccountModel(daoFactory, new CredentialsBean(username, "password"));
			
			List<AccountBean> accounts = model.readAll();
			
			if (username.equals("admin")) {
				assertNotNull(accounts);
				assertEquals(4, accounts.size());
			} else if (username.equals("manager")) {
				assertNotNull(accounts);
				assertEquals(2, accounts.size());				
			} else {
				assertNull(accounts);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			}
		}
		
	}

}
