/**
 * Address Data Access Object (DAO)
 */
package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.builders.AddressBuilder;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.loaders.AddressBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * @author Malik
 *
 */
public class AddressDAO {
	private ConnectionFactory connFactory;
	private AddressBeanLoader loader;
	
	public  AddressDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.loader = new AddressBeanLoader();
	}
	
	/**
	 * return all addresses in the database
	 * Empty list is returned if there are no addresses in the database
	 * @return
	 * @throws SQLException
	 */
	public List<AddressBean> getAll() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AddressBean> addresses = null;
		
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_QUERY);
			
			rs = ps.executeQuery();
			addresses = loader.loadList(rs);
			
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
		return addresses;
	}

	/**
	 * return address associated with the given id
	 * Null is returned if there are no address for the given id
	 * @param addressId
	 * @return
	 * @throws SQLException
	 */
	public AddressBean get(long addressId) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AddressBean address = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID_QUERY);
			
			ps.setLong(1, addressId);
			rs = ps.executeQuery();
			
			if (rs.next())
				address = loader.loadSingle(rs);
			
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
		return address;
	}
	
	/**
	 * insert a new address as a part of a database transaction
	 * @param conn database connection
	 * @param deliveryAddress address data (except the id). 
	 * @return address
	 * @throws SQLException
	 */
	public AddressBean transactionAdd(Connection conn, AddressBean address) throws DBException {
		PreparedStatement ps = null;
		AddressBean addedAddr = null;
		try {
			ps = conn.prepareStatement(INSERT_ADDR_QUERY, Statement.RETURN_GENERATED_KEYS);
			loader.loadParameters(ps, address, 1);
			
			boolean added = DAOUtil.validInsert(ps.executeUpdate());
			if (added) {
				// copy old address data
				addedAddr = new AddressBuilder(address).build();
				
				// set its new id
				addedAddr.setId(DAOUtil.getAutGeneratedKey(ps));						
			}
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
		return addedAddr;
	}
		
	/**
	 * update an existing address as a part of a database transaction
	 * @param conn database connection
	 * @param address updated address
	 * @return true if the address in database is updated
	 * @throws SQLException
	 */
	public boolean transactionUpdate(Connection conn, AddressBean address) throws DBException {
		PreparedStatement ps = null;
		boolean updated = false;
		try {
			ps = conn.prepareStatement(UPADTE_ADDR_QUERY);
			
			int paramIndex = 1;
			loader.loadParameters(ps, address, paramIndex);
			ps.setLong(paramIndex + UPDATE_COULMNS_COUNT, address.getId());
			updated = DAOUtil.validUpdate(ps.executeUpdate());			
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
		return updated;
	}
	
	/**
	 * delete an existing address as a part of a database transaction
	 * @param conn database connection
	 * @param addressId 
	 * @return true if address is deleted
	 * @throws SQLException
	 */
	public boolean transactionDelete(Connection conn, long addressId) throws DBException {
		PreparedStatement ps = null;
		boolean deleted = false;
		try {
			ps = conn.prepareStatement(DELETE_ADDR_QUERY);			
			ps.setLong(1, addressId);
			deleted = DAOUtil.validDelete(ps.executeUpdate());
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
	
	
	
	private static final String SELECT_ALL_QUERY = String.format("SELECT * FROM %s ORDER BY %s", 
																DBLabels.Address.TABLE, DBLabels.Address.ID);
	
	private static final String SELECT_BY_ID_QUERY = String.format("SELECT * FROM %s WHERE (%s = ?)", 
																DBLabels.Address.TABLE, DBLabels.Address.ID);
	
	private static final String INSERT_ADDR_QUERY = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?)", 
																DBLabels.Address.TABLE, DBLabels.Address.LINE_1, DBLabels.Address.LINE_2,
																DBLabels.Address.CITY, DBLabels.Address.STATE, DBLabels.Address.ZIPCODE);
	
	private static final String UPADTE_ADDR_QUERY = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE (%s = ?)", 
																DBLabels.Address.TABLE, DBLabels.Address.LINE_1, DBLabels.Address.LINE_2,
																DBLabels.Address.CITY, DBLabels.Address.STATE, DBLabels.Address.ZIPCODE,
																DBLabels.Address.ID);
	private static final String DELETE_ADDR_QUERY = String.format("DELETE FROM %s WHERE (%s=?)", DBLabels.Address.TABLE, DBLabels.Address.ID);
	
	private static final int UPDATE_COULMNS_COUNT = 5;
}
