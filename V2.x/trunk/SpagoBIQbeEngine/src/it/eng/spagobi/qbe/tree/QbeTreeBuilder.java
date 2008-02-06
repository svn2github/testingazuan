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
package it.eng.spagobi.qbe.tree;

import it.eng.qbe.datasource.BasicHibernateDataSource;
import it.eng.qbe.datasource.CompositeHibernateDataSource;
import it.eng.qbe.javascript.QbeJsTreeNodeId;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.urlgenerator.IURLGenerator;
import it.eng.qbe.urlgenerator.SelectFieldForSelectionURLGenerator;
import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.JsTreeUtils;
import it.eng.qbe.utility.QbeProperties;
import it.eng.qbe.utility.RelationField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.PortletQbeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.WebQbeUrlGenerator;
import it.eng.spagobi.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.qbe.tree.presentation.tag.DatamartImageFactory;
import it.eng.spagobi.qbe.tree.presentation.tag.DatamartLabelFactory;
import it.eng.spagobi.qbe.tree.urlgenerator.IQbeTreeUrlGenerator;

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
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QbeTreeBuilder  {
	
	private Class qbeTreeClass;
	private IQbeTreeUrlGenerator urlGenerator;	
	private QbeTreeFilter qbeTreeFilter;
	
	private IDataMartModel datamartModel;	
	
	String modality = DEFAULT_MODALITY;	
	public static final String FULL_MODALITY = "FULL";
	public static final String LIGHT_MODALITY = "LIGHT";
	public static final String DEFAULT_MODALITY = FULL_MODALITY;
	
	private String classPrefix = null;	
	
	
	public QbeTreeBuilder(String className, IQbeTreeUrlGenerator urlGenerator, QbeTreeFilter qbeTreeFilter) throws ClassNotFoundException {		
		setQbeTreeClass( Class.forName(className) );	
		setUrlGenerator(urlGenerator);
		setQbeTreeFilter( qbeTreeFilter );
	}	
	
		
	public List getQbeTrees(IDataMartModel datamartModel) throws InstantiationException, IllegalAccessException {			
		setDatamartModel(datamartModel);			
		return buildQbeTreeList();
	}

	private List buildQbeTreeList() throws InstantiationException, IllegalAccessException {	
		List list = new ArrayList();
		
		List dmNames = datamartModel.getDataSource().getDatamartNames();		
		for(int i = 0; i < dmNames.size(); i++) {
			String dmName = (String)dmNames.get(i);
			IQbeTree tree = buildQbeTree(dmName);
			list.add(tree);
			
		}	
		
		/*
		targetDatamartName = "Views";
		name = baseName + "_" + dmNames.size();
		if(getHibernateSession() != null && getClassNames().size() > 0) {
			String treeScript = buildQbeTree();
			treeScripts.put(targetDatamartName, treeScript);
		}	
		*/	
		
		
		return list;
	}
	
	
	
	private IQbeTree buildQbeTree(String datamartName) throws InstantiationException, IllegalAccessException {			
		IQbeTree tree = (IQbeTree)qbeTreeClass.newInstance();	
		
		// è importante non rischiare di creare due alberi con lo stesso nome. 
		//La cosa infatti crea casino quando gli alberi si trovano sulla stessa pagina
		tree.createTree(datamartName + "_" + System.currentTimeMillis());		
		addRootNode(tree, datamartName);
		addEntityNodes(tree, datamartName);
			
		
		/*
		for (Iterator it = getClassNames(datamartName).iterator(); it.hasNext(); ){
			String className = (String)it.next();
			if(selectedNodes.containsKey(className)) {
				getUrlGenerator().setClassName(className);
				nodeCounter = addEntityNode(tree, className, 
											null, 
											0, 
											nodeCounter,
											null, 1);
			}
		}	
	*/
		
		return tree;
	}
	
	
	public void addRootNode(IQbeTree tree, String rootNodeName) {

		tree.addNode("0", "-1", rootNodeName, "", "", rootNodeName, 
				getUrlGenerator().getResourceUrl("../img/base.gif"),
				getUrlGenerator().getResourceUrl("../img/base.gif"),
				"", "", "", "", "");
	}
	
	public void addEntityNodes(IQbeTree tree, String datamartName) {
		int nodeCounter = 0;
		
		List entities = getDatamartModel().getDataMartModelStructure().getRootEntities(datamartName);
		entities = qbeTreeFilter.filterEntities(getDatamartModel(), entities);
		
		Iterator it = entities.iterator();
		while(it.hasNext()) {
			DataMartEntity entity = (DataMartEntity)it.next();
			
			nodeCounter = addEntityNode(tree, entity, 
										0, /* rootNode */
										nodeCounter,
										1);
		}
	}
	
	
	public int addEntityNode(IQbeTree tree, 
							 DataMartEntity entity,
							 int rootNodeId, int nodeCounter, 
							 int recursionLevel) {
		
		int entityNodeId;		
				
		entityNodeId = addEntityRootNode(tree, entity, rootNodeId, nodeCounter);		
		nodeCounter  = addFieldNodes(tree, entity, entityNodeId, entityNodeId + 1);
		nodeCounter  = addCalculatedFieldNodes(tree, entity, entityNodeId, nodeCounter);
		nodeCounter  = addSubEntitiesNodes(tree, entity, entityNodeId, nodeCounter, recursionLevel, entity.getSubEntities());
				
		return nodeCounter;	
	}
		
	
	public int addEntityRootNode(IQbeTree tree,
								 DataMartEntity entity,
								 int rootNodeId, int nodeCounter) {		
		
		String label = DatamartLabelFactory.getEntityLabel(datamartModel, entity);	
		String image = DatamartImageFactory.getEntityImage(datamartModel, entity);			
		
		nodeCounter++;
		tree.addNode("" + nodeCounter, 
					 "" + rootNodeId, 
					 label, 
					 "", 
					 "", 
					 entity.getUniqueName(), 
					 getUrlGenerator().getResourceUrl(image),
					 getUrlGenerator().getResourceUrl(image),
					 "", "", "", "", "");
		
		return nodeCounter;
	}
	
	public int addFieldNodes(IQbeTree tree,
			 				 DataMartEntity entity,
							 int parentEntityNodeId, int nodeCounter) {		
		
		// add key fields
		List keyFields = entity.getKeyFields();
		keyFields = qbeTreeFilter.filterFields(getDatamartModel(), keyFields);
		
		Iterator keyFieldIterator = keyFields.iterator();
		while (keyFieldIterator.hasNext() ) {
			DataMartField field = (DataMartField)keyFieldIterator.next();
			nodeCounter = addFieldNode(tree, field, parentEntityNodeId, nodeCounter);
		}
		
		// add normal fields
		List normalFields = entity.getNormalFields();
		normalFields = qbeTreeFilter.filterFields(getDatamartModel(), normalFields);
		
		Iterator normalFieldIterator = normalFields.iterator();
		while (normalFieldIterator.hasNext() ) {
			DataMartField field = (DataMartField)normalFieldIterator.next();
			nodeCounter = addFieldNode(tree, field, parentEntityNodeId, nodeCounter);
		}
		
		return nodeCounter;		
	}
	
	public  int addFieldNode(IQbeTree tree,
							 DataMartField field,
			 				 int parentEntityNodeId, int nodeCounter) {
		
		String label = DatamartLabelFactory.getFieldLabel(getDatamartModel(), field);			
		String image = DatamartImageFactory.getFieldImage(getDatamartModel(), field);				
		String action = getUrlGenerator().getActionUrl(field);
					
		nodeCounter++;					
		tree.addNode("" + nodeCounter, 
					 "" + parentEntityNodeId, 
					label,
					action,  
					field.getUniqueName(), 
					"_self",
					getUrlGenerator().getResourceUrl(image),
					getUrlGenerator().getResourceUrl(image),
					"", "", "", "", "");	
		
		return nodeCounter;
	}
	
	
	/**
	 * Add Calculate Fields on the entity
 	 * Control recursion level because calculate field are applied only at entity level not in dimension level
 	 * 
	 * @param tree
	 * @param prefix
	 * @param className
	 * @param parentEntityNodeId
	 * @param nodeCounter
	 * @return
	 */
	public int addCalculatedFieldNodes(IQbeTree tree, 
			   						   DataMartEntity entity,
			   						   int parentEntityNodeId, int nodeCounter) {
			
		List manualCalcultatedFieldForEntity = 
			getDatamartModel().getDataSource().getFormula().getManualCalculatedFieldsForEntity( entity.getType() );
			
		CalculatedField calculatedField = null;
		String fieldAction = null;
		
		Iterator manualCalculatedFieldsIterator = manualCalcultatedFieldForEntity.iterator();
		while (manualCalculatedFieldsIterator.hasNext()){
			calculatedField = (CalculatedField)manualCalculatedFieldsIterator.next();
			
			/*
			if (prefix != null){
				calculatedField.setFldCompleteNameInQuery(prefix + "." + calculatedField.getId());
			}else{
				calculatedField.setFldCompleteNameInQuery(calculatedField.getId());
			}
			*/
			
			fieldAction = getUrlGenerator().getActionUrlForCalculateField(calculatedField.getId(), entity.getName(), calculatedField.getFldCompleteNameInQuery());
			
			nodeCounter++;
			tree.addNode("" + nodeCounter, "" + parentEntityNodeId, 
					calculatedField.getFldLabel(),
					fieldAction,  
					calculatedField.getFldLabel(),
					"_self",
					getUrlGenerator().getResourceUrl("../img/cfield.gif"),
					getUrlGenerator().getResourceUrl("../img/cfield.gif"),
					"", "", "",  "", "");
		}
			
		return nodeCounter;
	}
	
	
	public int addSubEntitiesNodes(IQbeTree tree,
								   DataMartEntity entity,
								   int parentEntityNodeId, int nodeCounter,
								   int recursionLevel, List subEntities) {
		
		subEntities = qbeTreeFilter.filterEntities(getDatamartModel(), subEntities);
		
		Iterator subEntitiesIterator = subEntities.iterator();
		while (subEntitiesIterator.hasNext()){
			DataMartEntity subentity = (DataMartEntity)subEntitiesIterator.next();
			if (subentity.getType().equalsIgnoreCase( entity.getType() ) || recursionLevel > 3) {
				/* stop recursion */
			} else {
				nodeCounter = addEntityNode(tree, subentity,
											parentEntityNodeId, nodeCounter, 
											recursionLevel+1);
			}
		}
		
		return nodeCounter;
	}
	
	
	
	
	

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	protected IDataMartModel getDatamartModel() {
		return datamartModel;
	}

	protected void setDatamartModel(IDataMartModel datamartModel) {
		this.datamartModel = datamartModel;
	}


	public IQbeTreeUrlGenerator getUrlGenerator() {
		return urlGenerator;
	}


	public void setUrlGenerator(IQbeTreeUrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}


	protected Class getQbeTreeClass() {
		return qbeTreeClass;
	}


	protected void setQbeTreeClass(Class qbeTreeClass) {
		this.qbeTreeClass = qbeTreeClass;
	}


	private QbeTreeFilter getQbeTreeFilter() {
		return qbeTreeFilter;
	}


	private void setQbeTreeFilter(QbeTreeFilter qbeTreeFilter) {
		this.qbeTreeFilter = qbeTreeFilter;
	}
}
