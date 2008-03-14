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

import javax.servlet.http.HttpServletResponse;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.locale.IQbeMessageHelper;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.query.IOrderByField;
import it.eng.qbe.query.IQuery;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.IWhereField;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class RefreshQueryAction extends AbstractQbeEngineAction {
	
		
	public void service(SourceBean request, SourceBean response)  {				
		super.service(request, response);		
		
		String recordsSTR = getAttributeAsString("records");
		String filtersSTR = getAttributeAsString("filters");
		
		if(getQuery().isEmpty() == false) {
			setDatamartWizard( new SingleDataMartWizardObjectSourceBeanImpl() );
		}
		
		try {
			JSONArray recordsJOSN = new JSONArray(recordsSTR);
			for(int i = 0; i < recordsJOSN.length(); i++) {
				JSONObject recordJSON = recordsJOSN.getJSONObject(i);
				String fieldUniqueName = recordJSON.getString("id");
				String alias = recordJSON.getString("alias");
				String group = recordJSON.getString("group");
				String order = recordJSON.getString("order");
				String funct = recordJSON.getString("funct");
								
				if(fieldUniqueName != null) {
					// add field
					DataMartField field = getDatamartModel().getDataMartModelStructure().getField(fieldUniqueName);
					
					String fieldName = field.getQueryName();			
					String className = field.getParent().getRoot().getType();
					String fieldLabel = field.getName();
					String fieldHibType = field.getType();
					String fieldHibScale = "" + field.getLength();
					String fieldHibPrec = "" + field.getPrecision();
					
					ISelectField selectField = getQuery().addSelectField(className, fieldName, fieldLabel, 
							fieldHibType, fieldHibScale, fieldHibPrec);
					if(alias != null && !alias.trim().equalsIgnoreCase("") && !alias.trim().equalsIgnoreCase("undefined")) {
						selectField.setFieldAlias(alias);
					}
					
					
					// add group by clause
					if(group.equalsIgnoreCase("true")) {
						getQuery().addGroupByField( selectField.getFieldNameWithoutOperators() );
					}
					
					// add function
					String fieldCompleteName = selectField.getFieldName();
					if(funct.equalsIgnoreCase("somma")) {
						fieldCompleteName = "SUM(" + selectField.getFieldName() + ")";
						
					} else if(funct.equalsIgnoreCase("media")) {
						fieldCompleteName = "AVG(" + selectField.getFieldName() + ")";
					} else if(funct.equalsIgnoreCase("massimo")) {
						fieldCompleteName = "MAX(" + selectField.getFieldName() + ")";
					}  else if(funct.equalsIgnoreCase("minimo")) {
						fieldCompleteName = "MIN(" + selectField.getFieldName() + ")";
					}
					selectField.setFieldName( fieldCompleteName );
					
					// add order by clause
					if(order.equalsIgnoreCase("crescente") || order.equalsIgnoreCase("decrescente")) {
						IOrderByField orderByField = getQuery().addOrderByField( fieldCompleteName );
						orderByField.setAscendingOrder( order.equalsIgnoreCase("crescente") );
					}					
				}	
			}
			
			JSONArray filtersJOSN = new JSONArray(filtersSTR);
			for(int i = 0; i < filtersJOSN.length(); i++) {
				JSONObject filterJSON = filtersJOSN.getJSONObject(i);
				String fieldUniqueName = filterJSON.getString("id");
				String alias = filterJSON.getString("alias");
				String operator = filterJSON.getString("operator");
				String value = filterJSON.getString("value");
				String type = filterJSON.getString("type");
				
				DataMartField field = getDatamartModel().getDataMartModelStructure().getField(fieldUniqueName);
				String className = field.getParent().getRoot().getType();
				
				
				String aliasedEntityName;
				String subQueryPrefix = "a";
				String classPrefix = "a";
				
				if (field.getParent().getRoot().getType().indexOf(".") > 0){
					aliasedEntityName = subQueryPrefix + field.getParent().getRoot().getType().replace(".", "_");
				}else{
					aliasedEntityName = subQueryPrefix + field.getParent().getRoot().getType();
				}
				
				String aliasedFieldName = aliasedEntityName + "." + field.getQueryName();
				
				String hibFieldType = field.getType();
				
				String classAlias = classPrefix  + className.replace(".", "_");
				EntityClass ec = new EntityClass(className, classAlias);	
				
				if (!getQuery().containEntityClass(ec)){
					getQuery().addEntityClass(ec);
				}

				IWhereField whereField = getQuery().addWhereField(aliasedFieldName, hibFieldType);
				whereField.setFieldEntityClassForLeftCondition(ec);
				
				whereField.setFieldValue( value );
			}
			
			
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		
		try {
			httpResp.getOutputStream().print("");
			httpResp.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
