package edu.sepaul.se491.utils;

import static org.junit.Assert.*;

import java.time.Year;

import org.junit.Test;

import edu.depaul.se491.utils.ParamValues;

public class ParamValuesTest {

	@Test
	public void testCreditCardValues() {
		assertEquals(1, ParamValues.CreditCard.MIN_EXP_MONTH);
		assertEquals((Year.now().getValue() - 20), ParamValues.CreditCard.MIN_EXP_YEAR);
		
		assertEquals(12, ParamValues.CreditCard.MAX_EXP_MONTH);
		assertEquals((Year.now().getValue() + 20), ParamValues.CreditCard.MAX_EXP_YEAR);
	}

	@Test
	public void testMenuItemValues() {
		assertEquals(0, Double.compare(0, ParamValues.MenuItem.MIN_PRICE));
		assertEquals(0, Double.compare(999.9, ParamValues.MenuItem.MAX_PRICE));
	}
	

	@Test
	public void testOrderValues() {
		assertEquals(1, ParamValues.Order.MIN_ORDER_ITEMS);
		assertEquals(100, ParamValues.Order.MAX_ORDER_ITEMS);
	}
	
	@Test
	public void testOrderItemValues() {
		assertEquals(0, ParamValues.OrderItem.MIN_QTY);
		assertEquals(65535, ParamValues.OrderItem.MAX_QTY);
	}
	
	@Test
	public void testPaymentValues() {
		assertEquals(0, Double.compare(0, ParamValues.Payment.MIN_TOTAL));
		assertEquals(0, Double.compare(99999.99, ParamValues.Payment.MAX_TOTAL));
	}
}
