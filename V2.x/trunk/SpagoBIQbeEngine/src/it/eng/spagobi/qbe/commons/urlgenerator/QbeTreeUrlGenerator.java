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
package it.eng.spagobi.qbe.commons.urlgenerator;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.spagobi.qbe.querybuilder.select.service.AddCalculatedFieldAction;
import it.eng.spagobi.qbe.querybuilder.select.service.AddSelectFieldAction;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTreeUrlGenerator implements IQbeTreeUrlGenerator {

	private IQbeUrlGenerator urlGenerator;
	private HttpServletRequest httpRequest;
	

	public QbeTreeUrlGenerator(IQbeUrlGenerator urlGenerator, HttpServletRequest httpRequest) {
		setUrlGenerator( urlGenerator );
		setHttpRequest( httpRequest );
	}
	
	
	
	private String toLowerCase(String str) {
		String head = str.substring(0,1);
		String tail = str.substring(1, str.length());
		
		return head.toLowerCase() + tail;
	}
	
	public String getActionUrlForSelecteField(IDataMartModel datamartModel, DataMartField f) {
		
		String fieldUniqueName = f.getUniqueName();
		DataMartField field = datamartModel.getDataMartModelStructure().getField(fieldUniqueName);
		
		String filedName = "x";
		String className = "x";
		String filedLabel = "x";
		String filedType = "x";
		String filedScale = "x";
		String filedPrecision = "x";
		
		
		if(field != null) {
			filedName = "";
			
			DataMartEntity entity = field.getParent();
			if(entity.getParent() != null) {
				filedName = toLowerCase( entity.getName() );
				entity = entity.getParent();
			}
			while(entity.getParent() != null) {
				filedName = toLowerCase( entity.getName() ) + "." + filedName;
				entity = entity.getParent();
			}
			
			/*
			if(field.getParent().getPath() != null && 
					!field.getParent().getPath().equalsIgnoreCase("")) {
				filedName += field.getParent().getPath() + ".";
			}
			*/
			//filedName += field.getParent().getName() + ".";			
			if(!filedName.equalsIgnoreCase("")) filedName +=  ".";
			filedName += field.getName();
			
			
			className = field.getParent().getRoot().getType();
			filedLabel =field.getName();
			filedType = field.getType();
			filedScale = "" + field.getLength();
			filedPrecision = "" + field.getPrecision();
		}
		
		Map params = new HashMap();
		
		
		params.put("ACTION_NAME", "SELECT_FIELD_FOR_SELECT_ACTION");	
		params.put(AddSelectFieldAction.FIELD_NAME, filedName);
		params.put(AddSelectFieldAction.CLASS_NAME, className);
		params.put(AddSelectFieldAction.FIELD_LABEL, filedLabel);
		params.put(AddSelectFieldAction.FIELD_HIBTYPE, filedType);	
		params.put(AddSelectFieldAction.FIELD_HIBSCALE, filedScale);
		params.put(AddSelectFieldAction.FIELD_HIBPREC, filedPrecision);
			
		return urlGenerator.getActionUrl(httpRequest, params);
	}
	
	
	

	public String getActionUrlForSelecteField(String fieldName, String fieldLabel, String additionalParameters) {
		Map params = new HashMap();
		
		params.put("ACTION_NAME","SELECT_FIELD_FOR_SELECT_ACTION");		
		
		String className = null; // field.getParent().getRoot().getType();
		params.put(AddSelectFieldAction.CLASS_NAME, className);
		params.put(AddSelectFieldAction.FIELD_NAME, fieldName);
		params.put(AddSelectFieldAction.FIELD_LABEL, fieldLabel);	
		
		if ( additionalParameters != null){
			String[] addPars = additionalParameters.split(";");
			if (addPars.length == 3){
				params.put(AddSelectFieldAction.FIELD_HIBTYPE, addPars[0]);	
				params.put(AddSelectFieldAction.FIELD_HIBSCALE, addPars[1]);
				params.put(AddSelectFieldAction.FIELD_HIBPREC, addPars[2]);
			}
		}
		
		
		return urlGenerator.getActionUrl(httpRequest, params);
	}
	
	public String getActionUrlForCalculateField(String calculatedFieldId, String entityName, String cFieldCompleteName){
		Map params = new HashMap();
		params.put("ACTION_NAME","SELECT_CALC_FIELD_ACTION");
		
		String className = null; // field.getParent().getRoot().getType();
		params.put(AddCalculatedFieldAction.CLASS_NAME, className);
		params.put(AddCalculatedFieldAction.CFIELD_ID, calculatedFieldId);
		params.put(AddCalculatedFieldAction.CFIELD_COMPLETE_NAME, cFieldCompleteName);
		return urlGenerator.getActionUrl(httpRequest, params);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	public String getResourceUrl(String url) {
		return urlGenerator.getResourceUrl(httpRequest, url);
	}

	protected IQbeUrlGenerator getUrlGenerator() {
		return urlGenerator;
	}

	protected void setUrlGenerator(IQbeUrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}
	
	protected HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	protected void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}


}
