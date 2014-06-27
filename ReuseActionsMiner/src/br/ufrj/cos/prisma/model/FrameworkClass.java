package br.ufrj.cos.prisma.model;

import japa.parser.ast.body.ClassOrInterfaceDeclaration;

import java.util.HashSet;
import java.util.Set;

public class FrameworkClass {

	Set<FrameworkMethod> frameworkMethods;
	ClassOrInterfaceDeclaration classDeclaration;
	
	public FrameworkClass() {
		frameworkMethods = new HashSet<FrameworkMethod>();
	}
	
	public FrameworkClass(ClassOrInterfaceDeclaration classDeclaration) {
		frameworkMethods = new HashSet<FrameworkMethod>();
		this.classDeclaration = classDeclaration;
	}

	public void addFrameworkMethod(FrameworkMethod fwMethod) {
		this.frameworkMethods.add(fwMethod);
	}
	
	public void addFrameworkMethods(Set<FrameworkMethod> methods) {
		this.frameworkMethods.addAll(methods);
	}

	public Set<FrameworkMethod> getFrameworkMethods() {
		return frameworkMethods;
	}

	public void setFrameworkMethods(Set<FrameworkMethod> frameworkMethods) {
		this.frameworkMethods = frameworkMethods;
	}

	public ClassOrInterfaceDeclaration getClassDeclaration() {
		return classDeclaration;
	}

	public void setClassDeclaration(ClassOrInterfaceDeclaration classDeclaration) {
		this.classDeclaration = classDeclaration;
	}

	
}
