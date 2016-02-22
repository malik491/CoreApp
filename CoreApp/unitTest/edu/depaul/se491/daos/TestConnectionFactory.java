/**
 *  Database Connection Driver for Testing
 *  package private
 */
package edu.depaul.se491.daos;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import edu.depaul.se491.daos.ConnectionFactory;

/**
 * @author Malik
 *
 */
public class TestConnectionFactory implements ConnectionFactory {
	private static ConnectionFactory instance;
	
	private String driverClassName;
	private String username;
	private String password;
	private String url;
	private BasicDataSource dataSource;
		
	/**
	 * return a test connection factory instance
	 * @return
	 */
	public synchronized static ConnectionFactory getInstance() {
		if (instance == null)
			instance = new TestConnectionFactory();
		
		return instance;
	}
	
		
	private TestConnectionFactory() {
		InputStream inputStream = null;
		try {
			// read config file
			Properties prop = new Properties();
			String propFileName = "testDBconfig.properties";
			inputStream = TestConnectionFactory.class.getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
	
				driverClassName = prop.getProperty("driverClassName");
				username = prop.getProperty("username");
				password = prop.getProperty("password");
				url = prop.getProperty("url");
				
				if (driverClassName == null || username == null || password == null || url == null) {
					driverClassName = username = password = url = null;
					System.err.println("Missing some properties for the TestDB configuration");
				}
				
				dataSource = new BasicDataSource();
				dataSource.setDriverClassName(driverClassName);
				dataSource.setUsername(username);
				dataSource.setPassword(password);
				dataSource.setUrl(url);
				dataSource.setMaxTotal(5); // only allow 5 active connections at the same time
				dataSource.setMaxIdle(5);  // only allow 5 idle connections at all time
				dataSource.setMaxWaitMillis(500); //wait max 500 ms for a connection to be returned to the pool before throwing an exception
				dataSource.setDefaultAutoCommit(true);

			} else {
				driverClassName = username = password = url = null;
				System.err.println("TestDB configuration file '" + propFileName + "' not found");
			}
		} catch (IOException e) {
			driverClassName = username = password = url = null;
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();				
				}
			}			
		}
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource != null? dataSource.getConnection() : null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}


	@Override
	public void close() throws SQLException {
		synchronized(this) {
			if (instance != null) {
				dataSource.close();
				dataSource = null;
				instance = null;
			}
			
		}
	}
}
