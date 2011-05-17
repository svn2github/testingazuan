package it.eng.spagobi.meta.generator.jpamapping.wrappers;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaViewInnerJoinRelatioship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaViewInnerTable;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;

import java.util.List;

public interface IJpaView {
	
	String getPackage();

	public String getName();
	
	public String getDescription();
	
	String getClassName();
	
	String getQualifiedClassName();
	
	String getUniqueName();
	

	List<IJpaTable> getInnerTables();

	List<IJpaColumn> getColumns(JpaViewInnerTable table);

	List<JpaViewInnerJoinRelatioship> getJoinRelationships();
	
	List<IJpaSubEntity> getSubEntities();

}