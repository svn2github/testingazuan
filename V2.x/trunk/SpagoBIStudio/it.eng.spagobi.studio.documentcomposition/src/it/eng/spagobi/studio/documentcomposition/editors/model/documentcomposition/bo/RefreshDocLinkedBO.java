package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.RefreshDocLinked;

import java.util.Vector;

public class RefreshDocLinkedBO {
	
	public RefreshDocLinked deleteRefreshedDocLink(DocumentComposition docComp, String id, String sbiParLabel, String  sbiObjLabel){
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
	    Vector documents = docConf.getDocuments();
	    RefreshDocLinked docRefrFound = null; 
	    if(documents != null){
		    //elabora documento master
		    for (int i = 0; i< documents.size(); i++){
		    	Document doc = (Document)documents.get(i);
	    		Parameters params = doc.getParameters();
	    		if(params != null){
	    			Vector<Parameter> parameters =params.getParameter();
	    			if(parameters == null){
	    				for(int j=0; j<parameters.size(); j++){	    			
	    					Parameter param = parameters.elementAt(j);
	    					if(param.getRefresh() != null && param.getRefresh().getRefreshDocLinked() != null){
								
								for(int k=0; k<param.getRefresh().getRefreshDocLinked().size(); k++){
									RefreshDocLinked docRef = param.getRefresh().getRefreshDocLinked().elementAt(k);
									if(docRef.getLabelDoc().equals(sbiObjLabel) && docRef.getLabelParam().equals(sbiParLabel) && docRef.getIdParam().equals(id)){
										param.getRefresh().getRefreshDocLinked().remove(docRef);
									}
								}
	    					}
	    				}
	    			}
	    		}
		    }
	    }				
		
		return docRefrFound;
	}
	public RefreshDocLinked refreshDocAlreadyExists(String id, String docName, Vector<RefreshDocLinked> refreshes){
		RefreshDocLinked docRefrFound = null; 
		for(int i=0; i<refreshes.size(); i++){
			RefreshDocLinked doc = refreshes.elementAt(i);
			if(doc.getLabelDoc().equals(docName) && doc.getIdParam().equals(id)){
				docRefrFound = doc;
			}
		}		
		return docRefrFound;
	}
	public RefreshDocLinked refreshByParamIdAlreadyExists(String id, Vector<RefreshDocLinked> refreshes){
		RefreshDocLinked docRefrFound = null; 
		for(int i=0; i<refreshes.size(); i++){
			RefreshDocLinked doc = refreshes.elementAt(i);
			if(doc.getIdParam().equals(id)){
				docRefrFound = doc;
			}
		}		
		return docRefrFound;
	}
	public RefreshDocLinked upadateRefreshedDocLink(DocumentComposition docComp, String id, String sbiParLabel, String  sbiObjLabel){
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
	    Vector documents = docConf.getDocuments();
	    RefreshDocLinked docRefrFound = null; 
	    if(documents != null){
		    //elabora documento master
		    for (int i = 0; i< documents.size(); i++){
		    	Document doc = (Document)documents.get(i);
	    		Parameters params = doc.getParameters();
	    		if(params != null){
	    			Vector<Parameter> parameters =params.getParameter();
	    			if(parameters != null){
	    				for(int j=0; j<parameters.size(); j++){	    			
	    					Parameter param = parameters.elementAt(j);
	    					if(param.getRefresh() != null && param.getRefresh().getRefreshDocLinked() != null){
								
							//non esisteva--> insersco
								RefreshDocLinked docRef = new RefreshDocLinked();
								docRef.setIdParam(id);
								docRef.setLabelDoc(sbiObjLabel);
								docRef.setLabelParam(sbiParLabel);
								param.getRefresh().getRefreshDocLinked().add(docRef);
							
	    					}
	    				}
	    			}
	    		}
		    }
	    }				
		
		return docRefrFound;
	}

	public boolean inputParameterIsUsedByOther(DocumentComposition docComp, String id){
		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
	    Vector documents = docConf.getDocuments();
	    boolean isUsedMoreThanOnce = false;
	    int counter =0;
	    if(documents != null){
		    //elabora documento master
		    for (int i = 0; i< documents.size(); i++){
		    	Document doc = (Document)documents.get(i);
	    		Parameters params = doc.getParameters();
	    		if(params != null){
	    			Vector<Parameter> parameters =params.getParameter();
	    			if(parameters != null){
	    				for(int j=0; j<parameters.size(); j++){	    			
	    					Parameter param = parameters.elementAt(j);
	    					if(param.getRefresh() != null && param.getRefresh().getRefreshDocLinked() != null){
	    						for(int k=0; k<param.getRefresh().getRefreshDocLinked().size(); k++){
	    							RefreshDocLinked docRef = param.getRefresh().getRefreshDocLinked().elementAt(k);
	    							if(docRef.getIdParam().equals(id)){
	    								counter++;
	    							}
	    						}	
							
	    					}
	    				}
	    			}
	    		}
		    }
	    }		

		if(counter > 1){
			isUsedMoreThanOnce = true;
		}
		return isUsedMoreThanOnce;
	}
}
