package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;

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
		for(int i=0; i<parameters.size(); i++){
			Parameter param = parameters.elementAt(i);
			if(param.getType().equals("OUT")){
				paramFound = param;
			}
		}
		
		return paramFound;
	}
}
