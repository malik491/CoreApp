package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.MenuItemCategory;

/**
 * MeanItem Bean
 * 
 * @author Malik
 */
@XmlRootElement
public class MenuItemBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private String description;
	private double price;
	private MenuItemCategory itemCategory;
	
	/**
	 * construct MneuItemBean
	 */
	public MenuItemBean() {
	}
	
	/**
	 * construct MneuItemBean
	 * @param id
	 * @param name
	 * @param description
	 * @param price
	 * @param itemCategory
	 */
	public MenuItemBean(long id, String name, String description, double price, MenuItemCategory itemCategory) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.itemCategory = itemCategory;
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
	 * return name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * set name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * return description
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * set description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * return price
	 * @return
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * set price
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * return item category
	 * @return
	 */
	public MenuItemCategory getItemCategory() {
		return itemCategory;
	}

	/**
	 * set item category
	 * @param itemCategory
	 */
	public void setItemCategory(MenuItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
}
