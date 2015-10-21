package com.elasticsearch.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class Util {
	public static String uglyJsonToPrettyJson(String uglyJson) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJson);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
	}
}
