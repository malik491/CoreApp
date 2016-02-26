package edu.depaul.se491.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;

/**
 * Populate database with data
 * 
 * @author Malik
 */
public class TestDataGenerator {
	private ConnectionFactory connFactory;
	private String dataDIR;

	public static void main(String[] args) {
		ConnectionFactory connFactory = TestConnectionFactory.getInstance();
		TestDataGenerator gen = new TestDataGenerator(connFactory);
		
		try {
			gen.generateData();
			connFactory.close();
			System.out.println("Successfully populated database (se491)");
		} catch (SQLException | IOException e) {
			System.out.println("Failed to populate database (se491). See stack trace.");
			e.printStackTrace();
		}
	}
	
	/**
	 * construct TestDataGenerator
	 * Uses ConnectionFactory for database connection
	 * Uses the following default base path to read SQL files:
	 * [project home]/sql/test_data/
	 * @param connFactory
	 */
	public TestDataGenerator(ConnectionFactory connFactory) {
		this.connFactory = connFactory;
		this.dataDIR = "sql/test_data";
	}

	/**
	 * construct TestDataGenerator
	 * Uses ConnectionFactory for database connection
	 * Uses the following base path to read SQL files:
	 * projectHome/sql/test_data/
	 * @param connFactory
	 * @param projectHome
	 */
	public TestDataGenerator(ConnectionFactory connFactory, String projectHome) {
		this.connFactory = connFactory;
		this.dataDIR = projectHome + "/sql/test_data";
	}
		
	/**
	 * populate database with data in the SQL files for testing
	 * base folder: [project home]/sql/test_data/
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	public void generateData() throws FileNotFoundException, SQLException, IOException {
		// order of method calls matters (Functional Dependency)

		generateMenuItems();
		
		generateAddresses();
		generateUsers();
		generateAccounts();

		
		generatePayments();
		generateOrders();
		generateOrderItems();
				
	}	
	
	private void generateMenuItems() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/menuItems/insert.sql");
	}

	private void generateAddresses() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/addresses/insert.sql");
	}
	
	private void generateUsers() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/users/insert.sql");
	}
	
	private void generateAccounts() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/accounts/insert.sql");
	}

	private void generatePayments() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/payments/insert.sql");
	}

	private void generateOrders() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/orders/insert.sql");
	}
	
	private void generateOrderItems() throws FileNotFoundException, SQLException, IOException {
		executeSQLFile(dataDIR + "/orderItems/insert.sql");
	}
	
	private void executeSQLFile(String filePath) throws FileNotFoundException, SQLException, IOException {
		DBUtil.executeSQLFile(connFactory, filePath);
	}
}
