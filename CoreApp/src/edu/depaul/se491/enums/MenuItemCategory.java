package edu.depaul.se491.enums;

import javax.xml.bind.annotation.XmlEnum;

/**
 * 
 * @author Malik
 *
 */
@XmlEnum(String.class)
public enum MenuItemCategory {
	BEVERAGE("Beverage"), MAIN("Main"), SIDE("Side");
	
	private String name;
	MenuItemCategory(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
