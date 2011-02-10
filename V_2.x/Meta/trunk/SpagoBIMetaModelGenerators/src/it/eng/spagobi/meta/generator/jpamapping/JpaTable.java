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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaTable {
	
	private static Logger logger = LoggerFactory.getLogger(JpaTable.class);
	
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
	private List<JpaColumn> getColumns() {
		if (jpaColumns == null) {
			jpaColumns = new ArrayList<JpaColumn>();
			for (BusinessColumn c : businessTable.getColumns()) {
				JpaColumn jpaColumn = new JpaColumn(c);
				jpaColumn.setJpaTable(this);
				jpaColumns.add(jpaColumn);
				logger.debug("Add Column:"+jpaColumn.getColumnName());
			}
		}
		return jpaColumns;
	}

	/**
	 * return the Package name....  TODO this should be read from model ( e.g. like a simple property )
	 * @return
	 */
	public String getPackage() {
		return "it.eng.spagobi.meta";
	}
	
	/**
	 * Return all the import statements ( the properties type )
	 * @return
	 */
	public String getImportStatements(){
		logger.debug("IN");
		buildColumnTypesMap();
		Collection<String> packages = columnTypesMap.keySet();
		StringBuilder ret = new StringBuilder();
		for ( String s : packages ) {
			ret.append( "import " + s + ";\n"); //$NON-NLS-1$
		}

		List<JpaRelationship> relationship = getRelationships();
		for ( JpaRelationship role :  relationship ) {
			if ( role.getCardinality().equals( JpaRelationship.ONE_TO_MANY )
					|| role.getCardinality().equals( JpaRelationship.MANY_TO_MANY ) ) {
				ret.append( "import " + role.getCollectionType() + ";\n"); //$NON-NLS-1$
				break;
			}
		}
		logger.debug("OUT: "+ret.toString());
		return ret.toString();
	}
	
	/**
	 * Returns true if the table has a Primary KEY
	 */
	public boolean hasPrimaryKey() {		
		return businessTable.getIdentifier() != null? businessTable.getIdentifier().getColumns().size() > 0 : false;
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
	 * Return only the PK column
	 * @return
	 */
	public List<JpaColumn> getPrimaryKeyColumns(){
		List<JpaColumn> result = new ArrayList<JpaColumn>();
		List<JpaColumn> columns = getColumns();
		for (int i = 0, n = columns.size(); i < n; ++i) {
			JpaColumn column = columns.get(i);
			if (column.isIdentifier())	{
				result.add(column);
				logger.debug("add PrimaryKeyColumns:"+column.getColumnName());
			}
		}
		return result;		
	}
	/**
	 * TODO .... da implementare
	 * Returns the composite key property name.
	 */
	public String getCompositeKeyPropertyName() {
		return "compId"; //$NON-NLS-1$
	}

	/**
	 * Return the name of the metod GETTER
	 * @return
	 */
	public String getCompositeKeyPropertyNameGetter() {
		return "get"+StringUtil.initUpper(getCompositeKeyPropertyName());
	}
	/**
	 * Return the name of the metod SETTER
	 * @return
	 */
	public String getCompositeKeyPropertyNameSetter() {
		return "set"+StringUtil.initUpper(getCompositeKeyPropertyName());
	}	
	
	/**
	 * 
	 * @return
	 */
	public String getPrimaryKeyEqualsClause(){
		List<JpaColumn> columns=getPrimaryKeyColumns();
		String result=null;
		for (int i = 0, n = columns.size(); i < n; ++i) {
			JpaColumn column = columns.get(i);
			if (result==null) result="( this."+column.getPropertyName()+".equals(castOther."+column.getPropertyName()+") )";
			else result=result+" \n && ( this."+column.getPropertyName()+".equals(castOther."+column.getPropertyName()+") )";
		}
		if (result==null) return "";
		else return result+";";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPrimaryKeyHashCodeClause(){
		List<JpaColumn> columns=getPrimaryKeyColumns();
		String result=null;
		for (int i = 0, n = columns.size(); i < n; ++i) {
			JpaColumn column = columns.get(i);
			if (result==null)  result=" hash = hash * prime + this."+column.getPropertyName()+".hashCode() ;\n";
			else result=result+" hash = hash * prime + this."+column.getPropertyName()+".hashCode() ;\n";
		}
		if (result==null) return "";
		return result;		
	}
	/**
	 * Returns the <code>JpaColumn</code> objects for the the columns that
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
	
	/**
	 * Return the <code>JpaRelationship</code> that contains this table
	 * @return
	 */
	public List<JpaRelationship> getRelationships() {
		logger.debug("IN");
		List<JpaRelationship> jpaRelationships;
		JpaRelationship jpaRelationship=null;
		
		jpaRelationships = new ArrayList<JpaRelationship>();
		logger.info("Number of relationschip of TABLE "+this.getBusinessTable().getName()+" : "+businessTable.getRelationships().size());
		for(BusinessRelationship relationshp : businessTable.getRelationships()) {
			jpaRelationship = new JpaRelationship(this, relationshp);
			
			if (jpaRelationship.getBusinessRelationship()==null || 
					jpaRelationship.getBusinessRelationship().getSourceTable()==null){
				logger.error("There is a problem , the relationship doesn't have any source Table");
				continue;
			}
			if (jpaRelationship.getBusinessRelationship()==null || 
					jpaRelationship.getBusinessRelationship().getDestinationTable()==null){
				logger.error("There is a problem , the relationship doesn't have any destination Table");
				continue;
			}			
			if (jpaRelationship.getBusinessRelationship().getSourceTable().equals(this.getBusinessTable())){
				// many-to-one
				jpaRelationship.setCardinality(JpaRelationship.MANY_TO_ONE);
			}else if (jpaRelationship.getBusinessRelationship().getDestinationTable().equals(this.getBusinessTable())){
				// one-to-many
				jpaRelationship.setCardinality(JpaRelationship.ONE_TO_MANY);				
			}
			if (jpaRelationship!=null) {
				jpaRelationship.setBidirectional(true);
				jpaRelationships.add(jpaRelationship);
			}			
		}
		logger.debug("OUT");
		return jpaRelationships;		
	}
	
	
	
	/**
	 * build the hasmap that contains the properties type of this "Business Table"
	 * @return
	 */
	private void buildColumnTypesMap() {
		if (columnTypesMap == null) {
			columnTypesMap = new HashMap<String, String>();
			for (BusinessColumn column : businessTable.getColumns()) {
				ModelProperty property = column.getProperties().get(BusinessModelDefaultPropertiesInitializer.COLUMN_DATATYPE);
				String modelType = property.getValue();
				String javaType = JDBCTypeMapper.getJavaTypeName(modelType);
				if ( /*
					 * !col.isPartOfCompositePrimaryKey() && !col.isForeignKey()
					 * &&
					 */!javaType.startsWith("java.lang")
						&& javaType.indexOf('.') > 0) {
					String simpleJavaType = javaType.substring(javaType
							.lastIndexOf('.') + 1);
					columnTypesMap.put(javaType, simpleJavaType);
				}
			}
		}
	}
	
	
	/**
	 * Returns the generated Java class name (not qualified).
	 */
	public String getClassName() {
		String name;
		name = StringUtil.tableNameToVarName(businessTable.getPhysicalTable().getName());
		name = StringUtil.initUpper(name);
		return name;
	}
	

	/**
	 * TODO .. da implementare
	 * @return
	 */
	public String getDefaultFetch(){
		return "lazy";
	}
}
