package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;

import java.util.Vector;

public class ParameterBO {
	
	public String getLastId(DocumentComposition docComp){
		int counter=0;

		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
		if(docConf != null){
		    Vector documents = docConf.getDocuments();
		    if(documents != null){
		    	for (int i = 0; i< documents.size(); i++){
		    		Document doc = (Document)documents.elementAt(i);
		    		String docLabel =doc.getSbiObjLabel();
		    		
	    			Parameters parameters = doc.getParameters();
	    			if(parameters != null){
	    				Vector<Parameter> params = parameters.getParameter();
	    				if(params != null){
	    					for(int j=0;j<params.size(); j++){
	    						Parameter param = params.elementAt(j);
	    						int last =Integer.valueOf(param.getId()).intValue();
	    						if(last > counter){
	    							counter = last;
	    						}
	    					}
	    				}
	    			}
		    	    		
		    	}
		    }
		}
		return String.valueOf(counter);
	}
	
	public Parameter getParameterById(String id , Vector<Parameter> parameters){
		Parameter paramFound = null; 
		for(int i=0; i<parameters.size(); i++){
			Parameter param = parameters.elementAt(i);
			if(param.getId().equals(id)){
				paramFound = param;
			}
		}		
		return paramFound;
	}
	public void cleanUnusedInputParameters(DocumentComposition docComp, Vector<String> idParamUsedByRefresh){
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
		if(docConf != null){
		    Vector documents = docConf.getDocuments();
		    if(documents != null){
		    	for (int i = 0; i< documents.size(); i++){
		    		Document doc = (Document)documents.elementAt(i);
		    		String docLabel =doc.getSbiObjLabel();
	    			Parameters parameters = doc.getParameters();
	    			if(parameters != null){
	    				Vector<Parameter> params = parameters.getParameter();
	    				if(params != null){
	    					for(int j=0;j<params.size(); j++){
	    						Parameter param = params.elementAt(j);
	    						if(param.getType().equalsIgnoreCase("IN") && !idParamUsedByRefresh.contains(param.getId())){
	    							params.remove(param);
	    						}
	    					}
	    				}
	    			}
	    		
		    	}
		    }
		}
	}
	public Parameter getParameterById(DocumentComposition docComp, String id){
		Parameter paramFound = null;
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
		if(docConf != null){
		    Vector documents = docConf.getDocuments();
		    if(documents != null){
		    	for (int i = 0; i< documents.size(); i++){
		    		Document doc = (Document)documents.elementAt(i);
		    		String docLabel =doc.getSbiObjLabel();

		    			Parameters parameters = doc.getParameters();
		    			if(parameters != null){
		    				Vector<Parameter> params = parameters.getParameter();
		    				if(params != null){
		    					for(int j=0;j<params.size(); j++){
		    						Parameter param = params.elementAt(j);
		    						if(param.getType().equals("IN") && param.getId().equals(id)){
		    							paramFound = param;
		    						}
		    					}
		    				}
		    			}
	    		
		    	}
		    }
		}
	
		return paramFound;
	}
	public String getParameterDocumentName(DocumentComposition docComp, String id){
		String docName = null;
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
		if(docConf != null){
		    Vector documents = docConf.getDocuments();
		    if(documents != null){
		    	for (int i = 0; i< documents.size(); i++){
		    		Document doc = (Document)documents.elementAt(i);
		    		String docLabel =doc.getSbiObjLabel();

		    			Parameters parameters = doc.getParameters();
		    			if(parameters != null){
		    				Vector<Parameter> params = parameters.getParameter();
		    				if(params != null){
		    					for(int j=0;j<params.size(); j++){
		    						Parameter param = params.elementAt(j);
		    						if(param.getType().equals("IN") && param.getId().equals(id)){
		    							docName = docLabel;
		    						}
		    					}
		    				}
		    			}
	    		
		    	}
		    }
		}
	
		return docName;
	}
	public boolean deleteParameterById(DocumentComposition docComp, String id){
		boolean paramFound = false;
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
		if(docConf != null){
		    Vector documents = docConf.getDocuments();
		    if(documents != null){
		    	for (int i = 0; i< documents.size(); i++){
		    		Document doc = (Document)documents.elementAt(i);
		    		String docLabel =doc.getSbiObjLabel();

		    			Parameters parameters = doc.getParameters();
		    			if(parameters != null){
		    				Vector<Parameter> params = parameters.getParameter();
		    				if(params != null){
		    					for(int j=0;j<params.size(); j++){
		    						Parameter param = params.elementAt(j);
		    						if(param.getType().equals("IN") && param.getId().equals(id)){
		    							params.remove(param);
		    						}
		    					}
		    				}
		    			}
	    		
		    	}
		    }
		}
	
		return paramFound;
	}
	
	public Parameter getDocOutputParameter(Vector<Parameter> parameters, String paramLabel){
		Parameter paramFound = null; 
		if(parameters != null){
			for(int i=0; i<parameters.size(); i++){
				Parameter param = parameters.elementAt(i);
				if(param.getType().equals("OUT") && paramLabel.equals(param.getSbiParLabel())){
					paramFound = param;
				}
			}
		}
		return paramFound;
	}
	
	public boolean outputParameterExists(DocumentComposition docComp, String masterDocLabel, String masterParamLabel){

		boolean ret = false;
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
		if(docConf != null){
		    Vector documents = docConf.getDocuments();
		    if(documents != null){
		    	for (int i = 0; i< documents.size(); i++){
		    		Document doc = (Document)documents.elementAt(i);
		    		String docLabel =doc.getSbiObjLabel();
		    		if(masterDocLabel.equals(docLabel)){
		    			Parameters parameters = doc.getParameters();
		    			if(parameters != null){
		    				Vector<Parameter> params = parameters.getParameter();
		    				if(params != null){
		    					for(int j=0;j<params.size(); j++){
		    						Parameter param = params.elementAt(j);
		    						if(param.getType().equals("OUT") && param.getSbiParLabel().equals(masterParamLabel)){
		    							ret = true;
		    						}
		    					}
		    				}
		    			}
		    			
		    		}		    		
		    	}
		    }
		}
		
		return ret;
	}
	public Parameter getDocInputParameterByLabel(Vector<Parameter> parameters, String label){
		Parameter paramFound = null; 
		if(parameters != null){
			for(int i=0; i<parameters.size(); i++){
				Parameter param = parameters.elementAt(i);
				if(param.getType().equals("IN") && param.getSbiParLabel().equals(label)){
					paramFound = param;
				}
			}
		}
		return paramFound;
	}

	public boolean inputParameterExists(DocumentComposition docComp, String destinDocLabel, String destinParamLabel){
		boolean ret = false;
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
		if(docConf != null){
		    Vector documents = docConf.getDocuments();
		    if(documents != null){
		    	for (int i = 0; i< documents.size(); i++){
		    		Document doc = (Document)documents.elementAt(i);
		    		String docLabel =doc.getSbiObjLabel();
		    		if(destinDocLabel.equals(docLabel)){
		    			Parameters parameters = doc.getParameters();
		    			if(parameters != null){
		    				Vector<Parameter> params = parameters.getParameter();
		    				if(params != null){
		    					for(int j=0;j<params.size(); j++){
		    						Parameter param = params.elementAt(j);
		    						if(param.getType().equals("IN") && param.getSbiParLabel().equals(destinParamLabel)){
		    							ret = true;
		    						}
		    					}
		    				}
		    			}
		    			
		    		}		    		
		    	}
		    }
		}
		
		return ret;
	}

}
