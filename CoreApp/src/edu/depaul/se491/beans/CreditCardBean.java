/**
 * 
 */
package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Malik
 */
@XmlRootElement
public class CreditCardBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ccNumber;
	private String ccHolderName;
	private int expMonth;
	private int expYear;
	
	public CreditCardBean() {
	}

	public CreditCardBean(String ccNumber, String ccHolderName, int expMonth, int expYear) {
		this.ccNumber = ccNumber;
		this.ccHolderName = ccHolderName;
		this.expMonth = expMonth;
		this.expYear = expYear;
	}

	
	public String getCcNumber() {
		return ccNumber;
	}


	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}


	public int getExpMonth() {
		return expMonth;
	}


	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}


	public int getExpYear() {
		return expYear;
	}


	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}


	public String getCcHolderName() {
		return ccHolderName;
	}


	public void setCcHolderName(String ccHolderName) {
		this.ccHolderName = ccHolderName;
	}	

}
