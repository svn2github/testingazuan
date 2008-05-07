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
package it.eng.spagobi.qbe.tree;

import it.eng.qbe.bo.DatamartLabels;
import it.eng.qbe.bo.DatamartProperties;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.QbeProperties;
import it.eng.spagobi.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.qbe.tree.presentation.tag.DatamartImageFactory;
import it.eng.spagobi.qbe.tree.presentation.tag.DatamartLabelFactory;
import it.eng.spagobi.qbe.tree.urlgenerator.IQbeTreeUrlGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSObject;

// TODO: Auto-generated Javadoc
/**
 * The Class ExtJsQbeTreeBuilder.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class ExtJsQbeTreeBuilder  {
	
	/** The qbe tree class. */
	private Class qbeTreeClass;
	
	/** The url generator. */
	private IQbeTreeUrlGenerator urlGenerator;	
	
	/** The qbe tree filter. */
	private QbeTreeFilter qbeTreeFilter;
	
	/** The datamart model. */
	private IDataMartModel datamartModel;	
	
	/** The modality. */
	String modality = DEFAULT_MODALITY;	
	
	/** The Constant FULL_MODALITY. */
	public static final String FULL_MODALITY = "FULL";
	
	/** The Constant LIGHT_MODALITY. */
	public static final String LIGHT_MODALITY = "LIGHT";
	
	/** The Constant DEFAULT_MODALITY. */
	public static final String DEFAULT_MODALITY = FULL_MODALITY;
	
	/** The class prefix. */
	private String classPrefix = null;	
	
	
	/**
	 * Instantiates a new ext js qbe tree builder.
	 * 
	 * @param qbeTreeFilter the qbe tree filter
	 */
	public ExtJsQbeTreeBuilder(QbeTreeFilter qbeTreeFilter)  {		
		setQbeTreeFilter( qbeTreeFilter );
	}	
	
		
	/**
	 * Gets the qbe trees.
	 * 
	 * @param datamartModel the datamart model
	 * 
	 * @return the qbe trees
	 */
	public List getQbeTrees(IDataMartModel datamartModel)  {			
		setDatamartModel(datamartModel);			
		return buildQbeTreeList();
	}

	/**
	 * Builds the qbe tree list.
	 * 
	 * @return the list
	 */
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
	
	
	
	/**
	 * Builds the qbe tree.
	 * 
	 * @param datamartName the datamart name
	 * 
	 * @return the jSON array
	 */
	private JSONArray buildQbeTree(String datamartName)  {			
		JSONArray nodes = new JSONArray();	
		addEntityNodes(nodes, datamartName);	
		return nodes;
	}
	
	
	/**
	 * Adds the entity nodes.
	 * 
	 * @param nodes the nodes
	 * @param datamartName the datamart name
	 */
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
	
	
	/**
	 * Adds the entity node.
	 * 
	 * @param nodes the nodes
	 * @param entity the entity
	 * @param recursionLevel the recursion level
	 */
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
		
	
	/**
	 * Adds the entity root node.
	 * 
	 * @param nodes the nodes
	 * @param entity the entity
	 * @param recursionLevel the recursion level
	 */
	public void addEntityRootNode(JSONArray nodes,
								  DataMartEntity entity,
								  int recursionLevel) {		
		
		DatamartProperties datamartProperties = datamartModel.getDataSource().getProperties();	
		if(!datamartProperties.isTableVisible( entity.getUniqueName().replaceAll(":", "/") )) return;
		int entityType = datamartProperties.getTableType( entity.getUniqueName().replaceAll(":", "/") );
		
		String label = DatamartLabelFactory.getEntityLabel(datamartModel, entity);	
			
		
		System.out.println( "\n\n"+ entity.getUniqueName().replaceAll(":", "/") + "=" );
		
		
		String iconCls = "dimension";		
		if(entityType == DatamartProperties.CLASS_TYPE_CUBE) {
			iconCls = "cube";
		} else if(entityType == DatamartProperties.CLASS_TYPE_VIEW) {
			iconCls = "view";
		} 			
		
		JSONArray childrenNodes = getFieldNodes(entity, recursionLevel);
		
		JSObject entityNode = new JSObject();
		try {
			entityNode.put("id", entity.getUniqueName());
			entityNode.put("text", label );
			entityNode.put("iconCls", iconCls);
			entityNode.put("children", childrenNodes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		nodes.put(entityNode);		
	}
	
	/**
	 * Gets the field nodes.
	 * 
	 * @param entity the entity
	 * @param recursionLevel the recursion level
	 * 
	 * @return the field nodes
	 */
	public JSONArray getFieldNodes(DataMartEntity entity,int recursionLevel) {		
		
		JSONArray children = new JSONArray();
		
		// add key fields
		List keyFields = entity.getKeyFields();
		keyFields = qbeTreeFilter.filterFields(getDatamartModel(), keyFields);
		
		Iterator keyFieldIterator = keyFields.iterator();
		while (keyFieldIterator.hasNext() ) {
			DataMartField field = (DataMartField)keyFieldIterator.next();
			JSObject jsObject = getFieldNode(entity, field);
			if(jsObject != null) {
				children.put( jsObject );
			}
			
		}
		
		// add normal fields
		List normalFields = entity.getNormalFields();
		normalFields = qbeTreeFilter.filterFields(getDatamartModel(), normalFields);
		
		Iterator normalFieldIterator = normalFields.iterator();
		while (normalFieldIterator.hasNext() ) {
			DataMartField field = (DataMartField)normalFieldIterator.next();
			JSObject jsObject = getFieldNode(entity, field);
			if(jsObject != null) {
				children.put( jsObject );
			}
		}
		
		// add subentities
		JSONArray subentities = getSubEntitiesNodes(entity, children, recursionLevel);
		
		return children;		
	}
	
	/**
	 * Gets the field node.
	 * 
	 * @param parentEntity the parent entity
	 * @param field the field
	 * 
	 * @return the field node
	 */
	public  JSObject getFieldNode(DataMartEntity parentEntity,
							 DataMartField field) {
		
		DatamartProperties datamartProperties = datamartModel.getDataSource().getProperties();
		if( !datamartProperties.isFieldVisible( field.getUniqueName().replaceAll(":", "/") ) ) return null;
		int fieldType = datamartProperties.getFieldType( field.getUniqueName().replaceAll(":", "/") );
		
		
		String fieldLabel = DatamartLabelFactory.getFieldLabel(getDatamartModel(), field);	
		String entityLabel = DatamartLabelFactory.getEntityLabel(datamartModel, parentEntity);	
				
		System.out.println( field.getUniqueName().replaceAll(":", "/") + "=" );
		
		
		String iconCls = "attribute";
		if(fieldType == DatamartProperties.FIELD_TYPE_ATTRIBUTE) {
			iconCls = "attribute";
		} else if(fieldType == DatamartProperties.FIELD_TYPE_MEASURE) {
			iconCls = "measure";
		} 	
		
		JSObject fieldNode = new JSObject();
		try {
			fieldNode.put("id", field.getUniqueName());
			fieldNode.put("text", fieldLabel);
			fieldNode.put("leaf", true);
			JSObject nodeAttributes = new JSObject();

			nodeAttributes.put("entity", entityLabel);
			nodeAttributes.put("field", fieldLabel);
			nodeAttributes.put("iconCls", iconCls);
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
	 * Control recursion level because calculate field are applied only at entity level not in dimension level.
	 * 
	 * @param tree the tree
	 * @param parentEntityNodeId the parent entity node id
	 * @param nodeCounter the node counter
	 * @param entity the entity
	 * 
	 * @return the int
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
	
	
	/**
	 * Gets the sub entities nodes.
	 * 
	 * @param entity the entity
	 * @param nodes the nodes
	 * @param recursionLevel the recursion level
	 * 
	 * @return the sub entities nodes
	 */
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
	 * Gets the datamart model.
	 * 
	 * @return the datamart model
	 */
	protected IDataMartModel getDatamartModel() {
		return datamartModel;
	}

	/**
	 * Sets the datamart model.
	 * 
	 * @param datamartModel the new datamart model
	 */
	protected void setDatamartModel(IDataMartModel datamartModel) {
		this.datamartModel = datamartModel;
	}


	/**
	 * Gets the url generator.
	 * 
	 * @return the url generator
	 */
	public IQbeTreeUrlGenerator getUrlGenerator() {
		return urlGenerator;
	}


	/**
	 * Sets the url generator.
	 * 
	 * @param urlGenerator the new url generator
	 */
	public void setUrlGenerator(IQbeTreeUrlGenerator urlGenerator) {
		this.urlGenerator = urlGenerator;
	}


	/**
	 * Gets the qbe tree class.
	 * 
	 * @return the qbe tree class
	 */
	protected Class getQbeTreeClass() {
		return qbeTreeClass;
	}


	/**
	 * Sets the qbe tree class.
	 * 
	 * @param qbeTreeClass the new qbe tree class
	 */
	protected void setQbeTreeClass(Class qbeTreeClass) {
		this.qbeTreeClass = qbeTreeClass;
	}


	/**
	 * Gets the qbe tree filter.
	 * 
	 * @return the qbe tree filter
	 */
	private QbeTreeFilter getQbeTreeFilter() {
		return qbeTreeFilter;
	}


	/**
	 * Sets the qbe tree filter.
	 * 
	 * @param qbeTreeFilter the new qbe tree filter
	 */
	private void setQbeTreeFilter(QbeTreeFilter qbeTreeFilter) {
		this.qbeTreeFilter = qbeTreeFilter;
	}
}
