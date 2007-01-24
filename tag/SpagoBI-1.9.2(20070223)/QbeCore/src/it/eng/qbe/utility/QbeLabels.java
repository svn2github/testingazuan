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
package it.eng.qbe.utility;

import it.eng.qbe.model.DataMartModel;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

/**
 * @author Andrea Gioia
 *
 */
public class QbeLabels {
	
	private SessionFactory sessionFactory;
	private Connection connection;
	Configuration configuration;
		
	public QbeLabels(File jarFile, Connection connection) {
		QbeLabels.updateCurrentClassLoader(jarFile);

		configuration = new Configuration();
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		/*
		configuration.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
		configuration.setProperty("hibernate.connection.url", "jdbc:hsqldb:hsql://localhost/foodmart");
		configuration.setProperty("hibernate.connection.username", "sa");
		configuration.setProperty("hibernate.connection.password", "");	
		*/	
		configuration.setProperty("hibernate.cglib.use_reflection_optimizer", "true");	
		configuration.addJar(jarFile);
		this.sessionFactory = configuration.buildSessionFactory();
		this.connection = connection;
	}
	
	public static void updateCurrentClassLoader(File jarFile){
		try {
				ClassLoader previous = Thread.currentThread().getContextClassLoader();
				
				ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
				
				Thread.currentThread().setContextClassLoader(current);
				
		} catch (Exception e) {
			Logger.error(DataMartModel.class, e);
		}
	}
	
	
	
	
	public void buildLabels() {
		
		
		
		Map classMetadata = sessionFactory.getAllClassMetadata();
		for(Iterator it = classMetadata.keySet().iterator(); it.hasNext(); ) {
			String className = (String)it.next();
			
			System.out.println("\n\n" +
					"# ================================================================================== \n" +
					"#  TABLE: " + className + "\n" +
					"# ================================================================================== ");
			
			System.out.println("\n# ---------------------------------------------------------------------");			
			System.out.println("# Table labal: ");
			System.out.println("# ---------------------------------------------------------------------");
			
			System.out.println("class." + className + "=");
	
			
			addFieldNodes(className, null, null, 0);
		}
		
		
		
	}
	
	public final void addFieldNodes (String className, String relationOnColumnName, 
			String prefix,  int recursionLevel){

		
		PersistentClass pc = configuration.getClassMapping(className);
		
		
		
		if (relationOnColumnName != null){
			
			//String labelForRelation = Utils.getLabelForForeignKey(Utils.getRequestContainer(httpRequest), dataMartModel,className+"("+relationOnColumnName+")");
			
			System.out.println("relation." + className+"("+relationOnColumnName+")=");
		} else {
			System.out.println("\n# ---------------------------------------------------------------------");			
			System.out.println("# Field labels: ");
			System.out.println("# ---------------------------------------------------------------------");
			
		}
		
		try {
			
			ClassMetadata aClassMetadata = sessionFactory.getClassMetadata(className);
			
			String[] metaPropertyNames = aClassMetadata.getPropertyNames();
			
			List associatedClassesArrayList = new ArrayList();
			
			Type aHibType = null;
			 	

			Type aType = aClassMetadata.getIdentifierType();
			
			String completeFieldName = "";
			
			String[] keyProperties = null;
			String[] keyPropertiesType = null;
			String[] hibTypes  = null;
			
	
			if (aType.isComponentType()){
					
					
					String idPropertyName = aClassMetadata.getIdentifierPropertyName();
					String[] tmpKeyProperties = ((ComponentType)aType).getPropertyNames();
					
					
					Type[] subtypes = ((ComponentType)aType).getSubtypes();
					keyProperties = new String[tmpKeyProperties.length];
					keyPropertiesType = new String[tmpKeyProperties.length];
					hibTypes  = new String[tmpKeyProperties.length];
					Class classOfSubTypesI = null;
					
					for (int j=0; j < tmpKeyProperties.length; j++){
						keyProperties[j] = idPropertyName + "." + tmpKeyProperties[j];
						classOfSubTypesI = subtypes[j].getClass();
						keyPropertiesType[j] = classOfSubTypesI.getName();
						hibTypes[j] = subtypes[j].getName();
					}
					
					
			}else{
					keyProperties = new String[1];
					keyProperties[0] = aClassMetadata.getIdentifierPropertyName();
					keyPropertiesType = new String[1];
					keyPropertiesType[0] = aType.getClass().getName();
					hibTypes = new String[1];
					hibTypes[0] = aType.getName();					
			}
			Iterator pkColumnIerator = pc.getIdentifierProperty().getColumnIterator();
			
			    	
			for (int j = 0; j < keyProperties.length; j++) {

				completeFieldName = keyProperties[j];

				
				if (prefix != null) {
					completeFieldName = prefix + "." + keyProperties[j];
				}
				
				System.out.println("field." + completeFieldName + "=");
				
				
			}
			
			List associatedCollectionClassesArrayList = new ArrayList();
				
			
			for(int i=0; i < metaPropertyNames.length; i++){
			 	aHibType = (Type)aClassMetadata.getPropertyType(metaPropertyNames[i]);
			 	org.hibernate.mapping.Property property = pc.getProperty(metaPropertyNames[i]);
			 	
			 	if (aHibType instanceof ManyToOneType){
			 		
				 	Iterator it = property.getColumnIterator();
				 	String columnName = null;
				 	if (it.hasNext()){
				 		columnName = ((Column)it.next()).getName();
				 	}
			 		completeFieldName = metaPropertyNames[i];
			 		
			 		if (prefix != null){
			 			 completeFieldName = prefix +"." + metaPropertyNames[i];
			 		}
			 		
			 		RelationField aRelationField = new RelationField( completeFieldName, ((ManyToOneType)aHibType).getAssociatedEntityName(), columnName ); 
			 		
			 		associatedClassesArrayList.add(aRelationField);																   
			 	}else if (aHibType instanceof CollectionType) {
					
			 		
				}else{
						Logger.debug(this.getClass()," HibType Class" + aHibType.getClass());
						completeFieldName = metaPropertyNames[i];
						String hibType = aHibType.getName();
						String hibScaleTmp = "";
						
			 		
						if (prefix != null){
							completeFieldName = prefix +"." + metaPropertyNames[i];
						}
						
						System.out.println("field." + completeFieldName + "=");					
			 		
					}
			 	}
			
			
			
			Iterator associatedClassIterator = associatedClassesArrayList.iterator();
			if(associatedClassIterator.hasNext()) {
				System.out.println("\n# ---------------------------------------------------------------------");
				System.out.println("# Referenced table labels (indirection-level = " + recursionLevel + "):");
				System.out.println("# ---------------------------------------------------------------------");
			}
			while (associatedClassIterator.hasNext()){
				RelationField aRelationField = (RelationField)associatedClassIterator.next();
				//if(true) {
				if (aRelationField.getClassName().equalsIgnoreCase(className)){
					//addFieldNodesNoRecursion(aRelationField.getClassName(), aRelationField.getRelationOnColumnName(), idxClassNode, nodeCounter, aRelationField.getFieldName(), fieldUrlGenerator, recursionLevel+1);
				}else{
					addFieldNodes(aRelationField.getClassName(),aRelationField.getRelationOnColumnName(), aRelationField.getFieldName(), recursionLevel+1);
				}
			}
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getLabelForClass(RequestContainer requestContainer, DataMartModel dmModel,  String className){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		Locale loc = LocaleUtils.getLocale(requestContainer, qbeMode);
		Properties prop = Utils.getLabelProperties(dmModel, ApplicationContainer.getInstance(), loc);
		
		String res =(String)prop.get("class." + className);
		if ((res != null) && (res.trim().length() > 0))
			return res;
		else
			return className;
	}
	
	public static String getLabelForForeignKey(RequestContainer requestContainer, DataMartModel dmModel,  String classForeignKeyID){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		Locale loc = LocaleUtils.getLocale(requestContainer, qbeMode);
		Properties prop = Utils.getLabelProperties(dmModel, ApplicationContainer.getInstance(), loc);
		
		String res =(String)prop.get("relation." + classForeignKeyID);
		if ((res != null) && (res.trim().length() > 0))
			return res;
		else
			return null;
	}
	
	public static String getLabelForField(RequestContainer requestContainer, DataMartModel dmModel,  String completeFieldName){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		Locale loc = LocaleUtils.getLocale(requestContainer, qbeMode);
		Properties prop = Utils.getLabelProperties(dmModel, ApplicationContainer.getInstance(), loc);
		String res =(String)prop.get("field." + completeFieldName); 
		if ((res != null) && (res.trim().length() > 0))
			return res;
		else
			return completeFieldName;
	}
	
	public static void main(String[] args) {
		String jarFilePath = "C:\\Programmi\\datamart.jar";
		File jarFile = new File(jarFilePath);
		QbeLabels qbeLabels = new QbeLabels(jarFile, null);
		qbeLabels.buildLabels();
	}

	
}
