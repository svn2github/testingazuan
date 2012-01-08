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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.spagobi.meta.generator.GenerationException;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.JpaProperties;
import it.eng.spagobi.meta.generator.utils.Compiler;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessModel;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaMappingClassesGenerator extends JpaMappingCodeGenerator {
	
	//List<File> libs;
	
	private File libDir;
	private File binDir;
	
	public static final String DEFAULT_BIN_DIR = "build";
	public static final String DEFAULT_LIB_DIR = "libs";

	private String[] libs = {
		"org.eclipse.persistence.core_2.1.1.v20100817-r8050.jar",
	  	"javax.persistence_2.0.1.v201006031150.jar"
	};
	
	private static Logger logger = LoggerFactory.getLogger(JpaMappingClassesGenerator.class);
	
	public JpaMappingClassesGenerator() {
		super();
	}
	
	

	@Override
	public void generate(ModelObject o, String outputDir)  {
		logger.trace("IN");
		
		try {
			BusinessModel model;
			
			super.generate(o, outputDir);
			
			binDir = (binDir == null)? new File(outputDir, DEFAULT_BIN_DIR): binDir;
			logger.debug("src dir is equal to [{}]", getSrcDir());
			libDir = (libDir == null)? new File(outputDir, DEFAULT_LIB_DIR): libDir;
			logger.debug("lib dir is equal to [{}]", libDir);
			
			model = (BusinessModel)o;
			
			//Get Package Name
			String packageName = model.getProperties().get(JpaProperties.MODEL_PACKAGE).getValue();
				
			//Call Java Compiler
			Compiler compiler;
			
			
			compiler = new Compiler(
					getSrcDir(), binDir, libDir, packageName.replace(".", "/")
			);
			compiler.addLibs(libs);
			
			
			
			boolean compiled = compiler.compile();
			
			if(!compiled) {
				throw new GenerationException("Impossible to compile mapping code");
			}
			
			copyFile( new File(srcDir, "views.json"),  binDir);
			copyFile( new File(srcDir, "label.properties"),  binDir);
			copyFile( new File(srcDir, "qbe.properties"),  binDir);
			copyFile( new File(srcDir, "META-INF/persistence.xml"),  new File(binDir, "META-INF"));
			
		} catch(Throwable t) {
			logger.error("An error occur while generating JPA jar", t);
			throw new GenerationException("An error occur while generating JPA jar", t);
		} finally {		
			logger.trace("OUT");
		}
	}
	
	private void copyFile(File sourceFile, File destinationFolder) {
		try {
			File destinationFile = new File(destinationFolder, sourceFile.getName());
			if(!destinationFolder.exists()) {
				destinationFolder.mkdirs();
			}
		    InputStream in = new FileInputStream(sourceFile);
		    OutputStream out = new FileOutputStream(destinationFile);

		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0){
		    	out.write(buf, 0, len);
		    }
		    in.close();
		    out.close();
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to copy file [" + sourceFile + "] into folder [" + destinationFolder + "]", t);
		}

	}
	
	// =======================================================================
	// ACCESSOR METHODS
	// =======================================================================
	
	public File getBinDir() {
		return binDir;
	}

	public void setBinDir(File binDir) {
		this.binDir = binDir;
	}

	
	public void setLibDir(File libDir) {
		this.libDir = libDir;
	}

	public File getLibDir() {
		return libDir;
	}

	public void setLibs(String[] libs) {
		this.libs = libs;
	}
}
