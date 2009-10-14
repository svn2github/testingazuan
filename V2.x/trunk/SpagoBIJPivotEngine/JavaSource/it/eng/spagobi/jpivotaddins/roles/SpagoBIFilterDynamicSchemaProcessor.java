/*
 * ====================================================================
 * This software is subject to the terms of the Common Public License
 * Agreement, available at the following URL:
 *   http://www.opensource.org/licenses/cpl.html .
 * Copyright (C) 2003-2004 TONBELLER AG.
 * All Rights Reserved.
 * You must accept the terms of that agreement to use this software.
 * ====================================================================
 *
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
	
	protected String filter(String schemaUrl, Util.PropertyList connectInfo, InputStream stream) throws Exception {
		logger.debug("IN");
		String originalSchema = super.filter(schemaUrl, connectInfo, stream);
		String modifiedSchema = originalSchema;
		if (connectInfo.get("filter") != null) {
			logger.debug("Filtering with  [" + connectInfo.get("filter") + "]");
			modifiedSchema = originalSchema.replaceAll("\\$\\{filter\\}", connectInfo.get("filter"));
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
	
}
