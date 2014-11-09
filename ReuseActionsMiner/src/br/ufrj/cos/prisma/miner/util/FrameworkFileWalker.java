package br.ufrj.cos.prisma.miner.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import minerv1.Activity;
import minerv1.ActivityType;
import minerv1.Minerv1Factory;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class FrameworkFileWalker {
	ASTParser parser;
	List<Activity> frameworkReuseActions;
	boolean parseMethods = false;
	
	public FrameworkFileWalker() {
		parser = ASTParser.newParser(AST.JLS3);
		frameworkReuseActions = new ArrayList<Activity>();
	}

	public List<Activity> getReuseActions() {
		return frameworkReuseActions;
	}

	public void walk(String path) {
		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath());
				continue;
			}

			try {
				String content = readFile(f.getAbsolutePath(),
						StandardCharsets.UTF_8);
				getClassInfo(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private void getClassInfo(String fileContent) {
		parser.setSource(fileContent.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit compilationUnit = (CompilationUnit) parser
				.createAST(null);
		compilationUnit.accept(new ASTVisitor() {
			Set<String> visited = new HashSet<String>();
			Activity classActivity = Minerv1Factory.eINSTANCE.createActivity();
			Set<String> imports = new HashSet<String>();

			public boolean visit(PackageDeclaration node) {
				classActivity.setPackageName(node.getName().getFullyQualifiedName());
				classActivity.setType(ActivityType.CLASS_EXTENSION);
				frameworkReuseActions.add(classActivity);
				return true;
			}

			public boolean visit(TypeDeclaration node) {
				classActivity.setName(node.getName().toString());
				
				// Mark as visited
				visited.add(node.getName().getIdentifier());

				frameworkReuseActions.add(classActivity);

				return true;
			}

			
			public boolean visit(ImportDeclaration importDeclaration) {
				String importName = importDeclaration.getName().getFullyQualifiedName();
				imports.add(importName);
				return false;
			}
			
			public boolean visit(MethodDeclaration node) {
				if (visited.contains(node.getName().getIdentifier())) {
					return false;
				}

				if (node.getBody() != null) {
					Activity methodActivity = Minerv1Factory.eINSTANCE.createActivity();
					methodActivity.setName(node.getName().getFullyQualifiedName());
					methodActivity.setType(ActivityType.METHOD_EXTENSION);

					// Mark as visited
					visited.add(node.getName().getIdentifier());
					
					if (parseMethods) {
						frameworkReuseActions.add(methodActivity);
					}
				}

				return true;
			}

		});
	}
}