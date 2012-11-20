/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.services.artifact.service;

import it.eng.spagobi.services.common.AbstractServiceImpl;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

public class ArtifactServiceImpl extends AbstractServiceImpl {

	static private Logger logger = Logger.getLogger(ArtifactServiceImpl.class);

	/**
	 * Instantiates a new artifact service impl.
	 */
	public ArtifactServiceImpl() {
		super();
	}

	/**
	 * return the artifact getted by name and type
	 * @param token. The token.
	 * @param user. The user.
	 * @param name. The artifact's name.
	 * @param type. The artifact's type.
	 * @return the content of the artifact.
	 */
	public DataHandler getArtifactContentByNameAndType(String token,String user, String name, String type){
		logger.debug("IN");
		String toReturn;
		try {
			validateTicket(token, user);
			ArtifactServiceImplSupplier a = new ArtifactServiceImplSupplier();			
			return a.getArtifactContentByNameAndType(name, type);
		} catch (Exception e) {
			logger.error("Exception", e);
			return null;
		} finally {
			logger.debug("OUT");
		}				
	}

	/**
	 * return the artifact getted by the id
	 * @param token. The token.
	 * @param user. The user.
	 * @param id. The artifact's id.
	 * @return the content of the artifact.
	 */
    public DataHandler getArtifactContentById(String token, String user, Integer id){
		logger.debug("IN");
		String toReturn;
		try {
			validateTicket(token, user);
			ArtifactServiceImplSupplier a = new ArtifactServiceImplSupplier();			
			return a.getArtifactContentById(id);
		} catch (Exception e) {
			logger.error("Exception", e);
			return null;
		} finally {
			logger.debug("OUT");
		}				
    }

}
