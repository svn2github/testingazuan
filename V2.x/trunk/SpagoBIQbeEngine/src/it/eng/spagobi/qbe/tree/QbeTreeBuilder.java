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

import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.utility.CalculatedField;
import it.eng.spagobi.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.qbe.tree.presentation.tag.DatamartImageFactory;
import it.eng.spagobi.qbe.tree.presentation.tag.DatamartLabelFactory;
import it.eng.spagobi.qbe.tree.urlgenerator.IQbeTreeUrlGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeTreeBuilder.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class QbeTreeBuilder  {
	
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
	 * Instantiates a new qbe tree builder.
	 * 
	 * @param className the class name
	 * @param urlGenerator the url generator
	 * @param qbeTreeFilter the qbe tree filter
	 * 
	 * @throws ClassNotFoundException the class not found exception
	 */
	public QbeTreeBuilder(String className, IQbeTreeUrlGenerator urlGenerator, QbeTreeFilter qbeTreeFilter) throws ClassNotFoundException {		
		setQbeTreeClass( Class.forName(className) );	
		setUrlGenerator(urlGenerator);
		setQbeTreeFilter( qbeTreeFilter );
	}	
	
		
	/**
	 * Gets the qbe trees.
	 * 
	 * @param datamartModel the datamart model
	 * 
	 * @return the qbe trees
	 * 
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	public List getQbeTrees(IDataMartModel datamartModel) throws InstantiationException, IllegalAccessException {			
		setDatamartModel(datamartModel);			
		return buildQbeTreeList();
	}

	/**
	 * Builds the qbe tree list.
	 * 
	 * @return the list
	 * 
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
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
	
	
	
	/**
	 * Builds the qbe tree.
	 * 
	 * @param datamartName the datamart name
	 * 
	 * @return the i qbe tree
	 * 
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
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
	
	
	/**
	 * Adds the root node.
	 * 
	 * @param tree the tree
	 * @param rootNodeName the root node name
	 */
	public void addRootNode(IQbeTree tree, String rootNodeName) {

		tree.addNode("0", "-1", rootNodeName, "", "", rootNodeName, 
				getUrlGenerator().getResourceUrl("../img/base.gif"),
				getUrlGenerator().getResourceUrl("../img/base.gif"),
				"", "", "", "", "");
	}
	
	/**
	 * Adds the entity nodes.
	 * 
	 * @param tree the tree
	 * @param datamartName the datamart name
	 */
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
	
	
	/**
	 * Adds the entity node.
	 * 
	 * @param tree the tree
	 * @param entity the entity
	 * @param rootNodeId the root node id
	 * @param nodeCounter the node counter
	 * @param recursionLevel the recursion level
	 * 
	 * @return the int
	 */
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
		
	
	/**
	 * Adds the entity root node.
	 * 
	 * @param tree the tree
	 * @param entity the entity
	 * @param rootNodeId the root node id
	 * @param nodeCounter the node counter
	 * 
	 * @return the int
	 */
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
	
	/**
	 * Adds the field nodes.
	 * 
	 * @param tree the tree
	 * @param entity the entity
	 * @param parentEntityNodeId the parent entity node id
	 * @param nodeCounter the node counter
	 * 
	 * @return the int
	 */
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
	
	/**
	 * Adds the field node.
	 * 
	 * @param tree the tree
	 * @param field the field
	 * @param parentEntityNodeId the parent entity node id
	 * @param nodeCounter the node counter
	 * 
	 * @return the int
	 */
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
	 * Adds the sub entities nodes.
	 * 
	 * @param tree the tree
	 * @param entity the entity
	 * @param parentEntityNodeId the parent entity node id
	 * @param nodeCounter the node counter
	 * @param recursionLevel the recursion level
	 * @param subEntities the sub entities
	 * 
	 * @return the int
	 */
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
