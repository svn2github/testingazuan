package it.eng.spagobi.tools.dataset.bo;

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


import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Defines method to manage dataset parametes
 */
public class DataSetParametersList {
    
	/**
	 * items of the list
	 */
	List items = new ArrayList();
	
	
	/**
	 * constructor
	 */
	public DataSetParametersList() {
	} 
	
	/**
	 * constructor 
	 */
	public DataSetParametersList(String dataDefinition) throws SourceBeanException {
		loadFromXML(dataDefinition);
	}
	
	/** loads the lov from an xml string 
	 * @param dataDefinition the xml definition of the lov
	 * @throws SourceBeanException 
	 */
	public void  loadFromXML (String dataDefinition) throws SourceBeanException {
		dataDefinition.trim();
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		if(!source.getName().equals("PARAMETERSLIST")) {
			SourceBean wrapper = new SourceBean("PARAMETERSLIST");
			wrapper.setAttribute(source);	
			source = wrapper;
		}
		// load data from xml
		List listRows = source.getAttributeAsList("ROWS.ROW");
		Iterator iterRows = listRows.iterator();
		ArrayList parsList = new ArrayList();
		while(iterRows.hasNext()){
			DataSetParameterItem par = new DataSetParameterItem();
			SourceBean element = (SourceBean)iterRows.next();
			String name = (String)element.getAttribute("NAME");
			par.setName(name);
			String type = (String)element.getAttribute("TYPE");
			par.setType(type);
			parsList.add(par);
		}
 		setPars(parsList);
	}	
	
	/**
	 * serialize the lov to an xml string
	 * @return the serialized xml string
	 */
	public String toXML() {
		String lovXML = "";
		lovXML += "<PARAMETERSLIST>";
		lovXML += "<ROWS>";
		DataSetParameterItem lov = null;
		Iterator iter = items.iterator();
		while(iter.hasNext()){
			lov = (DataSetParameterItem)iter.next();
			String name = lov.getName();
			String type = lov.getType();
			lovXML += "<ROW" +
					  " NAME=\"" + name + "\"" +
					  " TYPE=\"" + type + "\"" +
					  "/>";
		}
		lovXML += "</ROWS></PARAMETERSLIST>";
		return lovXML;
	}
	
	/**
	 * Returns the result of the lov using a user profile to fill the lov profile attribute
	 * @param profile the profile of the user
	 * @return the string result of the lov
	 * @throws Exception
	 */
	public String getDataSetResult(IEngUserProfile profile) throws Exception {
		String lovResult = "<ROWS>";
		DataSetParameterItem lov = null;
		Iterator iter = items.iterator();
		while(iter.hasNext()){
			lov = (DataSetParameterItem)iter.next();
			String name = lov.getName();
			String type = lov.getType();
			lovResult += "<ROW" +
					  " NAME=\"" + name + "\"" +
					  " TYPE=\"" + type + "\"" +
					  "/>";
		}
		lovResult += "</ROWS>";
		lovResult = GeneralUtilities.substituteProfileAttributesInString(lovResult, profile);
		return lovResult;
	}
		

	/**
	 * Gets the list of names of the profile attributes required
	 * @return list of profile attribute names
	 * @throws Exception
	 */
	public List getProfileAttributeNames() throws Exception {
		List names = new ArrayList();
		String lovResult = this.toXML();
		while(lovResult.indexOf("$P{")!=-1) {
			int startind = lovResult.indexOf("$P{");
			int endind = lovResult.indexOf("}", startind);
			String attributeDef = lovResult.substring(startind + 2, endind);
			if(attributeDef.indexOf("(")!=-1) {
				int indroundBrack = lovResult.indexOf("(", startind);
				String nameAttr = lovResult.substring(startind+2, indroundBrack);
				names.add(nameAttr);
			} else {
				names.add(attributeDef);
			}
			lovResult = lovResult.substring(endind);
		}
		return names;
	}

	/**
	 * Checks if the lov requires one or more profile attributes
	 * @return true if the lov require one or more profile attributes, false otherwise
	 * @throws Exception
	 */
	public boolean requireProfileAttributes() throws Exception {
		boolean contains = false;
		String lovResult = this.toXML();
		if(lovResult.indexOf("${")!=-1) {
			contains = true;
		}
		return contains;
	}	
	
	
	/**
	 * Adds a lov to the lov Detail List
	 * @param name The added lov name
	 * @param description The added lov description
	 */
	public void add(String name, String type) {
		// if name or description are empty don't add
		if((name==null) || (name.trim().equals("")))
				return;
		if((type==null) || (type.trim().equals("")))
			return;
		// if the element already exists don't add
		Iterator iter = items.iterator();
		while(iter.hasNext()) {
			DataSetParameterItem lovDet = (DataSetParameterItem)iter.next();
			if(name.equals(lovDet.getName()) && type.equals(lovDet.getType())) {
				return;
			}
		}
		// add the item
		DataSetParameterItem item = new DataSetParameterItem();
		item.setName(name);
		item.setType(type);
		items.add(item);
	}
	
	
	/**
	 * Deletes a lov from the lov Detail List
	 * @param value The deleted lov name
	 * @param description The deleted lov description
	 */
	public void remove(String name, String type) {
		Iterator iter = items.iterator();
		while(iter.hasNext()) {
			DataSetParameterItem lovDet = (DataSetParameterItem)iter.next();
			if(name.equals(lovDet.getName()) && type.equals(lovDet.getType())) {
				items.remove(lovDet);
				break;
			}
		}
	}
	
	
	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods
	 * in order to obtain the source <code>LovDetail</code> objects whom XML has been 
	 * built. 
	 * @param dataDefinition	The XML input String
	 * @return The corrispondent <code>LovDetailList</code> object
	 * @throws SourceBeanException If a SourceBean Exception occurred
	 */
	public static DataSetParametersList  fromXML (String dataDefinition) throws SourceBeanException {
		return new DataSetParametersList(dataDefinition);	
	}
	
	
	/**
	 * Gets item of the fixed list
	 * @return items of the fixed list
	 */
	public List getItems() {
		return items;
	}
	/**
	 * Sets items of the fixed list
	 * @param items the items to set
	 */
	public void setPars(List items) {
		this.items = items;
	}
	

	
}
