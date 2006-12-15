/**
 * 
 */
package it.eng.spagobi.utilities.javascript;

import it.eng.qbe.javascript.IURLGenerator;
import it.eng.qbe.javascript.SelectFieldForSelectionURLGenerator;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.IQbeUrlGenerator;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.PortletQbeUrlGenerator;
import it.eng.qbe.utility.RelationField;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.ApplicationContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

/**
 * @author Gioia
 *
 */
public abstract class QbeJsTreeBuilder extends BaseJsTreeBuilder {

	DataMartModel dataMartModel = null;
	ISingleDataMartWizardObject dataMartWizard = null;
	Map selectedNodes = null;
	
	HttpServletRequest httpRequest = null; 
	IQbeUrlGenerator qbeUrlGenerator = null;
	
	String actionName = null;
	
	String modality = DEFAULT_MODALITY;
	boolean checkable = false;
	
	public static final String FULL_MODALITY = "FULL";
	public static final String LIGHT_MODALITY = "LIGHT";
	public static final String DEFAULT_MODALITY = FULL_MODALITY;
	
	private String classPrefix = null;
	/**
	 * @param dataMartModel : The DatamartModel object reperesenting the datamart we're working on
	 * @param httpRequest : the httpRequest Object
	 */
	public QbeJsTreeBuilder(DataMartModel dataMartModel, ISingleDataMartWizardObject dataMartWizard, HttpServletRequest httpRequest){
		this.dataMartModel = dataMartModel;
		this.dataMartWizard = dataMartWizard;
		this.httpRequest = httpRequest;
		
		selectedNodes = getSelectdNodes();
		
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");   
		
		if (qbeMode.equalsIgnoreCase("WEB")){
			qbeUrlGenerator = new it.eng.qbe.utility.WebQbeUrlGenerator();
		} else if  (qbeMode.equalsIgnoreCase("PORTLET")){
			qbeUrlGenerator = new PortletQbeUrlGenerator();
		}		
	}
	
	public abstract Map getSelectdNodes();
	
	
	public String build() {		
		buffer = new StringBuffer();
		
		addHeader();
		addTree();				
		addRootNode();
		addNodes();				
		addFooter();
		
		return buffer.toString();
	}
	
	public void addRootNode() {
		addNode("0", "-1", dataMartModel.getName(), "", "", dataMartModel.getName(), 
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif"),
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif"),
				"", "", "", "", "");
	}
	
	public abstract void addNodes();
	
		
	private SessionFactory getHibernateSession() {
		SessionFactory sf = null;
		Logger.debug(this.getClass(), "writeSelectionTree: start method writeSelectionTree");
		ApplicationContainer application = ApplicationContainer.getInstance();
		Logger.debug(this.getClass(), "writeSelectionTree: application container retrived: " + application);
		sf = Utils.getSessionFactory(dataMartModel,application);
		Logger.debug(this.getClass(), "writeSelectionTree: session factory retrived: " + sf);
		if(sf == null){
			Logger.critical(this.getClass(), "writeSelectionTree: session factory NULL");
		}
		return sf;
	}
	
	protected Collection getClassNames() {
		if(getHibernateSession().getAllClassMetadata() == null){
			Logger.critical(this.getClass(), "writeSelectionTree: map metadata classes NULL");
			return null;
		}
		Logger.debug(this.getClass(), "writeSelectionTree: metadata class map retrived successfully");
		return getHibernateSession().getAllClassMetadata().keySet();
	}
	
	protected Collection getSelectedClassNames() {
		Set selectedClassNames = null;
		
		List list  = dataMartWizard.getEntityClasses();		
		if(list.size() > 0) {
			selectedClassNames = new HashSet();
			EntityClass ec = null;	
					
			for (Iterator it = list.iterator(); it.hasNext(); ){
				ec  = (EntityClass)it.next();			
				selectedClassNames.add(ec.getClassName());
			}		
		}
		return selectedClassNames;	 
	}
		
	
	/**
	 * Recursive Function To write the tree of model navigable  starting by className
	 * @param buffer
	 * @param name
	 * @param className
	 * @param rootNode
	 * @param nodeCounter
	 * @param prefix
	 * @param fieldUrlGenerator
	 * @return
	 */
	public final int addFieldNodes (String className, String relationOnColumnName, int rootNode, int nodeCounter, String prefix, IURLGenerator fieldUrlGenerator, int recursionLevel){
		
		
		nodeCounter++;
		
		PersistentClass pc = dataMartModel.getHibCfg().getClassMapping(className);
		String classLabel = Utils.getLabelForClass(Utils.getRequestContainer(httpRequest), dataMartModel, className);
		
		if (relationOnColumnName != null){
			
			String labelForRelation = Utils.getLabelForForeignKey(Utils.getRequestContainer(httpRequest), dataMartModel,className+"("+relationOnColumnName+")");
			if (labelForRelation != null){
				classLabel = labelForRelation;
			}else{
				classLabel += "("+ relationOnColumnName +")";
			}
		}
		// add class node
		addNode("" + nodeCounter, "" + rootNode, classLabel, "", "", classLabel, 
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif"),
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif"),
				"", "", "", "", "");
		
		
		int idxClassNode = nodeCounter;
		
		// Genero i field
		try{
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
			
			ClassMetadata aClassMetadata = sf.getClassMetadata(className);
			
			String[] metaPropertyNames = aClassMetadata.getPropertyNames();
			
			List associatedClassesArrayList = new ArrayList();
			
			Type aHibType = null;
			 	

			Type aType = aClassMetadata.getIdentifierType();
			
			String completeFieldName = "";
			String fieldAction = "";
			String[] keyProperties = null;
			String[] keyPropertiesType = null;
			String[] hibTypes  = null;
			String[] hibScale  = null;
			String[] hibPrecision = null;
			if (aType.isComponentType()){
					
					
					String idPropertyName = aClassMetadata.getIdentifierPropertyName();
					String[] tmpKeyProperties = ((ComponentType)aType).getPropertyNames();
					
					
					Type[] subtypes = ((ComponentType)aType).getSubtypes();
					keyProperties = new String[tmpKeyProperties.length];
					keyPropertiesType = new String[tmpKeyProperties.length];
					hibTypes  = new String[tmpKeyProperties.length];
					hibScale  = new String[tmpKeyProperties.length];
					hibPrecision = new String[tmpKeyProperties.length];
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
					hibScale = new String[1];
					hibPrecision = new String[1];
					
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
			    	
			for (int j = 0; j < keyProperties.length; j++) {

				Logger.debug(this.getClass(),"keyProperties[" + j + "] "
						+ keyProperties[j]);
				nodeCounter++;
				completeFieldName = keyProperties[j];

				
				if (prefix != null) {
					completeFieldName = prefix + "." + keyProperties[j];
				}
				
				String fldLabel = Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName);
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
				
				
				
				if(checkable) {
					String selected = "";
					if(selectedNodes.containsKey(new  QbeJsTreeNodeId(className, completeFieldName, classPrefix).getId())) selected = "true";
					
					String img = "/img/key.gif";
					
					
					addNode("" + nodeCounter, "" + idxClassNode, 
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName),
							fieldAction,  
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName), 
							"_self",
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,img),
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,img),
							"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);	
				}
				else {
					addNode("" + nodeCounter, "" + idxClassNode, 
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName),
							fieldAction,  
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName), 
							"_self",
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif"),
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif"),
							"", "", "", "", "");	
				}
			}
			
				
			
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
					Logger.debug(this.getClass()," Collection type skip........");
				}else{
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
						
						String fldLabel = Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, metaPropertyNames[i]);
						
						//
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
									qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
									qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
									"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);		
						} else {
							addNode("" + nodeCounter, "" + idxClassNode, 
				 				fldLabel,
								fieldAction,  
								fldLabel, 
								"_self",
								qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
								qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
								"", "", "",  "", "");
						}
					}
			 	}
			
			
			//
			// Add Calculate Fields on the entity
			// Control recursion level because calculate field are applied only at etity level not in dimension level
			//
			if (/*(recursionLevel == 1) &&*/ (fieldUrlGenerator instanceof SelectFieldForSelectionURLGenerator)){
				List manualCalcultatedFieldForEntity = Utils.getManualCalculatedFieldsForEntity(className, dataMartModel.getJarFile().getParent());
				
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
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/cfield.gif"),
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/cfield.gif"),
							"", "", "",  "", "");
				}
				
			}
			
			Iterator associatedClassIterator = associatedClassesArrayList.iterator();
			while (associatedClassIterator.hasNext()){
				RelationField aRelationField = (RelationField)associatedClassIterator.next();
				if (aRelationField.getClassName().equalsIgnoreCase(className)){
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
	
	
	public final int addFieldNodesNoRecursion (String className, String relationFieldName, int rootNode, int nodeCounter, String prefix, IURLGenerator fieldUrlGenerator, int recursionLevel){
		
		
		nodeCounter++;
		
		
		String classLabel = Utils.getLabelForClass(Utils.getRequestContainer(httpRequest), dataMartModel, className);
		
		// add class node
		addNode("" + nodeCounter, "" + rootNode, classLabel, "", "", classLabel, 
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif"),
				qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif"),
				"", "", "", "", "");
		
		
		int idxClassNode = nodeCounter;
		
		// Genero i field
		try{
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
			
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
				//
				String fldLabel = Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName);
				fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel,  keyPropertiesType[j]);
				
				if(checkable) {
					String selected = "";
					if(selectedNodes.containsKey(new  QbeJsTreeNodeId(className, completeFieldName, classPrefix).getId())) selected = "true";
					
					String img = "/img/key.gif";
					
					
					addNode("" + nodeCounter, "" + idxClassNode, 
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName),
							fieldAction,  
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName), 
							"_self",
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,img),
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,img),
							"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);	
				}
				else {
					addNode("" + nodeCounter, "" + idxClassNode, 
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName),
							fieldAction,  
							Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName), 
							"_self",
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif"),
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif"),
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
						System.out.println(" Collection type skip........");
					}else{
						Logger.debug(this.getClass()," HibType Class" + aHibType.getClass());
						nodeCounter++;
						completeFieldName = metaPropertyNames[i];
			 		
			 		
						if (prefix != null){
							completeFieldName = prefix +"." + metaPropertyNames[i];
						}
			 		
						String fldLabel = Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, metaPropertyNames[i]);
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
									qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
									qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
									"", "", "selectItem",  className + ";" + completeFieldName + ";" + fldLabel, selected);		
						} else {
							addNode("" + nodeCounter, "" + idxClassNode, 
				 				fldLabel,
								fieldAction,  
								fldLabel, 
								"_self",
								qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
								qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif"),
								"", "", "",  "", "");
						}
					}
			 	}
			}
			//
			// Aggiungo i campi Calcolati per la classe
			//
			if (fieldUrlGenerator instanceof SelectFieldForSelectionURLGenerator){
				List manualCalcultatedFieldForEntity = Utils.getManualCalculatedFieldsForEntity(className, dataMartModel.getJarFile().getParent());
				
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
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/cfield.gif"),
							qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/cfield.gif"),
							"", "", "",  "", "");
				}
				
			}
			
			Iterator associatedClassIterator = associatedClassesArrayList.iterator();
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
	
	public void addHeader() {
		Map params = new HashMap();		
		params.put("ACTION_NAME", actionName);
		if(checkable) {
			String url = qbeUrlGenerator.getUrl(httpRequest, params);
			buffer.append("<form method='POST' action='" + url + "' id ='treeForm' name='treeForm'>");
		}
		super.addHeader();
	}
	
	public void addFooter() {
		super.addFooter();
		if(checkable) {
			buffer.append("<input type=\"submit\" value=\"Update\">");
			buffer.append("</form>");
		}
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public boolean isCheckable() {
		return checkable;
	}

	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
	}
}
