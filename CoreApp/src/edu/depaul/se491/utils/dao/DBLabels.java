/**
 * 
 */
package edu.depaul.se491.utils.dao;

/**
 * a class to hold column names for tables in the database
 * use these labels to get the column data from a row in a ResultSet
 * Like this:
 * 
 * ResultSet rs = .... 
 * int id   = rs.getInt(columnLabel)
 * String s = rs.getString(columnLabel) 
 *
 * @author Malik
 */
public abstract class DBLabels {
	private DBLabels() {}
	
	public static final class Address {
		public static final String TABLE = "addresses";
		
		public static final String ID 	  = "addr_id";
		public static final String LINE_1 = "addr_line1";
		public static final String LINE_2 = "addr_line2";
		public static final String CITY   = "addr_city";
		public static final String STATE  = "addr_state";
		public static final String ZIPCODE = "addr_zipcode";
	}
	
	public static final class User {
		public static final String TABLE = "users";
		
		public static final String ID = "u_id";
		public static final String F_NAME = "u_first_Name";
		public static final String L_NAME = "u_last_Name";
		public static final String EMAIL = "u_email";	
		public static final String PHONE = "u_phone";
		public static final String ADDRESS_ID = DBLabels.Address.ID;
		
	}
	
	public static final class Account {
		public static final String TABLE = "accounts";
		
		public static final String USERNAME = "acc_username";
		public static final String PASSWORD = "acc_password";
		public static final String ROLE = "acc_role";
		public static final String USER_ID = DBLabels.User.ID;
	}
	
	
	public static final class MenuItem {
		public static final String TABLE = "menu_items";
		
		public static final String ID = "m_item_id";
		public static final String NAME = "m_item_name";
		public static final String DESC = "m_item_desc";
		public static final String PRICE = "m_item_price";
		public static final String CATEGORY = "m_item_category";
	}
	
	public static final class Payment {
		public static final String TABLE = "payments";
		
		public static final String ID = "p_id";
		public static final String TYPE = "p_type";
		public static final String TOTAL = "p_total";
		public static final String CC_TRANSACTION_CONFIRMATION = "p_cc_transaction_confm";
	}
	
	public static final class Order {
		public static final String TABLE = "orders";
		
		public static final String ID 		= "o_id";
		public static final String STATUS 	= "o_status";
		public static final String TYPE 	= "o_type";
		public static final String CONFIRMATION = "o_confirmation";
		public static final String TIMESTAMP = "o_timestamp";
		public static final String ADDRESS_ID = DBLabels.Address.ID;
		public static final String PAYMENT_ID = DBLabels.Payment.ID;
	}
	
	public static final class OrderItem {
		public static final String TABLE = "order_items";
		
		public static final String ORDER_ID = DBLabels.Order.ID;
		public static final String MENU_ITEM_ID = DBLabels.MenuItem.ID;
		public static final String QUANTITY = "o_item_qty";
		public static final String STATUS = "o_item_status";
		
	}
	
}
