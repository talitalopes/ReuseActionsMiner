package br.ufrj.cos.prisma.miner.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import minerv1.Commit;
import minerv1.Event;
import minerv1.EventDependency;
import minerv1.FrameworkApplication;

public class TopologicalSort {

	FrameworkApplication app;
	Commit commit;
	List<Event> sortedEvents;
	Set<String> visited = new HashSet<String>();
	
	public TopologicalSort(Commit c) {
		this.commit = c;
		this.sortedEvents = new ArrayList<Event>();
	}
	
	public TopologicalSort(FrameworkApplication app) {
		this.app = app;
		this.sortedEvents = new ArrayList<Event>();
	}

	public void sortCommit() {
		this.sortCommit(this.commit);
	}
	
	public void sortApplicationEvents() {
		for (Commit c: app.getCommits()) {
			System.out.println("Commit: " + c.getId());
			for (Event e: c.getEvents()) {
				this.visitDependencies(e);
			}
			System.out.println("");
		}
//		for (Commit c: app.getCommits()) {
//			List<Event> localSortedEvents = this.sortCommit(c);
//			List<Event> eventsToDelete = new ArrayList<Event>();
//			for (Event e: this.sortedEvents) {
//				if (localSortedEvents.contains(e)) {
//					eventsToDelete.add(e);
//				}
//			}
//			
//			this.sortedEvents.removeAll(eventsToDelete);
//			this.sortedEvents.addAll(localSortedEvents);
//		}
	}
	
	public void visitDependencies(Event e) {
		if (e.getDependencies() != null) {
			for (EventDependency dep: e.getDependencies()) {
				visitDependencies(dep.getEvent());
			}
		}
		
		visitEvent(e);
	}
	
	public void visitEvent(Event e) {
		if (!this.visited.contains(e.getId())) {
			System.out.println("Event: " + e.getId());
			this.visited.add(e.getId());
			this.sortedEvents.add(e);
		}
	}
	
	public List<Event> sortCommit(Commit c) {
		GraphTS g = new GraphTS(c);
		g.topo();
		
		List<Event> sortedEvents = new ArrayList<Event>();
		
		for (int i = 0; i < g.sortedArray.length; i++) {
			if (g.sortedArray[i].event == null) {
				System.out.println("null event");
				continue;
			}
			System.out.println("Event id: " + g.sortedArray[i].event.getActivity().getId());
			sortedEvents.add(g.sortedArray[i].event);
		}
		
		return sortedEvents;
	}
	
	public void sort() {
		for (Commit c : this.app.getCommits()) {
			sortCommit(c);
		}
	}
	
	public List<Event> getSortedEvents() {
//		this.sort();
		this.sortApplicationEvents();
		return this.sortedEvents;
	}
	
	public List<Event> getSortedEventsForCommit() {
		this.sortCommit();
		return this.sortedEvents;
	}
	

	class Vertex {
		public Event event;

		public Vertex(Event e) {
			event = e;
		}
	}

	public class GraphTS {
		private Vertex vertexList[]; // list of vertices
		private int matrix[][]; // adjacency matrix
		private int numVerts; // current number of vertices
		private Vertex sortedArray[];

		public GraphTS(Commit c) {
			int vertexCount = c.getEvents().size();
			vertexList = new Vertex[vertexCount];
			
			numVerts = 0;
			for (Event e: c.getEvents()) {
				addVertex(e);
			}
			
			matrix = new int[vertexCount][vertexCount];

			
			for (int i = 0; i < vertexCount; i++) {
				for (int k = 0; k < vertexCount; k++) {
					matrix[i][k] = 0;
				}
			}

			sortedArray = new Vertex[vertexCount];
		}

		public void addVertex(Event e) {
			vertexList[numVerts++] = new Vertex(e);
		}

		public void addEdge(int start, int end) {
			matrix[start][end] = 1;
		}

		public void displayVertex(int v) {
			System.out.print(vertexList[v].event.getId());
		}

		// toplogical sort
		public void topo() { 
			// while vertices remain, get a vertex with no successors, or -1
			while (numVerts > 0) {
				int currentVertex = noSuccessors();
				
				if (currentVertex == -1) { // must be a cycle
					System.out.println("ERROR: Graph has cycles");
					return;
				}
				
				// insert vertex label in sorted array (start at end)
				sortedArray[numVerts - 1] = vertexList[currentVertex];
				deleteVertex(currentVertex); // delete vertex
			}
		}

		// returns vert with no successors (or -1 if no such verts)
		public int noSuccessors() {
			boolean isEdge; // edge from row to column in adjMat

			for (int row = 0; row < numVerts; row++) {
				isEdge = false; // check edges
				for (int col = 0; col < numVerts; col++) {
					if (matrix[row][col] > 0) { // if edge to another,
						isEdge = true;
						break; // this vertex has a successor try another
					}
				}
				
				// if no edges, has no successors
				if (!isEdge) {
					return row;
				}
			}
			
			return -1; // no
		}

		public void deleteVertex(int delVert) {
			// if not last vertex, delete from vertexList
			if (delVert != numVerts - 1) {
				for (int j = delVert; j < numVerts - 1; j++) {
					vertexList[j] = vertexList[j + 1];
				}

				for (int row = delVert; row < numVerts - 1; row++) {
					moveRowUp(row, numVerts);
				}

				for (int col = delVert; col < numVerts - 1; col++) {
					moveColLeft(col, numVerts - 1);
				}
			}
			numVerts--; // one less vertex
		}

		private void moveRowUp(int row, int length) {
			for (int col = 0; col < length; col++) {
				matrix[row][col] = matrix[row + 1][col];
			}
		}

		private void moveColLeft(int col, int length) {
			for (int row = 0; row < length; row++) {
				matrix[row][col] = matrix[row][col + 1];
			}
		}

	}

}