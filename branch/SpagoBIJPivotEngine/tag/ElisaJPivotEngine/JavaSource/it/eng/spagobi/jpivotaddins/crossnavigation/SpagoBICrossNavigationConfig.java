/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package it.eng.spagobi.jpivotaddins.crossnavigation;

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

public class SpagoBICrossNavigationConfig {

	private transient Logger logger = Logger.getLogger(this.getClass());
	
	private Node config = null;
	
	public SpagoBICrossNavigationConfig(Node config) {
		this.config = config;
	}
	
	public String getTargetDocument() {
		logger.debug("IN");
		String targetDocumentLabel = config.selectSingleNode("TARGET_DOCUMENT").valueOf("@label").trim();
		logger.debug("OUT: returning [" + targetDocumentLabel + "]");		
		return targetDocumentLabel;
	}
	
	public int getChoicesNumber() {
		logger.debug("IN");
		int toReturn = 0;
		List customizedViews = config.selectNodes("TARGET_DOCUMENT/TARGET_CUSTOMIZED_VIEW");
		if (customizedViews != null && !customizedViews.isEmpty()) {
			toReturn = customizedViews.size();
		} else {
			logger.debug("No customized views found on cross navigation configuration");
			toReturn = 1;
		}
		logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}

	public Object[] getChoice(int rowIndex, Cell cell, MondrianModel model) {
		logger.debug("IN");
		Object[] toReturn = new Object[2];
		toReturn[0] = getTargetDocument();
		List customizedViews = config.selectNodes("TARGET_DOCUMENT/TARGET_CUSTOMIZED_VIEW");
		if (customizedViews != null && !customizedViews.isEmpty()) {
			Node customizedView = (Node) customizedViews.get(rowIndex);
			String label = customizedView.valueOf("@label").trim();
			String url = getCrossNavigationUrl(label, cell, model);
			toReturn[1] = new DefaultCell(url, label);
		} else {
			logger.debug("No customized views found on cross navigation configuration");
			toReturn[1] = "";
		}
		logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}
	
	private String getCrossNavigationUrl(String subObjectLabel, Cell cell, MondrianModel model) {
		logger.debug("IN");
		StringBuffer buffer = new StringBuffer("javascript:alert('" + getTargetDocument() + "' + '");
		String query = model.getCurrentMdx();
		Connection monConnection = model.getConnection();
	    Query monQuery = monConnection.parseQuery(query);
	    Cube cube = monQuery.getCube();
	    
	    List parametersNodes = config.selectNodes("PARAMETERS/PARAMETER");
	    if (parametersNodes != null && !parametersNodes.isEmpty()) {
	    	for (int i = 0; i < parametersNodes.size(); i++) {
		    	Node aParameterNode = (Node) parametersNodes.get(i);
		    	String parameterName = aParameterNode.valueOf("@name").trim();
		    	String parameterValue = getParameterValue(aParameterNode, cube, cell);
		    	if (parameterValue != null) {
		    		buffer.append(parameterName + "=" + parameterValue + "&");
		    	}
	    	}
	    }
	    
    	if (buffer.charAt(buffer.length() - 1) == '&') {
    		buffer.deleteCharAt(buffer.length() - 1);
    	}
    	
	    buffer.append("' + '" + subObjectLabel + "');");
	    String toReturn = buffer.toString();
	    logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}

	private String getParameterValue(Node parameterNode, Cube cube, Cell cell) {
		String scope = parameterNode.valueOf("@scope").trim();
		if (scope != null && scope.equalsIgnoreCase("absolute")) {
			return parameterNode.valueOf("@value");
		}
		String value = null;
		String dimensionName = parameterNode.valueOf("@dimension");
		String hierarchyName = parameterNode.valueOf("@hierarchy");
		String levelName = parameterNode.valueOf("@level");
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
	
}
