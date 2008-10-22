/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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

package it.eng.spagobi.engines.chart.utils;



import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.DataSetParameterItem;
import it.eng.spagobi.tools.dataset.bo.DataSetParametersList;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.dao.IDataSetDAO;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/** Internal Engine
 *  * @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */

public class DataSetAccessFunctions {

	public static final String FILE="0";
	public static final String QUERY="1";
	public static final String WEBSERVER="2";


	private static transient Logger logger=Logger.getLogger(DataSetAccessFunctions.class);


	/**
	 * returns the result of a LOV
	 * <p>
	 * it is used both to get the value of the chart and to get its configuration parameters if there defined.
	 * 
	 * @param profile IEngUserProfile of the user
	 * @param dsId the ds id
	 * @param parameters the parameters
	 * 
	 * @return the data set result from id
	 * 
	 * @throws EMFUserError 	 * @throws NumberFormatException 	 * @throws Exception the exception
	 */



	public static String getDataSetResultFromId(IEngUserProfile profile,String dsId, Map parameters) throws Exception {
		
		IDataSetDAO dsDAO = DAOFactory.getDataSetDAO();
		DataSetConfig ds = dsDAO.loadDataSetByID(Integer.valueOf(dsId));
	
		String result=DataSetAccessFunctions.getDataSetResult(profile, ds, parameters);
		return result;
	}

	

	/**
	 * Gets the data set result from label.
	 * 
	 * @param profile the profile
	 * @param label the label
	 * @param parameters the parameters
	 * 
	 * @return the data set result from label
	 * 
	 * @throws Exception the exception
	 */
	public static String getDataSetResultFromLabel(IEngUserProfile profile,String label, Map parameters) throws Exception {
		
		IDataSetDAO dsDAO = DAOFactory.getDataSetDAO();
		DataSetConfig ds = dsDAO.loadDataSetByLabel(label);
	
		String result=DataSetAccessFunctions.getDataSetResult(profile, ds, parameters);
		return result;
		
	}
	
	
	
	/**
	 * Gets the data set result.
	 * 
	 * @param profile the profile
	 * @param ds the ds
	 * @param parameters the parameters
	 * 
	 * @return the data set result
	 * 
	 * @throws Exception the exception
	 */
	public static String getDataSetResult(IEngUserProfile profile,DataSetConfig ds, Map parameters) throws Exception {
		logger.debug("IN");


		if (profile == null) {
			profile = new UserProfile("anonymous");
		}


		String dataPars = ds.getParameters();

		if(dataPars!=null && dataPars!=""){
			String type="";

			if(ds instanceof QueryDataSet){
				type="1";
				// resolve parameters presence
				HashMap usedPars=resolveParameters(type,dataPars, parameters);
				String query=((QueryDataSet)ds).getQuery();
				query=GeneralUtilities.substituteParametersInString(query, usedPars);
				((QueryDataSet)ds).setQuery(query);
				
			}
			else
				if(ds instanceof WSDataSet){
					type="2";
				}
				else
					if(ds instanceof FileDataSet){
						type="0";	
					}


		}
		String toReturn = ds.getDataSetResult(profile);
		logger.debug("OUT" + toReturn);
		return toReturn;
	}


	/**
	 * Takes the type of DataSet, DataSet parameter definition, the documents parameter definition
	 * the aim is returning a map (name,value) of the used parameters, where value is ready for substitution
	 */
	
	private static HashMap resolveParameters(String type,String parametersXML, Map parameters)throws Exception{
		logger.debug("IN");
		DataSetParametersList dsList=new DataSetParametersList(parametersXML);

		HashMap usedParameters=null;
		// if in query case
		if(type.equals("1")){

			usedParameters=new HashMap();
			// if in query case I must do conversion of parameter value conforming to their type
			for (Iterator iterator = dsList.getItems().iterator(); iterator.hasNext();) {
				DataSetParameterItem item= (DataSetParameterItem) iterator.next();
				String name=item.getName();
				String typePar=item.getType();

				String value=(String)parameters.get(name);
				boolean singleValue=true;
				if(value==null){
					throw new Exception();
				}
				else{
					if(typePar.equalsIgnoreCase("String")&& !value.startsWith("'")){
						value="'"+value+"'";
				
					}
					else if(typePar.equalsIgnoreCase("Number")){
						try {
							 StringTokenizer st = new StringTokenizer(value);
							 
							 String numTemp = "";
							 Double doubleValue = null;
							 value = "";
							 while(st.hasMoreTokens()){
								 
								 numTemp = st.nextToken("'");
								 if(numTemp.equals(",")){
									 continue;
								 }
								 doubleValue=new Double(Double.parseDouble(numTemp));
								 value = value + doubleValue.toString();
								 
								 if(st.hasMoreTokens()){
									 value = value + ",";
								 }
							 }
							  
			    		      } catch (NumberFormatException e) {
			    		    	  throw new Exception();
			    		      }
					}
					else if(typePar.equalsIgnoreCase("Date")){
						value=value;
					}
					usedParameters.put(name, value);
				}
			}
				
				}
			
		logger.debug("OUT");
		return usedParameters;
	}


}