package edu.depaul.se491.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.depaul.se491.utils.ParamLengths;

/**
 * @author Malik
 */
public class ParamLengthsTest {

	@Test
	public void testParamLengths() {
		assertNotNull(new ParamLengths());
	}
	
	@Test
	public void testAddressLengths() {
		assertNotNull(new ParamLengths.Address());
		
		assertEquals(1, ParamLengths.Address.MIN_LINE1);
		assertEquals(0, ParamLengths.Address.MIN_LINE2);
		assertEquals(1, ParamLengths.Address.MIN_CITY);
		assertEquals(5, ParamLengths.Address.MIN_ZIPCODE);

		assertEquals(100, ParamLengths.Address.MAX_LINE1);
		assertEquals(100, ParamLengths.Address.MAX_LINE2);
		assertEquals(100, ParamLengths.Address.MAX_CITY);
		assertEquals(10, ParamLengths.Address.MAX_ZIPCODE);
	}

	@Test
	public void testCreditCardLengths() {
		assertNotNull(new ParamLengths.CreditCard());
		
		assertEquals(12, ParamLengths.CreditCard.MIN_NUMBER);
		assertEquals(3, ParamLengths.CreditCard.MIN_HOLDER_NAME);

		assertEquals(19, ParamLengths.CreditCard.MAX_NUMBER);
		assertEquals(100, ParamLengths.CreditCard.MAX_HOLDER_NAME);
	}
	
	@Test
	public void testCredentialsLengths() {
		assertNotNull(new ParamLengths.Credentials());
		
		assertEquals(3, ParamLengths.Credentials.MIN_USERNAME);
		assertEquals(3, ParamLengths.Credentials.MIN_PASSWORD);

		assertEquals(30, ParamLengths.Credentials.MAX_USERNAME);
		assertEquals(60, ParamLengths.Credentials.MAX_PASSWORD);
	}

	@Test
	public void testMenuItemLengths() {
		assertNotNull(new ParamLengths.MenuItem());
		
		assertEquals(1, ParamLengths.MenuItem.MIN_NAME);
		assertEquals(1, ParamLengths.MenuItem.MIN_DESC);

		assertEquals(100, ParamLengths.MenuItem.MAX_NAME);
		assertEquals(300, ParamLengths.MenuItem.MAX_DESC);
	}

	@Test
	public void testOrderLengths() {
		assertNotNull(new ParamLengths.Order());
		
		assertEquals(1, ParamLengths.Order.MIN_CONFIRMATION);
		assertEquals(50, ParamLengths.Order.MAX_CONFIRMATION);
	}
	
	@Test
	public void testPaymentLengths() {
		assertNotNull(new ParamLengths.Payment());
		
		assertEquals(1, ParamLengths.Payment.MIN_TRANSACTION_CONFORMATION);
		assertEquals(50, ParamLengths.Payment.MAX_TRANSACTION_CONFORMATION);
	}
	
	@Test
	public void testUserLengths() {
		assertNotNull(new ParamLengths.User());
		
		assertEquals(1, ParamLengths.User.MIN_F_NAME);
		assertEquals(1, ParamLengths.User.MIN_L_NAME);
		assertEquals(5, ParamLengths.User.MIN_EMAIL);
		assertEquals(10, ParamLengths.User.MIN_PHONE);
		
		assertEquals(20, ParamLengths.User.MAX_F_NAME);
		assertEquals(20, ParamLengths.User.MAX_L_NAME);
		assertEquals(50, ParamLengths.User.MAX_EMAIL);
		assertEquals(15, ParamLengths.User.MAX_PHONE);
	}
}
