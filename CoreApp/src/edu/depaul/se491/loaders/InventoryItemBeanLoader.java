/**
 * Loader for InventoryItem bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.depaul.se491.beans.InventoryItemBean;

/**
 * @author Malik
 *
 */
public class InventoryItemBeanLoader implements BeanLoader<InventoryItemBean> {

	@Override
	public List<InventoryItemBean> loadList(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InventoryItemBean loadSingle(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadParameters(PreparedStatement ps, InventoryItemBean bean, int paramIndex) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
