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
package it.eng.qbe.model.structure.builder;



import it.eng.qbe.datasource.BasicHibernateDataSource;
import it.eng.qbe.datasource.IHibernateDataSource;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.qbe.utility.RelationField;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

/**
 * @author Andrea Gioia
 *
 */
public class CompositeHibernateDataMartStructureBuilder {
	
	IHibernateDataSource dataSource;	
	
	
	public CompositeHibernateDataMartStructureBuilder(IHibernateDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
			
	public DataMartModelStructure build() {
		
		DataMartModelStructure dataMartStructure = new DataMartModelStructure();
		
		Map classMetadata = dataSource.getSessionFactory().getAllClassMetadata();
		for(Iterator it = classMetadata.keySet().iterator(); it.hasNext(); ) {
			String className = (String)it.next();			
			addEntity(dataMartStructure, className, null, null, 0);			
		}		
		
		return dataMartStructure;
	}
	
	private void addEntity (DataMartModelStructure dataMartStructure,
							String entityName, 
							String relationOnColumnName, 
							String prefix,  
							int recursionLevel){

		DataMartEntity dataMartEntity = dataMartStructure.addEntity(entityName);				
		addKeyFields(dataMartEntity, prefix);			
		List associatedClassesList = addNormalFields(dataMartEntity, prefix);			
		addSubEntities(dataMartEntity, associatedClassesList, recursionLevel);
	}
	
	private void addSubEntity (DataMartEntity parentEntity,
			String entityName, 
			String relationOnColumnName, 
			String prefix,  
			int recursionLevel){

		DataMartEntity dataMartEntity = parentEntity.addSubEntity(entityName);				
		addKeyFields(dataMartEntity, prefix);			
		List associatedClassesList = addNormalFields(dataMartEntity, prefix);			
		addSubEntities(dataMartEntity, associatedClassesList, recursionLevel);
	}
	
	private void addKeyFields(DataMartEntity dataMartEntity, String prefix) {
		
		ClassMetadata classMetadata = dataSource.getSessionFactory().getClassMetadata(dataMartEntity.getName());
		PersistentClass persistentClass = dataSource.getConfiguration().getClassMapping(dataMartEntity.getName());
		
		Type identifierType = classMetadata.getIdentifierType();
		Property identifierproperty = persistentClass.getIdentifierProperty();		
		
		String fieldName = "";
		
		String[] keyProperties = null;
		String[] keyPropertiesType = null;
		String[] hibTypes  = null;
		int[] hibScale  = null;
		int[] hibPrecision = null;
		

		if (identifierType.isComponentType()) {
			
				String idPropertyName = classMetadata.getIdentifierPropertyName();
				String[] tmpKeyProperties = ((ComponentType)identifierType).getPropertyNames();				
				Type[] subtypes = ((ComponentType)identifierType).getSubtypes();
				
				keyProperties = new String[tmpKeyProperties.length];
				keyPropertiesType = new String[tmpKeyProperties.length];
				hibTypes  = new String[tmpKeyProperties.length];
				hibScale  = new int[tmpKeyProperties.length];
				hibPrecision = new int[tmpKeyProperties.length];
								
				
				for (int j=0; j < tmpKeyProperties.length; j++){
					keyProperties[j] = idPropertyName + "." + tmpKeyProperties[j];
					keyPropertiesType[j] = subtypes[j].getClass().getName();
					hibTypes[j] = subtypes[j].getName();
				}	
				
		} else {
			
				keyProperties = new String[1];
				keyProperties[0] = classMetadata.getIdentifierPropertyName();
								
				keyPropertiesType = new String[1];
				keyPropertiesType[0] = identifierType.getClass().getName();
				
				hibTypes = new String[1];
				hibTypes[0] = identifierType.getName();
				
				hibScale = new int[1];
				hibPrecision = new int[1];
		}		
		    	
		
		
		int k = 0;
		for (Iterator it = identifierproperty.getColumnIterator(); it.hasNext(); k++){
			Column column = (Column)it.next();
			hibScale[k] = column.getScale();
			hibPrecision[k] = column.getPrecision();
		}
		
		
		for (int j = 0; j < keyProperties.length; j++) {
			fieldName = keyProperties[j];			
			if (prefix != null) {
				fieldName = prefix + "." + keyProperties[j];
			}							
			
			DataMartField dataMartField = dataMartEntity.addField(fieldName);
			dataMartField.setType(hibTypes[j]);
			dataMartField.setPrecision(hibPrecision[j]);
			dataMartField.setLength(hibScale[j]);
		}
	}
	
	public List addNormalFields(DataMartEntity dataMartEntity, String prefix) {
		ClassMetadata classMetadata = dataSource.getSessionFactory().getClassMetadata(dataMartEntity.getName());
		PersistentClass persistentClass = dataSource.getConfiguration().getClassMapping(dataMartEntity.getName());
		
		String[] metaPropertyNames = classMetadata.getPropertyNames();		
		Type hibType = null;
		
		
		List associatedClassesArrayList = new ArrayList();
		
		String fieldName = null;
		
		for(int i=0; i < metaPropertyNames.length; i++) { // chiave esterna
		 	hibType = (Type)classMetadata.getPropertyType(metaPropertyNames[i]);
		 	Property property = persistentClass.getProperty(metaPropertyNames[i]);
		 	
		 	if (hibType instanceof ManyToOneType){
		 		
			 	Iterator it = property.getColumnIterator();
			 	String columnName = null;
			 	if (it.hasNext()){
			 		columnName = ((Column)it.next()).getName();
			 	}
		 		fieldName = metaPropertyNames[i];
		 		
		 		if (prefix != null){
		 			 fieldName = prefix +"." + metaPropertyNames[i];
		 		}
		 		
		 		RelationField aRelationField = new RelationField( fieldName, ((ManyToOneType)hibType).getAssociatedEntityName(), columnName ); 
		 		
		 		associatedClassesArrayList.add(aRelationField);	
		 		
		 	} else if (hibType instanceof CollectionType) { // chiave interna
				
		 		
			} else { // normal field
					fieldName = metaPropertyNames[i];
					
		 		
					if (prefix != null){
						fieldName = prefix +"." + metaPropertyNames[i];
					}
					
					dataMartEntity.addField(fieldName);						
		 		
				}
		 	}
		
			return associatedClassesArrayList;
	}
	
	private void addSubEntities(DataMartEntity dataMartEntity, List associatedClassesList, int recursionLevel) {
		Iterator it = associatedClassesList.iterator();
		while (it.hasNext()) {
			RelationField relationField = (RelationField)it.next();
			if (relationField.getClassName().equalsIgnoreCase(dataMartEntity.getName()) || recursionLevel > 5){
				// ciclo
				System.out.println(relationField.getClassName() + " - " + dataMartEntity.getName() + " - " + recursionLevel);
			} else {
				addSubEntity(dataMartEntity, relationField.getClassName(), relationField.getRelationOnColumnName(), relationField.getFieldName(), recursionLevel+1);
			}
		}
	}
}
