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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * @author Gioia
 *
 */
public class ParametersFiller {
	
	
	public static void fill(File template, File filledTemplate, Map params) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader(template));
		Writer writer = new FileWriter(filledTemplate);
		String line = null;
		while((line = reader.readLine()) != null) {
			int index = -1;
			while( (index = line.indexOf("$P{")) != -1) {
				String pname = line.substring(index + 3, line.indexOf("}"));
				//System.out.println(pname);
				line = line.substring(0, index) + params.get(pname) +
					   line.substring(line.indexOf("}") + 1 , line.length());
			}
			writer.write(line + "\n");
		}
		writer.flush();
		writer.close();
		reader.close();
	}
	
	public static void fill(Reader reader, Writer writer, Map params) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( reader );
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			int index = -1;
			while( (index = line.indexOf("$P{")) != -1) {
				String pname = line.substring(index + 3, line.indexOf("}"));
				System.out.println(pname + ": " +  params.get(pname));
				line = line.substring(0, index) + params.get(pname) +
					   line.substring(line.indexOf("}") + 1 , line.length());
			}
			writer.write(line + "\n");
		}
		writer.flush();
		writer.close();
		reader.close();
	}
	
}
