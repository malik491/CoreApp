package edu.depaul.se491.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEnums {

	@Test
	public void test() {
		for (OrderStatus status : OrderStatus.values())
			assertNotNull(status.toString());
		
		for (OrderItemStatus status : OrderItemStatus.values())
			assertNotNull(status.toString());
		
		for (OrderType type : OrderType.values())
			assertNotNull(type.toString());
		
		for (MenuItemCategory category : MenuItemCategory.values())
			assertNotNull(category.toString());
		
		for (PaymentType type : PaymentType.values())
			assertNotNull(type.toString());
		
		for (AccountRole role : AccountRole.values())
			assertNotNull(role.toString());

	}

}
