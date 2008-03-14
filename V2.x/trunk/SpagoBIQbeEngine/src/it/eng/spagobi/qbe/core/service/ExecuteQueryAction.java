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
package it.eng.spagobi.qbe.core.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.locale.IQbeMessageHelper;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ExecuteQueryAction extends AbstractQbeEngineAction {
	
		
	public void service(SourceBean request, SourceBean response)  {				
		super.service(request, response);		
		
		
		IStatement statement = getDatamartModel().createStatement( getQuery() );
		
		String limit = getAttributeAsString("limit");
		String start = getAttributeAsString("start");
		int l = Integer.parseInt(limit);
		int s = Integer.parseInt(start);
		
		int maxResults = 14000;
		
		statement.setMaxResults(maxResults);
		statement.setParameters(getDatamartModel().getDataMartProperties());
		SourceBean queryResponseSourceBean = null;
		try {
			queryResponseSourceBean = statement.executeWithPagination(s, l, maxResults);
			//queryResponseSourceBean = statement.executeWithPagination(1, 50);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		List results = (List)queryResponseSourceBean.getAttribute("list");
		Integer resultNumber = (Integer)queryResponseSourceBean.getAttribute("resultNumber");
		Iterator it = results.iterator();
		Object o = null;
		Object[] row;
		
		JSONObject gridDataFeed = new JSONObject();
		JSONObject metadata = new JSONObject();
		JSONArray fields = null;
		JSONArray rows = new JSONArray();
		
		try {
			
			metadata.put("totalProperty", "results");
			metadata.put("root", "rows");
			metadata.put("id", "id");
			gridDataFeed.put("metaData", metadata);
			gridDataFeed.put("results", resultNumber.intValue());
			gridDataFeed.put("rows", rows);
			
			int recNo = 0;
			while (it.hasNext()){	
				o = it.next();
				
			    if (!(o instanceof Object[])){
			    	row = new Object[1];
			    	row[0] = o.toString();
			    }else{
			    	row = (Object[])o;
			    }
			    
				if(fields == null) {
					fields = new JSONArray();
					fields.put("recNo");
					// Giro le intestazioni di colonne
					Iterator fieldsIterator = getQuery().getSelectFieldsIterator();
					for (int j=0; j < row.length; j++){ 
						JSONObject field = new JSONObject();
						field.put("name", "column-" + (j+1));
						if(fieldsIterator.hasNext()) {
							ISelectField f = (ISelectField)fieldsIterator.next();
							String header = f.getFieldAlias();
							field.put("header", header);
						} else {
							field.put("header", "Column-" + (j+1));
						}
						
						field.put("dataIndex", "column-" + (j+1));
						fields.put(field);
					}					
					metadata.put("fields", fields);
				}
			    
			    // Giro le colonne
				JSONObject record= null;
				for (int j=0; j < row.length; j++){ 
					if(record == null) {
						record = new JSONObject();
						record.put("id", ++recNo);
					}
					record.put("column-" + (j+1), row[j].toString());
				}
				if(record != null) rows.put(record);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		
		try {
			httpResp.getOutputStream().print(gridDataFeed.toString());
			/*
			httpResp.getOutputStream().print("{" +
					"'metaData': {" +
					"    totalProperty: 'results'," +
					"    root: 'rows'," +
					"    id: 'id'," +
					"    fields: [" +
					"      {name: 'name', header: 'Name', dataIndex: 'name'}," +
					"      {name: 'occupation', header: 'Occupation', dataIndex: 'occupation'} ]" +
					"   }," +
					"  'results': 2, 'rows': [" +
					"    { 'id': 1, 'name': 'Bill', occupation: 'Gardener' }," +
					"    { 'id': 2, 'name': 'Ben', occupation: 'Horticulturalist' } ]" +
					"}");
					*/
			httpResp.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
