package br.ufrj.cos.prisma.miner.popup.actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import minerv1.FrameworkApplication;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;

import br.ufrj.cos.prisma.helpers.LogHelper;
import br.ufrj.cos.prisma.miner.openxes.ClusteringHelper;
import br.ufrj.cos.prisma.miner.openxes.XESLogGenerator;

public class GenerateXESLogAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		super.run(action);
		
		// Generate log considering all applications
		String exportPath = this.getExportPath();
		XESLogGenerator xesGen = new XESLogGenerator(true, exportPath);
		xesGen.getProcessTraces(process);
		xesGen.serialize(generateFilename(process.getKeyword() + "-complete"));
		
		// Clustered logs
		getTracesClusters();
		
//		Testing log separated by commits
//		xesGen.getXESRepresentationForCommits(process);
		
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.FOLDER, null);
		} catch (CoreException e) {
			LogHelper.log("Error refreshing workspace");
		}
	}
	
	private void getTracesClusters() {
		ClusteringHelper clusteringHelper = new ClusteringHelper(process);
		Map<Float, Set<Set<FrameworkApplication>>> clustersAppMap = clusteringHelper.getClusters();
		
		String exportPath = this.getExportPath();
		XESLogGenerator xesGen = new XESLogGenerator(true, exportPath);
		
		for (Float threshold: clustersAppMap.keySet()) {
			
			Set<Set<FrameworkApplication>> clusters = clustersAppMap.get(threshold);
			int clusterIndex = 1;
			
			for (Set<FrameworkApplication> cluster: clusters) {
				String logPrefix = String.format("%s-Cluster%d-%.1fT", process.getKeyword(), clusterIndex, threshold);
				xesGen.generateLogForCluster(logPrefix, cluster, threshold);
				clusterIndex++;
			}
		}
	}
	
	private String getExportPath() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String workspacePathString = workspace.getRoot().getLocation().toFile().getAbsolutePath();
		URI resourceURI = this.resource.getURI();
		
		if (resourceURI.segmentCount() > 0) {
			Path exportPath = (Path) new Path(workspacePathString).append(resourceURI.segment(0)).append("xes logs");
			System.out.println("exportPath: " + exportPath);
			return exportPath.toString();
		}
		
		return null;
	}
	
	private static String generateFilename(String prefix) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd_HH:mm");
		String date = dateFormat.format(Calendar.getInstance().getTime());
		
		return String.format("%s-%s.xes",
				prefix,
				date);
	}
}
