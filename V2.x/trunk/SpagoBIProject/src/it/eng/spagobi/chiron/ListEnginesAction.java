/**
 * 
 */
package it.eng.spagobi.chiron;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.chiron.utils.AbstractBaseHttpAction;
import it.eng.spagobi.chiron.utils.JSONSuccess;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.dao.IEngineDAO;


/**
 * @author Andtra Gioia (andrea.gioia@eng.it)
 *
 */
public class ListEnginesAction extends AbstractBaseHttpAction{

	public void service(SourceBean request, SourceBean response) throws Exception {
		JSONObject results;
		JSONObject metadata;
		JSONArray fields;
		JSONObject field;
		JSONArray rows;
		
		
		
		
		IEngineDAO engineDAO = DAOFactory.getEngineDAO();
		
		List engines = engineDAO.loadAllEngines();
		
		results = new JSONObject();
		metadata = new JSONObject();
		fields = new JSONArray();
		rows = new JSONArray();
		
		metadata.put("fields", fields);
		results.put("metaData", metadata);
		results.put("rows", rows);
		
		
		
		field = new JSONObject();
		field.put("dataIndex", "label");
		field.put("name", "Label");
		fields.put(field);
		
		field = new JSONObject();
		field.put("dataIndex", "name");
		field.put("name", "Name");
		fields.put(field);
		
		field = new JSONObject();
		field.put("dataIndex", "description");
		field.put("name", "Description");
		fields.put(field);
		
		field = new JSONObject();
		field.put("dataIndex", "documentType");
		field.put("name", "DocumentType");
		fields.put(field);
		
		
		Iterator it = engines.iterator();
		while(it.hasNext()) {
			Engine engine = (Engine)it.next();
			
			JSONObject engineJSON= new JSONObject();
			engineJSON.put("id", engine.getId() );
			engineJSON.put("label", engine.getLabel() );
			engineJSON.put("name", engine.getName() );
			engineJSON.put("description", engine.getDescription() );
			engineJSON.put("documentType", engine.getBiobjTypeId() );
			
			engineJSON.put("engineType", engine.getEngineTypeId() );
			engineJSON.put("useDataSet", engine.getUseDataSet() );
			engineJSON.put("useDataSource", engine.getUseDataSource() );
			engineJSON.put("dataSource", engine.getDataSourceId() );
			engineJSON.put("class", engine.getClassName() );
			engineJSON.put("url", engine.getUrl() );
			engineJSON.put("driver", engine.getDriverName() );			
			rows.put( engineJSON );
		}
		
		try {
			writeBackToClient( new JSONSuccess(results) );
		} catch (IOException e) {
			String message = "Impossible to write back the responce to the client";
			throw new Exception(message, e);
		}
		
	}

}
