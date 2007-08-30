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
package it.eng.qbe.geo.action;

import it.eng.spago.base.SourceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Gioia
 * 
 */
public class GenearateGeoTemplateAction extends GeoAbstractAction { 
	
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);

		String qbeSqlQuery = null;
		List qbeQueryColumns = null;
		String expertSqlQuery = null;
		List expertQueryColumns = null;
		try {
			queryWizard.composeQuery(datamartModel);
			qbeSqlQuery = queryWizard.getFinalSqlQuery(datamartModel);
			qbeQueryColumns = getSelectFields(qbeSqlQuery);
			expertSqlQuery = queryWizard.getExpertQueryDisplayed();
			if(expertSqlQuery == null) expertSqlQuery = qbeSqlQuery;
			expertQueryColumns = getSelectFields(expertSqlQuery);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		response.setAttribute("QBE_QUERY", qbeSqlQuery);
		response.setAttribute("QBE_QUERY_COLUMNS", qbeQueryColumns);
		response.setAttribute("EXPERT_QUERY", expertSqlQuery);
		response.setAttribute("EXPERT_QUERY_COLUMNS", expertQueryColumns);
	}
	
	private List getSelectFields(String query) {
		List fields = new ArrayList();
		if(!query.trim().toLowerCase().startsWith("select")) return fields;
		String selectClause = query.substring(query.toLowerCase().indexOf("select") + "select".length(), query.toLowerCase().indexOf("from"));
		String[] selectBlocks = selectClause.split(",");
		for(int i = 0; i < selectBlocks.length; i++) {
			String field = " " + selectBlocks[i].trim();
			if(field.toLowerCase().indexOf(" distinct ") >= 0) {
				field = field.substring(0, field.toLowerCase().indexOf(" distinct ")) + field.substring(field.toLowerCase().indexOf(" distinct ") + " distinct ".length());
			}
			int index = -1;
			if( (index = field.toLowerCase().indexOf(" as ")) > 0) {
				field = field.substring(index + 4).trim();
			}
			
			if(field.lastIndexOf('.') > 0) field = field.substring(field.lastIndexOf('.')+1);
			fields.add(field.trim());
		}
		return fields;
	}
}
