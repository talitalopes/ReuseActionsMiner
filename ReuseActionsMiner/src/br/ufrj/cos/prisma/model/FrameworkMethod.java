package br.ufrj.cos.prisma.model;

import japa.parser.ast.body.MethodDeclaration;

public class FrameworkMethod {
	
	MethodDeclaration method;
	
	public FrameworkMethod() {
		
	}

	public FrameworkMethod(MethodDeclaration method) {
		this.method = method;
	}

	public MethodDeclaration getMethod() {
		return method;
	}

	public void setMethod(MethodDeclaration method) {
		this.method = method;
	}
	
}
