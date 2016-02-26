package edu.depaul.se491.validators;

import org.apache.commons.lang3.StringUtils;

/**
 * Base bean validator
 * 
 * @author Malik
 */
class BeanValidator {
	
	/**
	 * return true if (s is not null, s is not blank string (empty or all white spaces), and ( min <= s length <= max)
	 * it adds InvalidMsg if the return value is false
	 * @param s string to validate
	 * @param minLength
	 * @param maxLength
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidString(String s, int minLength, int maxLength) {
		boolean isValid = StringUtils.isNotBlank(s);
		isValid &= isValidLength(s, minLength, maxLength);

		return isValid;		
	}
	
	/**
	 * returns true is all characters in s are digit characters
	 * @param s
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidNumbericString(String s) {
		boolean isValid = StringUtils.isNotBlank(s);
		
		if (isValid) {
			for (char c: s.toCharArray()) {
				if (!Character.isDigit(c)) {
					isValid = false;
					break;
				}
			}
		}
		
		return isValid;
	}

	/**
	 * return true is id is valid:
	 * if isNewBean, id must be 0 else id must be > 0
	 * @param id
	 * @param isNewBean
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidId(long id, boolean isNewBean) {
		return isNewBean? (id == 0) : (id > 0);
	}
	
	/**
	 * return true if value is within min and max (inclusive)
	 * min <= value <= max
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	protected boolean isValidValue(double value, double min, double max) {
		return Double.compare(min, value) <= 0 && Double.compare(value, max) <= 0;
	}

	/**
	 * return true if value is within min and max (inclusive)
	 * min <= value <= max
	 * @param value
	 * @param min
	 * @param max
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidValue(int value, int min, int max) {
		return min <= value && value <= max;		
	}
	
	/**
	 * return true if obj is not null
	 * @param obj
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidObject(Object obj) {
		return (obj != null);
	}
	
	
	/**
	 * return true is (minLength <= s.length <= maxLength). null safe
	 * @param s
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	protected boolean isValidLength(String s, int minLength, int maxLength) {
		return s == null? false : (minLength <= s.length() && s.length() <= maxLength);
	}
}
