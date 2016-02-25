package edu.depaul.se491.utils;

import java.time.Year;

/**
 * utility for parameter (min-max) values.
 * 
 * @author Malik
 */
public class ParamValues {
	
	public static final class MenuItem {
		public static final double MIN_PRICE = 0; /*MySQL Decimal(5,2) ---.--*/

		public static final double MAX_PRICE = 999.9; /*MySQL Decimal(5,2) ---.--*/
	}
	
	public static final class Order {
		public static final int MIN_ORDER_ITEMS = 1;
		
		public static final int MAX_ORDER_ITEMS = 100;
	}
	
	public static final class OrderItem {
		public static final int MIN_QTY = 0; 	

		public static final int MAX_QTY = 65535; 		/*MySQL unsigned smallInt*/ 
	}
	
	public static final class Payment {
		public static final double MIN_TOTAL = 0; 					 /*MYSQL Decimal(7,2) --,---.--*/ 
		
		public static final double MAX_TOTAL = 99999.99; 			 /*MYSQL Decimal(7,2) --,---.--*/ 
	}
	
	public static final class CreditCard {
		private static final int CURRENT_YEAR = Year.now().getValue();
		
		public static final int MIN_EXP_MONTH = 1;				 /* January = 1*/
		public static final int MIN_EXP_YEAR = CURRENT_YEAR - 20; /* no standard among CC issuers so use 20 years back */

		
		public static final int MAX_EXP_MONTH = 12;				 /* December = 12*/
		public static final int MAX_EXP_YEAR = CURRENT_YEAR + 20; /* no standard among CC issuer so use 20 years from now */
	}	
}
