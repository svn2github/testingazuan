/*
 * Created on 3-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.engines.weka;

import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextSupport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import weka.gui.beans.BeanConnection;
import weka.gui.beans.BeanInstance;
import weka.gui.beans.Loader;
import weka.gui.beans.xml.XMLBeans;

/**
 * Jasper Report implementation built to provide all methods to
 * run a report inside SpagoBI. It is the jasper report Engine implementation
 * for SpagoBI.
 * 
 * @author Gioia
 */
public class WekaRunner {
	
	private String templatePath = null;
	private String spagobibaseurl = null;
	private static transient Logger logger = Logger.getLogger(WekaRunner.class);
	
	/**
	 * Class Constructor
	 * 
	 * @param spagobibaseurl The basic url for SpagoBI
	 * @param templatePath The path for the report template
	 */
	public WekaRunner(String spagobibaseurl, String templatePath) {
		super();
		this.templatePath = templatePath;
		this.spagobibaseurl = spagobibaseurl;
	}
	
		
	/**
	 * 
	 * @param parameters
	 * @throws Exception
	 */
	public void runReport(Map parameters) throws Exception {

		byte[] template = new SpagoBIAccessUtils().getContent(this.spagobibaseurl, this.templatePath);
		
		File file = File.createTempFile("weka", null);
		OutputStream out = new FileOutputStream(file);
		out.write(template);
		out.flush();
		out.close();
		
		BeanContextSupport beanContextSupport = new BeanContextSupport();
		Vector beans       = new Vector();
		Vector connections = new Vector();
		Vector loaders = new Vector();
		
				
		XMLBeans xml = new XMLBeans(null, beanContextSupport); 
		Vector v     = (Vector) xml.read(file);
		beans        = (Vector) v.get(XMLBeans.INDEX_BEANINSTANCES);
		connections  = (Vector) v.get(XMLBeans.INDEX_BEANCONNECTIONS);	
		
		//log("File parsed succesfully");
		//log("Starting knowledge flow preprocess phase ...");
		
		beanContextSupport = new BeanContextSupport(); // why?
		beanContextSupport.setDesignTime(true);
		
		
		for(int i = 0; i < beans.size(); i++) {
			BeanInstance bean = (BeanInstance)beans.get(i);
			//log("   " + (i+1) + ". " + bean.getBean().getClass().getName());
		
			// register loaders
			if (bean.getBean() instanceof Loader) {
				//log("    - Loader [" + ((Loader)bean.getBean()).getLoader().getClass() + "]");
								
				loaders.add(bean.getBean());
			}		
			
			if (bean.getBean() instanceof BeanContextChild) {
				//log("    - BeanContextChild" );
				beanContextSupport.add(bean.getBean());
			}			
		}
		
		for(int i = 0; i < connections.size(); i++) {
			BeanConnection connection = (BeanConnection)connections.get(i);
			// log("   " + (i+1) + ". " + connection.getClass().getName());
		}	
		
		BeanInstance.setBeanInstances(beans, null);
		BeanConnection.setConnections(connections);
		
		// log("Knowledge flow preprocess phase finished succesfully");
		// log("Executing knowledge flow ...");
		
		for(int i = 0; i < loaders.size(); i++) {
			Loader loader = (Loader)loaders.get(i);
			loader.startLoading();
		}
		
		file.delete();
		// log("Knowledge flow executed successfully (at least i hope so ;-))");		
	}	
	
}
