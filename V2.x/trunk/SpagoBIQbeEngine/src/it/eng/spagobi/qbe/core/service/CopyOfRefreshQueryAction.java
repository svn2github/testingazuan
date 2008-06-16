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
package it.eng.spagobi.qbe.core.service;

import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.newquery.Query;
import it.eng.qbe.query.IOrderByField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.query.IWhereField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



// TODO: Auto-generated Javadoc
/**
 * The Class RefreshQueryAction.
 */
public class CopyOfRefreshQueryAction extends AbstractQbeEngineAction {
	
		
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException  {				
		super.service(request, response);		
		
		String recordsSTR = getAttributeAsString("records");
		String filtersSTR = getAttributeAsString("filters");

		if(getQuery().isEmpty() == false) {
			setDatamartWizard( new SingleDataMartWizardObjectSourceBeanImpl() );
		}
		
		getEngineInstance().setQuery(new Query());
		
		try {
			JSONArray recordsJOSN = new JSONArray(recordsSTR);
			for(int i = 0; i < recordsJOSN.length(); i++) {
				JSONObject recordJSON = recordsJOSN.getJSONObject(i);
				String fieldUniqueName = recordJSON.getString("id");
				String alias = recordJSON.getString("alias") != null 
							   && !recordJSON.getString("alias").trim().equalsIgnoreCase("")
							   ? recordJSON.getString("alias")
							   : "Column_" + (i+1);
				String fieldA = recordJSON.getString("field");
				String group = recordJSON.getString("group");
				String order = recordJSON.getString("order");
				String funct = recordJSON.getString("funct");
				boolean visible = "si".equalsIgnoreCase( recordJSON.getString("visible") );
				
				
								
				if(fieldUniqueName != null) {
					// add field
					DataMartField field = getDatamartModel().getDataMartModelStructure().getField(fieldUniqueName);
					getEngineInstance().getQuery().addSelectFiled(field.getUniqueName(), funct, alias, visible, group.equalsIgnoreCase("true"), order);
					if(group.equalsIgnoreCase("true")) {
						getEngineInstance().getQuery().addGroupByField(field.getUniqueName());
					}
					if(order != null && (order.equalsIgnoreCase("ASC") || order.equalsIgnoreCase("DESC")) ) {
						getEngineInstance().getQuery().addOrderByField(fieldUniqueName, order.equalsIgnoreCase("ASC"));
					}
					// add field
					
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
					} else if(fieldA != null && !fieldA.trim().equalsIgnoreCase("") && !fieldA.trim().equalsIgnoreCase("undefined")) {
						selectField.setFieldAlias(fieldA);
					}
					
					selectField.setVisible(visible);
					
					
					if(group.equalsIgnoreCase("true")) {
						getQuery().addGroupByField( selectField.getFieldNameWithoutOperators() );
					}
					
					String fieldCompleteName = selectField.getFieldName();
					if(funct.equalsIgnoreCase("SUM")) {
						fieldCompleteName = "SUM(" + selectField.getFieldName() + ")";
						
					} else if(funct.equalsIgnoreCase("AVG")) {
						fieldCompleteName = "AVG(" + selectField.getFieldName() + ")";
					} else if(funct.equalsIgnoreCase("MAX")) {
						fieldCompleteName = "MAX(" + selectField.getFieldName() + ")";
					}  else if(funct.equalsIgnoreCase("MIN")) {
						fieldCompleteName = "MIN(" + selectField.getFieldName() + ")";
					}
					selectField.setFieldName( fieldCompleteName );
					
					// add order by clause
					if(order.equalsIgnoreCase("ASC") || order.equalsIgnoreCase("DESC")) {
						IOrderByField orderByField = getQuery().addOrderByField( fieldCompleteName );
						orderByField.setAscendingOrder( order.equalsIgnoreCase("ASC") );
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
				
				if(operator == null || operator.trim().equalsIgnoreCase("") || operator.equalsIgnoreCase("NONE")) continue;
				
				DataMartField field = getDatamartModel().getDataMartModelStructure().getField(fieldUniqueName);
				getEngineInstance().getQuery().addWhereFiled(field.getUniqueName(), operator, value);
				
				
				
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
				whereField.setFieldOperator(operator);				
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
