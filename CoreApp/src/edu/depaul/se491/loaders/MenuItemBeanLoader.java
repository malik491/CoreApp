/**
 * Loader for MenuItem bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.utils.dao.DBLabels;


/**
 * MenuItem Bean loader
 * - populate a preparedStatment using data store in a MenuItem bean
 * - populate/recreate a new MenuItem bean using data in a ResultSet
 * 
 * @author Malik
 */
public class MenuItemBeanLoader implements BeanLoader<MenuItemBean> {

	/**
	 * return a list of menuItem beans using menuItems data in the ResultSet (rows)
	 * Empty list is return if the ResultSet is empty
	 * The ResultSet cursor should be positioned before the first row before calling
	 * this method. Otherwise, the first row will not be included in the result.
	 * @param rs a ResultSet containing menu items data from the database
	 * @return list of menu items
	 */
	@Override
	public List<MenuItemBean> loadList(ResultSet rs) throws SQLException {
		List<MenuItemBean> mItems = new ArrayList<MenuItemBean>();
		while (rs.next())
			mItems.add(loadSingle(rs));
		return mItems;
	}


	/**
	 * return an menuItem bean using the ResultSet (a single row)
	 * THIS METHOD SHOULD BE CALLED ONLY WHEN (rs.next() is true before the call).
	 * It expects a ResultSet its cursor pointing at a row
	 * @param rs a ResultSet containing menuItem data from the database
	 * @return MenuItem bean object containing the data from a menuItem in the database
	 */
	@Override
	public MenuItemBean loadSingle(ResultSet rs) throws SQLException {
		MenuItemBean bean = new MenuItemBean();
		
		bean.setId(rs.getLong(DBLabels.MenuItem.ID));
		bean.setName(rs.getString(DBLabels.MenuItem.NAME));
		bean.setDescription(rs.getString(DBLabels.MenuItem.DESC));
		bean.setPrice(rs.getDouble(DBLabels.MenuItem.PRICE));
		bean.setItemCategory(MenuItemCategory.valueOf(rs.getString(DBLabels.MenuItem.CATEGORY)));
		
		return bean;
	}

	/**
	 * populate the PreparedStatment with data in the menuItem bean
	 * @param ps preparedStatement with sql string containing at least 3 '?'/placeholders
	 * @param bean menuItem bean with data
	 * @return return the passed ps
	 */
	@Override
	public void loadParameters(PreparedStatement ps, MenuItemBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getName());
		ps.setString(paramIndex++, bean.getDescription());
		ps.setDouble(paramIndex++, bean.getPrice());
		ps.setString(paramIndex++, bean.getItemCategory().name());
	}
}
