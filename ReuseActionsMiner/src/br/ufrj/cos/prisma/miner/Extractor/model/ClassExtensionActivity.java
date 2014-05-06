package br.ufrj.cos.prisma.miner.Extractor.model;

import org.eclipse.jdt.core.IType;

import miner.ActivityType;
import miner.MinerFactory;

public class ClassExtensionActivity extends MinerActivity {

	public ClassExtensionActivity(IType superClassFW, IType type) {
		this.superClassFWType = superClassFW;
		this.classType = type;
		
		this.activityKey = MinerActivity.getKeyForClass(superClassFW);
		this.activity = MinerFactory.eINSTANCE.createActivity();
		this.activity.setId(this.activityKey);
		this.activity.setType(ActivityType.CLASS_EXTENSION);
		this.activity.setName(superClassFW.getFullyQualifiedName());
		this.activity.setAppClass(type.getElementName());
	}
}
