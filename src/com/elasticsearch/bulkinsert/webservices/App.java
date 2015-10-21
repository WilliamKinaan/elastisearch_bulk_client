package com.elasticsearch.bulkinsert.webservices;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/test")
public class App extends ResourceConfig {
	public App() {
		this.packages("com.elasticsearch.bulkinsert.webservices");
	}
}
