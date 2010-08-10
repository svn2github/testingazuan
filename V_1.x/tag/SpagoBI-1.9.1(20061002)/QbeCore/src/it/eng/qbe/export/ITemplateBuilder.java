/**
 * 
 */
package it.eng.qbe.export;

import java.io.File;
import java.io.IOException;

/**
 * Interface of class that can build a jasper template from a query string
 * 
 * @author Gioia
 */
public interface ITemplateBuilder {
	public String buildTemplate();
	public void buildTemplateToFile(File templateFile) throws IOException;
}
