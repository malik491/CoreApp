package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.enums.OrderItemStatus;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * OrderItemBean Loader
 *
 * @author Malik
 */
public class OrderItemBeanLoader {
	private MenuItemBeanLoader loader;
	
	/**
	 * construct OrderItemBeanLoader
	 */
	public OrderItemBeanLoader() {
		loader = new MenuItemBeanLoader();
	}
	
	/**
	 * return a list of all OrderItemBeans in the result-set or empty list
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public OrderItemBean[] loadList(ResultSet rs) throws SQLException {
		List<OrderItemBean> oItems = new ArrayList<>();
		while (rs.next())
			oItems.add(loadSingle(rs));
		
		return oItems.toArray(new OrderItemBean[oItems.size()]);
	}

	/**
	 * return a single OrderItemBean in the result-set
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public OrderItemBean loadSingle(ResultSet rs) throws SQLException {
		OrderItemBean bean = new OrderItemBean();
		
		bean.setMenuItem(loader.loadSingle(rs));
		bean.setQuantity(rs.getInt(DBLabels.OrderItem.QUANTITY));
		bean.setStatus(OrderItemStatus.valueOf(rs.getString(DBLabels.OrderItem.STATUS)));

		return bean;
	}

	/**
	 * load parameters from OrderItemBean into the given PreparedStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @param orderId
	 * @throws SQLException
	 */
	public void loadParameters(PreparedStatement ps, OrderItemBean bean, int paramIndex, long orderId) throws SQLException {
		ps.setLong(paramIndex++, orderId);
		ps.setLong(paramIndex++, bean.getMenuItem().getId());
		ps.setLong(paramIndex++, bean.getQuantity());
		ps.setString(paramIndex++, bean.getStatus().name());

	}
}
