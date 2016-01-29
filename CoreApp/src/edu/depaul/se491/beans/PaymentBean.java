package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.PaymentType;

@XmlRootElement
public class PaymentBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private double total;
	private PaymentType type;
	private CreditCardBean creditCard;
	private String transactionConfirmation;
	
	public PaymentBean() {
	}
	
	public PaymentBean(long id, double total, PaymentType type, CreditCardBean creditCard, String transactionConfirmation) {
		this.id = id;
		this.total = total;
		this.type = type;
		this.creditCard = creditCard;
		this.transactionConfirmation = transactionConfirmation;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public PaymentType getType() {
		return type;
	}
	
	public void setType(PaymentType type) {
		this.type = type;
	}

	public CreditCardBean getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCardBean creditCard) {
		this.creditCard = creditCard;
	}
	
	public String getTransactionConfirmation() {
		return transactionConfirmation;
	}


	public void setTransactionConfirmation(String transactionConfirmation) {
		this.transactionConfirmation = transactionConfirmation;
	}
}
