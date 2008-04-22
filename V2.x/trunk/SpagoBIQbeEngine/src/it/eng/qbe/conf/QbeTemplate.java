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
package it.eng.qbe.conf;

import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Andrea Gioia
 *
 */
public class QbeTemplate {
	private SourceBean template = null;
	private boolean composite = false;
	private Map dblinkMap = null;
	private List datamartNames = null;
	private Map functionalities = null;	
	private DataMartModelAccessModality datamartModelAccessModality = null;
	private String dialect = null;
	
	public QbeTemplate(SourceBean template) {
		setTemplate(template);
		parse();
	}
	
	public void parse() {
		setComposite( template.getName().equalsIgnoreCase("COMPOSITE-QBE") );
				
		dblinkMap = new HashMap();
		List modalities = new ArrayList();
		
		if(isComposite()) {
			List dmNames = new ArrayList();
			List qbeList = template.getAttributeAsList("QBE");
			for(int i = 0; i < qbeList.size(); i++) {
				SourceBean qbeSB = (SourceBean)qbeList.get(i);
									
				SourceBean datamartSB = (SourceBean)qbeSB.getAttribute("DATAMART");
				String dmName = (String)datamartSB.getAttribute("name");
				dmNames.add(dmName);
				String dblink = (String)datamartSB.getAttribute("dblink");
				if(dblink != null) dblinkMap.put(dmName, dblink);
				
				SourceBean modalitySB = (SourceBean)qbeSB.getAttribute("MODALITY");
				modalities.add(modalitySB);					
			}			
			datamartNames = dmNames;
		} else {
			SourceBean datamartSB = (SourceBean)template.getAttribute("DATAMART");
			String dmName = (String)datamartSB.getAttribute("name");
			datamartNames = new ArrayList();
			datamartNames.add(dmName);
			SourceBean modalitySB = (SourceBean)template.getAttribute("MODALITY");
			modalities.add(modalitySB);
		}
		
		SourceBean datasourceSB = (SourceBean)template.getAttribute("DATASOURCE");
		dialect = (String)datasourceSB.getAttribute("dialect");
		
		SourceBean compositeModalitySB = null;
		try {
			compositeModalitySB = new SourceBean("MODALITY");
			if(modalities != null) {
				for(int i = 0; i < modalities.size(); i++) {
					SourceBean modalitySB = (SourceBean)modalities.get(i);
					if(modalitySB != null) {				
						List tables = modalitySB.getAttributeAsList("TABLE");
						for(int j = 0; j < tables.size(); j++) {
							SourceBean tableSB = (SourceBean)tables.get(j);
							compositeModalitySB.setAttribute(tableSB);
						}
					}
					
				}
			}
		} catch (SourceBeanException e) {			
			e.printStackTrace();
		}
		if(compositeModalitySB != null && compositeModalitySB.getAttribute("TABLE") != null) { 
			datamartModelAccessModality = new DataMartModelAccessModality(compositeModalitySB);
		}
		
		
		
		SourceBean functionalitiesSB = (SourceBean)template.getAttribute("FUNCTIONALITIES");
		parseFunctionalities(functionalitiesSB);
	}
	
	private void parseFunctionalities(SourceBean functionalitiesSB) {
		functionalities = new HashMap();
		if(functionalitiesSB == null) {
			return;
		}
		
		List list = functionalitiesSB.getAttributeAsList("FUNCTIONALITY");
		if(list == null) {
			return;
		}
		for(int i = 0; i < list.size(); i++) {
			SourceBean functionalitySB = (SourceBean)list.get(i);
			String functionalityName = (String)functionalitySB.getAttribute("name");
			Properties props = new Properties();
			List parameters = functionalitySB.getAttributeAsList("PARAMETER");
			for(int j = 0; j < parameters.size(); j++) {
				SourceBean parameterSB = (SourceBean)parameters.get(j);
				String parameterName = (String)parameterSB.getAttribute("name");
				String parameterValue = (String)parameterSB.getAttribute("value");
				props.put(parameterName, parameterValue);
			}
			addFunctianalityProperties(functionalityName, props);
		}
	}
	
	private void addFunctianalityProperties(String functionalityName, Properties props) {
		Properties p = (Properties)functionalities.get(functionalityName);
		if(p == null) {
			p = new Properties();
		}
		p.putAll(props);
		functionalities.put(functionalityName, p);
	}

	private SourceBean getTemplate() {
		return template;
	}

	private void setTemplate(SourceBean template) {
		this.template = template;
	}

	private boolean isComposite() {
		return composite;
	}

	private void setComposite(boolean composite) {
		this.composite = composite;
	}

	public Map getFunctionalities() {
		return functionalities;
	}

	public Map getDblinkMap() {
		return dblinkMap;
	}

	public void setDblinkMap(Map dblinkMap) {
		this.dblinkMap = dblinkMap;
	}

	public void setFunctionalities(Map functionalities) {
		this.functionalities = functionalities;
	}

	public DataMartModelAccessModality getDatamartModelAccessModality() {
		return datamartModelAccessModality;
	}

	public void setDatamartModelAccessModality(
			DataMartModelAccessModality datamartModelAccessModality) {
		this.datamartModelAccessModality = datamartModelAccessModality;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public List getDatamartNames() {
		return datamartNames;
	}

	public void setDatamartNames(List datamartNames) {
		this.datamartNames = datamartNames;
	}
}
