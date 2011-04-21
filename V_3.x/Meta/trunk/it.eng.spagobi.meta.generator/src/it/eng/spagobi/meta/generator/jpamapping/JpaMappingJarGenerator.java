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
package it.eng.spagobi.meta.generator.jpamapping;

import it.eng.spagobi.meta.generator.GenerationException;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.JpaProperties;
import it.eng.spagobi.meta.generator.utils.Compiler;
import it.eng.spagobi.meta.generator.utils.Zipper;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessModel;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaMappingJarGenerator extends JpaMappingClassesGenerator {
	
	private File distDir;
	private String jarFileName;
	
	public static final String DEFAULT_DIST_DIR = "dist";
	public static final String DEFAULT_JAR_FILE_NAME = "datamart.jar";
	
	private static Logger logger = LoggerFactory.getLogger(JpaMappingJarGenerator.class);
	
	@Override
	public void generate(ModelObject o, String outputDir)  {
		logger.trace("IN");
		
		try {
			//The output dir is the model directory plus the business model name
			outputDir = outputDir+File.separator+o.getName();
			super.generate(o, outputDir);
			
			distDir = (distDir == null)? new File(baseOutputDir, DEFAULT_DIST_DIR): distDir;
			jarFileName = (jarFileName == null)? DEFAULT_JAR_FILE_NAME : jarFileName;
			
			Zipper zipper = new Zipper();
			zipper.compressToJar(getBinDir(), getJarFile());

		} catch(Throwable t) {
			logger.error("An error occur while generating JPA jar", t);
			throw new GenerationException("An error occur while generating JPA jar", t);
		} finally {		
			logger.trace("OUT");
		}
	}

	
	
	// =======================================================================
	// ACCESSOR METHODS
	// =======================================================================
	
	public File getDistDir() {
		return distDir;
	}

	public void setDistDir(File distDir) {
		this.distDir = distDir;
	}
	
	public File getJarFile() {
		return new File(distDir, jarFileName);
	}

	public String getJarFileName() {
		return jarFileName;
	}

	public void setJarFileName(String jarFileName) {
		this.jarFileName = jarFileName;
	}
}
