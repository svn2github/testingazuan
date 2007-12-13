package it.eng.spagobi.commons.utilities;

import java.util.HashMap;

public class SessionBridge {
    
    private static HashMap data=new HashMap();
    private static SessionBridge instance=null;
    
    public static synchronized SessionBridge getInstance(){
		if (instance==null){
		    instance=new SessionBridge();
		}
		return instance;	    
    }
    
    public synchronized void putObject(String key,Object obj){
	data.put(key, obj);
    } 
    
    public synchronized Object removeObject(String key){
	   Object tmp=data.get(key);
	   if (tmp!=null){
	       data.remove(key);
	   }
	   return tmp;
    }     

}
