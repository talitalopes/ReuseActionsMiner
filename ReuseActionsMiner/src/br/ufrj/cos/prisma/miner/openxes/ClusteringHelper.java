package br.ufrj.cos.prisma.miner.openxes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import minerv1.Activity;
import minerv1.FrameworkApplication;
import minerv1.FrameworkProcess;
import br.ufrj.cos.prisma.model.ReuseMinerApplicationTree;
import br.ufrj.cos.prisma.model.ReuseMinerApplicationTree.CustomNode;
import br.ufrj.cos.prisma.model.ReuseMinerApplicationTree.VisitorStrategy;

public class ClusteringHelper {

	enum SimilarityStrategy {
		EDIT_DISTANCE, COMMON_ACTIVITIES
	}

	FrameworkProcess fwProcess;
	List<String> applicationsNames;
	
	public ClusteringHelper(FrameworkProcess fwProcess) {
		this.fwProcess = fwProcess;
		this.applicationsNames = new ArrayList<String>();
	}

	public List<List<CustomNode>> getProcessTraces() {
		List<List<CustomNode>> traces = new ArrayList<List<CustomNode>>();

		int numericalId = 0;
		Map<Activity, Integer> activityIds = new HashMap<Activity, Integer>();
		for (Activity a : fwProcess.getActivities()) {
			activityIds.put(a, numericalId);
			numericalId++;
		}

		for (FrameworkApplication application : fwProcess.getApplications()) {
			if (application == null) {
				System.out.println("Null application");
				continue;
			}
			String appName = application.getName();

			ReuseMinerApplicationTree tree = new ReuseMinerApplicationTree(
					application);
			List<CustomNode> traceNodes = tree
					.getTrace(VisitorStrategy.rootNode);

			if (traceNodes.size() == 0) {
				System.out.println("Empty trace for application: " + appName);
				continue;
			}
			System.out.println("Events: " + traceNodes.size());
			applicationsNames.add(appName);
			
			traces.add(traceNodes);
		}

		return traces;
	}

	public List<List<Float>> calculateSimilarityMatrix(
			List<List<CustomNode>> traces, SimilarityStrategy strategy) {
		List<List<Float>> matrix = new ArrayList<List<Float>>();

		for (int i = 0; i < traces.size(); i++) {
			List<Float> sim = new ArrayList<Float>();
			for (int j = 0; j < traces.size(); j++) {
				if (i == j) {
					sim.add((float) 1);
				} else {
					sim.add((float) 0);
				}
			}
			matrix.add(sim);
		}

		Map<Activity, String> activityCharMap = null;
		if (strategy == SimilarityStrategy.EDIT_DISTANCE) {
			activityCharMap = this.mapActivities();
		}

		for (int i = 0; i < traces.size(); i++) {
			List<Float> line = matrix.get(i);

			for (int j = 0; j < traces.size(); j++) {
				if (strategy == SimilarityStrategy.COMMON_ACTIVITIES && i == j) {
					line.set(j, (float) 1.0);
					continue;
				}

				List<CustomNode> trace1 = traces.get(i);
				List<CustomNode> trace2 = traces.get(j);

				float similarity = 0.0f;
				if (strategy == SimilarityStrategy.COMMON_ACTIVITIES) {
					similarity = calculateSimilarityByIntersection(trace1,
							trace2);
				} else {
					similarity = calculateSimilarityUsingEditDistance(
							activityCharMap, trace1, trace2);
				}

				line.set(j, similarity);
			}

			matrix.set(i, line);
		}

		printMatrix(fwProcess, matrix);
		return matrix;
	}

	public void printMatrix(FrameworkProcess fwProcess, List<List<Float>> matrix) {
		String appslineStr = "";
		for (String appName : this.applicationsNames) {
				appslineStr += String.format("%s\t", appName);
		}
		System.out.println(appslineStr);

		for (int i = 0; i < matrix.size(); i++) {
			List<Float> line = matrix.get(i);
			String lineStr = "";
			for (int j = 0; j < matrix.size(); j++) {
				// System.out.println(String.format("Tata nita!!! %d", j)); //
				// By Kiks
				lineStr += String.format("%.3f\t", line.get(j));
			}
			System.out.println(lineStr);
		}
	}

	public Map<Float, Set<Set<Integer>>> findClusters(List<List<Float>> matrix,
			List<Float> thresholds) {
		Map<Float, Set<Set<Integer>>> clustersMap = new HashMap<Float, Set<Set<Integer>>>();
		Map<Set<Integer>, Float> similarityMap = new HashMap<Set<Integer>, Float>();

		while (!thresholds.isEmpty()) {
			float currentThreshold = thresholds.remove(0);

			for (int matrixLine = 0; matrixLine < matrix.size(); matrixLine++) {
				Set<Integer> cluster = getTracesClusters(matrix, matrixLine,
						currentThreshold);
				if (cluster == null || cluster.size() <= 1) {
					continue;
				}

				if (!similarityMap.containsKey(cluster)) {
					similarityMap.put(cluster, currentThreshold);
				}
			}
		}

		for (Set<Integer> cluster : similarityMap.keySet()) {
			float threshold = similarityMap.get(cluster);

			Set<Set<Integer>> clusters = clustersMap.get(threshold);
			if (clusters == null) {
				clusters = new HashSet<Set<Integer>>();
			}
			clusters.add(cluster);
			clustersMap.put(threshold, clusters);
		}

		return clustersMap;
	}

	public Set<Integer> getTracesClusters(List<List<Float>> matrix,
			int lineIndex, float threshold) {
		Set<Integer> visited = new HashSet<Integer>();
		List<Integer> linesToVisit = new ArrayList<Integer>();
		linesToVisit.add(lineIndex);

		Set<Integer> cluster = new HashSet<Integer>();
		while (!linesToVisit.isEmpty()) {
			int currentLine = linesToVisit.remove(0);
			visited.add(currentLine);
			cluster.add(currentLine);

			for (int j = currentLine; j < matrix.size(); j++) {
				if (matrix.get(currentLine).get(j) >= threshold) {
					cluster.add(j);

					if (!visited.contains(j)) {
						linesToVisit.add(j);
					}
				}
			}
		}

		if (cluster.size() <= 1) {
			return null;
		}

		return cluster;
	}

	public Map<Float, Set<Set<FrameworkApplication>>> getClusters() {
		// All Applications
		List<FrameworkApplication> apps = fwProcess.getApplications();

		// List traces for process
		List<List<CustomNode>> traces = getProcessTraces();

		// Calculate similarity using the following metric : (|A| intersect |B|)
		// / (|A| union |B|)
		List<List<Float>> matrix = calculateSimilarityMatrix(traces, SimilarityStrategy.COMMON_ACTIVITIES);

		// Define Thresholds for clustering
		List<Float> thresholds = new ArrayList<Float>(Arrays.asList(1.0f));
		// , 0.9f,0.8f, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f));

		// Find clusters for each threshold in the list
		Map<Float, Set<Set<Integer>>> clustersMap = findClusters(matrix,
				thresholds);

		Map<Float, Set<Set<FrameworkApplication>>> clustersAppMap = new HashMap<Float, Set<Set<FrameworkApplication>>>();

		for (Float threshold : clustersMap.keySet()) {
			System.out
					.println("Clusters with similarity equal or greater than: "
							+ threshold);
			Set<Set<Integer>> clusters = clustersMap.get(threshold);
			Set<Set<FrameworkApplication>> thresholdAppsClusters = new HashSet<Set<FrameworkApplication>>();

			for (Set<Integer> cluster : clusters) {
				Set<FrameworkApplication> clusterApps = new HashSet<FrameworkApplication>();

				String clusterStr = "Cluster";
				for (Integer i : cluster) {
					clusterStr += String.format(" - %s", apps.get(i).getName());
					clusterApps.add(apps.get(i));
				}

				thresholdAppsClusters.add(clusterApps);
				System.out.println(clusterStr);
			}

			clustersAppMap.put(threshold, thresholdAppsClusters);
		}

		return clustersAppMap;
	}

	public float calculateSimilarityByIntersection(List<CustomNode> trace1,
			List<CustomNode> trace2) {
		Collections.sort(trace1);
		Collections.sort(trace2);

		Set<String> commomNodes = new HashSet<String>();
		Set<String> trace1Set = new HashSet<String>();
		Set<String> trace2Set = new HashSet<String>();

		for (CustomNode n : trace1) {
			if (n.getEvent() == null) {
				continue;
			}
			commomNodes.add(n.getEvent().getActivity().getName());
			trace1Set.add(n.getEvent().getActivity().getName());
		}

		for (CustomNode n : trace2) {
			if (n.getEvent() == null) {
				continue;
			}
			commomNodes.add(n.getEvent().getActivity().getName());
			trace2Set.add(n.getEvent().getActivity().getName());
		}
		int commonNodesCount = commomNodes.size();

		trace1Set.retainAll(trace2Set);
		int intersectionCount = trace1Set.size();

		float similarity = (float) intersectionCount / commonNodesCount;
		return similarity;
	}

	public float calculateSimilarityUsingEditDistance(
			Map<Activity, String> activityCharMap, List<CustomNode> trace1,
			List<CustomNode> trace2) {
		
		String trace1Str = mapTraceToString(activityCharMap, trace1);
		String trace2Str = mapTraceToString(activityCharMap, trace2);
		return minDistance(trace1Str, trace2Str);
	}
	
	public String mapTraceToString(Map<Activity, String> activityCharMap, List<CustomNode> trace) {
		String traceStr = "";
		
		for (CustomNode n: trace) {
			if (n.getEvent() == null || n.getEvent().getActivity() == null) {
				continue;
			}
			traceStr += activityCharMap.get(n.getEvent().getActivity());
		}
		return traceStr;
	}

	public int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}

		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}

		// iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);

				// if last two chars equal
				if (c1 == c2) {
					// update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}

		return dp[len1][len2];
	}

	public Map<Activity, String> mapActivities() {
		Map<Activity, String> activityCharMap = new HashMap<Activity, String>();

		int count = fwProcess.getActivities().size();
		int charSize = String.valueOf(count).length();
		String charFormat = "%0" + charSize + "d";
		int index = 1;

		for (Activity a : fwProcess.getActivities()) {
			String activityChar = String.format(charFormat, index);
			activityCharMap.put(a, activityChar);

			System.out.println(a.getName() + " -- " + activityChar);

			index++;
		}

		return activityCharMap;
	}

}