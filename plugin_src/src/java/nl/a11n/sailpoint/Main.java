package nl.a11n.sailpoint;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.integration.ListResult;
import sailpoint.integration.RequestResult;
import sailpoint.object.Attributes;
import sailpoint.object.Identity;
import sailpoint.object.QueryOptions;
import sailpoint.rest.BaseResource;
import sailpoint.rest.plugin.AllowAll;
import sailpoint.rest.plugin.BasePluginResource;
import sailpoint.tools.GeneralException;
import sailpoint.tools.JsonHelper;
import sailpoint.tools.Util;

import org.apache.log4j.Logger;

/**
 * @author Arie Timmerman
 */

// http://localhost:8080/identityiq/plugin/rest/helloworld
@Path("helloworld")
@Consumes({ "application/json", "*/*" })
public class Main extends BasePluginResource {
	
	@XmlRootElement
	static class Search {
		public String hql;
		
		public Search(String hql) {
			this.hql = hql;
		}
	}

	private static final Logger logger = Logger.getLogger(Main.class);

	@Override
	public String getPluginName() {
		return "Insight";
	}

	// http://localhost:8080/identityiq/plugin/rest/helloworld/hello
	@GET
	@Path("hello")
	@AllowAll
	public Object getInfo() throws GeneralException {
		return "hello 1234567";
	}

	@GET
	@Path("info/{name}")
	@Produces({ "application/json" })
	@AllowAll
	public Object getInfo(@PathParam("name") String identityName) throws GeneralException {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> getInfo");
			logger.debug("identityName = " + identityName);
		}
		Map<String, Object> identityModel = new HashMap<String, Object>();

		SailPointContext context = SailPointFactory.getCurrentContext();

		Iterator results = context.search("from Identity", null, null);

		Identity identity = context.getObject(Identity.class, identityName);
		if (identity != null) {
			identityModel.put("name", identity.getName());
			identityModel.put("id", identity.getId());
			identityModel.put("email", identity.getEmail());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("returns " + identityModel);
			logger.debug("<<< getInfo");
		}
		return identityModel;
	}

	@POST
	@Path("search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({"application/json;response-pass-through=true"})	@AllowAll
	public Response search(Map search) throws GeneralException {
		
		logger.error(search);

		SailPointContext context = SailPointFactory.getCurrentContext();

		QueryOptions queryOptions = new QueryOptions();
		queryOptions.setResultLimit((Integer)search.get("limit"));
		queryOptions.setFirstRow((Integer)search.get("first"));

		Iterator iterator = context.search(
			(String)search.get("hql")
		, null ,queryOptions);

		List<String> list = new ArrayList<String>();
		
		while(iterator.hasNext()) {
			list.add(JsonHelper.toJson(iterator.next()));
		}
		
		return Response.ok("[" + StringUtils.join(list, ",") + "]").build();

	}
}
