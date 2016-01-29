/**
 * Order Data Access Object (DAO)
 * 
 */
package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.loaders.OrderBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;

/**
 * @author Malik
 *
 */
public class OrderDAO {
	private ConnectionFactory connFactory;
	private OrderBeanLoader loader;
	private AddressDAO addressDAO;
	private OrderItemDAO orderItemDAO;

	public OrderDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
		this.addressDAO = daoFactory.getAddressDAO();
		this.orderItemDAO = daoFactory.getOrderItemDAO();
		this.loader = new OrderBeanLoader();
	}
	
	/**
	 * return all orders in the database
	 * Empty list is returned if there are no orders in the database
	 * @return
	 * @throws SQLException
	 */
	public List<OrderBean> getAll() throws DBException {
		return getMultiple(SELECT_ALL_WITH_ORDER_QUERY, null);
	}
	
	/**
	 * return all orders with the given order status in the database
	 * Empty list is returned if there are no orders for the given status in the database
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public List<OrderBean> getAllWithStatus(final OrderStatus status) throws DBException {
		return getMultiple(SELECT_BY_STATUS_QUERY, status.toString());
	}
	
	/**
	 * return all orders with the given order type in the database
	 * Empty list is returned if there are no orders for the given type in the database
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<OrderBean> getAllWithType(final OrderType type) throws DBException {
		return getMultiple(SELECT_BY_TYPE_QUERY, type.toString());
	}
	
	/**
	 * return order associated with the given id
	 * Null is returned if there are no order for the given id
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public OrderBean get(final long orderId) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		OrderBean order = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID_QUERY);
			
			ps.setLong(1, orderId);
			
			rs = ps.executeQuery();
			if (rs.next())
				order = loader.loadSingle(rs);
			
			if (order != null)
				order.setItems(orderItemDAO.getOrderItems(order.getId()));
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(rs);
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return order;		
	}
	
	/**
	 * return order by its confirmation number
	 * Null is returned if there are no order with the given confirmation number
	 * @param orderConfirmation
	 * @return
	 * @throws SQLException
	 */
	public OrderBean get(final String orderConfirmation) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderBean order = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_BY_CONFIRMATION_QUERY);
			
			ps.setString(1, orderConfirmation);
			rs = ps.executeQuery();
			
			if (rs.next())
				order = loader.loadSingle(rs);
			
			if (order != null)
				order.setItems(orderItemDAO.getOrderItems(order.getId()));
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(rs);
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return order;		
	}
		
	/**
	 * add order to the database using the data in the orderBean
	 * @param order order data (excluding the id)
	 * @return true if order is added
	 * @throws SQLException
	 */
	public OrderBean add(final OrderBean order) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		OrderBean result = null;
		try {
			boolean added = false;
			OrderBean addedOrder = null;// new DeliveryOrderBuilder(order).build(); // copied so not to modify the caller's order
			
			/* Transaction :
			 * - add address (if any)
			 * - add order
			 * - add order items
			 */
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(INSERT_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS);
			
			// handle address
			final AddressBean address = null;// order.getDeliveryAddress();
			
			if (address == null) {
				added = true;
			} else {
				AddressBean addedAddress = addressDAO.transactionAdd(conn, address);
				added = (addedAddress != null);
				//addedOrder.setDeliveryAddress(addedAddress);	
			}
			
			if (added) {
				loader.loadParameters(ps, addedOrder, 1);
				added = DAOUtil.validInsert(ps.executeUpdate());
			}
			
			if (added) {
				addedOrder.setId(DAOUtil.getAutGeneratedKey(ps));
				added = orderItemDAO.transactionAdd(conn, addedOrder);				
			}
			
			if (added) {
				// commit the transaction
				conn.commit();
				result = addedOrder;				
			} else {
				// rollback
				conn.rollback();
			}			

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.setAutoCommit(conn, true);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return result;
	}
	
	

	/**
	 * delete an existing order from the database
	 * @param order
	 * @return true if an order is deleted
	 * @throws SQLException
	 */
	public boolean delete(final long orderId) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean deleted = false;
		
		try {
			OrderBean order = get(orderId);
			if (order == null)
				return false;
			
			/*
			 * Transaction:
			 * - delete order items
			 * - delete order
			 * - delete address (if any)
			 */
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement(DELETE_ORDER_QUERY);
			ps.setLong(1, orderId);

			// delete order items first (have foreign key to order)
			deleted = orderItemDAO.transactionDelete(conn, orderId, order.getItems().size());

			// delete order (has foreign key to address)
			if (deleted)
				deleted = DAOUtil.validDelete(ps.executeUpdate());

			// finally you can delete the address
			if (deleted) {
				AddressBean address = null;//order.getDeliveryAddress();
				deleted = address != null? addressDAO.transactionDelete(conn, address.getId()) : true;
			}
			
			if (deleted) {
				// commit
				conn.commit();				
			} else {
				/*try to rollback*/
				conn.rollback();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.setAutoCommit(conn, true);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return deleted;
	}
	
		
	/**
	 * Update an existing order with new data in the orderBean.
	 * It updates all order fields except orderId
	 * For order items, only quantity is updated.
	 * @param order updated order
	 * @return true if order is updated
	 * @throws SQLException
	 */
	public boolean update(final OrderBean updatedOrder) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;		
		boolean updated = false;
		try {
			final long orderId = updatedOrder.getId();
			final OrderBean oldOrder = get(orderId);
			if (oldOrder == null)
				return false;

			/*
			 * transaction:
			 * - update order items
			 * - update order and order address
			 */
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(UPDATE_ORDER_QUERY);

			
			final AddressBean oldAddr = null;//oldOrder.getDeliveryAddress();
			final AddressBean updatedAddr = null;//updatedOrder.getDeliveryAddress();
			
			boolean isNullOldAddr = (oldAddr == null);
			boolean isNullNewAddr = (updatedAddr == null);
			boolean deleteOldAddr = false;
			
			if ( isNullOldAddr && isNullNewAddr) {
				// both addresses are null, no address to update
				updated = true;	
			} else {
				// some address or both are not null
				
				if (isNullOldAddr && !isNullNewAddr) {
					// add a new delivery address to a saved order with no address
					
					AddressBean addedAddr = addressDAO.transactionAdd(conn, updatedAddr);
					updated = (addedAddr != null);
					//if (updated)
					//	updatedOrder.setDeliveryAddress(addedAddr);
					
				} else if (!isNullOldAddr && isNullNewAddr) {
					// remove a saved old address for this order (the updated order basically removed the old address)
					// we can't delete it now/here (we must wait until the order.address_id (foreign key) is set to NULL first.
					
					updated = true;
					deleteOldAddr = true;
				
				} else {
					// both addresses not null so both must have the same id. If that's the case, then update the saved address data
				
					updated = (oldAddr.getId() == updatedAddr.getId());
					if (updated) {
						boolean isDifferent = hasDifferentData(oldAddr, updatedAddr);
						updated = isDifferent? addressDAO.transactionUpdate(conn, updatedAddr) : true;
					}
				}
								
			}
		
			// update order items
			if (updated)
				updated = updateOrderItems(conn, oldOrder, updatedOrder);
						
			// update order
			if (updated) {
				int paramIndex = 1;
				loader.loadParameters(ps, updatedOrder, paramIndex);
				ps.setLong(paramIndex + ORDER_COLUMNS_COUNT, orderId);
				
				updated = DAOUtil.validUpdate(ps.executeUpdate());

				// if this update deletes an old address, we can only do it now (after setting the order's address_id (foreign key) to NULL)
				if (updated && deleteOldAddr)
					updated = addressDAO.transactionDelete(conn, oldAddr.getId());

			}
				
			if (updated) {
				// commit
				conn.commit();				
			} else {
				conn.rollback();				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.setAutoCommit(conn, true);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return updated;
	}
	
	/**
	 * return multiple orders from the database based on the select sqlQuery and parameter value
	 * @param sqlQuery select query with optional WHERE clause '... [WHERE columnName = ?]'
	 * @param paramValue param value for the condition in WHERE clause, if present in the sqlQuery
	 * @return
	 * @throws SQLException
	 */
	private List<OrderBean> getMultiple(final String selectSQLQuery, final String paramValue) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderBean> orders = null;
		
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(selectSQLQuery);
			
			if (paramValue != null)
				ps.setString(1, paramValue);

			rs = ps.executeQuery();
			orders = loader.loadList(rs);
			
			for (OrderBean order: orders)
				order.setItems(orderItemDAO.getOrderItems(order.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(rs);
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return orders;
	}
	
	
	private boolean updateOrderItems(final Connection conn, final OrderBean oldOrder, final OrderBean updatedOrder) throws DBException {
		boolean updated = isValidUpdatedOrderItems(oldOrder.getItems(), updatedOrder.getItems());
		
		if (updated)
			updated = orderItemDAO.transactionUpdate(conn, updatedOrder);	
		
		return updated;
	}
		
	private boolean isValidUpdatedOrderItems(final List<OrderItemBean> oldOrderItems, final List<OrderItemBean> updatedOrderItems) {
		int oldItemsCount = oldOrderItems.size();
		int newItemsCount = updatedOrderItems.size();
		
		if (oldItemsCount != newItemsCount)
			return false;
			
		boolean allWithZeroQty = true;
		for (OrderItemBean oldOrderItem: oldOrderItems) 
		{
			boolean foundMatch = false;	
			
			for (OrderItemBean updatedOrderItem: updatedOrderItems) {
				foundMatch = (oldOrderItem.getMenuItem().getId() == updatedOrderItem.getMenuItem().getId());
				if (foundMatch) {
					allWithZeroQty &= (updatedOrderItem.getQuantity() == 0);
					break; // break from the inner loop (process the next oldOrderItem)
				}
			}
			
			if (!foundMatch) {
				// the oldOrderItems list has an orderItem that is not in the updatedOrderItems list
				return false;
			}
		}
		
		return allWithZeroQty == false;		
	}
	
	
	private boolean hasDifferentData(final AddressBean oldAddress, final AddressBean newAddress) {
		boolean isSame = false;
		
		isSame  = oldAddress.getLine1().equals(newAddress.getLine1());
		isSame &= oldAddress.getCity().equals(newAddress.getCity());
		isSame &= oldAddress.getState().equals(newAddress.getState());
		isSame &= oldAddress.getZipcode().equals(newAddress.getZipcode());
		
		String oldLine2 = oldAddress.getLine2();
		String newLine2 = newAddress.getLine2();
		
		if (oldLine2 != null && newLine2 != null) {
			isSame &= oldLine2.equals(newLine2);
		} else if (oldLine2 == null && newLine2 == null) {
			isSame &= true;
		} else {
			isSame &= false;
		}
		
		return !isSame;
	}
	
	
	
	private static final String SELECT_ALL_QUERY = 
			"SELECT o.*, a.line1, a.line2, a.city, a.state, a.zipcode FROM orders as o LEFT JOIN addresses as a ON (o.address_id = a.address_id)";
	
	private static final String SELECT_ALL_WITH_ORDER_QUERY = 	SELECT_ALL_QUERY + " ORDER BY (o.order_id)";
	private static final String SELECT_BY_ID_QUERY =			SELECT_ALL_QUERY + " WHERE (o.order_id = ?)";
	private static final String SELECT_BY_STATUS_QUERY = 		SELECT_ALL_QUERY + " WHERE (o.order_status = ?) ORDER BY order_timestamp ASC";
	private static final String SELECT_BY_TYPE_QUERY = 	 		SELECT_ALL_QUERY + " WHERE (o.order_type = ?) ORDER BY order_type ASC";
	private static final String SELECT_BY_CONFIRMATION_QUERY = 	SELECT_ALL_QUERY + " WHERE (o.order_confirmation = ?)";
	
	private static final String INSERT_ORDER_QUERY = 
			"INSERT INTO orders (order_status, order_type, order_timestamp, order_total, order_confirmation, address_id) VALUES (?, ?, ?, ?, ?, ?)";	
	
	private static final String UPDATE_ORDER_QUERY = 
			"UPDATE orders SET order_status=?, order_type=?, order_timestamp=?, order_total=?, order_confirmation=?, address_id=? WHERE (order_id = ?)";
	
	private static final String DELETE_ORDER_QUERY = 
			"DELETE FROM orders WHERE (order_id = ?)";
	
	private static final int ORDER_COLUMNS_COUNT = 6;
}
