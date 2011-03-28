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
import it.eng.spagobi.meta.generator.IGenerator;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.texen.util.FileUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaMappingGenerator implements IGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(JpaMappingGenerator.class);

	/**
	 *   The Velocity template directory
	 */
	private File templateDir;
	private String outputDir=null;
	
	public JpaMappingGenerator() {
		logger.debug("IN");
		//templateDir=new File("templates"); 
		Bundle generatorBundle = Platform.getBundle("it.eng.spagobi.meta.generator");
		try {
			IPath path = new Path(Platform.asLocalURL(generatorBundle.getEntry("templates")).getPath());
			String templatePath = path.toString();
			logger.info("templatePath="+templatePath);
			templateDir = new File(templatePath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}finally{
			logger.debug("OUT");
		}

	}
	
	@Override
	public void generate(ModelObject o, String outputDir)  {
		BusinessModel model;
		
		if(o instanceof BusinessModel) {
			model = (BusinessModel)o;
			try {
				generateJpaMapping(model, outputDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.error("Impossible to create JPA Mapping from an object of type [" + o.getClass().getName() + "]");
			throw new GenerationException("Impossible to create JPA Mapping from an object of type [" + o.getClass().getName() + "]");
		}
	}
	
	/**
	 * Generate the JPA Mapping of one BusinessModel in one outputFile
	 * @param model BusinessModel
	 * @param outputFile File
	 */
	public void generateJpaMapping(BusinessModel model, String outputDir) throws Exception {
		this.outputDir=outputDir;
		BusinessTable businessTable;
		Velocity.setProperty("file.resource.loader.path", getTemplateDir().getAbsolutePath());
		Iterator<BusinessColumnSet> tables=model.getTables().iterator();
	
		while (tables.hasNext()) {
			Object table = tables.next();
			if (table instanceof BusinessTable){
				logger.info("Create Java Class for BC:"+((BusinessTable)table).getName());
				businessTable = (BusinessTable)table;
				JpaTable jpaTable=new JpaTable(businessTable);
				createFile("sbi_table.vm",jpaTable,jpaTable.getClassName());
				if (jpaTable.hasCompositeKey()) {
					logger.info("Create Composite PK Java Class for BC:"+((BusinessTable)table).getName());
					createFile("sbi_pk.vm",jpaTable,jpaTable.getCompositeKeyClassName());
				}
			}else if (table instanceof BusinessView){
				logger.info("Create Java Class for BV:"+((BusinessView)table).getName());
				
				List<PhysicalTable> phisicalTables=((BusinessView)table).getPhysicalTables();
				for (PhysicalTable phyT : ((BusinessView)table).getPhysicalTables()) {
					JpaView jpaView=new JpaView((BusinessView)table,phyT);
					createFile("sbi_table.vm",jpaView,jpaView.getClassName()); 
					if (jpaView.hasCompositeKey()) {
						logger.info("Create Composite PK Java Class for BV:"+((BusinessView)table).getName());
						createFile("sbi_pk.vm",jpaView,jpaView.getCompositeKeyClassName());
					}					
					
				}
			}
		}	
	}

	/**
	 * This method create a single java class file
	 * @param templateFile
	 * @param businessTable
	 * @param jpaTable
	 */
	private void createFile(String templateFile,JpaTable jpaTable,String className){
		Template template=null;
		VelocityContext context=null;
		logger.debug("IN. createFile. templateFile="+templateFile+" className="+className+ " jpaTable="+jpaTable.getClassName());
		FileWriter fileWriter=null;
		try {
			template = Velocity.getTemplate( templateFile );
		} catch (Throwable t) {
			throw new GenerationException("Impossible to load template file [" + templateFile + "]");
		}
		
	    context = new VelocityContext();
	    context.put("physicalTable", jpaTable.getPhysicalTable()); //$NON-NLS-1$
        //context.put("businessTable", jpaTable.getBusinessTable()); //$NON-NLS-1$
        context.put("jpaTable",jpaTable ); //$NON-NLS-1$
		try {
			String path=outputDir+"/"+StringUtil.strReplaceAll(jpaTable.getPackage(), ".", "/");
			createPackage(path);
			fileWriter=new FileWriter(path +"/"+className+".java");
			template.merge(context, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			logger.error("Impossible to generate output file from template file [" + templateFile + "]",e);
			throw new GenerationException("Impossible to generate output file from template file [" + templateFile + "]");
		}	
		logger.debug("OUT");
	}
	
	
	// =======================================================================
	// ACCESSOR METHODS
	// =======================================================================
	
	private void createPackage(String path){
			FileUtil.mkdir(path);
	}
	
	
	public File getTemplateDir() {
		return templateDir;
	}


	public void setTemplateDir(File templateDir) {
		this.templateDir = templateDir;
	}

}
