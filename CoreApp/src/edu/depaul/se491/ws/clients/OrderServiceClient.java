/**
 * 
 */
package edu.depaul.se491.ws.clients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;

/**
 * @author Malik
 *
 */
public class OrderServiceClient extends BaseWebServiceClient {
	private final String serviceBaseUrl;
	private final Client client;
	private final CredentialsBean credentials;
	
	public OrderServiceClient(CredentialsBean credentials, String serviceBaseUrl) {
		this.client = ClientBuilder.newClient();
		this.credentials = credentials;
		this.serviceBaseUrl = serviceBaseUrl;
	}
	
	
	public OrderBean get(final long orderId) {
		setResponseMessage("no implementation");
		return null;
	}

	public OrderBean get(final String orderConfirmation) {
		setResponseMessage("no implementation");
		return null;
	}
	
	public OrderBean post(final OrderBean newOrder) {
		setResponseMessage("no implementation");
		return null;
	}
	
	public Boolean update(final OrderBean updatedOrder) {
		setResponseMessage("no implementation");
		return null;
	}
	
	public Boolean delete(final long orderId) {
		setResponseMessage("no implementation");
		return null;
	}
	
	public OrderBean[] getAll() {
		setResponseMessage("no implementation");
		return null;
	}
	
	public OrderBean[] getAllWithStatus(final OrderStatus status) {
		setResponseMessage("no implementation");
		return null;
	}
	
	public OrderBean[] getAllWithType(final OrderType type) {
		setResponseMessage("no implementation");
		return null;
	}
	
}
