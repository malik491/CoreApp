/**
 * Base class for all validator classes
 */
package edu.depaul.se491.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Malik
 *
 */
public abstract class BeanValidator {
	private List<String> messages;
	
	protected BeanValidator() {
		messages = new ArrayList<String>();
	}
	
	/**
	 * return validation error messages
	 * @return
	 */
	public List<String> getValidationMessages() {
		return messages;
	}
	
	/**
	 * add validation error message
	 * @param message
	 */
	protected void addMessage(String message) {
		messages.add(message);
	}
	
	/**
	 * return true if (s is not null, s is not blank string (empty or all white spaces), and ( min <= s length <= max)
	 * it adds InvalidMsg if the return value is false
	 * @param s string to validate
	 * @param minLength
	 * @param maxLength
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidString(String s, int minLength, int maxLength, String invalidMsg) {
		boolean isValid = StringUtils.isNotBlank(s);
		isValid &= isValidLength(s, minLength, maxLength);
		
		if (!isValid)
			addMessage(invalidMsg);		
		
		return isValid;		
	}
	
	/**
	 * returns true is all characters in s are digit characters
	 * @param s
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidNumbericString(String s, String invalidMsg) {
		boolean isValid = StringUtils.isNotBlank(s);
		
		if (isValid) {
			for (char c: s.toCharArray()) {
				if (!Character.isDigit(c)) {
					isValid = false;
					break;
				}
			}
		}
		
		if (!isValid)
			addMessage(invalidMsg);		
		
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
	protected boolean isValidId(long id, boolean isNewBean, String invalidMsg) {
		boolean isValid = isNewBean? (id == 0) : (id > 0);
		
		if (!isValid)
			addMessage(invalidMsg);
		
		return isValid;
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
	protected boolean isValidValue(double value, double min, double max, String invalidMsg) {
		boolean isValid = isValidValue(value, min, max);

		if (!isValid)
			addMessage(invalidMsg);
		
		return isValid;
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
	protected boolean isValidValue(int value, int min, int max, String invalidMsg) {
		boolean isValid = isValidValue(value, min, max);

		if (!isValid)
			addMessage(invalidMsg);
		
		return isValid;
	}
	
	/**
	 * return true if obj is not null
	 * @param obj
	 * @param invalidMsg
	 * @return
	 */
	protected boolean isValidObject(Object obj, String invalidMsg) {
		boolean isValid = (obj != null);
		
		if (!isValid)
			addMessage(invalidMsg);		
		
		return isValid;		
	}
	
	
	/**
	 * return true is (minLength <= s.length <= maxLength). null safe
	 * @param s
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	private boolean isValidLength(String s, int minLength, int maxLength) {
		return s == null? false : (minLength <= s.length() && s.length() <= maxLength);
	}
	
	/**
	 * return true is (min <= value <= max).
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	private boolean isValidValue(double value, double min, double max) {
		return Double.compare(min, value) <= 0 && Double.compare(value, max) <= 0;
	}

	/**
	 *  return true is (min <= value <= max).
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	private boolean isValidValue(int value, int min, int max) {
		return min <= value && value <= max;
	}
}
