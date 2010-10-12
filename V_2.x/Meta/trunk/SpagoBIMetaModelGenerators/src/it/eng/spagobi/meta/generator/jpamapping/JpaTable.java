/**
 * 
 */
package it.eng.spagobi.meta.generator.jpamapping;

import it.eng.spagobi.meta.commons.JDBCTypeMapper;
import it.eng.spagobi.meta.initializer.BusinessModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaTable {
	
	private BusinessTable businessTable;
	
	// cache
	List<JpaColumn> jpaColumns = null;
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
	
	/**
	 * Returns the <code>JpaColumn</code> objects to be generated for this
	 * table.
	 */
	public List<JpaColumn> getColumns() {
		if (jpaColumns == null) {
			jpaColumns = new ArrayList<JpaColumn>();
			for (BusinessColumn c : businessTable.getColumns()) {
				JpaColumn jpaColumn = new JpaColumn(c);
				jpaColumn.setJpaTable(this);
				jpaColumns.add(jpaColumn);
			}
		}
		return jpaColumns;
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
	
	/**
	 * Returns one of {@link #PROPERTY_ACCESS}|{@link #FIELD_ACCESS} indicating
	 * how the entity properties are mapped. Does not return null (defaults to
	 * {@link #FIELD_ACCESS}).
	 */
	public String getAccess() {
		String name = null;
		//String name = customized(ACCESS);
		if (name == null) {
			name = "field";
		}
		return name;
	}
	
	/**
	 * Returns true if there is more than 1 column in the table identifier
	 */
	public boolean hasCompositeKey() {		
		return businessTable.getIdentifier() != null? businessTable.getIdentifier().getColumns().size() > 1 : false;
	}
	
	/**
	 * Returns the composite key Java class name (not qualified).
	 */
	public String getCompositeKeyClassName() {
		String name = null;
		//String name = customized(COMPOSITE_KEY_CLASS_NAME);
		if (name == null) {
			name = getClassName() + "PK"; //$NON-NLS-1$
		}
		return name;
	}
	
	/**
	 * Returns the composite key property name.
	 */
	public String getCompositeKeyPropertyName() {
		return "id"; //$NON-NLS-1$
	}
	
	
	/**
	 * Returns the <code>ORMGenColumn</code> objects for the the columns that
	 * are not part of any association.
	 * 
	 * @param genOnly
	 *            Whether to include only the columns marked for generation.
	 * 
	 * @param includePk
	 *            Whether to include the primary kley column(s).
	 * 
	 * @param includeInherited
	 *            Whether to include the columns associated with Java properties
	 *            that exist in the super class (if any).
	 */
	public List<JpaColumn> getSimpleColumns(boolean genOnly, boolean includePk, boolean includeInherited) {
		List<JpaColumn> result = new ArrayList<JpaColumn>();
		List<JpaColumn> columns = getColumns();
	
		for (int i = 0, n = columns.size(); i < n; ++i) {
			JpaColumn column = columns.get(i);
			
			if (column.isIdentifier()) {
				if (!includePk || hasCompositeKey()) {
					continue;
				} else {
					result.add(0, column);
					continue;
				}
			} else if (column.isColumnInRelationship()) {
				continue;
			}
			result.add(column);
		}
		return result;
	}

	public List<JpaColumn> getSimpleColumns() {
		return getSimpleColumns(true/* genOnly */, true/* includePk */, true/* includeInherited */);
	}
	
	public List<JpaRelationship> getRelationships() {
		List<JpaRelationship> jpaRelationships;
		JpaRelationship jpaRelationship;
		
		jpaRelationships = new ArrayList<JpaRelationship>();
		for(BusinessRelationship relationshp : businessTable.getRelationships()) {
			jpaRelationship = new JpaRelationship(this, relationshp);
			jpaRelationships.add(jpaRelationship);
		}
		
		return jpaRelationships;		
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
			//System.out.println(modelType + " : " + javaType);
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
