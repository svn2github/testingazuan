/**
 * 
 */
package it.eng.qbe.export;

import java.sql.Connection;


/**
 * @author Gioia
 *
 */
public class SQLTemplateBuilder extends BasicTemplateBuilder {
	public SQLTemplateBuilder(String query, Connection connection) throws Exception {
		super(query, BasicTemplateBuilder.SQL_LANGUAGE, new SQLFieldsReader(query, connection).readFields());
	}
}
