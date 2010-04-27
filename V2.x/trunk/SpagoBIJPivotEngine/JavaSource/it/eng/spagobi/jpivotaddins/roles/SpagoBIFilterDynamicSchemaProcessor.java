/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.roles;

import java.io.File;
import java.io.InputStream;

import mondrian.olap.Util;
import mondrian.spi.impl.FilterDynamicSchemaProcessor;

import org.apache.log4j.Logger;

public class SpagoBIFilterDynamicSchemaProcessor extends
FilterDynamicSchemaProcessor {

	private static Logger logger = Logger.getLogger(SpagoBIFilterDynamicSchemaProcessor.class);

	Integer indexProgression = null;
	
	protected String filter(String schemaUrl, Util.PropertyList connectInfo, InputStream stream) throws Exception {
		logger.debug("IN");
		String originalSchema = super.filter(schemaUrl, connectInfo, stream);
		String modifiedSchema = originalSchema;
		// search for profile attributes to substitute in schema definition, identified by $P{attribute_name}
		indexProgression = Integer.valueOf(0);
		String att = findProfileAttributeInSchema(originalSchema);
		while(att != null){
			// if value is null I put null, if instead there is no the attribute name in connectInfo I don't substitute			
			if (connectInfo.get(att) != null) {
				logger.debug("change attribute " + att + " with  [" + connectInfo.get(att) + "]");
				modifiedSchema = modifiedSchema.replaceAll("\\$\\{"+att+"\\}", connectInfo.get(att));
			}		
			att = findProfileAttributeInSchema(modifiedSchema);
		}
		logger.debug("OUT");
		return modifiedSchema;
	}



	public String processSchema(
			String schemaUrl,
			Util.PropertyList connectInfo) throws Exception {
		logger.debug("IN: schemaUrl: " + schemaUrl);
		try {
			if (schemaUrl.startsWith("file:")) {
				schemaUrl = schemaUrl.substring("file:".length());
			}
			File schemaFile = new File(schemaUrl);
			schemaUrl = schemaFile.getAbsolutePath();
			logger.debug("Absolute file path: " + schemaUrl);
			return super.processSchema(schemaUrl, connectInfo);
		} finally {
			logger.debug("OUT");
		}
	}

	/** return the first profile attribute in schema
	 * 
	 * @param schema
	 * @param indexProgression. keeps track of the last found index to go always ahead in reading
	 * @return
	 */
	public String findProfileAttributeInSchema(String schema){
		logger.debug("IN");
		String toReturn = null;
		int index = schema.indexOf("${", indexProgression);	
		if (index != -1) {
			int indexEnd = schema.indexOf("}", index );	
			toReturn = schema.substring(index+2, indexEnd);
			indexProgression = new Integer(indexEnd);
		}
		logger.debug("OUT");
		return toReturn;
	}

}
