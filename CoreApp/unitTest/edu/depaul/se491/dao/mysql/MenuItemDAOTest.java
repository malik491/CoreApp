package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.daos.mysql.MenuItemDAO;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * 
 * @author Malik
 *
 */
public class MenuItemDAOTest {
	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	private static MenuItemDAO menuItemDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		
		menuItemDAO = new TestDAOFactory(connFactory).getMenuItemDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// rebuild to original state
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release resources
		dbBuilder = null;
		testDataGen = null;
		menuItemDAO = null;
		
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
	public void testGetAll() {
		List<MenuItemBean> items = null;
		
		items = menuItemDAO.getAll();
		assertNotNull(items);
		assertTrue(items.size() > 1);
		assertNotNull(items.get(0));
		assertEquals(1L, items.get(0).getId());		
	}

	@Test
	public void testGet() {
		MenuItemBean menuItem = null;
		
		menuItem = menuItemDAO.get(1L);
		assertNotNull(menuItem);
		
		assertEquals(1L, menuItem.getId());
		assertEquals("Soda", menuItem.getName());
		assertEquals("cold and refreshing soda", menuItem.getDescription());
		assertEquals(0, Double.compare(1.99, menuItem.getPrice()));
		assertEquals(MenuItemCategory.BEVERAGE, menuItem.getItemCategory());
	}

	@Test
	public void testAdd() {
		MenuItemBean item = new MenuItemBean(0, "new item", "description", 5.01, MenuItemCategory.MAIN);
		
		MenuItemBean addedItem = menuItemDAO.add(item);
		assertNotNull(addedItem);
		
		long expectedId = 7L;
		assertEquals(expectedId, addedItem.getId());
		assertEquals(item.getName(), addedItem.getName());
		assertEquals(item.getDescription(), addedItem.getDescription());
		assertEquals(0, Double.compare(item.getPrice(), addedItem.getPrice()));
		assertEquals(item.getItemCategory(), addedItem.getItemCategory());
		
	}

	@Test
	public void testUpdate() {
		MenuItemBean oldItem = menuItemDAO.get(1L);
		oldItem.setName("updated name");
		oldItem.setDescription("updated description");
		oldItem.setPrice(0.50);
		oldItem.setItemCategory(MenuItemCategory.SIDE);
		
		
		boolean updated = menuItemDAO.update(oldItem);
		assertTrue(updated);
		
		MenuItemBean updatedItem = menuItemDAO.get(1L);
		assertNotNull(updatedItem);
		
		assertEquals(oldItem.getId(), updatedItem.getId());
		assertEquals(oldItem.getName(), updatedItem.getName());
		assertEquals(oldItem.getDescription(), updatedItem.getDescription());
		assertEquals(0, Double.compare(oldItem.getPrice(), updatedItem.getPrice()));
		assertEquals(oldItem.getItemCategory(), updatedItem.getItemCategory());
	}

	@Test
	public void testDelete() {
		boolean deleted = menuItemDAO.delete(1L);
		assertFalse(deleted);
	}

}
