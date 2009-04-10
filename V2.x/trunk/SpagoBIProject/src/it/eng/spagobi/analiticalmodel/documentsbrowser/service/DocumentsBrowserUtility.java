/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.documentsbrowser.service;


import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class DocumentsBrowserUtility {
	
	// logger component
	private static Logger logger = Logger.getLogger(DocumentsBrowserUtility.class);
	
	
	
	public JSONObject defineConfiguration(SourceBean response) throws Exception{
		try{
			ConfigSingleton config = ConfigSingleton.getInstance();
			logger.debug ("SpagoBI Document Browser configuration retrived ");
			
			//create json object
			JSONObject jsonObj = new JSONObject();
			JSONArray jsonFolder = new JSONArray();
			
			//getting folder configurations
			SourceBean sbFolder = (SourceBean)config.getAttribute("DOCUMENT_BROWSER.FOLDER");
			boolean folderVisibility = true;
			if (sbFolder == null)
	    		logger.info("Configuration about folder not found! Visible is the default");
			else
				folderVisibility = Boolean.valueOf((String)sbFolder.getAttribute("visible"));
			
			JSONObject attrF = new JSONObject();
			attrF.put("visible", folderVisibility);
			jsonFolder.put(attrF);
			jsonObj.put("metaFolder", jsonFolder);
			
			//getting document configuration
			SourceBean sbDocs = (SourceBean)config.getAttribute("DOCUMENT_BROWSER.DOCUMENT");
			List fieldList = sbDocs.getAttributeAsList("FIELD");
			
			//loops on documents
			JSONArray jsonDocs = new JSONArray();
			for(int i = 0; i < fieldList.size(); i++) {
				SourceBean fieldSB = (SourceBean)fieldList.get(i);
				JSONObject attrD = new JSONObject();
				attrD.put("id", (String)fieldSB.getAttribute("id"));
				attrD.put("visible", (String)fieldSB.getAttribute("visible"));
				attrD.put("sortable", (String)fieldSB.getAttribute("sortable"));
				attrD.put("groupable", (String)fieldSB.getAttribute("groupable"));
				attrD.put("searchable", (String)fieldSB.getAttribute("searchable"));
				jsonDocs.put(attrD);
			}
			jsonObj.put("metaDocument", jsonDocs);
			
			logger.debug("OUT");
			
			return jsonObj;
			
		}catch (Exception e){
			logger.error ("Error while initializing the document browser . " , e);
			throw e;
		}
		
	}
}
