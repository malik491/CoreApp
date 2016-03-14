package edu.depaul.se491.utils;

import static org.junit.Assert.*;

import java.time.Year;

import org.junit.Test;

import edu.depaul.se491.utils.ParamValues;

/**
 * @author Malik
 */
public class ParamValuesTest {

	@Test
	public void testParamValues() {
		assertNotNull(new ParamValues());
	}
	
	@Test
	public void testCreditCardValues() {
		assertNotNull(new ParamValues.CreditCard());
		
		assertEquals(1, ParamValues.CreditCard.MIN_EXP_MONTH);
		assertEquals((Year.now().getValue()), ParamValues.CreditCard.MIN_EXP_YEAR);
		
		assertEquals(12, ParamValues.CreditCard.MAX_EXP_MONTH);
		assertEquals((Year.now().getValue() + 20), ParamValues.CreditCard.MAX_EXP_YEAR);
	}

	@Test
	public void testMenuItemValues() {
		assertNotNull(new ParamValues.MenuItem());
		
		assertEquals(0, Double.compare(0, ParamValues.MenuItem.MIN_PRICE));
		assertEquals(0, Double.compare(999.99, ParamValues.MenuItem.MAX_PRICE));
	}
	

	@Test
	public void testOrderValues() {
		assertNotNull(new ParamValues.Order());
		
		assertEquals(1, ParamValues.Order.MIN_ORDER_ITEMS);
		assertEquals(100, ParamValues.Order.MAX_ORDER_ITEMS);
	}
	
	@Test
	public void testOrderItemValues() {
		assertNotNull(new ParamValues.OrderItem());
		
		assertEquals(0, ParamValues.OrderItem.MIN_QTY);
		assertEquals(65535, ParamValues.OrderItem.MAX_QTY);
	}
	
	@Test
	public void testPaymentValues() {
		assertNotNull(new ParamValues.Payment());
		
		assertEquals(0, Double.compare(0, ParamValues.Payment.MIN_TOTAL));
		assertEquals(0, Double.compare(99999.99, ParamValues.Payment.MAX_TOTAL));
	}
}
