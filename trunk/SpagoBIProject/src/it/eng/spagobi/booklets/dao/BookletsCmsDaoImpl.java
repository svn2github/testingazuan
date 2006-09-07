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

package it.eng.spagobi.booklets.dao;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.util.PortletUtilities;
import it.eng.spagobi.booklets.bo.ConfiguredBIDocument;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements all the methods for access the booklet cms contents
 */
public class BookletsCmsDaoImpl implements IBookletsCmsDao {

	
	public String createNewConfigurationNode(String pathBiObj) {
		String pathToReturn = null;
		try {
			CmsManager manager = new CmsManager();
			String templateMessage = PortletUtilities.getMessage("book.templateMessage", "component_booklets_messages");
			SetOperation setOp = new SetOperation();
			setOp.setContent(new ByteArrayInputStream(templateMessage.getBytes()));
			setOp.setType(SetOperation.TYPE_CONTENT);
			String path =pathBiObj + "/template";
			setOp.setPath(path);
			// define properties list
			List properties = new ArrayList();
			String[] nameFilePropValues = new String[] { "bookletTemplate" };
			String today = new Long(new Date().getTime()).toString();
			String[] datePropValues = new String[] { today };
			CmsProperty propFileName = new CmsProperty("fileName", nameFilePropValues);
			CmsProperty propDateLoad = new CmsProperty("dateLoad", datePropValues);
			properties.add(propFileName);
			properties.add(propDateLoad);
            setOp.setProperties(properties);
			manager.execSetOperation(setOp);
			// create node for documents
			String documentsConfPathNode = path + "/documentsConfigured";
			setOp = new SetOperation(documentsConfPathNode, SetOperation.TYPE_CONTAINER, true);
			manager.execSetOperation(setOp);
			// return path of the booklet
			pathToReturn = path;
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI[Booklet]", this.getClass().getName(),
					            "createNewConfigurationNode", "Cannot save the new booklet configuration node ");
		} finally {	}
		return pathToReturn;
	}
	
	
	
	
	
	public void createStructureForTemplate(String pathBooklet, int numTempParts) {
		try {
			CmsManager manager = new CmsManager();
			String pathDocParts = pathBooklet + "/document_parts";
			// check if the node containing information exists
			GetOperation getOp = new GetOperation(pathDocParts, true, false, false, false);
			CmsNode cmsnode = manager.execGetOperation(getOp);
			// if the node exist delete it
			if(cmsnode!=null){
				DeleteOperation delOp = new DeleteOperation(pathDocParts);
				manager.execDeleteOperation(delOp);
			}
			// create the node and subnodes
			SetOperation setOp = new SetOperation(pathDocParts, SetOperation.TYPE_CONTAINER, true);
			manager.execSetOperation(setOp);
			for(int i=1; i<=numTempParts; i++) {
				String pathPart = pathDocParts + "/part_" + i;
				setOp = new SetOperation(pathPart, SetOperation.TYPE_CONTAINER, true);
				manager.execSetOperation(setOp);
				String pathImg = pathPart + "/images";
				setOp = new SetOperation(pathImg, SetOperation.TYPE_CONTAINER, true);
				manager.execSetOperation(setOp);
				String pathNotes = pathPart + "/notes";
				setOp = new SetOperation(pathNotes, SetOperation.TYPE_CONTENT, true);
				ByteArrayInputStream bais = new ByteArrayInputStream("".getBytes());
				setOp.setContent(bais);
				bais.close();
				manager.execSetOperation(setOp);
			}
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "createStructureForTemplate", "Error while creating structure for " +
					            "booklet " + pathBooklet, e);
		} finally {	}
	}

	
	
	
	
	private CmsProperty getProperty(String name, List properties) {
		Iterator iterProps = properties.iterator();
		while(iterProps.hasNext()) {
			CmsProperty prop  = (CmsProperty)iterProps.next();
			String nameProp = prop.getName();
			if(nameProp.equalsIgnoreCase(name))
				return prop;
		}
		return null;
	}




	public List getConfiguredDocumentList(String pathBooklet) {
		List docs = new ArrayList();
		try {
			GetOperation getOp = new GetOperation(pathBooklet+ "/documentsConfigured", true, false, false, false);
			CmsManager manager = new CmsManager();
			CmsNode cmsnode = manager.execGetOperation(getOp);
			List childs = cmsnode.getChilds();
			Iterator iterChilds = childs.iterator();
			while(iterChilds.hasNext()) {
				CmsNode childNode = (CmsNode)iterChilds.next();
				String pathChild = childNode.getPath();
				getOp = new GetOperation(); 
				getOp.setPath(pathChild);
				getOp.setRetriveChildsInformation("false");
				getOp.setRetrivePropertiesInformation("true");
				getOp.setRetriveVersionsInformation("false");
				getOp.setRetriveContentInformation("false");
				childNode = manager.execGetOperation(getOp);
				// get value of the properties
				List props = childNode.getProperties();
				CmsProperty proplabel = getProperty("label", props);
				CmsProperty propname = getProperty("name", props);
				CmsProperty propdescr = getProperty("description", props);
				CmsProperty propidobj = getProperty("idbiobject", props);
				CmsProperty proplogicalname = getProperty("logicalname", props);
                // fill the configured document bo				
				ConfiguredBIDocument confDoc = new ConfiguredBIDocument();
				confDoc.setName(propname.getStringValues()[0]);
				confDoc.setDescription(propdescr.getStringValues()[0]);
				confDoc.setId(new Integer(propidobj.getStringValues()[0]));
				confDoc.setLogicalName(proplogicalname.getStringValues()[0]);
				confDoc.setLabel(proplabel.getStringValues()[0]);
				// add document to list
				docs.add(confDoc);
			}
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getPumphletList", "Cannot recover pumplet list", e);
		} finally {	}
		return docs;
	}




	public void addConfiguredDocument(String pathBooklet, ConfiguredBIDocument doc) {
		try {
			// build path 
			String newPath = pathBooklet + "/documentsConfigured/" + doc.getLogicalName();
			// build list of properties
			List properties = new ArrayList();
			String[] namePropValues = new String[] { doc.getName() };
			CmsProperty propname = new CmsProperty("name", namePropValues);
			properties.add(propname);
			String[] labelPropValues = new String[] { doc.getLabel() };
			CmsProperty proplabel = new CmsProperty("label", labelPropValues);
			properties.add(proplabel);
			String[] descPropValues = new String[] { doc.getDescription() };
			CmsProperty propdesc = new CmsProperty("description", descPropValues);
			properties.add(propdesc);
			String[] idobjPropValues = new String[] { doc.getId().toString() };
			CmsProperty propidobj = new CmsProperty("idbiobject", idobjPropValues);
			properties.add(propidobj);
			String[] lognamePropValues = new String[] { doc.getLogicalName() };
			CmsProperty proplogname = new CmsProperty("logicalname", lognamePropValues);
			properties.add(proplogname);
			// build the xml configuration
			SourceBean conf = new SourceBean("CONFIGURATION");
			SourceBean pars = new SourceBean("PARAMETERS");
			Map parMap = doc.getParameters();
			Set keys = parMap.keySet();
			Iterator iterKeys = keys.iterator();
			while(iterKeys.hasNext()) {
				String paramname = (String)iterKeys.next();
				String paramvalue = (String)parMap.get(paramname);
				SourceBean par = new SourceBean("PARAMETER");
				par.setAttribute("name", paramname);
				par.setAttribute("value", paramvalue);
				pars.setAttribute(par);
			}
			conf.setAttribute(pars);
			String xmlStr = conf.toXML(false);
			byte[] xmlBytes = xmlStr.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(xmlBytes);
			// store all information into cms
			SetOperation setOp = new SetOperation(newPath, SetOperation.TYPE_CONTENT, true, properties);
			setOp.setContent(bais);
	        CmsManager manager = new CmsManager();
			manager.execSetOperation(setOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "addConfiguredDocument", "Cannot save the configured document");
		} finally {	}
	}




	public void deleteConfiguredDocument(String pathBooklet, String conDocIdentifier) {
		try {
			String path = pathBooklet + "/documentsConfigured/" + conDocIdentifier;
			DeleteOperation delOp = new DeleteOperation(path);
			CmsManager manager = new CmsManager();
			manager.execDeleteOperation(delOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		             			"deleteConfiguredDocument", "Cannot delete the configured document " + conDocIdentifier, e);
		} finally {	}
	}




	public ConfiguredBIDocument getConfigureDocument(String pathBooklet, String conDocIdentifier) {
		ConfiguredBIDocument confDoc = null;
		try {
			String path = pathBooklet + "/documentsConfigured/" + conDocIdentifier;
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("true");
			CmsManager manager = new CmsManager();
			CmsNode node = manager.execGetOperation(getOp);
			// get value of the properties
			List props = node.getProperties();
			CmsProperty proplabel = getProperty("label", props);
			CmsProperty propname = getProperty("name", props);
			CmsProperty propdescr = getProperty("description", props);
			CmsProperty propidobj = getProperty("idbiobject", props);
			CmsProperty proplogicalname = getProperty("logicalname", props);
            // fill the configured document bo				
			confDoc = new ConfiguredBIDocument();
			confDoc.setName(propname.getStringValues()[0]);
			confDoc.setDescription(propdescr.getStringValues()[0]);
			confDoc.setId(new Integer(propidobj.getStringValues()[0]));
			confDoc.setLogicalName(proplogicalname.getStringValues()[0]);
			confDoc.setLabel(proplabel.getStringValues()[0]);
			// get configuration content
			InputStream iscontent = node.getContent();
			byte[] contBytes = GeneralUtilities.getByteArrayFromInputStream(iscontent);
		    String contStr = new String(contBytes);
		    SourceBean confSB = SourceBean.fromXMLString(contStr);
		    List pars = confSB.getAttributeAsList("PARAMETERS.PARAMETER");
		    // build parameters map
		    Map parameterMap = new HashMap();
		    Iterator iterPar = pars.iterator();
		    while(iterPar.hasNext()){
		    	SourceBean param = (SourceBean)iterPar.next();
		    	String parName = (String)param.getAttribute("name");
		    	String parValue = (String)param.getAttribute("value");
		    	parameterMap.put(parName, parValue);
		    }
		    confDoc.setParameters(parameterMap);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
         			            "getConfigureDocument", "Cannot retrive configured document " + conDocIdentifier, e);
		}
		return confDoc;
	}




	public void storeBookletTemplate(String pathBooklet, String templateFileName, byte[] templateContent) {
		try {
			// build path 
			String tempPath = pathBooklet + "/template";
			// build list of properties
			List properties = new ArrayList();
			String[] fileNamePropValues = new String[] { templateFileName };
			CmsProperty propfilename = new CmsProperty("filename", fileNamePropValues);
			properties.add(propfilename);
			ByteArrayInputStream bais = new ByteArrayInputStream(templateContent);
			// store all information into cms
			SetOperation setOp = new SetOperation(tempPath, SetOperation.TYPE_CONTENT, true, properties);
			setOp.setContent(bais);
	        CmsManager manager = new CmsManager();
			manager.execSetOperation(setOp);
			bais.close();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "storeBookletTemplate", "Cannot save the template");
		} finally {	}
		
	}




	public String getBookletTemplateFileName(String pathBooklet) {
		String name = "";
		try{
			String path = pathBooklet + "/template";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("false");
			CmsManager manager = new CmsManager();
			CmsNode node = manager.execGetOperation(getOp);
			// get value of the properties
			List props = node.getProperties();
			CmsProperty propNameFile = getProperty("filename", props);
			name = propNameFile.getStringValues()[0];
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getBookletTemplateFileName", "Cannot recover template file name");
		}
		return name;
	}




	public InputStream getBookletTemplateContent(String pathBooklet) {
		InputStream content = null;
		try{
			String path = pathBooklet + "/template";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("true");
			CmsManager manager = new CmsManager();
			CmsNode node = manager.execGetOperation(getOp);
			content = node.getContent();
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getBookletTemplateContent", "Cannot recover template content");
		}
		return content;
	}



	public InputStream getBookletProcessDefinitionContent(String pathBooklet) {
		InputStream content = null;
		try{
			String path = pathBooklet + "/processdefinition";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("true");
			CmsManager manager = new CmsManager();
			CmsNode node = manager.execGetOperation(getOp);
			content = node.getContent();
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getBookletProcessDefinitionContent", "Cannot recover process definition content");
		}
		return content;
	}





	public String getBookletProcessDefinitionFileName(String pathBooklet) {
		String name = "";
		try{
			String path = pathBooklet + "/processdefinition";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("false");
			CmsManager manager = new CmsManager();
			CmsNode node = manager.execGetOperation(getOp);
			// get value of the properties
			List props = node.getProperties();
			CmsProperty propNameFile = getProperty("filename", props);
			name = propNameFile.getStringValues()[0];
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getBookletProcessDefinitionFileName", "Cannot recover process definition file name");
		}
		return name;
	}





	public void storeBookletProcessDefinition(String pathBooklet, String pdFileName, byte[] pdFileContent) {
		try {
			// build path 
			String tempPath = pathBooklet + "/processdefinition";
			// build list of properties
			List properties = new ArrayList();
			String[] fileNamePropValues = new String[] { pdFileName };
			CmsProperty propfilename = new CmsProperty("filename", fileNamePropValues);
			properties.add(propfilename);
			ByteArrayInputStream bais = new ByteArrayInputStream(pdFileContent);
			// store all information into cms
			SetOperation setOp = new SetOperation(tempPath, SetOperation.TYPE_CONTENT, true, properties);
			setOp.setContent(bais);
	        CmsManager manager = new CmsManager();
			manager.execSetOperation(setOp);
			bais.close();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "storeBookletProcessDefinition", "Cannot save the process definition");
		} finally {	}
	}
	



	public void storeTemplateImage(String pathBooklet, byte[] image, String docLogicalName, int indexTempPart) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathBooklet + "/document_parts/part_" + indexTempPart + "/images/" + docLogicalName;
			List properties = new ArrayList();
			String[] namePropValues = new String[] { docLogicalName };
			CmsProperty propname = new CmsProperty("logicalname", namePropValues);
			properties.add(propname);
			SetOperation setOp = new SetOperation(pathImg, SetOperation.TYPE_CONTENT, true, properties);
			ByteArrayInputStream bais = new ByteArrayInputStream(image);
			setOp.setContent(bais);
			manager.execSetOperation(setOp);
			bais.close();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "storeTemplateImage", "Error while storing image of " +
					            "the document with logica name = " + docLogicalName, e);
		} finally {	}
	}




	public Map getImagesOfTemplatePart(String pathBooklet, String indPart) {
		Map images = new HashMap();
		try{
			CmsManager manager = new CmsManager();
			String path = pathBooklet + "/document_parts/part_" + indPart + "/images";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("true");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("false");
			CmsNode node = manager.execGetOperation(getOp);
			List imageNodes = node.getChilds();
			Iterator iterImgs = imageNodes.iterator();
			while(iterImgs.hasNext()) {
				CmsNode childNode = (CmsNode)iterImgs.next();
				String pathChild = childNode.getPath();
				String logicalname = childNode.getName();
				getOp = new GetOperation(); 
				getOp.setPath(pathChild);
				getOp.setRetriveChildsInformation("false");
				getOp.setRetriveContentInformation("true");
				getOp.setRetrivePropertiesInformation("false");
				getOp.setRetriveVersionsInformation("false");
				childNode = manager.execGetOperation(getOp);
				InputStream is = childNode.getContent();
				byte[] imgBytes = GeneralUtilities.getByteArrayFromInputStream(is);
				images.put(logicalname, imgBytes);
			}
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getImagesOfTemplatePart", "Error while recovering images of the " +
		            			"booklet " + pathBooklet + " part " + indPart, e);
		}
		return images;
	}




	public byte[] getNotesTemplatePart(String pathBooklet, String indPart) {
		byte[] notes = null;
		try{
			CmsManager manager = new CmsManager();
			String path = pathBooklet + "/document_parts/part_" + indPart + "/notes";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("true");
			CmsNode node = manager.execGetOperation(getOp);
			InputStream is = node.getContent();
			notes = GeneralUtilities.getByteArrayFromInputStream(is);
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getNotesTemplatePart", "Error while recovering notes of the " +
		            			"booklet " + pathBooklet + " part " + indPart, e);
		}
		return notes;
	}




	public void storeNote(String pathBooklet, String indPart, byte[] noteContent) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathBooklet + "/document_parts/part_" + indPart + "/notes";
			SetOperation setOp = new SetOperation(pathImg, SetOperation.TYPE_CONTENT, true);
			ByteArrayInputStream bais = new ByteArrayInputStream(noteContent);
			setOp.setContent(bais);
			manager.execSetOperation(setOp);
			bais.close();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "storeNote", "Error while storing new note", e);
		} finally {	}
	}
	



	public void storeFinalDocument(String pathBooklet, byte[] docContent) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathBooklet + "/finalDocument";
			SetOperation setOp = new SetOperation(pathImg, SetOperation.TYPE_CONTENT, true);
			ByteArrayInputStream bais = new ByteArrayInputStream(docContent);
			setOp.setContent(bais);
			manager.execSetOperation(setOp);
			bais.close();
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "storeFinalDocument", "Error while storing final document", e);
		} finally {	}
	}
	
	
	
	
	public byte[] getFinalDocument(String pathBooklet) {
		byte[] doc = null;
		try{
			CmsManager manager = new CmsManager();
			String path = pathBooklet + "/finalDocument";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("false");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("true");
			CmsNode node = manager.execGetOperation(getOp);
			InputStream is = node.getContent();
			doc = GeneralUtilities.getByteArrayFromInputStream(is);
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getFinalDocument", "Error while recovering final document of the " +
		            			"booklet " + pathBooklet, e);
		}
		return doc;
	}


    /*
	public void saveWorkflowConfiguration(String pathBooklet, WorkflowConfiguration workConf) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathBooklet + "/workflowConfiguration";
			SetOperation setOp = new SetOperation(pathImg, SetOperation.TYPE_CONTAINER, true);
			List properties = new ArrayList();
			String[] namePackageValues = new String[] { workConf.getNameWorkflowPackage() };
			CmsProperty propnamePackage = new CmsProperty("nameWorkflowPackage", namePackageValues);
			properties.add(propnamePackage);
			String[] nameProcessValues = new String[] { workConf.getNameWorkflowProcess() };
			CmsProperty propnameProcess = new CmsProperty("nameWorkflowProcess", nameProcessValues);
			properties.add(propnameProcess);
			setOp.setProperties(properties);
			manager.execSetOperation(setOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "saveWorkflowConfiguration", "Error while storing workflow configuration", e);
		} finally {	}
	}




	public WorkflowConfiguration getWorkflowConfiguration(String pathBooklet) {
		WorkflowConfiguration workConf = new WorkflowConfiguration();
		try{
			CmsManager manager = new CmsManager();
			String path = pathBooklet + "/workflowConfiguration";
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(path);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("false");
			CmsNode node = manager.execGetOperation(getOp);
			List props = node.getProperties();
			CmsProperty namePack = getProperty("nameWorkflowPackage", props);
			CmsProperty nameProcess = getProperty("nameWorkflowProcess", props);
			workConf.setNameWorkflowPackage(namePack.getStringValues()[0]);
			workConf.setNameWorkflowProcess(nameProcess.getStringValues()[0]);
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getWorkflowConfiguration", "Error while recovering workflow configuration of the " +
		            			"pamphlet " + pathBooklet, e);
		}
		return workConf;
	}
    */



	


	
}



