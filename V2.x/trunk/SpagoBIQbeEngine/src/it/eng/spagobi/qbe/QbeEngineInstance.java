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
package it.eng.spagobi.qbe;

import it.eng.qbe.conf.QbeTemplate;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.SourceBean;

import it.eng.spagobi.qbe.commons.constants.QbeConstants;
import it.eng.spagobi.qbe.commons.datasource.QbeDataSourceManager;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.QbeEngineAnalysisState;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.utilities.engines.AbstractEngineInstance;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.IEngineAnalysisState;

import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeEngineInstance extends AbstractEngineInstance {
	
	Map functionalities;	
	boolean standaloneMode;
	
	DataMartModel datamartModel;	
	ISingleDataMartWizardObject datamartWizard;	
	Query query;
	
	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(QbeEngineInstance.class);
	

	protected QbeEngineInstance(SourceBean template, Map env) throws QbeEngineException {
		this( new QbeTemplate(template), env );
	}
	
	protected QbeEngineInstance(QbeTemplate template, Map env) throws QbeEngineException {
		super( env );
		
		logger.debug("IN");
		
		functionalities = template.getFunctionalities();
		standaloneMode = false;		
		query = new Query();
				
		SpagoBiDataSource ds = (SpagoBiDataSource)env.get( EngineConstants.ENV_DATASOURCE );
		
		DBConnection connection = new DBConnection();			
		connection.setName( ds.getLabel() );
		connection.setDialect( ds.getHibDialectClass() );			
		connection.setJndiName( ds.getJndiName() );			
		connection.setDriverClass( ds.getDriver() );			
		connection.setPassword( ds.getPassword() );
		connection.setUrl( ds.getUrl() );
		connection.setUsername( ds.getUser() );			
		
		IDataSource dataSource = QbeDataSourceManager.getInstance().getDataSource(template.getDatamartNames(), template.getDblinkMap(),  connection);
				
		datamartModel = new DataMartModel(dataSource);
		datamartModel.setDataMartProperties( env ); 
		
		if(template.getDatamartModelAccessModality() != null) {
			datamartModel.setDataMartModelAccessModality( template.getDatamartModelAccessModality() );
		}
		datamartModel.setName(datamartModel.getDataSource().getDatamartName());
		datamartModel.setDescription(datamartModel.getDataSource().getDatamartName());
		
		datamartWizard = new SingleDataMartWizardObjectSourceBeanImpl();
				
		validate();
		
		logger.debug("OUT");
	}
	
	public void validate() throws QbeEngineException {
		return;
	}
	
	public IEngineAnalysisState getAnalysisState() {
		QbeEngineAnalysisState analysisState = null;
		analysisState= new QbeEngineAnalysisState( datamartModel );
		analysisState.setQuery( query );
		return analysisState;
	}
	
	public void setAnalysisState(IEngineAnalysisState analysisState) {	
		QbeEngineAnalysisState qbeEngineAnalysisState = null;
		
		qbeEngineAnalysisState = (QbeEngineAnalysisState)analysisState;
		setQuery( qbeEngineAnalysisState.getQuery() );
	}
	
	
	
	
	
	

	public Map getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(Map functionalities) {
		this.functionalities = functionalities;
	}

	public boolean isStandaloneMode() {
		return standaloneMode;
	}

	public void setStandaloneMode(boolean standaloneMode) {
		this.standaloneMode = standaloneMode;
	}

	public void setAnalysisState(String analysisState) {
		datamartWizard = datamartModel.getQuery(analysisState);		
	}

	public DataMartModel getDatamartModel() {
		return datamartModel;
	}

	public void setDatamartModel(DataMartModel datamartModel) {
		this.datamartModel = datamartModel;
	}

	public ISingleDataMartWizardObject getDatamartWizard() {
		return datamartWizard;
	}

	public void setDatamartWizard(ISingleDataMartWizardObject datamartWizard) {
		this.datamartWizard = datamartWizard;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
}
