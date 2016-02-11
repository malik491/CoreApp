/**
 * 
 */
package edu.depaul.se491.utils;

import java.util.Random;

import edu.depaul.se491.beans.CreditCardBean;

/**
 * @author Malik
 *
 */
public abstract class ConfirmationGenerator {
	private static final Random random = new Random(System.currentTimeMillis());

	public static synchronized String getOrderConfirmation() {
		String confirmation = String.format("order-%d-%d-%d", random.nextInt(10000), random.nextInt(10000), random.nextInt(10000));
		return confirmation;
	}

	public static synchronized String getPaymentConfirmation(CreditCardBean creditCard) {
		String confirmation = String.format("cc-payment-%d-%d-%d", random.nextInt(10000), random.nextInt(10000), random.nextInt(10000));
		return confirmation;
	}

}
