package com.elasticsearch.bulkinsert.webservices;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import com.elasticsearch.bulkinsert.constants.IConstants;
import com.elasticsearch.helper.Util;

@Path("bulk")
public class Bulk {

	@GET
	@Path("/testinsert")
	public Response testInsert() throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut request = new HttpPut("http://" + IConstants.serverIpOrName
				+ ":" + IConstants.serverPort + "/" + IConstants.indexName
				+ "/" + IConstants.typeName + "/_bulk");
		StringEntity body = new StringEntity(
				"{\"create\":{}}\n{\"name\":\"william\"}\n");
		request.setEntity(body);
		HttpResponse response = client.execute(request);
		String responseString = new BasicResponseHandler()
				.handleResponse(response);
		String prettyResponse = Util.uglyJsonToPrettyJson(responseString);
		System.out.println(prettyResponse);
		return Response.ok(prettyResponse, MediaType.APPLICATION_JSON).build();
	}
}
