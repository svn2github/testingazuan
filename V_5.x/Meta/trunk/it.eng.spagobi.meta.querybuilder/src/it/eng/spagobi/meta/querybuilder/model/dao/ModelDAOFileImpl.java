/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.model.dao;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;

import java.io.File;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModelDAOFileImpl implements IModelDAO{
	
	File modelsFolder;
	
	private static Logger logger = LoggerFactory.getLogger(ModelDAOFileImpl.class);
	
	public ModelDAOFileImpl(File modelsFolder) {
		Assert.assertNotNull("Input parameter [" + modelsFolder + "] cannot be null", modelsFolder);
		if( (modelsFolder.exists() && modelsFolder.isDirectory()) == false ) {
			throw new ModelDAOException("The specified models' folder [" + modelsFolder + "] is not an existing folder");
		}

// 		the following test is commented because it fails even if the folder can be read
//		if( modelsFolder.canRead()) {
//			throw new ModelDAOException("The specified models' folder [" + modelsFolder + "]  cannot be read");
//		}

		this.modelsFolder = modelsFolder;
	}
	
	public Model loadModel(String modelName) {
		return loadModel( new File(modelsFolder, modelName) );
	}
	
	public Model loadModel(File modelFile) {
		Model model;
		
		logger.trace("IN");
		
		model = null;
		try {
			Assert.assertNotNull("Input parameter [" + modelsFolder + "] cannot be null", modelFile);
			if( (modelFile.exists() && modelFile.isFile()) == false ) {
				throw new ModelDAOException("The specified model file [" + modelFile + "] is not an existing file");
			}
			
//	 		the following test is commented because it fails even if the file can be read
//			if(modelFile.canRead()) {
//				throw new ModelDAOException("The specified models file [" + modelFile + "]  cannot be read");
//			}
			
			EmfXmiSerializer emfXmiSerializer = new EmfXmiSerializer();
			try {
				model = emfXmiSerializer.deserialize(modelFile);
			} catch(Throwable t) {
				throw new ModelDAOException("Impossible to deserialize the content of model file [" + modelFile + "]");
			}
			
			logger.debug("Model [{}] succesfully loaded from file [{}]", model.getName(), modelFile);
		} catch(ModelDAOException t) {
			throw t;
		} catch(Throwable t) {
			throw new SpagoBIPluginException("An unexpected error occurred while loading model from file [" + modelFile + "]", t);
		} finally {
			logger.trace("OUT");
		}
		
		return model;
	}
}
