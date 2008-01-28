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
package it.eng.qbe.datasource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;

import it.eng.qbe.bo.DatamartLabels;
import it.eng.qbe.bo.DatamartProperties;
import it.eng.qbe.locale.LocaleUtils;
import it.eng.qbe.model.BasicStatement;
import it.eng.qbe.model.IStatement;
import it.eng.qbe.query.IQuery;

/**
 * @author Andrea Gioia
 *
 */
public class AbstractDataSource implements IDataSource {
	
	private String name;
	private int type;
	
	private DatamartProperties properties = null;	
	private DatamartLabels labels = null;
	
	private Map localizedLabelMap = new HashMap();
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	protected void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	
	// =========================================================================================================
	// Label & Qbe properties
	// =========================================================================================================
	
	public DatamartLabels getLabels() {
		return labels;
	}

	public void setLabels(DatamartLabels labels) {
		this.labels = labels;
	}
	
	public DatamartLabels getLabels(Locale locale) {
		DatamartLabels props = (DatamartLabels)localizedLabelMap.get(locale.getLanguage());
		return props;
	}
	
	public void putLabelProperties(Locale locale, DatamartLabels labelProperties) {
		localizedLabelMap.put(locale.getLanguage(), labelProperties);
	}

	public DatamartProperties getProperties() {
		return properties;
	}

	public void setProperties(DatamartProperties properties) {
		this.properties = properties;
	}
	
}
