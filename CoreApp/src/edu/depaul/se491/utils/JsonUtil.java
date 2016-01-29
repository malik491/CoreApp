/**
 * 
 */
package edu.depaul.se491.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * @author Malik
 */
public class JsonUtil {
		
	public static <T> String toJson(T obj) throws JsonParseException {
		return new Gson().toJson(obj);
	}
	
	public static <T> T fromJson(String jsonFormatedObj, Class<T> type) throws JsonParseException {
		return new Gson().fromJson(jsonFormatedObj, type);
	}

	public static final String EMPTY_JSON_OBJ = "{}";
}
