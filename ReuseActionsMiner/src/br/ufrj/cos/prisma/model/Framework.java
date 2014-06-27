package br.ufrj.cos.prisma.model;

import java.util.HashSet;
import java.util.Set;

public class Framework {
	
	String name;
	Set<FrameworkClass> frameworkClasses;
	
	public Framework() {
		frameworkClasses = new HashSet<FrameworkClass>();
	}
	
	public void addFrameworkClass(FrameworkClass c) {
		this.frameworkClasses.add(c);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<FrameworkClass> getFrameworkClasses() {
		return frameworkClasses;
	}

	public void setFrameworkClasses(Set<FrameworkClass> frameworkClasses) {
		this.frameworkClasses = frameworkClasses;
	}
	
}
