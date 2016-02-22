package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.enums.PaymentType;

public class PaymentBeanTest {
	PaymentBean bean;

	@Before
	public void setUp() throws Exception {
		bean = new PaymentBean();
	}

	@Test
	public void testPaymentBean() {
		assertNotNull(bean);
		assertNull(bean.getCreditCard());
		assertNull(bean.getTransactionConfirmation());
		assertNull(bean.getType());
		assertEquals(0, bean.getId());
		assertTrue(bean.getTotal()==0);
		
		bean = new PaymentBean(0L, 1.00, PaymentType.CASH, null, null);
		assertNotNull(bean);
		assertEquals(0L, bean.getId());
		assertEquals(0, Double.compare(bean.getTotal(), 1.00));
		assertEquals(PaymentType.CASH, bean.getType());
		assertNull(bean.getCreditCard());
		assertNull(bean.getTransactionConfirmation());
		
		bean = new PaymentBean(0L, 1.00, PaymentType.CREDIT_CARD, new CreditCardBean(), "cc-payment-confirmation");
		assertNotNull(bean);
		assertEquals(0L, bean.getId());
		assertEquals(0, Double.compare(bean.getTotal(), 1.00));
		assertEquals(PaymentType.CREDIT_CARD, bean.getType());
		assertNotNull(bean.getCreditCard());
		assertEquals("cc-payment-confirmation", bean.getTransactionConfirmation());
		
	}

	@Test
	public void testPaymentBeanLongDoublePaymentTypeCreditCardBeanString() {
		long id = 1;
		String tc = "transconfirm";
		PaymentType type = PaymentType.CASH;
		CreditCardBean cc = new CreditCardBean();
		double total = 10.00;
		
		bean = new PaymentBean(id, total, type,cc, tc);
		assertNotNull(bean);
		assertEquals(id, bean.getId());
		assertEquals(type, bean.getType());
		assertEquals(cc, bean.getCreditCard());
		assertTrue(bean.getTotal()==total);
		assertEquals(tc, bean.getTransactionConfirmation());		
		
	}

	@Test
	public void testGetId() {
		assertEquals(0, bean.getId());
		
		long id = 1;
		bean.setId(id);
		assertEquals(id, bean.getId());
	}

	@Test
	public void testSetId() {
		long id = 1;
		bean.setId(id);
		assertEquals(id, bean.getId());
		
		id = -1;
		bean.setId(id);
		assertEquals(id, bean.getId());
		
		id = 0;
		bean.setId(id);
		assertEquals(id, bean.getId());			
	}

	@Test
	public void testGetTotal() {
		assertTrue(bean.getTotal()==0);
		
		double total = 1.1;
		bean.setTotal(total);;
		assertTrue(bean.getTotal()==total);
	}

	@Test
	public void testSetTotal() {
		double total = 1.1;
		bean.setTotal(total);;
		assertTrue(bean.getTotal()==total);
		
		total = 0;
		bean.setTotal(total);;
		assertTrue(bean.getTotal()==total);
		
		total = -1.1;
		bean.setTotal(total);;
		assertTrue(bean.getTotal()==total);
	}

	@Test
	public void testGetType() {
		assertNull(bean.getType());
		
		PaymentType type = PaymentType.CASH;
		bean.setType(type);
		assertNotNull(bean.getType());
		assertEquals(type, bean.getType());	
	}

	@Test
	public void testSetType() {
		PaymentType type = PaymentType.CASH;
		bean.setType(type);
		assertNotNull(bean.getType());
		assertEquals(type, bean.getType());	
		
		type = null;
		bean.setType(type);
		assertNull(bean.getType());
		
		type = PaymentType.CREDIT_CARD;
		bean.setType(type);
		assertNotNull(bean.getType());
		assertEquals(type, bean.getType());	
	}

	@Test
	public void testGetCreditCard() {
		assertNull(bean.getCreditCard());
		
		CreditCardBean creditcard = new CreditCardBean();
		bean.setCreditCard(creditcard);
		assertNotNull(bean.getCreditCard());
		assertEquals(creditcard, bean.getCreditCard());	
	}

	@Test
	public void testSetCreditCard() {
		CreditCardBean creditcard = new CreditCardBean();
		bean.setCreditCard(creditcard);
		assertNotNull(bean.getCreditCard());
		assertEquals(creditcard, bean.getCreditCard());	
		
		 creditcard = null;
		 bean.setCreditCard(creditcard);
		 assertNull(bean.getCreditCard());
		 assertEquals(creditcard, bean.getCreditCard());	
		
	}

	@Test
	public void testGetTransactionConfirmation() {
		assertNull(bean.getTransactionConfirmation());
		
		String transactionconfirmation = "transconfirm";
		bean.setTransactionConfirmation(transactionconfirmation);
		assertNotNull(bean.getTransactionConfirmation());
		assertEquals(transactionconfirmation, bean.getTransactionConfirmation());
	}

	@Test
	public void testSetTransactionConfirmation() {
		String transactionconfirmation = "transconfirm";
		bean.setTransactionConfirmation(transactionconfirmation);
		assertNotNull(bean.getTransactionConfirmation());
		assertEquals(transactionconfirmation, bean.getTransactionConfirmation());
		
		transactionconfirmation = "";
		bean.setTransactionConfirmation(transactionconfirmation);
		assertNotNull(bean.getTransactionConfirmation());
		assertEquals(transactionconfirmation, bean.getTransactionConfirmation());
	
		transactionconfirmation = null;
		bean.setTransactionConfirmation(transactionconfirmation);
		assertNull(bean.getTransactionConfirmation());
		assertEquals(transactionconfirmation, bean.getTransactionConfirmation());
	}

}
