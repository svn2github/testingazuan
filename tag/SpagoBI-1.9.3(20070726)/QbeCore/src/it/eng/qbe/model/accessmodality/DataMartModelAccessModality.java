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
package it.eng.qbe.model.accessmodality;


import it.eng.qbe.model.Filter;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.StringUtils;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.xml.sax.InputSource;

/**
 * @author Andrea Gioia
 *
 */
public class DataMartModelAccessModality {
	SourceBean modalitySB = null;
	Map entityAccessModalityMap = null;
	
	private static final String TAG_MODALITIES = "MODALITIES";
	private static final String TAG_MODALITY = "MODALITY";
	private static final String TAG_ENTITIES = "TABLES";
	private static final String TAG_ENTITY = "TABLE";
	private static final String TAG_FIELDS = "FIELDS";
	private static final String TAG_FIELD = "FIELD";
	private static final String TAG_FILTERS = "FILTERS";
	private static final String TAG_FILTER = "FILTER";
	
	public DataMartModelAccessModality() {}
	
	public DataMartModelAccessModality(File file) {
		try {			
			modalitySB = SourceBean.fromXMLStream(
					new InputSource(new FileInputStream(file)));
			entityAccessModalityMap = buildEntityAccessModalityMap(modalitySB);
		} catch (SourceBeanException e) {
			Logger.error(DataMartModelAccessModality.class, 
					"Impossible to parse access modality properties from file " + file.toString() 
					+ ": \n" + e.toString());
		} catch (FileNotFoundException e) {
			Logger.error(DataMartModelAccessModality.class, 
					"Impossible to load access modality properties from file " + file.toString() 
					+ ": \n" + e.toString());
		}
	}
	
	public DataMartModelAccessModality(File file, String modalityName) {
		try {			
			SourceBean modalitiesSB = SourceBean.fromXMLStream(
					new InputSource(new FileInputStream(file)));
			modalitySB = getModalitySBByName(modalitiesSB, modalityName);
			entityAccessModalityMap = buildEntityAccessModalityMap(modalitySB);
		} catch (SourceBeanException e) {
			Logger.error(DataMartModelAccessModality.class, 
					"Impossible to parse access modality properties from file " + file.toString() 
					+ ": \n" + e.toString());
		} catch (FileNotFoundException e) {
			Logger.error(DataMartModelAccessModality.class, 
					"Impossible to load access modality properties from file " + file.toString() 
					+ ": \n" + e.toString());
		}
	}
	
	public DataMartModelAccessModality(SourceBean modalitiesSB, String modalityName) {
		modalitySB = getModalitySBByName(modalitiesSB, modalityName);
		entityAccessModalityMap = buildEntityAccessModalityMap(modalitySB);
	}
	
	public DataMartModelAccessModality(SourceBean modalitySB) {
		this.modalitySB = modalitySB;
		entityAccessModalityMap = buildEntityAccessModalityMap(modalitySB);
	}
	
	private SourceBean getModalitySBByName(SourceBean modalitiesSB, String modalityName) {
		SourceBean modalitySB = null;
		if(modalityName != null) {
			modalitySB = (SourceBean)modalitiesSB.getFilteredSourceBeanAttribute(TAG_MODALITY, "name", modalityName);	
     		
		} else {
			List modalities = modalitySB.getAttributeAsList(TAG_MODALITY);
			if(modalities != null && modalities.size() > 0) {
				modalitySB = (SourceBean)modalities.get(0);
			}
		}
		return modalitySB;
	}
	
	private Map buildEntityAccessModalityMap(SourceBean modalitySB) {
		Map map = new HashMap();
		
		List entities =  modalitySB.getAttributeAsList(TAG_ENTITY);
		SourceBean entitySB;
		EntityAccessModalitty entityAccessModality;
		for(int i = 0; i < entities.size(); i++) {
			entitySB = (SourceBean)entities.get(i);
			String entityName = (String)entitySB.getAttribute("name");
			String accessibleStr = (String)entitySB.getAttribute("accessible");
			boolean accessibleFlg = accessibleStr == null? true: accessibleStr.equalsIgnoreCase("TRUE");
			entityAccessModality = new EntityAccessModalitty(entityName, accessibleFlg);
			
			// get filed accessibility constraints
			List fields = entitySB.getAttributeAsList(TAG_FIELDS + "." + TAG_FIELD);
			SourceBean fieldSB;
			for(int j = 0; j < fields.size(); j++) {
				fieldSB = (SourceBean)fields.get(j);
				String fieldName = (String)fieldSB.getAttribute("name");
				String accesible = (String)fieldSB.getAttribute("accessible");
				if(accesible.equalsIgnoreCase("FALSE")) {
					entityAccessModality.setFieldUnaccessible(fieldName);
				}				
			}
			// get entity filter conditions
			List filters = entitySB.getAttributeAsList(TAG_FILTERS + "." + TAG_FILTER);
			SourceBean filterSB;
			for(int j = 0; j < filters.size(); j++) {
				filterSB = (SourceBean)filters.get(j);
				String filterCondition = filterSB.getCharacters();
				entityAccessModality.addFilterConditions(filterCondition);
								
			}
			map.put(entityAccessModality.getName(), entityAccessModality);
		}
		
		return map;
	}

	public boolean isEntityAccessible(String entityName) {
		if(entityAccessModalityMap != null && entityAccessModalityMap.containsKey(entityName)) {
			EntityAccessModalitty entityAccessModalitty = (EntityAccessModalitty)entityAccessModalityMap.get(entityName);
			return entityAccessModalitty.isAccessible();
		}
		return true;
	}
	
	public boolean isFieldAccessible(String tableName, String fieldName) {
		if(entityAccessModalityMap != null && entityAccessModalityMap.containsKey(tableName)) {
			EntityAccessModalitty tableAccessModalitty = (EntityAccessModalitty)entityAccessModalityMap.get(tableName);
			return tableAccessModalitty.isFieldAccessible(fieldName);
		}
		return true;
	}
	
	public List getEntityFilterConditions(String entityName) {
		if(entityAccessModalityMap != null && entityAccessModalityMap.containsKey(entityName)) {
			EntityAccessModalitty entityAccessModalitty = (EntityAccessModalitty)entityAccessModalityMap.get(entityName);
			return entityAccessModalitty.getFilterConditions();
		}
		return new ArrayList();
	}
	
	public List getEntityFilterConditions(String entityName, Properties parameters) throws IOException {
		List newFilterConditions = new ArrayList();
		List filterConditions = getEntityFilterConditions(entityName);
		for(int i = 0; i < filterConditions.size(); i++) {
			String filterCondition = (String)filterConditions.get(i);
			filterCondition = StringUtils.replaceParameters(filterCondition, "P", parameters);
			newFilterConditions.add(filterCondition);
		}
		return newFilterConditions;
	}
	
	
		
	public static class EntityAccessModalitty {
		String name;
		boolean accessible;
		Map filedsAccessModalityMap;
		List filterConditions;
		
		public EntityAccessModalitty(String tableName, boolean accessible) {
			this.name = tableName;
			this.accessible = accessible;
			filedsAccessModalityMap = new HashMap();
			filterConditions = new ArrayList();
		}
		
		public void setFieldUnaccessible(String fieldName) {
			filedsAccessModalityMap.put(fieldName, "UNACCESSIBLE");
		}
		
		public boolean isFieldAccessible(String fieldName) {
			boolean accessible = !filedsAccessModalityMap.containsKey(fieldName);
			return accessible;
		}

		public String getName() {
			return name;
		}

		public boolean isAccessible() {
			return accessible;
		}
		
		public void addFilterConditions(String condition) {
			Filter filter = new Filter(name, condition);
			filterConditions.add(filter);
		}

		public List getFilterConditions() {
			return filterConditions;
		}
	}
}
