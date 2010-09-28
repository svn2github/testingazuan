/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.ModelPropertyCategory;
import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelDefaultPropertiesInitializer implements IPropertiesInitializer {
	
	static public ModelFactory FACTORY = ModelFactory.eINSTANCE;
	
	public void addProperties(ModelObject o) {
		
		if(o instanceof BusinessModel) {
			initModelProperties((BusinessModel)o);
		} else if(o instanceof BusinessTable) {
			initTableProperties((BusinessTable)o);
		} else if(o instanceof BusinessColumn) {
			initColumnProperties((BusinessColumn)o);
		} else if(o instanceof BusinessRelationship) {
			initRelationshipProperties((BusinessRelationship)o);
		} else {
			
		}
	}
	
	private void initModelProperties(BusinessModel o) {
		
	}

	private void initTableProperties(BusinessTable o) {
		
	}
	
	private void initColumnProperties(BusinessColumn o) {
		Model rootModel;
		ModelPropertyType propertyType;
		ModelProperty property;
		ModelPropertyCategory structuralCategory, styleCategory;
		
		rootModel = null;
		
		if(o.getTable() != null && o.getTable().getModel() != null) {
			rootModel = o.getTable().getModel().getParentModel();
		}
				
		structuralCategory =  o.getTable().getModel().getParentModel().getPropertyCategory("Structural");
		if(structuralCategory == null) {
			structuralCategory = FACTORY.createModelPropertyCategory();
			structuralCategory.setName("Structural");
			structuralCategory.setDescription("Structural properties");
			o.getTable().getModel().getParentModel().getPropertyCategories().add(structuralCategory);
		}		
		
		styleCategory =  o.getTable().getModel().getParentModel().getPropertyCategory("Style");
		if(styleCategory == null) {
			styleCategory = FACTORY.createModelPropertyCategory();
			styleCategory.setName("Style");
			styleCategory.setDescription("Style properties");
			o.getTable().getModel().getParentModel().getPropertyCategories().add(styleCategory);
		}
		
		
		// AGGREGATION TYPE
		propertyType = null;
		
		if(rootModel != null) propertyType = rootModel.getPropertyType("Agregation Type");
		if(propertyType == null) {
			propertyType = FACTORY.createModelPropertyType();
			propertyType.setName("Agregation Type");
			propertyType.setDescription("The preferred agregation type for the give column (COUNT, SUM, AVG, MAX, MIN)");
			propertyType.setCategory(structuralCategory);
			propertyType.getAdmissibleValues().add("COUNT");
			propertyType.getAdmissibleValues().add("SUM");
			propertyType.getAdmissibleValues().add("AVG");
			propertyType.getAdmissibleValues().add("MAX");
			propertyType.getAdmissibleValues().add("MIN");
			propertyType.setDefaultValue("COUNT");
			
			if(rootModel != null) rootModel.getPropertyTypes().add(propertyType);
		}
		
		property = FACTORY.createModelProperty();
		property.setPropertyType(propertyType);
		o.getProperties().put(property.getPropertyType().getName(), property);
		
		// DATA TYPE
		propertyType = null;
		
		if(rootModel != null) propertyType = rootModel.getPropertyType("Data Type");
		if(propertyType == null) {
			propertyType = FACTORY.createModelPropertyType();
			propertyType.setName("Data Type");
			propertyType.setDescription("The data type of the given column (VARCHAR, INTEGER, DOUBLE, ...)");
			propertyType.setCategory(structuralCategory);
			propertyType.getAdmissibleValues().add("VARCHAR");
			propertyType.getAdmissibleValues().add("INTEGER");
			propertyType.getAdmissibleValues().add("DOUBLE");
			propertyType.getAdmissibleValues().add("DATE");
			propertyType.getAdmissibleValues().add("TIMESTAMP");
			// ...
			propertyType.setDefaultValue("VARCHAR");
			
			if(rootModel != null) rootModel.getPropertyTypes().add(propertyType);
		}
		
		property = FACTORY.createModelProperty();
		property.setPropertyType(propertyType);
		o.getProperties().put(property.getPropertyType().getName(), property);
		
		
		// ALIGNMENT TYPE
		if(rootModel != null) propertyType = rootModel.getPropertyType("Alignment Type");
		if(propertyType == null) {
			propertyType = FACTORY.createModelPropertyType();
			propertyType.setName("Alignment Type");
			propertyType.setDescription("The preferred alignment type for the give column (LEFT, CENTER, RIGHT, JUSTIFIED)");
			propertyType.setCategory(structuralCategory);
			propertyType.getAdmissibleValues().add("LEFT");
			propertyType.getAdmissibleValues().add("CENTER");
			propertyType.getAdmissibleValues().add("RIGHT");
			propertyType.getAdmissibleValues().add("JUSTIFIED");
			propertyType.setDefaultValue("LEFT");
			
			if(rootModel != null) rootModel.getPropertyTypes().add(propertyType);
		}
		
		property = FACTORY.createModelProperty();
		property.setPropertyType(propertyType);
		o.getProperties().put(property.getPropertyType().getName(), property);
		
		
	}
	
	private void initRelationshipProperties(BusinessRelationship o) {
		
	}

}
