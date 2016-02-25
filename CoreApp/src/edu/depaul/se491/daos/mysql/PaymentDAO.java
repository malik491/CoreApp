package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.loaders.PaymentBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * Payment Data Access Object (DAO)
 *  
 * @author Malik
 */
public class PaymentDAO {
	private PaymentBeanLoader loader;
	
	/**
	 * construct PaymentDAO
	 * @param daoFactory
	 * @param connFactory
	 */
	public  PaymentDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.loader = new PaymentBeanLoader();
	}
	
	/**
	 * insert a new payment using the given connection (transaction)
	 * @param conn
	 * @param bean
	 * @return newly added payment
	 * @throws SQLException
	 */
	public PaymentBean transactionAdd(final Connection conn, PaymentBean bean) throws SQLException {
		PreparedStatement ps = null;
		PaymentBean addedPayment = null;
		try {
			ps = conn.prepareStatement(INSERT_PAYMENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			loader.loadParameters(ps, bean, 1);
			
			boolean added = DAOUtil.validInsert(ps.executeUpdate());
			if (added) {
				addedPayment = new PaymentBean(DAOUtil.getAutGeneratedKey(ps), bean.getTotal(), bean.getType(), bean.getCreditCard(), bean.getTransactionConfirmation());					
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				DAOUtil.close(ps);
			} catch (SQLException e) {
				throw e;	
			}
		}
		return addedPayment;
	}
	
	
	private static final String INSERT_PAYMENT_QUERY = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", 
																	DBLabels.Payment.TABLE, 
																	DBLabels.Payment.TYPE, DBLabels.Payment.TOTAL, 
																	DBLabels.Payment.CC_TRANSACTION_CONFIRMATION );
}
