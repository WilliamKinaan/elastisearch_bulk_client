package com.elasticsearch.bulkinsert.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

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

	@GET
	@Path("/insertFromFile")
	public Response insertFromFile(@QueryParam("filePath") String filePath) {

		try {
			ClassLoader classLoader = new Bulk().getClass().getClassLoader();
			String newFilePath = classLoader.getResource(filePath).getFile();
			File file = new File(newFilePath);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferReader = new BufferedReader(fileReader);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = bufferReader.readLine()) != null) {
				builder.append(line);
			}
			bufferReader.close();
			JSONArray array = new JSONArray(builder.toString());
			StringBuilder bulkRequestBody = new StringBuilder();
			for (int i = 0; i < array.length(); i++) {
				JSONObject oneJSON = array.getJSONObject(i);
				String building = oneJSON.getString(IConstants.building);
				String emirate = oneJSON.getString(IConstants.emirate);
				String country = oneJSON.getString(IConstants.country);
				String address = oneJSON.getString(IConstants.address);
				String latitude = oneJSON.getDouble(IConstants.latitude) + "";
				String longitude = oneJSON.getDouble(IConstants.longitude) + "";
				bulkRequestBody.append("{");
				bulkRequestBody.append("\"create\":{}");
				bulkRequestBody.append("}\n");

				bulkRequestBody.append("{");
				bulkRequestBody.append("\"building\":\"").append(building)
						.append("\",");
				bulkRequestBody.append("\"emirate\":\"").append(emirate)
						.append("\",");
				bulkRequestBody.append("\"country\":\"").append(country)
						.append("\",");
				bulkRequestBody.append("\"address\":\"").append(address)
						.append("\",");
				bulkRequestBody.append("\"latitude\":\"").append(latitude)
						.append("\",");
				bulkRequestBody.append("\"longitude\":\"").append(longitude)
						.append("\"");
				bulkRequestBody.append("}\n");
			}
			HttpHost host = new HttpHost(IConstants.serverIpOrName,
					IConstants.serverPort);
			HttpClient client = HttpClientBuilder.create().build();
			HttpPut request = new HttpPut("/" + IConstants.indexName + "/"
					+ IConstants.typeName + "/_bulk");
			StringEntity body = new StringEntity(bulkRequestBody.toString());
			request.setEntity(body);
			HttpResponse response = client.execute(host, request);
			String responseString = new BasicResponseHandler()
					.handleResponse(response);
			String prettyResponse = Util.uglyJsonToPrettyJson(responseString);
			System.out.println(prettyResponse);
			return Response.ok(prettyResponse, MediaType.APPLICATION_JSON)
					.build();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return Response.status(200).entity("fuck god file doesn't exist")
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
	}
}
