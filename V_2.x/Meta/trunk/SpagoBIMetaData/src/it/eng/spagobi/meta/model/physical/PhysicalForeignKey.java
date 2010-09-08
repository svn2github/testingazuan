package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;
/**
 * @model
 */
public interface PhysicalForeignKey extends EObject {

	/**
	 * @model
	 */
	public abstract String getFkName();
	public abstract void setFkName(String fkName);

	/**
	 * @model
	 */
	public abstract String getPkName();
	public abstract void setPkName(String pkName);

	public abstract String getFkTableName();
	public abstract void setFkTableName(String fkTableName);
	public abstract List<String> getFkColumnNames();
	public abstract void setFkColumnNames(List<String> fkColumnNames);
	public abstract String getPkTableName();
	public abstract void setPkTableName(String pkTableName);
	public abstract List<String> getPkColumnNames();
	public abstract void addFkColumnName(String columnName);
	public abstract void addPkColumnName(String columnName);
	public abstract void setPkColumnNames(List<String> pkColumnNames);

	/**
	 * @model
	 */
	public abstract EList<PhysicalColumn> getFkColumns();
	public abstract void setFkColumns(List<PhysicalColumn> fkColumns);

	/**
	 * @model
	 */
	public abstract EList<PhysicalColumn> getPkColumns();
	public abstract void setPkColumns(List<PhysicalColumn> pkColumns);

	public abstract void addFkColumn(PhysicalColumn fkColumn);
	public abstract void addPkColumn(PhysicalColumn pkColumn);

	/**
	 * @model
	 */
	public abstract PhysicalTable getPkTable();
	public abstract void setPkTable(PhysicalTable pkTable);

	/**
	 * @model
	 */
	public abstract PhysicalTable getFkTable();
	public abstract void setFkTable(PhysicalTable fkTable);

}