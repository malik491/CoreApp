package edu.depaul.se491.dao.mysql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.depaul.se491.beans.CreditCardBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.daos.BadConnection;
import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.ExceptionConnectionFactory;
import edu.depaul.se491.daos.TestConnectionFactory;
import edu.depaul.se491.daos.TestDAOFactory;
import edu.depaul.se491.daos.mysql.PaymentDAO;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.test.DBBuilder;
import edu.depaul.se491.test.TestDataGenerator;

/**
 * 
 * @author Malik
 *
 */
public class PaymentDAOTest {
	private static ConnectionFactory connFactory;
	private static DBBuilder dbBuilder;
	private static TestDataGenerator testDataGen;
	
	private static PaymentDAO paymentDAO;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connFactory = TestConnectionFactory.getInstance();
		dbBuilder = new DBBuilder(connFactory);
		testDataGen = new TestDataGenerator(connFactory);
		
		
		paymentDAO = new TestDAOFactory(connFactory).getPaymentDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// rebuild to original state
		dbBuilder.rebuildAll();
		testDataGen.generateStandardData();
		
		// release resources
		dbBuilder = null;
		testDataGen = null;
		paymentDAO = null;
		
		// close connection data source (pool)
		connFactory.close();
	}

	@Before
	public void setUp() throws Exception {
		// rebuild the DB before each method
		dbBuilder.rebuildAll();
			
		// generate test data
		testDataGen.generateStandardData();
	}

	@Test
	public void testPaymentDAO() {
		assertNotNull(paymentDAO);
	}

	@Test
	public void testTransactionAdd() throws Exception {
		PaymentBean cashPayment = new PaymentBean(0L, 5.00, PaymentType.CASH, null, null);
		PaymentBean ccPayment = new PaymentBean(0L, 10.50, PaymentType.CREDIT_CARD, new CreditCardBean(), "credit-card-confirmation");
		
		
		PaymentBean addedCashPayment = null;
		PaymentBean addedCCPayment = null;
		
		Connection con = null;
		try {
			con = connFactory.getConnection();
			addedCashPayment = paymentDAO.transactionAdd(con, cashPayment);
			addedCCPayment = paymentDAO.transactionAdd(con, ccPayment);
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
		
		assertNotNull(addedCashPayment);
		assertNotNull(addedCCPayment);
		
		long expectedId = 3L;
		assertEquals(expectedId, addedCashPayment.getId());
		assertEquals(PaymentType.CASH, addedCashPayment.getType());
		assertEquals(0, Double.compare(cashPayment.getTotal(), addedCashPayment.getTotal()));
		assertNull(addedCashPayment.getCreditCard());
		assertNull(addedCashPayment.getTransactionConfirmation());
		
		assertEquals(expectedId + 1, addedCCPayment.getId());
		assertEquals(PaymentType.CREDIT_CARD, addedCCPayment.getType());
		assertEquals(0, Double.compare(ccPayment.getTotal(), addedCCPayment.getTotal()));
		assertNotNull(addedCCPayment.getCreditCard());
		assertEquals(ccPayment.getTransactionConfirmation(), addedCCPayment.getTransactionConfirmation());		
	}
	
	@Test
	public void testExceptions() {
		PaymentDAO dao = new TestDAOFactory(new ExceptionConnectionFactory()).getPaymentDAO();
		
		try {
			dao.transactionAdd(new BadConnection(), new PaymentBean());
			fail("No Exception Thrown");
		} catch(Exception e) {}
		
		assertTrue(true);
	}

}
