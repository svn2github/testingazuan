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

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.generator.GenerationException;
import it.eng.spagobi.meta.generator.IGenerator;
import it.eng.spagobi.meta.generator.SpagoBIMetaGeneratorPlugin;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaSubEntity;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaView;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaModel;
import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class JpaMappingCodeGenerator implements IGenerator {

	/**
	 * The base output dir as passed to the method generate (this class is not thread safe!) 
	 */
	protected File baseOutputDir;

	protected File srcDir;

	protected File distDir;



	/**
	 *   The velocity template directory
	 */
	private File templateDir;

	/**
	 * The velocity template used to map business table to java class
	 */
	private File tableTemplate;

	/**
	 * The velocity template used to map business view to json mapping file
	 */
	private File viewTemplate;

	/**
	 * The velocity template used to map business table's composed key to a java class
	 */
	private File keyTemplate;	

	/**
	 * The velocity template used to generate the persistence.xml file
	 */
	private File persistenceUnitTemplate;

	private File labelsTemplate;

	private File propertiesTemplate;

	private File cfieldsTemplate;

	private String persistenceUnitName;

	public static final String DEFAULT_SRC_DIR = "src";

	public static final String DEFAULT_DIST_DIR = "dist";


	private static final IResourceLocator RL = SpagoBIMetaGeneratorPlugin.getInstance().getResourceLocator(); 

	private static Logger logger = LoggerFactory.getLogger(JpaMappingCodeGenerator.class);


	public JpaMappingCodeGenerator() {
		String templatesDirRelativePath;

		logger.trace("IN");

		templatesDirRelativePath = null;
		try {
			templatesDirRelativePath = RL.getPropertyAsString("jpamapping.templates.dir", "templates");

			templateDir = RL.getFile(templatesDirRelativePath);
			logger.debug("Template dir is equal to [{}]", templateDir);
			Assert.assertTrue("Template dir [" + templateDir + "] does not exist", templateDir.exists());

			tableTemplate = new File(templateDir, "sbi_table.vm");
			logger.trace("[Table] template file is equal to [{}]", tableTemplate);
			Assert.assertTrue("[Table] template file [" + tableTemplate + "] does not exist", tableTemplate.exists());

			viewTemplate = new File(templateDir, "sbi_view.vm");
			logger.trace("[View] template file is equal to [{}]", viewTemplate);
			Assert.assertTrue("[View] template file [" + viewTemplate + "] does not exist", viewTemplate.exists());

			keyTemplate = new File(templateDir, "sbi_pk.vm");
			logger.trace("[Key] template file is equal to [{}]", keyTemplate);
			Assert.assertTrue("[Key] template file [" + keyTemplate + "] does not exist", keyTemplate.exists());

			persistenceUnitTemplate  = new File(templateDir, "sbi_persistence_unit.vm");
			logger.trace("[PersistenceUnit] template file is equal to [{}]", persistenceUnitTemplate);
			Assert.assertTrue("[PersistenceUnit] template file [" + persistenceUnitTemplate + "] does not exist", persistenceUnitTemplate.exists());

			labelsTemplate  = new File(templateDir, "sbi_labels.vm");
			logger.trace("[Labels] template file is equal to [{}]", labelsTemplate);
			Assert.assertTrue("[Labels] template file [" + labelsTemplate + "] does not exist", labelsTemplate.exists());

			propertiesTemplate  = new File(templateDir, "sbi_properties.vm");
			logger.trace("[Properties] template file is equal to [{}]", propertiesTemplate);
			Assert.assertTrue("[Properties] template file [" + propertiesTemplate + "] does not exist", propertiesTemplate.exists());

			cfieldsTemplate  = new File(templateDir, "sbi_cfields.vm");
			logger.trace("[Calculated Fields] template file is equal to [{}]", cfieldsTemplate);
			Assert.assertTrue("[Calculated Fields] template file [" + cfieldsTemplate + "] does not exist", cfieldsTemplate.exists());
		} catch (Throwable t) {
			logger.error("Impossible to resolve folder [" + templatesDirRelativePath + "]", t);
		} finally{
			logger.trace("OUT");
		}

	}

	@Override
	public void generate(ModelObject o, String outputDir)  {
		BusinessModel model;

		logger.trace("IN");

		if(o instanceof BusinessModel) {
			model = (BusinessModel)o;
			try {
				baseOutputDir = new File(outputDir);
				deleteFile(baseOutputDir);
				baseOutputDir = new File(outputDir);

				logger.debug("Output dir is equal to [{}]", baseOutputDir);

				srcDir = (srcDir == null)? new File(baseOutputDir, DEFAULT_SRC_DIR): srcDir;
				logger.debug("src dir is equal to [{}]", srcDir);

				distDir = (distDir == null)? new File(baseOutputDir, DEFAULT_DIST_DIR): distDir;
				logger.debug("dist dir is equal to [{}]", distDir);

				if (distDir.mkdirs())
				{
					logger.debug("Created directory [{}]", distDir);
				}  

				generateJpaMapping(model);

				//Syncronization with eclipse workspace, hiding mapping directory

				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IPath location = Path.fromOSString(baseOutputDir.getAbsolutePath());
				IProject proj = workspace.getRoot().getProject(baseOutputDir.getParentFile().getParentFile().getName());
				workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
				IFolder iFolder = proj.getFolder(baseOutputDir.getParentFile().getName()+"\\"+baseOutputDir.getName()) ;
				if(iFolder.exists()){
					iFolder.setHidden(true);
					iFolder.setTeamPrivateMember(true);
					iFolder.setDerived(true, null);
					iFolder.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
					proj.refreshLocal(IResource.DEPTH_INFINITE, null);
				}


				logger.info("Jpa mapping code generated succesfully");
			} catch (Exception e) {
				logger.error("An error occur while generating jpa mapping code", e);
				throw new GenerationException("An error occur while generating JPA mapping", e);
			}
		} else {
			throw new GenerationException("Impossible to generate JPA mapping from an object of type [" + o.getClass().getName() + "]");
		}

		logger.trace("OUT");
	}

	/**
	 * Delete the file and all it's children
	 * @param file
	 */
	private void deleteFile(File file){
		if(!file.exists()){
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++){
				deleteFile(files[i]);
			}
		}
		boolean fileDeletionResult = file.delete();
		if(!fileDeletionResult){
			logger.error("Can't delete the file [{}] the file is writtable? [{}]", file.getAbsolutePath(), file.canWrite());
			throw new SpagoBIPluginException("Can't delete the file "+ file.getAbsolutePath()+" the file is writtable? "+file.canWrite() +" "+file.canExecute());
		}
	}

	/**
	 * Generate the JPA Mapping of one BusinessModel in one outputFile
	 * @param model BusinessModel
	 * @param outputFile File
	 */
	public void generateJpaMapping(BusinessModel model) throws Exception {

		logger.trace("IN");

		Velocity.setProperty("file.resource.loader.path", getTemplateDir().getAbsolutePath());

		JpaModel jpaModel = new JpaModel(model);
		generateBusinessTableMappings(  jpaModel.getTables() );
		logger.info("Java files for tables of model [{}] succesfully created", model.getName());

		generateBusinessViewMappings( jpaModel.getViews() );
		logger.info("Java files for views of model [{}] succesfully created", model.getName());

		createLabelsFile(labelsTemplate, jpaModel);
		logger.info("Labels file for model [{}] succesfully created", model.getName());

		createPropertiesFile(propertiesTemplate, jpaModel);
		logger.info("Properties file for model [{}] succesfully created", model.getName());

		generatePersistenceUnitMapping(jpaModel);
		logger.info("Persistence unit for model [{}] succesfully created", model.getName());

		createCfieldsFile(cfieldsTemplate, jpaModel);
		logger.info("Properties file for model [{}] succesfully created", model.getName());

		logger.trace("OUT");		
	}

	public void generateBusinessTableMappings(List<IJpaTable> jpaTables) throws Exception {
		//logger.debug("Creating mapping for business class [{}]", table.getName());

		for(IJpaTable jpaTable : jpaTables) {
			createJavaFile(tableTemplate, jpaTable, jpaTable.getClassName());
			logger.debug("Mapping for table [" + jpaTable.getName() + "] succesfully created");
			if (jpaTable.hasCompositeKey()) {
				createJavaFile(keyTemplate, jpaTable, jpaTable.getCompositeKeyClassName());
				logger.debug("Mapping for composite PK of business table [{}] succesfully", jpaTable.getName());				
			}
		}

		//logger.debug("Mapping for business class [{}] created succesfully", table.getName());
	}

	public void generateBusinessViewMappings(List<IJpaView> jpaViews) throws Exception {
		for(IJpaView jpaView : jpaViews) {
			generateBusinessTableMappings( jpaView.getInnerTables() ) ;
		}	
		createViewsFile(jpaViews);
	}



	public void generatePersistenceUnitMapping(JpaModel model) throws Exception {
		model.setPersistenceUnitName(persistenceUnitName);
		createPersistenceUnitFile(persistenceUnitTemplate, model);
	}


	/**
	 * This method create a single java class file
	 * @param templateFile
	 * @param businessTable
	 * @param jpaTable
	 */
	private void createJavaFile(File templateFile, IJpaTable jpaTable, String className){

		VelocityContext velocityContext;

		velocityContext = new VelocityContext();	  
		velocityContext.put("jpaTable", jpaTable ); //$NON-NLS-1$


		File outputDir = new File(srcDir, StringUtils.strReplaceAll(jpaTable.getPackage(), ".", "/") );
		outputDir.mkdirs();
		File outputFile = new File(outputDir, className+".java");

		createFile(templateFile, outputFile, velocityContext);

		//logger.debug("Created mapping file [{}] for table [{}]", outputFile, jpaTable.getClassName());
	}

	/**
	 * This method create a single java class file
	 * @param templateFile
	 * @param businessTable
	 * @param views
	 */
	private void createViewsFile(List<IJpaView> jpaViews){

		VelocityContext context;

		logger.trace("IN");

		try {

			context = new VelocityContext();
			context.put("jpaViews", jpaViews ); //$NON-NLS-1$


			File outputFile = new File(srcDir, "views.json");

			createFile(viewTemplate, outputFile, context);
		} catch(Throwable t) {
			logger.error("Impossible to create mapping", t);
		} finally {
			logger.trace("OUT");
		}

	}

	/**
	 * This method create a single java class file
	 * @param templateFile
	 * @param businessTable
	 * @param jpaView
	 */
	private void createPersistenceUnitFile(File templateFile, JpaModel model){

		VelocityContext context;

		logger.trace("IN");

		try {
			context = new VelocityContext();
			List<IJpaTable> jpaTables  = new ArrayList<IJpaTable>();
			jpaTables.addAll( model.getTables() );
			List<IJpaView> jpaViews = model.getViews();
			for(IJpaView jpaView : jpaViews) {
				jpaTables.addAll( jpaView.getInnerTables() );
			}

			context.put("jpaTables", jpaTables ); //$NON-NLS-1$
			context.put("model", model);

			File outputDir = new File( srcDir, "META-INF" );
			outputDir.mkdirs();

			File outputFile = new File(outputDir, "persistence.xml");

			createFile(templateFile, outputFile, context);
		} catch(Throwable t) {
			logger.error("Impossible to create persitance.xml", t);
		} finally {
			logger.trace("OUT");
		}
	}

	private void createLabelsFile(File templateFile, JpaModel model){

		VelocityContext context;

		logger.trace("IN");

		try {
			logger.debug("Create labels.properties");

			context = new VelocityContext();
			List<IJpaTable> tables = new ArrayList<IJpaTable>();
			tables.addAll( model.getTables() );
			for( IJpaView jpaView : model.getViews()) {
				tables.addAll(jpaView.getInnerTables());
			}

			context.put("jpaTables", tables ); //$NON-NLS-1$
			context.put("jpaViews", model.getViews() );


			File outputFile = new File(srcDir, "label.properties");

			createFile(templateFile, outputFile, context);
		} catch(Throwable t) {
			logger.error("Impossible to create label.properties", t);
		} finally {
			logger.trace("OUT");
		}
	}

	private void createPropertiesFile(File templateFile, JpaModel model){

		VelocityContext context;

		logger.trace("IN");

		try {
			logger.debug("Create qbe.properties");

			context = new VelocityContext();
			context.put("jpaTables", model.getTables() ); //$NON-NLS-1$
			context.put("jpaViews", model.getViews() ); //$NON-NLS-1$



			File outputFile = new File(srcDir, "qbe.properties");

			createFile(templateFile, outputFile, context);
		} catch(Throwable t) {
			logger.error("Impossible to create qbe.properties", t);
		} finally {
			logger.trace("OUT");
		}
	}

	private void createCfieldsFile(File templateFile, JpaModel model){
		VelocityContext context;

		logger.trace("IN");

		try {
			logger.debug("Create cfields_meta.xml");

			context = new VelocityContext();
			context.put("jpaTables", model.getTables() ); //$NON-NLS-1$
			context.put("jpaViews", model.getViews() ); //$NON-NLS-1$

			File outputFile = new File(distDir, "cfields_meta.xml");

			createFile(templateFile, outputFile, context);
		} catch(Throwable t) {
			logger.error("Impossible to create cfields_meta.xml", t);
		} finally {
			logger.trace("OUT");
		}
	}	


	private void createFile(File templateFile, File outputFile, VelocityContext context) {
		Template template;

		try {
			template = Velocity.getTemplate( templateFile.getName() );
		} catch (Throwable t) {
			throw new GenerationException("Impossible to load template file [" + templateFile + "]");
		}

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputFile);

			template.merge(context, fileWriter);

			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			logger.error("Impossible to generate output file from template file [" + templateFile + "]",e);
			throw new GenerationException("Impossible to generate output file from template file [" + templateFile + "]");
		}	
	}


	// =======================================================================
	// ACCESSOR METHODS
	// =======================================================================


	public File getTemplateDir() {
		return templateDir;
	}


	public void setTemplateDir(File templateDir) {
		this.templateDir = templateDir;
	}

	public File getSrcDir() {
		return srcDir;
	}

	public void setSrcDir(File srcDir) {
		this.srcDir = srcDir;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}





}
