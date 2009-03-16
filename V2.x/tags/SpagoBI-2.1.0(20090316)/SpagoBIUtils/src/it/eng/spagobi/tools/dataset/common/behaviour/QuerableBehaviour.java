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

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.bo.JDBCDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.common.query.IQueryTransformer;

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
				try {
				statement = StringUtilities.substituteProfileAttributesInString(statement, getTargetDataSet().getUserProfile() );
				}
				catch (Exception e) {
					EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 9213);
					logger.error("Errore nella valorizzazione dei parametri",e);
					throw userError;}

				//check if there are parameters filled
				if( getTargetDataSet().getParamsMap() != null && !getTargetDataSet().getParamsMap().isEmpty()){
				try{
					statement = StringUtilities.substituteParametersInString(statement, getTargetDataSet().getParamsMap(), true );
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

		return statement;
	}

	public IQueryTransformer getQueryTransformer() {
		return queryTransformer;
	}

	public void setQueryTransformer(IQueryTransformer queryTransformer) {
		this.queryTransformer = queryTransformer;
	}
}
