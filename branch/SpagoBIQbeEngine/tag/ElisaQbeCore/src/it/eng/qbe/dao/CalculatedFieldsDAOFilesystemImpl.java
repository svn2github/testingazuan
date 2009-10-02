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
package it.eng.qbe.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import it.eng.qbe.model.structure.DataMartCalculatedField;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.utilities.assertion.Assert;


/**
 * @author Andrea Gioia
 */
public class CalculatedFieldsDAOFilesystemImpl implements ICalculatedFieldsDAO {
	
	private File datamartsDir;

	public static final String CFIELDS_FILE_NAME = "cfields.xml";
	
	public static transient Logger logger = Logger.getLogger(CalculatedFieldsDAOFilesystemImpl.class);
	
	public CalculatedFieldsDAOFilesystemImpl(File datamartsDir) {
		setDatamartsDir(datamartsDir);
	}
	
	
	public Map loadCalculatedFields(String datamartName) {
		Map calculatedFiledsMap;
		File calculatedFieldsFile;
		FileInputStream is;
		SAXReader reader;
		Document document;
		
		Assert.assertTrue(!StringUtilities.isEmpty(datamartName), "Input parameter [datamartName] cannot be null or empty");
		
		calculatedFiledsMap = new HashMap();
		
		calculatedFieldsFile = getCalculatedFieldsFile(datamartName);
		if(calculatedFieldsFile != null && calculatedFieldsFile.exists()) {
			is = null;
			try {
				is = new FileInputStream(calculatedFieldsFile);
				
				reader = new SAXReader();
				document = reader.read(is);
				
				List calculatedFieldNodes = document.selectNodes("//CFIELDS/CFIELD");
				Iterator it = calculatedFieldNodes.iterator();
				Node calculatedFieldNode = null;
				while (it.hasNext()) {
					calculatedFieldNode = (Node) it.next();
					String entity = calculatedFieldNode.valueOf("@entity");
					String name = calculatedFieldNode.valueOf("@name");
					String type = calculatedFieldNode.valueOf("@type");
					String expression = calculatedFieldNode.getStringValue();
					DataMartCalculatedField calculatedField = new DataMartCalculatedField(name, type, expression);
					
					List calculatedFileds;
					if(!calculatedFiledsMap.containsKey(entity)) {
						calculatedFiledsMap.put(entity, new ArrayList());
					}
					calculatedFileds = (List)calculatedFiledsMap.get(entity);					
					calculatedFileds.add(calculatedField);
				}
				
			} catch (FileNotFoundException e) {
				logger.error("Impossible to load calculated fields from file [" + calculatedFieldsFile.getName() + "]", e);
			} catch (DocumentException e) {
				logger.error("Impossible to parse calculated fields file [" + calculatedFieldsFile.getName() + "]", e);
			} catch (Throwable t) {
				logger.error("An unpredictable error occurred while loading calculated fields from file [" + calculatedFieldsFile.getName() + "]", t);
			} 					
		} else {
			calculatedFiledsMap = new HashMap();
		}
		
		return calculatedFiledsMap;
	}

	public List saveCalculatedFields(String datamartName) {
		return null;
	}
	
	
	public File getCalculatedFieldsFile(String datamartName) {
		File calculatedFieldsFile = null;
		File targetDatamartDir = null;
		
		targetDatamartDir = new File(getDatamartsDir(), datamartName);
		calculatedFieldsFile = new File(targetDatamartDir, CFIELDS_FILE_NAME);
		
		return calculatedFieldsFile;
	}
	
	public File getDatamartsDir() {
		return datamartsDir;
	}

	public void setDatamartsDir(File datamartsDir) {
		this.datamartsDir = datamartsDir;
	}
	
}
