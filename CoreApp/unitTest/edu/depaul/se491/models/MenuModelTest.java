package edu.depaul.se491.models;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;
import edu.depaul.se491.utils.ParamLengths;

public class MenuModelTest {
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
	public void testMenuModelDAOFactoryCredentialsBean() {
		CredentialsBean cBean = new CredentialsBean("manager", "password");
		MenuModel mModel = new MenuModel(daoFactory, cBean);
		assertNotNull(mModel);
	}

	@Test
	public void testCreate() {
		
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			MenuModel model = new MenuModel(daoFactory, credentials);
			
			MenuItemBean mItem = new MenuItemBean(0, "name", "description", 2.5, MenuItemCategory.BEVERAGE);
			
			MenuItemBean createdItem = model.create(mItem);
			if (username.equals("manager")) {
				assertNotNull(createdItem);
				
				// test invalid menu item data (id != 0)
				mItem.setId(100L);
				createdItem = model.create(mItem);
				assertNull(createdItem);
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
			} else {
				assertNull(createdItem);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			}
		}		
	}

	@Test
	public void testUpdate() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			MenuModel model = new MenuModel(daoFactory, credentials);
			MenuItemBean mItem = new MenuItemBean(1, "updated name", " updated description", 2.5, MenuItemCategory.BEVERAGE);

			Boolean updated = model.update(mItem);
			if (username.equals("manager")) {
				assertNotNull(updated);
				assertTrue(updated);
				
				// test invalid menu item data (id != 0)
				StringBuilder sb = new StringBuilder();
				for(int i=0; i < ParamLengths.MenuItem.MAX_NAME + 1; i++)
					sb.append("x");
				mItem.setName(sb.toString());
				
				updated = model.update(mItem);
				assertNull(updated);
				assertEquals(Response.Status.BAD_REQUEST, model.getResponseStatus());
			} else {
				assertNull(updated);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			}
		}
	}

	@Test
	public void testRead() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			MenuModel model = new MenuModel(daoFactory, credentials);
			
			MenuItemBean menuItem = model.read(1L);
			if (username.equals("manager") || username.equals("employee1")) {
				assertNotNull(menuItem);
				
				// test menu item not found
				menuItem = model.read(100L);
				assertNull(menuItem);
				assertEquals(Response.Status.NOT_FOUND, model.getResponseStatus());
			} else {
				assertNull(menuItem);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			}
		}
	}

	@Test
	public void testReadAllVisible() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			MenuModel model = new MenuModel(daoFactory, credentials);
			
			List<MenuItemBean> visibleMenuItems = model.readAllVisible();
			if (username.equals("admin")) {
				assertNull(visibleMenuItems);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			} else {
				assertNotNull(visibleMenuItems);
				assertEquals(6, visibleMenuItems.size());
			}
		}
	}
	
	@Test
	public void testReadAllHidden() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			MenuModel model = new MenuModel(daoFactory, credentials);
			
			List<MenuItemBean> hiddenMenuItems = model.readAllHidden();
			if (username.equals("admin")) {
				assertNull(hiddenMenuItems);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			} else {
				assertNotNull(hiddenMenuItems);
				assertTrue(hiddenMenuItems.size() == 1);
			}
		}
	}

	@Test
	public void testDelete() {
		for (String username : new String[]{"admin", "manager", "employee1", "customerapp"}) {
			CredentialsBean credentials = new CredentialsBean(username, "password");
			MenuModel model = new MenuModel(daoFactory, credentials);
			
			Boolean deleted = model.delete(2L);
			if (username.equals("manager")) {
				assertNotNull(deleted);
				assertFalse(deleted); // we don't delete menu items
			} else {
				assertNull(deleted);
				assertEquals(Response.Status.UNAUTHORIZED, model.getResponseStatus());
			}
		}
		
	}

}
