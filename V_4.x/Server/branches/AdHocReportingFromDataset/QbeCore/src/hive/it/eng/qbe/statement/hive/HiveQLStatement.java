package it.eng.qbe.statement.hive;

import org.apache.log4j.Logger;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.sql.SQLStatement;

public class HiveQLStatement extends SQLStatement{
	
	public static transient Logger logger = Logger.getLogger(HiveQLStatement.class);
	
	protected HiveQLStatement(IDataSource dataSource) {
		super(dataSource);
	}
	
	public HiveQLStatement(IDataSource dataSource, Query query) {
		super(dataSource, query);
	}

}
