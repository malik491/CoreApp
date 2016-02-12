package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.enums.OrderItemStatus;

public class OrderItemValidatorTest {
	private OrderItemValidator validator;
	
	@Before
	public void setUp() throws Exception {
		validator = new OrderItemValidator();
	}

	@Test
	public void testValidate() {
		// null
		assertFalse(validator.validate(null));
		
		// valid id (new and old)
		assertTrue(validator.validate( getValidBean(0) ) );
		assertTrue(validator.validate( getValidBean(1) ) );

		// invalid MenuItem (new and old)
		assertFalse(validator.validate(getBeanWithInvalidMenuItem(0)));
		assertFalse(validator.validate(getBeanWithInvalidMenuItem(1)));
		
		// invalid Quantity (new and old)
		assertFalse(validator.validate(getBeanWithInvalidQuantity(0)));
		assertFalse(validator.validate(getBeanWithInvalidQuantity(1)));

		// invalid OrderItemStatus (new and old)
		assertFalse(validator.validate(getBeanWithInvalidOrderItemStatus(0)));
		assertFalse(validator.validate(getBeanWithInvalidOrderItemStatus(1)));
	}

	private OrderItemBean getValidBean(long id) {
		
		OrderItemBean bean = new OrderItemBean();
		MenuItemBean menuItem = new MenuItemBean();
		
		menuItem.setDescription("Hamburger, lettuce and cheese");
		menuItem.setId(id);
		menuItem.setItemCategory(MenuItemCategory.BEVERAGE);
		menuItem.setName("Hamburger");
		menuItem.setPrice(2.0);
		
		bean.setMenuItem(menuItem);
		bean.setQuantity(2);
		bean.setStatus(OrderItemStatus.READY);

		return bean;
	}
	
	private OrderItemBean getBeanWithInvalidMenuItem(long id) {
		OrderItemBean bean = getValidBean(id);
		MenuItemBean invalidMenuItem = null;
		bean.setMenuItem(invalidMenuItem);
		
		return bean;
	}
	
	
	private OrderItemBean getBeanWithInvalidQuantity(long id) {
		OrderItemBean bean = getValidBean(id);
		bean.setQuantity(-1);
		
		return bean;
	}
	
	private OrderItemBean getBeanWithInvalidOrderItemStatus(long id) {
		OrderItemBean bean = getValidBean(id);
		bean.setStatus(null);
		
		return bean;
	}
	
}
