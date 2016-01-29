package edu.depaul.se491.utils;

public abstract class ValidatorUtil {
	
	/**
	 * return true is (minLength <= s.length <= maxLength). null safe
	 * @param s
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static boolean isValidLength(String s, int minLength, int maxLength) {
		return s == null? false : (minLength <= s.length() && s.length() <= maxLength);
	}
	
	/**
	 * return true is (min <= value <= max).
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isValidValue(double value, double min, double max) {
		return Double.compare(min, value) <= 0 && Double.compare(value, max) <= 0;
	}

	/**
	 *  return true is (min <= value <= max).
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isValidValue(int value, int min, int max) {
		return min <= value && value <= max;
	}
}
