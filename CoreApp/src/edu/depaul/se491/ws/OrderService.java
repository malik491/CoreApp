/**
 * Order Web Service 
 */
package edu.depaul.se491.ws;


import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.depaul.se491.utils.ParamLabels;

/**
 * @author Malik
 *
 */
@Path("/order")
public class OrderService {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/x-www-form-urlencoded")
	@Path("/get")
	public String get(@FormParam(ParamLabels.Request.REQUEST)String jsonFormatedParamList) {
		return null;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/x-www-form-urlencoded")
	@Path("/put")
	public String put(@FormParam(ParamLabels.Request.REQUEST)String jsonFormatedParamList) {
		return null;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/x-www-form-urlencoded")
	@Path("/update")
	public String update(@FormParam(ParamLabels.Request.REQUEST)String jsonFormatedParamList) {
		return null;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/x-www-form-urlencoded")
	@Path("/delete")
	public String delete(@FormParam(ParamLabels.Request.REQUEST)String jsonFormatedParamList) {
		return null;
	}
}
