/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.sdk.config;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

/**
 * Class that read spagobisdk-config.xml file
 */
public class SpagoBISDKConfig {

	public final String CONFIG_FILE_NAME = "spagobisdk-config.xml";
	
	private SourceBean config = null;

	private String spagoBiServerUrl = null;

	private static transient Logger logger = Logger
			.getLogger(SpagoBISDKConfig.class);

	private static SpagoBISDKConfig instance = null;

	/**
	 * Gets the instance.
	 * 
	 * @return SpagoBISDKConfig
	 */
	public static SpagoBISDKConfig getInstance() {
		if (instance == null)
			instance = new SpagoBISDKConfig();
		return instance;
	}

	private SpagoBISDKConfig() {
		try {
			logger.debug("Resource: "
					+ getClass().getResource("/" + CONFIG_FILE_NAME));
			if (getClass().getResource("/" + CONFIG_FILE_NAME) != null) {
				InputSource source = new InputSource(getClass()
						.getResourceAsStream("/" + CONFIG_FILE_NAME));
				config = SourceBean.fromXMLStream(source);
				setSpagoBIServerUrl();
			} else
				logger.debug("Impossible to load configuration");
		} catch (SourceBeanException e) {
			logger.error("Impossible to load configuration", e);
		}
	}

	/**
	 * Gets the config.
	 * 
	 * @return SourceBean contain the configuration
	 */
	public SourceBean getConfig() {
		return config;
	}

	public String getSpagoBIServerUrl() {
		return spagoBiServerUrl;
	}

	private void setSpagoBIServerUrl() {
		logger.debug("IN");
		SourceBean sb = (SourceBean) config.getAttribute("SPAGOBI_SERVER_URL");
		String server = (String) sb.getCharacters();
		if (server != null && server.length() > 0) {
			spagoBiServerUrl = server;
		} else {
			sb = (SourceBean) config
					.getAttribute("SPAGOBI_SERVER_URL_JNDI_NAME");
			server = (String) sb.getCharacters();
			spagoBiServerUrl = SpagoBIUtilities.readJndiResource(server);
		}

		logger.debug("OUT");

	}

}
