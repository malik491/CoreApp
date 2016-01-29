package edu.depaul.se491.utils;

public class ParamLengths {


	public static final class Order {
		public static final int MIN_CONFIRMATION = 1;
		public static final int MIN_NOTIFICATION_EMAIL = ParamLengths.Email.MIN_ADDR;

		public static final int MAX_CONFIRMATION = 50;
		public static final int MAX_NOTIFICATION_EMAIL = ParamLengths.Email.MAX_ADDR;

	}
	
	public static final class Payment {
		public static final int MIN_TRANSACTION_CONFORMATION = 1;

		public static final int MAX_TRANSACTION_CONFORMATION = 50;		
	}
	
	public static final class CreditCard {
		public static final int MIN_CC_NUMBER = 12;   		/*CC number range from 12 to 19 digit string*/
		public static final int MIN_CC_HOLDER_NAME = 3;		/* holder's full name*/

		public static final int MAX_CC_NUMBER = 19;
		public static final int MAX_CC_HOLDER_NAME = 100;
	}
	
	public static final class Account {
		public static final int MIN_USERNAME = 3; 
		public static final int MIN_PASSWORD = 3; 	

		public static final int MAX_USERNAME = 30; 
		public static final int MAX_PASSWORD = 60; 	
	}
	
	public static final class MenuItem {
		public static final int MIN_DESC = 1; 
		public static final int MIN_NAME = 1;

		public static final int MAX_DESC = 300; 
		public static final int MAX_NAME = 100;
	}
	
	public static final class Address {
		public static final int MIN_LINE1 = 1; 
		public static final int MIN_LINE2 = 0;
		public static final int MIN_CITY = 1;
		public static final int MIN_ZIPCODE = 5;
		
		public static final int MAX_LINE1 = 100; 
		public static final int MAX_LINE2 = 100;
		public static final int MAX_CITY = 100;
		public static final int MAX_ZIPCODE = 10;
	}
		
	public static final class User {
		public static final int MIN_F_NAME = 1;
		public static final int MIN_L_NAME = 1; 
		public static final int MIN_PHONE = 10;
		public static final int MIN_EMAIL = ParamLengths.Email.MIN_ADDR; 
		
		public static final int MAX_F_NAME = 20;
		public static final int MAX_L_NAME = 20; 
		public static final int MAX_PHONE = 15;
		public static final int MAX_EMAIL = ParamLengths.Email.MAX_ADDR; 
	}
	
	public static final class Email {
		public static final int MIN_ADDR = 5; 
		public static final int MIN_SUBJECT = 1; 
		public static final int MIN_TEXT = 1; 
		
		public static final int MAX_ADDR = 30;
		public static final int MAX_SUBJECT = 5; 
		public static final int MAX_TEXT = 5; 
		
	}
	public static final class Credentials {
		public static final int MIN_USERNAME = ParamLengths.Account.MIN_USERNAME;
		public static final int MIN_PASSWORD = ParamLengths.Account.MIN_PASSWORD;	

		public static final int MAX_USERNAME = ParamLengths.Account.MAX_USERNAME;
		public static final int MAX_PASSWORD = ParamLengths.Account.MAX_PASSWORD;	
	}

}
