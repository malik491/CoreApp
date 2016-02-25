package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Credit Card Bean
 * 
 * @author Malik
 */
@XmlRootElement
public class CreditCardBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ccNumber;
	private String ccHolderName;
	private int expMonth;
	private int expYear;
	
	/**
	 * construct CreditCardBean
	 */
	public CreditCardBean() {
	}

	/**
	 * construct CreditCardBean
	 * @param ccNumber
	 * @param ccHolderName
	 * @param expMonth
	 * @param expYear
	 */
	public CreditCardBean(String ccNumber, String ccHolderName, int expMonth, int expYear) {
		this.ccNumber = ccNumber;
		this.ccHolderName = ccHolderName;
		this.expMonth = expMonth;
		this.expYear = expYear;
	}

	/**
	 * return credit card number
	 * @return
	 */
	public String getCcNumber() {
		return ccNumber;
	}

	/**
	 * set credit card number
	 * @param ccNumber
	 */
	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	/**
	 * return credit card holder name
	 * @return
	 */
	public String getCcHolderName() {
		return ccHolderName;
	}

	/**
	 * set credit card holder name
	 * @param ccHolderName
	 */
	public void setCcHolderName(String ccHolderName) {
		this.ccHolderName = ccHolderName;
	}
	
	/**
	 * return credit card expiration month
	 * @return
	 */
	public int getExpMonth() {
		return expMonth;
	}

	/**
	 * set credit card expiration month
	 * @param expMonth
	 */
	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}

	/**
	 * return credit card expiration year
	 * @return
	 */
	public int getExpYear() {
		return expYear;
	}

	/**
	 * set credit card expiration month
	 * @param expYear
	 */
	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}	

}
