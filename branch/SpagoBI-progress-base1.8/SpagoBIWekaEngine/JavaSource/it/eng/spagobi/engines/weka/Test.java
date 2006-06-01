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
package it.eng.spagobi.engines.weka;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gioia
 *
 */
public class Test {
	
	static private String pathStr = "C:\\Documents and Settings\\gioia\\Documenti\\Codice\\Java\\SpagoBIBranch\\SpagoBIWekaEngine\\JavaSource";
	static private File path = new File(pathStr);
	
	static private void log(String msg) {
		System.out.println(msg);
	}
	
	static private File getTemplateFile(String[] args) {
		File inputFile = null;
		
		
		if(args.length > 1)
			inputFile = new File(args[1]);
		
		if(inputFile == null || !inputFile.exists()) {
			log("No input file!");
			log("Default file will be used just for test pourpose.");
			inputFile = new File(path, "clusterer_flow_filtered_params.kfml");
			//inputFile = new File(path b, "simple_flow.kfml");
			//inputFile = new File(path, "complex_flow.kfml");
			
		}
		
		return (inputFile);
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
				
		WekaKFRunner runner = new WekaKFRunner();
		
		File inputFile = getTemplateFile(args);
		File filledInputFile = new File(path, "out.xml");
		Map params = new HashMap();
		params.put("clusterNum", "15");
		params.put("clusterer", "weka.clusterers.SimpleKMeans");
		ParametersFiller.fill(inputFile, filledInputFile, params);
		log("Starting parsing file: " + inputFile);
		runner.loadKFTemplate(filledInputFile);
		runner.setupSavers();
		runner.setupLoaders();
		log("\nGetting loaders & savers infos ...\n");
		log( Utils.getLoderDesc(runner.getLoaders()) );
		log( Utils.getSaverDesc(runner.getSavers()) );
		log("Executing knowledge flow ...");
		runner.run(false, true);
		log("Knowledge flow executed successfully (at least i hope so ;-))");
	}

}
