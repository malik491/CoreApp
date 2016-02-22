package edu.depaul.se491.enums;

import javax.xml.bind.annotation.XmlEnum;

/**
 * 
 * @author Malik
 *
 */
@XmlEnum(String.class)
public enum OrderItemStatus {
	READY("Ready"), NOT_READY("Not Ready");
	
	private String name;
	OrderItemStatus(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
