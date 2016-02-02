package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.utils.dao.DBLabels;

public class PaymentBeanLoader implements BeanLoader<PaymentBean> {

	@Override
	public List<PaymentBean> loadList(ResultSet rs) throws SQLException {
		List<PaymentBean> beans = new ArrayList<PaymentBean>();
		while (rs.next())
			beans.add(loadSingle(rs));
	
		return beans;
	}

	@Override
	public PaymentBean loadSingle(ResultSet rs) throws SQLException {
		PaymentType type = PaymentType.valueOf(rs.getString(DBLabels.Payment.TYPE));
		
		PaymentBean bean = new PaymentBean();
		bean.setType(type);
		bean.setId(rs.getLong(DBLabels.Payment.ID));
		bean.setTotal(rs.getDouble(DBLabels.Payment.TOTAL));
		bean.setTransactionConfirmation(rs.getString(DBLabels.Payment.CC_TRANSACTION_CONFIRMATION));
		
		return bean;
	}

	@Override
	public void loadParameters(PreparedStatement ps, PaymentBean bean, int paramIndex) throws SQLException {
		ps.setString(paramIndex++, bean.getType().name());
		ps.setDouble(paramIndex++, bean.getTotal());
		
		if (bean.getType() == PaymentType.CASH)
			ps.setNull(paramIndex++, java.sql.Types.NULL);
		else
			ps.setString(paramIndex++, bean.getTransactionConfirmation());
		
	}

}
