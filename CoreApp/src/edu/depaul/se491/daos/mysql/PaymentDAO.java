/**
 * 
 */
package edu.depaul.se491.daos.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.builders.PaymentBuilder;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.loaders.PaymentBeanLoader;
import edu.depaul.se491.utils.dao.DAOUtil;
import edu.depaul.se491.utils.dao.DBLabels;

/**
 * @author Malik
 */
public class PaymentDAO {
	private PaymentBeanLoader loader;
	
	public  PaymentDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.loader = new PaymentBeanLoader();
	}
	
	
	public PaymentBean transactionAdd(final Connection conn, PaymentBean bean) throws DBException {
		PreparedStatement ps = null;
		PaymentBean addedPayment = null;
		try {
			ps = conn.prepareStatement(INSERT_PAYMENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			loader.loadParameters(ps, bean, 1);
			
			boolean added = DAOUtil.validInsert(ps.executeUpdate());
			if (added) {
				// copy old payment data
				addedPayment = new PaymentBuilder(bean).build();
				
				// set its new id
				addedPayment.setId(DAOUtil.getAutGeneratedKey(ps));						
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
		} finally {
			try {
				DAOUtil.close(ps);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException(DAOUtil.GENERIC_BD_ERROR_MSG);
				
			}
		}
		return addedPayment;
	}
	
	
	private static final String INSERT_PAYMENT_QUERY = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", 
																	DBLabels.Payment.TABLE, 
																	DBLabels.Payment.TYPE, DBLabels.Payment.TOTAL, 
																	DBLabels.Payment.CC_TRANSACTION_CONFIRMATION );
}
