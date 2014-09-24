package br.ufrj.cos.prisma.miner.popup.actions;

import java.util.ArrayList;
import java.util.List;

import minerv1.FrameworkApplication;

import org.eclipse.jface.action.IAction;

public class SimilarityCalculationAction extends BaseAction {

	List<List<Integer>> similarityMatrix;
	
	public SimilarityCalculationAction() {
		super();
		similarityMatrix = new ArrayList<List<Integer>>();
	}

	@Override
	public void run(IAction action) {
		super.run(action);
		calculateSimilarity();
	}

	public void calculateSimilarity() {
		
		System.out.println("Applications total: "
				+ this.process.getApplications().size());
		
		for (FrameworkApplication app1: this.process.getApplications()) {
			List<Integer> similarityValues = new ArrayList<Integer>();
			
			for (FrameworkApplication app2: this.process.getApplications()) {
				similarityValues.add(1);
			}
			
			similarityMatrix.add(similarityValues);
		}
	}
	
	private void printSimilarityMatrix() {
		
	}

}
