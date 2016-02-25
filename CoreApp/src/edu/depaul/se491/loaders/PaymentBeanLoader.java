package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * PaymentBean Loader
 * 
 * @author Malik
 */
public class PaymentBeanLoader {

	/**
	 * return a single PaymentBean in the result-set
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public PaymentBean loadSingle(ResultSet rs) throws SQLException {
		PaymentType type = PaymentType.valueOf(rs.getString(DBLabels.Payment.TYPE));
		
		PaymentBean bean = new PaymentBean();
		bean.setType(type);
		bean.setId(rs.getLong(DBLabels.Payment.ID));
		bean.setTotal(rs.getDouble(DBLabels.Payment.TOTAL));
		bean.setTransactionConfirmation(rs.getString(DBLabels.Payment.CC_TRANSACTION_CONFIRMATION));
		
		return bean;
	}

	/**
	 * laod parameters from the PaymentBean into the given PreparedStatement
	 * @param ps
	 * @param bean
	 * @param paramIndex
	 * @throws SQLException
	 */
	public void loadParameters(PreparedStatement ps, PaymentBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getType().name());
		ps.setDouble(paramIndex++, bean.getTotal());
		
		if (bean.getType() == PaymentType.CASH)
			ps.setNull(paramIndex++, java.sql.Types.NULL);
		else
			ps.setString(paramIndex++, bean.getTransactionConfirmation());
		
	}

}
