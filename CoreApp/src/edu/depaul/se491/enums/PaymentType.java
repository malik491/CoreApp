/**
 * 
 */
package edu.depaul.se491.enums;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author Malik
 */
@XmlEnum(String.class)
public enum PaymentType {
	CASH("Cash"), CREDIT_CARD("Credit Card");
	
	private String name;
	PaymentType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
