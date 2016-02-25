package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * AddressBean Loader
 * 
 * @author Malik
 */
public class AddressBeanLoader {
	
	/**
	 * return a single AddressBean in the result-set
	 * @param rs
	 * @return
	 * @throws SQLException
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
	 * load parameters from the AddressBean into the given PrepareStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @throws SQLException
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
