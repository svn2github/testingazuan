/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

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
