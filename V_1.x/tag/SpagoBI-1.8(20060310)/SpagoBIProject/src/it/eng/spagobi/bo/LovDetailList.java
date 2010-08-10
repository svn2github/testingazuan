/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Gives us all methods to handle the list of all values 
 * for the Fix Lov Selection Wizard.
 * 
 * @author sulis
 */
public class LovDetailList  implements Serializable  {

	List lovs = new ArrayList();
	
	/**
	 * Loads the XML string defined by a <code>LovDetail</code> object. The object
	 * gives us all XML field values. Once obtained, the XML represents the data 
	 * definition for a Fixed Lov  Type Value LOV object. 
	 * 
	 * @return The XML output String
	 */
	 
	public String toXML() throws SourceBeanException{
		Iterator i = lovs.iterator();
		String lovXML = "<LOV>";
		char apice = '"';
		while(i.hasNext()){
			LovDetail lov = new LovDetail();
			lov = (LovDetail)i.next();
			String name = lov.getName();
			String description = lov.getDescription();
			lovXML = lovXML + "<LOV-ELEMENT DESC = "+apice+name+apice+" VALUE = "+apice+description+apice+"/>";
		}
		lovXML = lovXML + "</LOV>";
		return lovXML;	
	}
	
	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods
	 * in order to obtain the source <code>LovDetail</code> objects whom XML has been 
	 * built. 
	 * 
	 * @param dataDefinition	The XML input String
	 * @return The corrispondent <code>LovDetailList</code> object
	 * @throws SourceBeanException If a SourceBean Exception occurred
	 */
	public static LovDetailList  fromXML (String dataDefinition) throws SourceBeanException{
		dataDefinition.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		List list = source.getAttributeAsList("LOV-ELEMENT");
		Iterator i = list.iterator();
		ArrayList lovList = new ArrayList();
		while (i.hasNext()){
			LovDetail lov = new LovDetail();
			SourceBean element = (SourceBean)i.next();
			String name = (String)element.getAttribute("DESC");
			lov.setName(name);
			String description = (String)element.getAttribute("VALUE");
			lov.setDescription(description);
			lovList.add(lov);
		}
		LovDetailList lovsListObj = new LovDetailList();
		lovsListObj.setLovs(lovList);
	    return lovsListObj;	
	}	
	
	/**
	 * Adds a lov to the lov Detail List
	 * 
	 * @param name The added lov name
	 * @param description The added lov description
	 */
	
	public void add(String name, String description) {
		
		// if name or description are empty don't add
		if((name==null) || (name.trim().equals("")))
				return;
		if((description==null) || (description.trim().equals("")))
			return;
		
		// if the element already exists don't add
		Iterator iter = lovs.iterator();
		while(iter.hasNext()) {
			LovDetail lovDet = (LovDetail)iter.next();
			if(name.equals(lovDet.getName()) && description.equals(lovDet.getDescription())) {
				return;
			}
		}
		
		LovDetail lovdet = new LovDetail();
		lovdet.setName(name);
		lovdet.setDescription(description);
		lovs.add(lovdet);
	}
	
	/**
	 * Deletes a lov from the lov Detail List
	 * 
	 * @param name The deleted lov name
	 * @param description The deleted lov description
	 */
	
	public void remove(String name, String description) {
		Iterator iter = lovs.iterator();
		while(iter.hasNext()) {
			LovDetail lovDet = (LovDetail)iter.next();
			if(name.equals(lovDet.getName()) && description.equals(lovDet.getDescription())) {
				lovs.remove(lovDet);
				break;
			}
		}
	}
	
	/**
	 * 
	 * @return lovs
	 */
	public List getLovs() {
		return lovs;
	}
	/**
	 * 
	 * @param lovs the lovs to set
	 */
	public void setLovs(List lovs) {
		this.lovs = lovs;
	}
}
