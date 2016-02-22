/**
 * Account role type
 */
package edu.depaul.se491.enums;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author Malik
 *
 */
@XmlEnum(String.class)
public enum AccountRole {
	ADMIN("Admin"), MANAGER("Manager"), EMPLOYEE("Employee"), CUSTOMER_APP("Customer App");
	
	private String name;
	AccountRole(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
