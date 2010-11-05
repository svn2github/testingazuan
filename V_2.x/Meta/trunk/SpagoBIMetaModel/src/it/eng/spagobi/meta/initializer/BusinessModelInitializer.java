/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.eng.spagobi.meta.commons.IModelObjectFilter;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
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
public class BusinessModelInitializer {
	
	IPropertiesInitializer propertiesInitializer;
	
	static public BusinessModelFactory FACTORY = BusinessModelFactory.eINSTANCE;
	
	public BusinessModelInitializer() {
		setPropertiesInitializer( new BusinessModelDefaultPropertiesInitializer() );
	}
	
	public BusinessModel initialize(String modelName, PhysicalModel physicalModel) {
		return initialize(modelName, null, physicalModel);
	}
	public BusinessModel initialize(String modelName, IModelObjectFilter tableFilter, PhysicalModel physicalModel) {
		BusinessModel businessModel;
		
		try {
			businessModel = FACTORY.createBusinessModel();
			businessModel.setName(modelName);
			
			if(physicalModel.getParentModel() != null) {
				businessModel.setParentModel(physicalModel.getParentModel());
			}
			
			businessModel.setPhysicalModel(physicalModel);
			
			
			// for each physical model object create a related business object
			
			
			// tables
			addTables( physicalModel, tableFilter, businessModel );
			
			// idetifiers-primary keys
			addIdentifiers( physicalModel, businessModel );
			
			// relationships-foreign keys
			addRelationships( physicalModel, businessModel );
			
			getPropertiesInitializer().addProperties(businessModel);
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business model", t);
		}
		
		return businessModel;
	}

	public void addTables(PhysicalModel physicalModel, BusinessModel businessModel) {
		addTables(physicalModel, businessModel);
	}
	public void addTables(PhysicalModel physicalModel, IModelObjectFilter tableFilter, BusinessModel businessModel) {
		PhysicalTable physicalTable;		
		
		try {
			for(int i = 0; i < physicalModel.getTables().size(); i++) {
				physicalTable = physicalModel.getTables().get(i);
				if(tableFilter == null || !tableFilter.filter(physicalTable)) {
					addTable(physicalTable, businessModel,false);
				}
			}
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business tables", t);
		}	
	}
	
	public void addTable(PhysicalTable physicalTable,  BusinessModel businessModel, boolean addIdentifier) {
		addTable(physicalTable, null, businessModel, addIdentifier);
	}
	public void addTable(PhysicalTable physicalTable, IModelObjectFilter columnFilter, BusinessModel businessModel, boolean addIdentifier) {
		BusinessTable businessTable;
		
		try {
			businessTable = FACTORY.createBusinessTable();
			
			businessTable.setPhysicalTable(physicalTable);
			businessTable.setName( beutfyName(physicalTable.getName()) );
			businessTable.setDescription( physicalTable.getDescription() );
			businessTable.setModel(businessModel);
							
			addColumns(physicalTable, columnFilter, businessTable);
			
			businessModel.getTables().add(businessTable);
			
			//adding table identifier if requested
			if(addIdentifier){
				if (physicalTable.getPrimaryKey() != null){
					//addIdentifier(physicalTable.getPrimaryKey(),businessModel);
					addIdentifier(businessTable, businessModel);
				}
			}
						
			getPropertiesInitializer().addProperties(businessTable);	
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business table from physical table [" + physicalTable.getName() + "]", t);
		}
	}
	

	public void addColumns(PhysicalTable physicalTable, BusinessTable businessTable) {
		addColumns(physicalTable, null, businessTable);
	}
	public void addColumns(PhysicalTable physicalTable, IModelObjectFilter columnFilter, BusinessTable businessTable) {
		PhysicalColumn physicalColumn;
		
		try {
			for(int i = 0; i < physicalTable.getColumns().size(); i++) {
				physicalColumn = physicalTable.getColumns().get(i);	
				if(columnFilter == null || !columnFilter.filter(physicalColumn)) {
					addColumn(physicalColumn, businessTable);
				}
			}
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize columns meta", t);
		}
	}
	
	public void addColumn(PhysicalColumn physicalColumn, BusinessTable businessTable) {
		BusinessColumn businessColumn;
		
		try {
			businessColumn = FACTORY.createBusinessColumn();
			
			businessColumn.setPhysicalColumn(physicalColumn);
			businessColumn.setName( beutfyName(physicalColumn.getName()) );
			businessColumn.setDescription( physicalColumn.getDescription() );
			businessColumn.setTable(businessTable);
			
			businessTable.getColumns().add(businessColumn);
			
			getPropertiesInitializer().addProperties(businessColumn);
			businessColumn.setProperty(BusinessModelDefaultPropertiesInitializer.COLUMN_DATATYPE, physicalColumn.getDataType());
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business column from physical column [" + physicalColumn.getName() + "]", t);
		}
	}

	
	public void addIdentifiers(PhysicalModel physicalModel, BusinessModel businessModel) {
		PhysicalPrimaryKey physicalPrimaryKey;
		
		try {
			for(int i = 0; i < physicalModel.getPrimaryKeys().size(); i++) {
				physicalPrimaryKey = physicalModel.getPrimaryKeys().get(i);
				addIdentifier(physicalPrimaryKey, businessModel);
			}
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize identifier meta", t);
		}
	}
	
	public void addIdentifier(PhysicalPrimaryKey physicalPrimaryKey, BusinessModel businessModel) {
		BusinessIdentifier businessIdentifier;
		PhysicalTable physicalTable;
		BusinessTable businessTable;
		BusinessColumn businessColumn;
		
		try {
			businessIdentifier = FACTORY.createBusinessIdentifier();
			businessIdentifier.setName( physicalPrimaryKey.getName() );
			businessIdentifier.setPhysicalPrimaryKey(physicalPrimaryKey);
			businessIdentifier.setModel(businessModel);
			
			physicalTable = physicalPrimaryKey.getTable();
			businessTable = businessModel.getTable( physicalTable );
			businessIdentifier.setTable(businessTable);
			
			for(int j = 0; j < physicalPrimaryKey.getColumns().size(); j++) {
				businessColumn = businessTable.getColumn(physicalPrimaryKey.getColumns().get(j));
				if (businessColumn != null){
					businessIdentifier.getColumns().add(businessColumn);
				}
			}
			
			if (businessIdentifier.getColumns().size() > 0){
				businessModel.getIdentifiers().add(businessIdentifier);			
				getPropertiesInitializer().addProperties(businessIdentifier);
			}
			else{
				//remove "empty" Business Identifier from memory
				businessIdentifier = null;
			}
				
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize identifier meta", t);
		}
	}
	
	//add Identifier without PhysicalPrimaryKey specified
	public void addIdentifier(String businessIdentifierName, BusinessTable businessTable, List<BusinessColumn> businessColumns) {
		BusinessIdentifier businessIdentifier;
		BusinessModel businessModel = businessTable.getModel();
		
		try {
			businessIdentifier = FACTORY.createBusinessIdentifier();
			businessIdentifier.setName( businessIdentifierName );
			businessIdentifier.setModel( businessModel );
			businessIdentifier.setTable( businessTable );
			
			for(BusinessColumn businessColumn : businessColumns) {
				businessIdentifier.getColumns().add(businessColumn);
			}
			
			businessModel.getIdentifiers().add(businessIdentifier);			
			getPropertiesInitializer().addProperties(businessIdentifier);
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize identifier meta", t);
		}
	}
	
	//add Identifier with PhysicalPrimaryKey discovered through passed BusinessTable 
	public void addIdentifier(BusinessTable businessTable, BusinessModel businessModel){
		BusinessIdentifier businessIdentifier;		
		BusinessColumn businessColumn;
		PhysicalPrimaryKey physicalPrimaryKey = businessTable.getPhysicalTable().getPrimaryKey();
	
		if (physicalPrimaryKey != null) {
			try {
				businessIdentifier = FACTORY.createBusinessIdentifier();
				businessIdentifier.setName( physicalPrimaryKey.getName() );
				businessIdentifier.setModel( businessModel );
				businessIdentifier.setTable( businessTable );
				
				for(int j = 0; j < physicalPrimaryKey.getColumns().size(); j++) {
					businessColumn = businessTable.getColumn(physicalPrimaryKey.getColumns().get(j));
					if (businessColumn != null){
						businessIdentifier.getColumns().add(businessColumn);
					}
				}
				
				if (businessIdentifier.getColumns().size() > 0){
					businessModel.getIdentifiers().add(businessIdentifier);			
					getPropertiesInitializer().addProperties(businessIdentifier);
				}
				else{
					//remove "empty" Business Identifier from memory
					businessIdentifier = null;
				}
		
				getPropertiesInitializer().addProperties(businessIdentifier);
			} catch(Throwable t) {
				throw new RuntimeException("Impossible to initialize identifier meta", t);
			}
		}

	}
	
	
	public void addRelationships(PhysicalModel physicalModel, BusinessModel businessModel) {
		PhysicalForeignKey physicalForeignKey;
		
		try {
			for(int i = 0; i < physicalModel.getForeignKeys().size(); i++) {
				physicalForeignKey = physicalModel.getForeignKeys().get(i);
				addRelationship(physicalForeignKey, businessModel);
			}
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize relationship meta", t);
		}
	}
	
	public void addRelationship(PhysicalForeignKey physicalForeignKey, BusinessModel businessModel) {
		BusinessRelationship businessRelationship;
		PhysicalTable physicalTable;
		BusinessTable businessTable;
		BusinessColumn businessColumn;
		
		try {
			businessRelationship = FACTORY.createBusinessRelationship();
			
			businessRelationship.setName( physicalForeignKey.getSourceName() );
			businessRelationship.setPhysicalForeignKey(physicalForeignKey);
			
			physicalTable = physicalForeignKey.getSourceTable();
			businessTable = businessModel.getTable( physicalTable );
			businessRelationship.setSourceTable(businessTable);
			for(int j = 0; j < physicalForeignKey.getSourceColumns().size(); j++) {
				businessColumn = businessTable.getColumn(physicalForeignKey.getSourceColumns().get(j));
				businessRelationship.getSourceColumns().add(businessColumn);
			}
			
			physicalTable = physicalForeignKey.getDestinationTable();
			businessTable = businessModel.getTable( physicalTable );
			businessRelationship.setDestinationTable(businessTable);
			for(int j = 0; j < physicalForeignKey.getDestinationColumns().size(); j++) {
				businessColumn = businessTable.getColumn(physicalForeignKey.getDestinationColumns().get(j));
				businessRelationship.getDestinationColumns().add(businessColumn);
			}
			
			businessModel.getRelationships().add(businessRelationship);
			
			getPropertiesInitializer().addProperties(businessRelationship);
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business relationship from physical foreign key [" + physicalForeignKey.getSourceName() + "]", t);
		}
	}
	
	//add Relationship without PhysicalForeignKey specified
	//public void addRelationship(BusinessTable sourceTable, BusinessTable destinationTable, List<BusinessColumn> sourceColumns, List<BusinessColumn> destinationColumns, String relationshipName){
	public void addRelationship(BusinessRelationshipDescriptor descriptor){
			
		BusinessRelationship businessRelationship;	
		BusinessModel businessModel = descriptor.getSourceTable().getModel();
		
		try {
			businessRelationship = FACTORY.createBusinessRelationship();
			
			if (descriptor.getRelationshipName() == null){
				businessRelationship.setName( "Business Relationship "+  descriptor.getSourceTable().getName()+"_"+  descriptor.getDestinationTable().getName() );
			}
			else {
				businessRelationship.setName( descriptor.getRelationshipName() );
			}

			businessRelationship.setSourceTable(descriptor.getSourceTable());
			for(BusinessColumn businessColumn : descriptor.getSourceColumns()) {
				businessRelationship.getSourceColumns().add(businessColumn);
			}			
			businessRelationship.setDestinationTable(descriptor.getDestinationTable());
			for(BusinessColumn businessColumn : descriptor.getDestinationColumns()) {
				businessRelationship.getDestinationColumns().add(businessColumn);
			}
			
			businessModel.getRelationships().add(businessRelationship);
			
			getPropertiesInitializer().addProperties(businessRelationship);
			
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business relationship", t);
		}
	}
	
	//  --------------------------------------------------------
	//	Accessor methods
	//  --------------------------------------------------------

	public IPropertiesInitializer getPropertiesInitializer() {
		return propertiesInitializer;
	}

	public void setPropertiesInitializer(IPropertiesInitializer propertyInitializer) {
		this.propertiesInitializer = propertyInitializer;
	}

	
	
	
	//  --------------------------------------------------------
	//	Static methods
	//  --------------------------------------------------------
	
	public static String beutfyName(String name) {
		return StringUtils.capitalize(name.replace("_", " "));
	}
	
	private static void log(String msg) {
		//System.out.println(msg);
	}
}
