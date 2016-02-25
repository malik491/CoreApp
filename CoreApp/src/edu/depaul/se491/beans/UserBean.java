package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User Bean
 * 
 * @author Malik
 */
@XmlRootElement
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String firstName, lastName;
	private String email, phone;
	private AddressBean address;
	
	/**
	 * construct UserBean
	 */
	public UserBean() {
	}
	
	/**
	 * construct UserBean
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param address
	 */
	public UserBean(long id, String firstName, String lastName, String email, String phone, AddressBean address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
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
	 * return first name
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * set first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * return last name
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * set last name
	 * @return
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * return email
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * set email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * return phone number
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * set phone number
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * return address
	 * @return
	 */
	public AddressBean getAddress() {
		return address;
	}

	/**
	 * set address
	 * @param address
	 */
	public void setAddress(AddressBean address) {
		this.address = address;
	}
}
