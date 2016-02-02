/**
 * Populate database with data
 */
package edu.depaul.se491.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;

/**
 * @author Malik
 *
 */
public class TestDataGenerator {
	private ConnectionFactory connFactory;
	private String dataDIR;

	public static void main(String[] args) {
		TestDataGenerator gen = new TestDataGenerator();
		
		try {
			gen.generateStandardData();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	
	public TestDataGenerator() {
		this.connFactory = TestConnectionFactory.getInstance();
		this.dataDIR = "sql/test_data";
	}

	public TestDataGenerator(ConnectionFactory connFactory, String projectHome) {
		this.connFactory = connFactory;
		this.dataDIR = projectHome + "/sql/test_data";
	}
		
	/**
	 * create standard data for testing
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	public void generateStandardData() throws FileNotFoundException, SQLException, IOException {
		// order of method calls matters (Functional Dependency)

		generateMenuItems();
		
		generateAddresses();
		generateUsers();    	// after Addresss
		generateAccounts(); 	// after Users

		
		generatePayments();
		generateOrders();     	// after Addresses and Payments
		generateOrderItems(); 	// after Orders and MenuItems
				
	}	
	
	public void generateMenuItems() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/menuItems/insert.sql");
	}

	public void generateAddresses() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/addresses/insert.sql");
	}
	
	public void generateUsers() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/users/insert.sql");
	}
	
	public void generateAccounts() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/accounts/insert.sql");
	}

	public void generatePayments() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/payments/insert.sql");
	}

	public void generateOrders() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/orders/insert.sql");
	}
	
	public void generateOrderItems() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/orderItems/insert.sql");
	}
	
	
	private void executeSQLFile(String filePath) throws FileNotFoundException, SQLException, IOException {
		DBUtil.executeSQLFile(connFactory, filePath);
	}
}
