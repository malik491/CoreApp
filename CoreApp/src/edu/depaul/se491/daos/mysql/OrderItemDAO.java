/**
 * OrderItem Data Access Object (DAO)
 */
package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.loaders.OrderItemBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;

/**
 * @author Malik
 *
 */
public class OrderItemDAO {
	private final ConnectionFactory connFactory;
	private final OrderItemBeanLoader loader;
	
	public OrderItemDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
		this.loader = new OrderItemBeanLoader();
	}
	
	/**
	 * return a list of all order items for a given order (specified by orderId)
	 * Empty List is returned if there are no order items for the given order
	 * @param orderId order id associated with order items
	 * @return
	 * @throws SQLException
	 */
	public List<OrderItemBean> getOrderItems(final long orderId) throws DBException  {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<OrderItemBean> orderItems = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ORDER_ID_QUERY);
			
			ps.setLong(1, orderId);
			rs = ps.executeQuery();
			
			orderItems = loader.loadList(rs);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
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
		return orderItems;
	}
	
	/**
	 * Insert a list of order items for the given order as a part of a database transaction
	 * @param conn connection for this transaction
	 * @param orderId order id associated with items param
	 * @param items order items to add
	 * @return true if all items are added
	 * @throws SQLException
	 */
	public boolean transactionAdd(final Connection conn, final OrderBean order) throws DBException {
		PreparedStatement ps = null;
		boolean added = false;
		try {
			List<OrderItemBean> items = order.getItems();
			int itemsCount = items.size();
			if (itemsCount < 1)
				return added;
			
			String multipleRowInsertQuery = getMultipleRowInsert(itemsCount);
			ps = conn.prepareStatement(multipleRowInsertQuery);
			
			int paramIndex = 1;
			for(OrderItemBean oItem: items) {
				loader.loadParameters(ps, oItem, paramIndex, order.getId());
				paramIndex += INSERT_ORDER_ITEM_COLUMNS_COUNT;
			}
			
			added = ps.executeUpdate() == itemsCount;
					
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} finally {
			try {
				DAOUtil.close(ps);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return true;
	}

	/**
	 * update order items quantities as a part of a database transaction
	 * order item with quantity <= 0 will be deleted
	 * @param conn connection for this transaction
	 * @param orderId order id associated with items param
	 * @param items order items to update
	 * @return true if all items are updated
	 * @throws SQLException
	 */
	public boolean transactionUpdate(Connection conn, final OrderBean order) throws DBException {
		Statement batchedUpateStatement = null;
		
		boolean updated = false;
		try {
			final long orderId = order.getId();
			List<OrderItemBean> items = order.getItems();
			int itemsCount = items.size();
			if (itemsCount < 1)
				return updated;
			
			// have you used Statement (instead of preparedStatement).
			// according to the docs, we can't get the number of rows
			// affected by each preparedStatement when using executeBatch 
			batchedUpateStatement = conn.createStatement();

			String batchStatement = null;
			
			for(OrderItemBean oItem: items) {
				int newQuantity = oItem.getQuantity();
				long menuItemId = oItem.getMenuItem().getId();
				if (newQuantity > 0) {
					// batch update statement (update quantity query)
					batchStatement = String.format(
							"UPDATE order_items SET quantity=%d WHERE (order_id = %d AND menu_item_id= %d)",
							newQuantity, orderId, menuItemId);
					batchedUpateStatement.addBatch(batchStatement);
				} else {
					//batch update statement (delete orderItem query / quantity must be 0)
					batchStatement = String.format(
							"DELETE FROM order_items WHERE (order_id = %d AND menu_item_id= %d)",
							orderId, menuItemId);
					batchedUpateStatement.addBatch(batchStatement);
				}
			}
			
			updated &= executeBatch(batchedUpateStatement);
					
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} finally {
			try {
				DAOUtil.close(batchedUpateStatement);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return true;
	}
	
	
	/**
	 * delete all order items for a given order as a part of a database transaction
	 * @param conn connection for this transaction
	 * @param orderId order id associated with the items
	 * @param itemsCount number of items for the order
	 * @return true if all items are deleted (deletedCount = itemsCount)
	 * @throws SQLException
	 */
	public boolean transactionDelete(Connection conn, long orderId, int itemsCount) throws DBException {
		PreparedStatement ps = null;
		boolean deleted = false;
		try {
			ps = conn.prepareStatement(DELETE_ORDER_ITEMS_QUERY);
			ps.setLong(1, orderId);
			deleted = ps.executeUpdate() == itemsCount;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} finally {
			try {
				DAOUtil.close(ps);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return deleted;
	}
	
	/**
	 * execute a batched Statement (multiple queries)
	 * @param statement statement to batch-execute
	 * @return false if (any) batch statement has affectedRow = Statement.EXECUTE_FAILED 
	 * @throws SQLException
	 */
	private boolean executeBatch(Statement statement) throws SQLException {
		boolean updated = true;
		
		// executeBatch() is implementation specific (in our case, Connector J for mysql)
		// current doc for mysql driver does say whether executeBatch stops executing batch
		// statements when one fails so we can't rely on counting the number of affected rows  
		
		for(int affectedRowsPerBatchStatement: statement.executeBatch()) {
			if (affectedRowsPerBatchStatement == Statement.EXECUTE_FAILED) {
				return false;
			}
		}
		return updated;
	}
	
	/**
	 * return a multiple row INSERT query for preparedStatement
	 * Example of 2 rows insert query (one column):INSERT ... VALUES (?), (?)
	 * @param numOfRows number of rows to be inserted
	 * @return
	 */
	private String getMultipleRowInsert(int numOfRows) {
		StringBuilder sb = new StringBuilder(INSERT_ORDER_ITEM_QUERY);
		for (int i=1; i < numOfRows; i++) {
			sb.append(MULTIPLE_INSERT_ROW);
		}
		return sb.toString();	
	}
	
	
	
	private static final String SELECT_BY_ORDER_ID_QUERY = "SELECT * FROM order_items NATURAL JOIN menu_items WHERE (order_id = ?)";
	private static final String INSERT_ORDER_ITEM_QUERY =  "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";
	private static final String DELETE_ORDER_ITEMS_QUERY = "DELETE FROM order_items WHERE (order_id = ?)";

	
	private static final String MULTIPLE_INSERT_ROW = " ,(?,?,?)";
	private static final int INSERT_ORDER_ITEM_COLUMNS_COUNT = 3;

}
