/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.export;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Davide Zerbetto
 */
public class FormViewerTemplateBuilder {
	
	public static transient Logger logger = Logger.getLogger(FormViewerTemplateBuilder.class);
	
	private JSONArray nodes = null;
	private JSONObject baseTemplate = null;
	private JSONObject queryJSON = null;
	private String datamartName = null;
	
	public FormViewerTemplateBuilder(JSONArray nodes, JSONObject queryJSON, String datamartName) {
		this.nodes = nodes;
		this.queryJSON = queryJSON;
		this.datamartName = datamartName;
		
		StringBuffer buffer = new StringBuffer();
		InputStream is = getClass().getClassLoader().getResourceAsStream("template.json");
		
		BufferedReader reader = new BufferedReader( new InputStreamReader(is) );
		String line = null;
		try {
			while( (line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
			baseTemplate = new JSONObject(buffer.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String buildTemplate() throws Exception {
		String toReturn = null;
		logger.debug("IN");
		JSONObject qbeConf = (JSONObject) baseTemplate.get("qbeConf");
		qbeConf.put("datamartName", this.datamartName);
		qbeConf.put("query", this.queryJSON);
		
		JSONArray fields = new JSONArray();
		getFields(nodes, fields);
		baseTemplate.put("fields", fields);
		
		toReturn = baseTemplate.toString(3);
		logger.debug("OUT");
		return toReturn;
	}
	
	private void getFields(JSONArray nodes, JSONArray container) throws Exception {
		for (int i = 0; i < nodes.length(); i++) {
			JSONObject node = (JSONObject) nodes.get(i);
			
			JSONArray children = (JSONArray) node.opt("children");
			
			if (children != null && children.length() > 0) {
				getFields(children, container);
			} else {
				container.put(node.get("id"));
			}
		}
	}

}
