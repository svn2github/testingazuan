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
package it.eng.spagobi.presentation.listobjectshtmlgenerators;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.dao.DAOFactory;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Built to transform XML tree data into XML list data. When from tree we
 * ask to obtain the relative list, these transformation methods are called.
 */
public class ListObjectsTransformerTreeImpl implements IListObjectsTransformer {

	private TreeMap mapObjects = new TreeMap(
			new Comparator() {
				public int compare(Object obj1, Object obj2) {
					String str1 = (String)obj1;
					String str2 = (String)obj2;
					str1 = str1.toUpperCase();
					str2 = str2.toUpperCase();
					return str1.compareTo(str2);
				}
			} 
	);
	/**
	 * Transforms the data contained into the input Source Bean in order to obtaon a list.
	 * The output data are stored into a <code>ListIFace</code> object.
	 * 
	 * @param data The data input Source Bean
	 * @return ListIFace The list Interface data output object
	 * 
	 */
	public ListIFace transform(SourceBean data) {
		PaginatorIFace paginator = new GenericPaginator();
		paginator.setPageSize(10);
		extractData(data);
		fillPaginator(paginator);
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		return list;
	}
	
	/**
	 * Extracts data from the source bean and applies the transformation, calling the
	 * <code>transformData</code> method.
	 * 
	 * @param data	Input data Source Bean
	 */
	private void extractData(SourceBean data) {
		List attrs = data.getContainedSourceBeanAttributes();
	    Iterator attrsIter = attrs.iterator();
	    while(attrsIter.hasNext()) {
	    	SourceBeanAttribute attrSBA = (SourceBeanAttribute)attrsIter.next();
	    	SourceBean attrSB = (SourceBean)attrSBA.getValue();
	    	transformData(attrSB);
	    	extractData(attrSB);
	    }
	}
	/**
	 * Applies transformation to input data
	 * 
	 * @param data The input data source bean
	 */
	private void transformData(SourceBean data) {
		try {	
			// get all types of objects as domains
			List types = DAOFactory.getDomainDAO().loadListDomainsByType("BIOBJ_TYPE");
            // get the name of the sourceBean (is the type of the element)
			String typeEl = data.getName();
			if(typeEl==null) {
				return;
			}
			// control if the type of the element is an object type
			Iterator iterTypes = types.iterator();
			boolean isType = false;
			Domain domain = null;
			while(iterTypes.hasNext()) {
				domain = (Domain)iterTypes.next();
				if(domain.getValueCd().equalsIgnoreCase(typeEl)) {
					isType = true;
				}
			}
			if(!isType) {
				return;
			}
			// if the element is an object create row sourceBean for the list
			SourceBean row = new SourceBean(DataRow.ROW_TAG);
			String nameObj = data.getAttribute("name").toString();
			String state = data.getAttribute("state").toString();
			Integer visible = (Integer)data.getAttribute("visible");
			String path = data.getAttribute("path").toString();
			String label = data.getAttribute("label").toString();
			String description = data.getAttribute("description").toString();
			String codeType = data.getAttribute("codeType").toString();
			String id = data.getAttribute("id").toString();
		    row.setAttribute("Type", codeType);
		    row.setAttribute("Name", nameObj);
		    row.setAttribute("State", state);
		    row.setAttribute("Visible", visible);
		    row.setAttribute("Path", path);
		    row.setAttribute("Label", label);
		    row.setAttribute("Description", description);
		    row.setAttribute("Id", id);
		    mapObjects.put(label, row);
		} catch (Exception e) {
			return;
		}
	}
	
	
	/**
	 * In the Spago application framework, a List of elements is associated to a paginator object.
	 * This method fills the paginator in order to set it for the list.
	 * 
	 * @param paginator	The input paginator to fill
	 */
	private void fillPaginator(PaginatorIFace paginator) {
		Set keys = mapObjects.keySet();
		Iterator iterKeys = keys.iterator();
		while(iterKeys.hasNext()) {
			String key = (String)iterKeys.next();
			Object rowObj = mapObjects.get(key);
			paginator.addRow(rowObj);
		}
	}
	
}
