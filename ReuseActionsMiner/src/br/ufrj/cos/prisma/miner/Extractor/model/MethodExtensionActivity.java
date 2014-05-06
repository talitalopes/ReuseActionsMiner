package br.ufrj.cos.prisma.miner.Extractor.model;

import miner.ActivityType;
import miner.MinerFactory;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class MethodExtensionActivity extends MinerActivity {

	public MethodExtensionActivity(IType superClassFW, IType type,
			IMethod method) {

		this.activityKey = MinerActivity.getKeyForMethod(superClassFW, method);
		this.activity = MinerFactory.eINSTANCE.createActivity();
		this.activity.setId(this.activityKey);
		this.activity.setType(getMethodExtensionType(method));

		String activityName = String.format("%s.%s",
				superClassFW.getFullyQualifiedName(), method.getElementName());
		this.activity.setName(activityName);
		this.activity.setAppClass(type.getElementName());
	}

	private ActivityType getMethodExtensionType(IMethod method) {
		try {
			if (method.getSource().contains(
					"super." + method.getElementName() + "()")) {
				return ActivityType.METHOD_EXTENSION;
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return ActivityType.OVERRIDES_METHOD;
	}
}
