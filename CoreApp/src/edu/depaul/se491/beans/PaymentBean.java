package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.PaymentType;

/***
 * Payment Bean
 * 
 * @author Malik
 */
@XmlRootElement
public class PaymentBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private double total;
	private PaymentType type;
	private CreditCardBean creditCard;
	private String transactionConfirmation;
	
	/**
	 * construct PaymentBean
	 */
	public PaymentBean() {
	}
	
	/**
	 * construct PaymentBean
	 * @param id
	 * @param total
	 * @param type
	 * @param creditCard
	 * @param transactionConfirmation
	 */
	public PaymentBean(long id, double total, PaymentType type, CreditCardBean creditCard, String transactionConfirmation) {
		this.id = id;
		this.total = total;
		this.type = type;
		this.creditCard = creditCard;
		this.transactionConfirmation = transactionConfirmation;
	}
	
	/**
	 * return id
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * set id
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * return total
	 * @return
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * set total
	 * @param total
	 */
	public void setTotal(double total) {
		this.total = total;
	}
	
	/**
	 * return type
	 * @return
	 */
	public PaymentType getType() {
		return type;
	}
	
	/**
	 * set type
	 * @param type
	 */
	public void setType(PaymentType type) {
		this.type = type;
	}

	/**
	 * return credit card
	 * @return
	 */
	public CreditCardBean getCreditCard() {
		return creditCard;
	}

	/**
	 * set credit card
	 * @param creditCard
	 */
	public void setCreditCard(CreditCardBean creditCard) {
		this.creditCard = creditCard;
	}
	
	/**
	 * return credit card transaction confirmation
	 * @return
	 */
	public String getTransactionConfirmation() {
		return transactionConfirmation;
	}

	/**
	 * set credit card transaction confirmation
	 * @param transactionConfirmation
	 */
	public void setTransactionConfirmation(String transactionConfirmation) {
		this.transactionConfirmation = transactionConfirmation;
	}
}
