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
package it.eng.spagobi.engines.geo.map.renderer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.engines.geo.Constants;
import it.eng.spagobi.engines.geo.datasource.DataSource;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class QueryLabelProducer implements LabelProducer {
	
	private DataSource dataSource;
	private String query;
	private String text;
	private Set paramNames;
	
	public void init(SourceBean conf) {
		SourceBean dataSourceSB = (SourceBean)conf.getAttribute("DATASOURCE");
		dataSource = new DataSource(dataSourceSB);
		
		SourceBean querySB = (SourceBean)conf.getAttribute("QUERY");
		query = querySB.getCharacters();
		SourceBean textSB = (SourceBean)conf.getAttribute("TEXT");
		text = textSB.getCharacters();
		
		paramNames = new HashSet();
		int fromIndex = 0;
		int beginIndex = -1;
		while( (beginIndex = text.indexOf("${", fromIndex)) != -1) {
			int endIndex = text.indexOf("}", beginIndex);
			String param = text.substring(beginIndex + 2, endIndex);
			paramNames.add(param);
			fromIndex = endIndex;
		}
	}
	
	public String getLabel(){
		String label = text;
		Connection connection = null;
		try {       
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet(); 
            if( resultSet.next() ) {
	            Iterator it = paramNames.iterator();
	            while(it.hasNext()) {
	            	String pName = (String)it.next();
	            	int col_index = resultSet.findColumn( pName );
	            	String pValue = resultSet.getString( col_index );
	            	if(pValue == null) pValue = "";
	            	label = label.replaceAll("\\$\\{" + pName + "\\}", pValue);
	            }
            }
		 } catch (Exception ex) {
	        	ex.printStackTrace();
	     } finally {
	       if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
	       }
	     }
		
		return label;
	}
}
