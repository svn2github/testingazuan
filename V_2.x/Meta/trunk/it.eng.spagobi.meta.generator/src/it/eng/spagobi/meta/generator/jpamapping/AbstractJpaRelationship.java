/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.generator.jpamapping;

import it.eng.spagobi.meta.generator.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbstractJpaRelationship implements IJpaRelationship{
	AbstractJpaTable jpaTable;
	String cardinality;
	boolean bidirectional;
	
	
	
	public boolean isBidirectional() {
		return bidirectional;
	}

	public String getCardinality() {
		return cardinality;
	}

	public boolean isOneToMany() {
		return JpaRelationship.ONE_TO_MANY.equals( cardinality );
	}
	
	public boolean isManyToMany() {
		return JpaRelationship.MANY_TO_MANY.equals( cardinality );
	}
	
	public String getDescription()  {
		if (isBidirectional()){
			return "Bidirectional " +  getCardinality() + " association to " + getReferencedTable().getClassName();	
		} else {
			return  getCardinality() + " association to " + getReferencedTable().getClassName();
		}
	}
	
	public AbstractJpaTable getJpaTable() {
		return jpaTable;
	}
	
	/**
	 * @return the name of the metod GETTER
	 */
	public String getGetter(String par) {
		return "get"+StringUtil.initUpper(par);
	}
	/**
	 * @return the name of the metod SETTER
	 */
	public String getSetter(String par) {
		return "set"+StringUtil.initUpper(par);
	}
	
	/**
	 * TODO ... implementazione da verificare.!!!!!!!!
	 * @param role
	 * @return
	 */
	private String genCascades() {
		
        //List cascades = StringUtil.strToList(role.getCascade(), ',', true);
		List<String> cascades = new ArrayList<String>();
		cascades.add("all");

        StringBuffer buffer = new StringBuffer();
        buffer.append('{');
        int i = 0;
        for(int n = cascades.size(); i < n; i++)
        {
            String cascade = (String)cascades.get(i);
            String enumStr;
            if(cascade.equals("all"))
                enumStr = "CascadeType.ALL";
            else
            if(cascade.equals("persist"))
                enumStr = "CascadeType.PERSIST";
            else
            if(cascade.equals("merge"))
                enumStr = "CascadeType.MERGE";
            else
            if(cascade.equals("remove"))
            {
                enumStr = "CascadeType.REMOVE";
            } else
            {
                enumStr = "CascadeType.REFRESH";
            }
            if(i != 0)
                buffer.append(", ");
            buffer.append(enumStr);
        }

        buffer.append('}');
        return buffer.toString();
    }
	
	/**
	 * TODO ... da verificare
	 * @param s
	 * @param memberName
	 * @param memberValue
	 * @param quote
	 * @return
	 */
	    private String appendAnnotation(String s, String memberName, String memberValue, boolean quote)
	    {
	        if(memberValue == null || memberValue.length() == 0)
	            return s;
	        StringBuffer buffer = new StringBuffer(s);
	        if(buffer.length() != 0)
	            buffer.append(", ");
	        buffer.append(memberName);
	        buffer.append('=');
	        if(quote)
	            buffer.append('"');
	        buffer.append(memberValue);
	        if(quote)
	            buffer.append('"');
	        return buffer.toString();
	    }
	    
	    /**
	     * 
	     * @return
	     */
	    public String getGenCascadesWithAnnotation(){
	    	return appendAnnotation("", "cascade",genCascades(),false);
	    }
	    public String getGenCascadesWithAnnotation(String parameter){
	    	return appendAnnotation(parameter, "cascade",genCascades(),false);
	    }    
	    public String getGenFetchWithAnnotation(String s){
	    	return appendAnnotation(s, "fetch",genFetch(),false);
	    }    

	    
	    private String genFetch()
	    {
	        String fetch = jpaTable.getDefaultFetch();
	        if(fetch == null || "defaultFetch".equals(fetch))
	            return "";
	        if(fetch.equals("lazy"))
	            return "FetchType.LAZY";
	        else
	            return "FetchType.EAGER";
	    }
	    
	    /**
		 * @return
		 */
		public String getCollectionType(){
			
			return "java.util.Set";
		}
		
		protected abstract String getOppositeRoleName();
		
		public String getOppositeWithAnnotation(){
			return appendAnnotation("", "mappedBy", getOppositeRoleName(),true);

		}
	
}
