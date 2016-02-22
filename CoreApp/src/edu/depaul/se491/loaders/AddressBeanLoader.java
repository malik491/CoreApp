/**
 * Loader for Address bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * Address Bean loader
 * - populate a preparedStatment using data store in an Address bean
 * - populate/recreate a new Address bean using data in a ResultSet
 * 
 * @author Malik
 */
public class AddressBeanLoader {
	
	/**
	 * return an account bean using the ResultSet (a single row)
	 * THIS METHOD SHOULD BE CALLED ONLY WHEN (rs.next() is true before the call).
	 * It expects a ResultSet its cursor pointing at a row
	 * @param rs a ResultSet containing account data from the database
	 * @return account bean object containing the data from an account in the database
	 */
	public AddressBean loadSingle(ResultSet rs) throws SQLException {
		AddressBean bean = new AddressBean();		
		
		bean.setId(rs.getLong(DBLabels.Address.ID));
		bean.setLine1(rs.getString(DBLabels.Address.LINE_1));
		bean.setLine2(rs.getString(DBLabels.Address.LINE_2));
		bean.setCity(rs.getString(DBLabels.Address.CITY));	
		bean.setState(AddressState.valueOf(rs.getString(DBLabels.Address.STATE)));
		bean.setZipcode(rs.getString(DBLabels.Address.ZIPCODE));
		
		return bean;
	}

	/**
	 * populate the PreparedStatment with data in the address bean
	 * @param ps preparedStatement with sql string containing at least 5 '?'/placeholders
	 * @param bean address bean with data
	 * @return return the passed ps
	 */
	public void loadParameters(PreparedStatement ps, AddressBean bean, int paramIndex) throws SQLException {		
		ps.setString(paramIndex++, bean.getLine1());
		
		String line2 = bean.getLine2();
		if (line2 != null)
			ps.setString(paramIndex++, bean.getLine2());
		else
			ps.setNull(paramIndex++, java.sql.Types.NULL);
		
		ps.setString(paramIndex++, bean.getCity());
		ps.setString(paramIndex++, bean.getState().name());
		ps.setString(paramIndex++, bean.getZipcode());
	}
}
