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
package it.eng.qbe.utility;

import it.eng.qbe.model.DataMartModel;
import it.eng.spago.base.ApplicationContainer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * @author Andrea Gioia
 *
 */
public class QbeProperties {
	
	public static final int CLASS_TYPE_TABLE = 1;
	public static final int CLASS_TYPE_RELATION = 2;
	public static final int CLASS_TYPE_VIEW = 3;
	
	public static final int FIELD_TYPE_MEASURE = 1;
	public static final int FIELD_TYPE_DIMENSION = 2;
	
	private Properties qbeProperties = null;
	
	
	public QbeProperties(DataMartModel dm) {
		qbeProperties = getQbeProperties(dm);
	}
	
	public boolean isTableVisible(String className) {
		if(qbeProperties == null) return true;
		
		String visiblePropertyValue = qbeProperties.getProperty(className + ".visible");
		if(visiblePropertyValue == null || visiblePropertyValue.trim().equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFieldVisible(String fieldName) {
		if(qbeProperties == null) return true;
		
		String visiblePropertyValue = qbeProperties.getProperty(fieldName + ".visible");
		if(visiblePropertyValue == null || visiblePropertyValue.trim().equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getTableType(String fieldName) {
		if(qbeProperties == null) return CLASS_TYPE_TABLE;
		String type = qbeProperties.getProperty(fieldName + ".type");
		if(type == null || type.trim().equalsIgnoreCase("table")) {
			return CLASS_TYPE_TABLE;
		} else if(type.trim().equalsIgnoreCase("view")) {
			return CLASS_TYPE_VIEW;
		} else {
			return CLASS_TYPE_RELATION;
		}
	}
	
	
	
	public int getFieldType(String className) {
		if(qbeProperties == null) return FIELD_TYPE_DIMENSION;
		String type = qbeProperties.getProperty(className + ".type");
		if(type == null || type.trim().equalsIgnoreCase("dimension")) {
			return FIELD_TYPE_DIMENSION;
		} else {
			return FIELD_TYPE_MEASURE;
		}
	}

	public static Properties getQbeProperties(DataMartModel dm) {
		
		Properties qbeProperties = null;
		
		File dmJarFile = dm.getJarFile();
		JarFile jf = null;
		try {
			jf = new JarFile(dmJarFile);
			qbeProperties = getQbeProperties(jf);
			
			List views = Utils.getViewJarFiles(dm.getDataSource());
			Iterator it = views.iterator();
			while(it.hasNext()) {
				File viewJarFile = (File)it.next();
				jf = new JarFile(viewJarFile);
				Properties tmpProps = getQbeProperties(jf);
				qbeProperties.putAll(tmpProps);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		return qbeProperties;	
	}
	
	public static Properties getQbeProperties(JarFile jf){
		Properties prop = new Properties();
		
		try{
			ZipEntry ze = jf.getEntry("qbe.properties");
			if (ze != null){
				prop = new Properties();
				prop.load(jf.getInputStream(ze));
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		return prop;
	}
}
