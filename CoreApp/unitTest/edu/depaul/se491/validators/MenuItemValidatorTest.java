package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

public class MenuItemValidatorTest {
	private MenuItemValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new MenuItemValidator();
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
		
		// invalid name (new & old)
		assertFalse(validator.validate(getBeanWithInvalidName(0), true));
		assertFalse(validator.validate(getBeanWithInvalidName(1), false));
		
		
		// invalid desc (new & old)
		assertFalse(validator.validate(getBeanWithInvalidDesc(0), true));
		assertFalse(validator.validate(getBeanWithInvalidDesc(1), false));
		
		// invalid price (old & old)
		assertFalse(validator.validate(getBeanWithInvalidPrice(0), true));
		assertFalse(validator.validate(getBeanWithInvalidPrice(1), false));

		// invalid menu item category (old & old)
		assertFalse(validator.validate(getBeanWithInvalidCategoryItem(0), true));
		assertFalse(validator.validate(getBeanWithInvalidCategoryItem(1), false));
		
	}

	@Test
	public void testValidateId() {
		boolean isNewMenuItem;
		
		// null
		Long id = null;
		isNewMenuItem = false;
		assertFalse(validator.validateId(id, isNewMenuItem));
		
		isNewMenuItem = true;
		assertFalse(validator.validateId(id, isNewMenuItem));
		
		
		
		// zero (the L to make 0 long instead of int)
		id = 0L; 
		isNewMenuItem = true;
		assertTrue(validator.validateId(id, isNewMenuItem));
		
		isNewMenuItem = false;
		assertFalse(validator.validateId(id, isNewMenuItem));
		
		// > 0
		id = Long.MAX_VALUE;
		isNewMenuItem = false;
		assertTrue(validator.validateId(id, isNewMenuItem));
		
		isNewMenuItem = true;
		assertFalse(validator.validateId(id, isNewMenuItem));
		
		// < 0
		id = Long.MIN_VALUE;
		isNewMenuItem = true;
		assertFalse(validator.validateId(id, isNewMenuItem));
		
		isNewMenuItem = false;
		assertFalse(validator.validateId(id, isNewMenuItem));		
		
	}
	
	
	private MenuItemBean getBeanWithInvalidName(long id) {
		MenuItemBean bean = getValidBean(id);
		
		String moreThanMaxLen = getLongString(ParamLengths.MenuItem.MAX_NAME + 1);
		bean.setName(moreThanMaxLen);
		
		return bean;
	}
	
	private MenuItemBean getBeanWithInvalidDesc(long id) {
		MenuItemBean bean = getValidBean(id);
		
		String moreThanMaxLen = getLongString(ParamLengths.MenuItem.MAX_DESC + 1);
		bean.setDescription(moreThanMaxLen);
		
		return bean;
	}
	
	private MenuItemBean getBeanWithInvalidPrice(long id) {
		MenuItemBean bean = getValidBean(id);
		bean.setPrice(ParamValues.MenuItem.MAX_PRICE + 1.00);
		return bean;
	}
	
	private MenuItemBean getBeanWithInvalidCategoryItem(long id) {
		MenuItemBean bean = getValidBean(id);
		bean.setItemCategory(null);
		return bean;
	}
	
	
	private String getLongString(int maxLength) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < maxLength; i++)
			sb.append("x");
		
		return sb.toString();
	}
	
	private MenuItemBean getValidBean(long id) {
		MenuItemBean bean = new MenuItemBean();
		bean.setId(id);
		bean.setName("name");
		bean.setPrice(1.99);
		bean.setDescription("description");
		bean.setItemCategory(MenuItemCategory.BEVERAGE);
		return bean;
	}

}
