package br.ufrj.cos.prisma.miner.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

public class CustomSearchRequestor extends SearchRequestor {
	
	private static CustomSearchRequestor instance = new CustomSearchRequestor();
	private SearchMatch mMatch;
	
	private CustomSearchRequestor() {}
	
	public static CustomSearchRequestor getInstance() {
		return instance;
	}
	
    @Override
    public void acceptSearchMatch(SearchMatch match)
            throws CoreException {
    	mMatch = match;
//        System.out.println("Resource: " + match.getResource());
//        System.out.println("Element: " + match.getElement());
    }
    
    public SearchMatch getMatch() {
    	return mMatch;
    }

}
