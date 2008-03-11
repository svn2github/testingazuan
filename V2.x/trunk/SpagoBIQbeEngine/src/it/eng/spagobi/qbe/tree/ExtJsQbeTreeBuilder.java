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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ExtJsQbeTreeBuilder  {
	
	private Class qbeTreeClass;
	private IQbeTreeUrlGenerator urlGenerator;	
	private QbeTreeFilter qbeTreeFilter;
	
	private IDataMartModel datamartModel;	
	
	String modality = DEFAULT_MODALITY;	
	public static final String FULL_MODALITY = "FULL";
	public static final String LIGHT_MODALITY = "LIGHT";
	public static final String DEFAULT_MODALITY = FULL_MODALITY;
	
	private String classPrefix = null;	
	
	
	public ExtJsQbeTreeBuilder(QbeTreeFilter qbeTreeFilter)  {		
		setQbeTreeFilter( qbeTreeFilter );
	}	
	
		
	public List getQbeTrees(IDataMartModel datamartModel)  {			
		setDatamartModel(datamartModel);			
		return buildQbeTreeList();
	}

	private List buildQbeTreeList()  {	
		List list = new ArrayList();
		
		List dmNames = datamartModel.getDataSource().getDatamartNames();		
		for(int i = 0; i < dmNames.size(); i++) {
			String dmName = (String)dmNames.get(i);
			JSONArray tree = buildQbeTree(dmName);
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
	
	
	
	private JSONArray buildQbeTree(String datamartName)  {			
		JSONArray nodes = new JSONArray();	
		addEntityNodes(nodes, datamartName);	
		return nodes;
	}
	
	
	public void addEntityNodes(JSONArray nodes, String datamartName) {
		int nodeCounter = 0;
		
		List entities = getDatamartModel().getDataMartModelStructure().getRootEntities(datamartName);
		entities = qbeTreeFilter.filterEntities(getDatamartModel(), entities);
		
		Iterator it = entities.iterator();
		while(it.hasNext()) {
			DataMartEntity entity = (DataMartEntity)it.next();			
			addEntityNode(nodes, entity, 1);
		}
	}
	
	
	public void addEntityNode(JSONArray nodes, 
							 DataMartEntity entity,
							 int recursionLevel) {
		
			
		addEntityRootNode(nodes, entity, recursionLevel);		
		
		/*
		addFieldNodes(nodes, entity);
		nodeCounter  = addCalculatedFieldNodes(tree, entity, entityNodeId, nodeCounter);
		nodeCounter  = addSubEntitiesNodes(tree, entity, entityNodeId, nodeCounter, recursionLevel, entity.getSubEntities());
				
		return nodeCounter;	
		*/
	}
		
	
	public void addEntityRootNode(JSONArray nodes,
								  DataMartEntity entity,
								  int recursionLevel) {		
		
		String label = DatamartLabelFactory.getEntityLabel(datamartModel, entity);	
		String image = DatamartImageFactory.getEntityImage(datamartModel, entity);			
		String iconCls = null;
		
		QbeProperties qbeProperties; 
		
		qbeProperties = new QbeProperties(datamartModel);		
		int entityType = qbeProperties.getTableType( entity.getType() );
		
		if(entityType == QbeProperties.CLASS_TYPE_TABLE) {
			iconCls = "cube";
		} else if(entityType == QbeProperties.CLASS_TYPE_VIEW) {
			iconCls = "view";
		} else {
			iconCls = "dimension";
		}				
		
		JSONArray childrenNodes = getFieldNodes(entity, recursionLevel);
		
		JSObject entityNode = new JSObject();
		try {
			entityNode.put("id", entity.getUniqueName());
			entityNode.put("text", entity.getName());
			entityNode.put("iconCls", iconCls);
			entityNode.put("children", childrenNodes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		nodes.put(entityNode);		
	}
	
	public JSONArray getFieldNodes(DataMartEntity entity,int recursionLevel) {		
		
		JSONArray children = new JSONArray();
		
		// add key fields
		List keyFields = entity.getKeyFields();
		keyFields = qbeTreeFilter.filterFields(getDatamartModel(), keyFields);
		
		Iterator keyFieldIterator = keyFields.iterator();
		while (keyFieldIterator.hasNext() ) {
			DataMartField field = (DataMartField)keyFieldIterator.next();
			children.put( getFieldNode(entity, field) );
		}
		
		// add normal fields
		List normalFields = entity.getNormalFields();
		normalFields = qbeTreeFilter.filterFields(getDatamartModel(), normalFields);
		
		Iterator normalFieldIterator = normalFields.iterator();
		while (normalFieldIterator.hasNext() ) {
			DataMartField field = (DataMartField)normalFieldIterator.next();
			children.put( getFieldNode(entity, field) );
		}
		
		// add subentities
		JSONArray subentities = getSubEntitiesNodes(entity, children, recursionLevel);
		
		return children;		
	}
	
	public  JSObject getFieldNode(DataMartEntity parentEntity,
							 DataMartField field) {
		
		
		
		String label = DatamartLabelFactory.getFieldLabel(getDatamartModel(), field);			
		String image = DatamartImageFactory.getFieldImage(getDatamartModel(), field);				
		//String action = getUrlGenerator().getActionUrl(field);
					
		JSObject fieldNode = new JSObject();
		try {
			fieldNode.put("id", field.getUniqueName());
			fieldNode.put("text", field.getName());
			fieldNode.put("leaf", true);
			JSObject nodeAttributes = new JSObject();
			//"{iconCls:'measure', entity:'sales', field:'unit_sales', type:'field'}"
			nodeAttributes.put("entity", parentEntity.getName());
			nodeAttributes.put("field", field.getName());
			nodeAttributes.put("iconCls", "attribute");
			nodeAttributes.put("type", "field");
			fieldNode.put("attributes", nodeAttributes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fieldNode;
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
	
	
	public JSONArray getSubEntitiesNodes(DataMartEntity entity,
									JSONArray nodes,
								   int recursionLevel ) {
		
		List subEntities = entity.getSubEntities();
		subEntities = qbeTreeFilter.filterEntities(getDatamartModel(), subEntities);
		
	
		Iterator subEntitiesIterator = subEntities.iterator();
		while (subEntitiesIterator.hasNext()){
			DataMartEntity subentity = (DataMartEntity)subEntitiesIterator.next();
			if (subentity.getType().equalsIgnoreCase( entity.getType() ) || recursionLevel > 3) {
				// stop recursion 
			} else {
				addEntityNode(nodes,
							  subentity, 
								recursionLevel+1);
			}
		}
		
		return nodes;
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
