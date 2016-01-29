/**
 * 
 */
package edu.depaul.se491.builders;

import edu.depaul.se491.beans.MenuItemBean;

/**
 * @author Malik
 *
 */
public class MenuItemBuilder {
	private MenuItemBean bean;
	
	public MenuItemBuilder() {
		this.bean = new MenuItemBean();
	}
	
	public MenuItemBuilder(final MenuItemBean bean) {
		this.bean = new MenuItemBean(bean.getId(), bean.getName(), bean.getDescription(), bean.getPrice(), bean.getItemCategory());
	}
		
	public MenuItemBean build() {
		return bean;
	}
	
	public void clearAll() {
		bean = new MenuItemBean();
	}

}
