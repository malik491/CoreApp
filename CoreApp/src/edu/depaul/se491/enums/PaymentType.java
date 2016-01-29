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
	CASH, CREDIT_CARD;
}
