package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.loaders.MenuItemBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * Menu Data Access Object (DAO)
 * 
 * @author Malik
 */
public class MenuItemDAO {
	private ConnectionFactory connFactory;
	private MenuItemBeanLoader loader;
	
	public MenuItemDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
		this.loader = new MenuItemBeanLoader();
	}
	
	/**
	 * return all (hidden or visible) menu items or empty list
	 * @param isHidden select hidden items?
	 * @return
	 * @throws SQLException
	 */
	public List<MenuItemBean> getAll(boolean isHidden) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MenuItemBean> menu = null;
		
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_SQL);
			ps.setString(1, isHidden? "TRUE" : "FALSE");
			
			rs = ps.executeQuery();
			menu = loader.loadList(rs);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(rs);
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return menu;
	}
	
	/**
	 * return a menu item with the given id or null
	 * @param menuItemId
	 * @return
	 * @throws SQLException
	 */
	public MenuItemBean get(long menuItemId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MenuItemBean menuItem = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID_SQL);
			
			ps.setLong(1, menuItemId);
			rs = ps.executeQuery();
			
			if (rs.next())
				menuItem = loader.loadSingle(rs);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(rs);
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return menuItem;
	}
	
	/**
	 * insert a new menu item
	 * @param menuItem
	 * @return newly added menu item 
	 * @throws SQLException
	 */
	public MenuItemBean add(MenuItemBean menuItem) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		MenuItemBean addedMenuItem = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(INSERT_ITEM_SQL, Statement.RETURN_GENERATED_KEYS);
			
			loader.loadParameters(ps, menuItem, 1);
			
			boolean added = DAOUtil.validInsert(ps.executeUpdate());
			if (added) {
				long menuItemId = DAOUtil.getAutGeneratedKey(ps);
				addedMenuItem = new MenuItemBean(menuItemId, menuItem.getName(), menuItem.getDescription(), menuItem.getPrice(), menuItem.getItemCategory());
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return addedMenuItem;
	}
	
	/**
	 * update a menu item
	 * @param menuItem
	 * @return
	 * @throws SQLException
	 */
	public boolean update(MenuItemBean menuItem) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean updated = false;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(UPADTE_ITEM_SQL);
			
			int paramIndex = 1;
			loader.loadParameters(ps, menuItem, paramIndex);
			ps.setLong(paramIndex + UPDATE_COLUMNS_COUNT, menuItem.getId());
			
			updated = DAOUtil.validUpdate(ps.executeUpdate()); 
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return updated;
	}
	
	/**
	 * delete a menu item
	 * NOTE: deleting menu item is not supported at this time (based on DB design)
	 * Return False ALWAYS.
	 * @param menuItemId
	 * @return false (always)
	 * @throws SQLException
	 */
	public boolean delete(long menuItemId) throws SQLException {
		return false;
	}
	
	public boolean updateIsHidden(long menuItemId, boolean hide) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean updated = false;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(UPDATE_HIDDEN_ITEM_SQL);
			ps.setString(1, (hide? "TRUE" : "FALSE"));
			ps.setLong(2, menuItemId);
			
			updated = DAOUtil.validUpdate(ps.executeUpdate()); 
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return updated;
	}
	
	
	private static final String SELECT_ALL_SQL = String.format("SELECT * FROM %s WHERE (%s = ?) ORDER BY %s", 
																DBLabels.MenuItem.TABLE, DBLabels.MenuItem.IS_HIDDENT, DBLabels.MenuItem.ID);
	
	private static final String SELECT_BY_ID_SQL = String.format("SELECT * FROM %s WHERE (%s = ?)", 
																DBLabels.MenuItem.TABLE, DBLabels.MenuItem.ID);
	
	private static final String INSERT_ITEM_SQL = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?)",  
																DBLabels.MenuItem.TABLE,  DBLabels.MenuItem.NAME,
																DBLabels.MenuItem.DESC,  DBLabels.MenuItem.PRICE,
																DBLabels.MenuItem.CATEGORY);
	
	private static final String UPADTE_ITEM_SQL = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE (%s = ?)",
																DBLabels.MenuItem.TABLE,  DBLabels.MenuItem.NAME,
																DBLabels.MenuItem.DESC,  DBLabels.MenuItem.PRICE,
																DBLabels.MenuItem.CATEGORY, DBLabels.MenuItem.ID);
	
	private static final String UPDATE_HIDDEN_ITEM_SQL = String.format("UPDATE %s SET %s=? WHERE (%s = ?)",
																DBLabels.MenuItem.TABLE,  DBLabels.MenuItem.IS_HIDDENT,
																DBLabels.MenuItem.ID);


	private static final int UPDATE_COLUMNS_COUNT = 4;
}
