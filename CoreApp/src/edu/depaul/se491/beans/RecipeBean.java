/**
 * 
 */
package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Malik
 */
@XmlRootElement
public class RecipeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private MenuItemBean menuItem;
	private String description;
	
	public RecipeBean() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MenuItemBean getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItemBean menuItem) {
		this.menuItem = menuItem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
