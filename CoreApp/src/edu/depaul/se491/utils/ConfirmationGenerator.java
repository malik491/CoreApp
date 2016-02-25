package edu.depaul.se491.utils;

import java.util.Random;

import edu.depaul.se491.beans.CreditCardBean;

/**
 * Utility for generating confirmation
 * 
 * @author Malik
 */
public class ConfirmationGenerator {
	private static final Random random = new Random(System.currentTimeMillis());

	/**
	 * return unique order confirmation
	 * @return
	 */
	public static synchronized String getOrderConfirmation() {
		String confirmation = String.format("order-%d-%d-%d", random.nextInt(10000), random.nextInt(10000), random.nextInt(10000));
		return confirmation;
	}

	/**
	 * return unique credit card transaction confirmation
	 * @param creditCard
	 * @return
	 */
	public static synchronized String getPaymentConfirmation(CreditCardBean creditCard) {
		String confirmation = String.format("cc-payment-%d-%d-%d", random.nextInt(10000), random.nextInt(10000), random.nextInt(10000));
		return confirmation;
	}

}
