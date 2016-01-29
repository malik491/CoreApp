/**
 * Class to parse and cache queries ina SQL file
 */
package edu.depaul.se491.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Malik
 */
public class SQLFileCache {
	private static SQLFileCache instance; // only one instance to use caching
	private HashMap<String, List<String>> cache = new HashMap<String, List<String>>(100);
	
	
	public static SQLFileCache getInstance() {
		if (instance == null)
			instance = new SQLFileCache();
		return instance;
	}

	private SQLFileCache() {}

	public List<String> getQueries(String fileName) throws FileNotFoundException, IOException {
		List<String> queries = cache.get(fileName);
		if (queries == null)
			queries = parseAndCache(fileName);
		return queries;
	}

	private List<String> parseAndCache(String fileName) throws FileNotFoundException, IOException {
		List<String> queries = parseSQLFile(fileName);
		cache.put(fileName, queries);
		return queries;
	}

	private List<String> parseSQLFile(String filepath) throws FileNotFoundException, IOException {
		List<String> queries = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		String line = "";
		String currentQuery = "";
		while ((line = reader.readLine()) != null) {
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == ';') {
					queries.add(currentQuery);
					currentQuery = "";
				} else
					currentQuery += line.charAt(i);
			}
		}		
		reader.close();
		return queries;
	}
	
	
}
