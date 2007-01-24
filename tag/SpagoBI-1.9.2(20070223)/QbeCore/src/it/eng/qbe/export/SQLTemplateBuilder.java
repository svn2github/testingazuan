/**
 * 
 */
package it.eng.qbe.export;

import java.sql.Connection;
import java.util.Map;


/**
 * @author Gioia
 *
 */
public class SQLTemplateBuilder extends BasicTemplateBuilder {
	public SQLTemplateBuilder(String query, Connection connection, Map params, String orderedFldList, String extractedEntitiesList, String formulaFilePath) throws Exception {
		super(query, BasicTemplateBuilder.SQL_LANGUAGE, new SQLFieldsReader(query, connection).readFields(), params, orderedFldList, extractedEntitiesList, formulaFilePath);
	}
}
