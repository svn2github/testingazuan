package it.eng.spago.cms.exec;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.CmsNode;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.CmsVersion;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.exceptions.PathNotValidException;
import it.eng.spago.cms.util.OperationDescriptorUtils;
import it.eng.spago.cms.util.Path;
import it.eng.spago.cms.util.RepositoryNodeUtils;
import it.eng.spago.cms.util.constants.OperationsConstants;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.tracing.TracerSingleton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.version.Version;
import javax.jcr.version.VersionIterator;



/**
 * 
 * Class that define methods for the get operation on the repository
 * 
 */

public class GetObjectImpl {
	
	/**
	 * Method taht perform the get operation on a node of the repository
	 * and return a SourceBean contains all request information.
	 * 	 
	 * @param parameters SourceBean operation descriptor
	 * @param conn Connection to the repository
	 * @return SourceBean contains all request information
	 * @throws EMFInternalError
	 */
	protected CmsNode execute(SourceBean parameters, CMSConnection conn) throws OperationExecutionException {
		
		CmsNode returnNode = null;
		Session session = conn.getSession();
        String pathStr = null;  
		String verNameStr = null;
		String verLabelStr = null;
		Boolean getVersions;
		Boolean getContent;
		Boolean getChilds;
		Boolean getProperties;
		boolean versionExist;
		boolean isContainer; 
		Node node = null;
		
		try {
			// get path string new object
		    pathStr = OperationDescriptorUtils.getStringAttribute(parameters, OperationsConstants.PATH);
			// get version name to retrive  (version name can be null)			
            verNameStr = OperationDescriptorUtils.getStringAttribute(parameters, OperationsConstants.VERSION);
            // get flag of required information
            getVersions = OperationDescriptorUtils.getBooleanAttribute(parameters, OperationsConstants.GETVERSIONS);
            if(getVersions == null) { getVersions = new Boolean(false); }
            getChilds = OperationDescriptorUtils.getBooleanAttribute(parameters, OperationsConstants.GETCHILDS);
            if(getChilds == null) { getChilds = new Boolean(false); }
            getProperties = OperationDescriptorUtils.getBooleanAttribute(parameters, OperationsConstants.GETPROPERTIES);
            if(getProperties == null) { getProperties = new Boolean(false); }
            getContent = OperationDescriptorUtils.getBooleanAttribute(parameters, OperationsConstants.GETCONTENT);
            if(getContent == null) { getContent = new Boolean(false); }
            // create the path object  
			Path path = Path.create(pathStr);
			// get node
			if(path.isRootNode()) {
				node = session.getRootNode();
				isContainer = true;
			} else {
		    	node = (Node)session.getRootNode().getNode(path.getRootRelativePathStr());
			}            
			// if version name is set control if version exist
			if( (verNameStr!= null) && !verNameStr.trim().equalsIgnoreCase("") ) {
				versionExist = RepositoryNodeUtils.versionNameExist(node, verNameStr);
				if(!versionExist) {
		    		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
			    	"GetObjectImpl::execute: version doesn't exists");				
				    throw new OperationExecutionException("GetObjectImpl::execute: version doesn't exists");
		    	}
			}
            // checkout node
			if(node.isNodeType("mix:versionable")) {
				if(!node.isCheckedOut()) {
					node.checkout();
				}
			}
			
			
			
			returnNode = toNode(conn, node, verNameStr, getVersions.booleanValue(), 
								getChilds.booleanValue(), getProperties.booleanValue(), 
								getContent.booleanValue());
			 						
		} catch (PathNotFoundException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MINOR,
	    	"GetObjectImpl::execute: object not found");	
		} catch (RepositoryException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
	    	"GetObjectImpl::execute: repository acess error");
			throw new OperationExecutionException("GetObjectImpl::execute: repository acess error");
		} catch (PathNotValidException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
	    	"GetObjectImpl::execute: malformed path");
			throw new OperationExecutionException("GetObjectImpl::execute: malformed path");
		} 
		
		// return the result sourceBean
		return returnNode;
	}

	
	
	
	
	/**
	 * Retrive request information from a node a store them 
	 * inside a SourceBean with a fixed structure (see 
	 * Documentaion of Spago CMS)
	 * 
	 * @param conn, connection to the repository
	 * @param node, node from which retrive information
	 * @param ver, name version to retrive
	 * @param incVer, flag to include versions informations
	 * @param incChild, flag to include childs informations
	 * @param incProp, flag to include properties informations
	 * @param incCont, flag to include Content information
	 * @return CmsNode
	 * @throws EMFInternalError
	 */
	private CmsNode toNode(CMSConnection conn, Node node, String ver, boolean incVer, 
			               boolean incChild, boolean incProp, boolean incCont) 
	                       throws OperationExecutionException {
	
		CmsNode cmsnode = null;
		String prefixSys = conn.getPrefixPropertySystem();
		String prefixUsr = conn.getPrefixPropertyUser();
		String objectType = "";
		boolean requestVer = false;
		if( (ver != null) && (!ver.trim().equals("")) ) {
			requestVer = true;
		}
		Node version = null;
	  	// control if node is container or content
		boolean isContainer = true;
		try {
			Property typeProp = node.getProperty( prefixSys + ":"+OperationsConstants.TYPE);
			try{
				Value[] typePropValues = typeProp.getValues();
				Value typePropVal = typePropValues[0];
				objectType = typePropVal.getString();
			} catch (Exception e) {
				Value typePropVal = typeProp.getValue();
				objectType = typePropVal.getString();
			}
			if(objectType.equalsIgnoreCase(OperationsConstants.CONTAINER)) {
				isContainer = true;
			} else {
				isContainer = false;
			}
		}
		catch (PathNotFoundException pnfe) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
								"GetObjectImpl::toSourceBean: cannot retrive property type of the node", pnfe);				
		    throw new OperationExecutionException("GetObjectImpl::execute: " +
		    					"cannot retrive property type of the node, path not found");
		}
		catch (Exception e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
	    	"GetObjectImpl::toSourceBean: impossible to retrive property type of the node", e);				
		    throw new OperationExecutionException("GetObjectImpl::execute: " +
		    		"impossible to retrive property type of the node");
		}
		
		try {
			if(requestVer) {
				try {
					version = node.getVersionHistory().getVersion(ver).getNodes().nextNode();
				} catch (Exception e) {
					requestVer = false;
				}
			}
			
			String name = "";
			String uuid = "";
			String versionname = "";
			String path = "";
			String type = "";
			name = node.getName();
			path = node.getPath();
		    try{
		    	versionname = node.getBaseVersion().getName();
		    } catch(Exception e) { /* ignore */ }
			try{
				uuid = node.getUUID();
			} catch(Exception e) { /* ignore */	}
			if(isContainer) {
				type = OperationsConstants.CONTAINER;
			} else {
				type = OperationsConstants.CONTENT;
			}
			cmsnode = new CmsNode(name, path, versionname, uuid, type);
			
			
			
			
			// retrive versions
			List versions = new ArrayList();
			if(incVer && !requestVer) {
				SourceBean versionsSB = new SourceBean(OperationsConstants.VERSIONS);
				if(node.isNodeType("mix:versionable")) {
					try{
						VersionIterator vit = node.getVersionHistory().getAllVersions();
						while (vit.hasNext()){
							Version v = (Version)vit.next();
							if(!v.getName().equalsIgnoreCase("jcr:rootVersion")) {
								String namever = v.getName();
								String dataCreation = v.getCreated().getTime().toString();
								CmsVersion cmsver = new CmsVersion(namever, dataCreation);
								versions.add(cmsver);
							}
						}
					} catch (Exception e) {
						// ignore
					}
				}
				cmsnode.setVersions(versions);
			}
			
			
			
			
			// get properties
			List props = new ArrayList();
			if(incProp) {
				Node nodeForProp = node;
				if(requestVer){
					nodeForProp = version;
				}
				PropertyIterator properties = nodeForProp.getProperties(prefixUsr + ":*");
				while (properties.hasNext()){
					CmsProperty cmsprop = null;
					Property property = (Property)properties.next();				
					String nameprop = property.getName();
					nameprop = nameprop.substring(prefixUsr.length()+1);
					Value[] values = null;
					try {
						values = property.getValues();
					} catch (Exception e) {
						values = new Value[1];
						values[0] = property.getValue();
					}
					int codType = values[0].getType();
					String proptype = PropertyType.nameFromValue(codType);	
					int numValues = values.length;
					if(proptype.equalsIgnoreCase(CmsProperty.TYPE_STRING)) {
						String[] propvalues = new String[numValues];
						for(int i=0; i<values.length; i++)
							propvalues[i] = values[i].getString();
						cmsprop = new CmsProperty(nameprop, propvalues);
					}
					if(proptype.equalsIgnoreCase(CmsProperty.TYPE_LONG)) {
						Long[] propvalues = new Long[numValues];
						for(int i=0; i<values.length; i++)
							propvalues[i] = new Long(values[i].getLong());
						cmsprop = new CmsProperty(nameprop, propvalues);
					}
					if(proptype.equalsIgnoreCase(CmsProperty.TYPE_DOUBLE)) {
						Double[] propvalues = new Double[numValues];
						for(int i=0; i<values.length; i++)
							propvalues[i] = new Double(values[i].getString());
						cmsprop = new CmsProperty(nameprop, propvalues);
					}
					if(proptype.equalsIgnoreCase(CmsProperty.TYPE_DATE)) {
						Calendar[] propvalues = new Calendar[numValues];
						for(int i=0; i<values.length; i++)
							propvalues[i] = values[i].getDate();
						cmsprop = new CmsProperty(nameprop, propvalues);
					}
					if(proptype.equalsIgnoreCase(CmsProperty.TYPE_BOOLEAN)) {
						Boolean[] propvalues = new Boolean[numValues];
						for(int i=0; i<values.length; i++)
							propvalues[i] = new Boolean(values[i].getBoolean());
						cmsprop = new CmsProperty(nameprop, propvalues);
					}
					if(proptype.equalsIgnoreCase(CmsProperty.TYPE_BINARY)) {
						InputStream[] propvalues = new InputStream[numValues];
						for(int i=0; i<values.length; i++)
							propvalues[i] = values[i].getStream();
						cmsprop = new CmsProperty(nameprop, propvalues);
					}
					props.add(cmsprop);
				}
				cmsnode.setProperties(props);
			}
			
			
			
			
			
			// getChilds
			List childList = new ArrayList();
			if(incChild && isContainer) {
				Node nodeForChild = node;
				if(requestVer){
					nodeForChild = version;
				}		
				NodeIterator childs = nodeForChild.getNodes();
				while (childs.hasNext()){
					CmsNode cmsChildNode = null;
					String nameChildNode = "";
					String pathChild = "";
					String uuidChild = "";
					String objectTypeChild = "";
					Node child = (Node)childs.next();
					// if node starts with jcr prefix is a system node 
					nameChildNode = child.getName();
					if(nameChildNode.startsWith("jcr:")) {
						continue;
					}
					if(!requestVer) {
						pathChild = child.getPath();
					}
					try {
						uuidChild = child.getUUID();
					} catch (Exception e) { /* ignore */ }
					try{
						Property childTypeProp = child.getProperty(prefixSys + ":" + OperationsConstants.TYPE);
						try{
							Value[] childTypePropVals = childTypeProp.getValues();
							Value childTypePropVal = childTypePropVals[0];
							objectTypeChild = childTypePropVal.getString();
						} catch (Exception e) {
							Value childTypePropVal = childTypeProp.getValue();
							objectTypeChild = childTypePropVal.getString();
						}
					} catch (Exception e) { 
						/* ignore */ 
					}
					cmsChildNode = new CmsNode(nameChildNode, pathChild, "", uuidChild, objectTypeChild);
					childList.add(cmsChildNode);
				}
				cmsnode.setChilds(childList);
			}

		    
		    // get content
            if(incCont && !isContainer) { 
            	Node nodeForCont = node;
				if(requestVer){
					nodeForCont = version;
				}
				try {
					Property propjcr = nodeForCont.getProperty(prefixSys + ":" + OperationsConstants.STREAM);
					InputStream stream = null;  
					try{
						Value[] valuesjcr = propjcr.getValues();
						Value contval = valuesjcr[0];
						stream = contval.getStream();
					} catch (Exception e) {
						stream = propjcr.getStream(); 
					}
					cmsnode.setContent(stream);
				} catch (Exception e) {
					cmsnode.setContent(null);
				}
			}
		    
		    
		}
		catch (SourceBeanException e){
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR,
	    	"GetObjectImpl::toSourceBean: sourceBean error ");
			throw new OperationExecutionException("GetObjectImpl::toSourceBean: sourceBean error", e);
		} catch (PathNotFoundException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MINOR,
	    	"GetObjectImpl::toSourceBean: path not find");
			throw new OperationExecutionException("GetObjectImpl::toSourceBean: path not find", e);
		} catch (RepositoryException e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL,
	    	"GetObjectImpl::toSourceBean: repository access error");
			throw new OperationExecutionException("GetObjectImpl::toSourceBean: repository access error", e);
		}
		
		return cmsnode;
	}
	
	
}
	
	

	
	
