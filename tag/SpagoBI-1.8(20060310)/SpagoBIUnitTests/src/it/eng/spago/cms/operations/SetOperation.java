package it.eng.spago.cms.operations;

import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.tracing.TracerSingleton;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/*
 	<OPERATION name="">
  		<SETOPERATION path="" type="(container/content)" cancelOldProperties="(true/flase)">
  		 	<CONTENT stream="" /> (optional)
  		 	<PROPERTIES> (optional)
  		 		<PROPERTY name="" type="STRING/DATE/LONG/DOUBLE/BOOLEAN/BINARY" >
  		 			<PROPERTYVALUE value=""/>
  		 			<PROPERTYVALUE value=""/>
  		 			....
  		 		</PROPERTY>
  		 		....
  		 	</PROPERTIES>
  		</SETOPERATION>
  	</OPERATION> 


*/
public class SetOperation {

	public final static String TYPE_CONTENT = "content";
	public final static String TYPE_CONTAINER = "container";
	
	private SourceBean operationDescriptor = null;
		
	
	
	
	public SetOperation() throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_SET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, "");
			operationDescriptor.setAttribute(Constants.TYPE, "");
			SourceBean contentSB = new SourceBean(Constants.CONTENTSB);
			contentSB.setAttribute(Constants.STREAMATTR, new ByteArrayInputStream(new byte[0]));
			operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "false");
			SourceBean props = new SourceBean(Constants.PROPERTIES);
			operationDescriptor.setAttribute(props);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::<init>(): Error during the creation of the set operation" +
                                "SourceBean");
			throw new BuildOperationException("SetOperation::<init>(): Error during the creation of the " +
					                          "set operation SourceBean", e);
		}
	}
	
	
	public SetOperation(String path, String type, boolean holdProperties) throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_SET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.TYPE, type);
			SourceBean contentSB = new SourceBean(Constants.CONTENTSB);
			contentSB.setAttribute(Constants.STREAMATTR, new ByteArrayInputStream(new byte[0]));
			if(holdProperties)
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "false");
			else 
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "true");
			SourceBean props = new SourceBean(Constants.PROPERTIES);
			operationDescriptor.setAttribute(props);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::<init>(String, String, boolean): " +
                                "Error during the creation of the set operation" +
                                "SourceBean");
			throw new BuildOperationException("SetOperation::<init>(String, String, booelan): " +
											  "Error during the creation of the " +
					                          "set operation SourceBean", e);
		}
	}
	
	
	public SetOperation(String path, String type, 
			            boolean holdProperties, InputStream content) throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_SET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.TYPE, type);
			SourceBean contentSB = new SourceBean(Constants.CONTENTSB);
			contentSB.setAttribute(Constants.STREAMATTR, content);
			if(holdProperties)
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "false");
			else 
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "true");
			SourceBean props = new SourceBean(Constants.PROPERTIES);
			operationDescriptor.setAttribute(props);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::<init>(String, String, boolean, InputStream): " +
                                "Error during the creation of the set operation" +
                                "SourceBean");
			throw new BuildOperationException("SetOperation::<init>(String, String, booelan, InputStream): " +
											  "Error during the creation of the " +
					                          "set operation SourceBean", e);
		}
	}
	
	
	
	public SetOperation(String path, String type, boolean holdProperties, List cmsProperties) 
						throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_SET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.TYPE, type);
			SourceBean contentSB = new SourceBean(Constants.CONTENTSB);
			contentSB.setAttribute(Constants.STREAMATTR, new ByteArrayInputStream(new byte[0]));
			if(holdProperties)
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "false");
			else 
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "true");
			SourceBean propsSB = buildSourceBeanProperties(cmsProperties);
			operationDescriptor.setAttribute(propsSB);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::<init>(String, String, boolean, List): " +
                                "Error during the creation of the set operation" +
                                "SourceBean");
			throw new BuildOperationException("SetOperation::<init>(String, String, booelan, List): " +
											  "Error during the creation of the " +
					                          "set operation SourceBean", e);
		}
	}
	
	
	
	
	public SetOperation(String path, String type, boolean holdProperties, 
			            List cmsProperties, InputStream content) throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_SET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.TYPE, type);
			SourceBean contentSB = new SourceBean(Constants.CONTENTSB);
			contentSB.setAttribute(Constants.STREAMATTR, content);
			if(holdProperties)
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "false");
			else 
				operationDescriptor.setAttribute(Constants.OLDPROPERTIES, "true");
			
			SourceBean propsSB = buildSourceBeanProperties(cmsProperties);
			operationDescriptor.setAttribute(propsSB);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
								"SetOperation::<init>(String, String, boolean, List): " +
								"Error during the creation of the set operation" +
            					"SourceBean");
			throw new BuildOperationException("SetOperation::<init>(String, String, booelan, List): " +
						  					  "Error during the creation of the " +
						  					  "set operation SourceBean", e);
		}
	}
	
	
	
	public void setPath(String path) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.PATH, path);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::setPath: Error during the path setting");
            throw new BuildOperationException("SetOperation::setPath: Error during the path setting", e);
		}
	}
	
	public void setContent(InputStream is) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.STREAM, is);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::setContent: Error during the content setting");
            throw new BuildOperationException("SetOperation::setContent: Error during the content setting", e);
		}
	}
	
	public void setType(String type) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.TYPE, type);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::setType: Error during the type setting");
            throw new BuildOperationException("SetOperation::setType: Error during the type setting", e);
		}
	}
	
	public void setEraseOldProperties(boolean erase) throws BuildOperationException {
		String eraseStr = "false";
		if(erase) {
			eraseStr = "true";
		}
		try {
			operationDescriptor.updAttribute(Constants.OLDPROPERTIES, eraseStr);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::setEraseOldProperties: Error during the erase old properties setting");
            throw new BuildOperationException("SetOperation::etEraseOldProperties: Error during the erase old properties setting", e);
		}
	}
	
	
	
	public void setProperties(List properties) throws BuildOperationException {	
		try{
			SourceBean props = buildSourceBeanProperties(properties);
			operationDescriptor.delAttribute(Constants.PROPERTIES);
			operationDescriptor.setAttribute(props);
		}catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SetOperation::setProperties: Error during the properties setting");
            throw new BuildOperationException("SetOperation::setProperties: Error during the properties setting", e);
		}
	}
	
	
	private SourceBean buildSourceBeanProperties(List properties) {
		SourceBean propsSB = null;
		try {
			propsSB = new SourceBean(Constants.PROPERTIES);
			Iterator iterProps = properties.iterator();
			while(iterProps.hasNext()) {
				CmsProperty prop = (CmsProperty)iterProps.next();
				String name = prop.getName();
				String type = prop.getType();
				SourceBean propSB = new SourceBean(Constants.PROPERTY);
				propSB.setAttribute("name", name);
				propSB.setAttribute("type", type);
			    if(type.equalsIgnoreCase(CmsProperty.TYPE_STRING)) {
			    	String [] values = prop.getStringValues();
			    	for(int i=0; i<values.length; i++) {
			    		SourceBean valuePropSB = new SourceBean(Constants.VALUE_PROPERTY_SB);
			    		valuePropSB.setAttribute("value", values[i]);
			    		propSB.setAttribute(valuePropSB);
			    	}
			    }
			    if(type.equalsIgnoreCase(CmsProperty.TYPE_LONG)) {
			    	Long [] values = prop.getLongValues();
			    	for(int i=0; i<values.length; i++) {
			    		SourceBean valuePropSB = new SourceBean(Constants.VALUE_PROPERTY_SB);
			    		valuePropSB.setAttribute("value", values[i]);
			    		propSB.setAttribute(valuePropSB);
			    	}
			    }
			    if(type.equalsIgnoreCase(CmsProperty.TYPE_DOUBLE)) {
			    	Double [] values = prop.getDoubleValues();
			    	for(int i=0; i<values.length; i++) {
			    		SourceBean valuePropSB = new SourceBean(Constants.VALUE_PROPERTY_SB);
			    		valuePropSB.setAttribute("value", values[i]);
			    		propSB.setAttribute(valuePropSB);
			    	}
			    }
			    if(type.equalsIgnoreCase(CmsProperty.TYPE_DATE)) {
			    	Calendar [] values = prop.getDateValues();
			    	for(int i=0; i<values.length; i++) {
			    		SourceBean valuePropSB = new SourceBean(Constants.VALUE_PROPERTY_SB);
			    		valuePropSB.setAttribute("value", values[i]);
			    		propSB.setAttribute(valuePropSB);
			    	}
			    }
			    if(type.equalsIgnoreCase(CmsProperty.TYPE_BOOLEAN)) {
			    	Boolean [] values = prop.getBooleanValues();
			    	for(int i=0; i<values.length; i++) {
			    		SourceBean valuePropSB = new SourceBean(Constants.VALUE_PROPERTY_SB);
			    		valuePropSB.setAttribute("value", values[i]);
			    		propSB.setAttribute(valuePropSB);
			    	}
			    }
			    if(type.equalsIgnoreCase(CmsProperty.TYPE_BINARY)) {
			    	InputStream [] values = prop.getBinaryValues();
			    	for(int i=0; i<values.length; i++) {
			    		SourceBean valuePropSB = new SourceBean(Constants.VALUE_PROPERTY_SB);
			    		valuePropSB.setAttribute("value", values[i]);
			    		propSB.setAttribute(valuePropSB);
			    	}
			    }
			    // set into the properties SB the single property SB
			    propsSB.setAttribute(propSB);
			}
		} catch (Exception e) {
			return null;
		}
		return propsSB;
	}

	
	public SourceBean getOperationDescriptor() {
		return operationDescriptor;
	}

	


	
	
}
