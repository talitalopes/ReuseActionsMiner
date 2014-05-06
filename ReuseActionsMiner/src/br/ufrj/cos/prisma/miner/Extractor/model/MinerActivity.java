package br.ufrj.cos.prisma.miner.Extractor.model;

import miner.Activity;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class MinerActivity {

	IType superClassFWType;
	IType classType;
	Activity activity;
	String activityKey;

	public Activity getActivity() {
		return this.activity;
	}

	public String getActivityKey() {
		return this.activityKey;
	}

	public static String getKeyForMethod(IType superClassFW, IMethod method) {
		return String.format("%s+%s", superClassFW.getElementName(),
				method.getElementName());
	}

	public static String getKeyForClass(IType superClassFW) {
		return superClassFW.getFullyQualifiedName();
	}
}
