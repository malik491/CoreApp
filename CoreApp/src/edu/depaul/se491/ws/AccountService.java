/**
 * Account Web Service 
 */
package edu.depaul.se491.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.RequestBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
import edu.depaul.se491.models.AccountModel;
import edu.depaul.se491.validators.CredentialsValidator;

/**
 * @author Malik
 *
 */
@Path("/account")
public class AccountService {

	private static DAOFactory daoFactory;
	
	public AccountService() {
		daoFactory = ProductionDAOFactory.getInstance();
	}

	public AccountService(DAOFactory factory) {
		daoFactory = factory;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response get(RequestBean<String> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			AccountModel model = new AccountModel(daoFactory, request.getCredentials());	
			AccountBean accountBean = model.read(request.getExtra());
			if (accountBean == null) {
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			} else {
				response = getResponse(Status.OK, accountBean);
			}
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		return response;
	}
		

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/post")
	public Response post(RequestBean<AccountBean> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			AccountModel model = new AccountModel(daoFactory, request.getCredentials());	
			AccountBean createdAccount  = model.create(request.getExtra());
			if (createdAccount == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, createdAccount);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response update(RequestBean<AccountBean> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			AccountModel model = new AccountModel(daoFactory, request.getCredentials());
			Boolean updated  = model.update(request.getExtra());
			if (updated == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, updated);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public Response delete(RequestBean<String> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			AccountModel model = new AccountModel(daoFactory, request.getCredentials());	
			Boolean deleted  = model.delete(request.getExtra());
			if (deleted == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, deleted);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get/all")
	public Response getAll(RequestBean<Object> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, true);
		
		if (isValid) {	
			AccountModel model = new AccountModel(daoFactory, request.getCredentials());	
			List<AccountBean> accounts  = model.readAll();
			if (accounts == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, accounts);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	private <T> boolean isValidRequest(RequestBean<T> request, boolean extraCanBeNull) {
		boolean isValid = false;
		
		isValid  = request != null;
		isValid &= isValid? new CredentialsValidator().validate(request.getCredentials()) : false;
		isValid &= isValid && !extraCanBeNull? request.getExtra() != null : true;
		
		return isValid;
	}
	
	
	private <T> Response getResponse(Response.Status status, T entity) {
		ResponseBuilder responseBuilder = Response.status(status);
		responseBuilder = responseBuilder.entity(entity);
		return responseBuilder.build();
	}
	
	
	private static final String INVALID_RQST_MSG = "Invalid Web Service Request";
}
