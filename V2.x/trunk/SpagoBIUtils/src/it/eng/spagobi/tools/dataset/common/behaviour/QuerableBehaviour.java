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
package it.eng.spagobi.tools.dataset.common.behaviour;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.tools.dataset.bo.DataSetParameterItem;
import it.eng.spagobi.tools.dataset.bo.DataSetParametersList;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.bo.JDBCDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.common.query.IQueryTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QuerableBehaviour extends AbstractDataSetBehaviour {

	IQueryTransformer queryTransformer;
	private static transient Logger logger = Logger.getLogger(QuerableBehaviour.class);

	public QuerableBehaviour(IDataSet targetDataSet) {
		super(QuerableBehaviour.class.getName(), targetDataSet);
	}

	public String getStatement() throws EMFInternalError, EMFUserError{
		logger.debug("IN");
		String statement;

		IDataSet dataSet=getTargetDataSet();
		if (dataSet instanceof ScriptDataSet) {
			statement = (String) ((ScriptDataSet)dataSet).getScript();
		}
		else
			if (dataSet instanceof JDBCDataSet) {
				statement = (String) ((JDBCDataSet)dataSet).getQuery();
			}
			else 
				// maybe better to delete getQuery from IDataSet
				statement = (String)dataSet.getQuery();

		if(statement != null) {
			// if script substitute profile attributes in a strict way
			if (dataSet instanceof ScriptDataSet) {
				try{
					HashMap attributes = getAllProfileAttributes(getTargetDataSet().getUserProfile()); // to be cancelled, now substitutution inline
					statement=substituteProfileAttributes(statement, attributes);
				}
				catch (EMFInternalError e) {
					logger.error("Errore nella valorizzazione degli attributi i profilo del dataset Script",e);
					throw(e);
				}


			}			
			else if (dataSet instanceof JDBCDataSet) {

				try {
					statement = StringUtilities.substituteProfileAttributesInString(statement, getTargetDataSet().getUserProfile() );
				}
				catch (Exception e) {
					EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 9213);
					logger.error("Errore nella valorizzazione degli attributi di profilodel dataset JDBC",e);
					throw userError;}
			}


			//check if there are parameters filled
			if( getTargetDataSet().getParamsMap() != null && !getTargetDataSet().getParamsMap().isEmpty()){

				try{
					Map parTypeMap=getParTypeMap(getTargetDataSet());
					statement = StringUtilities.substituteParametersInString(statement, getTargetDataSet().getParamsMap(), parTypeMap ,false );
				}
				catch (Exception e) {
					EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 9220);
					logger.error("Errore nella valorizzazione dei parametri",e);
					throw userError;
				}

			}	

			if(queryTransformer != null) {
				statement = (String)queryTransformer.transformQuery( statement );
			}
		}
		logger.debug("OUT");
		return statement;
	}


	private String substituteProfileAttributes(String script, HashMap attributes) throws EMFInternalError{
		logger.debug("IN");
		String cleanScript=new String(script);
		int indexSubstitution=0;
		int profileAttributeStartIndex = script.indexOf("${",indexSubstitution);

		while (profileAttributeStartIndex != -1) {
			int profileAttributeEndIndex=script.indexOf("}",profileAttributeStartIndex);
			String attributeName = script.substring(profileAttributeStartIndex + 2, profileAttributeEndIndex).trim();
			Object attributeValueObj = attributes.get(attributeName);
			if(attributeValueObj==null)
			{
				logger.error("Profile attribute "+attributeName+" not found");
				attributeValueObj="undefined";
			}
			cleanScript=cleanScript.replaceAll("\\$\\{"+attributeName+"\\}", attributeValueObj.toString());
			indexSubstitution=profileAttributeEndIndex;
			profileAttributeStartIndex = script.indexOf("${",indexSubstitution);
		}
		logger.debug("OUT");
		return cleanScript;	
	}


	public Map getParTypeMap(IDataSet dataSet) throws SourceBeanException{
		logger.debug("IN");
		// recover parameters from dataset and their type
		Map parTypeMap=new HashMap();
		String parametersXML=dataSet.getParameters();	
		List parameters = new ArrayList();
		if (parametersXML != null  &&  !parametersXML.equals("")){
			//lovProvider = GeneralUtilities.substituteQuotesIntoString(lovProvider);
			parameters = DataSetParametersList.fromXML(parametersXML).getItems();
		}	
		for (int i = 0; i < parameters.size(); i++) {
			DataSetParameterItem dsDet = (DataSetParameterItem) parameters.get(i); 
			String name = dsDet.getName();
			String type = dsDet.getType();
			parTypeMap.put(name, type);
		}
		logger.debug("OUT");
		return parTypeMap;

	}

	/**
	 * Gets the all profile attributes. (Also present in GeneralUtilities) TODO: centralization of two methods
	 * 
	 * @param profile the profile
	 * 
	 * @return the all profile attributes
	 * 
	 * @throws EMFInternalError the EMF internal error
	 */
	public static HashMap getAllProfileAttributes(IEngUserProfile profile) throws EMFInternalError {
		logger.debug("IN");
		if (profile == null)
			throw new EMFInternalError(EMFErrorSeverity.ERROR,
			"getAllProfileAttributes method invoked with null input profile object");
		HashMap profileattrs = new HashMap();
		Collection profileattrsNames = profile.getUserAttributeNames();
		if (profileattrsNames == null || profileattrsNames.size() == 0)
			return profileattrs;
		Iterator it = profileattrsNames.iterator();
		while (it.hasNext()) {
			Object profileattrName = it.next();
			Object profileattrValue = profile.getUserAttribute(profileattrName.toString());
			profileattrs.put(profileattrName, profileattrValue);
		}
		logger.debug("OUT");
		return profileattrs;
	}

	public IQueryTransformer getQueryTransformer() {
		return queryTransformer;
	}

	public void setQueryTransformer(IQueryTransformer queryTransformer) {
		this.queryTransformer = queryTransformer;
	}
}
