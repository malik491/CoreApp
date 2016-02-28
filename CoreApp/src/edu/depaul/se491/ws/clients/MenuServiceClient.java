package edu.depaul.se491.ws.clients;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.RequestBean;

/**
 * Menu RESTful Service Client
 * 
 * @author Malik
 */
public class MenuServiceClient extends BaseWebServiceClient {
	private final String serviceBaseUrl;
	private final Client client;
	private final CredentialsBean credentials;
	
	/**
	 * construct MenuServiceClient
	 * @param credentials
	 * @param serviceBaseUrl
	 */
	public MenuServiceClient(CredentialsBean credentials, String serviceBaseUrl) {
		this.client = ClientBuilder.newClient();
		this.credentials = credentials;
		this.serviceBaseUrl = serviceBaseUrl;
	}
	
	/**
	 * return MenuItemBean or null
	 * @param menuItemId
	 * @return
	 */
	public MenuItemBean get(long menuItemId) {
		final String getTarget = serviceBaseUrl + "/get";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<Long> request = super.<Long>getRequestBean(credentials, new Long(menuItemId));
		
		MenuItemBean responseBean = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				responseBean = response.readEntity(MenuItemBean.class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
		
		return responseBean;
	}
	
	/**
	 * add new MenuItemBean
	 * @param newMenuItem
	 * @return newly created menuItemBean or null
	 */
	public MenuItemBean post(final MenuItemBean newMenuItem) {
		final String postTarget = serviceBaseUrl + "/post";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, postTarget);
		
		RequestBean<MenuItemBean> request = super.<MenuItemBean>getRequestBean(credentials, newMenuItem);
		
		MenuItemBean responseBean = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				responseBean = response.readEntity(MenuItemBean.class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
		return responseBean;
	}
	
	/**
	 * update menu item
	 * @param updatedMenuItem
	 * @return Boolean or null
	 */
	public Boolean update(final MenuItemBean updatedMenuItem) {
		final String updateTarget = serviceBaseUrl + "/update";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, updateTarget);
		
		RequestBean<MenuItemBean> request = super.<MenuItemBean>getRequestBean(credentials, updatedMenuItem);
		
		Boolean updated = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				updated = response.readEntity(Boolean.class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
				
		return updated;
	}
	
	/**
	 * delete menu item
	 * @param menuItemId
	 * @return Boolean or null
	 */
	public Boolean delete(long menuItemId) {
		final String deleteTarget = serviceBaseUrl + "/delete";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, deleteTarget);
		
		RequestBean<Long> request = super.<Long>getRequestBean(credentials, new Long(menuItemId));

		Boolean deleted = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				deleted = response.readEntity(Boolean.class);
			}
			response.close();

		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
				
		return deleted;
	}
	
	/**
	 * hide a menu item
	 * @param menuItemId
	 * @return Boolean or null
	 */
	public Boolean hideMenuItem(long menuItemId) {
		return updateIsHidden(menuItemId, true);
	}
	
	/**
	 * hide a menu item
	 * @param menuItemId
	 * @return Boolean or null
	 */
	public Boolean unhideMenuItem(long menuItemId) {
		return updateIsHidden(menuItemId, false);
	}
	
	/**
	 * return all visible menu items or null
	 * @return
	 */
	public MenuItemBean[] getAllVisible() {
		return getAll(false);
	}
	
	/**
	 * return all hidden menu items or null
	 * @return
	 */
	public MenuItemBean[] getAllHidden() {
		return getAll(true);
	}
	
	/**
	 * return all menu items or null
	 * @param hidden get all hidden menu items
	 * @return
	 */
	private MenuItemBean[] getAll(boolean hidden) {
		final String getAllTarget = serviceBaseUrl + "/get/all" + (hidden? "/hidden" : "");
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getAllTarget);
		
		RequestBean<Object> request = super.<Object>getRequestBean(credentials, null);
		
		MenuItemBean[] responseBeans = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				responseBeans = response.readEntity(MenuItemBean[].class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
		
		return responseBeans;
	}
	
	/**
	 * hide / unhide a menu item
	 * @param menuItemId
	 * @param hide hide a menu item?
	 * @return Boolean or null
	 */
	public Boolean updateIsHidden(long menuItemId, boolean hide) {
		final String deleteTarget = serviceBaseUrl + (hide? "/hide" : "/unhide");
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, deleteTarget);
		
		RequestBean<Long> request = super.<Long>getRequestBean(credentials, new Long(menuItemId));

		Boolean deleted = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				deleted = response.readEntity(Boolean.class);
			}
			response.close();

		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
				
		return deleted;
	}
}
