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
package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree;

import java.util.Locale;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.properties.IModelProperties;
import it.eng.qbe.model.structure.IModelObject;
import it.eng.spagobi.commons.utilities.StringUtilities;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ModelLabelProvider {

	private IModelProperties modelI18NProperties;
	
	public ModelLabelProvider(IDataSource dataSource) {
		Locale userLocale;
		String userLanguage;
		
		userLanguage = System.getProperty("user.language");
		userLocale = userLanguage != null? new Locale(userLanguage): Locale.getDefault();
		modelI18NProperties = dataSource.getModelI18NProperties( userLocale );
	}
	
	
	public String getLabel(IModelObject modelObject) {
		String label;
		label = modelI18NProperties.getProperty(modelObject, "label");
		return StringUtilities.isEmpty(label)? modelObject.getName(): label;
	}
	
	public String getTooltip(IModelObject modelObject) {
		String tooltip = modelI18NProperties.getProperty(modelObject, "tooltip");
		return tooltip != null ? tooltip : "";
	}


}
