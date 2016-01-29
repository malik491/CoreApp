/**
 * MeanItem bean
 */
package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.MenuItemCategory;

/**
 * @author Malik
 *
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
	 * construct an empty menu item with no id
	 * description filed defaults to empty string
	 */
	public MenuItemBean() {
	}
	
	
	public MenuItemBean(long id, String name, String description, double price, MenuItemCategory itemCategory) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.itemCategory = itemCategory;
	}
	
	/**
	 * return item id
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * set item id
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * return item name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * set item name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * return item description
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * set item description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * return item price
	 * @return
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * set item price
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	public MenuItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(MenuItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
}
