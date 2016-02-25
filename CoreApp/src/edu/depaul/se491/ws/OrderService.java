/**
 * Order Web Service 
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

import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.RequestBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
import edu.depaul.se491.enums.MenuItemCategory;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.models.OrderModel;
import edu.depaul.se491.validators.CredentialsValidator;

/**
 * @author Malik
 *
 */
@Path("/order")
public class OrderService {
	private static DAOFactory daoFactory;
	
	public OrderService() {
		daoFactory = ProductionDAOFactory.getInstance();
	}

	public OrderService(DAOFactory factory) {
		daoFactory = factory;
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get/id")
	public Response getById(RequestBean<Long> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			OrderBean order = model.read(request.getExtra());
			if (order == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, order);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get/confirmation")
	public Response getByConfirmation(RequestBean<String> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			OrderBean order = model.read(request.getExtra());
			if (order == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, order);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/put")
	public Response put(RequestBean<OrderBean> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			OrderBean createdOrder = model.create(request.getExtra());
			if (createdOrder == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, createdOrder);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response update(RequestBean<OrderBean> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			Boolean updated = model.update(request.getExtra());
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
	@Path("/update/station/main")
	public Response mainStationUpdate(RequestBean<OrderBean> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			Boolean updated = model.update(request.getExtra(), MenuItemCategory.MAIN);
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
	@Path("/update/station/side")
	public Response sideStationupdate(RequestBean<OrderBean> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			Boolean updated = model.update(request.getExtra(), MenuItemCategory.SIDE);
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
	@Path("/update/station/beverage")
	public Response beverageStationupdate(RequestBean<OrderBean> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			Boolean updated = model.update(request.getExtra(), MenuItemCategory.BEVERAGE);
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
	public Response delete(RequestBean<Long> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			Boolean deleted = model.delete(request.getExtra());
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
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			List<OrderBean> orders = model.readAll();
			if (orders == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, orders);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get/type")
	public Response getAllByType(RequestBean<OrderType> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			List<OrderBean> orders = model.readAll(request.getExtra());
			if (orders == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, orders);
		} else {
			response = getResponse(Status.BAD_REQUEST, INVALID_RQST_MSG);
		}
		
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get/status")
	public Response getAllByStatus(RequestBean<OrderStatus> request) {
		Response response = null;
		boolean isValid = isValidRequest(request, false);
		
		if (isValid) {	
			OrderModel model = new OrderModel(daoFactory, request.getCredentials());
			List<OrderBean> orders = model.readAll(request.getExtra());
			if (orders == null)
				response = getResponse(model.getResponseStatus(), model.getResponseMessage());
			else
				response = getResponse(Status.OK, orders);
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
