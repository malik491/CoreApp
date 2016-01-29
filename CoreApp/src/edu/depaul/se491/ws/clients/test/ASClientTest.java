package edu.depaul.se491.ws.clients.test;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.ws.clients.AccountServiceClient;

public class ASClientTest {
	private static final String serviceBaseUrl = "http://localhost/CoreApp/account";
	private AccountServiceClient accountServiceClient;
	
	
	public static void main(String[] args) {
		CredentialsBean credentials = new CredentialsBean();
		credentials.setUsername("manager");
		credentials.setPassword("password");
		
		AccountServiceClient client = new AccountServiceClient(credentials, serviceBaseUrl);
		ASClientTest clientTest = new ASClientTest(client);
		clientTest.run();
	}
	
	
	public ASClientTest(AccountServiceClient accountServiceClient) {
		this.accountServiceClient = accountServiceClient;
	}

	
	public void run() {
		testGetAll();
		
		//System.out.println("\n");
		
		//testGet();

		//System.out.println("\n");

		//testPost();

		//System.out.println("\n");

		//testUpdate();

		//System.out.println("\n");

		testDelete();
		
		//System.out.println("\n");

		//testGetAll();
	}
	
	private void testGet() {
		String accountUsername = "vendor";
		
		AccountBean bean = accountServiceClient.get(accountUsername);
		
		System.out.println("accountServiceClient.get():");
		report(bean);
		System.out.println();
	}

	private void testGetAll() {
		AccountBean[] beans = accountServiceClient.getAll();
		
		System.out.println("accountServiceClient.getAll():" + (beans == null? "\n -- null account list --" : "(size = " + beans.length + ")"));
		
		if (beans != null) {
			for (AccountBean bean: beans) {
				report(bean);
				System.out.println();
			}
		}
		System.out.println();		
	}

	private void testPost() {
		AddressBean newAddress = getAddressBean(0, "1000 line1 st", "apt 100", "Chicago", AddressState.IL, "12345");
		UserBean newUser = getUserBean(0, "new_employee_fn", "new_employee_ln", "new.employee@email.com", "1234567890", newAddress);
		AccountBean newAccount = getAccountBean(accountUsername, "password", AccountRole.ADMIN, newUser);
		
		AccountBean createdAccount = accountServiceClient.post(newAccount);
		
		System.out.println("accountServiceClient.post():");
		report(createdAccount);
		System.out.println();
	}

	private void testUpdate() {
		// must call testPost() before calling this
		AccountBean existingAccount = accountServiceClient.get(accountUsername);
		
		if (existingAccount == null) {
			System.out.println("call accountServiceClient.get() returned null. Either you called testUpdate() before testPost() or testPost() used a diffeent account name");
			report(existingAccount);
			System.out.println();
		} else {
			// update it
			existingAccount.getCredentials().setPassword("updatedPassword");
			existingAccount.getUser().setEmail("updated.employee@email.com");
			
			// update
			Boolean isUpdated = accountServiceClient.update(existingAccount);
			
			System.out.println("accountServiceClient.update():");
			System.out.println(" -- update: " + isUpdated);
			System.out.println();
		}
	}

	private void testDelete() {
		String newAccountUsername = "customerApp";
//		AddressBean newAddress = getAddressBean(0, "1000 line1 st", "apt 100", "Chicago", AddressState.IL, "12345");
//		UserBean newUser = getUserBean(0, "new_employee_fn", "new_employee_ln", "tobedelete.employee@email.com", "1234567890", newAddress);
//		AccountBean newAccount = getAccountBean(newAccountUsername, "password", AccountRole.CUSTOMER_APP, newUser);
//		
//		AccountBean createdAccount = accountServiceClient.post(newAccount);
//		if (createdAccount == null) {
//			System.out.println("call accountServiceClient.post() failed to add new account");
//			report(createdAccount);
//			System.out.println();
//		} else {
			Boolean isDeleted = accountServiceClient.delete(newAccountUsername);
			
			System.out.println("accountServiceClient.delete():");
			System.out.println(" -- deleted: " + isDeleted);
			System.out.println();

//		}
	}
	
	private void report(AccountBean bean) {
		if (bean == null) {
			System.out.println("-- null account bean --");
		} else {
			System.out.println("-- Account Bean --");
			System.out.println("bean.username = " + bean.getCredentials().getUsername());
			System.out.println("bean.password = " + bean.getCredentials().getPassword());
			System.out.println("bean.role = " + bean.getRole());
			report(bean.getUser());			
		}
	}
	
	private void report(UserBean user) {
		if (user == null) {
			System.out.println("bean.USER = NULL");
		} else {
			System.out.println("bean.USER.id = " + user.getId());
			System.out.println("bean.USER.firtName = " + user.getFirstName());
			System.out.println("bean.USER.lastName = " + user.getLastName());
			System.out.println("bean.USER.email = " + user.getEmail());
			System.out.println("bean.USER.phone = " + user.getPhone());
			report(user.getAddress());
		}

	}
	
	private void report(AddressBean address) {
		if (address == null) {
			System.out.println("bean.user.ADDRESS = NULL");
		} else {
			System.out.println("bean.user.ADDRESS.id = " + address.getId());
			System.out.println("bean.user.ADDRESS.line_1 = " + address.getLine1());
			System.out.println("bean.user.ADDRESS.line_2 = " + address.getLine2());
			System.out.println("bean.user.ADDRESS.city = " + address.getCity());
			System.out.println("bean.user.ADDRESS.state = " + address.getState());
			System.out.println("bean.user.ADDRESS.zipcode = " + address.getZipcode());
		}
	}
	
	private AccountBean getAccountBean(String username, String password, AccountRole role, UserBean user) {
		CredentialsBean credentials = new CredentialsBean();
		credentials.setUsername(username);
		credentials.setPassword(password);
		
		AccountBean bean = new AccountBean();
		bean.setCredentials(credentials);
		bean.setRole(role);
		bean.setUser(user);
		return bean;
	}
	
	private UserBean getUserBean(long id, String fn, String ln, String email, String phn, AddressBean address) {
		UserBean bean = new UserBean();
		bean.setId(id);
		bean.setFirstName(fn);
		bean.setLastName(ln);
		bean.setEmail(email);
		bean.setPhone(phn);
		bean.setAddress(address);
		return bean;
	}
	
	private AddressBean getAddressBean(long id, String l1, String l2, String city, AddressState state, String zipcode) {
		AddressBean bean = new AddressBean();
		bean.setId(id);
		bean.setLine1(l1);
		bean.setLine2(l2);
		bean.setCity(city);
		bean.setState(state);
		bean.setZipcode(zipcode);
		return bean;
	}
	
	private static final String accountUsername = "admin2";
}
