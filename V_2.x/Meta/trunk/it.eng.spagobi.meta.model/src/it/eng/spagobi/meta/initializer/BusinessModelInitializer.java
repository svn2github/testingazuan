/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.filter.IModelObjectFilter;
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

	/*
	 * Create an empty Business Model with only the Physical Model reference
	 */
	public BusinessModel initializeEmptyBusinessModel(String modelName, PhysicalModel physicalModel){
		BusinessModel businessModel;
		
		try {
			businessModel = FACTORY.createBusinessModel();
			businessModel.setName(modelName);
			
			if(physicalModel.getParentModel() != null) {
				businessModel.setParentModel(physicalModel.getParentModel());
			}
			
			businessModel.setPhysicalModel(physicalModel);
			
			getPropertiesInitializer().addProperties(businessModel);
		}
		catch(Throwable t) {
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
	
	public BusinessTable addTable(PhysicalTable physicalTable,  BusinessModel businessModel, boolean addIdentifier) {
		return addTable(physicalTable, null, businessModel, addIdentifier);
	}
	public BusinessTable addTable(PhysicalTable physicalTable, IModelObjectFilter columnFilter, BusinessModel businessModel, boolean addIdentifier) {
		BusinessTable businessTable;
		
		try {
			businessTable = FACTORY.createBusinessTable();
			
			businessTable.setPhysicalTable(physicalTable);
			//check if BusinessTable name is already used
			String businessTableName = beutfyName(physicalTable.getName());
			boolean nameUsed = checkNameAlreadyUsed(businessModel, businessTableName );
			if (!nameUsed){
				businessTable.setName( businessTableName );
			} else {
				while(nameUsed){
					businessTableName = businessTableName + "_copy";
					nameUsed = checkNameAlreadyUsed(businessModel, businessTableName );
				}	
				businessTable.setName( businessTableName );
			}
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
		return businessTable;
	}
	
	//create a BusinessTable with the name and description passed ad parameter
	public BusinessTable addTable(PhysicalTable physicalTable, IModelObjectFilter columnFilter, String businessTableName, String businessTableDescription, BusinessModel businessModel, boolean addIdentifier) {
		BusinessTable businessTable;
		
		try {
			businessTable = FACTORY.createBusinessTable();
			
			businessTable.setPhysicalTable(physicalTable);
			businessTable.setName( businessTableName );
			businessTable.setDescription( businessTableDescription);
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
		return businessTable;
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
	
	public void addColumn(PhysicalColumn physicalColumn, BusinessColumnSet businessColumnSet) {
		BusinessColumn businessColumn;
		
		try {
			businessColumn = FACTORY.createBusinessColumn();
			
			businessColumn.setPhysicalColumn(physicalColumn);
			businessColumn.setName( beutfyName(physicalColumn.getName()) );
			businessColumn.setDescription( physicalColumn.getDescription() );
			businessColumn.setTable(businessColumnSet);
			
			businessColumnSet.getColumns().add(businessColumn);
			
			getPropertiesInitializer().addProperties(businessColumn);
			businessColumn.setProperty(BusinessModelDefaultPropertiesInitializer.COLUMN_DATATYPE, physicalColumn.getDataType());
			businessColumn.setProperty(BusinessModelDefaultPropertiesInitializer.COLUMN_PHYSICAL_TABLE, businessColumn.getPhysicalColumn().getTable().getName());
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
	
	public BusinessIdentifier addIdentifier(PhysicalPrimaryKey physicalPrimaryKey, BusinessModel businessModel) {
		BusinessIdentifier businessIdentifier;
		PhysicalTable physicalTable;
		BusinessTable businessTable;
		BusinessColumn businessColumn;
		
		try {
			businessIdentifier = FACTORY.createBusinessIdentifier();
			businessIdentifier.setName( physicalPrimaryKey.getName() );
			businessIdentifier.setPhysicalPrimaryKey(physicalPrimaryKey);
			businessIdentifier.setModel(businessModel);
			//Note: use a filter on physical table to check?
			physicalTable = physicalPrimaryKey.getTable();
			businessTable = businessModel.getBusinessTable( physicalTable );
			
			//check if businessTable is present in Business Model
			if (businessTable != null){
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
			}
	
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize identifier meta", t);
		}
		return businessIdentifier;
	}
	
	//add Identifier without PhysicalPrimaryKey specified
	public BusinessIdentifier addIdentifier(String businessIdentifierName, BusinessColumnSet businessColumnSet, Collection<BusinessColumn> businessColumns) {
		BusinessIdentifier businessIdentifier;
		BusinessModel businessModel = businessColumnSet.getModel();
		
		try {
			businessIdentifier = FACTORY.createBusinessIdentifier();
			businessIdentifier.setName( businessIdentifierName );
			businessIdentifier.setModel( businessModel );
			businessIdentifier.setTable( businessColumnSet );
			
			for(BusinessColumn businessColumn : businessColumns) {
				businessIdentifier.getColumns().add(businessColumn);
			}
			
			businessModel.getIdentifiers().add(businessIdentifier);			
			getPropertiesInitializer().addProperties(businessIdentifier);
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize identifier meta", t);
		}
		return businessIdentifier;
	}
	
	//add Identifier with PhysicalPrimaryKey discovered through passed BusinessTable 
	public BusinessIdentifier addIdentifier(BusinessTable businessTable, BusinessModel businessModel){
		BusinessIdentifier businessIdentifier = null;		
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
		return businessIdentifier;
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
	
	public BusinessRelationship addRelationship(PhysicalForeignKey physicalForeignKey, BusinessModel businessModel) {
		BusinessRelationship businessRelationship;
		PhysicalTable physicalTable;
		BusinessTable businessTable;
		BusinessColumn businessColumn;
		
		try {
			businessRelationship = FACTORY.createBusinessRelationship();
			
			businessRelationship.setName( physicalForeignKey.getSourceName() );
			businessRelationship.setPhysicalForeignKey(physicalForeignKey);
			
			physicalTable = physicalForeignKey.getSourceTable();
			businessTable = businessModel.getBusinessTable( physicalTable );
			if (businessTable != null) {
				businessRelationship.setSourceTable(businessTable);
				for(int j = 0; j < physicalForeignKey.getSourceColumns().size(); j++) {
					businessColumn = businessTable.getColumn(physicalForeignKey.getSourceColumns().get(j));
					businessRelationship.getSourceColumns().add(businessColumn);
				}
				
				physicalTable = physicalForeignKey.getDestinationTable();
				businessTable = businessModel.getBusinessTable( physicalTable );
				if (businessTable != null) {
					businessRelationship.setDestinationTable(businessTable);
					for(int j = 0; j < physicalForeignKey.getDestinationColumns().size(); j++) {
						businessColumn = businessTable.getColumn(physicalForeignKey.getDestinationColumns().get(j));
						businessRelationship.getDestinationColumns().add(businessColumn);
					}
					
					businessModel.getRelationships().add(businessRelationship);
					
					getPropertiesInitializer().addProperties(businessRelationship);
				}

			}
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business relationship from physical foreign key [" + physicalForeignKey.getSourceName() + "]", t);
		}
		return businessRelationship;
	}
	
	
	
	//add Relationship without PhysicalForeignKey specified
	public BusinessRelationship addRelationship(BusinessRelationshipDescriptor descriptor){
			
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
		return businessRelationship;
	}
	
	
	
	/**
	 * Create a BusinessView using the data from a BusinessTable and the added PhysicalTable with a specified join path
	 * @return BusinessView created
	 */
	public BusinessView upgradeBusinessTableToBusinessView(BusinessTable businessTable, BusinessViewInnerJoinRelationshipDescriptor innerJoinRelationshipDescriptor){
		BusinessView businessView;
		BusinessModel businessModel = businessTable.getModel();
		Collection<BusinessColumn> businessColumns = businessTable.getColumns();
		
		Collection<BusinessRelationship> businessRelationships = businessTable.getRelationships();
		BusinessIdentifier businessIdentifier;
		
		try {
			//create BusinessViewInnerJoinRelationship object
			BusinessViewInnerJoinRelationship innerJoinRelationship = addBusinessViewInnerJoinRelationship(businessModel, innerJoinRelationshipDescriptor);
			
			businessView = FACTORY.createBusinessView();
			businessView.setModel(businessModel);
			businessView.setName(businessTable.getName());
			businessView.setDescription((businessTable.getDescription()));
			
			//add all the columns of Business Table to the Business View
			businessView.getColumns().addAll(businessColumns);
			//add the inner join relationship between two physical table
			businessView.getJoinRelationships().add(innerJoinRelationship);
			
			//check Business Table relationships 
			for( BusinessRelationship relationship : businessRelationships){
				if (relationship.getDestinationTable() == businessTable){
					//replace business table with business view
					relationship.setDestinationTable(businessView);
				}
				else if (relationship.getSourceTable() == businessTable){
					//replace business table with business view
					relationship.setSourceTable(businessView);
				}
			}

			//check Identifier to inherit
			businessIdentifier = businessTable.getIdentifier();
			if (businessIdentifier != null){
				businessIdentifier.setTable(businessView);
			}
			
			//add BusinessView to BusinessModel
			businessModel.getTables().add(businessView);
			
			//add BusinessView properties(?)
			getPropertiesInitializer().addProperties(businessView);
			
			//destroy Business Table
			businessModel.getTables().remove(businessTable);
		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business view", t);
		}
		return businessView;
	}
	
	/**
	 * Transform a BusinessView with only one PhysicalTable in the corresponding BusinessTable
	 * 
	 * @param businessView
	 * @return businessTable
	 */
	public BusinessTable downgradeBusinessViewToBusinessTable(BusinessView businessView){
		BusinessTable businessTable;
		PhysicalTable physicalTable;
		BusinessModel businessModel = businessView.getModel();
		Collection<BusinessColumn> businessColumns = businessView.getColumns();
		
		Collection<BusinessRelationship> businessRelationships = businessView.getRelationships();
		BusinessIdentifier businessIdentifier;
		
		try {
			//get the only PhysicalTable to mantain
			physicalTable = businessView.getJoinRelationships().get(0).getSourceTable();
			
			//remove BusinessViewInnerJoinRelationship object
			businessView.getJoinRelationships().clear();
			
			businessTable = FACTORY.createBusinessTable();
			businessTable.setModel(businessModel);
			businessTable.setName(businessView.getName());
			businessTable.setPhysicalTable(physicalTable);
			businessTable.setDescription(businessView.getDescription() );
			
			//add all the columns of Business View to the Business Table
			businessTable.getColumns().addAll(businessColumns);
			
			//check Business View relationships 
			for( BusinessRelationship relationship : businessRelationships){
				if (relationship.getDestinationTable() == businessView){
					//replace business view with business table
					relationship.setDestinationTable(businessTable);
				}
				else if (relationship.getSourceTable() == businessView){
					//replace business view with business table
					relationship.setSourceTable(businessTable);
				}
			}

			//check Identifier to inherit
			businessIdentifier = businessView.getIdentifier();
			if (businessIdentifier != null) {
				businessIdentifier.setTable(businessTable);
			}
			
			//add BusinessTable to BusinessView
			businessModel.getTables().add(businessTable);
			
			//add BusinessView properties(?)
			getPropertiesInitializer().addProperties(businessTable);
			
			//destroy Business View
			businessModel.getTables().remove(businessView);
		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business view", t);
		}
		return businessTable;		
	}
	
	/**
	 * Add a Physical Tables reference to the passed BusinessView
	 */
	public BusinessView addPhysicalTableToBusinessView(BusinessView businessView, BusinessViewInnerJoinRelationshipDescriptor joinRelationshipDescriptor){
		BusinessModel businessModel = businessView.getModel();
		
		try {
			//create BusinessViewInnerJoinRelationship object
			BusinessViewInnerJoinRelationship innerJoinRelationship = addBusinessViewInnerJoinRelationship(businessModel, joinRelationshipDescriptor);
			//add the inner join relationship between two physical table
			businessView.getJoinRelationships().add(innerJoinRelationship);			
		} 
		catch(Throwable t) {
			throw new RuntimeException("Impossible to add physical table to business view", t);
		}
		return businessView;
	}

	/**
	 * Remove a Physical Tables reference to the passed BusinessView
	 */
	
	public BusinessView removePhysicalTableToBusinessView(BusinessView businessView, BusinessViewInnerJoinRelationshipDescriptor joinRelationshipDescriptor){
		BusinessModel businessModel = businessView.getModel();
		
		try {
			//create BusinessViewInnerJoinRelationship object
			BusinessViewInnerJoinRelationship innerJoinRelationship = removeBusinessViewInnerJoinRelationship(businessModel, joinRelationshipDescriptor);
			
			//check if the removed physicalTable was in join with another PhysicalTable in this BusinessView
			EList<BusinessViewInnerJoinRelationship> joinRelationships = businessView.getJoinRelationships();
			for(BusinessViewInnerJoinRelationship joinRelationship : joinRelationships){
				//if the physicalTable to remove is used as a SourceTable in a joinRelationship
				//then DO NOT remove the physical table
				if (joinRelationship.getSourceTable() == innerJoinRelationship.getDestinationTable()){
					return null;
				}
			}
			
			//remove the inner join relationship between two physical table
			businessView.getJoinRelationships().remove(innerJoinRelationship);
		
			//remove physical table's columns
			EList<BusinessColumn> businessColumns = businessView.getColumns();
			for (BusinessColumn businessColumn : businessColumns){
				if (businessColumn.getPhysicalColumn().getTable() == innerJoinRelationship.getDestinationTable()){
					businessView.getColumns().remove(businessColumn);
				}
			}
		} 
		catch(Throwable t) {
			throw new RuntimeException("Impossible to remove physical table to business view", t);
		}
		return businessView;
	}	
	
	
	public BusinessViewInnerJoinRelationship removePhysicalTableToBusinessView(BusinessView businessView, PhysicalTable physicalTable){
		BusinessModel businessModel = businessView.getModel();
		BusinessViewInnerJoinRelationship innerJoinRelationship = null;

		try {
			if (isSourceTable(businessView,physicalTable)){
				//if the physicalTable to remove is used as a SourceTable in a joinRelationship
				//then DO NOT remove the physical table	
				return null;
			}
			else {
				//check if the removed physicalTable was in join with another PhysicalTable in this BusinessView
				EList<BusinessViewInnerJoinRelationship> joinRelationships = businessView.getJoinRelationships();
				for(BusinessViewInnerJoinRelationship joinRelationship : joinRelationships){
					if (joinRelationship.getDestinationTable() == physicalTable){
						innerJoinRelationship = joinRelationship;
						if (businessView.getJoinRelationships().size() == 1){
							//downgrade to BusinessTable
							downgradeBusinessViewToBusinessTable(businessView);
						}
						else{
							//remove the inner join relationship between two physical table
							businessView.getJoinRelationships().remove(joinRelationship);
							//remove he inner join relationship from BusinessModel
							businessModel.getJoinRelationships().remove(joinRelationship);
							
							//remove physical table's columns
							EList<BusinessColumn> businessColumns = businessView.getColumns();
							for (BusinessColumn businessColumn : businessColumns){
								if (businessColumn.getPhysicalColumn().getTable() == physicalTable){
									businessView.getColumns().remove(businessColumn);
								}
							}
						}
						break;
					}
				}
			}

			
		} 
		catch(Throwable t) {
			throw new RuntimeException("Impossible to remove physical table to business view", t);
		}
		return innerJoinRelationship;	
	}
	
	//check if the PhysicalTable is used as a SourceTable in the inner join relationship for BusinessView
	public boolean isSourceTable(BusinessView businessView, PhysicalTable physicalTable){
		EList<BusinessViewInnerJoinRelationship> joinRelationships = businessView.getJoinRelationships();
		for(BusinessViewInnerJoinRelationship joinRelationship : joinRelationships){
			if (joinRelationship.getSourceTable() == physicalTable){
				return true;
			} 
		}
		return false;
	}
	
	/**
	 * Create BusinessViewInnerJoinRelationship from a BusinessViewInnerJoinRelationshipDescriptor
	 * @param businessModel
	 * @return 
	 */
	public BusinessViewInnerJoinRelationship addBusinessViewInnerJoinRelationship(BusinessModel businessModel, BusinessViewInnerJoinRelationshipDescriptor innerJoinRelationshipDescriptor){
		BusinessViewInnerJoinRelationship innerJoinRelationship;
		try {
			innerJoinRelationship = FACTORY.createBusinessViewInnerJoinRelationship();
			innerJoinRelationship.setSourceTable(innerJoinRelationshipDescriptor.getSourceTable());
			innerJoinRelationship.getSourceColumns().addAll(innerJoinRelationshipDescriptor.getSourceColumns());
			innerJoinRelationship.setDestinationTable(innerJoinRelationshipDescriptor.getDestinationTable());
			innerJoinRelationship.getDestinationColumns().addAll(innerJoinRelationshipDescriptor.getDestinationColumns());	
			
			//get max identifier for innerJoinRelationship 
			long maxId = getMaxNumberInnerJoinRelationship(businessModel);
			//increase value to set new identifier
			maxId = maxId + 1;
			innerJoinRelationship.setId(new Long(maxId).toString());
			
			//add BusinessViewInnerJoinRelationship properties
			getPropertiesInitializer().addProperties(innerJoinRelationship);
			
			//add BusinessViewInnerJoinRelationship to BusinessModel
			businessModel.getJoinRelationships().add(innerJoinRelationship);
			
		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business view inner join relationship", t);
		}
		return innerJoinRelationship;
	}
	
	/**
	 * Remove BusinessViewInnerJoinRelationship from a BusinessViewInnerJoinRelationshipDescriptor
	 * @param businessModel
	 * @return 
	 */
	public BusinessViewInnerJoinRelationship removeBusinessViewInnerJoinRelationship(BusinessModel businessModel, BusinessViewInnerJoinRelationshipDescriptor innerJoinRelationshipDescriptor){
		EList<BusinessViewInnerJoinRelationship> joinRelationships;
		BusinessViewInnerJoinRelationship innerJoinRelationship = null;
		try {
			
			joinRelationships = businessModel.getJoinRelationships();
			//search the corresponding BusinessViewInnerJoinRelationship
			for(BusinessViewInnerJoinRelationship joinRelationship:joinRelationships){
				if ( (joinRelationship.getSourceTable() == innerJoinRelationshipDescriptor.getSourceTable()) && 
					(joinRelationship.getDestinationTable() == innerJoinRelationshipDescriptor.getDestinationTable())  )
				{   
					innerJoinRelationship = joinRelationship;
					break;
				}
			}
			//remove BusinessViewInnerJoinRelationship to BusinessModel
			businessModel.getJoinRelationships().remove(innerJoinRelationship);
			
		}
		catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business view inner join relationship", t);
		}
		return innerJoinRelationship;
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

	/*
	 * Check if business table name is already in use in the Business Model
	 */
	public boolean checkNameAlreadyUsed(BusinessModel businessModel, String BusinessTableName){
		if (businessModel.getTable(BusinessTableName) != null){
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Return the current maximum number used as identifier for InnerJoinRelationship objects
	 */
	public long getMaxNumberInnerJoinRelationship(BusinessModel model){
		EList<BusinessViewInnerJoinRelationship> joinRelationships = model.getJoinRelationships();
		long maxId = 0;
		for (BusinessViewInnerJoinRelationship joinRelation : joinRelationships){
			//id is a number that I convert to long
			String stringId = joinRelation.getId();
			long longId = Long.parseLong(stringId.trim());
			if (longId > maxId) {
				maxId = longId;
			}
		}
		return maxId;
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
