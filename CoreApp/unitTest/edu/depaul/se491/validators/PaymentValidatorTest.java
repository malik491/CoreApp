package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.CreditCardBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.enums.PaymentType;
import edu.depaul.se491.utils.ParamValues;

public class PaymentValidatorTest {
	private PaymentValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new PaymentValidator();
	}
	
	@Test
	public void testValidate() {
		
		PaymentBean payment = new PaymentBean();
		assertFalse(validator.validate(payment,true));
		
		payment.setId(1);
		payment.setTotal(103.69);
		payment.setType(PaymentType.CASH);
		payment.setCreditCard(null);
		payment.setTransactionConfirmation(null);
		assertTrue(validator.validate(payment,false));
		
		payment.setTotal(ParamValues.Payment.MIN_TOTAL - 1);
		assertFalse(validator.validate(payment,false));
		
		payment.setTotal(ParamValues.Payment.MAX_TOTAL + 1);
		assertFalse(validator.validate(payment,false));
		
		payment.setTotal(103.69);
		assertTrue(validator.validate(payment,false));
		
		payment.setType(PaymentType.CREDIT_CARD);
		assertFalse(validator.validate(payment,false));
		
		String ccNumber = "1234567890123456";
		String ccHolderName = "John Doe";
		int expMonth = 12;
		int expYear = 2020;
		
		CreditCardBean creditCard = new CreditCardBean(ccNumber, ccHolderName, expMonth, expYear);
		payment.setCreditCard(creditCard);
		assertFalse(validator.validate(payment,false));
				
		payment.setTransactionConfirmation(""); // too short
		assertFalse(validator.validate(payment,false));
		
		payment.setTransactionConfirmation("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"); // too long
		assertFalse(validator.validate(payment,false));
		
		payment.setTransactionConfirmation("1029-384-7560-1892"); // just right
		assertTrue(validator.validate(payment,false));
	}

	@Test
	public void testValidateId() {
		
		long id=0;
		double total=103.67;
		PaymentType type = PaymentType.CASH;
		CreditCardBean creditCard = null;
		String transactionConfirmation = "1234567890";
		
		PaymentBean payment = new PaymentBean(id, total, type, creditCard, transactionConfirmation);

		assertTrue(validator.validateId(payment.getId(),true));
		assertFalse(validator.validateId(payment.getId(),false));
	
		payment.setId(1);
		
		assertFalse(validator.validateId(payment.getId(),true));	
		assertTrue(validator.validateId(payment.getId(),false));
	}
}
