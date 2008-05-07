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
package it.eng.spagobi.qbe.tree.presentation.tag;

import it.eng.qbe.bo.DatamartLabels;
import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.model.IDataMartModel;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;

import java.util.Locale;

// TODO: Auto-generated Javadoc
/**
 * The Class DatamartLabelFactory.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class DatamartLabelFactory {	
	
	/**
	 * Gets the field label.
	 * 
	 * @param datamartModel the datamart model
	 * @param field the field
	 * 
	 * @return the field label
	 */
	public static String getFieldLabel(IDataMartModel datamartModel, DataMartField field) {
		
		Locale locale = QbeEngineConf.getInstance().getLocale();	
		DatamartLabels datamartModelLabels = datamartModel.getDataSource().getLabels(locale);
		
		
		if(datamartModelLabels == null) return field.getName();
		
		String label =(String)datamartModelLabels.getLabel( field.getUniqueName().replaceAll(":", "/") ); 
		if ((label != null) && (label.trim().length() > 0))
			return label;
		else
			return field.getName();
	}
	
	/**
	 * Gets the entity label.
	 * 
	 * @param datamartModel the datamart model
	 * @param entity the entity
	 * 
	 * @return the entity label
	 */
	public static String getEntityLabel(IDataMartModel datamartModel, DataMartEntity entity) {
		Locale locale = QbeEngineConf.getInstance().getLocale();		
		DatamartLabels prop = datamartModel.getDataSource().getLabels(locale);
		
		if(prop == null) return entity.getName();
		
		String label =(String)prop.getLabel( entity.getUniqueName().replaceAll(":", "/") );
		if ((label != null) && (label.trim().length() > 0)) {
			return label;
		} else {
			return entity.getName();
		}
	}
}
