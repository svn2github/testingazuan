/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.geo.application;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.configuration.FileCreatorConfiguration;
import it.eng.spagobi.engines.geo.configuration.ConfigurationException;
import it.eng.spagobi.engines.geo.configuration.Constants;
import it.eng.spagobi.engines.geo.configuration.MapConfiguration;
import it.eng.spagobi.engines.geo.datamart.provider.DatamartProviderFactory;
import it.eng.spagobi.engines.geo.datamart.provider.IDatamartProvider;
import it.eng.spagobi.engines.geo.map.provider.IMapProvider;
import it.eng.spagobi.engines.geo.map.provider.MapProviderFactory;
import it.eng.spagobi.engines.geo.map.renderer.IMapRenderer;
import it.eng.spagobi.engines.geo.map.renderer.MapRendererFactory;
import it.eng.spagobi.engines.geo.map.utils.SVGMapConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class GeoEngineCLI {
	
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(GeoEngineCLI.class);
	
	public static void main(String[] args) {
		File templateFile = null;
        File outputMapFileDir = null;
        String outputMapFileName = null;
        String outputFormat = null;
        File outputMapFile = null;
        
        MapConfiguration mapConfiguration = null;		
		File maptmpfile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		long star = System.currentTimeMillis();
		
		try {			
			logger.info("Execution started");
			logger.debug("Invocation command arguments: " + Arrays.toString(args) );
			
			logger.debug("Initialization of spago configuration started");
			initSpagoConfiguration();
			logger.debug("Spago configuration initialization ended successfully");
			
			logger.debug("Command line parsing started");
			Options options = getCommandLineOptions();
			CommandLine commandLine = parseCommandLine(options, args);
			logger.debug("Command line parsed succesfully");
			
			if( commandLine.hasOption( "help" ) ) {
			  	HelpFormatter formatter = new HelpFormatter();
			   	formatter.printHelp( "mapRenderer", options );
			   	System.exit(0);
			}
			
			templateFile = getTemplateFile(commandLine, options);
			outputMapFileDir = getOutputDir(commandLine);
			outputMapFileName = getOutputFileName(commandLine);
			outputFormat = getOutputFormat(commandLine);	
			outputMapFile = getOutputMapFile(outputMapFileDir, outputMapFileName, outputFormat);
			logger.debug("Output map file: " + outputMapFile);
			
			StringBuffer templateContent =  getFileContent(templateFile);
			
			logger.debug("Parsing of template file started");
			mapConfiguration = getMapConfiguration( templateContent );
			logger.debug("Parsing of template file ended successfully");			
			setContextPath(mapConfiguration);      	
			
			logger.debug("Rendering of output map started");
			maptmpfile = renderMap(mapConfiguration, outputFormat);
			logger.debug("Rendering of output map ended successfully");			
			
			inputStream =  getFileInputStream(maptmpfile);
			outputStream = getFileOutputStream(outputMapFile);
			
			try {
				if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
					SVGMapConverter.SVGToJPEGTransform(inputStream, outputStream);
				} else if(outputFormat.equalsIgnoreCase(Constants.XDSVG)
						  || outputFormat.equalsIgnoreCase(Constants.SVG)) {
					copyStream(inputStream, outputStream);
				} else {
					throw new Exception("Exportation fomat not supported: " + outputFormat);
				}
			} catch(Exception e) {
				logger.fatal("Error while exporting map to " + outputFormat + " format", e);
				e.printStackTrace();
				System.exit(1);	
			} 
		} catch (Exception e) {
			logger.fatal("Unexpected error", e);
			e.printStackTrace();
			System.exit(1);	
		} finally {
			try{
				if(inputStream != null) {
					inputStream.close();
				}
				if(outputStream != null){
					outputStream.flush();
					outputStream.close();
				}
				if(maptmpfile != null && maptmpfile.exists()) {
					boolean isDeleted = maptmpfile.delete();
					if(isDeleted == false) {
						logger.error("Impossible to delete temporary svg-map file: " + maptmpfile);
					} else {
						logger.error("Temporary svg-map file deleted successfully: " + maptmpfile);
					}
				}
			} catch (Exception e ){
				logger.fatal("Error while closing input and output streams", e);
				e.printStackTrace();
				System.exit(1);				
			}
		}
		
	    logger.info("Output map file genearated successfully: " + outputMapFile);
	    long elapsed = System.currentTimeMillis() - star;
	    logger.info("Execution terminated successfully in " + elapsed + " ms");
	}
	
	
	
	
	
	
	private static void initSpagoConfiguration() {
		logger.debug("Root path directory: " + System.getProperty("user.dir"));
		ConfigSingleton.setConfigurationCreation( new FileCreatorConfiguration( System.getProperty("user.dir") + "/conf") );
		ConfigSingleton.setRootPath(System.getProperty("user.dir") + "/conf");
		ConfigSingleton.setConfigFileName("/empty.xml");
	}
	
	private static Options getCommandLineOptions() {
		Options options = null;
		
		options = new Options();
		options.addOption( "t", "template", true,"template file that contains all configuration settings" );
		options.addOption( "d", "output-dir", true, "output map dir" );
		options.addOption( "o", "output-file", true, "output map file (widthout extension)" );
		options.addOption( "f", "output-format", true, "output map format (JPEG|SVG|XDSVG)");
		options.addOption( "h", "help", false, "print usage message");
		
		return options;
	}
	
	private static CommandLine parseCommandLine(Options options, String[] args) {
		CommandLine commandLine = null;
		CommandLineParser parser = null;
		
		parser = new PosixParser();
		try {
			commandLine = parser.parse( options, args );
		} catch (ParseException e) {
			logger.fatal("An error occurred while parsing command line options", e);
			e.printStackTrace();
			System.exit(1);
		}
		
		return commandLine;
	}
	
	private static File getTemplateFile(CommandLine commandLine, Options options) {
		String templateFileName = null;
		if( commandLine.hasOption( "template" ) ) {		        
		     templateFileName = commandLine.getOptionValue( "template" );
		     logger.debug("Template file name: " + templateFileName);
		} else {
		  	 logger.fatal("Template file name not defined");
		  	 HelpFormatter formatter = new HelpFormatter();
		     formatter.printHelp( "ERROR: Template file name not defined !!!!\nmapRenderer", options );
		     System.exit(1);
		}
		
		return new File( templateFileName );
	}
	
	private static File getOutputDir(CommandLine commandLine) {
		String outputMapFileDir = null;
		if( commandLine.hasOption( "output-dir" ) ) {		        
		  	outputMapFileDir = commandLine.getOptionValue( "output-dir" );
		    logger.debug("Output dir: " + outputMapFileDir);
		} else {
		   	logger.debug("Output dir not defined. Current working dir will be used as output dir");
		}
		return new File(outputMapFileDir);
	}
	
	private static String getOutputFileName(CommandLine commandLine) {
		String outputMapFileName = null;
		if( commandLine.hasOption( "output-file" ) ) {		        
		   	outputMapFileName = commandLine.getOptionValue( "output-file" );
		    logger.debug("Output fileName: " + outputMapFileName);
		} else {
		   	logger.debug("Output file name not defined. 'out' will be used as output file name");
		   	outputMapFileName = "out";
		}
		return outputMapFileName;
	}
	
	private static String getOutputFormat(CommandLine commandLine) {
		String outputFormat = null;
		if( commandLine.hasOption( "output-format" ) ) {		        
		   	outputFormat = commandLine.getOptionValue( "output-format" );
		    logger.debug("Output format: " + outputFormat);
		} else {
		  	logger.debug("Output format not defined. 'JPEG' will be used as output format");
		   	outputFormat = "JPEG";
		}
		return outputFormat;
	}
	
	private static StringBuffer getFileContent(File file) {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader;
		try {
			reader = new BufferedReader( new FileReader(file) );
			String line = null;
		    while( (line = reader.readLine()) != null ) {
		       	buffer.append(line + "\n");
		    }
		} catch (FileNotFoundException e) {
			logger.fatal("Template file not found: " + file.getAbsoluteFile(), e);
			System.exit(1);
			e.printStackTrace();
		} catch (IOException e) {
			logger.fatal("Impossible to read template file: " + file.getAbsoluteFile(), e);
			System.exit(1);
			e.printStackTrace();
		}
		
		return buffer;
	}
	
	private static MapConfiguration getMapConfiguration(StringBuffer buffer) {
		MapConfiguration mapConfiguration = null;
		
		try {
			mapConfiguration = new MapConfiguration(buffer.toString(), null, getStandardHierarchy());
		} catch (ConfigurationException e) {
			logger.fatal("An error occurred while parsing template file", e);
			System.exit(1);
			e.printStackTrace();
		}
		
		return mapConfiguration;		      
	}
	
	private static String getStandardHierarchy() {
		return  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 		"<HIERARCHY name=\"default\" table_name=\"gerarchia_geo\">\n" +
   		"  <LEVEL name=\"unita_urbanistiche\" column_id=\"cod_uu\"  column_desc=\"desc_uu\" feature_name=\"unita_urbanistiche\"/>\n" +
   		"<LEVEL name=\"vecchie_circos\" column_id=\"cod_vecchiacirc\"  column_desc=\"desc_vecchiacirc\" feature_name=\"vecchie_circos\"/>\n" +
   	    "<LEVEL name=\"circoscrizioni\" column_id=\"cod_circoscrizione\"  column_desc=\"desc_circoscrizione\" feature_name=\"circoscrizioni\"/>\n" +
   		"</HIERARCHY>";
	}
	
	private static void setContextPath(MapConfiguration mapConfiguration) {
		File rootDir = null;
		URL rootDirUrl = null;
		
		rootDir = new File(ConfigSingleton.getRootPath());
		try {
			rootDirUrl  = rootDir.toURI().toURL();
		} catch (MalformedURLException e) {
			logger.fatal("Impossible to get the URL of application root directory: " + rootDir.getAbsoluteFile(), e);
			System.exit(1);
			e.printStackTrace();
		}
		mapConfiguration.getMapRendererConfiguration().setContextPath( rootDirUrl.toString() );	        
	}
	
	private static IMapRenderer buildMapRenderer(MapConfiguration mapConfiguration) {
		IMapRenderer mapRenderer = null;
		try {
			mapRenderer = MapRendererFactory.getMapRenderer(mapConfiguration.getMapRendererConfiguration());
		} catch (Exception e) {
			logger.fatal("Impossible to instatiate map renderer of type: " + mapConfiguration.getMapRendererConfiguration().getClassName(), e);
			System.exit(1);
			e.printStackTrace();
		}
		
		return mapRenderer;
	}
	
	private static IMapProvider buildMapProvider(MapConfiguration mapConfiguration) {
		IMapProvider mapProvider = null;
		
		try {
			mapProvider = MapProviderFactory.getMapProvider(mapConfiguration.getMapProviderConfiguration());			
	    } catch (Exception e) {
			logger.fatal("Impossible to instatiate map provider of type: " + mapConfiguration.getMapProviderConfiguration().getClassName(), e);
			System.exit(1);
			e.printStackTrace();
		}
	    return mapProvider;
	}
	
	private static IDatamartProvider buildDatamartProvider(MapConfiguration mapConfiguration) {
		IDatamartProvider datamartProvider = null;
		try {
			datamartProvider = DatamartProviderFactory.getDatamartProvider(mapConfiguration.getDatamartProviderConfiguration());			
		} catch (Exception e) {
			logger.fatal("Impossible to instatiate datamart provider of type: " + mapConfiguration.getDatamartProviderConfiguration().getClassName(), e);
			System.exit(1);
			e.printStackTrace();
		}
		return datamartProvider;
	}
	
	private static File renderMap(MapConfiguration mapConfiguration, String outputFormat) {
		File maptmpfile = null;
		IMapRenderer mapRenderer = null;
		IMapProvider mapProvider = null;			
		IDatamartProvider datamartProvider = null;
		
		logger.debug("Creation of map renderer of type " + mapConfiguration.getMapRendererConfiguration().getClassName() + " started");
		mapRenderer = buildMapRenderer( mapConfiguration );
		logger.debug("Map renderer created succesfully");
		
		logger.debug("Creation of map provider of type " + mapConfiguration.getMapProviderConfiguration().getClassName() + " started");
		mapProvider = buildMapProvider( mapConfiguration );
		logger.debug("Map provider created succesfully");
		
		logger.debug("Creation of map provider of type " + mapConfiguration.getDatamartProviderConfiguration().getClassName() + " started");
		datamartProvider = buildDatamartProvider( mapConfiguration );
		logger.debug("Creation of datamart provider started");
		
		try {
			maptmpfile = mapRenderer.renderMap(mapProvider, datamartProvider, outputFormat);
		} catch (Exception e) {
			logger.fatal("Impossible to render map", e);
			System.exit(1);
			e.printStackTrace();
		}
		return maptmpfile;
	}
	
	private static File getOutputMapFile(File outputMapFileDir, String outputMapFileName, String outputFormat) {
		File outputMapFile = null;
		if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
			outputMapFile = new File(outputMapFileDir, outputMapFileName + ".jpeg");
		} else if(outputFormat.equalsIgnoreCase(Constants.XDSVG)
				|| outputFormat.equalsIgnoreCase(Constants.SVG)) {
			outputMapFile = new File(outputMapFileDir, outputMapFileName + ".svg");
		}
		return outputMapFile;
	}
	
	private static void copyStream(InputStream in, OutputStream out) throws IOException{
	  
	   byte[] buffer = new byte[100000];
	   
	   while (true) {
		   int amountRead = in.read(buffer);
	       if (amountRead == -1) {
	    	   break;
	       }
	       out.write(buffer, 0, amountRead); 
	   } 
	}
	
	private static FileInputStream getFileInputStream(File file) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			logger.fatal("Impossible to create file input stream", e);
			System.exit(1);
			e.printStackTrace();
		}
		return inputStream;
	}
	
	private static FileOutputStream getFileOutputStream(File file) {
		FileOutputStream outputStream = null;
		
		if(file.exists()) { 
			logger.warn("Output file already exists: " + file);
			boolean isDeleted = file.delete();
			if(isDeleted == false) {
				logger.warn("Impossible to delete the existing outputfile");
			} else {
				logger.debug("Existing outputfile deleted sucessfully");
			}
		}
		
		try {
			file.createNewFile();
			logger.debug("Empty output file created successfully");
		} catch (IOException e) {
			logger.debug("Impossible tro create a new empty output file: " + e.getMessage());
		}
		
		
		try {
			outputStream = new FileOutputStream(file);
			logger.debug("File output stream created successfully");
		} catch (FileNotFoundException e) {
			logger.fatal("Impossible to create file output stream", e);
			System.exit(1);
			e.printStackTrace();
		}
		
		return outputStream;
	}
}
