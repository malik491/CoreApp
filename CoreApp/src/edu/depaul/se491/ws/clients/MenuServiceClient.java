package edu.depaul.se491.ws.clients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.RequestBean;

public class MenuServiceClient extends BaseWebServiceClient {
	private final String serviceBaseUrl;
	private final Client client;
	private final CredentialsBean credentials;
	
	public MenuServiceClient(CredentialsBean credentials, String serviceBaseUrl) {
		this.client = ClientBuilder.newClient();
		this.credentials = credentials;
		this.serviceBaseUrl = serviceBaseUrl;
	}
	
	public MenuItemBean get(long menuItemId) {
		final String getTarget = serviceBaseUrl + "/get";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<Long> request = super.<Long>getRequestBean(credentials, new Long(menuItemId));
		
		Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
		
		MenuItemBean responseBean = response.readEntity(MenuItemBean.class);
		response.close();
		
		return responseBean;
	}
	
	public MenuItemBean post(final MenuItemBean newMenuItem) {
		final String postTarget = serviceBaseUrl + "/post";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, postTarget);
		
		RequestBean<MenuItemBean> request = super.<MenuItemBean>getRequestBean(credentials, newMenuItem);
		
		Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
		
		MenuItemBean responseBean = response.readEntity(MenuItemBean.class);
		
		response.close();
		
		return responseBean;
	}
	
	public Boolean update(final MenuItemBean updatedMenuItem) {
		final String updateTarget = serviceBaseUrl + "/update";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, updateTarget);
		
		RequestBean<MenuItemBean> request = super.<MenuItemBean>getRequestBean(credentials, updatedMenuItem);
		
		Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
		
		Boolean updated = response.readEntity(Boolean.class);
		
		response.close();
		
		return updated;
	}
	
	public Boolean delete(long menuItemId) {
		final String deleteTarget = serviceBaseUrl + "/delete";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, deleteTarget);
		
		RequestBean<Long> request = super.<Long>getRequestBean(credentials, new Long(menuItemId));
		
		Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
		
		Boolean deleted = response.readEntity(Boolean.class);
		
		response.close();
		
		return deleted;
	}
	
	
	public MenuItemBean[] getAll() {
		final String getAllTarget = serviceBaseUrl + "/get/all";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getAllTarget);
		
		RequestBean<Object> request = super.<Object>getRequestBean(credentials, null);
		
		Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
		
		MenuItemBean[] responseBeans = response.readEntity(MenuItemBean[].class);
		
		response.close();
		
		return responseBeans;
	}
}
