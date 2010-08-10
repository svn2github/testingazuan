package it.eng.spago.cms.operations;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.tracing.TracerSingleton;


 /*
 	<OPERATION name="">
  	    <GETOPERATION path="?" version="?" getVersions="(true/false)" getChilds="(true/false)" 
  	    			  getContent="(true/false)" getProperties="(true/false)" />
  	</OPERATION>
 */


public class GetOperation {

	private SourceBean operationDescriptor = null;
	
	public GetOperation() throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_GET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, "");
			operationDescriptor.setAttribute(Constants.VERSION, "");
			operationDescriptor.setAttribute(Constants.GETCHILDS, "true");
			operationDescriptor.setAttribute(Constants.GETCONTENT, "true");
			operationDescriptor.setAttribute(Constants.GETPROPERTIES, "true");
			operationDescriptor.setAttribute(Constants.GETVERSIONS, "true");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::<init>(): Error during the creation of the get operation" +
                                "SourceBean");
			throw new BuildOperationException("GetOperation::<init>(): Error during the creation of the " +
					                          "get operation SourceBean", e);
		}
	}
	
	
	
	public GetOperation(String path, boolean getChild, boolean getProps, 
			            boolean getCont, boolean getVers) throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_GET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.VERSION, "");
			if(getChild)
				operationDescriptor.setAttribute(Constants.GETCHILDS, "true");
			else 
				operationDescriptor.setAttribute(Constants.GETCHILDS, "false");
			if(getCont)
				operationDescriptor.setAttribute(Constants.GETCONTENT, "true");
			else 
				operationDescriptor.setAttribute(Constants.GETCONTENT, "false");
			if(getProps)
				operationDescriptor.setAttribute(Constants.GETPROPERTIES, "true");
			else 
				operationDescriptor.setAttribute(Constants.GETPROPERTIES, "false");
            if(getVers)
            	operationDescriptor.setAttribute(Constants.GETVERSIONS, "true");
            else 
            	operationDescriptor.setAttribute(Constants.GETPROPERTIES, "false");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::<init>(String, boolean, boolean, boolean, boolean): " +
                                "Error during the creation of the get operation" +
                                "SourceBean");
			throw new BuildOperationException("GetOperation::<init>(String, boolean, boolean, boolean, boolean): " +
											  "Error during the creation of the " +
					                          "get operation SourceBean", e);
		}
	}
	
	
	
	
	public GetOperation(String path, String version, boolean getChild, boolean getProps, 
            			boolean getCont, boolean getVers) throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_GET_OPERATION);
			operationDescriptor.setAttribute(Constants.PATH, path);
			operationDescriptor.setAttribute(Constants.VERSION,	version);
			if(getChild)
				operationDescriptor.setAttribute(Constants.GETCHILDS, "true");
			else 
				operationDescriptor.setAttribute(Constants.GETCHILDS, "false");
			if(getCont)
				operationDescriptor.setAttribute(Constants.GETCONTENT, "true");
			else 
				operationDescriptor.setAttribute(Constants.GETCONTENT, "false");
			if(getProps)
				operationDescriptor.setAttribute(Constants.GETPROPERTIES, "true");
			else 
				operationDescriptor.setAttribute(Constants.GETPROPERTIES, "false");
			if(getVers)
				operationDescriptor.setAttribute(Constants.GETVERSIONS, "true");
			else 
				operationDescriptor.setAttribute(Constants.GETPROPERTIES, "false");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                    "GetOperation::<init>(String, String, boolean, boolean, boolean, boolean): " +
                    "Error during the creation of the get operation" +
                    "SourceBean");
			throw new BuildOperationException("GetOperation::<init>(String, String, boolean, boolean, boolean, boolean): " +
								  "Error during the creation of the " +
		                          "get operation SourceBean", e);
		}
	}
	
	
	public SourceBean getOperationDescriptor() {
		return operationDescriptor;
	}

	
	public void setPath(String path) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.PATH, path);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::setPath: Error during the path setting");
            throw new BuildOperationException("GetOperation::setPath: Error during the path setting", e);
		}
	}
	
	public void setVersion(String ver) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.VERSION, ver);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::setVersion: Error during the vversion setting");
            throw new BuildOperationException("GetOperation::setVersion: Error during the version setting", e);
		}
	}
	
	public void setRetriveVersionsInformation(String bool) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.GETVERSIONS, bool);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::setRetriveVersionsInformation: Error during the getVersion setting");
            throw new BuildOperationException("GetOperation::setRetriveVersionsInformation: Error during the getVersion setting", e);
		}
	}
	
	public void setRetriveContentInformation(String bool) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.GETCONTENT, bool);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::setRetriveContentInformation: Error during the getContent setting");
            throw new BuildOperationException("GetOperation::setRetriveContentInformation: Error during the getContent setting", e);
		}
	}

	
	public void setRetrivePropertiesInformation(String bool) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.GETPROPERTIES, bool);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::setRetrivePropertiesInformation: Error during the getProperties setting");
            throw new BuildOperationException("GetOperation::setRetrivePropertiesInformation: Error during the getProperties setting", e);
		}
	}
	
	public void setRetriveChildsInformation(String bool) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.GETCHILDS, bool);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "GetOperation::setRetriveChildsInformation: Error during the getChilds setting");
            throw new BuildOperationException("GetOperation::setRetriveChildsInformation: Error during the getChilds setting", e);
		}
	}
}
