/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.tree.urlgenerator;

import it.eng.qbe.model.structure.DataMartField;


// TODO: Auto-generated Javadoc
/**
 * The Interface IQbeTreeUrlGenerator.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public interface IQbeTreeUrlGenerator {
	
	/**
	 * Gets the action url.
	 * 
	 * @param field the field
	 * 
	 * @return the action url
	 */
	public String getActionUrl(DataMartField field);	
	
	/**
	 * Gets the action url for calculate field.
	 * 
	 * @param calculatedFieldId the calculated field id
	 * @param entityName the entity name
	 * @param cFieldCompleteName the c field complete name
	 * 
	 * @return the action url for calculate field
	 */
	public String getActionUrlForCalculateField(String calculatedFieldId, String entityName, String cFieldCompleteName);
	
	/**
	 * Gets the resource url.
	 * 
	 * @param url the url
	 * 
	 * @return the resource url
	 */
	public String getResourceUrl(String url);
	
	
}
