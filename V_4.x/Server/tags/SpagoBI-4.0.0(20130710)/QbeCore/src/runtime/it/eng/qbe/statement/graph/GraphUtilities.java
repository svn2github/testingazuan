/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GraphUtilities {
	
	/**
	 * Removes the subpaths
	 * @param ambiguousModelField
	 * @param sort
	 */
	public static void cleanSubPaths( Set<ModelFieldPaths> ambiguousModelField, boolean sort){
		StringComparator stringComparator = new StringComparator();
		PathChoiceComparator pathChoiceComparator = new PathChoiceComparator();
		
		if(ambiguousModelField!=null){
			Iterator<ModelFieldPaths> iter = ambiguousModelField.iterator();
			while (iter.hasNext()) {
				List<String> listRelations = new ArrayList<String>();
				ModelFieldPaths modelFieldPaths = (ModelFieldPaths) iter.next();
				Set<PathChoice> pathChoices = modelFieldPaths.getChoices();
				
				if(pathChoices!=null){
					
					Set<PathChoice> pathChoicesFiltered;
					if(sort){
						pathChoicesFiltered= new TreeSet(pathChoiceComparator);	
					}else{
						pathChoicesFiltered= new HashSet<PathChoice>();
					}
					Iterator<PathChoice> pathChoicesIter = pathChoices.iterator();
					
					while (pathChoicesIter.hasNext()) {
						PathChoice pathChoice2 = (PathChoice) pathChoicesIter.next();
						String relation = pathChoice2.getRelations().toString();
						listRelations.add(relation);
					}
					
					Collections.sort(listRelations, stringComparator);
					
					
					pathChoicesIter = pathChoices.iterator();
					while (pathChoicesIter.hasNext()) {
						PathChoice pathChoice2 = (PathChoice) pathChoicesIter.next();
						String relation = pathChoice2.getRelations().toString();
						if(!isSubPath(listRelations, relation)){
							pathChoicesFiltered.add(pathChoice2);
						}
					}
	
					modelFieldPaths.setChoices(pathChoicesFiltered);
				}
				
			}
		}
	}
	
	private static boolean isSubPath(List<String> listRelations, String relation){
		relation = relation.substring(1, relation.length()-1);
		for(int i=listRelations.size()-1; i>0; i--){
			String aRelation = (listRelations.get(i));
			aRelation = aRelation.substring(1, aRelation.length()-1);
			
			if(aRelation.length()>relation.length() && aRelation.indexOf(relation)>=0){
				return true;
			}
		}
		return false;
	}
	
	
	private static class StringComparator implements Comparator<String>{

		public int compare(String arg0, String arg1) {
			if(arg0==null){
				return -1;
			}
			if(arg1==null){
				return 1;
			}
			if(arg0.length()!=arg1.length()){
				return arg0.length()-arg1.length();
			}
			return arg0.compareTo(arg1);
		}
		
	}
	
	
	private static class PathChoiceComparator implements Comparator<PathChoice>{

		public int compare(PathChoice arg1, PathChoice arg0) {
			if(arg0==null){
				return -1;
			}
			if(arg1==null){
				return 1;
			}
			String arg0rel = arg0.getRelations().toString();
			String arg1rel = arg1.getRelations().toString();
			if(arg0rel.length()!=arg1rel.length()){
				return arg0rel.length()-arg1rel.length();
			}
			return arg0rel.compareTo(arg1rel);
		}

		
		
	}
	
}
