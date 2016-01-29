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
	ADMIN, MANAGER, EMPLOYEE, VENDOR, CUSTOMER_APP
}
