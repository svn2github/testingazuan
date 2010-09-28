/**
 * 
 */
package it.eng.spagobi.meta.generator.jpamapping;

import it.eng.spagobi.meta.commons.JDBCTypeMapper;
import it.eng.spagobi.meta.initializer.BusinessModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaTable {
	
	private BusinessTable businessTable;
	private HashMap<String, String> columnTypesMap =  null;
	
	public JpaTable() {
		businessTable = null;
	}
	
	public JpaTable(BusinessTable businessTable) {
		setBusinessTable(businessTable);
	}
	
	public BusinessTable getBusinessTable() {
		return businessTable;
	}

	public void setBusinessTable(BusinessTable businessTable) {
		this.businessTable = businessTable;
	}

	public String getPackage() {
		return "it.eng.spagobi";
	}
	
	public String getImportStatements(){
		buildColumnTypesMap();
		Collection<String> packages = columnTypesMap.keySet();
		StringBuilder ret = new StringBuilder();
		for ( String s : packages ) {
			ret.append( "import " + s + ";\n"); //$NON-NLS-1$
		}
		/*
		List<AssociationRole> associationRoles = getAssociationRoles();
		for ( AssociationRole role :  associationRoles ) {
			if ( role.getCardinality().equals( Association.ONE_TO_MANY )
					|| role.getCardinality().equals( Association.MANY_TO_MANY ) ) {
				ret.append( "import " + getDefaultCollectionType() + ";\n"); //$NON-NLS-1$
				break;
			}
		}
		*/
		
		return ret.toString();
	}
	
	public HashMap<String, String> buildColumnTypesMap(){
		if ( columnTypesMap != null) {
			return columnTypesMap;
		}
		columnTypesMap = new HashMap<String, String>();
		for ( BusinessColumn column : businessTable.getColumns() ) {
			ModelProperty property = column.getProperties().get(BusinessModelDefaultPropertiesInitializer.COLUMN_DATATYPE);
			String modelType = property.getValue();
			String javaType = JDBCTypeMapper.getJavaTypeName(modelType);
			System.out.println(modelType + " : " + javaType);
			if ( /*!col.isPartOfCompositePrimaryKey()
					&& !col.isForeignKey()
					&& */ !javaType.startsWith("java.lang") && javaType.indexOf('.') > 0 ) {
				String simpleJavaType= javaType.substring( javaType.lastIndexOf('.')+1 );
				columnTypesMap.put(javaType, simpleJavaType);
			}
		}
		return columnTypesMap;
	}
	
	/**
	 * Return true if the values of name element in the @Table is default so we
	 * can skip generating the annotation
	 * 
	 * @return true
	 */
	public boolean isDefaultname() {
		return true;
	}
	
	/**
	 * Returns the generated Java class name (not qualified).
	 */
	public String getClassName() {
		String name;
		name = StringUtil.tableNameToVarName(businessTable.getPhysicalTable().getName());
		name = StringUtil.initUpper(name);
		//name = EntityGenTools.convertToUniqueJavaStyleClassName(getName(), new ArrayList<String>());
		//name = StringUtil.singularise(name);
		return name;
	}
}
