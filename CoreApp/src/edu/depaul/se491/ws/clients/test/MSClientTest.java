package edu.depaul.se491.ws.clients.test;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.ws.clients.MenuServiceClient;

public class MSClientTest {
	private static final String serviceBaseUrl = "http://localhost/CoreApp/menuItem";
	private MenuServiceClient menuServiceClient;
	
	
	public static void main(String[] args) {
		CredentialsBean credentials = new CredentialsBean();
		credentials.setUsername("manager");
		credentials.setPassword("password");
		
		MenuServiceClient client = new MenuServiceClient(credentials, serviceBaseUrl);
		MSClientTest clientTest = new MSClientTest(client);
		clientTest.run();
	}
	
	
	public MSClientTest(MenuServiceClient menuServiceClient) {
		this.menuServiceClient = menuServiceClient;
	}

	
	public void run() {
		testGet();
		testPost();
		testUpdate();
		testDelete();
		testGetAll();
	}
	
	private void testGet() {
		MenuItemBean bean = menuServiceClient.get(1);
		
		System.out.println("menuServiceClient.get():");
		report(bean);
		System.out.println();
	}

	private void testGetAll() {
		MenuItemBean[] beans = menuServiceClient.getAll();
		
		System.out.println("menuServiceClient.getAll():" + (beans == null? "\n -- null menu item list --" : "(size = " + beans.length + ")"));
		
		if (beans != null) {
			for (MenuItemBean bean: beans)
				report(bean);			
		}
		System.out.println();		
	}

	private void testPost() {
		MenuItemBean newItem = getMenuItem(0, "new item name", 1.99);
		
		MenuItemBean createdItem = menuServiceClient.post(newItem);
		
		System.out.println("menuServiceClient.testPost():");
		report(createdItem);
		System.out.println();
	}

	private void testUpdate() {
		MenuItemBean oldItem = getMenuItem(1, "new name", 5.99);
		oldItem.setDescription("item name & price has been updated");
		
		Boolean isUpdated = menuServiceClient.update(oldItem);
		
		System.out.println("menuServiceClient.testUpdate():");
		System.out.println(" -- update: " +  (isUpdated != null? isUpdated : menuServiceClient.getResponseMessage()));
		System.out.println();
	}

	private void testDelete() {
		Boolean isDeleted = menuServiceClient.delete(1);
		
		System.out.println("menuServiceClient.testDelete():");
		System.out.println(" -- deleted: " + (isDeleted != null? isDeleted : menuServiceClient.getResponseMessage()));
		System.out.println();

	}
	
	private void report(MenuItemBean bean) {
		if (bean == null) {
			System.out.println("-- null menu item bean -- [ " + menuServiceClient.getResponseMessage() + "]");
		} else {
			System.out.println("-- Menu Item Bean --");
			System.out.println("bean.id = " + bean.getId());
			System.out.println("bean.name = " + bean.getName());
			System.out.println("bean.description = " + bean.getDescription());
			System.out.println("bean.price = " + bean.getPrice());
			System.out.println("bean.itemCategory = " + bean.getItemCategory());
		}
		
	}
	
	private MenuItemBean getMenuItem(long id, String name, double price) {
		MenuItemBean bean = new MenuItemBean();
		bean.setId(id);
		bean.setName(name);
		bean.setDescription("this is a long decsription here blah");
		bean.setPrice(price);
		bean.setItemCategory(MenuItemCategory.MAIN);
		return bean;
	}


}
