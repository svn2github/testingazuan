/**
 * 
 */
package it.eng.spagobi.meta.generator.jpamapping;

import it.eng.spagobi.meta.generator.GenerationException;
import it.eng.spagobi.meta.generator.IGenerator;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.texen.util.FileUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaMappingGenerator implements IGenerator {
	
	/**
	 *   The Velocity template directory
	 */
	private File templateDir;
	private String outputDir=null;
	
	public JpaMappingGenerator() {

		//templateDir=new File("templates"); 
		Bundle generatorBundle = Platform.getBundle("it.eng.spagobi.meta.generator");
		try {
			IPath path = new Path(Platform.asLocalURL(generatorBundle.getEntry("templates")).getPath());
			String templatePath = path.toString();
			templateDir = new File(templatePath);
		} catch (IOException e) {
			e.printStackTrace();
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
			throw new GenerationException("Impossible to create JPA Mapping from an object of type [" + o.getClass().getName() + "]");
		}
	}
	
	/**
	 * Generate the JPA Mapping of one BusinessModel in one outputFile
	 * @param model BusinessModel
	 * @param outputFile File
	 */
	public void generateJpaMapping(BusinessModel model, String outputDir) {
		this.outputDir=outputDir;
		BusinessTable businessTable;
		Velocity.setProperty("file.resource.loader.path", getTemplateDir().getAbsolutePath());
		Iterator<BusinessColumnSet> tables=model.getTables().iterator();
	
		while (tables.hasNext()) {
			Object table = tables.next();
			if (table instanceof BusinessTable){
				businessTable = (BusinessTable)table;
				JpaTable jpaTable=new JpaTable(businessTable);
				createFile("sbi_table.vm",businessTable,jpaTable,jpaTable.getClassName());
				if (jpaTable.hasCompositeKey()) {
					createFile("sbi_pk.vm",businessTable,jpaTable,jpaTable.getCompositeKeyClassName());
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
	private void createFile(String templateFile,BusinessTable businessTable,JpaTable jpaTable,String className){
		Template template=null;
		VelocityContext context=null;

		FileWriter fileWriter=null;
		try {
			template = Velocity.getTemplate( templateFile );
		} catch (Throwable t) {
			throw new GenerationException("Impossible to load template file [" + templateFile + "]");
		}
		
	    context = new VelocityContext();
	    context.put("physicalTable", businessTable.getPhysicalTable()); //$NON-NLS-1$
        context.put("businessTable", businessTable); //$NON-NLS-1$
        context.put("jpaTable",jpaTable ); //$NON-NLS-1$
		try {
			String path=outputDir+"/"+StringUtil.strReplaceAll(jpaTable.getPackage(), ".", "/");
			createPackage(path);
			fileWriter=new FileWriter(path +"/"+className+".java");
			template.merge(context, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			throw new GenerationException("Impossible to generate output file from template file [" + templateFile + "]");
		}		
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
