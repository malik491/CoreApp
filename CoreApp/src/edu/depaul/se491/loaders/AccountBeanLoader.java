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
 * AccountBean Loader
 * 
 * @author Malik
 */
public class AccountBeanLoader {
	private UserBeanLoader loader;
	
	/**
	 * construct AccountBeanLoader
	 */
	public AccountBeanLoader() {
		loader = new UserBeanLoader();
	}
	
	/**
	 * return a list of all accounts in the given result-set or empty list
	 * @param rs result-set
	 * @return
	 * @throws SQLException
	 */
	public List<AccountBean> loadList(ResultSet rs) throws SQLException {
		List<AccountBean> accounts = new ArrayList<>();
		while(rs.next())
			accounts.add(loadSingle(rs));
		return accounts;
	}

	/**
	 * return a single account in the result-set
	 * @param rs
	 * @return
	 * @throws SQLException
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
	 * load parameters from the AccountBean into the given PrepareStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @throws SQLException
	 */
	public void loadParameters(PreparedStatement ps, AccountBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getCredentials().getUsername().toLowerCase());
		ps.setString(paramIndex++, bean.getCredentials().getPassword());		
		ps.setString(paramIndex++, bean.getRole().name());
		ps.setLong(paramIndex, bean.getUser().getId());
	}
	
	/**
	 * load update parameters from the AccountBean into the given PrepareStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @throws SQLException
	 */
	public void loadUpdateParameters(PreparedStatement ps, AccountBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getCredentials().getPassword());		
		ps.setString(paramIndex++, bean.getRole().name());
		ps.setLong(paramIndex, bean.getUser().getId());
	}
}
