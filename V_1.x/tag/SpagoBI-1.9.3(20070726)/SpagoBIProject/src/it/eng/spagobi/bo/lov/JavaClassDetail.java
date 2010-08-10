/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.bo.lov;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.javaClassLovs.IJavaClassLov;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Defines the <code>JavaClassDetail</code> objects. This object is used to store 
 * JavaClass Wizard detail information.
 */
public class JavaClassDetail implements ILovDetail {

	/**
	 * name of the class which return the data
	 */ 
	private String javaClassName = "";
	private List visibleColumnNames = null;
	private String valueColumnName = "";
	private String descriptionColumnName = "";
	private List invisibleColumnNames = null;
	
	
	/**
	 * constructor
	 */
	public JavaClassDetail() {}
	
	/**
	 * constructor
	 * @param dataDefinition
	 * @throws SourceBeanException
	 */
	public JavaClassDetail(String dataDefinition) throws SourceBeanException {
		loadFromXML(dataDefinition);
	}
	
	/** 
	 * loads the lov from an xml string 
	 * @param dataDefinition the xml definition of the lov
	 * @throws SourceBeanException 
	 */
	public void loadFromXML(String dataDefinition) throws SourceBeanException {
		dataDefinition.trim();
		// build the sourcebean
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		// get and set the java class name
		SourceBean javaClassNameSB = (SourceBean)source.getAttribute("JAVA_CLASS_NAME");
		String javaClassName = javaClassNameSB.getCharacters();
	    setJavaClassName(javaClassName);	
	    // get and set value column
	    String valueColumn = "";
	    SourceBean valCol = (SourceBean)source.getAttribute("VALUE-COLUMN");
		if(valCol!=null)
			valueColumn = valCol.getCharacters();
		setValueColumnName(valueColumn);
		 // get and set the description column
	    String descrColumn = "";
	    SourceBean descColSB = (SourceBean)source.getAttribute("DESCRIPTION-COLUMN");
		if(descColSB!=null)
			descrColumn = descColSB.getCharacters();
		setDescriptionColumnName(descrColumn);
		// get and set list of visible columns
		List visColNames = new ArrayList();
		SourceBean visColSB = (SourceBean)source.getAttribute("VISIBLE-COLUMNS");
		if(visColSB!=null){
			String visColConc = visColSB.getCharacters();
			if( (visColConc!=null) && !visColConc.trim().equalsIgnoreCase("") ) {
				String[] visColArr = visColConc.split(",");
				visColNames = Arrays.asList(visColArr);
			}
		}
		setVisibleColumnNames(visColNames);
		// get and set list of invisible columns
		List invisColNames = new ArrayList();
		SourceBean invisColSB = (SourceBean)source.getAttribute("INVISIBLE-COLUMNS");
		if(invisColSB!=null){
			String invisColConc = invisColSB.getCharacters();
			if( (invisColConc!=null) && !invisColConc.trim().equalsIgnoreCase("") ) {
				String[] invisColArr = invisColConc.split(",");
				invisColNames = Arrays.asList(invisColArr);
			}
		}
		setInvisibleColumnNames(invisColNames);
	}
	
	/**
	 * serialize the lov to an xml string
	 * @return the serialized xml string
	 */
	public String toXML () { 
		String XML = "<JAVACLASSLOV>" +
				     "<JAVA_CLASS_NAME>"+this.getJavaClassName()+"</JAVA_CLASS_NAME>" +
				     "<VALUE-COLUMN>"+this.getValueColumnName()+"</VALUE-COLUMN>" +
				     "<DESCRIPTION-COLUMN>"+this.getDescriptionColumnName()+"</DESCRIPTION-COLUMN>" +
				     "<VISIBLE-COLUMNS>"+GeneralUtilities.fromListToString(this.getVisibleColumnNames(), ",")+"</VISIBLE-COLUMNS>" +
				     "<INVISIBLE-COLUMNS>"+GeneralUtilities.fromListToString(this.getInvisibleColumnNames(), ",")+"</INVISIBLE-COLUMNS>" +
				     "</JAVACLASSLOV>";
		return XML;
	}
	
	/**
	 * Returns the result of the lov using a user profile to fill the lov profile attribute
	 * @param profile the profile of the user
	 * @return the string result of the lov
	 * @throws Exception
	 */
	public String getLovResult(IEngUserProfile profile) throws Exception {
		IJavaClassLov javaClassLov = createClassInstance();
		String result = javaClassLov.getValues(profile);
		result = result.trim();
		// check if the result must be converted into the right xml sintax
		boolean toconvert = checkSintax(result);
		if(toconvert) { 
			result = convertResult(result);
		}
		return result;
	}

	/**
	 * checks if the result is formatted in the right xml structure
	 * @param result the result of the lov
	 * @return true if the result is formatted correctly false otherwise
	 */
	private boolean checkSintax(String result) {
		boolean toconvert = false;
		try{
			SourceBean source = SourceBean.fromXMLString(result);
			if(!source.getName().equalsIgnoreCase("ROWS")) {
				toconvert = true;
			} else {
				List rowsList = source.getAttributeAsList(DataRow.ROW_TAG);
				if( (rowsList==null) || (rowsList.size()==0) ) {
					toconvert = true;
				}
			}
			
		} catch (Exception e) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					              "checkSintax", "the result of the java class lov is not formatted " +
					              "with the right structure so it will be wrapped inside an xml envelope");
		}
		return toconvert;
	}
	
	/**
	 * Gets the list of names of the profile attributes required
	 * @return list of profile attribute names
	 * @throws Exception
	 */
	public List getProfileAttributeNames() throws Exception {
		IJavaClassLov javaClassLov = createClassInstance();
		List attrNames = javaClassLov.getNamesOfProfileAttributeRequired();
		return attrNames;
	}

	/**
	 * Checks if the lov requires one or more profile attributes
	 * @return true if the lov require one or more profile attributes, false otherwise
	 * @throws Exception
	 */
	public boolean requireProfileAttributes() throws Exception {
		boolean requires = false;
		IJavaClassLov javaClassLov = createClassInstance();
		List attrNames = javaClassLov.getNamesOfProfileAttributeRequired();
		if(attrNames.size()!=0) {
			requires = true;
		}
		return requires;
	}
	
	/**
	 * Creates and returns an instance of the lov class
	 * @return instance of the lov class which must implement IJavaClassLov interface
	 * @throws EMFUserError
	 */
	private IJavaClassLov createClassInstance() throws EMFUserError {
		String javaClassName = getJavaClassName();
		if (javaClassName == null || javaClassName.trim().equals("")){
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
									"getLovResult", "The java class name is not specified");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1071");
		}
		IJavaClassLov javaClassLov = null;
		Class javaClass = null;
		try {
			javaClass = Class.forName(javaClassName);
		} catch (ClassNotFoundException e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
									"getLovResult", "Java class '" + javaClassName + "' not found!!");
				List pars = new ArrayList();
				pars.add(javaClassName);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1072", pars);
		}
		try {
			javaClassLov = (IJavaClassLov) javaClass.newInstance();
		} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
								    "getLovResult", "Error while instatiating Java class '" + javaClassName + "'.");
				List pars = new ArrayList();
				pars.add(javaClassName);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1073", pars);
		}
		return javaClassLov;
	}
	
	
	/**
	 * Wraps the result of the query execution into the right xml structure
	 * @param result the result of the query (which is not formatted with the right xml structure)
	 * @return the xml structure of the result 
	 */
	private String convertResult(String result) {
		StringBuffer sb = new StringBuffer();
		sb.append("<ROWS>");
		sb.append("<ROW VALUE=\"" + result +"\"/>");
		sb.append("</ROWS>");
		return sb.toString();
	}
	
	/**
	 * Gets the class name
	 * @return the complete name of the class
	 */
	public String getJavaClassName() {
		return javaClassName;
	}
	
	/**
	 * Sets the class name
	 * @param javaClassName the complete name of the class 
	 */
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}	
	
	
	/**
	 * Builds a JavaClassDetail starting from ax xml representation
	 * @param dataDefinition1 xml representation of the JavaClassDetail
	 * @return The JavaClassDetail object
	 * @throws SourceBeanException
	 */
	public static JavaClassDetail fromXML(String dataDefinition) throws SourceBeanException {
		JavaClassDetail jcd = new JavaClassDetail();
		jcd.loadFromXML(dataDefinition);
	    return jcd;
	}

	
	public String getDescriptionColumnName() {
		return descriptionColumnName;
	}

	public void setDescriptionColumnName(String descriptionColumnName) {
		this.descriptionColumnName = descriptionColumnName;
	}

	public List getInvisibleColumnNames() {
		return invisibleColumnNames;
	}

	public void setInvisibleColumnNames(List invisibleColumnNames) {
		this.invisibleColumnNames = invisibleColumnNames;
	}

	public String getValueColumnName() {
		return valueColumnName;
	}

	public void setValueColumnName(String valueColumnName) {
		this.valueColumnName = valueColumnName;
	}

	public List getVisibleColumnNames() {
		return visibleColumnNames;
	}

	public void setVisibleColumnNames(List visibleColumnNames) {
		this.visibleColumnNames = visibleColumnNames;
	}

	

	
}
