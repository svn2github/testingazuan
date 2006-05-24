/*
 * Created on 3-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.engines.weka;

import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;

/** 
 * @author Gioia
 */
public class WekaRunner {
	
	private WekaKFRunner kfRunner = null;	
	
	private static transient Logger logger = Logger.getLogger(WekaRunner.class);
	
	/**
	 * Class Constructor
	 * 
	 * @param spagobibaseurl The basic url for SpagoBI
	 * @param templatePath The path for the report template
	 */
	public WekaRunner() {
		super();
		kfRunner = new WekaKFRunner();		
	}
	
		
	/**
	 * @param parameters
	 * @throws Exception
	 */
	public void runReport(Map parameters) throws Exception {
		String templatePath = (String) parameters.get("templatePath");
		String spagobibaseurl = (String) parameters.get("spagobiurl");
		byte[] template = new SpagoBIAccessUtils().getContent(spagobibaseurl, templatePath);
		
		File file = File.createTempFile("weka", null);
		OutputStream out = new FileOutputStream(file);
		out.write(template);
		out.flush();
		out.close();
		
		logger.debug("WekaRunner:runReport:Start parsing file: " + file);
		kfRunner.loadKFTemplate(file);
		kfRunner.setupSavers();
		kfRunner.setupLoaders();
		logger.debug("WekaRunner:runReport:Getting loaders & savers infos ...");
		logger.debug( Utils.getLoderDesc(kfRunner.getLoaders()) );
		logger.debug( Utils.getSaverDesc(kfRunner.getSavers()) );
		logger.debug("WekaRunner:runReport:Executing knowledge flow ...");
		kfRunner.run(false);
		
		
		file.delete();				
	}	
	
}
