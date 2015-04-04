package br.ufrj.cos.prisma.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

	List<CustomNode> treeNodes = new ArrayList<CustomNode>();
	DirectedGraph<CustomNode, DefaultEdge> applicationTree;
	String applicationName;
	CustomNode rootNode;

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
					String superClass1 = this.getEvent().getActivity().getName();
					String superClass2 = node.getEvent().getActivity().getName();
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

	}

	public enum VisitorStrategy {
		leaves, rootNode
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
			return this.trace;
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
			System.out.println("Nodes count: "
					+ applicationTree.vertexSet().size());
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
				System.out.println("leaves to go: " + leaves.size());
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
					this.applicationTree.addVertex(node);
					treeNodesMap.put(node.getEventId(), node);
				} else {
					node = treeNodesMap.get(node.getEventId());
				}

				for (EventDependency dep : e.getDependencies()) {
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
				}
			}
			commitIndex++;
		}

		this.rootNode = addRootNode();
		if (this.rootNode == null) {
			System.out.println("Error: No root node found");
			return;
		}
		System.out.println("Root node: " + this.rootNode.id);
	}

	private CustomNode addRootNode() {
		List<CustomNode> nodesWithoutIncomingEdges = getNodesWithoutIncomingEdges();
		System.out.println("nodesWithoutIncomingEdges - "
				+ nodesWithoutIncomingEdges.size());

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
		} else {
			Visitor treeVisitor = new RootVisitor(this);
			trace = treeVisitor.getTrace();
		}
				
		int index = 0;
		for (CustomNode node: trace) {
			if (node == null || node.getEventId() == null) {
				continue;
			}
			System.out.println(String.format("%d - %s", index, node.getEvent().getActivity().getName()));
			index++;
		}
		
		return trace;
	}

}
