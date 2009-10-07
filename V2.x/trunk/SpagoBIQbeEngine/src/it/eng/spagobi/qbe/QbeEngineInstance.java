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

import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.qbe.catalogue.QueryCatalogue;
import it.eng.qbe.conf.QbeTemplate;
import it.eng.qbe.datasource.DBConnection;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.query.Query;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.datasource.QbeDataSourceManager;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.QbeEngineAnalysisState;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.utilities.engines.AbstractEngineInstance;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.engines.IEngineAnalysisState;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeEngineInstance extends AbstractEngineInstance {
	
	Map functionalities;	
	boolean standaloneMode;	
	DataMartModel datamartModel;	
	
	
	QueryCatalogue queryCatalogue;
	

	String activeQueryId;
	
	//Query query;	
	
	
	

	// executable version of the query. cached here for performance reasons (i.e. avoid query recompilation over resultset paging)
	IStatement statment;

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
		queryCatalogue = new QueryCatalogue();
		queryCatalogue.addQuery(new Query());
		
				
		it.eng.spagobi.tools.datasource.bo.IDataSource dataSrc = (it.eng.spagobi.tools.datasource.bo.IDataSource)env.get( EngineConstants.ENV_DATASOURCE );
		SpagoBiDataSource ds = dataSrc.toSpagoBiDataSource();
		
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
	
		
		
		validate();
		
		logger.debug("OUT");
	}
	
	public void validate() throws QbeEngineException {
		return;
	}
	
	public IEngineAnalysisState getAnalysisState() {
		QbeEngineAnalysisState analysisState = null;
		analysisState= new QbeEngineAnalysisState( datamartModel );
		analysisState.setCatalogue( this.getQueryCatalogue() );
		return analysisState;
	}
	
	public void setAnalysisState(IEngineAnalysisState analysisState) {	
		QbeEngineAnalysisState qbeEngineAnalysisState = null;
		
		qbeEngineAnalysisState = (QbeEngineAnalysisState)analysisState;
		setQueryCatalogue( qbeEngineAnalysisState.getCatalogue(  ) );
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
		Query query = datamartModel.getQuery(analysisState);	
		setActiveQuery(query);
	}

	public DataMartModel getDatamartModel() {
		return datamartModel;
	}

	public void setDatamartModel(DataMartModel datamartModel) {
		this.datamartModel = datamartModel;
	}	
	
	
	public QueryCatalogue getQueryCatalogue() {
		return queryCatalogue;
	}

	public void setQueryCatalogue(QueryCatalogue queryCatalogue) {
		this.queryCatalogue = queryCatalogue;
	}
	
	private String getActiveQueryId() {
		return activeQueryId;
	}

	private void setActiveQueryId(String activeQueryId) {
		this.activeQueryId = activeQueryId;
	}
	
	public Query getActiveQuery() {
		return getQueryCatalogue().getQuery( getActiveQueryId() );
	}

	public void setActiveQuery(Query query) {
		setActiveQueryId(query.getId());
		this.statment = getDatamartModel().createStatement( query );
	}
	
	public void setActiveQuery(String queryId) {
		Query query;
		
		query = getQueryCatalogue().getQuery( queryId );
		if(query != null) {
			setActiveQueryId(query.getId());
			this.statment = getDatamartModel().createStatement( query );
		}
	}
	
	public void resetActiveQuery() {
		setActiveQueryId(null);
		setStatment(null);
	}
	
	public IStatement getStatment() {
		return statment;
	}

	public void setStatment(IStatement statment) {
		this.statment = statment;
	}
}
