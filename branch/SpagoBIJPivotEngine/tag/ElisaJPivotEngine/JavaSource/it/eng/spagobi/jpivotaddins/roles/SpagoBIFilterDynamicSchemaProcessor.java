package it.eng.spagobi.jpivotaddins.roles;

import java.io.InputStream;

import mondrian.olap.Util;
import mondrian.spi.impl.FilterDynamicSchemaProcessor;

public class SpagoBIFilterDynamicSchemaProcessor extends
		FilterDynamicSchemaProcessor {

	protected String filter(String schemaUrl, Util.PropertyList connectInfo, InputStream stream) throws Exception {
		
		String originalSchema = super.filter(schemaUrl, connectInfo, stream);
		String modifiedSchema = originalSchema.replaceAll("\\$\\{family\\}", connectInfo.get("family"));
		return modifiedSchema;
    }
	
}
