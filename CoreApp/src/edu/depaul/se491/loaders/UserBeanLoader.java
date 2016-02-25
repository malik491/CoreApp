package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * UserBean Loader
 * 
 * @author Malik
 */
public class UserBeanLoader {
	private AddressBeanLoader loader;
	
	/**
	 * construct UserBeanLoader
	 */
	public UserBeanLoader() {
		loader = new AddressBeanLoader(); 
	}

	/**
	 * return a single UserBean in the result-set
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public UserBean loadSingle(ResultSet rs) throws SQLException {
		UserBean bean = new UserBean();
		
		bean.setId(rs.getLong(DBLabels.User.ID));
		bean.setFirstName(rs.getString(DBLabels.User.F_NAME));
		bean.setLastName(rs.getString(DBLabels.User.L_NAME));
		bean.setEmail(rs.getString(DBLabels.User.EMAIL));
		bean.setPhone(rs.getString(DBLabels.User.PHONE));
		bean.setAddress(loader.loadSingle(rs));
		
		return bean;
	}

	/**
	 * load parameters from UserBean into the given PreparedStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @throws SQLException
	 */
	public void loadParameters(PreparedStatement ps, UserBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getFirstName());
		ps.setString(paramIndex++, bean.getLastName());
		ps.setString(paramIndex++, bean.getEmail());
		ps.setString(paramIndex++, bean.getPhone());
		ps.setLong(paramIndex++, bean.getAddress().getId());
	}
}
