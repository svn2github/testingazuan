/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.services.artifact.service;

import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.sdk.utilities.SDKObjectsConverter.MemoryOnlyDataSource;
import it.eng.spagobi.tools.catalogue.bo.Artifact;
import it.eng.spagobi.tools.catalogue.bo.Content;
import it.eng.spagobi.tools.catalogue.dao.IArtifactsDAO;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;
import org.apache.axis.attachments.ManagedMemoryDataSource;

public class ArtifactServiceImplSupplier {
    static private Logger logger = Logger.getLogger(ArtifactServiceImplSupplier.class);

    /**
	 * return the artifact getted by name and type
	 * @param token. The token.
	 * @param user. The user.
	 * @param name. The artifact's name.
	 * @param type. The artifact's type.
	 * @return the content of the artifact.
	 */
    public DataHandler getArtifactContentByNameAndType(String name, String type) {
		logger.debug("IN.name:" + name);
		logger.debug("IN.type:" + type);
		DataHandler toReturn;
		
		if (name == null || type == null)
		    return null;
	
		// gets artifact content from database
		try {
			IArtifactsDAO artdao = DAOFactory.getArtifactsDAO();
			Artifact artifact = artdao.loadArtifactByNameAndType(name, type);
			Content content = artdao.loadArtifactContentById(Integer.valueOf(artifact.getId()));
			byte[] cont = content.getContent();
			ManagedMemoryDataSource mods =  new ManagedMemoryDataSource(new java.io.ByteArrayInputStream(cont), Integer.MAX_VALUE - 2,
					null, true);
			toReturn = new DataHandler(mods);
			//BASE64Encoder bASE64Encoder = new BASE64Encoder();
			//toReturn = bASE64Encoder.encode(cont);			
			return toReturn;	
		} catch (Exception e) {
		    logger.error("The artifact is not correctly returned", e);
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
    public DataHandler getArtifactContentById(Integer id){
    	logger.debug("IN.id:" + id);
    	DataHandler toReturn;
    	
    	if (id == null)
    	    return null;

    	// gets artifact content from database
		try {
			IArtifactsDAO artdao = DAOFactory.getArtifactsDAO();
			Content content = artdao.loadArtifactContentById(id);
			byte[] cont = content.getContent();
			ManagedMemoryDataSource mods =  new ManagedMemoryDataSource(new java.io.ByteArrayInputStream(cont), Integer.MAX_VALUE - 2,
			null, true);
			toReturn = new DataHandler(mods);
			//BASE64Encoder bASE64Encoder = new BASE64Encoder();
			//toReturn = bASE64Encoder.encode(cont);			
			return toReturn;
		} catch (Exception e) {
			logger.error("The artifact is not correctly returned", e);
			return null;
		} finally {
			logger.debug("OUT");
		}				
    }
}
