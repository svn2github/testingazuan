package it.eng.spagobi.meta.generator.jpamapping.wrappers;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaViewInnerJoinRelatioship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaViewInnerTable;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;

import java.util.List;

public interface IJpaView {
	
	String getPackage();

	String getClassName();

	List<IJpaTable> getInnerTables();

	List<IJpaColumn> getColumns(JpaViewInnerTable table);

	List<JpaViewInnerJoinRelatioship> getJoinRelationships();

}