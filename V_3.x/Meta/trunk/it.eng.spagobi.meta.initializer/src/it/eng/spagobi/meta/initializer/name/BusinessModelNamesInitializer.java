/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.initializer.name;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.initializer.InitializationException;
import it.eng.spagobi.meta.initializer.SpagoBIMetaInitializerPlugin;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.ModelPropertyCategory;
import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.CalculatedBusinessColumn;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Andrea Gioia
 *
 */
public class BusinessModelNamesInitializer {

	
	static public ModelFactory FACTORY = ModelFactory.eINSTANCE;
	static public IResourceLocator RL = SpagoBIMetaInitializerPlugin.getInstance().getResourceLocator();
	
	private static Logger logger = LoggerFactory.getLogger(BusinessModelNamesInitializer.class);
	
	

	public void setUniqueName(ModelObject o) {

		if(o instanceof BusinessModel) {
			setModelUniqueName((BusinessModel)o);
		} else if(o instanceof BusinessTable) {
			setTableUniqueName((BusinessTable)o);
		} else if(o instanceof BusinessView) {
			setViewUniqueName((BusinessView)o);
		} else if(o instanceof SimpleBusinessColumn) {
			setColumnUniqueName((SimpleBusinessColumn)o);
		} else if(o instanceof CalculatedBusinessColumn) {
			setCalculatedColumnUniqueName((CalculatedBusinessColumn)o);
		} else if(o instanceof BusinessIdentifier) {
			setIdentifierUniqueName((BusinessIdentifier)o);
		} else if(o instanceof BusinessRelationship) {
			setRelationshipUniqueName((BusinessRelationship)o);
		} else {
			
		}
	}
	
	public void setName(ModelObject o) {

		if(o instanceof BusinessModel) {
			setModelName((BusinessModel)o);
		} else if(o instanceof BusinessTable) {
			setTableName((BusinessTable)o);
		} else if(o instanceof BusinessView) {
			setViewName((BusinessView)o);
		} else if(o instanceof SimpleBusinessColumn) {
			setColumnName((SimpleBusinessColumn)o);
		} else if(o instanceof CalculatedBusinessColumn) {
			setCalculatedColumnName((CalculatedBusinessColumn)o);
		} else if(o instanceof BusinessIdentifier) {
			setIdentifierName((BusinessIdentifier)o);
		} else if(o instanceof BusinessRelationship) {
			setRelationshipName((BusinessRelationship)o);
		} else {
			
		}
	}
	

	
	private void setModelName(BusinessModel o) {
        
		try {


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setModelUniqueName(BusinessModel o) {
        
		try {


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void setTableName(BusinessTable businessTable) {
		try {
			String physicalTableName = businessTable.getPhysicalTable().getName();
			String baseName = StringUtils.capitalize(physicalTableName.replace("_", " "));
			BusinessModel businessModel = businessTable.getPhysicalTable().getModel().getParentModel().getBusinessModels().get(0);
			String name = baseName;
			while(businessModel.getBusinessTableByUniqueName(name) != null) {
				name += "_copy";
			}
			businessTable.setName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setTableUniqueName(BusinessTable businessTable) {
		try {
			String physicalTableName = businessTable.getPhysicalTable().getName();
			String baseUniqueName = StringUtils.capitalize(physicalTableName.replace("_", " "));
			BusinessModel businessModel = businessTable.getPhysicalTable().getModel().getParentModel().getBusinessModels().get(0);
			
			int index = 1;
			String uniqueName = baseUniqueName;
			while(businessModel.getBusinessTableByUniqueName(uniqueName) != null) {
				uniqueName = baseUniqueName + index++;
			}
			businessTable.setUniqueName(uniqueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setViewName(BusinessView o) {
		
	}
	private void setViewUniqueName(BusinessView o) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	

	private void setColumnName(SimpleBusinessColumn businessColumn) {
		
	}
	private void setColumnUniqueName(SimpleBusinessColumn businessColumn) {
      
		try {
			String baseUniqueName;
			String physicalColumnName = businessColumn.getPhysicalColumn().getName();
			baseUniqueName = StringUtils.capitalize(physicalColumnName.replace("_", " "));
			baseUniqueName = baseUniqueName.trim().replace(" ", "_");
			int index = 1;
			String uniqueName = baseUniqueName;
			while(businessColumn.getTable().getSimpleBusinessColumnByUniqueName(uniqueName) != null) {
				uniqueName = baseUniqueName + index++;
			}
			businessColumn.setUniqueName(uniqueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void setCalculatedColumnName(CalculatedBusinessColumn o) {
		
	}
	private void setCalculatedColumnUniqueName(CalculatedBusinessColumn o) {

		try {
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}		
	
	private void setIdentifierName(BusinessIdentifier o) {
		
	}
	private void setIdentifierUniqueName(BusinessIdentifier o) {
		
	}
	
	private void setRelationshipName(BusinessRelationship o) {
		
	}
	private void setRelationshipUniqueName(BusinessRelationship o) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
