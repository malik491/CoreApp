package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BeanValidatorTest {
	private BeanValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new BeanValidator();
	}

	@Test
	public void testIsValidString() {
		assertTrue(validator.isValidString("1", 1, 2));
		assertTrue(validator.isValidString("12", 1, 2));
		
		assertFalse(validator.isValidString(null, 0, 1)); // null
		assertFalse(validator.isValidString("", 0, 1));  // blank (empty)
		assertFalse(validator.isValidString(" ", 0, 1)); // blank (all white space)
		assertFalse(validator.isValidString("1", 2, 3));  // < min
		assertFalse(validator.isValidString("123", 1, 2)); // > max
	}

	@Test
	public void testIsValidNumbericString() {
		assertTrue(validator.isValidNumbericString("1234"));
		
		assertFalse(validator.isValidNumbericString(null)); // null
		assertFalse(validator.isValidNumbericString(""));  // blank (empty)
		assertFalse(validator.isValidNumbericString(" ")); // blank (all white space)
		assertFalse(validator.isValidNumbericString(" 123"));
		assertFalse(validator.isValidNumbericString("12 3"));
		assertFalse(validator.isValidNumbericString("123 "));
		assertFalse(validator.isValidNumbericString("12-3"));
	}

	@Test
	public void testIsValidId() {
		assertTrue(validator.isValidId(0L, true));
		assertTrue(validator.isValidId(1L, false));

		assertFalse(validator.isValidId(0L, false));
		assertFalse(validator.isValidId(1L, true));
		assertFalse(validator.isValidId(-1L, false));
		assertFalse(validator.isValidId(-1L, true));
	}

	@Test
	public void testIsValidValueDoubleDoubleDouble() {
		assertTrue(validator.isValidValue(0.99, 0.99, 1.00));    // min = v < max
		assertTrue(validator.isValidValue(0.998, 0.997, 0.999)); // min < v < max
		assertTrue(validator.isValidValue(1.00, 0.99, 1.00));    // min < v = max
		
		assertFalse(validator.isValidValue(0, 1.00, 2.00));
		assertFalse(validator.isValidValue(2.01, 1.00, 2.00));
	}

	@Test
	public void testIsValidValueIntIntInt() {
		assertTrue(validator.isValidValue(1,1,3)); // min = v < max
		assertTrue(validator.isValidValue(2,1,3)); // min < v < max
		assertTrue(validator.isValidValue(3,1,3)); // min < v = max
		
		assertFalse(validator.isValidValue(1, 2, 3));
		assertFalse(validator.isValidValue(3, 1, 2));
	}

	@Test
	public void testIsValidObject() {
		assertTrue(validator.isValidObject("abc"));
		assertFalse(validator.isValidObject(null));
	}

	@Test
	public void testIsValidLength() {
		assertTrue(validator.isValidLength("", 0, 3));
		assertTrue(validator.isValidLength("12", 1, 3));
		assertTrue(validator.isValidLength("123", 1, 3));
		
		assertFalse(validator.isValidLength("", 1, 2));		
		assertFalse(validator.isValidLength("123", 1, 2));
	}

}
