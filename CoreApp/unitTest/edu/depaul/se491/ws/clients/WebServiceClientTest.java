/**
 * 
 */
package edu.depaul.se491.ws.clients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.depaul.se491.daos.TestConnectionFactory;

/**
 * @author Malik
 *
 */
class WebServiceClientTest {
	protected String BASE_WEB_SERVICES_URL = "";
	
	protected WebServiceClientTest() {
		InputStream inputStream = null;
		try {
			// read config file
			Properties prop = new Properties();
			String propFileName = "testWSClientConfig.properties";
			inputStream = TestConnectionFactory.class.getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);

				String hostname = prop.getProperty("hostname");
				String port = prop.getProperty("port");
				String appname = prop.getProperty("appname");
				
				if (hostname == null || port == null || appname == null) {
					System.err.println("Missing some properties for testWSClientConfig configuration file");
				} else {
					BASE_WEB_SERVICES_URL = String.format("http://%s:%s/%s", hostname, port, appname);
				}
			} else {
				System.err.println("TestWSClient configuration file '" + propFileName + "' not found");
			}
		} catch (IOException e) {
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
