/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.query.dao;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.meta.querybuilder.ui.editor.SpagoBIDataSetEditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QueryDAOFileImpl {
	
	private static Logger logger = LoggerFactory.getLogger(SpagoBIDataSetEditor.class);
	
	/**
	 * 
	 * @param query the new query to save
	 * @param inputStream a stream to the previously saved query (query +  meta)
	 */
	public JSONObject getContentToSave(Query query, InputStream inputStream, IDataSource dataSource) {
		JSONObject queryJSON;
		String modelPath;
		JSONObject o = null;
		
		logger.trace("IN");
	
		try {
			//Read the file to get the Model Path value
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
				
			String line = null;
			StringBuffer stringBuffer  = new StringBuffer();
			while((line = reader.readLine())!= null) {
				stringBuffer.append(line);
			}
			inputStreamReader.close();
			reader.close();
			inputStream.close();
			String queryString = stringBuffer.toString();
			o = new JSONObject(queryString);

			JSONObject queryMeta = o.optJSONObject("queryMeta");
			modelPath = queryMeta.optString("modelPath");
		
			long UUID =System.currentTimeMillis();
			query.setId("q"+Long.valueOf(UUID).toString());
			//query.setDescription(fileEditorInput.getURI().toString());
			query.setDistinctClauseEnabled(false);
		} catch (IOException e) {		
			throw new RuntimeException("Impossible to save query, IOException", e);
		} catch (JSONException e) {
			throw new RuntimeException("Impossible to save query, JSONException", e);
		}
		
		queryJSON = null;
		try {
			queryJSON = (JSONObject)SerializerFactory.getSerializer("application/json").serialize(query, dataSource, Locale.ENGLISH);
			logger.debug(queryJSON.toString());	
		} catch (SerializationException e) {
			throw new RuntimeException("Impossible to save query", e);
		}
		
	
		BufferedWriter out = null;
		ByteArrayInputStream in = null;
		try {
			o.put("query", queryJSON);
		
			//Write model path inside query file
			JSONObject queryMeta = new JSONObject(); 
			o.put("queryMeta", queryMeta);
			queryMeta.put("modelPath",modelPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		logger.trace("OUT");
		
		return o;
	}
}
