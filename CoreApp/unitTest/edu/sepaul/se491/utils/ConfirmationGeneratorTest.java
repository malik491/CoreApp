package edu.sepaul.se491.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.depaul.se491.beans.CreditCardBean;
import edu.depaul.se491.utils.ConfirmationGenerator;

public class ConfirmationGeneratorTest {

	@Test
	public void testConfirmationGenerator() {
		assertNotNull(new ConfirmationGenerator());
	}
	
	@Test
	public void testGetOrderConfirmation() {
		assertNotNull(ConfirmationGenerator.getOrderConfirmation());
	}

	@Test
	public void testGetPaymentConfirmation() {
		assertNotNull(ConfirmationGenerator.getPaymentConfirmation(new CreditCardBean("123456789012", "name", 5, 2016)));
	}

}
