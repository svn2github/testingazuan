/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.qbe.commons.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class LabelsConverter {
	
	private static void log(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//Properties labelsIn;
		//Properties labelsOut;
		File inputFile;
		BufferedReader reader;
		Writer writer;
		File outputFile;
		String fieldUniqueNameIn;
		String fieldUniqueNameOut;
		String label;
		
		log("Input file [" + args[0] +"]");
		log("Output file [" + args[1] +"]");
		
		inputFile = new File(args[0]);
		outputFile = new File(args[1]);
		
		//labelsIn = new Properties();
		//labelsOut = new Properties();
		
		reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			log("Inpit file [" + inputFile.getName() + "] not found");
		}	
		
		writer = null;
		try {
			writer = new FileWriter(outputFile);
			
			String line = null;
			while ((line = reader.readLine()) != null)   {
				line = line.trim();
				if(line.equals("") || line.startsWith("#")) {
					writer.write(line + "\n");
				} else {
					String[] c = line.split("=");
					
					fieldUniqueNameIn = c[0].trim();
					if(c.length == 2) {
						label = c[1].trim();
					} else {
						label = "";
						log("WARN: property [" + fieldUniqueNameIn + "] not set at line [" + line + "]");
					}
					fieldUniqueNameOut = convert(fieldUniqueNameIn); 
					line = fieldUniqueNameOut + " = " + label;
					writer.write(line + "\n");
				}			
			}
		} catch (IOException e) {
			log("An error occurred while converting file [" + inputFile.getName() + "]");
			e.printStackTrace();
		} finally {
			if(writer != null) {
			writer.flush();
			writer.close();
			}
			if(reader != null) {
				reader.close();
			}
			
		}
	
		
	}

	private static String convert(String fieldUniqueName) {
		String result;
		String[] chunks;
		
		log( "Field unique name to convert [" + fieldUniqueName + "]" );
		
		
		
		String key = fieldUniqueName;
		String property = null;
		if(key.endsWith(".visible")) {
			key = key.substring(0, key.length()-8);
			property = "visible";
		} else if(key.endsWith(".type")) {
			key = key.substring(0, key.length()-5);
			property = "type";
		} else if(key.endsWith(".position")) {
			key = key.substring(0, key.length()-9);
			property = "position";
		}
		
		chunks = key.split("/");
		
		if(chunks.length == 3 && chunks[1].trim().equals("") && chunks[0].trim().endsWith(chunks[2].trim())) {
			log("Skip first level entity [" + fieldUniqueName + "]");
			result = key;
		} else  {
			result = "";
			for(int i = chunks.length-1; i > 0 ; i--) {
				if(!chunks[i].trim().equals("")) {
					/*
					if(chunks[i].indexOf("(") > 0 ) {
						chunks[i] = chunks[i].substring(0, chunks[i].indexOf("("));
					}
					*/				
					chunks[i] = chunks[i].substring(0, 1).toLowerCase() + chunks[i].substring(1);
				}
				result = result.trim().equals("")? chunks[i]: chunks[i] + "/" + result;
			}
			
			result = chunks[0] + "/" + result;
			
			//log( "Converted field unique name [" + result + "]" );
		}
		
		result = property != null? result + "." + property: result;
		return result;
	}

}
