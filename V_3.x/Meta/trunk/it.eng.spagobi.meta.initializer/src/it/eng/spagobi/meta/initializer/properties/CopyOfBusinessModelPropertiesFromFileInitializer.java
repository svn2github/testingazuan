/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.initializer.properties;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.initializer.InitializationException;
import it.eng.spagobi.meta.initializer.SpagoBIMetaInitializerPlugin;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.ModelPropertyCategory;
import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author cortella
 *
 */
public class CopyOfBusinessModelPropertiesFromFileInitializer implements IPropertiesInitializer {

	private Document document;
	
	public static final String COLUMN_PHYSICAL_TABLE = "physical.physicaltable";
	
	static public ModelFactory FACTORY = ModelFactory.eINSTANCE;
	static public IResourceLocator RL = SpagoBIMetaInitializerPlugin.getInstance().getResourceLocator();
	
	private static Logger logger = LoggerFactory.getLogger(CopyOfBusinessModelPropertiesFromFileInitializer.class);
	
	
	public CopyOfBusinessModelPropertiesFromFileInitializer() {
		
		logger.trace("IN");
		try {
			File propertiesFile = RL.getFile("properties/customProperties.xml");
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = domFactory.newDocumentBuilder();
	        document = builder.parse(propertiesFile);
		} catch(Throwable t) {
			throw new InitializationException("Impossible to load properties from configuration file", t);
		} finally {
			logger.trace("OUT");
		}
	}
	
	public void addProperties(ModelObject o) {
		
		//read file
//		try {
//			document = readFile();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		if(o instanceof BusinessModel) {
			initModelProperties((BusinessModel)o);
		} else if(o instanceof BusinessTable) {
			initTableProperties((BusinessTable)o);
		} else if(o instanceof BusinessColumn) {
			initColumnProperties((BusinessColumn)o);
		} else if(o instanceof BusinessIdentifier) {
			initIdentifierProperties((BusinessIdentifier)o);
		} else if(o instanceof BusinessRelationship) {
			initRelationshipProperties((BusinessRelationship)o);
		} else {
			
		}
	}
	
//	private Document readFile() throws Exception{
//		File propertiesFile = null;
//		Bundle generatorBundle = Platform.getBundle("it.eng.spagobi.meta.initializer");
//		try {
//			IPath path = new Path(Platform.asLocalURL(generatorBundle.getEntry("properties")).getPath());
//			String propertiesPath = path.toString();
//			propertiesFile = new File(propertiesPath+"\\customProperties.xml");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	
//		
//		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = domFactory.newDocumentBuilder();
//        Document doc = builder.parse(propertiesFile);
//        return doc;		
//	}
	
    private NodeList readXMLNodes(Document doc, String xpathExpression) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile(xpathExpression);
 
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
 
        return nodes;
    }
	

	private void initModelProperties(BusinessModel o) {
        NodeList nodes;
        int nodesLength;
		try {
			
			//1- Search model categories definitions
			nodes = readXMLNodes(document, "/properties/model/categories/category");
			nodesLength = nodes.getLength();
	        for (int i = 0; i < nodesLength; i++) {
	        	NamedNodeMap nodeAttributes = nodes.item(i).getAttributes();
	        	if (nodeAttributes != null) {
	        		String categoryName = nodeAttributes.getNamedItem("name").getNodeValue();
	        		String categoryDescription = nodeAttributes.getNamedItem("description").getNodeValue();
	        		
	                // if doesn't exist, create a model category
	        		ModelPropertyCategory modelCategory =  o.getParentModel().getPropertyCategory(categoryName);
	                if(modelCategory == null) {
	                    modelCategory = FACTORY.createModelPropertyCategory();
	                    modelCategory.setName(categoryName);
	                    modelCategory.setDescription(categoryDescription);
	                    o.getParentModel().getPropertyCategories().add(modelCategory);	
	                }   
	        	}
	        }
	        
	      	//2- Search model types definitions
			nodes = readXMLNodes(document, "/properties/model/types/type");
			nodesLength = nodes.getLength();
	        for (int j = 0; j < nodesLength; j++) {
	        	NamedNodeMap nodeAttributes = nodes.item(j).getAttributes();
	        	if (nodeAttributes != null) {
	        		String typeId = nodeAttributes.getNamedItem("id").getNodeValue();
	        		String typeName = nodeAttributes.getNamedItem("name").getNodeValue();
	        		String typeDescription = nodeAttributes.getNamedItem("description").getNodeValue();
	        		String typeCategory = nodeAttributes.getNamedItem("category").getNodeValue();
	        		String typeDefaultValue = nodeAttributes.getNamedItem("defaultValue").getNodeValue();
	        		
	                // Create the new property type 
	        		ModelPropertyType propertyType = null;
	               
	                if(o.getParentModel() != null) {
	                	propertyType = o.getParentModel().getPropertyType(typeId);
	                }
	                if(propertyType == null) {
	                    propertyType = FACTORY.createModelPropertyType();
	                    propertyType.setId( typeId );
	                    propertyType.setName(typeName);
	                    propertyType.setDescription(typeDescription);
	                    propertyType.setCategory(getModelPropertyCategory(o, typeCategory));
	                    propertyType.setDefaultValue(typeDefaultValue);
	                    
	                    if(o.getParentModel() != null) {
	                    	o.getParentModel().getPropertyTypes().add(propertyType);
	                    }
	                }
	               
	                // add a model property type for model object
	                ModelProperty property = FACTORY.createModelProperty();
	                property.setPropertyType(propertyType);
	                o.getProperties().put(property.getPropertyType().getId(), property); 
	        	}
	        }
	        
	        //3- Search model admissible types values definitions
	        nodes = readXMLNodes(document, "/properties/model/typesValues/admissibleValuesOf");
			nodesLength = nodes.getLength();
			for (int j = 0; j < nodesLength; j++) {
				NamedNodeMap nodeAttributes = nodes.item(j).getAttributes();
				if (nodeAttributes != null) {
					String typeId = nodeAttributes.getNamedItem("typeId").getNodeValue();
					ModelPropertyType propertyType = getModelPropertyType(o, typeId);

					NodeList values = readXMLNodes(document, "/properties/model/typesValues/admissibleValuesOf"+"[@typeId"+"='"+typeId+"']/value");
					//search each admissible values for this type
					int valuesLength = values.getLength();
					for (int z = 0; z < valuesLength; z++) {
						String value = values.item(z).getTextContent();
						//add admissible value
						propertyType.getAdmissibleValues().add(value);
					}
				}
			}
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void initTableProperties(BusinessTable o) {
		
	}
	

	private void initColumnProperties(BusinessColumn o) {
        NodeList nodes;
        Model rootModel = null;
		
        int nodesLength;
		try {
			if(o.getTable() != null && o.getTable().getModel() != null) {
				rootModel = o.getTable().getModel().getParentModel();
			}
			//1- Search column categories definitions
			nodes = readXMLNodes(document, "/properties/column/categories/category");
			nodesLength = nodes.getLength();
	        for (int i = 0; i < nodesLength; i++) {
	        	NamedNodeMap nodeAttributes = nodes.item(i).getAttributes();
	        	if (nodeAttributes != null) {
	        		String categoryName = nodeAttributes.getNamedItem("name").getNodeValue();
	        		String categoryDescription = nodeAttributes.getNamedItem("description").getNodeValue();
	        		
	                // if doesn't exist, create a column category
	        		ModelPropertyCategory modelCategory =  rootModel.getPropertyCategory(categoryName);
	                if(modelCategory == null) {
	                    modelCategory = FACTORY.createModelPropertyCategory();
	                    modelCategory.setName(categoryName);
	                    modelCategory.setDescription(categoryDescription);
	                    rootModel.getPropertyCategories().add(modelCategory);	
	                }   
	        	}
	        }
	        
	      	//2- Search column types definitions
			nodes = readXMLNodes(document, "/properties/column/types/type");
			nodesLength = nodes.getLength();
	        for (int i = 0; i < nodesLength; i++) {
	        	NamedNodeMap nodeAttributes = nodes.item(i).getAttributes();
	        	if (nodeAttributes != null) {
	        		String typeId = nodeAttributes.getNamedItem("id").getNodeValue();
	        		String typeName = nodeAttributes.getNamedItem("name").getNodeValue();
	        		String typeDescription = nodeAttributes.getNamedItem("description").getNodeValue();
	        		String typeCategory = nodeAttributes.getNamedItem("category").getNodeValue();
	        		String typeDefaultValue = nodeAttributes.getNamedItem("defaultValue").getNodeValue();
	        		
	                // Create the new property type 
	        		ModelPropertyType propertyType = null;
	               
	                if(rootModel != null) {
	                	propertyType = rootModel.getPropertyType(typeId);
	                }
	                if(propertyType == null) {
	                    propertyType = FACTORY.createModelPropertyType();
	                    propertyType.setId( typeId );
	                    propertyType.setName(typeName);
	                    propertyType.setDescription(typeDescription);
	                    propertyType.setCategory(getModelPropertyCategory(o, typeCategory));
	                    propertyType.setDefaultValue(typeDefaultValue);
	                    
	                    if(rootModel != null) {
	                    	rootModel.getPropertyTypes().add(propertyType);
	                    }
	                }
	               
	                // add a column property type for column object
	                ModelProperty property = FACTORY.createModelProperty();
	                property.setPropertyType(propertyType);
	                o.getProperties().put(property.getPropertyType().getId(), property); 
	        	}
	        }
	        
	        //3- Search column admissible types values definitions
	        nodes = readXMLNodes(document, "/properties/column/typesValues/admissibleValuesOf");
			nodesLength = nodes.getLength();
	        for (int j = 0; j < nodesLength; j++) {
	        	NamedNodeMap nodeAttributes = nodes.item(j).getAttributes();
	        	if (nodeAttributes != null) {
	        		String typeId = nodeAttributes.getNamedItem("typeId").getNodeValue();
	        		ModelPropertyType propertyType = getModelPropertyType(o, typeId);
	        		
	        		NodeList values = readXMLNodes(document, "/properties/column/typesValues/admissibleValuesOf"+"[@typeId"+"='"+typeId+"']/value");
	        		//search each admissible values for this type
	        		int valuesLength = values.getLength();
	        		for (int z = 0; z < valuesLength; z++) {
	        			String value = values.item(z).getTextContent();
	        			//add admissible value
	        			propertyType.getAdmissibleValues().add(value);
	        		}
	        	}
	        }
	        
	        // *********************************************************************
	        // THIS WILL ADD PHYSICAL TABLE REFERENCE PROPERTY FOR BUSINESS COLUMN
	        // This is not a custom property readed from xml file
	        // *********************************************************************

	        ModelPropertyCategory physicalReferenceCategory =  o.getTable().getModel().getParentModel().getPropertyCategory("Physical Reference");
			if(physicalReferenceCategory == null) {
				physicalReferenceCategory = FACTORY.createModelPropertyCategory();
				physicalReferenceCategory.setName("Physical Reference");
				physicalReferenceCategory.setDescription("The reference to the original physical object");
				o.getTable().getModel().getParentModel().getPropertyCategories().add(physicalReferenceCategory);
			}
			
			// Column Physical Table TYPE
			ModelPropertyType propertyType = null;
			
			if(rootModel != null) propertyType = rootModel.getPropertyType(COLUMN_PHYSICAL_TABLE);
			if(propertyType == null) {
				propertyType = FACTORY.createModelPropertyType();
				propertyType.setId(COLUMN_PHYSICAL_TABLE);
				propertyType.setName("Physical Table");
				propertyType.setDescription("The original physical table of this column");
				propertyType.setCategory(physicalReferenceCategory);
				propertyType.setDefaultValue(o.getPhysicalColumn().getTable().getName());
				
				if(rootModel != null) rootModel.getPropertyTypes().add(propertyType);
			}
			
			ModelProperty property = FACTORY.createModelProperty();
			property.setPropertyType(propertyType);
			o.getProperties().put(property.getPropertyType().getId(), property);
	        
			//**************************************************************************
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initIdentifierProperties(BusinessIdentifier o) {
		
	}
	
	private void initRelationshipProperties(BusinessRelationship o) {
		
	}	
	
	// Utility methods
	//-----------------------------------------------------------------------
	
	
	private ModelPropertyCategory getModelPropertyCategory(Object o, String categoryName){
		EList<ModelPropertyCategory> categories = null;
		if (o instanceof BusinessModel){
			categories = ((BusinessModel)o).getParentModel().getPropertyCategories();
		}
		else if (o instanceof BusinessTable){
			categories = ((BusinessTable)o).getModel().getParentModel().getPropertyCategories();
		} 
		else if (o instanceof BusinessColumn){
			categories = ((BusinessColumn)o).getTable().getModel().getParentModel().getPropertyCategories();			
		}
		else if (o instanceof BusinessIdentifier){
			categories = ((BusinessIdentifier)o).getModel().getParentModel().getPropertyCategories();				
		}
		else if (o instanceof BusinessRelationship){
			categories = ((BusinessRelationship)o).getModel().getParentModel().getPropertyCategories();							
		}
		
		if (categories != null){
			for (ModelPropertyCategory category : categories){
				if (category.getName().equalsIgnoreCase(categoryName)){
					return category;					
				}
			}
		}
		return null;
	}
	
	private ModelPropertyType getModelPropertyType(Object o, String typeId){
		EList<ModelPropertyType> types = null;
		
		if (o instanceof BusinessModel){
			types = ((BusinessModel)o).getParentModel().getPropertyTypes();
		}
		else if (o instanceof BusinessTable){
			types = ((BusinessTable)o).getModel().getParentModel().getPropertyTypes();
		}
		else if (o instanceof BusinessColumn){
			types = ((BusinessColumn)o).getTable().getModel().getParentModel().getPropertyTypes();
		}
		else if (o instanceof BusinessIdentifier){
			types = ((BusinessIdentifier)o).getModel().getParentModel().getPropertyTypes();
		}
		else if (o instanceof BusinessRelationship){
			types = ((BusinessRelationship)o).getModel().getParentModel().getPropertyTypes();			
		}
		
		if (types != null){
			for (ModelPropertyType type : types){
				if (type.getId().equalsIgnoreCase(typeId)){
					return type;					
				}
			}
		}
		return null;
	}

}
