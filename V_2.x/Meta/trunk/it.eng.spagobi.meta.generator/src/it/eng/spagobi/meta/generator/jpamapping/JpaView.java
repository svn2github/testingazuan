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
package it.eng.spagobi.meta.generator.jpamapping;

import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessView;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaView {
	private BusinessView businessView;
	
	private static Logger logger = LoggerFactory.getLogger(JpaViewInnerTable.class);
	
	
	public JpaView(BusinessView businessView) {
		super();
		Assert.assertNotNull("Parameter [businessView] cannot be null", businessView);
		this.businessView = businessView;
	}


	protected BusinessModel getModel(){
		return businessView.getModel();
	}
	
	public BusinessView getBusinessView() {
		return businessView;
	}
	
	public String getPackage() {
		logger.debug("IN");
		String result=null;
		ModelProperty property =  getModel().getProperties().get(JpaProperties.MODEL_PACKAGE);
        //check if property is setted, else get default value
        if (property.getValue() != null){
        	result= property.getValue();
        }
        else {
        	result= property.getPropertyType().getDefaultValue();
        }
        logger.debug("OUT: "+result);  
        return result;
	}
	
	
}
