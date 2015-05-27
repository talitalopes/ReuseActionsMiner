package br.ufrj.cos.prisma.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import minerv1.Commit;
import minerv1.Event;
import minerv1.EventDependency;
import minerv1.FrameworkApplication;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class ReuseMinerApplicationTree {

//	List<CustomNode> treeNodes = new ArrayList<CustomNode>();
	DirectedGraph<CustomNode, DefaultEdge> applicationTree;
	String applicationName;
	CustomNode rootNode;

	public class Path {
		String id;
		List<CustomNode> nodes;
		boolean repeats;
		
		public Path() {
			this.id = "";
			this.nodes = new ArrayList<CustomNode>();
		}
		
		public Path(List<CustomNode> nodes) {
			this.nodes = nodes;
		}
		
		public void add(CustomNode node) {
			this.nodes.add(node);
			if (node.getEvent() != null) {
				this.id += node.getEvent().getActivity().getId();
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			return this.id.equals(((Path)obj).id);
		}
		
		@Override
		public int hashCode() {
			return this.id.hashCode();
		}
		
		public void printPath() {
//			String pathString = id + "(" + repeats + ") \t\t\t ";
			String pathString = "";
			for (CustomNode node: this.nodes) {
				if (node.getEvent() == null) {
					pathString += String.format("%s - ", node.id);
					continue;
				}
				String activityKey = node.getEvent().getActivity().getId();
				if (activityKey == null) {
					activityKey = node.getEvent().getActivity().getName();
				}
				pathString += String.format("%s - ", node.commitIndex + activityKey);
			}
			pathString.trim();
//			pathString = pathString.substring(0, pathString.lastIndexOf(" -"));
//			System.out.println(pathString);
		}
	}
	
	public class CustomNode implements Comparable<CustomNode> {
		String id;
		String parent;
		Event event;
		int commitIndex;
		boolean visited;

		public CustomNode(String id) {
			this.id = id;
		}

		public CustomNode(Event e, int commitIndex) {
			this.event = e;
			this.commitIndex = commitIndex;
			this.id = e.getId();
		}

		public Event getEvent() {
			return this.event;
		}
		
		public String getActivityName() {
			if (this.event == null || this.event.getActivity() == null) return null;
			return this.event.getCompleteName();
		}

		public String getEventId() {
			if (this.event == null) {
				return id;
			}
			return this.event.getId();
		}

		@Override
		public boolean equals(Object obj) {
			return ((CustomNode) obj).getEventId().equals(this.getEventId())
					&& ((CustomNode) obj).id.equals(this.id);
		}

		@Override
		public int hashCode() {
			if (this.getEventId().length() > 0) {
				return this.getEventId().hashCode();
			}
			return this.id.hashCode();
		}

		@Override
		public int compareTo(CustomNode node) {
			if (this.commitIndex == node.commitIndex) {
				if (node.getEventId().length() > 0
						&& this.getEventId().length() > 0) {
					String superClass1 = this.getEventId();
					if (this.getEvent() != null) {
						superClass1 = this.getEvent().getActivity().getName();
					}
					
					String superClass2 = node.getEventId();
					if (node.getEvent() != null) {
						superClass2 = node.getEvent().getActivity().getName();
					}
					
					int compareSuperclasses = superClass1.compareTo(superClass2);
					
					if (compareSuperclasses == 0) {
						return this.getEventId().compareTo(node.getEventId());
					}
					return compareSuperclasses;
				}
				
				return this.id.compareTo(node.id);
			}

			if (this.commitIndex < node.commitIndex) {
				return -1;
			}

			return 1;
		}

		public List<CustomNode> getSortedChildren(ReuseMinerApplicationTree tree) {
			Set<DefaultEdge> edges = tree.applicationTree
					.outgoingEdgesOf(this);
			
			List<CustomNode> childrenNodes = new ArrayList<CustomNode>();
			for (DefaultEdge edge: edges) {
				childrenNodes.add(tree.applicationTree.getEdgeTarget(edge));
			}
			
			Collections.sort(childrenNodes, new Comparator<CustomNode>() {
				@Override
				public int compare(CustomNode node1, CustomNode node2) {
					return node1.compareTo(node2);
				}
			});
			
			return childrenNodes;
		}
	}

	public enum VisitorStrategy {
		leaves, rootNode, features
	}

	interface Visitor {
		public void visit();
		public void visitChildren(CustomNode node);
		public void visitNode(CustomNode node);
		public List<CustomNode> getTrace();
	}
	
	class RootVisitor implements Visitor {
		List<CustomNode> trace;
		ReuseMinerApplicationTree tree;
		List<CustomNode> nodesToVisit = new ArrayList<CustomNode>();
		
		public RootVisitor(ReuseMinerApplicationTree tree) {
			this.tree = tree;
			this.trace = new ArrayList<CustomNode>();
		}
		
		@Override
		public void visit() {
			if (this.tree.applicationTree == null) {
				System.out.println("No information for application trace");
				return;
			}
			
			CustomNode rootNode = tree.rootNode;
			nodesToVisit.add(rootNode);
			
			while (!nodesToVisit.isEmpty()) {
				CustomNode nodeToVisit = nodesToVisit.remove(0);
				visitChildren(nodeToVisit);
				visitNode(nodeToVisit);
			}	
		}
		
		@Override
		public void visitChildren(CustomNode node) {
			if (node == null) {
				return;
			}
			
			List<CustomNode> children = this.getSortedChildren(node);
			
			if (!children.isEmpty()) {
				for (CustomNode child : children) {
					nodesToVisit.add(0, child);
				}
			}
		}

		@Override
		public void visitNode(CustomNode node) {
			if (node == null || node.getEvent() == null) {
				return;
			}
			this.trace.add(0, node);
		}
		
		@Override
		public List<CustomNode> getTrace() {
			this.visit();
			return this.trace; //this.groupedActivitiesTrace();
		}
		
		private List<CustomNode> groupedActivitiesTrace() {
			List<CustomNode> groupedArray = new ArrayList<CustomNode>();
			for (int i = 0; i < this.trace.size(); i++) {
				CustomNode n = this.trace.get(i);
				
				if (groupedArray.size() == 0) {
					groupedArray.add(n);
					continue;
				}
				
				int size = groupedArray.size();
				for (int j = 0; j < size; j++) {
					if (groupedArray.get(j).getEvent().getActivity().equals(n.getEvent().getActivity())) {
						groupedArray.add(j, n);
						break;
					}
				}
				
				if (groupedArray.size() == size) {
					groupedArray.add(n);
				}
				
			}
			
			return groupedArray;
		}
		
		public List<CustomNode> getSortedChildren(CustomNode node) {
			Set<DefaultEdge> edges = this.tree.applicationTree
					.outgoingEdgesOf(node);
			
			List<CustomNode> childrenNodes = new ArrayList<CustomNode>();
			for (DefaultEdge edge: edges) {
				childrenNodes.add(this.tree.applicationTree.getEdgeTarget(edge));
			}
			
			Collections.sort(childrenNodes, new Comparator<CustomNode>() {
				@Override
				public int compare(CustomNode node1, CustomNode node2) {
					return node1.compareTo(node2);
				}
			});
			
			return childrenNodes;
		}
	}
	
	class LeavesVisitor implements Visitor {

		Set<CustomNode> nodesToVisit;
		List<CustomNode> trace;
		ReuseMinerApplicationTree tree;		

		public LeavesVisitor(ReuseMinerApplicationTree tree) {
			this.tree = tree;
			this.nodesToVisit = tree.applicationTree.vertexSet();
			this.trace = new ArrayList<CustomNode>();
		}

		private List<CustomNode> getLeaves() {
			List<CustomNode> leaves = new ArrayList<CustomNode>();

			Set<CustomNode> vertexSet = this.nodesToVisit;
			for (CustomNode node : vertexSet) {
				if (this.tree.applicationTree.inDegreeOf(node) > 0
						&& this.tree.applicationTree.outDegreeOf(node) == 0) {
					leaves.add(node);
				}
			}

			Collections.sort(leaves, new Comparator<CustomNode>() {
				@Override
				public int compare(CustomNode node1, CustomNode node2) {
					return node1.compareTo(node2);
				}
			});

			return leaves;
		}
		
		public List<CustomNode> getTrace() {
			return this.trace;
		}

		@Override
		public void visit() {
//			System.out.println("Nodes count: "
//					+ applicationTree.vertexSet().size());
			for (DefaultEdge n : applicationTree.edgeSet()) {
				System.out
						.println(applicationTree.getEdgeSource(n).commitIndex
								+ " -> "
								+ applicationTree.getEdgeTarget(n).commitIndex);
				System.out.println(applicationTree.getEdgeSource(n)
						.getEventId()
						+ " -> "
						+ applicationTree.getEdgeTarget(n).getEventId());
			}
			System.out.println("------");

			List<CustomNode> leaves = this.getLeaves();

			while (!leaves.isEmpty()) {
				CustomNode currentNode = leaves.remove(0);
				if (currentNode.visited) {
					continue;
				}

				visitNode(currentNode);
//				System.out.println("leaves to go: " + leaves.size());
			}
		}

		@Override
		public void visitChildren(CustomNode node) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitNode(CustomNode node) {
			this.trace.add(0, node);

			Set<DefaultEdge> incomingEdges = this.tree.applicationTree
					.incomingEdgesOf(node);
			for (DefaultEdge edge : incomingEdges) {
				CustomNode incomingNode = this.tree.applicationTree
						.getEdgeSource(edge);
				if (incomingNode.visited || incomingNode == node) {
					continue;
				}

				visitNode(incomingNode);
			}

			if (!node.visited) {
				System.out.println("Node: " + node.id);
				node.visited = true;
			}	
		}
	}

	public ReuseMinerApplicationTree(FrameworkApplication app) {
		this.applicationTree = new DefaultDirectedGraph<CustomNode, DefaultEdge>(
				DefaultEdge.class);
		this.applicationName = app.getName();
		this.buildTree(app);
	}

	private void buildTree(FrameworkApplication app) {
		Map<String, CustomNode> treeNodesMap = new HashMap<String, CustomNode>();

		int commitIndex = 0;
		for (Commit c : app.getCommits()) {
			
			for (Event e : c.getEvents()) {
				CustomNode node = new CustomNode(e, commitIndex);
					
				if (!treeNodesMap.containsKey(node.getEventId())) {
					if (e == null) {
						continue;
					}
					
					this.applicationTree.addVertex(node);
					treeNodesMap.put(node.getEventId(), node);
				} else {
					node = treeNodesMap.get(node.getEventId());
				}
				System.out.println(e.getId() + " Node: " + e.getCompleteName() + "commit Index: " + node.commitIndex);
				
				for (EventDependency dep : e.getDependencies()) {
					if (dep.getEvent() == null) {
						continue;
					}
					CustomNode depNode = new CustomNode(dep.getEvent(),
							commitIndex);

					if (!applicationTree.containsVertex(depNode)) {
						applicationTree.addVertex(depNode);
					}

					if (applicationTree.incomingEdgesOf(depNode).size() == 1) {
						DefaultEdge edge = applicationTree
								.incomingEdgesOf(depNode).iterator().next();
						CustomNode parentNode = applicationTree
								.getEdgeSource(edge);
						if (!parentNode.equals(node)) {
							depNode.id = depNode.id + "-" + node.getEventId();
							applicationTree.addVertex(depNode);
//							System.out.println("Duplicated: "
//									+ depNode.getEventId() + " "
//									+ parentNode.getEventId());
						}
					}

					this.applicationTree.addEdge(node, depNode);
//					System.out.println("Dep: " + node.getEvent().getId() + " - " + depNode.getEvent().getId());
				}
			}
			commitIndex++;
		}

		this.rootNode = addRootNode();
		if (this.rootNode == null) {
			System.out.println("Error: No root node found");
			return;
		}
	}

	private CustomNode addRootNode() {
		List<CustomNode> nodesWithoutIncomingEdges = getNodesWithoutIncomingEdges();

		if (nodesWithoutIncomingEdges.size() > 1) {
			CustomNode rootNode = new CustomNode("rootNode");
			applicationTree.addVertex(rootNode);

			for (CustomNode node : nodesWithoutIncomingEdges) {
				applicationTree.addEdge(rootNode, node);
			}

			return rootNode;
		}

		if (nodesWithoutIncomingEdges.size() == 0) {
			return null;
		}

		return nodesWithoutIncomingEdges.get(0);
	}

	private List<CustomNode> getNodesWithoutIncomingEdges() {
		List<CustomNode> rootNodes = new ArrayList<CustomNode>();

		Set<CustomNode> vertexSet = this.applicationTree.vertexSet();
		for (CustomNode node : vertexSet) {
			if (this.applicationTree.inDegreeOf(node) == 0) {
				// && this.applicationTree.outDegreeOf(node) > 0) {
				rootNodes.add(node);
			}
		}

		if (rootNodes.size() == 0) {
			System.out.println("Root node for application: "
					+ this.applicationName + " not found");
		}

		return rootNodes;
	}

	public List<CustomNode> getTrace(VisitorStrategy strategy) {
		List<CustomNode> trace = new ArrayList<CustomNode>();
		
		if (strategy == VisitorStrategy.leaves) {
			Visitor treeVisitor = new LeavesVisitor(this);
			trace = treeVisitor.getTrace();
		} else if (strategy == VisitorStrategy.features) {
			Visitor treeVisitor = new FeatureVisitor(this);
			trace = treeVisitor.getTrace();
		} else {
			Visitor treeVisitor = new RootVisitor(this);
			trace = treeVisitor.getTrace();
		}
				
		int index = 0;
		for (CustomNode node: trace) {
			if (node == null || node.getEventId() == null) {
				continue;
			}
			String key = node.id;
			if (node.getEvent() != null) {
				key = node.getEvent().getActivity().getName();
			}
			index++;
		}
		
		return trace;
	}

	public CustomNode getRootNode() {
		return this.rootNode;
	}
	
	class FeatureVisitor implements Visitor {
		ReuseMinerApplicationTree tree;
		Set<Path> paths = new HashSet<Path>();
		Map<String, Path> pathsMap = new HashMap<String, Path>();
		
		List<CustomNode> nodesToVisit;
		
		public FeatureVisitor(ReuseMinerApplicationTree tree) {
			this.tree = tree;
			this.nodesToVisit = new ArrayList<CustomNode>();
			this.pathsMap = new HashMap<String, Path>();
		}
		
		@Override
		public void visit() {
			if (this.tree == null) {
				return;
			}
			
			CustomNode root = this.tree.rootNode;
			if (root == null) {
				return;
			}
			
			CustomNode rootNode = tree.rootNode;
			nodesToVisit.add(rootNode);
			
			while (!nodesToVisit.isEmpty()) {
				CustomNode nodeToVisit = nodesToVisit.remove(0);
//				visitChildren(nodeToVisit);
				visitNode(nodeToVisit);
			}
			
//			System.out.println("Paths: " + applicationName);
			for (String feature: this.pathsMap.keySet()) {
//				System.out.print(feature + " --- ");
				this.pathsMap.get(feature).printPath();
			}
//			for (Path p: this.pathsMap.values()) {
//				p.printPath();
//			}
//			System.out.println("\n");
		}

		@Override
		public void visitChildren(CustomNode node) {
			for (CustomNode child: node.getSortedChildren(this.tree)) {
				nodesToVisit.add(0, child);
				
				Path path = new Path();
				path.add(node);
				path.add(child);
				
				if (this.paths.contains(path)) {
					this.paths.remove(path);
					path.repeats = true;
					this.paths.add(path);
					
				} else {
					this.paths.add(path);
				}
			}
		}

		@Override
		public void visitNode(CustomNode node) {
			if (tree.applicationTree.outDegreeOf(node) == 0) {
				return;
			}
			
			Path completePath = new Path();
			if (node.getEvent() != null) {
				completePath.add(node);
			}
			
			for (CustomNode child: node.getSortedChildren(this.tree)) {
				if (child.getEvent() == null) {
					continue;
				}
				
				if (tree.applicationTree.outDegreeOf(child) > 0) {
					nodesToVisit.add(0, child);
					String featureId = child.getEvent().getActivity().getId();
					if (featureId == null) {
						featureId = child.getEvent().getActivity().getName();
					}
					
					CustomNode featureNode = new CustomNode("Feature-" + featureId);
					completePath.add(featureNode);
					
				} else {
					completePath.add(child);
				}
			}
			
			if (node.getEvent() == null) {
				pathsMap.put("Root", completePath);	
			} else {
				pathsMap.put("Feature-"+node.getEvent().getActivity().getId(), completePath);
			}
		}

		@Override
		public List<CustomNode> getTrace() {
			this.visit();
			if (pathsMap.get("Root") != null) {
				return pathsMap.get("Root").nodes;	
			}
			return new ArrayList<CustomNode>();
		}
		
	}
}
