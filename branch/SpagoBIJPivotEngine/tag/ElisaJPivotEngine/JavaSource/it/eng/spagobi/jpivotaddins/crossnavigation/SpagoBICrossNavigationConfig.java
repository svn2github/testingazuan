/*
 * ====================================================================
 * This software is subject to the terms of the Common Public License
 * Agreement, available at the following URL:
 *   http://www.opensource.org/licenses/cpl.html .
 * Copyright (C) 2003-2004 TONBELLER AG.
 * All Rights Reserved.
 * You must accept the terms of that agreement to use this software.
 * ====================================================================
 *
 *
 */
package it.eng.spagobi.jpivotaddins.crossnavigation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mondrian.olap.Cell;
import mondrian.olap.Connection;
import mondrian.olap.Cube;
import mondrian.olap.Dimension;
import mondrian.olap.Hierarchy;
import mondrian.olap.Level;
import mondrian.olap.Member;
import mondrian.olap.Query;

import org.apache.log4j.Logger;
import org.dom4j.Node;

import com.tonbeller.jpivot.mondrian.MondrianModel;
import com.tonbeller.wcf.table.DefaultCell;

/**
 * An instance of this class contains information for cross navigation choices, retrieved by SpagoBI OLAP document template.
 * An example of this configuration could be:<br/>
 * 	&lt;CROSS_NAVIGATION&gt;<br/>
 * 	 &lt;TARGET_DOCUMENT label="QBE_DATAMART" name="Qbe Datamart" clickable="false" description="Go to Qbe Datamart"&gt;<br/>
 *    	 &lt;TARGET_CUSTOMIZED_VIEW label="Query1" name="Query1" description="Go to Query1 of Qbe Datamart" /&gt;<br/>
 *    	 &lt;PARAMETERS&gt;<br/>
 *    	   &lt;PARAMETER name="family" scope="relative" dimension="Product" hierarchy="[Product]" level="[Product].[Product Family]" /&gt;<br/>
 *    	   &lt;PARAMETER name="city" scope="relative" dimension="Region" hierarchy="[Region]" level="[Region].[Sales City]" /&gt;<br/>
 *    	 &lt;/PARAMETERS&gt;<br/>
 * 	 &lt;/TARGET_DOCUMENT&gt;<br/>
 * 	 &lt;TARGET_DOCUMENT label="INVENTORY" name="Inventory" clickable="true" description="Go to Inventory analysis"&gt;<br/>
 *    	 &lt;TARGET_CUSTOMIZED_VIEW label="Query1" name="Inventory analysis 1" description="Go to Inventory analysis 1" /&gt;<br/>
 *    	 &lt;TARGET_CUSTOMIZED_VIEW label="Query2" name="Inventory analysis 3" description="Go to Inventory analysis 2" /&gt;<br/>
 *    	 &lt;TARGET_CUSTOMIZED_VIEW label="Query3" name="Inventory analysis 2" description="Go to Inventory analysis 3" /&gt;<br/>
 *    	 &lt;PARAMETERS&gt;<br/>
 *    	   &lt;PARAMETER name="family" scope="relative" dimension="Product" hierarchy="[Product]" level="[Product].[Product Family]" /&gt;<br/>
 *    	   &lt;PARAMETER name="city" scope="relative" dimension="Region" hierarchy="[Region]" level="[Region].[Sales City]" /&gt;<br/>
 *    	 &lt;/PARAMETERS&gt;<br/>
 * 	 &lt;/TARGET_DOCUMENT&gt;<br/>
 * 	 &lt;TARGET_DOCUMENT label="CITY_DETAIL" name="City Detail" clickable="true" description="Go to City Detail"&gt;<br/>
 *    	 &lt;PARAMETERS&gt;<br/>
 *    	   &lt;PARAMETER name="outputType" scope="absolute" value="PDF" /&gt;<br/>
 *    	   &lt;PARAMETER name="city" scope="relative" dimension="Region" hierarchy="[Region]" level="[Region].[Sales City]" /&gt;<br/>
 *    	 &lt;/PARAMETERS&gt;<br/>
 * 	 &lt;/TARGET_DOCUMENT&gt;<br/>
 * 	&lt;/CROSS_NAVIGATION&gt;<br/>
 * 
 * @author Zerbetto Davide (davide.zerbetto@eng.it)
 *
 */
public class SpagoBICrossNavigationConfig {

	private static transient Logger logger = Logger.getLogger(SpagoBICrossNavigationConfig.class);
	
	private List<TargetObject> targets = null;
	
	public static final String ID = "cross_navigation_config"; 
	
	/**
	 * Constructor given the CROSS_NAVIGATION node of the xml document template.
	 * @param config: the CROSS_NAVIGATION node of the xml document template
	 */
	public SpagoBICrossNavigationConfig(Node config) {
		logger.debug("Configuration:\n" + config.asXML());
		init(config);
	}
	
	private void init(Node node){
		targets = new ArrayList<TargetObject>();
		List targetNodes = node.selectNodes("TARGET_DOCUMENT");
		if (targetNodes != null && !targetNodes.isEmpty()) {
			for (int i = 0; i < targetNodes.size(); i++) {
				TargetObject target = new TargetObject((Node) targetNodes.get(i));
				if (target != null) {
					targets.add(target);
				}
			}
		}
	}
	
	public int getChoicesNumber() {
		logger.debug("IN");
		int toReturn = 0;
		Iterator<TargetObject> it = targets.iterator();
		while (it.hasNext()) {
			TargetObject target = it.next();
			if (target.subObjects.isEmpty()) {
				toReturn += 1;
			} else {
				toReturn += 1 + target.subObjects.size();
			}
		}
		logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}

	public Object[] getChoice(int rowIndex, Cell cell, MondrianModel model) {
		logger.debug("IN");
		Object[] toReturn = new Object[3];
		TargetObject targetObject = null;
		TargetSubObject targetSubObject = null;
		Iterator<TargetObject> it = targets.iterator();
		int targetObjectCount = 0;
		while (it.hasNext()) {
			TargetObject target = it.next();
			targetObjectCount += 1;
			//if (targetObjectCount >= rowIndex + 1 && targetObjectCount < rowIndex + 1 + target.subObjects.size()) {
			if (rowIndex + 1 >= targetObjectCount && rowIndex + 1 <= targetObjectCount + target.subObjects.size()) {
				targetObject = target;
			} else {
				targetObjectCount += target.subObjects.size();
			}
			if (targetObject != null) {
				int subObjectIndex = rowIndex + 1 - targetObjectCount;
				if (subObjectIndex == 0) {
					targetSubObject = null;
				} else {
					targetSubObject = targetObject.subObjects.get(subObjectIndex - 1);
				}
				break;
			}
		}
		if (targetSubObject != null) {
			toReturn[0] = "";
			String url = getCrossNavigationUrl(targetObject, targetSubObject, cell, model);
			toReturn[1] = new DefaultCell(url, targetSubObject.name);
			toReturn[2] = targetSubObject.description;
		} else {
			if (targetObject.isClickable) {
				String url = getCrossNavigationUrl(targetObject, targetSubObject, cell, model);
				toReturn[0] = new DefaultCell(url, targetObject.name);
			} else {
				toReturn[0] = targetObject.name;
			}
			toReturn[1] = "";
			toReturn[2] = targetObject.description;
		}
		logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}
	
	private String getCrossNavigationUrl(TargetObject targetObject, TargetSubObject targetSubObject, Cell cell, MondrianModel model) {
		logger.debug("IN");
		StringBuffer buffer = new StringBuffer("javascript:alert('" + targetObject.label + "' + '");
		String query = model.getCurrentMdx();
		Connection monConnection = model.getConnection();
	    Query monQuery = monConnection.parseQuery(query);
	    Cube cube = monQuery.getCube();
	    
	    List<TargetParameter> parameters = targetObject.parameters;
	    if (!parameters.isEmpty()) {
	    	for (int i = 0; i < parameters.size(); i++) {
	    		TargetParameter aParameter = parameters.get(i);
		    	String parameterName = aParameter.name;
		    	String parameterValue = getParameterValue(aParameter, cube, cell);
		    	if (parameterValue != null) {
		    		buffer.append(parameterName + "=" + parameterValue + "&");
		    	}
	    	}
	    }
	    
    	if (buffer.charAt(buffer.length() - 1) == '&') {
    		buffer.deleteCharAt(buffer.length() - 1);
    	}
    	if (targetSubObject != null) {
    		buffer.append("' + '" + targetSubObject.label + "');");
    	} else {
    		buffer.append("');");
    	}
	    String toReturn = buffer.toString();
	    logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}

	private String getParameterValue(TargetParameter parameter, Cube cube, Cell cell) {
		if (parameter.isAbsolute) {
			return parameter.value;
		}
		String value = null;
		String dimensionName = parameter.dimension;
		String hierarchyName = parameter.hierarchy;
		String levelName = parameter.level;
		Dimension dimension = getDimension(cube, dimensionName);
		if (dimension == null) {
			logger.error("Dimension " + dimensionName + " not found in cube " + cube.getName() + "Returning null");
			return null;
		}
		Member member = cell.getContextMember(dimension);
		Hierarchy hierarchy = member.getHierarchy();
		if (hierarchy.getUniqueName().equals(hierarchyName)) {
			value = getLevelValue(member, levelName);
		}
		return value;
	}
	
	private Dimension getDimension(Cube cube, String dimensionName) {
		Dimension toReturn = null;
		Dimension[] dimensions = cube.getDimensions();
		for (int i = 0; i < dimensions.length; i++) {
			Dimension aDimension = dimensions[i];
			if (aDimension.getName().equals(dimensionName)) {
				toReturn = aDimension;
				break;
			}
		}
		return toReturn;
	}
	
	private String getLevelValue(Member member, String levelName) {
		String toReturn = null;
		Level level = member.getLevel();
		if (level.getUniqueName().equals(levelName)) {
			String uniqueName = member.getUniqueName();
			toReturn = uniqueName.substring(uniqueName.lastIndexOf("].[") + 3, uniqueName.lastIndexOf("]"));
		} else {
			// look for parent member at parent level
			Member parent = member.getParentMember();
			if (parent == null) {
				return null;
			} else {
				return getLevelValue(parent, levelName);
			}
		}
		return toReturn;
	}
	
	protected class TargetObject {
		String name;
		String label;
		String description;
		boolean isClickable;
		List<TargetSubObject> subObjects;
		List<TargetParameter> parameters;
		TargetObject(Node node) {
			name = node.valueOf("@name");
			label = node.valueOf("@label");
			description = node.valueOf("@description");
			isClickable = Boolean.parseBoolean(node.valueOf("@clickable"));
			List subobjectsNodes = node.selectNodes("TARGET_CUSTOMIZED_VIEW");
			boolean hasSubObjects = subobjectsNodes != null && !subobjectsNodes.isEmpty();
			subObjects = new ArrayList<TargetSubObject>();
			if (hasSubObjects) {
				for (int i = 0; i < subobjectsNodes.size(); i++) {
					TargetSubObject aSubObject = new TargetSubObject((Node) subobjectsNodes.get(i));
					if (aSubObject != null) {
						subObjects.add(aSubObject);
					}
				}
			}
			List parametersNodes = node.selectNodes("PARAMETERS/PARAMETER");
			boolean hasParameters = parametersNodes != null && !parametersNodes.isEmpty();
			parameters = new ArrayList<TargetParameter>();
			if (hasParameters) {
				for (int i = 0; i < parametersNodes.size(); i++) {
					TargetParameter aParameter = new TargetParameter((Node) parametersNodes.get(i));
					if (aParameter != null) {
						parameters.add(aParameter);
					}
				}
			}
		}
	}
	
	protected class TargetSubObject {
		String name;
		String label;
		String description;
		TargetSubObject(Node node) {
			name = node.valueOf("@name");
			label = node.valueOf("@label");
			description = node.valueOf("@description");
		}
	}
	
	protected class TargetParameter {
		String name;
		boolean isAbsolute;
		String value;
		String dimension;
		String hierarchy;
		String level;
		TargetParameter(Node node) {
			name = node.valueOf("@name");
			isAbsolute = node.valueOf("@scope").trim().equalsIgnoreCase("absolute");
			if (isAbsolute) {
				value = node.valueOf("@value");
			} else {
				dimension = node.valueOf("@dimension");
				hierarchy = node.valueOf("@hierarchy");
				level = node.valueOf("@level");
			}
		}
	}
	
}
