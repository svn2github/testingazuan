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
package it.eng.qbe.export;

import it.eng.qbe.bo.Formula;

import java.sql.Connection;
import java.util.Map;


// TODO: Auto-generated Javadoc
/**
 * The Class SQLTemplateBuilder.
 * 
 * @author Gioia
 */
public class SQLTemplateBuilder extends BasicTemplateBuilder {
	
	/**
	 * Instantiates a new sQL template builder.
	 * 
	 * @param query the query
	 * @param connection the connection
	 * @param params the params
	 * @param orderedFldList the ordered fld list
	 * @param extractedEntitiesList the extracted entities list
	 * @param formula the formula
	 * 
	 * @throws Exception the exception
	 */
	public SQLTemplateBuilder(String query, Connection connection, Map params, String orderedFldList, String extractedEntitiesList, Formula formula) throws Exception {
		super(query, BasicTemplateBuilder.SQL_LANGUAGE, new SQLFieldsReader(query, connection).readFields(), params, orderedFldList, extractedEntitiesList, formula);
	}
}
