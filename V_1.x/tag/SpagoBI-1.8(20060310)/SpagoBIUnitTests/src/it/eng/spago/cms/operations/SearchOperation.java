package it.eng.spago.cms.operations;

import it.eng.spago.base.SourceBean;
import it.eng.spago.cms.exceptions.BuildOperationException;
import it.eng.spago.cms.constants.Constants;
import it.eng.spago.tracing.TracerSingleton;

/*
    <OPERATION name="">
  	    <SEARCHOPERATION query="?" language="(xpath/sql)" />
  	</OPERATION>
*/

public class SearchOperation {

	private SourceBean operationDescriptor = null;
	
	public SearchOperation() throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_SEARCH_OPERATION);
			operationDescriptor.setAttribute(Constants.QUERY_ATTRIBUTE, "");
			operationDescriptor.setAttribute(Constants.LANGUAGE_ATTRIBUTE, "xpath");
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SearchOperation::<init>(): Error during the creation of the search operation" +
                                "SourceBean");
			throw new BuildOperationException("SearchOperation::<init>(): Error during the creation of the " +
					                          "search operation SourceBean", e);
		}
	}
	
	
	public SearchOperation(String query, String language) throws BuildOperationException {	
		try {
			operationDescriptor = new SourceBean(Constants.NAME_SEARCH_OPERATION);
			operationDescriptor.setAttribute(Constants.QUERY_ATTRIBUTE, query);
			operationDescriptor.setAttribute(Constants.LANGUAGE_ATTRIBUTE, language);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SearchOperation::<init>(): Error during the creation of the search operation" +
                                "SourceBean");
			throw new BuildOperationException("SearchOperation::<init>(String, String): " +
											  "Error during the creation of the " +
					                          "search operation SourceBean", e);
		}
	}
		
	
	public SourceBean getOperationDescriptor() {
		return operationDescriptor;
	}

	
	public void setQuery(String query) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.QUERY_ATTRIBUTE, query);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SearchOperation::setQuery: Error during the query setting");
            throw new BuildOperationException("SearchOperation::setQuery: Error during the path setting", e);
		}
	}
	
	
	public void setLanguage(String language) throws BuildOperationException {
		try {
			operationDescriptor.updAttribute(Constants.LANGUAGE_ATTRIBUTE, language);
		} catch (Exception e) {
			TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.CRITICAL,
                                "SearchOperation::setLanguage: Error during the language setting");
            throw new BuildOperationException("SearchOperation::setLanguage: Error during the language setting", e);
		}
	}
	
}
