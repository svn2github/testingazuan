/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.utilities.DataSourceUtilities;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.Field;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IField;
import it.eng.spagobi.tools.dataset.common.datastore.IFieldMeta;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;
import it.eng.spagobi.tools.dataset.service.DetailDataSetModule;
import it.eng.spagobi.tools.datasource.bo.DataSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class SQLResultSetReader implements IDataReader {

	private static transient Logger logger = Logger.getLogger(SQLResultSetReader.class);
    IEngUserProfile profile=null;
    QueryDataSet ds=null;

    
    public IEngUserProfile getProfile() {
		return profile;
	}

	public void setProfile(IEngUserProfile profile) {
		this.profile = profile;
	}

	public QueryDataSet getDs() {
		return ds;
	}


    public SQLResultSetReader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IDataStore read(HashMap parameters) {
		
		logger.debug("IN");
		IDataStore ids = (IDataStore)new DataStoreImpl();
		DataSource dataSource=ds.getDataSource();
		String datasource=null;
		try{
			datasource = dataSource.getLabel();

			String query = ds.getQuery();
		
			query = GeneralUtilities.substituteProfileAttributesInString(query, profile);
			
			//check if there are parameters filled
		
			if(parameters!=null && !parameters.isEmpty()){
				query = GeneralUtilities.substituteParametersInString(query, parameters);	
			}
			
			SourceBean rowsSourceBean = null;
			List colNames = new ArrayList();
			
			rowsSourceBean = (SourceBean) executeSelect(datasource, query, colNames);

				//I must get columnNames. assumo che tutte le righe abbiano le stesse colonne
				if(rowsSourceBean!=null){
					List row =rowsSourceBean.getAttributeAsList("ROW");
					if(row.size()>=1){
						Iterator iterator = row.iterator(); 
						
						while(iterator.hasNext()){
												
						SourceBean sb = (SourceBean) iterator.next();
						//For each row I instanciate an IRecord
						IRecord r = (IRecord)new Record();
						
						List sbas=sb.getContainedAttributes();
						for (Iterator iterator2 = sbas.iterator(); iterator2.hasNext();) {
							
							SourceBeanAttribute object = (SourceBeanAttribute) iterator2.next();
							String fieldName=object.getKey();
							IFieldMeta fMeta = (IFieldMeta)new FieldMetadata();
							fMeta.setName(fieldName);
							//Each Record is made out of different IFields with a value and metadata
							IField f = (IField)new Field(fMeta,object);
							r.appendField(f);
						}
						ids.appendRow(r);
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.debug("Exception during Query result reading");
			}
			logger.debug("OUT");
	return ids;
    }

	public static Object executeSelect(String datasource, String statement, List columnsNames) throws EMFInternalError {
		
		logger.debug("IN");
		Object result = null;

		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		try {
			DataSourceUtilities dsUtil = new DataSourceUtilities();
			Connection conn = dsUtil.getConnection(datasource); 
			dataConnection = dsUtil.getDataConnection(conn);
	
			sqlCommand = dataConnection.createSelectCommand(statement);
			dataResult = sqlCommand.execute();
			ScrollableDataResult scrollableDataResult = (ScrollableDataResult) dataResult
			.getDataObject();
			List temp = Arrays.asList(scrollableDataResult.getColumnNames());
			columnsNames.addAll(temp);
			result = scrollableDataResult.getSourceBean();
		} finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		}
		logger.debug("OUT");
		return result;
		
	}

	public void setDataSetConfig(DataSetConfig ds) {
		this.ds =(QueryDataSet) ds;	
	}





}
