package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.ExceptionConnectionFactory;
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
		testDataGen.generateData();
		
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
		testDataGen.generateData();
	}

	@Test
	public void testGetAll() throws SQLException {
		List<MenuItemBean> items = null;
		
		items = menuItemDAO.getAll(false);
		assertNotNull(items);
		assertEquals(6, items.size());
		for (MenuItemBean item : items)
			assertNotNull(item);
		
		items = menuItemDAO.getAll(true);
		assertNotNull(items);
		assertEquals(1, items.size());
		assertNotNull(items.get(0));
		
	}
	

	@Test
	public void testGet() throws SQLException {
		MenuItemBean menuItem = null;
		
		menuItem = menuItemDAO.get(1L);
		assertNotNull(menuItem);
		
		assertEquals(1L, menuItem.getId());
		assertEquals("Soda", menuItem.getName());
		assertEquals("cold and refreshing soda", menuItem.getDescription());
		assertEquals(0, Double.compare(1.99, menuItem.getPrice()));
		assertEquals(MenuItemCategory.BEVERAGE, menuItem.getItemCategory());
		
		// no menu item with id 100
		assertNull(menuItemDAO.get(100));
	}

	@Test
	public void testAdd() throws SQLException {
		MenuItemBean item = new MenuItemBean(0, "new item", "description", 5.01, MenuItemCategory.MAIN);
		
		MenuItemBean addedItem = menuItemDAO.add(item);
		assertNotNull(addedItem);
		
		long expectedId = 8L;
		assertEquals(expectedId, addedItem.getId());
		assertEquals(item.getName(), addedItem.getName());
		assertEquals(item.getDescription(), addedItem.getDescription());
		assertEquals(0, Double.compare(item.getPrice(), addedItem.getPrice()));
		assertEquals(item.getItemCategory(), addedItem.getItemCategory());
		
	}

	@Test
	public void testUpdate() throws SQLException {
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
	public void testDelete() throws SQLException {
		boolean deleted = menuItemDAO.delete(1L);
		assertFalse(deleted);
	}
	
	@Test
	public void testUpdateIsHidden() throws SQLException {
		boolean updated = menuItemDAO.updateIsHidden(1L, true);
		assertTrue(updated);
		
		updated = menuItemDAO.updateIsHidden(1L, false);
		assertTrue(updated);
	}
	
	@Test
	public void testExceptions() {
		MenuItemDAO dao = new TestDAOFactory(new ExceptionConnectionFactory()).getMenuItemDAO();
		try {
			dao.get(1L);
			fail("No Exception Thrown");
		} catch (Exception e) {}

		try {
			dao.add(new MenuItemBean());
			fail("No Exception Thrown");			
		} catch (Exception e) {}
		
		try {
			dao.getAll(false);
			fail("No Exception Thrown");
		} catch (Exception e) {}
		
		try {
			dao.getAll(true);
			fail("No Exception Thrown");
		} catch (Exception e) {}

		try {
			dao.update(new MenuItemBean());
			fail("No Exception Thrown");
		} catch (Exception e) {}
		
		try {
			dao.updateIsHidden(1L, true);
			fail("No Exception Thrown");
		} catch (Exception e) {}

		try {
			dao.updateIsHidden(1L, false);
			fail("No Exception Thrown");
		} catch (Exception e) {}

		assertTrue(true);
	}

}
