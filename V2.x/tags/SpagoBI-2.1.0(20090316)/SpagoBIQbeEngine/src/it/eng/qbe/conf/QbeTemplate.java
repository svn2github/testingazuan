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
package it.eng.qbe.conf;

import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeTemplate.
 * 
 * @author Andrea Gioia
 */
public class QbeTemplate {
	
	/** The template. */
	private SourceBean template = null;
	
	/** The composite. */
	private boolean composite = false;
	
	/** The dblink map. */
	private Map dblinkMap = null;
	
	/** The datamart names. */
	private List datamartNames = null;
	
	/** The functionalities. */
	private Map functionalities = null;	
	
	/** The datamart model access modality. */
	private DataMartModelAccessModality datamartModelAccessModality = null;
	
	/** The dialect. */
	private String dialect = null;
	
	/**
	 * Instantiates a new qbe template.
	 * 
	 * @param template the template
	 */
	public QbeTemplate(SourceBean template) {
		setTemplate(template);
		parse();
	}
	
	/**
	 * Parses the.
	 */
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
	
	/**
	 * Parses the functionalities.
	 * 
	 * @param functionalitiesSB the functionalities sb
	 */
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
	
	/**
	 * Adds the functianality properties.
	 * 
	 * @param functionalityName the functionality name
	 * @param props the props
	 */
	private void addFunctianalityProperties(String functionalityName, Properties props) {
		Properties p = (Properties)functionalities.get(functionalityName);
		if(p == null) {
			p = new Properties();
		}
		p.putAll(props);
		functionalities.put(functionalityName, p);
	}

	/**
	 * Gets the template.
	 * 
	 * @return the template
	 */
	private SourceBean getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 * 
	 * @param template the new template
	 */
	private void setTemplate(SourceBean template) {
		this.template = template;
	}

	/**
	 * Checks if is composite.
	 * 
	 * @return true, if is composite
	 */
	private boolean isComposite() {
		return composite;
	}

	/**
	 * Sets the composite.
	 * 
	 * @param composite the new composite
	 */
	private void setComposite(boolean composite) {
		this.composite = composite;
	}

	/**
	 * Gets the functionalities.
	 * 
	 * @return the functionalities
	 */
	public Map getFunctionalities() {
		return functionalities;
	}

	/**
	 * Gets the dblink map.
	 * 
	 * @return the dblink map
	 */
	public Map getDblinkMap() {
		return dblinkMap;
	}

	/**
	 * Sets the dblink map.
	 * 
	 * @param dblinkMap the new dblink map
	 */
	public void setDblinkMap(Map dblinkMap) {
		this.dblinkMap = dblinkMap;
	}

	/**
	 * Sets the functionalities.
	 * 
	 * @param functionalities the new functionalities
	 */
	public void setFunctionalities(Map functionalities) {
		this.functionalities = functionalities;
	}

	/**
	 * Gets the datamart model access modality.
	 * 
	 * @return the datamart model access modality
	 */
	public DataMartModelAccessModality getDatamartModelAccessModality() {
		return datamartModelAccessModality;
	}

	/**
	 * Sets the datamart model access modality.
	 * 
	 * @param datamartModelAccessModality the new datamart model access modality
	 */
	public void setDatamartModelAccessModality(
			DataMartModelAccessModality datamartModelAccessModality) {
		this.datamartModelAccessModality = datamartModelAccessModality;
	}

	/**
	 * Gets the dialect.
	 * 
	 * @return the dialect
	 */
	public String getDialect() {
		return dialect;
	}

	/**
	 * Sets the dialect.
	 * 
	 * @param dialect the new dialect
	 */
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	/**
	 * Gets the datamart names.
	 * 
	 * @return the datamart names
	 */
	public List getDatamartNames() {
		return datamartNames;
	}

	/**
	 * Sets the datamart names.
	 * 
	 * @param datamartNames the new datamart names
	 */
	public void setDatamartNames(List datamartNames) {
		this.datamartNames = datamartNames;
	}
}
