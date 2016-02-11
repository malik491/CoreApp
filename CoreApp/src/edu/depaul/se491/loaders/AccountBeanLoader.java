/**
 * Loader for Account Bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * @author Malik
 *
 */
public class AccountBeanLoader {
	private UserBeanLoader loader;
	
	public AccountBeanLoader() {
		loader = new UserBeanLoader();
	}
	
	/**
	 * return a list of account beans using accounts data in the ResultSet (rows)
	 * Empty list is return if the ResultSet is empty
	 * The ResultSet cursor should be positioned before the first row before calling
	 * this method. Otherwise, the first row will not be included in the result.
	 * @param rs a ResultSet containing accounts data from the database
	 * @return list of accounts
	 */
	public List<AccountBean> loadList(ResultSet rs) throws SQLException {
		List<AccountBean> accounts = new ArrayList<>();
		while(rs.next())
			accounts.add(loadSingle(rs));
		return accounts;
	}

	/**
	 * return an account bean using the ResultSet (a single row)
	 * THIS METHOD SHOULD BE CALLED ONLY WHEN (rs.next() is true before the call).
	 * It expects a ResultSet its cursor pointing at a row
	 * @param rs a ResultSet containing account data from the database
	 * @return account bean object containing the data from an account in the database
	 */
	public AccountBean loadSingle(ResultSet rs) throws SQLException {
		AccountBean bean = new AccountBean();
		
		CredentialsBean credentials = new CredentialsBean();
		credentials.setUsername(rs.getString(DBLabels.Account.USERNAME));
		credentials.setPassword(rs.getString(DBLabels.Account.PASSWORD));

		bean.setCredentials(credentials);
		bean.setRole(AccountRole.valueOf(rs.getString(DBLabels.Account.ROLE)));
		bean.setUser(loader.loadSingle(rs));
		
		return bean;
	}

	/**
	 * populate the PreparedStatment with data in the account bean
	 * @param ps preparedStatement with sql string containing at least 4 '?'/placeholders
	 * @param bean account bean with data
	 * @return return the passed ps
	 */
	public void loadParameters(PreparedStatement ps, AccountBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getCredentials().getUsername().toLowerCase());
		ps.setString(paramIndex++, bean.getCredentials().getPassword());		
		ps.setString(paramIndex++, bean.getRole().name());
		ps.setLong(paramIndex, bean.getUser().getId());
	}
	

	/**
	 * populate the PreparedStatment with data in the account bean
	 * @param ps preparedStatement with update sql
	 * @param bean account bean with data
	 * @return return the passed ps
	 */
	public void loadUpdateParameters(PreparedStatement ps, AccountBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getCredentials().getPassword());		
		ps.setString(paramIndex++, bean.getRole().name());
		ps.setLong(paramIndex, bean.getUser().getId());
	}
}
