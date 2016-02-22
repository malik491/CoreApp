package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.CreditCardBean;
import edu.depaul.se491.utils.ParamLengths;

public class CreditCardValidatorTest {
private CreditCardValidator validator;
	@Before
	public void setUp() throws Exception {
		validator = new CreditCardValidator();
	}

	@Test
	public void testValidate() {
		
		// null
		assertFalse(validator.validate(null));
		
		CreditCardBean bean = new CreditCardBean();
		
		//not null
		assertFalse(validator.validate(bean));
		
		bean.setCcHolderName("name");
		bean.setCcNumber("1234567890123");
		bean.setExpMonth(2);
		bean.setExpYear(2018);
		
		//valid bean
		assertTrue(validator.validate(bean));
		
		//ccHolderName < MIN_CC_HOLDER_NAME
		bean.setCcHolderName("la");		
		assertFalse(validator.validate(bean));
		
		//ccHolderName > MAX_CC_HOLDER_NAME
		String moreThanMaxLen = getLongString(ParamLengths.CreditCard.MAX_HOLDER_NAME + 1);
		bean.setCcHolderName(moreThanMaxLen);		
		assertFalse(validator.validate(bean));
		
		//ccNumber < MIN_CC_NUMBER
		bean.setCcHolderName("name");
		bean.setCcNumber("12345678901");
		bean.setExpMonth(2);
		bean.setExpYear(2018);
		assertFalse(validator.validate(bean));
		
		//ccNumber > MAX_CC_NUMBER
		moreThanMaxLen = getLongString(ParamLengths.CreditCard.MAX_NUMBER + 1);
		bean.setCcHolderName("name");
		bean.setCcNumber(moreThanMaxLen);
		bean.setExpMonth(2);
		bean.setExpYear(2018);
		assertFalse(validator.validate(bean));
		
		//ccMonth < MIN_CC_EXP_MONTH
		bean.setCcHolderName("name");
		bean.setCcNumber("1234567890123");
		bean.setExpMonth(0);
		bean.setExpYear(2018);
		assertFalse(validator.validate(bean));
		
		//ccMonth > MAX_CC_EXP_MONTH
		bean.setCcHolderName("name");
		bean.setCcNumber("1234567890123");
		bean.setExpMonth(13);
		bean.setExpYear(2018);
		assertFalse(validator.validate(bean));
		
		//ccYear < MIN_CC_EXP_YEAR
		bean.setCcHolderName("name");
		bean.setCcNumber("1234567890123");
		bean.setExpMonth(2);
		bean.setExpYear(1995);
		assertFalse(validator.validate(bean));
		
		//ccYear > MAX_CC_EXP_YEAR
		bean.setCcHolderName("name");
		bean.setCcNumber("1234567890123");
		bean.setExpMonth(2);
		bean.setExpYear(2037);
		assertFalse(validator.validate(bean));

	}
	
	private String getLongString(int maxLength) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < maxLength; i++)
			sb.append("x");
		
		return sb.toString();
	}

}
