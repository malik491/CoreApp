package edu.depaul.se491.ws.clients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.RequestBean;

/**
 * Base RESTful Service Client
 * 
 * @author Malik
 */
class BaseWebServiceClient {
	private String responseMessage;
	
	BaseWebServiceClient() {
	}

	/**
	 * return response message
	 * @return
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * create request bean
	 * @param credentials
	 * @param extra
	 * @return
	 */
	protected <T> RequestBean<T> getRequestBean(CredentialsBean credentials, T extra) {
		RequestBean<T> requestBean = new RequestBean<>();
		requestBean.setCredentials(credentials);
		requestBean.setExtra(extra);
		return requestBean;
	}
	
	/**
	 * return Invocation.Builder for json
	 * @param client
	 * @param target
	 * @return
	 */
	protected Invocation.Builder getJsonInvocationBuilder(Client client, String target) {
		return client.target(target).request(MediaType.APPLICATION_JSON); 
	}

	/**
	 * set response message
	 * @param responseMessage
	 */
	protected void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	protected final String WEB_SERVICE_ERROR_MESSAGE = "Web Service Error (web service is down, wrong URL, or I/O exception)";
	
}
