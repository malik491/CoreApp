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
import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.RequestBean;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.models.AccountModel;

/**
 * @author Malik
 *
 */
@Path("/account")
public class AccountService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response get(RequestBean<String> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				AccountModel model = new AccountModel(request.getCredentials());	
				AccountBean accountBean = model.read(request.getExtra());
				if (accountBean == null)
					response = Response.noContent().build();
				else
					response = Response.ok(accountBean, MediaType.APPLICATION_JSON).build();
			} catch (DBException e) {
				response = Response.serverError().build();
			}
		} else {
			response = Response.status(Status.BAD_REQUEST).build();
		}
		
		return response;
	}
		

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/post")
	public Response post(RequestBean<AccountBean> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				AccountModel model = new AccountModel(request.getCredentials());	
				AccountBean createdAccount  = model.create(request.getExtra());
				if (createdAccount == null)
					response = Response.noContent().build();
				else
					response = Response.ok(createdAccount, MediaType.APPLICATION_JSON).build();
			} catch (DBException e) {
				response = Response.serverError().build();
			}
		} else {
			response = Response.status(Status.BAD_REQUEST).build();
		}
		
		return response;
	}
	
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response update(RequestBean<AccountBean> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				AccountModel model = new AccountModel(request.getCredentials());
				Boolean updated  = model.update(request.getExtra());
				if (updated == null)
					response = Response.noContent().build();
				else
					response = Response.ok(updated, MediaType.APPLICATION_JSON).build();
			} catch (DBException e) {
				response = Response.serverError().build();
			}
		} else {
			response = Response.status(Status.BAD_REQUEST).build();
		}
		
		return response;
	}
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public Response delete(RequestBean<String> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				AccountModel model = new AccountModel(request.getCredentials());	
				Boolean deleted  = model.delete(request.getExtra());
				if (deleted == null)
					response = Response.noContent().build();
				else
					response = Response.ok(deleted, MediaType.APPLICATION_JSON).build();
			} catch (DBException e) {
				response = Response.serverError().build();
			}
		} else {
			response = Response.status(Status.BAD_REQUEST).build();
		}
		
		return response;
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get/all")
	public Response getAll(RequestBean<Object> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() == null);
		
		if (isValid) {	
			try {
				AccountModel model = new AccountModel(request.getCredentials());	
				List<AccountBean> accounts  = model.readAll();
				if (accounts == null)
					response = Response.noContent().build();
				else
					response = Response.ok(accounts, MediaType.APPLICATION_JSON).build();
			} catch (DBException e) {
				response = Response.serverError().build();
			}
		} else {
			response = Response.status(Status.BAD_REQUEST).build();
		}
		
		return response;
	}
		
}
