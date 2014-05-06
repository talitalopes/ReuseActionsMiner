package br.ufrj.cos.prisma.model;

import java.io.File;

import com.google.gson.annotations.SerializedName;

/**
 * @author talitalopes
 *
 */
public class GithubRepository {

	@SerializedName("full_name")
	String fullName;
	String id;
	String name;
	String description;
	String url;
	@SerializedName("clone_url")
	String cloneUrl;
	
	String localDir;
	File repoFile;
	
	public String getFullName() {
		return fullName;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getCloneUrl() {
		return cloneUrl;
	}

	public String getLocalDir() {
		return localDir;
	}

	public File getRepoFile() {
		return repoFile;
	}
	
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
		this.repoFile = new File(localDir);
	}
	
}
