package edu.depaul.se491.utils;

public abstract class ParamLabels {

	public static final class Order {
		public static final String ID = "orderId";
		public static final String TYPE = "orderType";
		public static final String STATUS = "orderStatus";
		public static final String TIMESTAMP = "orderTime";
		public static final String CONFIRMATION = "orderConf";
		public static final String NOTIFICATION_EMAIL = "orderNotificationEmail";
	}
	
	public static final class Payment {
		public static final String ID = "paymentId";
		public static final String TOTAL = "paymentTotal";
		public static final String TYPE = "paymentType";
	}
		
	public static final class CreditCard {
		public static final String CC_NUMBER = "ccNumber";
		public static final String CC_HOLDER_NAME = "ccHolderName";
		public static final String CC_EXP_MONTH = "ccExpMonth";
		public static final String CC_EXP_YEAR = "ccExpYear";
		
	}
		
	public static final class OrderItem {
		public static final String ORDER_ITEM = "orderItem";
		public static final String MENU_ITEM = "oImItemId";
		public static final String QUANTITY = "oItemQty";
	}
	
	public static final class MenuItem {
		public static final String ID = "mItemId";
		public static final String NAME = "mItemName";
		public static final String PRICE = "mItemPrice";
		public static final String DESC = "mItemDesc";	
		public static final String ITEM_CATEGORY = "mItemCategory";
	}
	
	
	public static final class Account {
		public static final String USERNAME = "accntUsername";
		public static final String PASSWORD = "accntPassword";
		public static final String ROLE = "accntRole";	
	}
		
	public static final class User {
		public static final String ID = "userId";
		public static final String F_NAME = "userFName";
		public static final String L_NAME = "userLName";
		public static final String EMAIL = "userEmail";	
		public static final String PHONE = "userPhone";	
	}
	
	public static final class Address {
		public static final String ID = "addrId";
		public static final String LINE_1 = "addrLine1";
		public static final String LINE_2 = "addrLine2";
		public static final String CITY = "addrCity";
		public static final String STATE = "addrState";
		public static final String ZIP_CODE = "addrZipCode";
	}
	
	public static final class Response {
		public static final String CREATED = "created";
		public static final String UPDATED = "updated";
		public static final String DELETED = "deleted";
		
		public static final String ERROR_FLAG = "error";
		public static final String ERROR_MSG = "errorMsg";	
		
		public static final String NULL_MSG = "nullMsg";
	}
	
	public static final class Request {
		public static final String REQUEST = "request";
	}
	
	public static final class Credentials {
		public static final String CREDENTIALS = "credentials";
	}
}
