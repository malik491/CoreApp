/**
 * Menu Web Service 
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

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.beans.RequestBean;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.models.MenuModel;

/**
 * @author Malik
 *
 */
@Path("/menuItem")
public class MenuService {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response get(RequestBean<Long> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				MenuModel model = new MenuModel(request.getCredentials());	
				MenuItemBean menuItem  = model.read(request.getExtra());
				if (menuItem == null)
					response = Response.noContent().build();
				else
					response = Response.ok(menuItem, MediaType.APPLICATION_JSON).build();
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
	public Response post(RequestBean<MenuItemBean> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				MenuModel model = new MenuModel(request.getCredentials());	
				MenuItemBean createdMenuItem  = model.create(request.getExtra());
				if (createdMenuItem == null)
					response = Response.noContent().build();
				else
					response = Response.ok(createdMenuItem, MediaType.APPLICATION_JSON).build();
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
	public Response update(RequestBean<MenuItemBean> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				MenuModel model = new MenuModel(request.getCredentials());	
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
	public Response delete(RequestBean<Long> request) {
		Response response = null;
		boolean isValid = (request != null && request.getCredentials() != null && request.getExtra() != null);
		
		if (isValid) {	
			try {
				MenuModel model = new MenuModel(request.getCredentials());	
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
				MenuModel model = new MenuModel(request.getCredentials());	
				List<MenuItemBean> menuIitems  = model.readAll();
				if (menuIitems == null)
					response = Response.noContent().build();
				else
					response = Response.ok(menuIitems, MediaType.APPLICATION_JSON).build();
			} catch (DBException e) {
				response = Response.serverError().build();
			}
		} else {
			response = Response.status(Status.BAD_REQUEST).build();
		}
		
		return response;
	}

}
