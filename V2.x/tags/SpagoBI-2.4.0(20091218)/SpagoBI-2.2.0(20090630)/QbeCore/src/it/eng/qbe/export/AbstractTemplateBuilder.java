/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractTemplateBuilder.
 * 
 * @author Gioia
 */
public abstract class AbstractTemplateBuilder implements ITemplateBuilder {
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.export.ITemplateBuilder#buildTemplateToFile(java.io.File)
	 */
	public void buildTemplateToFile(File templateFile) throws IOException {
		BufferedWriter writer = new BufferedWriter( new FileWriter(templateFile) );
		writer.write( buildTemplate() );
		writer.flush();
		writer.close();
	}

}
