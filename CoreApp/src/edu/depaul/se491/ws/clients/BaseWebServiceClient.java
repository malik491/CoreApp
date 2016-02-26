package edu.depaul.se491.ws.clients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.RequestBean;

class BaseWebServiceClient {
	private String responseMessage;
	
	BaseWebServiceClient() {
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	
	protected <T> RequestBean<T> getRequestBean(CredentialsBean credentials, T extra) {
		RequestBean<T> requestBean = new RequestBean<>();
		requestBean.setCredentials(credentials);
		requestBean.setExtra(extra);
		return requestBean;
	}
	
	
	protected Invocation.Builder getJsonInvocationBuilder(Client client, String target) {
		return client.target(target).request(MediaType.APPLICATION_JSON); 
	}


	protected void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	protected final String WEB_SERVICE_ERROR_MESSAGE = "Web Service Error (web service is down, wrong URL, or I/O exception)";
	
}
