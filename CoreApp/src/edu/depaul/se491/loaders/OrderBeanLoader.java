package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * OrderBean Loader
 * 
 * @author Malik
 */
public class OrderBeanLoader {
	private AddressBeanLoader addressLoader;
	private PaymentBeanLoader paymentLoader;
	
	/**
	 * construct OrderBeanLoader
	 */
	public OrderBeanLoader() {
		addressLoader = new AddressBeanLoader();
		paymentLoader = new PaymentBeanLoader();
	}
	
	/**
	 * return a list of all orders in the result-set or empty list
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public List<OrderBean> loadList(ResultSet rs) throws SQLException {
		List<OrderBean> orders = new ArrayList<>();
		while (rs.next())
			orders.add(loadSingle(rs));
	
		return orders;
	}
	
	/**
	 * return a single OrderBean in the result-set
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
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
	 * load parameters from OrderBean into the given PreparedStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @throws SQLException
	 */
	public void loadParameters(PreparedStatement ps, OrderBean bean, int paramIndex) throws SQLException {		
		ps.setString(paramIndex++, bean.getType().name());
		ps.setString(paramIndex++, bean.getStatus().name());
		ps.setString(paramIndex++, bean.getConfirmation());
		ps.setTimestamp(paramIndex++, bean.getTimestamp());		
		ps.setLong(paramIndex++, bean.getPayment().getId());

		if (bean.getType() == OrderType.DELIVERY) {
			ps.setLong(paramIndex++, bean.getAddress().getId());
		} else {
			ps.setNull(paramIndex++, java.sql.Types.NULL);		
		}
	}
}
