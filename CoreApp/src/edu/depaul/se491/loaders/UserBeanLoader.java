/**
 * Loader for User bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.UserBean;
import edu.depaul.se491.utils.dao.DBLabels;


/**
 * User Bean loader
 * - populate a preparedStatment using data store in a User bean
 * - populate/recreate a new User bean using data in a ResultSet
 * 
 * @author Malik
 */
public class UserBeanLoader {
	private AddressBeanLoader loader;
	
	public UserBeanLoader() {
		loader = new AddressBeanLoader(); 
	}
	
	/**
	 * return a list of User beans using users data in the ResultSet (rows)
	 * Empty list is return if the ResultSet is empty
	 * The ResultSet cursor should be positioned before the first row before calling
	 * this method. Otherwise, the first row will not be included in the result.
	 * @param rs a ResultSet containing Users data from the database
	 * @return list of Users
	 */
	public List<UserBean> loadList(ResultSet rs) throws SQLException {
		List<UserBean> users = new ArrayList<>();
		while (rs.next())
			users.add(loadSingle(rs));
		return users;
	}

	/**
	 * return a User bean using the ResultSet (a single row)
	 * THIS METHOD SHOULD BE CALLED ONLY WHEN (rs.next() is true before the call).
	 * It expects a ResultSet its cursor pointing at a row
	 * @param rs a ResultSet containing User data from the database
	 * @return User bean object containing the data for a user in the database
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
	 * populate the PreparedStatment with data in the User bean
	 * @param ps preparedStatement with sql string containing at least 5 '?'/placeholders
	 * @param bean user bean with data
	 * @return return the passed ps
	 */
	public void loadParameters(PreparedStatement ps, UserBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getFirstName());
		ps.setString(paramIndex++, bean.getLastName());
		ps.setString(paramIndex++, bean.getEmail());
		ps.setString(paramIndex++, bean.getPhone());
		ps.setLong(paramIndex++, bean.getAddress().getId());
	}
}
