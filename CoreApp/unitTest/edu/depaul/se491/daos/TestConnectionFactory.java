/**
 *  Database Connection Driver for Testing
 *  package private
 */
package edu.depaul.se491.daos;

import java.io.FileNotFoundException;
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
	private static BasicDataSource dataSource;
	private boolean isInitialized;
	
	/**
	 * return a test connection factory instance
	 * @return
	 */
	public static ConnectionFactory getInstance() {
		if (instance == null)
			instance = new TestConnectionFactory();
		return instance;
	}
	
	
	private TestConnectionFactory() {
		isInitialized = false;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if (!isInitialized)
			lazyInitialize();
		return dataSource.getConnection();
	}

	private void lazyInitialize() {
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			String propFileName = "testDBconfig.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
	
				String driverClassName = prop.getProperty("driverClassName");
				String username = prop.getProperty("username");
				String password = prop.getProperty("password");
				String url = prop.getProperty("url");
				
				if (driverClassName == null || username == null || password == null || url == null)
					throw new IOException("Missing some properties for the TestDB configuration");
				
				// database driver
				dataSource = new BasicDataSource();
				dataSource.setDriverClassName(driverClassName);
				dataSource.setUsername(username);
				dataSource.setPassword(password);
				dataSource.setUrl(url);
				dataSource.setMaxTotal(10); // only allow three connections open at a time
				dataSource.setMaxWaitMillis(250); // wait 250ms until throwing an exception
				dataSource.setPoolPreparedStatements(true);
				
				isInitialized = true;
			} else {
				throw new FileNotFoundException("TestDB configuration file '" + propFileName + "' not found");
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
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

}
