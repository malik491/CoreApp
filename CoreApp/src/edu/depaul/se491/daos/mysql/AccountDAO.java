package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.loaders.AccountBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * Account Data Access Object (DAO)
 * 
 * @author Malik
 */
public class AccountDAO {
	private ConnectionFactory connFactory;
	private AccountBeanLoader loader;
	private UserDAO userDAO;
	
	/**
	 * construct AccountDAO
	 * @param daoFactory
	 * @param connFactory
	 */
	public  AccountDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
		this.loader = new AccountBeanLoader();
		this.userDAO = daoFactory.getUserDAO();
	}
	
	/**
	 * return all accounts with the given role (or empty list)
	 * @param role
	 * @return
	 * @throws SQLException
	 */
	public List<AccountBean> getAllByRole(AccountRole role) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AccountBean> accounts = null;
		
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_BY_ROLE_Query);
			ps.setString(1, role.name());
			
			rs = ps.executeQuery();
			accounts = loader.loadList(rs);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(rs);
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return accounts;
	}
	
	/**
	 * return account with the given username or null
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public AccountBean get(String username) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AccountBean account = null;
		try {
			conn = connFactory.getConnection();
			ps = conn.prepareStatement(SELECT_BY_USERNAME_Query);
			
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			if (rs.next())
				account = loader.loadSingle(rs);
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(rs);
				DAOUtil.close(ps);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return account;
	}
	
	/**
	 * insert a new account
	 * @param account
	 * @return newly inserted account
	 * @throws SQLException
	 */
	public AccountBean add(AccountBean account) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		AccountBean result = null;
		
		try {
			boolean added = false;
			/* Transaction:
			 * - add user
			 * - add account
			 */
			
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			
			UserBean addedUser = userDAO.transactionAdd(conn, account.getUser());
			added = addedUser != null;
			
			AccountBean addedAccount = null;
			if (added) {
				// copy old data, set new user (user with id)
				addedAccount = new AccountBean(account.getCredentials(), account.getUser(), account.getRole());
				addedAccount.setUser(addedUser);
				
				ps = conn.prepareStatement(INSERT_ACC_QUERY);
				loader.loadParameters(ps, addedAccount, 1);
				added = DAOUtil.validInsert(ps.executeUpdate());
			}
			
			if (added) {
				// commit the transaction
				conn.commit();
				result = addedAccount;
			} else {
				// rollback
				conn.rollback();
			}			

		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.addSuppressed(e);
					throw e1;
				}				
			}
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.setAutoCommit(conn, true);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return result;
	}
	
	/**
	 * update account
	 * @param account
	 * @return
	 * @throws SQLException
	 */
	public boolean update(AccountBean account) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean updated = false;
		try {
			/*
			 * Transaction:
			 * - update user
			 * - update account
			 */
			
			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(UPADTE_ACC_QUERY);
					
			updated = userDAO.transactionUpdate(conn, account.getUser());
			
			if (updated) {
				int paramIndex = 1;
				loader.loadUpdateParameters(ps, account, paramIndex);
				ps.setString(paramIndex + UPDATE_COLUMNS_COUNT, account.getCredentials().getUsername());
				updated = DAOUtil.validUpdate(ps.executeUpdate());
			}
			
			if (updated) {
				// commit
				conn.commit();
			} else {
				conn.rollback();				
			}
			
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.addSuppressed(e);
					throw e1;
				}				
			}
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.setAutoCommit(conn, true);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return updated;
	}
	
	/**
	 * delete account with the given username
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(final String username) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean deleted = false;
		try {
			AccountBean account = get(username);
			if (account == null)
				return deleted;
			
			/*
			 * Transaction:
			 * - delete account (has foreign key to user)
			 * - delete user
			 */

			conn = connFactory.getConnection();
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement(DELETE_ACC_QUERY);
			ps.setString(1, username);
			deleted = DAOUtil.validDelete(ps.executeUpdate());
			
			if (deleted)
				deleted = userDAO.transactionDelete(conn, account.getUser());
			
			
			if (deleted) {
				// commit
				conn.commit();
			} else {
				conn.rollback();				
			}
			
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.addSuppressed(e);
					throw e1;
				}				
			}
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
				DAOUtil.setAutoCommit(conn, true);
				DAOUtil.close(conn);
			} catch (SQLException e) {
				throw e;
			}
		}
		return deleted;
	}
	
	private static final String SELECT_ALL_BY_ROLE_Query = String.format("SELECT * FROM %s NATURAL JOIN %s NATURAL JOIN %s WHERE (%s = ?)",
																DBLabels.Account.TABLE, DBLabels.User.TABLE, DBLabels.Address.TABLE, 
																DBLabels.Account.ROLE);
	
	private static final String SELECT_BY_USERNAME_Query = String.format("SELECT * FROM %s NATURAL JOIN %s NATURAL JOIN %s WHERE (%s = ?)",
																		DBLabels.Account.TABLE, DBLabels.User.TABLE, DBLabels.Address.TABLE, 
																		DBLabels.Account.USERNAME);
	
	private static final String INSERT_ACC_QUERY = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?, ?,?)",
																DBLabels.Account.TABLE, DBLabels.Account.USERNAME, 
																DBLabels.Account.PASSWORD, DBLabels.Account.ROLE, DBLabels.Account.USER_ID);
	
	private static final String UPADTE_ACC_QUERY = String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE (%s = ?)", 
																DBLabels.Account.TABLE, DBLabels.Account.PASSWORD, DBLabels.Account.ROLE,
																DBLabels.Account.USER_ID, DBLabels.Account.USERNAME);
	
	
	private static final String DELETE_ACC_QUERY = String.format("DELETE FROM %s WHERE (%s = ?)", DBLabels.Account.TABLE, DBLabels.Account.USERNAME);
	
	private static final int UPDATE_COLUMNS_COUNT = 3;

}
