package edu.depaul.se491.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;

/**
 * Build Database tables
 * 
 * @author Malik
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
	
	/**
	 * construct DBBuilder
	 * uses ConnectionFactory for database connections
	 * @param connFactory
	 */
	public DBBuilder(ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}

	/**
	 * drop (if exists) and create database tables
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void rebuildAll() throws FileNotFoundException, IOException, SQLException {
		DBBuilder dbBuilder = new DBBuilder(connFactory);
		dbBuilder.dropTables();
		dbBuilder.createTables();
	}

	/**
	 * drop tables
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	private void dropTables() throws FileNotFoundException, IOException, SQLException {
		String filePath = "sql/dropTables.sql";
		DBUtil.executeSQLFile(connFactory ,filePath);
	}

	/**
	 * create tables
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	private void createTables() throws FileNotFoundException, IOException, SQLException {
		String filePath = "sql/createTables.sql";
		DBUtil.executeSQLFile(connFactory ,filePath);
	}
}
