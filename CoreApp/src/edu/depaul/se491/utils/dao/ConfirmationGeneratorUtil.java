/**
 * Class for generating confirmation numbers
 */
package edu.depaul.se491.utils.dao;

import java.util.Random;

/**
 * @author Malik
 *
 */
public abstract class ConfirmationGeneratorUtil {
	private static final Random rand;
	
	static {
		rand = new Random();
		rand.setSeed(System.currentTimeMillis());
	}
	
	public static synchronized String getConfirmation() {
		// return a string hashcode of (currentTimeMillis + random integer)
		return "confirmation#" + Integer.toString(Long.toString(System.currentTimeMillis() + rand.nextInt()).hashCode());
	}
}
