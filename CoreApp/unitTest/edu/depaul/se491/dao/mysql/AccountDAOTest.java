package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.util.List;

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
import edu.depaul.se491.daos.mysql.AccountDAO;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * 
 * @author Malik
 *
 */
public class AccountDAOTest {
	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	private static AccountDAO accountDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		
		accountDAO = new TestDAOFactory(connFactory).getAccountDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// rebuild to original state
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release resources
		dbBuilder = null;
		testDataGen = null;
		accountDAO = null;
		
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
	public void testAccountDAO() {
		assertNotNull(accountDAO);
	}

	@Test
	public void testGetAllByRole() {
		List<AccountBean> accounts = null;
		
		accounts = accountDAO.getAllByRole(AccountRole.ADMIN);
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
		
		accounts = accountDAO.getAllByRole(AccountRole.MANAGER);
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
		
		accounts = accountDAO.getAllByRole(AccountRole.EMPLOYEE);
		assertNotNull(accounts);
		assertEquals(2, accounts.size());
		
		accounts = accountDAO.getAllByRole(AccountRole.CUSTOMER_APP);
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
	}

	@Test
	public void testGet() {
		AccountBean account = accountDAO.get("admin");
		assertNotNull(account);
		
		CredentialsBean credentials = account.getCredentials();
		assertNotNull(credentials);
		assertEquals("admin", credentials.getUsername());
		assertEquals("password", credentials.getPassword());
		
		UserBean user = account.getUser();
		assertNotNull(user);
		assertEquals(1L, user.getId());
		assertNotNull(user.getAddress());
		assertEquals(1L, user.getAddress().getId());
		
		assertNotNull(account.getRole());
		assertEquals(AccountRole.ADMIN, account.getRole());
		
	}

	@Test
	public void testAdd() {
		AddressBean address = new AddressBean(0L, "street address", null, "Chicago", AddressState.IL, "53123");
		UserBean user = new UserBean(0L, "first name", "last name", "myemail1@gmail.com", "0987654321", address);
		AccountBean account = new AccountBean(new CredentialsBean("myusername", "password"), user, AccountRole.EMPLOYEE);
		
		AccountBean addedAccount = accountDAO.add(account);
		
		assertNotNull(addedAccount);
		assertEquals(account.getCredentials().getUsername(), addedAccount.getCredentials().getUsername());
		assertEquals(account.getCredentials().getPassword(), addedAccount.getCredentials().getPassword());

		assertNotNull(account.getRole());
		assertEquals(account.getRole(), addedAccount.getRole());
		
		
		long expectedUserId = 6L;
		
		UserBean addedUser = addedAccount.getUser();
		assertNotNull(addedUser);		
		assertEquals(expectedUserId, addedUser.getId());
		assertEquals(user.getFirstName(), addedUser.getFirstName());
		assertEquals(user.getLastName(), addedUser.getLastName());
		assertEquals(user.getEmail(), addedUser.getEmail());
		assertEquals(user.getPhone(), addedUser.getPhone());
		
		long expectedAddressId = 7L;
		
		AddressBean addedAddress = addedUser.getAddress();
		assertNotNull(addedAddress);
		assertEquals(expectedAddressId, addedAddress.getId());
		assertEquals(address.getLine1(), addedAddress.getLine1());
		assertNull(addedAddress.getLine2());
		assertEquals(address.getCity(), addedAddress.getCity());
		assertEquals(address.getState(), addedAddress.getState());
		assertEquals(address.getZipcode(), addedAddress.getZipcode());

	}

	@Test
	public void testUpdate() {
		AccountBean oldAccount = accountDAO.get("employee1");
		
		CredentialsBean oldCredentials = oldAccount.getCredentials();
		oldCredentials.setPassword("updated");
		
		UserBean oldUser = oldAccount.getUser();
		oldUser.setFirstName("updated");
		
		oldAccount.setCredentials(oldCredentials);
		oldAccount.setUser(oldUser);
		oldAccount.setRole(AccountRole.MANAGER);
		
		boolean updated = accountDAO.update(oldAccount);
		assertTrue(updated);
		
		AccountBean updatedAccount = accountDAO.get("employee1");
		assertEquals(oldAccount.getCredentials().getUsername(), updatedAccount.getCredentials().getUsername());
		assertEquals(oldAccount.getCredentials().getPassword(), updatedAccount.getCredentials().getPassword());
		assertEquals(oldAccount.getUser().getFirstName(), updatedAccount.getUser().getFirstName());
		assertEquals(oldAccount.getRole(), updatedAccount.getRole());
	}

	@Test
	public void testDelete() {
		boolean deleted = accountDAO.delete("employee2");
		assertTrue(deleted);
		assertNull(accountDAO.get("employee2"));
	}

}
