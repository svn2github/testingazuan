package it.eng.spagobi.meta.datamarttree.tree.i18n;

import java.util.Locale;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.properties.i18n.ModelI18NProperties;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.ModelField;
import it.eng.spagobi.commons.utilities.StringUtilities;

public class ModelLabelProvider {

	private ModelI18NProperties labels;
	
	public ModelLabelProvider(IDataSource dataSource){
		// TODO PRENDERE IL LOCALE
		setLabels( dataSource.getModelI18NProperties( Locale.getDefault()));
	}
	
	
	public String getEntityLabel(IModelEntity entity) {
		String label;
		label = getLabels().getLabel(entity);
		return StringUtilities.isEmpty(label)? entity.getName(): label;
	}
	
	public String getEntityTooltip(IModelEntity entity) {
		String tooltip = getLabels().getTooltip(entity);
		return tooltip != null ? tooltip : "";
	}
	
	public String getFieldLabel(ModelField field) {
		String label;
		label = getLabels().getLabel(field);
		return StringUtilities.isEmpty(label)? field.getName(): label;
	}

	public String getFieldTooltip(ModelField field) {
		String tooltip = getLabels().getTooltip(field);
		return tooltip != null ? tooltip : "";
	}
	
	public ModelI18NProperties getLabels() {
		return labels;
	}
	
	public void setLabels(ModelI18NProperties datamartLabels) {
		this.labels = datamartLabels;
	}
	
}
