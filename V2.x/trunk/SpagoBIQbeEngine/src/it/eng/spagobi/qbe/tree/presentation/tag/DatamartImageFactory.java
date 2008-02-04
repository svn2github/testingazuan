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
package it.eng.spagobi.qbe.tree.presentation.tag;

import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.utility.QbeProperties;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DatamartImageFactory {
	
	
	public static String getFieldImage(IDataMartModel datamart, DataMartField field) {
		String fieldImage;
		QbeProperties qbeProperties = new QbeProperties(datamart);
		
		int fieldType = qbeProperties.getFieldType( field.getName() );
		
		
		if(field.isKey()) {
			if(fieldType == QbeProperties.FIELD_TYPE_GEOREF) {
				fieldImage = "../img/world.gif";
			} else {
				fieldImage = "../img/key.gif";
			}
		} else  {
			if(fieldType == QbeProperties.FIELD_TYPE_DIMENSION) {
				fieldImage = "../img/redbox.gif"; 
			} else if(fieldType == QbeProperties.FIELD_TYPE_MEASURE) {
				fieldImage = "../img/Method.gif"; 
			} else if(fieldType == QbeProperties.FIELD_TYPE_GEOREF) {
				fieldImage = "../img/world.gif";
			} else {
				fieldImage = "../img/redbox.gif"; 
			}
		}
		
		return fieldImage;
	}
	
	public static String getEntityImage(IDataMartModel datamart, DataMartEntity entity) {
		String entityImage;
		int entityType;
		QbeProperties qbeProperties; 
		
		qbeProperties = new QbeProperties(datamart);		
		entityType = qbeProperties.getTableType( entity.getType() );
		
		if(entityType == QbeProperties.CLASS_TYPE_TABLE) {
			entityImage = "../img/Class.gif";
		} else if(entityType == QbeProperties.CLASS_TYPE_VIEW) {
			entityImage = "../img/view.gif";
		} else {
			entityImage = "../img/relationship.gif";
		}		
		
		return entityImage;		
	}
}
