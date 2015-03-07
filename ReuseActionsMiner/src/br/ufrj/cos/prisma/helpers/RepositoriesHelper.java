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
	private static final String REPO_SEARCH_PAGE_URL_FORMAT = "https://api.github.com/search/repositories?q=%s+language:java+in:name,description,readme&sort=stars&order=desc&page=%d";	
	private static final String REPO_CLONE_LOCAL_DIR = "/users/talitalopes/Documents/Mestrado/github/";
	
	public static List<GithubRepository> listRepositories(String searchKey) {
		ArrayList<String> jsonResponses = getRepositoriesJSON(searchKey);
		List<GithubRepository> repositories = new ArrayList<GithubRepository>();
		
		for (String jsonStr : jsonResponses) {
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
		}
		
		return repositories;
	}

	private static ArrayList<String> getRepositoriesJSON(String searchKey) {
		String urlStr = String.format(REPO_SEARCH_URL_FORMAT, searchKey);
		ArrayList<String> output = new ArrayList<String>();
		
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
			output.add(writer.toString());
			conn.disconnect();
			
			String allLinksStr = conn.getHeaderField("Link");
			String[] links = allLinksStr.split(",");
			ArrayList<String> urls = new ArrayList<String>();
			
			for (int i = 0; i < links.length; i++) {
				String trimmedUrl = links[i].split(";")[0].trim().replace("<", "").replace(">", ""); 
				urls.add(trimmedUrl);
			}
			
			String nextUrl = urls.get(0);
			String lastUrl = urls.get(1);
			
			int nextPage = Integer.parseInt(nextUrl.split("page=")[1]);
			int lastPage = Integer.parseInt(lastUrl.split("page=")[1]);
			
			for (int page = nextPage; page <= lastPage; page++) {
				String pageUrlString = String.format(REPO_SEARCH_PAGE_URL_FORMAT, searchKey, page);
				
				URL pageUrl = new URL(pageUrlString);
				HttpURLConnection pageConn = (HttpURLConnection) pageUrl.openConnection();
				pageConn.setRequestMethod("GET");
				pageConn.setRequestProperty("Accept", "application/json");

				if (pageConn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ pageConn.getResponseCode());
				}

				StringWriter pageWriter = new StringWriter();
				IOUtils.copy(pageConn.getInputStream(), pageWriter, "UTF-8");
				output.add(pageWriter.toString());
				pageConn.disconnect();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}
	
}
