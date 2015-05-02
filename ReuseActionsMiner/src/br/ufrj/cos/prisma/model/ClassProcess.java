package br.ufrj.cos.prisma.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClassProcess {
	public class ClassProcessOccurrence {
		List<ClassProcess> children = new ArrayList<ClassProcess>();
		String applicationName;
		
		public ClassProcessOccurrence(String applicationName) {
			this.applicationName = applicationName;
		}
		
		public void addChild(ClassProcess child) {
			children.add(child);
		}
		
		public List<String> getListOfChildren() {
			if (children.size() == 0) {
				return new ArrayList<String>();
			}
			
			List<ClassProcess> list = new ArrayList<ClassProcess>();
			for (ClassProcess c: children) {
				list.add(c);
			}
			
			Collections.sort(list, new Comparator<ClassProcess>(){

				@Override
				public int compare(ClassProcess o1, ClassProcess o2) {
					if (o1.commitIndex < o2.commitIndex) {
						return -1;
					} else if (o1.commitIndex > o2.commitIndex) {
						return 1;
					}
					return o1.getName().compareTo(o2.getName());
				}
				
			});
			
			List<String> childrenNames = new ArrayList<String>();
			for (ClassProcess p: list) {
				childrenNames.add(p.getName());
			}
			
			return childrenNames;
		}
		
		public String getApplicationName() {
			return this.applicationName;
		}
	}
	
	List<ClassProcessOccurrence> occurrences = new ArrayList<ClassProcessOccurrence>();
	boolean defined = false;
	String name;
	String applicationName;
	int commitIndex;
	
	public ClassProcess(String name, int commitIndex) {
		this.name = name;
		this.commitIndex = commitIndex;
	}
	
	public boolean getDefined() {
		return this.defined;
	}
	
	public void setDefined(boolean value) {
		this.defined = value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<ClassProcessOccurrence> getOccurrences() {
		return this.occurrences;
	}
	
	public void addOccurrence(ClassProcessOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
}
