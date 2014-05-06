package br.ufrj.cos.prisma.helpers;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import br.ufrj.cos.prisma.model.GithubRepository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RepositoriesHelper {

	private static final String REPO_SEARCH_URL_FORMAT = "https://api.github.com/search/repositories?q=%s+language:java+in:name,description,readme&sort=stars&order=desc";
	private static final String REPO_CLONE_LOCAL_DIR = "/users/talitalopes/Documents/Mestrado/github/";
	
	public static List<GithubRepository> listRepositories(String searchKey) {
		String jsonStr = getRepositoriesJSON(searchKey);
		List<GithubRepository> repositories = new ArrayList<GithubRepository>();
		
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonParser().parse(jsonStr)
				.getAsJsonObject();
		JsonArray content = jsonObject.get("items").getAsJsonArray();
		
		Iterator<JsonElement> it = content.iterator();
		while (it.hasNext()) {
			JsonElement repoJson = it.next();
			GithubRepository repo = gson.fromJson(repoJson, GithubRepository.class);
			
			String repoLocalDir = String.format("%s%s", REPO_CLONE_LOCAL_DIR, repo.getName());
			repo.setLocalDir(repoLocalDir);
			
			repositories.add(repo);
		}
		
		return repositories;
	}

	private static String getRepositoriesJSON(String searchKey) {
		String output = "";
		String urlStr = String.format(REPO_SEARCH_URL_FORMAT, searchKey);

		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			StringWriter writer = new StringWriter();
			IOUtils.copy(conn.getInputStream(), writer, "UTF-8");
			output = writer.toString();			
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}
}
