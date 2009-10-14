/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.crossnavigation;

import java.util.ArrayList;
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

/**
 * An instance of this class contains information for cross navigation choices, retrieved by SpagoBI OLAP document template.
 * An example of this configuration could be:<br/>
 * 	&lt;CROSS_NAVIGATION&gt;<br/>
 * 	 &lt;TARGET documentLabel="QBE_FOODMART" customizedView="Unit sales on product family"&gt;<br/>
 *     &lt;TITLE&gt;<br/>
 * 	   &lt;![CDATA[<br/>
 *         Go to QbE over Foodmart DB<br/>
 *        ]]&gt;<br/>
 *     &lt;/TITLE&gt;<br/>
 *     &lt;DESCRIPTION&gt;<br/>
 * 	   &lt;![CDATA[<br/>
 *        Detail on unit sales per selected product family<br/>
 *        ]]&gt;<br/>
 *     &lt;/DESCRIPTION&gt;<br/>
 * 	  &lt;PARAMETERS&gt;<br/>
 * 	   &lt;PARAMETER name="family" scope="relative" dimension="Product" hierarchy="[Product]" level="[Product].[Product Family]" /&gt;<br/>
 *      &lt;PARAMETER name="city" scope="relative" dimension="Region" hierarchy="[Region]" level="[Region].[Sales City]" /&gt;<br/>
 * 	  &lt;/PARAMETERS&gt;<br/>
 * 	 &lt;/TARGET&gt;<br/>
 * 	&lt;/CROSS_NAVIGATION&gt;<br/>
 * 
 * @author Zerbetto Davide (davide.zerbetto@eng.it)
 *
 */
public class SpagoBICrossNavigationConfig {

	private static transient Logger logger = Logger.getLogger(SpagoBICrossNavigationConfig.class);
	
	private List<Target> targets = null;
	
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
		targets = new ArrayList<Target>();
		List targetNodes = node.selectNodes("TARGET");
		if (targetNodes != null && !targetNodes.isEmpty()) {
			for (int i = 0; i < targetNodes.size(); i++) {
				Target target = new Target((Node) targetNodes.get(i));
				if (target != null) {
					targets.add(target);
				}
			}
		}
	}
	
	public int getChoicesNumber() {
		logger.debug("IN");
		int toReturn = targets.size();
		logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}

	public String[] getChoice(int rowIndex, Cell cell, MondrianModel model) {
		logger.debug("IN");
		String[] toReturn = new String[2];
		Target target = targets.get(rowIndex);
		String url = getCrossNavigationUrl(target, cell, model);
		toReturn[0] = target.title;
		toReturn[1] = url;
		logger.debug("OUT: returning [" + toReturn + "]");
		return toReturn;
	}
	
	private String getCrossNavigationUrl(Target target, Cell cell, MondrianModel model) {
		logger.debug("IN");
		StringBuffer buffer = new StringBuffer("parent.execCrossNavigation(this.name, '" + target.documentLabel + "', '");
		String query = model.getCurrentMdx();
		Connection monConnection = model.getConnection();
	    Query monQuery = monConnection.parseQuery(query);
	    Cube cube = monQuery.getCube();
	    
	    List<TargetParameter> parameters = target.parameters;
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
    	if (target.customizedView != null) {
    		buffer.append("', '" + target.customizedView + "');");
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
	
	protected class Target {
		String documentLabel;
		String customizedView;
		String title;
		String description;
		List<TargetParameter> parameters;
		Target(Node node) {
			documentLabel = node.valueOf("@documentLabel");
			customizedView = node.valueOf("@customizedView");
			if (customizedView != null && customizedView.trim().equals("")) {
				customizedView = null;
			}
			title = node.selectSingleNode("TITLE").getText();
			description = node.selectSingleNode("DESCRIPTION").getText();
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
