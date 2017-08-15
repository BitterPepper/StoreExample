package com.developer.rest;
 
import java.io.IOException;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.developer.data.AnswerStatuses;
import com.developer.data.Commodity;
import com.developer.data.Commoditybalance;
import com.developer.utils.Dao;
import com.developer.utils.DaoRedis;
import com.developer.utils.JsonUtils;
 
@Path("/getprice")
public class WarehouseService {
 
	@GET
	@Produces({"text/plain", "application/json"})
	public Response getMsg(@QueryParam("mpn")  String mpn,
			@QueryParam("availability") @DefaultValue("0") int availability,
			@QueryParam("pricesort") @DefaultValue("0") int pricesort) throws IOException{
		
		Commoditybalance result = new Commoditybalance();
		result.setMpn(mpn);
		
		StringBuilder stackError = new StringBuilder();
		if (mpn==null || mpn.isEmpty()){
			stackError.append("Not right MPN");
		}
		if (availability < 0 || availability > 2){
			stackError.append("Not right availability");
		}
		if (pricesort <  0 || pricesort > 2){
			stackError.append("Not right pricesort");
		}

		if (stackError.length() > 0){
			result.setErrorDescr(stackError.toString());
			result.setStatus(AnswerStatuses.ERROR);
			String output = JsonUtils.toJson(result);
	 		return Response.status(400).entity(output).build();
		}
		
		Dao dao = new DaoRedis();
		List<Commodity> commodities = dao.getCommodities(mpn, availability, pricesort);
		result.setCommodities(commodities);
		result.setId(commodities.get(0).getId());

		result.setStatus(AnswerStatuses.OK);

		String output = JsonUtils.toJson(result);
 		return Response.status(200).entity(output).build();
 	}
 
}