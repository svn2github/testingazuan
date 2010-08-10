package it.eng.spago.cms.exec;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.exceptions.OperationExecutionException;
import it.eng.spago.cms.exceptions.PathNotValidException;
import it.eng.spago.cms.exceptions.ValueCastException;
import it.eng.spago.cms.util.OperationDescriptorUtils;
import it.eng.spago.cms.util.Path;
import it.eng.spago.cms.util.Utils;
import it.eng.spago.cms.util.constants.CMSConstants;
import it.eng.spago.cms.util.constants.OperationsConstants;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;



/**
 * 
 * Implements method that perform the set operation
 * on the repository
 * 
 */
public class SetObjectImpl {

	
	/**
	 * Implements the set operation on the repository
	 * 
	 * @param parameters, SourceBean of operation configuration
	 * @param conn, Connection to the repository
	 * @param roles, Array of user roles string names 
	 * @return SourceBean that contains information about execution
	 * @throws OperationExecutionException, if some error occurs during
	 * the execution 
	 */
	protected void execute(SourceBean parameters, CMSConnection conn) throws OperationExecutionException{
		
		Session _session = conn.getSession();
		String prefixSys = conn.getPrefixPropertySystem();
		String prefixUsr = conn.getPrefixPropertyUser();
		String nodeName;
		String pathStr;
		String pathFatherStr;
		Path path;
		Path pathFather;
		String type;
		Node node = null;
		Node nodeFather = null;
		Node rootNode = null;
		InputStream data = null;
		Hashtable properties = new Hashtable();
		boolean nodeExist;
		boolean nodeFatherExist;
		boolean cancProps = false;
		String method = "SetObjectImpl::execute(SourceBean,CmsConnection):";
		
		
		try{ 
			
			// ********************************************************************************
			// SET VARIABLE
			// ********************************************************************************
						
            // get root node
			rootNode = _session.getRootNode();
			// get path string new object
		    pathStr = OperationDescriptorUtils.getStringAttribute(parameters, OperationsConstants.PATH);
		    // get flag cancel prop
		    Boolean cancPropB = OperationDescriptorUtils.getBooleanAttribute(parameters, OperationsConstants.OLDPROPERTIES);
		    if(cancPropB != null) {
		    	cancProps = cancPropB.booleanValue();
		    }
		    // get type object
		    type = OperationDescriptorUtils.getStringAttribute(parameters, CMSConstants.TYPE);
		    // if object is type content get stream data
		    if(type.equalsIgnoreCase(OperationsConstants.CONTENT)) {
		    	data = (InputStream)OperationDescriptorUtils.getObjectAttribute(parameters, OperationsConstants.STREAM);
		    }
		    // get path object
		    path = Path.create(pathStr);
		    // cotrol if path is root
		    if(path.isRootNode()) {
		    	throw new OperationExecutionException("SetObjectImpl:execute: it's not possible to " +
		    									      "execute set operation on root node");
		    } else {
		    	 // get name node
			    nodeName = path.getNameLastElement();
		    }
		    // get path father
		    pathFather = path.getPathAncestor(1); 
		    // get properties
		    properties = OperationDescriptorUtils.getProperties(parameters);
		    //  control if node exists
		    try {
		    	node = rootNode.getNode(path.getRootRelativePathStr());
		    	nodeExist = true;
		    } catch (Exception e) {
		    	nodeExist = false;
		    }
		    // control if father node exists
		    try {
		    	if(pathFather.isRootNode()) {
		    		nodeFather = rootNode;
		    	} else {
		    		nodeFather = rootNode.getNode(pathFather.getRootRelativePathStr());
		    	}
		    	nodeFatherExist = true;
		    } catch (Exception e) {
		    	nodeFatherExist = false;
		    }
		    
      
		    
            // if the node is a content type and there's  no content to store throw an exception
		    if(type.equals(CMSConstants.CONTAINER) && (data != null)) {
		    	throw new OperationExecutionException("SetObjectImpl::execute: a container can have properties " +
		    				                          "but not content bytes");
		    }
		    // if the node exists but has a different type from the new one throw an exception
		    if(nodeExist) {
		    	String typeSetted = "";
		    	Property propType = node.getProperty(prefixSys+":"+OperationsConstants.TYPE);
		    	try{
		    		Value[] typeValues = propType.getValues();
		    		typeSetted = typeValues[0].getString();
		    	} catch(Exception e) {
		    		typeSetted = propType.getValue().getString();
		    	}
		    	if(!typeSetted.equals(type)) {
		    		throw new OperationExecutionException("SetObjectImpl::execute: node already exists but types " +
		    							                  "are not correspondent ");
		    	}
		    }
            // if the father of the node doesn't exist throw an exception 
		    // TODO creation of the father chain if some of the node don't exist
		    if(!nodeFatherExist) {
		    	throw new OperationExecutionException("SetObjectImpl::execute: node father doesn't exist ");
		    }				        
            // if the node doesn't exist it's necessary to create it otherwise the node
		    // must be chekout
		    if(!nodeExist){
		    	node = nodeFather.addNode(nodeName, "nt:unstructured");
			    node.addMixin("mix:versionable");
			} else {
				if(!node.isCheckedOut()) {
				   	node.checkout();
				}
			}
		    // get value Factory
		    ValueFactory vf = _session.getValueFactory();
		    Utils.debug(method + "ValueFactory obtained: " + vf + "\n");
			// set type and eventual content
		    Value typeVal = vf.createValue(type);
		    Value[] typeValues = {typeVal};
		    try {
		    	node.setProperty(prefixSys + ":"+OperationsConstants.TYPE, typeValues);
		    	Utils.debug(method + "Type Property setted as multivalue \n");
		    } catch (Exception e){
		    	node.setProperty(prefixSys + ":"+OperationsConstants.TYPE, type);
		    	Utils.debug(method + "Type Property setted as single value \n");
		    }
            // if the node is a content node store the inputstream data
			if(type.equalsIgnoreCase(CMSConstants.CONTENT)) {
				Utils.debug(method + "Node ia content node: start store the content \n");
		        Value contVal = vf.createValue(data);
		    	Value[] contValues = new Value[1];
		    	contValues[0] = contVal;
		    	try{
		    		node.setProperty(prefixSys+":"+OperationsConstants.STREAM, contValues);
		    		Utils.debug(method + "content stored  as multivalue \n");
		    	} catch (Exception e) {
		    		node.setProperty(prefixSys+":"+OperationsConstants.STREAM, data);
		    		Utils.debug(method + "content stored  as single value \n");
		    	}
		    }
		    // if cancel old properties is requested it's necessary to erase all user property of the node
		    if(cancProps) {
		    	PropertyIterator propIter = node.getProperties();
		    	while(propIter.hasNext()) {
		    		Property prop = propIter.nextProperty();
		    		String nameP = prop.getName();
		    		if(nameP.startsWith(prefixUsr) ) {
		    			prop.remove();
		    		}
		    	}
		    	if(nodeExist) {
		    		node.save();
		    	}
		    }
		    // set new properties
		    Enumeration propEnum = properties.keys();
		    while(propEnum.hasMoreElements()){
		    	String nameProp = (String)propEnum.nextElement();
		    	CmsProperty prop = (CmsProperty)properties.get(nameProp);
		    	Value[] jcrvalues = getJCRValues(vf, prop);
		    	if(jcrvalues.length == 0) {
                	continue;
                }
                nameProp = prefixUsr + ":" + nameProp;
                node.setProperty(nameProp, jcrvalues);
		    }
		    // save
		    if(nodeExist) {
		        node.save();
		    	node.checkin();
		    	node.checkout();
		    }
		    else {
		      nodeFather.save();
		      node.checkin();
		      node.checkout();
		    }
		}
		catch(PathNotValidException mpe){
			throw new OperationExecutionException("SetObjectImpl::execute: malformed path", mpe);
		}
		catch(RepositoryException re){
			throw new OperationExecutionException("SetObjectImpl::execute: repository acess error", re);
		}
		catch(ValueCastException vce) {
			throw new OperationExecutionException("SetObjectImpl::execute: property value cast exception", vce);
		}
	}
	
	
	
	
	
	
	
	public Value[] getJCRValues(ValueFactory vf, CmsProperty prop) throws ValueCastException {
		Value[] jcrvalues = null;
		String type = prop.getType();
		if(type.equalsIgnoreCase(CmsProperty.TYPE_STRING)) {
			String[] values = prop.getStringValues();
		    int numValues = values.length;
		    jcrvalues = new Value[numValues];
		    for(int i=0; i<values.length; i++) {
    			jcrvalues[i] = vf.createValue(values[i]);
    		}
		}
		if(type.equalsIgnoreCase(CmsProperty.TYPE_LONG)) {
			Long[] values = prop.getLongValues();
		    int numValues = values.length;
		    jcrvalues = new Value[numValues];
		    for(int i=0; i<values.length; i++) {
		    	Long lObj = values[i];
		    	long l = lObj.longValue();
    			jcrvalues[i] = vf.createValue(l);
    		}
		}
		if(type.equalsIgnoreCase(CmsProperty.TYPE_DOUBLE)) {
			Double[] values = prop.getDoubleValues();
		    int numValues = values.length;
		    jcrvalues = new Value[numValues];
		    for(int i=0; i<values.length; i++) {
    			Double dObj = values[i];
    			double d = dObj.doubleValue();
		    	jcrvalues[i] = vf.createValue(d);
    		}
		}
		if(type.equalsIgnoreCase(CmsProperty.TYPE_DATE)) {
			Calendar[] values = prop.getDateValues();
		    int numValues = values.length;
		    jcrvalues = new Value[numValues];
		    for(int i=0; i<values.length; i++) {
    			jcrvalues[i] = vf.createValue(values[i]);
    		}
		}
		if(type.equalsIgnoreCase(CmsProperty.TYPE_BOOLEAN)) {
			Boolean[] values = prop.getBooleanValues();
		    int numValues = values.length;
		    jcrvalues = new Value[numValues];
		    for(int i=0; i<values.length; i++) {
		    	Boolean bObj = values[i];
		    	boolean b = bObj.booleanValue();
    			jcrvalues[i] = vf.createValue(b);
    		}
		}
		if(type.equalsIgnoreCase(CmsProperty.TYPE_BINARY)) {
			InputStream[] values = prop.getBinaryValues();
		    int numValues = values.length;
		    jcrvalues = new Value[numValues];
		    for(int i=0; i<values.length; i++) {
    			jcrvalues[i] = vf.createValue(values[i]);
    		}
		}
		return jcrvalues;
    }
	
	
	
	
	
	
}
