package it.eng.spagobi.meta.datamarttree.tree.i18n;

import java.util.Locale;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.properties.IModelProperties;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelObject;
import it.eng.qbe.model.structure.ModelField;
import it.eng.spagobi.commons.utilities.StringUtilities;

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
