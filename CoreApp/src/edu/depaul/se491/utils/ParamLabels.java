package edu.depaul.se491.utils;

public abstract class ParamLabels {

	public static final class MenuItem {
		public static final String MENU_ITEM_BEAN = "menuItemBean";  			// use this label to mean a menu item bean
		public static final String MENU_ITEM_BEAN_LIST = "menuItemList";	// use this label to mean a list of menu item beans
		
		// these for parameter names (jsp html form & servlets)
		public static final String ID = "mItemId";
		public static final String NAME = "mItemName";
		public static final String PRICE = "mItemPrice";
		public static final String DESC = "mItemDesc";	
		public static final String ITEM_CATEGORY = "mItemCategory";
	}
	
	
	public static final class Account {
		public static final String ACCOUNT_BEAN = "accountBean";
		public static final String ACCOUNT_BEAN_LIST = "accountList";
		
		public static final String ROLE = "accntRole";
	}
	
	public static final class Credentials {
		public static final String CREDENTIALS_BEAN = "credentialsBean";
		
		public static final String USERNAME = "credentialsUsername";
		public static final String PASSWORD = "credentialsPassword";
	}
	
	public static final class User {
		public static final String USER_BEAN = "userBean";
		
		public static final String ID = "userId";
		public static final String F_NAME = "userFName";
		public static final String L_NAME = "userLName";
		public static final String EMAIL = "userEmail";	
		public static final String PHONE = "userPhone";	
	}
	
	public static final class Address {		
		public static final String ADDRESS_BEAN = "addressBean";
		
		public static final String ID = "addrId";
		public static final String LINE_1 = "addrLine1";
		public static final String LINE_2 = "addrLine2";
		public static final String CITY = "addrCity";
		public static final String STATE = "addrState";
		public static final String ZIP_CODE = "addrZipCode";
	}
	
	
	public static final class Order {
		public static final String ORDER_BEAN = "orderBean";
		public static final String ORDER_BEAN_LIST = "orderList";
		
		public static final String ID = "orderId";
		public static final String TYPE = "orderType";
		public static final String STATUS = "orderStatus";
		public static final String TIMESTAMP = "orderTime";
		public static final String CONFIRMATION = "orderConf";
		public static final String NOTIFICATION_EMAIL = "orderNotificationEmail";
	}
	
	public static final class OrderItem {
		public static final String ORDER_ITEM = "orderItem";
		public static final String MENU_ITEM = "oImItemId";
		public static final String QUANTITY = "oItemQty";
	}
	
	public static final class Payment {
		public static final String PAYMENT_BEAN = "paymentBean";
		
		public static final String ID = "paymentId";
		public static final String TOTAL = "paymentTotal";
		public static final String TYPE = "paymentType";
	}
		
	public static final class CreditCard {
		public static final String CRIDET_CARD_BEAN = "ccBean";
		
		public static final String CC_NUMBER = "ccNumber";
		public static final String CC_HOLDER_NAME = "ccHolderName";
		public static final String CC_EXP_MONTH = "ccExpMonth";
		public static final String CC_EXP_YEAR = "ccExpYear";
	}

	public static final class JspMsg {
		public static final String MSG = "msg";
	}

}
