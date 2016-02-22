/**
 * Order status type
 */
package edu.depaul.se491.enums;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author Malik
 *
 */
@XmlEnum(String.class)
public enum OrderStatus {
	SUBMITTED("Submitted"), PREPARED("Prepared"), CANCELED("Canceled");
	
	private String name;
	OrderStatus(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
