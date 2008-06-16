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
package it.eng.qbe.javascript;

import it.eng.qbe.bo.DatamartProperties;
import it.eng.qbe.datasource.BasicHibernateDataSource;
import it.eng.qbe.datasource.CompositeHibernateDataSource;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.urlgenerator.IURLGenerator;
import it.eng.qbe.urlgenerator.SelectFieldForSelectionURLGenerator;
import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.JsTreeUtils;
import it.eng.qbe.utility.QbeProperties;
import it.eng.qbe.utility.RelationField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.PortletQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.WebQbeUrlGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeJsTreeBuilder.
 * 
 * @author Andrea Gioia
 */
public abstract class QbeJsTreeBuilder extends BaseJsTreeBuilder {

	/** The data mart model. */
	DataMartModel dataMartModel = null;
	
	/** The data mart wizard. */
	ISingleDataMartWizardObject dataMartWizard = null;
	
	/** The target datamart name. */
	String targetDatamartName = null;
	
	/** The selected nodes. */
	Map selectedNodes = null;
	
	/** The http request. */
	HttpServletRequest httpRequest = null; 
	
	/** The qbe url generator. */
	IQbeUrlGenerator qbeUrlGenerator = null;
	
	/** The qbe properties. */
	DatamartProperties qbeProperties;
	
	/** The action name. */
	String actionName = null;
	
	/** The modality. */
	String modality = DEFAULT_MODALITY;
	
	/** The checkable. */
	boolean checkable = false;
	
	//QbeAccessModality qbeAccessModality;
	
	/** The Constant FULL_MODALITY. */
	public static final String FULL_MODALITY = "FULL";
	
	/** The Constant LIGHT_MODALITY. */
	public static final String LIGHT_MODALITY = "LIGHT";
	
	/** The Constant DEFAULT_MODALITY. */
	public static final String DEFAULT_MODALITY = FULL_MODALITY;
	
	/** The class prefix. */
	private String classPrefix = null;
	
	/**
	 * The Constructor.
	 * 
	 * @param dataMartModel : The DatamartModel object reperesenting the datamart we're working on
	 * @param httpRequest : the httpRequest Object
	 * @param dataMartWizard the data mart wizard
	 */
	public QbeJsTreeBuilder(DataMartModel dataMartModel, ISingleDataMartWizardObject dataMartWizard, HttpServletRequest httpRequest){
		this.dataMartModel = dataMartModel;
		this.dataMartWizard = dataMartWizard;
		this.httpRequest = httpRequest;
		this.qbeProperties = dataMartModel.getDataSource().getProperties();
		//this.qbeAccessModality = QbeAccessModalityFactory.getAccessModality("", "");
		
		selectedNodes = getSelectdNodes();
		
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");   
		
		if (qbeMode.equalsIgnoreCase("WEB")){
			qbeUrlGenerator = new WebQbeUrlGenerator();
		} else if  (qbeMode.equalsIgnoreCase("PORTLET")){
			qbeUrlGenerator = new PortletQbeUrlGenerator();
		}		
	}
	
	/**
	 * Gets the selectd nodes.
	 * 
	 * @return the selectd nodes
	 */
	public abstract Map getSelectdNodes();
	
	

	
	/* (non-Javadoc)
	 * @see it.eng.qbe.javascript.IJsTreeBuilder#build()
	 */
	public String build() {	
		StringBuffer treeScriptBuffer = new StringBuffer();
		
		List jsTreeList = getJsTreeList();
		if(jsTreeList != null) {
			for(int i = 0; i < jsTreeList.size(); i++) {
				String treeScript = (String)jsTreeList.get(i);
				treeScriptBuffer.append(treeScript + "\n\n\n");
			}
		}
		
		return treeScriptBuffer.toString();
	}
	
	/**
	 * Gets the js tree list.
	 * 
	 * @return the js tree list
	 */
	public List getJsTreeList() {
		List jsTreeList = null;
		
		Map jsTreeMap = getJsTreeMap();
		if(jsTreeMap != null) {
			jsTreeList = new ArrayList();
			Iterator it = jsTreeMap.keySet().iterator();
			while(it.hasNext()) {
				String dmName = (String)it.next();
				String treeScript = (String)jsTreeMap.get(dmName);
				jsTreeList.add(treeScript);
			}
		}
		
		return jsTreeList;
	}
	
	/**
	 * Gets the js tree map.
	 * 
	 * @return the js tree map
	 */
	public Map getJsTreeMap() {
		Map jsTreeMap = null;
		
		long start = System.currentTimeMillis();
		
		if(dataMartModel.getDataSource() instanceof BasicHibernateDataSource) {
			String treeScript = buildSingleTree();
			jsTreeMap = new HashMap();
			jsTreeMap.put(dataMartModel.getName(), treeScript);
		} else if (dataMartModel.getDataSource() instanceof CompositeHibernateDataSource) {
			jsTreeMap = buildCompositeTree();
		} else {
			// fail fast behaviour
			throw new RuntimeException("Impossible to build a jsTree using a datasource of type " + dataMartModel.getDataSource().getClass().getName());
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Elapsed: " + ((end - start)/1000));
		
		return jsTreeMap;
	}
	
	/**
	 * Builds the single tree.
	 * 
	 * @return the string
	 */
	private String buildSingleTree() {	
		
		buffer = new StringBuffer();
		addHeader();
		addTree();
		addRootNode();
		addNodes();
		addFooter();
		
		return buffer.toString();
	}

	/**
	 * Builds the composite tree.
	 * 
	 * @return the map
	 */
	private Map buildCompositeTree() {	
		Map treeScripts = new HashMap();
		CompositeHibernateDataSource dataSource = (CompositeHibernateDataSource)dataMartModel.getDataSource();
		List dmNames = dataSource.getDatamartNames();
		String baseName = name;
		for(int i = 0; i < dmNames.size(); i++) {
			targetDatamartName = (String)dmNames.get(i);
			name = baseName + "_" + i;
			
			if(getClassNames().size() > 0) {
				String treeScript = buildSingleTree();
				treeScripts.put(targetDatamartName, treeScript);
			}
		}	
		
		targetDatamartName = "Views";
		name = baseName + "_" + dmNames.size();
		if(getHibernateSession() != null && getClassNames().size() > 0) {
			String treeScript = buildSingleTree();
			treeScripts.put(targetDatamartName, treeScript);
		}
		
		name = baseName;
		
		return treeScripts;
	}
	
	
	
	/**
	 * Adds the root node.
	 */
	public void addRootNode() {
		String rootNodeName = targetDatamartName;
		if(rootNodeName == null) rootNodeName = dataMartModel.getName();
		
		addNode("0", "-1", rootNodeName, "", "", rootNodeName, 
				qbeUrlGenerator.getResourceUrl(httpRequest,"../img/base.gif"),
				qbeUrlGenerator.getResourceUrl(httpRequest,"../img/base.gif"),
				"", "", "", "", "");
	}
	
	
	/**
	 * Adds the nodes.
	 */
	public abstract void addNodes();
	
		
	/**
	 * Gets the hibernate session.
	 * 
	 * @return the hibernate session
	 */
	private SessionFactory getHibernateSession() {
		SessionFactory sf = null;
		if(dataMartModel.getDataSource() instanceof CompositeHibernateDataSource && targetDatamartName != null) {
			sf = ((CompositeHibernateDataSource)dataMartModel.getDataSource()).getSessionFactory(targetDatamartName);
		} else {
			sf = dataMartModel.getDataSource().getSessionFactory();
		}
			
	
		if(sf == null){
			Logger.critical(this.getClass(), "writeSelectionTree: session factory NULL");
		}
		return sf;
	}
	
	/**
	 * Gets the class names.
	 * 
	 * @return the class names
	 */
	protected Collection getClassNames() {
		if(getHibernateSession().getAllClassMetadata() == null){
			Logger.critical(this.getClass(), "writeSelectionTree: map metadata classes NULL");
			return null;
		}
		Logger.debug(this.getClass(), "writeSelectionTree: metadata class map retrived successfully");
		Set namesSet = getHibernateSession().getAllClassMetadata().keySet();
		QbeTreeFields fields = new QbeTreeFields();
		fields.addAllFields(namesSet);
		List namesList = fields.getFieldsOrderedByLabel();
		return namesList;
	}
	
	/**
	 * Gets the selected class names.
	 * 
	 * @return the selected class names
	 */
	protected Collection getSelectedClassNames() {
		Set selectedClassNames = null;
				
		selectedClassNames = new HashSet();
		EntityClass ec = null;	
		Collection allClassNames = getClassNames();		
		for (Iterator it = dataMartWizard.getQuery().getEntityClassesIterator(); it.hasNext(); ){
				ec  = (EntityClass)it.next();	
				if(allClassNames.contains(ec.getClassName())) {
					selectedClassNames.add(ec.getClassName());
				}
		}
				
		return selectedClassNames;	 
	}
		
	
	/**
	 * Recursive Function To write the tree of model navigable  starting by className.
	 * 
	 * @param className the class name
	 * @param rootNode the root node
	 * @param nodeCounter the node counter
	 * @param prefix the prefix
	 * @param fieldUrlGenerator the field url generator
	 * @param relationOnColumnName the relation on column name
	 * @param recursionLevel the recursion level
	 * 
	 * @return the int
	 */
	public final int addFieldNodes (String className, String relationOnColumnName, 
			int rootNode, int nodeCounter, String prefix, 
			IURLGenerator fieldUrlGenerator, int recursionLevel){
		
		String newClassName = className;
		if (relationOnColumnName != null){
			newClassName = className+"("+relationOnColumnName+")";
		}
		if(!qbeProperties.isEntityVisible(newClassName)) return nodeCounter;
		
		nodeCounter++;
		
		PersistentClass pc = dataMartModel.getDataSource().getConfiguration().getClassMapping(className);
		
		
		String classLabel = JsTreeUtils.getLabelForClass(dataMartModel, className);
		
		if (relationOnColumnName != null){
			
			String labelForRelation = JsTreeUtils.getLabelForForeignKey(dataMartModel,className+"("+relationOnColumnName+")");
			if (labelForRelation != null){
				classLabel = labelForRelation;
			}else{
				classLabel += "("+ relationOnColumnName +")";
			}
		}
		
		String classImage;
		if(qbeProperties.getEntityType(newClassName) == DatamartProperties.CLASS_TYPE_CUBE) {
			classImage = "../img/Class.gif";
		} else if(qbeProperties.getEntityType(newClassName) == DatamartProperties.CLASS_TYPE_VIEW) {
			classImage = "../img/view.gif";
		} else {
			classImage = "../img/relationship.gif";
		}
		
		if(!qbeProperties.isEntityVisible(newClassName)) return nodeCounter;
		//if(!qbeAccessModality.isTableAccessible(newClassName)) return nodeCounter;
		if(!dataMartModel.getDataMartModelAccessModality().isEntityAccessible(newClassName)) return nodeCounter;
		
		
		// add class node
		addNode("" + nodeCounter, "" + rootNode, classLabel, "", "", classLabel, 
				qbeUrlGenerator.getResourceUrl(httpRequest,classImage),
				qbeUrlGenerator.getResourceUrl(httpRequest,classImage),
				"", "", "", "", "");
		
		
		int idxClassNode = nodeCounter;
		
		// Genero i field
		try{
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = dataMartModel.getDataSource().getSessionFactory();
			
			ClassMetadata aClassMetadata = sf.getClassMetadata(className);
			
			String[] metaPropertyNames = aClassMetadata.getPropertyNames();
			
			List associatedClassesArrayList = new ArrayList();
			
			Type aHibType = null;
			
		

			Type aType = aClassMetadata.getIdentifierType();
			
			String completeFieldName = "";
			String fieldAction = "";
			List keyProperties = new ArrayList();
			String[] keyPropertiesType = null;
			String[] hibTypes  = null;
			String[] hibScale  = null;
			String[] hibPrecision = null;
			if (aType.isComponentType()){
					
					
					String idPropertyName = aClassMetadata.getIdentifierPropertyName();
					String[] tmpKeyProperties = ((ComponentType)aType).getPropertyNames();
					
					
					Type[] subtypes = ((ComponentType)aType).getSubtypes();
					//keyProperties = new String[tmpKeyProperties.length];
					keyPropertiesType = new String[tmpKeyProperties.length];
					hibTypes  = new String[tmpKeyProperties.length];
					hibScale  = new String[tmpKeyProperties.length];
					hibPrecision = new String[tmpKeyProperties.length];
					Class classOfSubTypesI = null;
					
					for (int j=0; j < tmpKeyProperties.length; j++){
						/*
						org.hibernate.mapping.Property property = pc.getProperty(idPropertyName);
						Iterator it = property.getColumnIterator();
					 	String columnName = null;
					 	if (it.hasNext()){
					 		columnName = ((Column)it.next()).getName();
					 	}
						
						
						if(QbeProperties.getTableType(dataMartModel, className) == QbeProperties.CLASS_TYPE_RELATION 
								&& foreignFields.containsKey(columnName)) continue;
						*/
						
						keyProperties.add(idPropertyName + "." + tmpKeyProperties[j]);
						classOfSubTypesI = subtypes[j].getClass();
						keyPropertiesType[j] = classOfSubTypesI.getName();
						hibTypes[j] = subtypes[j].getName();
					}
					
					
			}else{
					/*
					if(QbeProperties.getTableType(dataMartModel, className) != QbeProperties.CLASS_TYPE_RELATION 
						|| foreignFields.containsKey(aClassMetadata.getIdentifierPropertyName())){
				*/
						keyProperties.add(aClassMetadata.getIdentifierPropertyName());
						keyPropertiesType = new String[1];
						keyPropertiesType[0] = aType.getClass().getName();
						hibTypes = new String[1];
						hibTypes[0] = aType.getName();
						hibScale = new String[1];
						hibPrecision = new String[1];
					//}
					
			}
			Iterator pkColumnIerator = pc.getIdentifierProperty().getColumnIterator();
			int k = -1;
			Column col = null;
			while (pkColumnIerator.hasNext()){
				k++;
				col = (Column)pkColumnIerator.next();
				hibScale[k] = String.valueOf(col.getScale());
				hibPrecision[k] = String.valueOf(col.getPrecision());
			}
			    	
			for (int j = 0; j < keyProperties.size(); j++) {

				Logger.debug(this.getClass(),"keyProperties[" + j + "] "
						+ (String)keyProperties.get(j));
				nodeCounter++;
				completeFieldName = (String)keyProperties.get(j);

				
				if (prefix != null) {
					completeFieldName = prefix + "." + (String)keyProperties.get(j);
				}
				
				  // unique name of the field for label resolving procedure
				String cn = fieldUrlGenerator.getClassName();
				cn = (cn.lastIndexOf('.') > 0 ?
					  cn.substring(cn.lastIndexOf('.') + 1 , cn.length()) :
					  cn);
						
				 String completeFieldRef = cn + "." + completeFieldName;
				
				String fldLabel = JsTreeUtils.getLabelForField(dataMartModel, completeFieldRef);
				if(fldLabel.equalsIgnoreCase(completeFieldRef)) {
					fldLabel = JsTreeUtils.getLabelForField(dataMartModel, (String)keyProperties.get(j));
				}
				
				String addParameters = "";
				if (fieldUrlGenerator instanceof SelectFieldForSelectionURLGenerator ) {
					addParameters = "";
					addParameters += hibTypes[j];
					addParameters += ";"+hibScale[j];
					addParameters += ";"+hibPrecision[j];
					fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel,  addParameters);
					
				}else{					
					fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel,  keyPropertiesType[j]);
				}
				
				String fieldImage;
				int type = qbeProperties.getFieldType(completeFieldRef);
				if(type == DatamartProperties.FIELD_TYPE_UNDEFINED) {
					type = qbeProperties.getFieldType((String)keyProperties.get(j));
					if(type == DatamartProperties.FIELD_TYPE_UNDEFINED) {
						type = DatamartProperties.FIELD_TYPE_ATTRIBUTE;
					} else {
						qbeProperties.setFieldType(completeFieldRef, type);
					}
				}
				
				if(type == DatamartProperties.FIELD_TYPE_ATTRIBUTE) {
					fieldImage = "../img/key.gif";
				} else if(type == DatamartProperties.FIELD_TYPE_ATTRIBUTE) {
					fieldImage = "../img/key.gif";
				} else if(type == DatamartProperties.FIELD_TYPE_GEOREF) {
					fieldImage = "../img/world.gif";
				} else {
					fieldImage = "../img/key.gif";
				}
				
				if(checkable) {
					String selected = "";
					if(selectedNodes.containsKey(new  QbeJsTreeNodeId(className, completeFieldName, classPrefix).getId())) selected = "true";
					
					String img = "/img/key.gif";
					
					
					addNode("" + nodeCounter, "" + idxClassNode, 
							fldLabel,
							fieldAction,  
							fldLabel, 
							"_self",
							qbeUrlGenerator.getResourceUrl(httpRequest,img),
							qbeUrlGenerator.getResourceUrl(httpRequest,img),
							"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);	
				}
				else {
					addNode("" + nodeCounter, "" + idxClassNode, 
							fldLabel,
							fieldAction,  
							fldLabel, 
							"_self",
							qbeUrlGenerator.getResourceUrl(httpRequest,fieldImage),
							qbeUrlGenerator.getResourceUrl(httpRequest,fieldImage),
							"", "", "", "", "");	
				}
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
					//Logger.debug(this.getClass()," Collection type skip........");
					
					CollectionType collection = (CollectionType)aHibType;
					
					
					Iterator it = property.getColumnIterator();
				 	String columnName = null;
				 	if (it.hasNext()){
				 		columnName = ((Column)it.next()).getName();
				 	}
			 		completeFieldName = metaPropertyNames[i];
			 		
			 		if (prefix != null){
			 			 completeFieldName = prefix +"." + metaPropertyNames[i];
			 		}
					
					String str = collection.getAssociatedEntityName((SessionFactoryImpl)sf);
					//System.out.println("entity: " + str);
		
					RelationField aRelationField = new RelationField( completeFieldName, 
							collection.getAssociatedEntityName((SessionFactoryImpl)sf), 
							columnName ); 
					// associatedClassesArrayList.add(aRelationField);
					
			 		
				}else{
						if(!qbeProperties.isFieldVisible(metaPropertyNames[i])) continue;
						if(!dataMartModel.getDataMartModelAccessModality().isFieldAccessible(className, metaPropertyNames[i])) continue;
						
						
						
						Logger.debug(this.getClass()," HibType Class" + aHibType.getClass());
						nodeCounter++;
						completeFieldName = metaPropertyNames[i];
						String hibType = aHibType.getName();
						String hibScaleTmp = "";
						String hibPrec = "";
						Column col1 = null;
						Iterator it = property.getColumnIterator();
					 	if (it.hasNext()){
					 		col1 = (Column)it.next();
					 		hibScaleTmp = String.valueOf(col1.getScale());
					 		hibPrec = String.valueOf(col1.getPrecision());
					 	}
			 		
						if (prefix != null){
							completeFieldName = prefix +"." + metaPropertyNames[i];
						}
						
						
						 // unique name of the field for label resolving procedure
						String cn = fieldUrlGenerator.getClassName();
						cn = (cn.lastIndexOf('.') > 0 ?
							  cn.substring(cn.lastIndexOf('.') + 1 , cn.length()) :
							  cn);
								
						String completeFieldRef = cn + "." + completeFieldName;
					
						String fldLabel = JsTreeUtils.getLabelForField(dataMartModel, completeFieldRef);
						if(fldLabel.equalsIgnoreCase(completeFieldRef)) {
							fldLabel = JsTreeUtils.getLabelForField(dataMartModel, metaPropertyNames[i]);
						}
						
						String fieldImage;
						int type = qbeProperties.getFieldType(completeFieldRef);
						if(type == DatamartProperties.FIELD_TYPE_UNDEFINED) {
							type = qbeProperties.getFieldType(metaPropertyNames[i]);
							if(type == DatamartProperties.FIELD_TYPE_UNDEFINED) {
								type = DatamartProperties.FIELD_TYPE_ATTRIBUTE;
							} else {
								qbeProperties.setFieldType(completeFieldRef, type);
							}
						}
						
						if(type == DatamartProperties.FIELD_TYPE_ATTRIBUTE) {
							fieldImage = "../img/redbox.gif"; //"../img/Method.gif";
						} else if(type == DatamartProperties.FIELD_TYPE_MEASURE) {
							fieldImage = "../img/Method.gif"; //"../img/dot.png";
						} else if(type == DatamartProperties.FIELD_TYPE_GEOREF) {
							fieldImage = "../img/world.gif"; //"../img/dot.png";
						} else {
							fieldImage = "../img/undef.gif"; //"../img/Method.gif";
						}
						
						
					
						String addParameters = "";
						if (fieldUrlGenerator instanceof SelectFieldForSelectionURLGenerator ) {
							addParameters = "";
							addParameters += aHibType.getName();
							addParameters += ";"+hibScaleTmp;
							addParameters += ";"+hibPrec;
							fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel,  addParameters);
							
						}else{
							
							fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel, aHibType.getClass().getName());
						
						}
						
						//
						
						
			 		
			 		
						if(checkable) {
							String selected = "";
							if(selectedNodes.containsKey(new  QbeJsTreeNodeId(className, completeFieldName).getId())) selected = "true";
						
							if(selected.equalsIgnoreCase("true"))
								System.out.println("check[" + className + "," + completeFieldName + "]: " 
										+ (new  QbeJsTreeNodeId(className, completeFieldName).getId()) + " -> " + selected.toUpperCase());
						
						
							addNode("" + nodeCounter, "" + idxClassNode, 
									fldLabel,
									fieldAction,  
									fldLabel, 
									"_self",
									qbeUrlGenerator.getResourceUrl(httpRequest,fieldImage),
									qbeUrlGenerator.getResourceUrl(httpRequest,fieldImage),
									"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);		
						} else {
							addNode("" + nodeCounter, "" + idxClassNode, 
				 				fldLabel,
								fieldAction,  
								fldLabel, 
								"_self",
								qbeUrlGenerator.getResourceUrl(httpRequest,fieldImage),
								qbeUrlGenerator.getResourceUrl(httpRequest,fieldImage),
								"", "", "",  "", "");
						}
					}
			 	}
			
			
			//
			// Add Calculate Fields on the entity
			// Control recursion level because calculate field are applied only at etity level not in dimension level
			//
			if (/*(recursionLevel == 1) &&*/ (fieldUrlGenerator instanceof SelectFieldForSelectionURLGenerator)){
				
				List manualCalcultatedFieldForEntity = dataMartModel.getFormula().getManualCalculatedFieldsForEntity(className);
				
				CalculatedField cField = null;
				String cFieldAction = null;
				for (Iterator itManualCalculatedFields=manualCalcultatedFieldForEntity.iterator(); itManualCalculatedFields.hasNext();){
					cField = (CalculatedField)itManualCalculatedFields.next();
					if (prefix != null){
						cField.setFldCompleteNameInQuery(prefix + "." + cField.getId());
					}else{
						cField.setFldCompleteNameInQuery(cField.getId());
					}
					
					cFieldAction = ((SelectFieldForSelectionURLGenerator)fieldUrlGenerator).generateURLForCalculateField(cField.getId(), className, cField.getFldCompleteNameInQuery());
					nodeCounter++;
					addNode("" + nodeCounter, "" + idxClassNode, 
							cField.getFldLabel(),
							cFieldAction,  
							cField.getFldLabel(),
							"_self",
							qbeUrlGenerator.getResourceUrl(httpRequest,"../img/cfield.gif"),
							qbeUrlGenerator.getResourceUrl(httpRequest,"../img/cfield.gif"),
							"", "", "",  "", "");
				}
				
			}
			
			// ordinare qui
			//List sortedList = sortList(associatedClassesArrayList);
			QbeTreeRelations fields = new QbeTreeRelations();
			fields.addAllRelations(associatedClassesArrayList);
			List sortedList = fields.getFieldsOrderedByLabel();
			
			//Iterator associatedClassIterator = associatedClassesArrayList.iterator();
			Iterator associatedClassIterator = sortedList.iterator();
			while (associatedClassIterator.hasNext()){
				RelationField aRelationField = (RelationField)associatedClassIterator.next();
				//if(true) {
				if (aRelationField.getClassName().equalsIgnoreCase(className) ||
						recursionLevel > 2){
					nodeCounter = addFieldNodesNoRecursion(aRelationField.getClassName(), aRelationField.getRelationOnColumnName(), idxClassNode, nodeCounter, aRelationField.getFieldName(), fieldUrlGenerator, recursionLevel+1);
				}else{
					nodeCounter = addFieldNodes(aRelationField.getClassName(),aRelationField.getRelationOnColumnName(), idxClassNode, nodeCounter, aRelationField.getFieldName(), fieldUrlGenerator, recursionLevel+1);
				}
			}
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return nodeCounter;
	}
	
//	private static List sortList(List source) {
//		List toReturn = new ArrayList();
//		if (source != null && source.size() > 0) {
//			Iterator it = source.iterator();
//			RelationField aRelationField = (RelationField) it.next();
//			addToSortedList(aRelationField, toReturn);
//		}
//		return toReturn;
//	}
	
//	private static void addToSortedList(RelationField aRelationField, List ordered) {
//		if (ordered == null) return;
//		if (aRelationField == null) return;
//		for (int i = 0; i < ordered.size(); i++) {
//			RelationField aField = (RelationField) ordered.get(i);
//			System.out.println("******************* Class name: " + aField.getClassName());
//			System.out.println("******************* Field name: " + aField.getFieldName());
//			System.out.println("******************* RelationOnColumnName: " + aField.getRelationOnColumnName());
//			if (aRelationField.getFieldName().compareTo(aField.getFieldName()) < 0) {
//				ordered.add(i, aField);
//				return;
//			}
//		}
//		ordered.add(aRelationField);
//	}
	
	/**
 * Adds the field nodes no recursion.
 * 
 * @param className the class name
 * @param relationFieldName the relation field name
 * @param rootNode the root node
 * @param nodeCounter the node counter
 * @param prefix the prefix
 * @param fieldUrlGenerator the field url generator
 * @param recursionLevel the recursion level
 * 
 * @return the int
 */
public final int addFieldNodesNoRecursion (String className, String relationFieldName, int rootNode, int nodeCounter, String prefix, IURLGenerator fieldUrlGenerator, int recursionLevel){
		
		
		nodeCounter++;
		
		String classLabel = JsTreeUtils.getLabelForClass(dataMartModel, className);
		if (relationFieldName != null){
			String labelForRelation = JsTreeUtils.getLabelForForeignKey(dataMartModel,className+"("+relationFieldName+")");
			if (labelForRelation != null){
				classLabel = labelForRelation;
			}else{
				classLabel += "("+ relationFieldName +")";
			}
		}
		
		// add class node
		addNode("" + nodeCounter, "" + rootNode, classLabel, "", "", classLabel, 
				qbeUrlGenerator.getResourceUrl(httpRequest,"../img/Class.gif"),
				qbeUrlGenerator.getResourceUrl(httpRequest,"../img/Class.gif"),
				"", "", "", "", "");
		
		
		int idxClassNode = nodeCounter;
		
		// Genero i field
		try{
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = dataMartModel.getDataSource().getSessionFactory();
			
			ClassMetadata aClassMetadata = sf.getClassMetadata(className);
			
			String[] metaPropertyNames = aClassMetadata.getPropertyNames();
			
			List associatedClassesArrayList = new ArrayList();
			
			Type aHibType = null;
			String urlRef = "";    	

			Type aType = aClassMetadata.getIdentifierType();
			
			String completeFieldName = "";
			String fieldAction = "";
			String[] keyProperties = null;
			String[] keyPropertiesType = null;
			if (aType.isComponentType()){
					
					
					String idPropertyName = aClassMetadata.getIdentifierPropertyName();
					String[] tmpKeyProperties = ((ComponentType)aType).getPropertyNames();
					
					Type[] subtypes = ((ComponentType)aType).getSubtypes();
					keyProperties = new String[tmpKeyProperties.length];
					keyPropertiesType = new String[tmpKeyProperties.length];
					for (int j=0; j < tmpKeyProperties.length; j++){
						keyProperties[j] = idPropertyName + "." + tmpKeyProperties[j];
						keyPropertiesType[j] = subtypes[j].getClass().getName();
					}
			}else{
					keyProperties = new String[1];
					keyProperties[0] = aClassMetadata.getIdentifierPropertyName();
					keyPropertiesType = new String[1];
					keyPropertiesType[0] = aType.getClass().getName();
			}
			    	
			for (int j = 0; j < keyProperties.length; j++) {

				Logger.debug(this.getClass(),"keyProperties[" + j + "] "
						+ keyProperties[j]);
				nodeCounter++;
				completeFieldName = keyProperties[j];

				
				if (prefix != null) {
					completeFieldName = prefix + "." + keyProperties[j];
				}

				 // unique name of the field for label resolving procedure
				String cn = fieldUrlGenerator.getClassName();
				cn = (cn.lastIndexOf('.') > 0 ?
					  cn.substring(cn.lastIndexOf('.') + 1 , cn.length()) :
					  cn);
						
				String completeFieldRef = cn + "." + completeFieldName;
				
				String fldLabel = JsTreeUtils.getLabelForField(dataMartModel, completeFieldRef);
				if(fldLabel.equalsIgnoreCase(completeFieldRef)) {
					fldLabel = JsTreeUtils.getLabelForField(dataMartModel, keyProperties[j]);
				}
				
				
				fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel,  keyPropertiesType[j]);
				
				if(checkable) {
					String selected = "";
					if(selectedNodes.containsKey(new  QbeJsTreeNodeId(className, completeFieldName, classPrefix).getId())) selected = "true";
					
					String img = "/img/key.gif";
					
					
					addNode("" + nodeCounter, "" + idxClassNode, 
							JsTreeUtils.getLabelForField(dataMartModel, completeFieldRef),
							fieldAction,  
							JsTreeUtils.getLabelForField(dataMartModel, completeFieldRef), 
							"_self",
							qbeUrlGenerator.getResourceUrl(httpRequest,img),
							qbeUrlGenerator.getResourceUrl(httpRequest,img),
							"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);	
				}
				else {
					addNode("" + nodeCounter, "" + idxClassNode, 
							JsTreeUtils.getLabelForField(dataMartModel, completeFieldRef),
							fieldAction,  
							JsTreeUtils.getLabelForField(dataMartModel, completeFieldRef), 
							"_self",
							qbeUrlGenerator.getResourceUrl(httpRequest,"../img/key.gif"),
							qbeUrlGenerator.getResourceUrl(httpRequest,"../img/key.gif"),
							"", "", "", "", "");	
				}
			}
			
				
			
			for(int i=0; i < metaPropertyNames.length; i++){
			 	aHibType = (Type)aClassMetadata.getPropertyType(metaPropertyNames[i]);
			 
			 	if (aHibType instanceof ManyToOneType){
			 		completeFieldName = metaPropertyNames[i];
			 		
			 		if (prefix != null){
			 			 completeFieldName = prefix +"." + metaPropertyNames[i];
			 		}
			 		RelationField aRelationField = new RelationField( completeFieldName, ((ManyToOneType)aHibType).getAssociatedEntityName()); 
			 		
			 		associatedClassesArrayList.add(aRelationField);																   
			 	}else {
			 		if (aHibType instanceof CollectionType) {
						//System.out.println(" Collection type skip........");
					}else{
						Logger.debug(this.getClass()," HibType Class" + aHibType.getClass());
						nodeCounter++;
						completeFieldName = metaPropertyNames[i];
			 		
			 		
						if (prefix != null){
							completeFieldName = prefix +"." + metaPropertyNames[i];
						}
			 		
						String fldLabel = JsTreeUtils.getLabelForField(dataMartModel, metaPropertyNames[i]);
						fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel, aHibType.getClass().getName());
			 		
			 		
						if(checkable) {
							String selected = "";
							if(selectedNodes.containsKey(new  QbeJsTreeNodeId(className, completeFieldName).getId())) selected = "true";
						
							if(selected.equalsIgnoreCase("true"))
								System.out.println("check[" + className + "," + completeFieldName + "]: " 
										+ (new  QbeJsTreeNodeId(className, completeFieldName).getId()) + " -> " + selected.toUpperCase());
						
						
							addNode("" + nodeCounter, "" + idxClassNode, 
									fldLabel,
									fieldAction,  
									fldLabel, 
									"_self",
									qbeUrlGenerator.getResourceUrl(httpRequest,"../img/Method.gif"),
									qbeUrlGenerator.getResourceUrl(httpRequest,"../img/Method.gif"),
									"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);		
						} else {
							addNode("" + nodeCounter, "" + idxClassNode, 
				 				fldLabel,
								fieldAction,  
								fldLabel, 
								"_self",
								qbeUrlGenerator.getResourceUrl(httpRequest,"../img/Method.gif"),
								qbeUrlGenerator.getResourceUrl(httpRequest,"../img/Method.gif"),
								"", "", "",  "", "");
						}
					}
			 	}
			}
			//
			// Aggiungo i campi Calcolati per la classe
			//
			if (fieldUrlGenerator instanceof SelectFieldForSelectionURLGenerator){
				List manualCalcultatedFieldForEntity = dataMartModel.getFormula().getManualCalculatedFieldsForEntity(className);
				
				CalculatedField cField = null;
				String cFieldAction = null;
				for (Iterator itManualCalculatedFields=manualCalcultatedFieldForEntity.iterator(); itManualCalculatedFields.hasNext();){
					if (prefix != null){
						cField.setFldCompleteNameInQuery(prefix + "." + cField.getId());
					}else{
						cField.setFldCompleteNameInQuery(cField.getId());
					}
					cFieldAction = ((SelectFieldForSelectionURLGenerator)fieldUrlGenerator).generateURLForCalculateField(cField.getId(), className, cField.getFldCompleteNameInQuery());
					nodeCounter++;
					addNode("" + nodeCounter, "" + idxClassNode, 
							cField.getFldLabel(),
							cFieldAction,  
							cField.getFldLabel(),
							"_self",
							qbeUrlGenerator.getResourceUrl(httpRequest,"../img/cfield.gif"),
							qbeUrlGenerator.getResourceUrl(httpRequest,"../img/cfield.gif"),
							"", "", "",  "", "");
				}
				
			}
			
			QbeTreeRelations fields = new QbeTreeRelations();
			fields.addAllRelations(associatedClassesArrayList);
			List sortedList = fields.getFieldsOrderedByLabel();
			
			Iterator associatedClassIterator = sortedList.iterator();
			while (associatedClassIterator.hasNext()){
				RelationField aRelationField = (RelationField)associatedClassIterator.next();
				if (aRelationField.getClassName().equalsIgnoreCase(className)){
					continue;
				}else{
					nodeCounter = addFieldNodes(aRelationField.getClassName(), null, idxClassNode, nodeCounter, aRelationField.getFieldName(), fieldUrlGenerator,recursionLevel+1);
				}
			}
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return nodeCounter;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.javascript.BaseJsTreeBuilder#addHeader()
	 */
	public void addHeader() {
		Map params = new HashMap();		
		params.put("ACTION_NAME", actionName);
		if(checkable) {
			String url = qbeUrlGenerator.getActionUrl(httpRequest, params);
			buffer.append("<form method='POST' action='" + url + "' id ='treeForm' name='treeForm'>");
		}
		super.addHeader();
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.javascript.BaseJsTreeBuilder#addFooter()
	 */
	public void addFooter() {
		super.addFooter();
		if(checkable) {
			buffer.append("<input type=\"submit\" value=\"Update\">");
			buffer.append("</form>");
		}
	}

	/**
	 * Gets the modality.
	 * 
	 * @return the modality
	 */
	public String getModality() {
		return modality;
	}

	/**
	 * Sets the modality.
	 * 
	 * @param modality the new modality
	 */
	public void setModality(String modality) {
		this.modality = modality;
	}

	/**
	 * Checks if is checkable.
	 * 
	 * @return true, if is checkable
	 */
	public boolean isCheckable() {
		return checkable;
	}

	/**
	 * Sets the checkable.
	 * 
	 * @param checkable the new checkable
	 */
	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
	}
	
	/**
	 * The Class QbeTreeFields.
	 */
	private class QbeTreeFields {

		/** The list. */
		private List list;
		
		/**
		 * Instantiates a new qbe tree fields.
		 */
		QbeTreeFields() {
			list = new ArrayList();
		}
		
		/**
		 * Adds the field.
		 * 
		 * @param hibernateClassName the hibernate class name
		 */
		void addField(String hibernateClassName) {
			String label = 
				JsTreeUtils.getLabelForClass(dataMartModel, hibernateClassName);
			QbeTreeField field = new QbeTreeField(label, hibernateClassName);
			list.add(field);
		}
		
		/**
		 * Adds the all fields.
		 * 
		 * @param hibernateClassNames the hibernate class names
		 */
		void addAllFields(Set hibernateClassNames) {
			if (hibernateClassNames != null && hibernateClassNames.size() > 0) {
				Iterator it = hibernateClassNames.iterator();
				while (it.hasNext()) {
					String hibernateClassName = (String) it.next();
					addField(hibernateClassName);
				}
			}
		}
		
		/**
		 * Adds the all fields.
		 * 
		 * @param hibernateClassNames the hibernate class names
		 */
		void addAllFields(List hibernateClassNames) {
			if (hibernateClassNames != null && hibernateClassNames.size() > 0) {
				Iterator it = hibernateClassNames.iterator();
				while (it.hasNext()) {
					String hibernateClassName = (String) it.next();
					addField(hibernateClassName);
				}
			}
		}
		
		/**
		 * Gets the fields ordered by label.
		 * 
		 * @return the fields ordered by label
		 */
		List getFieldsOrderedByLabel () {
			Collections.sort(list);
			List toReturn = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				QbeTreeField field = (QbeTreeField) it.next();
				toReturn.add(field.getHibernateClassName());
			}
			return toReturn;
		}
		
	}
	
	/**
	 * The Class QbeTreeField.
	 */
	private class QbeTreeField implements Comparable {
		
		/** The hibernate class name. */
		private String hibernateClassName;
		
		/** The label. */
		private String label;
		
		/**
		 * Instantiates a new qbe tree field.
		 * 
		 * @param label the label
		 * @param hibernateClassName the hibernate class name
		 */
		QbeTreeField (String label, String hibernateClassName) {
			this.hibernateClassName = hibernateClassName;
			this.label = label;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			if (o == null) throw new NullPointerException();
			if (!(o instanceof QbeTreeField)) throw new ClassCastException();
			QbeTreeField anotherField = (QbeTreeField) o;
			return this.getLabel().compareTo(anotherField.getLabel());
		}
		
		/**
		 * Gets the hibernate class name.
		 * 
		 * @return the hibernate class name
		 */
		public String getHibernateClassName() {
			return hibernateClassName;
		}
		
		/**
		 * Sets the hibernate class name.
		 * 
		 * @param hibernateClassName the new hibernate class name
		 */
		public void setHibernateClassName(String hibernateClassName) {
			this.hibernateClassName = hibernateClassName;
		}
		
		/**
		 * Gets the label.
		 * 
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		
		/**
		 * Sets the label.
		 * 
		 * @param label the new label
		 */
		public void setLabel(String label) {
			this.label = label;
		}
		
	}
	
	/**
	 * The Class QbeTreeRelations.
	 */
	private class QbeTreeRelations {

		/** The list. */
		private List list;
		
		/**
		 * Instantiates a new qbe tree relations.
		 */
		QbeTreeRelations() {
			list = new ArrayList();
		}
		
		/**
		 * Adds the relation.
		 * 
		 * @param relationField the relation field
		 */
		void addRelation(RelationField relationField) {
			String label = null;
			String classLabel = JsTreeUtils.getLabelForClass(dataMartModel, relationField.getClassName());
			String labelForRelation =
				JsTreeUtils.getLabelForForeignKey(dataMartModel, relationField.getClassName() + "(" + relationField.getRelationOnColumnName() + ")");
			if (labelForRelation != null){
				label = labelForRelation;
			} else {
				label = classLabel + "(" + relationField.getRelationOnColumnName() + ")";
			}
			QbeTreeRelation field = new QbeTreeRelation(label, relationField);
			list.add(field);
		}
		
		/**
		 * Adds the all relations.
		 * 
		 * @param relations the relations
		 */
		void addAllRelations(Set relations) {
			if (relations != null && relations.size() > 0) {
				Iterator it = relations.iterator();
				while (it.hasNext()) {
					RelationField relation = (RelationField) it.next();
					addRelation(relation);
				}
			}
		}
		
		/**
		 * Adds the all relations.
		 * 
		 * @param relations the relations
		 */
		void addAllRelations(List relations) {
			if (relations != null && relations.size() > 0) {
				Iterator it = relations.iterator();
				while (it.hasNext()) {
					RelationField relation = (RelationField) it.next();
					addRelation(relation);
				}
			}
		}
		
		/**
		 * Gets the fields ordered by label.
		 * 
		 * @return the fields ordered by label
		 */
		List getFieldsOrderedByLabel () {
			Collections.sort(list);
			List toReturn = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				QbeTreeRelation field = (QbeTreeRelation) it.next();
				toReturn.add(field.getRelationField());
			}
			return toReturn;
		}
		
	}
	
	
	/**
	 * The Class QbeTreeRelation.
	 */
	private class QbeTreeRelation implements Comparable {
		
		/** The relation field. */
		private RelationField relationField;
		
		/** The label. */
		private String label;
		
		/**
		 * Instantiates a new qbe tree relation.
		 * 
		 * @param label the label
		 * @param relationField the relation field
		 */
		QbeTreeRelation (String label, RelationField relationField) {
			this.relationField = relationField;
			this.label = label;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			if (o == null) throw new NullPointerException();
			if (!(o instanceof QbeTreeRelation)) throw new ClassCastException();
			QbeTreeRelation anotherField = (QbeTreeRelation) o;
			return this.getLabel().compareTo(anotherField.getLabel());
		}
		
		/**
		 * Gets the relation field.
		 * 
		 * @return the relation field
		 */
		public RelationField getRelationField() {
			return relationField;
		}
		
		/**
		 * Sets the relation field.
		 * 
		 * @param relationField the new relation field
		 */
		public void setRelationField(RelationField relationField) {
			this.relationField = relationField;
		}
		
		/**
		 * Gets the label.
		 * 
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		
		/**
		 * Sets the label.
		 * 
		 * @param label the new label
		 */
		public void setLabel(String label) {
			this.label = label;
		}
		
	}
}
