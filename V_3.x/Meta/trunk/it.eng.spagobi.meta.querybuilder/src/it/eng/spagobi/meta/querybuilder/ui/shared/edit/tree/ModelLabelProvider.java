/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.properties.IModelProperties;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelObject;
import it.eng.spagobi.commons.utilities.StringUtilities;

import java.util.Locale;

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
		if(label == null) {
			if(modelObject instanceof IModelField) {
				IModelField field = (IModelField)modelObject;
				IModelEntity rootEntity = field.getParent().getStructure().getRootEntity(field.getParent());
				if(rootEntity != null) {
					for(IModelField rootField : rootEntity.getAllFields()) {
						if(rootField.getName().equals(field.getName())) {
							label = modelI18NProperties.getProperty(rootField, "label");
							break;
						}
					}
				}
			} else if(modelObject instanceof IModelEntity) {
				IModelEntity entity = (IModelEntity)modelObject;
//				IModelEntity rootEntity = entity.getStructure().getRootEntity(entity);
//				if(rootEntity != null) {
//					label = modelI18NProperties.getProperty(rootEntity, "label");
//				}
				if(entity != null) {
					label = modelI18NProperties.getProperty(entity, "label");
				}
			}
		}
		return StringUtilities.isEmpty(label)? modelObject.getName(): label;
	}
	
	public String getTooltip(IModelObject modelObject) {
		String tooltip = modelI18NProperties.getProperty(modelObject, "tooltip");
		return tooltip != null ? tooltip : "";
	}


}
