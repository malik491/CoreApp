/**
 * Build Database
 * 
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
public class DBBuilder {
	private ConnectionFactory connFactory;
	
	
	public static void main(String[] args) {
		ConnectionFactory connFactory = TestConnectionFactory.getInstance();
		DBBuilder dbBuilder = new DBBuilder(connFactory);
		try {
			dbBuilder.rebuildAll();
			connFactory.close();
			System.out.println("Successfully created database (se491)");
		} catch (IOException | SQLException e) {
			System.out.println("Failed to create database (se491). See stack trace.");
			e.printStackTrace();
		}
	}
	
	public DBBuilder(ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}

	public void rebuildAll() throws FileNotFoundException, IOException, SQLException {
		DBBuilder dbBuilder = new DBBuilder(connFactory);
		dbBuilder.dropTables();
		dbBuilder.createTables();
	}

	public void dropTables() throws FileNotFoundException, IOException, SQLException {
		String filePath = "sql/dropTables.sql";
		DBUtil.executeSQLFile(connFactory ,filePath);
	}

	public void createTables() throws FileNotFoundException, IOException, SQLException {
		String filePath = "sql/createTables.sql";
		DBUtil.executeSQLFile(connFactory ,filePath);
	}
}
