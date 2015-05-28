package br.ufrj.cos.prisma.miner.popup.actions;

import minerv1.Activity;
import minerv1.ActivityType;
import minerv1.Minerv1Factory;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.miner.util.BaseFileWalker;

public class ParseFrameworkAction extends BaseAction {

	@Override
	public void run(IAction arg0) {
		super.run(arg0);

		String filePath = this.process.getDir();
		System.out.println("Framework File path: " + filePath);
		
		FrameworkFileWalker fileWalker = new FrameworkFileWalker();
		fileWalker.walk(filePath);
	}
	
	class FrameworkFileWalker extends BaseFileWalker {
		String packageName;
		
		public FrameworkFileWalker() {
			super();
		}
		
		protected void getClassInfo(String className, String fileContent) {
			parser.setSource(fileContent.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			
			final CompilationUnit compilationUnit = (CompilationUnit) parser
					.createAST(null);
			compilationUnit.accept(new ASTVisitor() {
								
				public boolean visit(PackageDeclaration node) {
					packageName = node.getName().getFullyQualifiedName();
					return true;
				}
				
				public boolean visit(TypeDeclaration node) {
					Activity classActivity = Minerv1Factory.eINSTANCE.createActivity();
					classActivity.setName(node.getName().getIdentifier());
					classActivity.setPackageName(packageName);
					
					if (node.isInterface()) {
						System.out.println("Interface: " + node.isInterface());
						classActivity.setType(ActivityType.INTERFACE_REALIZATION);
					} else {
						classActivity.setType(ActivityType.CLASS_EXTENSION);
					}
					classActivity.setId(node.getName().getIdentifier());
					
					if (process.getActivitiesMap().get(classActivity.getName()) == null) {
						int index = process.getActivities().size() == 0 ? 0 : process.getActivities().size() - 1;
						process.getActivitiesMap().put(classActivity.getName(), index);
						process.getActivities().add(classActivity);
					}
					
					save();
					return true;
				}
				
//				Set<String> imports = new HashSet<String>();				
//				public boolean visit(ImportDeclaration importDeclaration) {
//					String importName = importDeclaration.getName().getFullyQualifiedName();
//					imports.add(importName);
//					return false;
//				}
//
//				public boolean visit(MethodDeclaration node) {
//					if (classActivity.getName().contains("GraphicalEditorWithFlyoutPalette")) {
//						System.out.println("found");
//					}
//
//					if (visited.contains(node.getName().getIdentifier())) {						
//						return false;
//					}
//
//					if (classActivity.getName().contains("GraphicalEditorWithFlyoutPalette")) {
//						System.out.println("found later");
//					}
//
//					if (node.getBody() != null) {
//						Activity methodActivity = Minerv1Factory.eINSTANCE.createActivity();
//						methodActivity.setName(node.getName().getFullyQualifiedName());
//						methodActivity.setType(ActivityType.METHOD_EXTENSION);
//						
//						if (parseMethods) {
////							frameworkReuseActions.add(methodActivity);
//						}
//					}
//
//					// Mark as visited
//					visited.add(node.getName().getIdentifier());
//
//					return true;
//				}

			});
			
//			if (!this.framework.getActivitiesMap().containsKey(classActivity.getId())) {
//				this.framework.getActivitiesMap().put(classActivity.getName(), index);
//				this.framework.getActivities().add(classActivity);
//				index++;
//			}
		}
	}

}
