/**
 * User Data Access Object (DAO)
 */
package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.builders.UserBuilder;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.loaders.UserBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * @author Malik
 *
 */
public class UserDAO {
	private ConnectionFactory connFactory;
	private UserBeanLoader loader;
	private AddressDAO addressDAO;

	public UserDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
		this.addressDAO = daoFactory.getAddressDAO();
		this.loader = new UserBeanLoader();
	}
	
	/**
	 * return all users in the database
	 * Empty list is returned if there are no users in the database
	 * @return
	 * @throws SQLException
	 */
	public List<UserBean> getAll() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<UserBean> users = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_QUERY);
			
			rs = ps.executeQuery();
			users = loader.loadList(rs);
			
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
		return users;
	}
	
	/**
	 * return user associated with the given email
	 * Null is returned if there are no user for the given id
	 * @param email user email
	 * @return User or Null
	 * @throws SQLException
	 */
	public UserBean get(long id) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean user = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID_QUERY);
			
			ps.setLong(1, id);
			rs = ps.executeQuery();
			
			if (rs.next())
				user = loader.loadSingle(rs);
			
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
		return user;
	}
	
	/**
	 * insert a new user (including user address) as a part of a database transaction
	 * @param conn connection
	 * @param user user data. 
	 * @return inserted user or Null
	 * @throws SQLException
	 */
	public UserBean transactionAdd(Connection conn, final UserBean user) throws DBException {
		PreparedStatement ps = null;
		UserBean addedUser = null;
		try {
			
			// add new address
			AddressBean addedAddress = addressDAO.transactionAdd(conn, user.getAddress());
			boolean added = addedAddress != null;
			
			if (added) {
				// copy old user data
				addedUser = new UserBuilder(user).build();
				// set its new address (with id)
				addedUser.setAddress(addedAddress);
				
				ps = conn.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
				loader.loadParameters(ps, addedUser, 1);
				added = DAOUtil.validInsert(ps.executeUpdate());
				
				if (added) {
					// set its new id
					addedUser.setId(DAOUtil.getAutGeneratedKey(ps));
				} else {
					addedUser = null;
				}
			}
						
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
			}
		}
		return addedUser;
	}

	/**
	 * update an existing user as a part of database transaction
	 * @param user updated user
	 * @return true if user is updated
	 * @throws SQLException
	 */
	public boolean transactionUpdate(Connection conn, final UserBean user) throws DBException {
		PreparedStatement ps = null;
		boolean updated = false;
		try {
			updated = addressDAO.transactionUpdate(conn, user.getAddress());	
			
			if (updated) {
				ps = conn.prepareStatement(UPDATE_USER_QUERY);
				int paramIndex = 1;
				loader.loadParameters(ps, user, paramIndex);
				ps.setLong(paramIndex + UPDATE_COLUMNS_COUNT, user.getId());
				updated = DAOUtil.validUpdate(ps.executeUpdate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
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
	 * delete an existing user as a part of database transaction
	 * @param conn
	 * @param user
	 * @return true if user is deleted
	 * @throws SQLException
	 */
	public boolean transactionDelete(Connection conn, UserBean user) throws DBException {
		PreparedStatement ps = null;
		boolean deleted = false;
		try {
			// delete user then delete address (foreign key in user)
			ps = conn.prepareStatement(DELETE_USER_QUERY);
			ps.setLong(1, user.getId());
			deleted = DAOUtil.validDelete(ps.executeUpdate());
			
			if (deleted)
				deleted = addressDAO.transactionDelete(conn, user.getAddress().getId());	
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} catch (DBException e) {
			throw e;
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

	
	private static final String SELECT_ALL_QUERY = String.format("SELECT * FROM %s NATURAL JOIN %s", DBLabels.User.TABLE, DBLabels.Address.TABLE);
	
	private static final String SELECT_BY_ID_QUERY = String.format("%s WHERE (%s = ?)", SELECT_ALL_QUERY, DBLabels.User.ID);
	
	private static final String INSERT_USER_QUERY = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?)",
																DBLabels.User.TABLE, DBLabels.User.F_NAME, DBLabels.User.L_NAME,
																DBLabels.User.EMAIL, DBLabels.User.PHONE, DBLabels.User.ADDRESS);
	
	private static final String UPDATE_USER_QUERY = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE (%s = ?)",
																DBLabels.User.TABLE, DBLabels.User.F_NAME, DBLabels.User.L_NAME,
																DBLabels.User.EMAIL, DBLabels.User.PHONE, DBLabels.User.ADDRESS, 
																DBLabels.User.ID);
	
	private static final String DELETE_USER_QUERY = String.format("DELETE FROM %s WHERE (%s = ?)", DBLabels.User.TABLE, DBLabels.User.ID);
	
	
	private static final int UPDATE_COLUMNS_COUNT = 5;
}
