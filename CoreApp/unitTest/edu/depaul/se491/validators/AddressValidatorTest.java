package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.enums.AddressState;
import edu.depaul.se491.utils.ParamLengths;

public class AddressValidatorTest {
	private AddressValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new AddressValidator();
	}

	@Test
	public void testValidate() {
		// null
		assertFalse(validator.validate(null, true));
		assertFalse(validator.validate(null, false));

		// bean with id = 0 (new)
		// bean with id > 0 (old)
		
		// valid new & old
		assertTrue(validator.validate(getValidBean(0), true));
		assertTrue(validator.validate(getValidBean(1), false));
		
		
		// invalid id (new but id > 0 & old but id < 1)
		assertFalse(validator.validate(getValidBean(1), true));
		assertFalse(validator.validate(getValidBean(0), false));
		
		// invalid line1 (new & old)
		assertFalse(validator.validate(getBeanWithInvalidLine1(0), true));
		assertFalse(validator.validate(getBeanWithInvalidLine1(1), false));

		// invalid line2 (new & old)
		assertFalse(validator.validate(getBeanWithInvalidLine2(0), true));
		assertFalse(validator.validate(getBeanWithInvalidLine2(1), false));

		// invalid city (new & old)
		assertFalse(validator.validate(getBeanWithInvalidCity(0), true));
		assertFalse(validator.validate(getBeanWithInvalidCity(1), false));

		// invalid ZipCode (new & old)
		assertFalse(validator.validate(getBeanWithInvalidState(0), true));
		assertFalse(validator.validate(getBeanWithInvalidState(1), false));

		// invalid ZipCode (new & old)
		assertFalse(validator.validate(getBeanWithInvalidZipCode(0), true));
		assertFalse(validator.validate(getBeanWithInvalidZipCode(1), false));


	}

	@Test
	public void testValidateId() {
		
		boolean isNewAddress;
		
		// null
		Long id = null;
		isNewAddress = false;
		assertFalse(validator.validateId(id, isNewAddress));

		
		isNewAddress = true;
		assertFalse(validator.validateId(id, isNewAddress));
		
		// zero (the L to make 0 long instead of int)
		id = 0L; 
		isNewAddress = true;
		assertTrue(validator.validateId(id, isNewAddress));
		
		isNewAddress = false;
		assertFalse(validator.validateId(id, isNewAddress));

		// > 0
		id = Long.MAX_VALUE;
		isNewAddress = false;
		assertTrue(validator.validateId(id, isNewAddress));
		
		isNewAddress = true;
		assertFalse(validator.validateId(id, isNewAddress));
		

		// < 0
		id = Long.MIN_VALUE;
		isNewAddress = true;
		assertFalse(validator.validateId(id, isNewAddress));
		
		isNewAddress = false;
		assertFalse(validator.validateId(id, isNewAddress));		
	}
	
	private AddressBean getValidBean(long id) {
		AddressBean bean = new AddressBean();
		bean.setId(id);
		bean.setLine1("123 Avenue");
		bean.setLine2("Unit 101");
		bean.setCity("Milwaukee");
		bean.setState(AddressState.AZ);
		bean.setZipcode("60001");
		return bean;
	}

	private AddressBean getBeanWithInvalidLine1(long id) {
		AddressBean bean = getValidBean(id);
		
		String moreThanMaxLen = getLongString(ParamLengths.Address.MAX_LINE1 + 1);
		bean.setLine1(moreThanMaxLen);
		
		return bean;
	}
	
	private AddressBean getBeanWithInvalidLine2(long id) {
		AddressBean bean = getValidBean(id);
		
		String moreThanMaxLen = getLongString(ParamLengths.Address.MAX_LINE2 + 1);
		bean.setLine2(moreThanMaxLen);
		
		return bean;
	}
	
	private AddressBean getBeanWithInvalidCity(long id) {
		AddressBean bean = getValidBean(id);
		
		String moreThanMaxLen = getLongString(ParamLengths.Address.MAX_CITY + 1);
		bean.setCity(moreThanMaxLen);
		
		return bean;
	}
	
	private AddressBean getBeanWithInvalidState(long id) {
		AddressBean bean = getValidBean(id);
		bean.setState(null);
		return bean;
	}
	
	private AddressBean getBeanWithInvalidZipCode(long id) {
		AddressBean bean = getValidBean(id);
		
		String moreThanMaxLen = getLongString(ParamLengths.Address.MAX_ZIPCODE + 1);
		bean.setZipcode(moreThanMaxLen);
		
		return bean;
	}
	
	private String getLongString(int maxLength) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < maxLength; i++)
			sb.append("x");
		
		return sb.toString();
	}
}
