/**
 * Loader for Order bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * Order Bean loader
 * - populate a preparedStatment using data store in an Order bean
 * - populate/recreate a new Order bean using data in a ResultSet
 * 
 * @author Malik
 */
public class OrderBeanLoader implements BeanLoader<OrderBean>{
	private AddressBeanLoader addressLoader;
	private PaymentBeanLoader paymentLoader;
	
	public OrderBeanLoader() {
		addressLoader = new AddressBeanLoader();
		paymentLoader = new PaymentBeanLoader();
	}
	
	/**
	 * return a list of order beans using orders data in the ResultSet (rows)
	 * Empty list is return if the ResultSet is empty
	 * The ResultSet cursor should be positioned before the first row before calling
	 * this method. Otherwise, the first row will not be included in the result.
	 * @param rs a ResultSet containing orders data from the database
	 * @return list of orders
	 */
	@Override
	public List<OrderBean> loadList(ResultSet rs) throws SQLException {
		List<OrderBean> orders = new ArrayList<>();
		while (rs.next())
			orders.add(loadSingle(rs));
	
		return orders;
	}
	
	/**
	 * return an order bean using the ResultSet (a single row)
	 * THIS METHOD SHOULD BE CALLED ONLY WHEN (rs.next() is true before the call).
	 * It expects a ResultSet its cursor pointing at a row
	 * @param rs a ResultSet containing order data from the database
	 * @return order bean object containing the data from an order in the database
	 */
	@Override
	public OrderBean loadSingle(ResultSet rs) throws SQLException {
		OrderType type = OrderType.valueOf(rs.getString(DBLabels.Order.TYPE));
		
		
		OrderBean bean = new OrderBean();
		bean.setId(rs.getLong(DBLabels.Order.ID));	
		bean.setType(type);
		bean.setStatus(OrderStatus.valueOf(rs.getString(DBLabels.Order.STATUS)));
		bean.setConfirmation(rs.getString(DBLabels.Order.CONFIRMATION));
		bean.setTimestamp(rs.getTimestamp(DBLabels.Order.TIMESTAMP));			
		bean.setPayment(paymentLoader.loadSingle(rs));		
		
		if (type == OrderType.DELIVERY) {
			bean.setAddress(addressLoader.loadSingle(rs));
		}

		return bean;
	}

	/**
	 * populate the PreparedStatment with data in the order bean
	 * @param ps preparedStatement with sql string containing at least 5 '?'/placeholders
	 * @param bean order bean with data
	 * @return return the passed ps
	 */
	@Override
	public void loadParameters(PreparedStatement ps, OrderBean bean, int paramIndex) throws SQLException {		
		ps.setString(paramIndex++, bean.getType().name());
		ps.setString(paramIndex++, bean.getStatus().name());
		ps.setString(paramIndex++, bean.getConfirmation());
		ps.setTimestamp(paramIndex++, bean.getTimestamp());		
		ps.setLong(paramIndex++, bean.getPayment().getId());

		AddressBean address = bean.getAddress();
		if (address != null) {
			ps.setLong(paramIndex++, bean.getAddress().getId());
		} else {
			ps.setNull(paramIndex++, java.sql.Types.NULL);		
		}
	}
	
	
	public void loadUpdateParameters(PreparedStatement ps, OrderBean bean, int paramIndex) throws SQLException {		
		ps.setString(paramIndex++, bean.getType().name());
		ps.setString(paramIndex++, bean.getStatus().name());
		ps.setString(paramIndex++, bean.getConfirmation());
		
		AddressBean address = bean.getAddress();
		if (address != null) {
			ps.setLong(paramIndex++, bean.getAddress().getId());
		} else {
			ps.setNull(paramIndex++, java.sql.Types.NULL);		
		}
	}
}
