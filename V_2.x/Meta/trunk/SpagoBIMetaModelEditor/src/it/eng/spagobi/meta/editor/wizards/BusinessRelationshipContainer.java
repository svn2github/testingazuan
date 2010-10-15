/**
*  Utility Class used by the UI for creating BusinessRelationship
*/
package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessTable;

public class BusinessRelationshipContainer{
	private BusinessTable sourceTable, destinationTable;
	private java.util.List<BusinessColumn> sourceColumns, destinationColumns;
	private int relationCardinality;
	
	public BusinessRelationshipContainer(BusinessTable source, BusinessTable destination, java.util.List<BusinessColumn> sourceCol, java.util.List<BusinessColumn> destinationCol, int cardinality){
		sourceTable = source;
		destinationTable = destination;
		sourceColumns = sourceCol;
		destinationColumns = destinationCol;
		relationCardinality = cardinality;
	}

	/**
	 * @param sourceTable the sourceTable to set
	 */
	public void setSourceTable(BusinessTable sourceTable) {
		this.sourceTable = sourceTable;
	}

	/**
	 * @return the sourceTable
	 */
	public BusinessTable getSourceTable() {
		return sourceTable;
	}

	/**
	 * @param destinationTable the destinationTable to set
	 */
	public void setDestinationTable(BusinessTable destinationTable) {
		this.destinationTable = destinationTable;
	}

	/**
	 * @return the destinationTable
	 */
	public BusinessTable getDestinationTable() {
		return destinationTable;
	}

	/**
	 * @param sourceColumns the sourceColumns to set
	 */
	public void setSourceColumns(java.util.List<BusinessColumn> sourceColumns) {
		this.sourceColumns = sourceColumns;
	}

	/**
	 * @return the sourceColumns
	 */
	public java.util.List<BusinessColumn> getSourceColumns() {
		return sourceColumns;
	}

	/**
	 * @param destinationColumns the destinationColumns to set
	 */
	public void setDestinationColumns(java.util.List<BusinessColumn> destinationColumns) {
		this.destinationColumns = destinationColumns;
	}

	/**
	 * @return the destinationColumns
	 */
	public java.util.List<BusinessColumn> getDestinationColumns() {
		return destinationColumns;
	}

	/**
	 * @param relationCardinality the relationCardinality to set
	 */
	public void setRelationCardinality(int relationCardinality) {
		this.relationCardinality = relationCardinality;
	}

	/**
	 * @return the relationCardinality
	 */
	public int getRelationCardinality() {
		return relationCardinality;
	}
	
}