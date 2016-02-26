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
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.RequestBean;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;

/**
 * Order RESTful Service Client
 * 
 * @author Malik
 */
public class OrderServiceClient extends BaseWebServiceClient {
	private final String serviceBaseUrl;
	private final Client client;
	private final CredentialsBean credentials;
	
	/**
	 * construct OrderServiceClient
	 * @param credentials
	 * @param serviceBaseUrl
	 */
	public OrderServiceClient(CredentialsBean credentials, String serviceBaseUrl) {
		this.client = ClientBuilder.newClient();
		this.credentials = credentials;
		this.serviceBaseUrl = serviceBaseUrl;
	}
	
	/**
	 * return order or null
	 * @param orderId
	 * @return
	 */
	public OrderBean get(final long orderId) {
		final String getTarget = serviceBaseUrl + "/get/id";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<Long> request = super.<Long>getRequestBean(credentials, new Long(orderId));

		OrderBean responseBean = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				responseBean = response.readEntity(OrderBean.class);
			}
			response.close();
				
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
		
		return responseBean;
	}

	/**
	 * return order or null
	 * @param orderConfirmation
	 * @return
	 */
	public OrderBean get(final String orderConfirmation) {
		final String getTarget = serviceBaseUrl + "/get/confirmation";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<String> request = super.<String>getRequestBean(credentials, orderConfirmation);
		
		OrderBean responseBean = null;
		try {		
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				responseBean = response.readEntity(OrderBean.class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
		
		return responseBean;
	}
	
	/**
	 * add new order
	 * @param newOrder
	 * @return newly added order or null
	 */
	public OrderBean post(final OrderBean newOrder) {
		final String getTarget = serviceBaseUrl + "/post";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<OrderBean> request = super.<OrderBean>getRequestBean(credentials, newOrder);
		
		OrderBean responseBean = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				responseBean = response.readEntity(OrderBean.class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}

		return responseBean;
	}
	
	/**
	 * update order
	 * @param updatedOrder
	 * @return Boolean or null
	 */
	public Boolean update(final OrderBean updatedOrder) {
		final String getTarget = serviceBaseUrl + "/update";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<OrderBean> request = super.<OrderBean>getRequestBean(credentials, updatedOrder);

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
	 * update order item status
	 * @param updatedOrder
	 * @return Boolean or null
	 */
	public Boolean mainStationUpdate(final OrderBean updatedOrder) {
		final String getTarget = serviceBaseUrl + "/update/station/main";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<OrderBean> request = super.<OrderBean>getRequestBean(credentials, updatedOrder);
		
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
	 * update order item status
	 * @param updatedOrder
	 * @return Boolean or null
	 */
	public Boolean sideStationUpdate(final OrderBean updatedOrder) {
		final String getTarget = serviceBaseUrl + "/update/station/side";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<OrderBean> request = super.<OrderBean>getRequestBean(credentials, updatedOrder);
		
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
	 * update order item status
	 * @param updatedOrder
	 * @return Boolean or null
	 */
	public Boolean beverageStationUpdate(final OrderBean updatedOrder) {
		final String getTarget = serviceBaseUrl + "/update/station/beverage";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<OrderBean> request = super.<OrderBean>getRequestBean(credentials, updatedOrder);
		
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
	 * delete order
	 * @param orderId
	 * @return Boolean or null
	 */
	public Boolean delete(final long orderId) {
		final String getTarget = serviceBaseUrl + "/delete";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<Long> request = super.<Long>getRequestBean(credentials, new Long(orderId));
		
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
	 * return array of OrderBean or null
	 * @return
	 */
	public OrderBean[] getAll() {
		final String getTarget = serviceBaseUrl + "/get/all";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<Object> request = super.<Object>getRequestBean(credentials, null);
		
		OrderBean[] orders = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				orders = response.readEntity(OrderBean[].class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
		
		return orders;
	}
	
	/**
	 * return array of OrderBean or null
	 * @param status
	 * @return
	 */
	public OrderBean[] getAllWithStatus(final OrderStatus status) {
		final String getTarget = serviceBaseUrl + "/get/status";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<OrderStatus> request = super.<OrderStatus>getRequestBean(credentials, status);
		
		OrderBean[] orders = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				orders = response.readEntity(OrderBean[].class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}
		
		return orders;
	}
	
	/**
	 * return array of OrderBean or null
	 * @param type
	 * @return
	 */
	public OrderBean[] getAllWithType(final OrderType type) {
		final String getTarget = serviceBaseUrl + "/get/type";
		Invocation.Builder invocationBuilder = getJsonInvocationBuilder(client, getTarget);
		
		RequestBean<OrderType> request = super.<OrderType>getRequestBean(credentials, type);
		
		OrderBean[] orders = null;
		try {
			Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			if (response.getStatus() != Status.OK.getStatusCode()) {
				setResponseMessage(response.readEntity(String.class));
			} else {
				orders = response.readEntity(OrderBean[].class);
			}
			response.close();
			
		} catch (ProcessingException | IllegalStateException e) {
			setResponseMessage(WEB_SERVICE_ERROR_MESSAGE);
		}

		return orders;
	}
	
}
