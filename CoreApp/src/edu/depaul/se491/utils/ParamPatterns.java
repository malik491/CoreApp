package edu.depaul.se491.utils;

/**
 * utility for parameter patterns
 * 
 * @author Malik
 */
public class ParamPatterns {

	public static final class MenuItem {
		public static final String NAME 	= String.format("[a-zA-Z0-9\\s]{%d,%d}", ParamLengths.MenuItem.MIN_NAME, ParamLengths.MenuItem.MAX_NAME);
	}
		
	public static final class Credentials {
		public static final String USERNAME = String.format("[a-zA-Z0-9\\s]{%d,%d}", ParamLengths.Credentials.MIN_USERNAME, ParamLengths.Credentials.MAX_USERNAME);
		public static final String PASSWORD = String.format("[a-zA-Z0-9\\s]{%d,%d}", ParamLengths.Credentials.MIN_PASSWORD, ParamLengths.Credentials.MAX_PASSWORD);
	}
	
	public static final class User {
		public static final String F_NAME 	 = String.format("[a-zA-Z\\s]{%d,%d}", ParamLengths.User.MIN_F_NAME, ParamLengths.User.MAX_F_NAME);
		public static final String L_NAME	 = String.format("[a-zA-Z\\s]{%d,%d}", ParamLengths.User.MIN_L_NAME, ParamLengths.User.MAX_L_NAME);
		public static final String PHONE	 = String.format("[0-9]{%d,%d}", 	ParamLengths.User.MIN_PHONE, ParamLengths.User.MAX_PHONE);
		public static final String EMAIL	 =  "[a-zA-Z0-9_.]{1,35}@[a-z0-9]{1,10}\\.[a-z]{2,3}";
	}
	
	public static final class Address {
		public static final String LINE_1 	= String.format("[a-zA-Z0-9\\s]{%d,%d}", ParamLengths.Address.MIN_LINE1, ParamLengths.Address.MAX_LINE1);
		public static final String CITY 	= String.format("[a-zA-Z0-9\\s]{%d,%d}", ParamLengths.Address.MIN_CITY, ParamLengths.Address.MAX_CITY);
		public static final String ZIPCODE 	= String.format("[0-9]{%d,%d}", ParamLengths.Address.MIN_ZIPCODE, ParamLengths.Address.MAX_ZIPCODE);	
	}
		
	public static final class CreditCard {
		public static final String NUMBER 	= String.format("[0-9]{%d,%d}", ParamLengths.CreditCard.MIN_NUMBER, ParamLengths.CreditCard.MAX_NUMBER);
		public static final String HOLDER_NAME 	= String.format("[a-zA-Z\\s]{%d,%d}", ParamLengths.CreditCard.MIN_HOLDER_NAME, ParamLengths.CreditCard.MAX_HOLDER_NAME);
		
	}
}
