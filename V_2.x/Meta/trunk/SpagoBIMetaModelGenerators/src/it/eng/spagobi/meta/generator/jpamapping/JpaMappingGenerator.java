/**
 * 
 */
package it.eng.spagobi.meta.generator.jpamapping;

import it.eng.spagobi.meta.generator.GenerationException;
import it.eng.spagobi.meta.generator.IGenerator;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaMappingGenerator implements IGenerator {
	
	private File templateDir;
	private String templateFilename;
	
	public File DEFAULT_TEMPLATE_DIR = new File("templates");
	public String DEFAULT_TEMPLATE_FILENAME = "table.vm";
	
	public JpaMappingGenerator() {
		setTemplateDir( DEFAULT_TEMPLATE_DIR );
		setTemplateFilename( DEFAULT_TEMPLATE_FILENAME );
	}
	
	public JpaMappingGenerator(File templateDir, String templateFilename) {
		setTemplateDir( templateDir );
		setTemplateFilename( templateFilename );
	}
	
	
	@Override
	public void generate(ModelObject o, File outputFile)  {
		BusinessModel model;
		
		if(o instanceof BusinessModel) {
			model = (BusinessModel)o;
			try {
				generateJpaMapping(model, outputFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new GenerationException("Impossible to create JPA Mapping from an object of type [" + o.getClass().getName() + "]");
		}
	}
	
	public void generateJpaMapping(BusinessModel model, File outputFile) {
		
		VelocityContext context;
		Template template;
		BusinessTable businessTable;
		StringWriter fileWriter;
		
		Velocity.setProperty("file.resource.loader.path", getTemplateDir().getAbsolutePath());
		try {
			template = Velocity.getTemplate( getTemplateFilename() );
		} catch (Throwable t) {
			throw new GenerationException("Impossible to load template file [" + getTemplateFilename() + "]");
		}
		
		for(int i = 0; i < 1; i++) {
			businessTable = model.getTables().get(i);
			
		    context = new VelocityContext();
		    context.put("physicalTable", businessTable.getPhysicalTable()); //$NON-NLS-1$
	        context.put("businessTable", businessTable); //$NON-NLS-1$
	        context.put("jpaTable", new JpaTable(businessTable)); //$NON-NLS-1$
	        
	        fileWriter = new StringWriter();
			try {
				template.merge(context, fileWriter);
			} catch (IOException e) {
				throw new GenerationException("Impossible to generate output file from template file [" + getTemplateFilename() + "]");
			}
			System.out.println( fileWriter.toString() );
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


	public String getTemplateFilename() {
		return templateFilename;
	}


	public void setTemplateFilename(String templateFilename) {
		this.templateFilename = templateFilename;
	}
}
