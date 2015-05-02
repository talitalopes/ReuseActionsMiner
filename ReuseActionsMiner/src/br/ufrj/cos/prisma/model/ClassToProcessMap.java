package br.ufrj.cos.prisma.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrj.cos.prisma.model.ClassProcess.ClassProcessOccurrence;
import br.ufrj.cos.prisma.model.ReuseMinerApplicationTree.CustomNode;

public class ClassToProcessMap {
	Map<String, ClassProcess> map = new HashMap<String, ClassProcess>();

	public void Visit(CustomNode node, ReuseMinerApplicationTree tree) {
		if (node == null) return;
		List<CustomNode> children = node.getSortedChildren(tree);
		
		// Visit children
		for (CustomNode child: children) {
			Visit(child, tree);
		}
		
		ClassProcess p = getProcess(node);
		if (p == null) return;
		
		// Insert process occurrence
		ClassProcessOccurrence occurrence = p.new ClassProcessOccurrence(tree.applicationName); 
		for (CustomNode child: children) {
			ClassProcess childProcess = getProcess(child);
			if (childProcess == null) continue;
			
			occurrence.addChild(childProcess);
		}
		p.addOccurrence(occurrence);
	}
	
	public Map<String, ClassProcess> getMap() {
		return this.map;
	}
	
	private ClassProcess getProcess(CustomNode node) {
		String className = node.getActivityName();
		if (className == null) {
			className = node.getEventId();
		}
		
		if (map.containsKey(className))
			return map.get(className);
		
		ClassProcess p = new ClassProcess(className, node.commitIndex);
		map.put(className, p);
		return p;
	}
}
