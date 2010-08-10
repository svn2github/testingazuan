/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.util;

import org.apache.log4j.Logger;

import com.tonbeller.jpivot.olap.model.OlapModel;

public class FilteringUtilities {

	private static Logger logger = Logger.getLogger(FilteringUtilities.class);
	
	
	public static void setMondrianRole(OlapModel olapModel, String dimAccRulStr, String query) {
		

		/*
		ScriptableMondrianDrillThrough smdt = (ScriptableMondrianDrillThrough) olapModel.getExtension("drillThrough");
		Connection monConnection = smdt.getConnection();
		// get the connection role, cube and schema reader
	    Role connRole = monConnection.getRole().makeMutableClone();
	    logger.debug("MondrianModel::initialize:connection role retrived: " + connRole);
	    Query monQuery = monConnection.parseQuery(query);
	    Cube cube = monQuery.getCube();
	    logger.debug("MondrianModel::initialize: cube retrived: " + cube);
	    SchemaReader schemaReader = cube.getSchemaReader(null);
	    logger.debug("MondrianModel::initialize: schema reader retrived: " + schemaReader);
		
	    // clean dimension access list
	    logger.debug("MondrianModel::initialize: start setting data access using dimension list: " + dimAccList);
	    logger.debug("MondrianModel::initialize: start cleaning dimension list");
	    Iterator iterDimAcc = dimAccList.iterator();
	    List tmpDimAccList = new ArrayList();
	    while(iterDimAcc.hasNext()) {
	    	String dimAccStr = (String)iterDimAcc.next();
	    	if((dimAccStr==null) || (dimAccStr.trim().equals(""))){
	    		continue;
	    	} else {
	    		tmpDimAccList.add(dimAccStr);
	    	}
	    }
	    dimAccList = tmpDimAccList;
	    logger.debug("MondrianModel::initialize: end cleaning dimension list: " + dimAccList);
	    
	    
	    // build a List containing all the dimension name to filter
	    logger.debug("MondrianModel::initialize: start calculating access dimension names list");
	    List dimNames = new ArrayList();
	    iterDimAcc = dimAccList.iterator();
	    while(iterDimAcc.hasNext()) {
	    	String dimAccStr = (String)iterDimAcc.next();
	    	String dimName = null;
	    	if(dimAccStr.indexOf(".") == -1){
	    		continue;
	    	} else {
	    		dimName = dimAccStr.substring(0, dimAccStr.indexOf("."));
	    	}
	    	if((dimName!=null) && !dimName.trim().equals(""))
	    		if(!dimNames.contains(dimName))
	    			dimNames.add(dimName);
	    }
	    logger.debug("MondrianModel::initialize: end calculating access dimension names list: " + dimNames);
	    
	    
	    // calculate a map with couple { dimName, List of members of the dimension }
	    logger.debug("MondrianModel::initialize: start calculating memeber list for each dimension");
	    Map memberMap = new HashMap();
	    Iterator iterDimNames = dimNames.iterator();
	    while(iterDimNames.hasNext()){
	    	String dimName = (String)iterDimNames.next();
	    	dimName = dimName.substring(1, dimName.length() - 1);
	    	List dimMembers = new ArrayList();
	    	iterDimAcc = dimAccList.iterator();
		    while(iterDimAcc.hasNext()) {
		    	String dimAccStr = (String)iterDimAcc.next();
		    	if(dimAccStr.indexOf(".") == -1){
		    		continue;
		    	} else {
		    		String memberDim = dimAccStr.substring(0, dimAccStr.indexOf("."));
		    		String tmp = "[" + dimName + "]";
		    		if(tmp.equalsIgnoreCase(memberDim)){
		    			dimMembers.add(dimAccStr);
			    	}
		    	}
		    }
		    memberMap.put(dimName, dimMembers);
	    }
	    logger.debug("MondrianModel::initialize: end calculating memeber list for each dimension: " + memberMap);
	    */
	    
		
	    // FOR EACH DIMENSION NAME SET THE RIGHT GRANT TO THE DIMENSION OR HIERARCHY
		/*
		logger.debug("MondrianModel::initialize: start setting grant for each dimension or hierachy");
	    Set dimKeys = memberMap.keySet();
	    Iterator iterDimKeys = dimKeys.iterator();
	    while(iterDimKeys.hasNext()){
	    	String dimName = (String)iterDimKeys.next();
	    	logger.debug("MondrianModel::initialize: processing dimension named: " + dimName);
	    	List dimMembs = (List)memberMap.get(dimName);
	    	logger.debug("MondrianModel::initialize: members of dimension: " + dimMembs);
	    	//if(dimMembs.isEmpty()) {
	    		logger.debug("MondrianModel::initialize: try to search the dimension into the cube");
				Dimension[] dimensions = cube.getDimensions();
	    		//Set cubeDimKeys = hDimensions.keySet();
	    		// Iterator itCubeDimKeys = cubeDimKeys.iterator();
	 		    // while(itCubeDimKeys.hasNext()) {
	 		    for (int i = 0; i < dimensions.length; i++) {
	 		    	//String cubeDimKey = (String)itCubeDimKeys.next();
	 		    	Dimension dim = dimensions[i];
	 		    	String cubeDimKey = dim.getName();
	 		    	if (cubeDimKey.equalsIgnoreCase(dimName)) {
	 		    		logger.debug("MondrianModel::initialize: dimension found into the cube");
	 		    		//MondrianDimension monDim = (MondrianDimension)hDimensions.get(cubeDimKey);
	 		    		//mondrian.olap.Dimension dim = monDim.getMonDimension();
	 		    		mondrian.olap.Hierarchy[] hierarchies = dim.getHierarchies();
	 		    		if (hierarchies == null || hierarchies.length == 0) {
		 		    		connRole.grant(dim, Access.NONE);
		 		    		logger.debug("MondrianModel::initialize: setted access.none to the dimension");
		 		    		break;
	 		    		} else {
			 		    	for (int j = 0; j < hierarchies.length; j++) {
			 		    		mondrian.olap.Hierarchy aHierarchy =  hierarchies[j];
			 		    		if (aHierarchy.getName().equalsIgnoreCase(dimName)) {
			 		    			 logger.debug("MondrianModel::initialize: hierarchy found into the cube");
			 		    			 connRole.grant(aHierarchy, Access.CUSTOM, null, null);
			 		    			 logger.debug("MondrianModel::initialize: setted access.custom to the hierarchy");
			 		    		 }
			 		    	}
	 		    		}
	 		    	}

	 		     }
	 		    logger.debug("MondrianModel::initialize: end search dimension into the cube");
	    	//} else {
	    		// logger.debug("MondrianModel::initialize: try to search the hierarchy into the cube");
	    		// mondrian.olap.Hierarchy hier = cube.getHierarchy();
	    		// if (hier!= null && hier.getName().equalsIgnoreCase(dimName)) {
	    		//	 logger.debug("MondrianModel::initialize: hierarchy found into the cube");
	    		//	 connRole.grant(hier, Access.CUSTOM, null, null);
	    		//	 logger.debug("MondrianModel::initialize: setted access.custom to the hierarchy");
	    		// }
	    		 //Set hierKeys = hHierarchies.keySet();
	    		 //Iterator itHierKeys = hierKeys.iterator();
	 		     //while(itHierKeys.hasNext()) {
	 		   	 //String hierKey = (String)itHierKeys.next();
	 		     //	if(hierKey.equalsIgnoreCase(dimName)) {
	 		     //		logger.debug("MondrianModel::initialize: hierarchy found into the cube");
	 		     //		MondrianHierarchy monHier = (MondrianHierarchy)hHierarchies.get(hierKey);
	 		     //		mondrian.olap.Hierarchy hier = monHier.getMonHierarchy();
	 		     //		connRole.grant(hier, Access.CUSTOM, null, null);
	 		     //		logger.debug("MondrianModel::initialize: setted access.custom to the hierarchy");
	 		     //		break;
	 		     //	}
	    	//}
	    }
	    logger.debug("MondrianModel::initialize: end setting grant for each dimension or hierachy");
	    
	    
	    
	    // FOR EACH MEMBER SET THE GRANT
	    logger.debug("MondrianModel::initialize: start setting grant for members of dimensions");
	    dimKeys = memberMap.keySet();
	    iterDimKeys = dimKeys.iterator();
	    while(iterDimKeys.hasNext()){
	    	String dimName = (String)iterDimKeys.next();
	    	logger.debug("MondrianModel::initialize: *************** dinname = " + dimName);
	    	logger.debug("MondrianModel::initialize: start processing dimension named: " + dimName);
	    	List dimMembs = (List)memberMap.get(dimName);
	    	logger.debug("MondrianModel::initialize: using member list: " + dimMembs);
	    	Iterator iterDimMembs = dimMembs.iterator();
	        while(iterDimMembs.hasNext()) {
	        	String dimMemb = (String)iterDimMembs.next();
	        	logger.debug("MondrianModel::initialize: processing member : " + dimMemb);
	        	String[] membParts = Util.explode(dimMemb);
	    	    mondrian.olap.Member member = schemaReader.getMemberByUniqueName(membParts,true);
	    	    logger.debug("MondrianModel::initialize: mondrian member object retrived: " + member);
	    	    connRole.grant(member, Access.ALL);
	    	    logger.debug("MondrianModel::initialize: setted access.all to the member");
	        }
	    }
	    logger.debug("MondrianModel::initialize: end setting grant for members of dimensions");
	        
	    
	    // SET THE ROLE INTO CONNECTION
	    connRole.makeImmutable();
	    monConnection.setRole(connRole); 
	    logger.debug("MondrianModel::initialize: setted role with grants into connection");
	    logger.debug("MondrianModel::initialize: end setting data access");
	  */ 
	}
	
	
	
	
	/*
    if (dimAccRulStr != null) {
    	if(dimAccRulStr.trim().equalsIgnoreCase("")) {
    		session.setAttribute("dimension_access_rules", new ArrayList());
    	} else {
    		String[] dimAccArray = dimAccRulStr.split(",");
    		List dimAccList = Arrays.asList(dimAccArray);
    		session.setAttribute("dimension_access_rules", dimAccList);
    	}
    }
    */
	
}
