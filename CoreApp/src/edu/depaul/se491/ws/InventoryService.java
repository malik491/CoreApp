/**
 * Inventory Web Service 
 */
package edu.depaul.se491.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.depaul.se491.beans.RequestBean;

/**
 * @author Malik
 *
 */
@Path("/ajax")
public class InventoryService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response get(RequestBean<String> request) {
		return null;
	}
}
