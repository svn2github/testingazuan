package it.eng.spago.cms;

import it.eng.spago.cms.constants.Constants;
import it.eng.spago.tracing.TracerSingleton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CmsProperty {

	public final static String TYPE_BINARY = "BINARY";
	public final static String TYPE_BOOLEAN = "BOOLEAN";
	public final static String TYPE_DATE = "DATE";
	public final static String TYPE_DOUBLE = "DOUBLE";
	public final static String TYPE_LONG = "LONG";
	public final static String TYPE_STRING = "STRING";

	String name = "";
	String type = "";
	List values = new ArrayList();
	
	
	public CmsProperty(String namein, String[] valuesin) {
		name = namein;
		type = TYPE_STRING;
		setStringValues(valuesin);
	}
	
	public CmsProperty(String namein, Long[] valuesin) {
		name = namein;
		type = TYPE_LONG;
		setLongValues(valuesin);
	}
	
	public CmsProperty(String namein, Double[] valuesin) {
		name = namein;
		type = TYPE_DOUBLE;
		setDoubleValues(valuesin);
	}
	
	public CmsProperty(String namein, Calendar[] valuesin) {
		name = namein;
		type = TYPE_DATE;
		setDateValues(valuesin);
	}
	
	public CmsProperty(String namein, Boolean[] valuesin) {
		name = namein;
		type = TYPE_BOOLEAN;
		setBooleanValues(valuesin);
	}
	
	public CmsProperty(String namein, InputStream[] valuesin) {
		name = namein;
		type = TYPE_BINARY;
		setBinaryValues(valuesin);
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	
    public String[] getStringValues() {	
    	try {
    		int size = values.size();
    		String [] array = new String[size];
    		for(int i=0; i<size; i++) {
    			array[i] = (String)values.get(i);
    		}
    		return array;
    	} catch (Exception e) {
    		 TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.WARNING,
                                 "Property::getStringValues: property value cast exception");
    	     return new String[0];
    	}
    }
    
    
    public void setStringValues(String[] invalues) {	
    	for(int i=0; i<invalues.length; i++)
    		values.add(invalues[i]);
    }
    
    
    
    
    public InputStream[] getBinaryValues() {
    	try {
    		int size = values.size();
    		InputStream [] array = new InputStream[size];
    		for(int i=0; i<size; i++) {
    			array[i] = (InputStream)values.get(i);
    		}
    		return array;
    	} catch (Exception e) {
    		 TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.WARNING,
                                 "Property::getBinaryValues: property value cast exception");
    		 return new InputStream[0];
    	}
    }
    
    public void setBinaryValues(InputStream[] invalues) {	
    	for(int i=0; i<invalues.length; i++)
    		values.add(invalues[i]);
    }
    
    
    
    
    
    public Long[] getLongValues() {
    	try {
    		int size = values.size();
    		Long [] array = new Long[size];
    		for(int i=0; i<size; i++) {
    			array[i] = (Long)values.get(i);
    		}
    		return array;
    	} catch (Exception e) {
    		 TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.WARNING,
                                 "Property::getLongValues: property value cast exception");
             return new Long[0];
    	}
    }
    
    public void setLongValues(Long[] invalues) {	
    	for(int i=0; i<invalues.length; i++)
    		values.add(invalues[i]);
    }
    
    
    
    
    
    public Double[] getDoubleValues() {
    	try {
    		int size = values.size();
    		Double [] array = new Double[size];
    		for(int i=0; i<size; i++) {
    			array[i] = (Double)values.get(i);
    		}
    		return array;
    	} catch (Exception e) {
    		 TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.WARNING,
                                 "Property::getDoubleValues: property value cast exception");
             return new Double[0];
    	}
    }
    
    public void setDoubleValues(Double[] invalues) {	
    	for(int i=0; i<invalues.length; i++)
    		values.add(invalues[i]);
    }
    
    
    
    
    
    
    public Boolean[] getBooleanValues() {
    	try {
    		int size = values.size();
    		Boolean [] array = new Boolean[size];
    		for(int i=0; i<size; i++) {
    			array[i] = (Boolean)values.get(i);
    		}
    		return array;
    	} catch (Exception e) {
    		 TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.WARNING,
                                 "Property::getBooleanValues: property value cast exception");
             return new Boolean[0];
    	}
    }
    
    
    
    public void setBooleanValues(Boolean[] invalues) {	
    	for(int i=0; i<invalues.length; i++)
    		values.add(invalues[i]);
    }
    
    
    
    
    
    public Calendar[] getDateValues() {
    	try {
    		int size = values.size();
    		Calendar [] array = new Calendar[size];
    		for(int i=0; i<size; i++) {
    			array[i] = (Calendar)values.get(i);
    		}
    		return array;
    	} catch (Exception e) {
    		TracerSingleton.log(Constants.NAME_MODULE, TracerSingleton.WARNING,
                                 "Property::getDateValues: property value cast exception");
            return new Calendar[0];
    	}
    }
	
    
    public void setDateValues(Calendar[] invalues) {	
    	for(int i=0; i<invalues.length; i++)
    		values.add(invalues[i]);
    }
	
	
}
