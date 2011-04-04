package it.eng.spagobi.meta.generator.jpamapping.wrappers;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.AbstractJpaRelationship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaViewInnerTable;

import java.util.List;

public interface IJpaView {
	
	String getPackage();

	String getClassName();

	List<IJpaTable> getTables();

	List<IJpaColumn> getColumns(JpaViewInnerTable table);

	List<AbstractJpaRelationship> getJoinRelationships();

}