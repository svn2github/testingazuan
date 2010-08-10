/**
 * 
 */
package it.eng.qbe.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Gioia
 *
 */
public abstract class AbstractTemplateBuilder implements ITemplateBuilder {
	
	public void buildTemplateToFile(File templateFile) throws IOException {
		BufferedWriter writer = new BufferedWriter( new FileWriter(templateFile) );
		writer.write( buildTemplate() );
		writer.flush();
		writer.close();
	}

}
