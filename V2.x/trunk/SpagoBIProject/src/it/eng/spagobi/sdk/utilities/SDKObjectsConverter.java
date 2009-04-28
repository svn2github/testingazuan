/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.sdk.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.sdk.documents.bo.SDKConstraint;
import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;
import it.eng.spagobi.sdk.documents.bo.SDKFunctionality;

public class SDKObjectsConverter {

	static private Logger logger = Logger.getLogger(SDKObjectsConverter.class);
	
	public SDKDocument fromBIObjectToSDKDocument(BIObject obj) {
		logger.debug("IN");
		if (obj == null) {
			logger.warn("BIObject in input is null!!");
			return null;
		}
        SDKDocument aDoc = new SDKDocument();
        aDoc.setId(obj.getId());
        aDoc.setLabel(obj.getLabel());
        aDoc.setName(obj.getName());
        aDoc.setDescription(obj.getDescription());
        aDoc.setType(obj.getBiObjectTypeCode());
        aDoc.setState(obj.getStateCode());
        Engine engine = obj.getEngine();
        if (engine != null) {
            aDoc.setEngineId(engine.getId());
        }
        logger.debug("OUT");
        return aDoc;
	}
	
	public SDKDocumentParameter fromBIObjectParameterToSDKDocumentParameter(BIObjectParameter biParameter) {
		logger.debug("IN");
		if (biParameter == null) {
			logger.warn("BIObjectParameter in input is null!!");
			return null;
		}
		SDKDocumentParameter aDocParameter = new SDKDocumentParameter();
        aDocParameter.setId(biParameter.getId());
        aDocParameter.setLabel(biParameter.getLabel());
        aDocParameter.setUrlName(biParameter.getParameterUrlName());
        Parameter parameter = biParameter.getParameter();
        List checks = null;
        if (parameter != null) {
        	checks = parameter.getChecks();
        }
        List newConstraints = new ArrayList<SDKConstraint>();
        if (checks != null && !checks.isEmpty()) {
        	Iterator checksIt = checks.iterator();
        	while (checksIt.hasNext()) {
        		Check aCheck = (Check) checksIt.next();
        		SDKConstraint constraint = fromCheckToSDKConstraint(aCheck);
        		newConstraints.add(constraint);
        	}
        }
        it.eng.spagobi.sdk.documents.bo.SDKConstraint[] constraintsArray = new it.eng.spagobi.sdk.documents.bo.SDKConstraint[newConstraints.size()];
        constraintsArray = (it.eng.spagobi.sdk.documents.bo.SDKConstraint[]) newConstraints.toArray(constraintsArray);
        aDocParameter.setConstraints(constraintsArray);
		logger.debug("OUT");
		return aDocParameter;
	}
	
	public SDKConstraint fromCheckToSDKConstraint(Check aCheck) {
		logger.debug("IN");
		if (aCheck == null) {
			logger.warn("Check in input is null!!");
			return null;
		}
		SDKConstraint constraint = new SDKConstraint();
		constraint.setId(aCheck.getCheckId());
		constraint.setLabel(aCheck.getLabel());
		constraint.setName(aCheck.getName());
		constraint.setDescription(aCheck.getDescription());
		constraint.setType(aCheck.getValueTypeCd());
		constraint.setFirstValue(aCheck.getFirstValue());
		constraint.setSecondValue(aCheck.getSecondValue());
		logger.debug("OUT");
		return constraint;
	}
	
	public SDKFunctionality fromLowFunctionalityToSDKFunctionality(LowFunctionality lowFunctionality) {
		logger.debug("IN");
		if (lowFunctionality == null) {
			logger.warn("LowFunctionality in input is null!!");
			return null;
		}
		SDKFunctionality functionality = new SDKFunctionality();
		functionality.setId(lowFunctionality.getId());
		functionality.setName(lowFunctionality.getName());
		functionality.setCode(lowFunctionality.getCode());
		functionality.setDescription(lowFunctionality.getDescription());
		functionality.setParentId(lowFunctionality.getParentId());
		functionality.setPath(lowFunctionality.getPath());
		functionality.setProg(lowFunctionality.getProg());
        logger.debug("OUT");
        return functionality;
	}
	
}
