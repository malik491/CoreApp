package edu.sepaul.se491.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.depaul.se491.utils.ParamLabels;

/**
 * @author Malik
 */
public class ParamLabelsTest {
	
	@Test
	public void testParamLabels() {
		assertNotNull(new ParamLabels());
	}

	@Test
	public void testOrderLabels() {
		assertNotNull(new ParamLabels.Order());
				
		assertNotNull(ParamLabels.Order.ORDER_BEAN);
		assertNotNull(ParamLabels.Order.ORDER_BEAN_LIST);
		assertNotNull(ParamLabels.Order.ID);
		assertNotNull(ParamLabels.Order.TYPE);
		assertNotNull(ParamLabels.Order.CONFIRMATION);
		assertNotNull(ParamLabels.Order.CONFIRMATION);
	}
	
	@Test
	public void testOrderItemLabels() {
		assertNotNull(new ParamLabels.OrderItem());
		
		assertNotNull(ParamLabels.OrderItem.MENU_ITEM);
		assertNotNull(ParamLabels.OrderItem.ORDER_ITEM);
		assertNotNull(ParamLabels.OrderItem.QUANTITY);
		assertNotNull(ParamLabels.OrderItem.STATUS);
	}

	@Test
	public void testMenuItemLabels() {
		assertNotNull(new ParamLabels.MenuItem());
		
		assertNotNull(ParamLabels.MenuItem.MENU_ITEM_BEAN);
		assertNotNull(ParamLabels.MenuItem.MENU_ITEM_BEAN_LIST);
		assertNotNull(ParamLabels.MenuItem.ID);
		assertNotNull(ParamLabels.MenuItem.NAME);
		assertNotNull(ParamLabels.MenuItem.DESC);
		assertNotNull(ParamLabels.MenuItem.PRICE);
		assertNotNull(ParamLabels.MenuItem.ITEM_CATEGORY);
	}

	@Test
	public void testAccountLabels() {
		assertNotNull(new ParamLabels.Account());
		
		assertNotNull(ParamLabels.Account.ACCOUNT_BEAN);
		assertNotNull(ParamLabels.Account.ACCOUNT_BEAN_LIST);
		assertNotNull(ParamLabels.Account.ROLE);
	}
	
	@Test
	public void testCredentialsLabels() {
		assertNotNull(new ParamLabels.Credentials());
		
		assertNotNull(ParamLabels.Credentials.CREDENTIALS_BEAN);
		assertNotNull(ParamLabels.Credentials.USERNAME);
		assertNotNull(ParamLabels.Credentials.PASSWORD);
	}
	
	@Test
	public void testCreditCardLabels() {
		assertNotNull(new ParamLabels.CreditCard());
		
		assertNotNull(ParamLabels.CreditCard.CRIDET_CARD_BEAN);
		assertNotNull(ParamLabels.CreditCard.NUMBER);
		assertNotNull(ParamLabels.CreditCard.HOLDER_NAME);
		assertNotNull(ParamLabels.CreditCard.EXP_MONTH);
		assertNotNull(ParamLabels.CreditCard.EXP_YEAR);
	}
	
	@Test
	public void testPaymentLabels() {
		assertNotNull(new ParamLabels.Payment());
		
		assertNotNull(ParamLabels.Payment.PAYMENT_BEAN);
		assertNotNull(ParamLabels.Payment.ID);
		assertNotNull(ParamLabels.Payment.TYPE);
		assertNotNull(ParamLabels.Payment.TOTAL);
		assertNotNull(ParamLabels.Payment.CC_TRANSACTION_CONFIRMATION);
	}
	
	@Test
	public void testAddressLabels() {
		assertNotNull(new ParamLabels.Address());
		
		assertNotNull(ParamLabels.Address.ADDRESS_BEAN);
		assertNotNull(ParamLabels.Address.ID);
		assertNotNull(ParamLabels.Address.LINE_1);
		assertNotNull(ParamLabels.Address.LINE_2);
		assertNotNull(ParamLabels.Address.CITY);
		assertNotNull(ParamLabels.Address.STATE);
		assertNotNull(ParamLabels.Address.ZIP_CODE);
	}
	
	@Test
	public void testUserLabels() {
		assertNotNull(new ParamLabels.User());
		
		assertNotNull(ParamLabels.User.USER_BEAN);
		assertNotNull(ParamLabels.User.ID);
		assertNotNull(ParamLabels.User.F_NAME);
		assertNotNull(ParamLabels.User.L_NAME);
		assertNotNull(ParamLabels.User.EMAIL);
		assertNotNull(ParamLabels.User.PHONE);
	}
	
	@Test
	public void testJSPLabels() {
		assertNotNull(new ParamLabels.JspMsg());
				
		assertNotNull(ParamLabels.JspMsg.MSG);
	}
}

