package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.AddressState;

/**
 * Address Bean
 * 
 * @author Malik
 */
@XmlRootElement
public class AddressBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String line1, line2;
	private String city, zipcode;
	private AddressState state;
	
	/**
	 * construct AddressBean
	 */
	public AddressBean() {
	}
	
	/**
	 * construct AddressBean
	 * @param id
	 * @param line1
	 * @param line2
	 * @param city
	 * @param state
	 * @param zipcode
	 */
	public AddressBean(long id, String line1, String line2, String city, AddressState state, String zipcode) {
		this.id = id;
		this.line1 = line1;
		this.line2 = line2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
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
	 * return line 1
	 * @return
	 */
	public String getLine1() {
		return line1;
	}
	
	/**
	 * set line 1
	 * @param line1
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	
	/**
	 * return line 2
	 * @return
	 */
	public String getLine2() {
		return line2;
	}
	
	/**
	 * set line 2
	 * @param line2
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	
	/**
	 * return city
	 * @return
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * set city
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * return state
	 * @return
	 */
	public AddressState getState() {
		return state;
	}
	
	/**
	 * set state
	 * @param state
	 */
	public void setState(AddressState state) {
		this.state = state;
	}
	
	/**
	 * return zipcode
	 * @return
	 */
	public String getZipcode() {
		return zipcode;
	}
	
	/**
	 * set zipcode
	 * @param zipcode
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
