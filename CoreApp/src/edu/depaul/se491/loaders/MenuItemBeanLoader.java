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
 * MenuItemBean Loader
 * 
 * @author Malik
 */
public class MenuItemBeanLoader {

	/**
	 * return all MenuItemBeans in the result-set or empty list
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public List<MenuItemBean> loadList(ResultSet rs) throws SQLException {
		List<MenuItemBean> mItems = new ArrayList<MenuItemBean>();
		while (rs.next())
			mItems.add(loadSingle(rs));
		return mItems;
	}

	/**
	 * return a single MenuItemBean in the result-set
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
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
	 * load parameters from MenuItemBean into the given PreparedStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @throws SQLException
	 */
	public void loadParameters(PreparedStatement ps, MenuItemBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getName());
		ps.setString(paramIndex++, bean.getDescription());
		ps.setDouble(paramIndex++, bean.getPrice());
		ps.setString(paramIndex++, bean.getItemCategory().name());
	}
}
