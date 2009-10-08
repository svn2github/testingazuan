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
package it.eng.spagobi.engines.qbe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import it.eng.qbe.model.accessmodality.DataMartModelAccessModality;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.utilities.assertion.Assert;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeTemplate.
 * 
 * @author Andrea Gioia
 */
public class QbeTemplate {
	
	
	private SourceBean template;	
	private boolean composite;	
	private Map dblinkMap;	
	private List datamartNames;
	private Map functionalities;		
	private DataMartModelAccessModality datamartModelAccessModality;
	private String dialect;

	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(QbeTemplate.class);
	
	
	public QbeTemplate(SourceBean template) {
		Assert.assertNotNull(template, "Template parameter cannot be null");
		setTemplate(template);
		parse();
	}
	
	
	public static String TAG_ROOT_COMPOSITE = "COMPOSITE-QBE";
	public static String TAG_ROOT_NORMAL = "QBE";
	public static String TAG_DATAMART = "DATAMART";
	public static String PROP_DATAMART_NAME = "name";
	public static String PROP_DATAMART_DBLINK = "dblink";
	public static String TAG_MODALITY = "MODALITY";
	public static String TAG_MODALITY_TABLE = "TABLE";
	public static String TAG_FUNCTIONALITIES = "FUNCTIONALITIES";
	
	public void parse() {
		String templateName;
		SourceBean datamartSB;
		String dmName;
		SourceBean modalitySB;
		List modalities;
		SourceBean compositeModalitySB;
		SourceBean functionalitiesSB;
		
		try {
			templateName = template.getName();
			logger.debug("Parsing template [" + templateName + "] ...");
			Assert.assertNotNull(templateName, "Root tag cannot be not be null");
			
			
			if(!TAG_ROOT_COMPOSITE.equalsIgnoreCase(templateName)
					&& !TAG_ROOT_NORMAL.equalsIgnoreCase(templateName)){
				/*
				throw new QbeTemplateParseException("Malformed template structure: template root tag cannot be equals to [" + templateName +"]. " +
						"It must be equal to [" + TAG_ROOT_NORMAL + "] or [" + TAG_ROOT_COMPOSITE + "]");
				*/
				QbeTemplateParseException e = new QbeTemplateParseException("Malformed template structure");
				e.setDescription("template root tag cannot be equals to [" + templateName +"]. " +
						"It must be equal to [" + TAG_ROOT_NORMAL + "] or [" + TAG_ROOT_COMPOSITE + "]");
				e.addHint("Check document template in document details page");
				throw e;
			}
			
			setComposite( TAG_ROOT_COMPOSITE.equalsIgnoreCase(templateName) );
			
			datamartNames = new ArrayList();
			dblinkMap = new HashMap();
			modalities  = new ArrayList();
			dmName = null;
			
			if(isComposite()) {
				List qbeList;
				SourceBean qbeSB;
				String dblink;
				
				logger.debug("The QBE described in the template is of type COMPOSITE");
								
				qbeList = template.getAttributeAsList(TAG_ROOT_NORMAL);
				for(int i = 0; i < qbeList.size(); i++) {
					qbeSB = (SourceBean)qbeList.get(i);
					
					// DATAMART block
					if(qbeSB.containsAttribute(TAG_DATAMART)) {
						datamartSB = (SourceBean)qbeSB.getAttribute(TAG_DATAMART);	
						dmName = (String)datamartSB.getAttribute(PROP_DATAMART_NAME);
						Assert.assertTrue(!StringUtilities.isEmpty(dmName), "Attribute [" + PROP_DATAMART_NAME +"] in tag [" + TAG_DATAMART + "] must be properly defined");
						
						datamartNames.add(dmName);					
						dblink = (String)datamartSB.getAttribute(PROP_DATAMART_DBLINK);
						if(dblink != null) {
							dblinkMap.put(dmName, dblink);
						}
					} else {
						Assert.assertUnreachable("Missing compolsury tag [" + TAG_DATAMART + "]");
					}
					
					// MODALITY block
					if(qbeSB.containsAttribute(TAG_MODALITY)) {
						modalitySB = (SourceBean)qbeSB.getAttribute(TAG_MODALITY);
						modalities.add(modalitySB);		
					} else {
						logger.debug("Qbe template associated to datamart [" + dmName + "] does not contain tag [" + TAG_MODALITY +"] so it will be not profiled");
					}
				}			
			} else {
				logger.debug("The QBE described in the template is of type STANDARD");
				
				// DATAMART block
				if(template.containsAttribute(TAG_DATAMART)) {
					datamartSB = (SourceBean)template.getAttribute(TAG_DATAMART);
					dmName = (String)datamartSB.getAttribute(PROP_DATAMART_NAME);
					Assert.assertTrue(!StringUtilities.isEmpty(dmName), "Attribute [" + PROP_DATAMART_NAME +"] in tag [" + TAG_DATAMART + "] must be properly defined");
					
					datamartNames.add(dmName);
				} else {
					Assert.assertUnreachable("Missing compolsury tag [" + TAG_DATAMART + "]");
				}
				
				// MODALITY block
				if(template.containsAttribute(TAG_MODALITY)) {
					modalitySB = (SourceBean)template.getAttribute(TAG_MODALITY);
					modalities.add(modalitySB);
				} else {
					logger.debug("Qbe template does not contain tag [" + TAG_MODALITY +"] so it will be not profiled");
				}
				
			}
			
			compositeModalitySB = new SourceBean(TAG_MODALITY);
			
			for(int i = 0; i < modalities.size(); i++) {
				modalitySB = (SourceBean)modalities.get(i);
				List tables = modalitySB.getAttributeAsList(TAG_MODALITY_TABLE);
				for(int j = 0; j < tables.size(); j++) {
					SourceBean tableSB = (SourceBean)tables.get(j);
					compositeModalitySB.setAttribute(tableSB);
				}
			}
					
			if(compositeModalitySB != null && compositeModalitySB.getAttribute(TAG_MODALITY_TABLE) != null) { 
				datamartModelAccessModality = new DataMartModelAccessModality(compositeModalitySB);
			}
		
			functionalitiesSB = (SourceBean)template.getAttribute(TAG_FUNCTIONALITIES);
			parseFunctionalities(functionalitiesSB);
			
			logger.debug("Templete parsed succesfully");
		} catch(Throwable t) {
			throw new QbeTemplateParseException("Impossible to parse tempate [" + template.toString()+ "]", t);
		} finally {
			logger.debug("OUT");
		}		
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
