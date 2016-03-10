package edu.depaul.se491.utils.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class DBLabelsTest {

	@Test
	public void test() {
		assertNotNull(new DBLabels());
	}
	
	@Test
	public void testAddress() {
		assertNotNull(new DBLabels.Address());
		assertNotNull(DBLabels.Address.TABLE);
		assertNotNull(DBLabels.Address.ID);		
		assertNotNull(DBLabels.Address.LINE_1);		
		assertNotNull(DBLabels.Address.LINE_2);		
		assertNotNull(DBLabels.Address.CITY);
		assertNotNull(DBLabels.Address.STATE);
		assertNotNull(DBLabels.Address.ZIPCODE);
	}

	@Test
	public void testAccount() {
		assertNotNull(new DBLabels.Account());
		assertNotNull(DBLabels.Account.TABLE);
		assertNotNull(DBLabels.Account.USERNAME);		
		assertNotNull(DBLabels.Account.PASSWORD);		
		assertNotNull(DBLabels.Account.ROLE);		
		assertNotNull(DBLabels.Account.USER_ID);
	}

	@Test
	public void testMenuItem() {
		assertNotNull(new DBLabels.MenuItem());
		assertNotNull(DBLabels.MenuItem.TABLE);
		assertNotNull(DBLabels.MenuItem.ID);		
		assertNotNull(DBLabels.MenuItem.NAME);		
		assertNotNull(DBLabels.MenuItem.DESC);		
		assertNotNull(DBLabels.MenuItem.PRICE);
		assertNotNull(DBLabels.MenuItem.CATEGORY);		
		assertNotNull(DBLabels.MenuItem.IS_HIDDENT);
	}

	@Test
	public void testUser() {
		assertNotNull(new DBLabels.User());
		assertNotNull(DBLabels.User.TABLE);
		assertNotNull(DBLabels.User.ID);		
		assertNotNull(DBLabels.User.F_NAME);		
		assertNotNull(DBLabels.User.L_NAME);		
		assertNotNull(DBLabels.User.EMAIL);
		assertNotNull(DBLabels.User.PHONE);		
		assertNotNull(DBLabels.User.ADDRESS_ID);
	}
	
	@Test
	public void testOrder() {
		assertNotNull(new DBLabels.Order());
		assertNotNull(DBLabels.Order.TABLE);
		assertNotNull(DBLabels.Order.ID);		
		assertNotNull(DBLabels.Order.STATUS);		
		assertNotNull(DBLabels.Order.TYPE);		
		assertNotNull(DBLabels.Order.CONFIRMATION);
		assertNotNull(DBLabels.Order.TIMESTAMP);
		assertNotNull(DBLabels.Order.PAYMENT_ID);
		assertNotNull(DBLabels.Order.ADDRESS_ID);
	}
	
	@Test
	public void testOrderItem() {
		assertNotNull(new DBLabels.OrderItem());
		assertNotNull(DBLabels.OrderItem.TABLE);
		assertNotNull(DBLabels.OrderItem.ORDER_ID);		
		assertNotNull(DBLabels.OrderItem.MENU_ITEM_ID);		
		assertNotNull(DBLabels.OrderItem.QUANTITY);		
		assertNotNull(DBLabels.OrderItem.STATUS);
	}
	
	@Test
	public void testPayment() {
		assertNotNull(new DBLabels.Payment());
		assertNotNull(DBLabels.Payment.TABLE);
		assertNotNull(DBLabels.Payment.ID);		
		assertNotNull(DBLabels.Payment.TYPE);		
		assertNotNull(DBLabels.Payment.TOTAL);		
		assertNotNull(DBLabels.Payment.CC_TRANSACTION_CONFIRMATION);
	}
}
