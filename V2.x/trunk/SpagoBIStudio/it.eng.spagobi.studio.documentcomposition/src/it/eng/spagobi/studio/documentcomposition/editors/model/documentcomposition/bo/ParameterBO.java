package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;

import java.util.Vector;

public class ParameterBO {
	
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
	public Parameter getDocOutputParameter(Vector<Parameter> parameters){
		Parameter paramFound = null; 
		if(parameters != null){
			for(int i=0; i<parameters.size(); i++){
				Parameter param = parameters.elementAt(i);
				if(param.getType().equals("OUT")){
					paramFound = param;
				}
			}
		}
		return paramFound;
	}
	
	public boolean ouputParameterExists(DocumentComposition docComp, String masterDocLabel, String masterParamLabel){
		System.out.println(masterDocLabel +" "+masterParamLabel);
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
}
