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

package it.eng.spagobi.pamphlets.dao;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.cms.operations.GetOperation;
import it.eng.spago.cms.operations.SetOperation;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.pamphlets.bo.ConfiguredBIDocument;
import it.eng.spagobi.pamphlets.bo.Pamphlet;
import it.eng.spagobi.pamphlets.bo.WorkflowConfiguration;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.sun.org.apache.xerces.internal.dom.ChildNode;

/**
 * Implements all the methods for access the pamphlets cms contents
 */
public class PamphletsCmsDaoImpl implements IPamphletsCmsDao {

	public List getPamphletList(String pathBasePamp) {
		List pamphlets = new ArrayList();
		try {
			GetOperation getOp = new GetOperation(pathBasePamp, true, false, false, false);
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
			    List properties = childNode.getProperties();
				if(properties==null)
					continue;
			    CmsProperty prop = getProperty("name",properties);
			    if(prop!=null) {
			    	Pamphlet pamp = new Pamphlet();
			    	pamp.setPath(pathChild);
			    	String[] values = prop.getStringValues();
			    	pamp.setName(values[0]);
			    	pamphlets.add(pamp);
			    }
			}
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "getPumphletList", "Cannot recover pumplet list", e);
		} finally {	}
		return pamphlets;
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




	public void addPamphlet(String pathBase, String name) {
		try {
			String newPath = pathBase + "/" + name;
			List properties = new ArrayList();
			String[] namePropValues = new String[] { name };
			CmsProperty propname = new CmsProperty("name", namePropValues);
			properties.add(propname);
			SetOperation setOp = new SetOperation(newPath, SetOperation.TYPE_CONTAINER, true, properties);
	        CmsManager manager = new CmsManager();
			manager.execSetOperation(setOp);
			String documentsConfPathNode = newPath + "/documentsConfigured";
			setOp = new SetOperation(documentsConfPathNode, SetOperation.TYPE_CONTAINER, true);
			manager.execSetOperation(setOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
					            "addPamphlet", "Cannot save the new pamphlet " + name);
		} finally {	}
	}




	public List getConfiguredDocumentList(String pathPamphlet) {
		List docs = new ArrayList();
		try {
			GetOperation getOp = new GetOperation(pathPamphlet+ "/documentsConfigured", true, false, false, false);
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




	public void addConfiguredDocument(String pathPamphlet, ConfiguredBIDocument doc) {
		try {
			// build path 
			String newPath = pathPamphlet + "/documentsConfigured/" + doc.getLogicalName();
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
					            "addPamphlet", "Cannot save the configured document");
		} finally {	}
	}




	public void deleteConfiguredDocument(String pathPamphlet, String conDocIdentifier) {
		try {
			String path = pathPamphlet + "/documentsConfigured/" + conDocIdentifier;
			DeleteOperation delOp = new DeleteOperation(path);
			CmsManager manager = new CmsManager();
			manager.execDeleteOperation(delOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		             			"deleteConfiguredDocument", "Cannot delete the configured document " + conDocIdentifier, e);
		} finally {	}
	}




	public ConfiguredBIDocument getConfigureDocument(String pathPamphlet, String conDocIdentifier) {
		ConfiguredBIDocument confDoc = null;
		try {
			String path = pathPamphlet + "/documentsConfigured/" + conDocIdentifier;
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




	public void storePamphletTemplate(String pathPamphlet, String templateFileName, byte[] templateContent) {
		try {
			// build path 
			String tempPath = pathPamphlet + "/template";
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
					            "storePamphletTemplate", "Cannot save the template");
		} finally {	}
		
	}




	public String getPamphletTemplateFileName(String pathPamphlet) {
		String name = "";
		try{
			String path = pathPamphlet + "/template";
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
		            			"getPamphletTemplateFileName", "Cannot recover template file name");
		}
		return name;
	}




	public InputStream getPamphletTemplateContent(String pathPamphlet) {
		InputStream content = null;
		try{
			String path = pathPamphlet + "/template";
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
		            			"getPamphletTemplateContent", "Cannot recover template content");
		}
		return content;
	}




	public void createStructureForTemplate(String pathPamphlet, int numTempParts) {
		try {
			CmsManager manager = new CmsManager();
			String pathDocParts = pathPamphlet + "/document_parts";
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
					            "pamphlet " + pathPamphlet, e);
		} finally {	}
	}




	public void storeTemplateImage(String pathPamphlet, byte[] image, String docLogicalName, int indexTempPart) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathPamphlet + "/document_parts/part_" + indexTempPart + "/images/" + docLogicalName;
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




	public Map getImagesOfTemplatePart(String pathPamphlet, String indPart) {
		Map images = new HashMap();
		try{
			CmsManager manager = new CmsManager();
			String path = pathPamphlet + "/document_parts/part_" + indPart + "/images";
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
		            			"pamphlet " + pathPamphlet + " part " + indPart, e);
		}
		return images;
	}




	public byte[] getNotesTemplatePart(String pathPamphlet, String indPart) {
		byte[] notes = null;
		try{
			CmsManager manager = new CmsManager();
			String path = pathPamphlet + "/document_parts/part_" + indPart + "/notes";
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
		            			"pamphlet " + pathPamphlet + " part " + indPart, e);
		}
		return notes;
	}




	public void storeNote(String pathPamphlet, String indPart, byte[] noteContent) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathPamphlet + "/document_parts/part_" + indPart + "/notes";
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
	
	
	
	
	public String getPamphletName(String pathPamp) {
		String name = "";
		try{
			CmsManager manager = new CmsManager();
			GetOperation getOp = new GetOperation(); 
			getOp.setPath(pathPamp);
			getOp.setRetriveChildsInformation("false");
			getOp.setRetrivePropertiesInformation("true");
			getOp.setRetriveVersionsInformation("false");
			getOp.setRetriveContentInformation("false");
			CmsNode node = manager.execGetOperation(getOp);
			List props = node.getProperties();
			CmsProperty prop = getProperty("name", props);
			name = prop.getStringValues()[0];
		} catch(Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		            			"getPamphletName", "Error while recovering name of the of the " +
		            			"pamphlet " + pathPamp, e);
		}
		return name;	
	}




	public void storeFinalDocument(String pathPamphlet, byte[] docContent) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathPamphlet + "/finalDocument";
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
	
	
	
	
	public byte[] getFinalDocument(String pathPamphlet) {
		byte[] doc = null;
		try{
			CmsManager manager = new CmsManager();
			String path = pathPamphlet + "/finalDocument";
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
		            			"pamphlet " + pathPamphlet, e);
		}
		return doc;
	}




	public void deletePamphlet(String pathPamphlet) {
		try {
			DeleteOperation delOp = new DeleteOperation(pathPamphlet);
			CmsManager manager = new CmsManager();
			manager.execDeleteOperation(delOp);
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBI", this.getClass().getName(),
		             			"deletePamphlet", "Cannot delete pamphlet " + pathPamphlet, e);
		} finally {	}
	}




	public void saveWorkflowConfiguration(String pathPamphlet, WorkflowConfiguration workConf) {
		try {
			CmsManager manager = new CmsManager();
			String pathImg = pathPamphlet + "/workflowConfiguration";
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




	public WorkflowConfiguration getWorkflowConfiguration(String pathPamphlet) {
		WorkflowConfiguration workConf = new WorkflowConfiguration();
		try{
			CmsManager manager = new CmsManager();
			String path = pathPamphlet + "/workflowConfiguration";
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
		            			"pamphlet " + pathPamphlet, e);
		}
		return workConf;
	}

	
	
	
}



