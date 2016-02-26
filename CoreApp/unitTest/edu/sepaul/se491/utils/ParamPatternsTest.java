package edu.sepaul.se491.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.depaul.se491.utils.ParamPatterns;

/**
 * @author Malik
 */
public class ParamPatternsTest {

	@Test
	public void test() {
		assertNotNull(new ParamPatterns());
		assertNotNull(new ParamPatterns.Address());
		assertNotNull(new ParamPatterns.Credentials());
		assertNotNull(new ParamPatterns.CreditCard());
		assertNotNull(new ParamPatterns.MenuItem());
		assertNotNull(new ParamPatterns.User());
		
		assertNotNull(ParamPatterns.Address.LINE_1);
		assertNotNull(ParamPatterns.Address.CITY);
		assertNotNull(ParamPatterns.Address.ZIPCODE);
		
		assertNotNull(ParamPatterns.Credentials.PASSWORD);
		assertNotNull(ParamPatterns.Credentials.USERNAME);
		
		
		assertNotNull(ParamPatterns.CreditCard.HOLDER_NAME);
		assertNotNull(ParamPatterns.CreditCard.NUMBER);
	}

}
