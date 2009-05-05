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

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.dao.IEngineDAO;
import it.eng.spagobi.sdk.datasets.bo.SDKDataSet;
import it.eng.spagobi.sdk.datasets.bo.SDKDataSetParameter;
import it.eng.spagobi.sdk.datasets.bo.SDKDataStoreFieldMetadata;
import it.eng.spagobi.sdk.datasets.bo.SDKDataStoreMetadata;
import it.eng.spagobi.sdk.documents.bo.SDKConstraint;
import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;
import it.eng.spagobi.sdk.documents.bo.SDKFunctionality;
import it.eng.spagobi.sdk.documents.bo.SDKTemplate;
import it.eng.spagobi.sdk.engines.bo.SDKEngine;
import it.eng.spagobi.services.dataset.bo.SpagoBiDataSet;
import it.eng.spagobi.tools.dataset.bo.DataSetParameterItem;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.JDBCDataSet;
import it.eng.spagobi.tools.dataset.bo.JavaClassDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WebServiceDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreMetaData;
import it.eng.spagobi.tools.dataset.common.datastore.FieldMetadata;
import it.eng.spagobi.tools.dataset.service.DetailDataSetModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axis.attachments.ManagedMemoryDataSource;
import org.apache.log4j.Logger;

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
	
	public BIObject fromSDKDocumentToBIObject(SDKDocument document) {
		logger.debug("IN");
		if (document == null) {
			logger.warn("SDKDocument in input is null!!");
			return null;
		}
		BIObject obj = null;
		try {
			obj = new BIObject();
			obj.setId(document.getId());
			obj.setLabel(document.getLabel());
			obj.setName(document.getName());
			obj.setDescription(document.getDescription());
			obj.setDataSourceId(document.getDataSourceId());
			obj.setDataSetId(document.getDataSetId());
			
			IDomainDAO domainDAO = DAOFactory.getDomainDAO();
			
			// sets biobject type domain
			Domain type = domainDAO.loadDomainByCodeAndValue("BIOBJ_TYPE", document.getType());
			obj.setBiObjectTypeCode(type.getValueCd());
			obj.setBiObjectTypeID(type.getValueId());
			
			// sets biobject state domain
			Domain state = domainDAO.loadDomainByCodeAndValue("STATE", document.getState());
			obj.setStateCode(state.getValueCd());
			obj.setStateID(state.getValueId());
			
			// gets engine
			Engine engine = null;
			IEngineDAO engineDAO = DAOFactory.getEngineDAO();
			if (document.getEngineId() != null) {
				// if engine id is not specified take the first engine for the biobject type
				List engines = engineDAO.loadAllEnginesForBIObjectType(document.getType());
				if (engines.size() == 0) {
					throw new Exception("No engines defined for document type = [" + document.getType() + "]");
				}
				engine = (Engine) engines.get(0);
			} else {
				engine = engineDAO.loadEngineByID(document.getEngineId());
			}
			obj.setEngine(engine);

		} catch (Exception e) {
			logger.error("Error while converting SDKDocument into BIObject.", e);
			logger.debug("Returning null.");
			return null;
		}
        logger.debug("OUT");
        return obj;
	}
	
	public SDKTemplate fromObjTemplateToSDKTemplate(ObjTemplate objTemplate) {
		logger.debug("IN");
		if (objTemplate == null) {
			logger.warn("ObjTemplate in input is null!!");
			return null;
		}
		SDKTemplate toReturn = null;
		try {
		    byte[] templateContent = objTemplate.getContent();
		    toReturn = new SDKTemplate();
		    toReturn.setFileName(objTemplate.getName());
		    MemoryOnlyDataSource mods = new MemoryOnlyDataSource(templateContent, null);
		    DataHandler dhSource = new DataHandler(mods);
		    toReturn.setContent(dhSource);
		} catch (Exception e) {
			logger.error("Error while converting ObjTemplate into SDKTemplate.", e);
			logger.debug("Returning null.");
			return null;
		}
	    logger.debug("OUT");
	    return toReturn;
	}
	
	public ObjTemplate fromSDKTemplateToObjTemplate(SDKTemplate sdkTemplate) {
		logger.debug("IN");
		if (sdkTemplate == null) {
			logger.warn("SDKTemplate in input is null!!");
			return null;
		}
        InputStream is = null;
        DataHandler dh = null;
        ObjTemplate toReturn = null;
        try {
        	toReturn = new ObjTemplate();
        	toReturn.setName(sdkTemplate.getFileName());
        	dh = sdkTemplate.getContent();
        	is = dh.getInputStream();
        	byte[] templateContent = SpagoBIUtilities.getByteArrayFromInputStream(is);
        	toReturn.setContent(templateContent);
        	toReturn.setDimension(Long.toString(templateContent.length/1000)+" KByte");
		} catch (Exception e) {
			logger.error("Error while converting SDKTemplate into ObjTemplate.", e);
			logger.debug("Returning null.");
			return null;
        } finally {
        	if (is != null) {
        		try {
					is.close();
				} catch (IOException e) {
					logger.error("Error closing input stream of attachment", e);
				}
        	}
        	if (dh != null) {
	        	logger.debug("Deleting attachment file ...");
	        	File attachment = new File(dh.getName());
	        	if (attachment.exists() && attachment.isFile()) {
	        		boolean attachmentFileDeleted = attachment.delete();
	        		if (attachmentFileDeleted) {
	        			logger.debug("Attachment file deleted");
	        		} else {
	        			logger.warn("Attachment file NOT deleted");
	        		}
	        	}
        	}
        }
	    logger.debug("OUT");
	    return toReturn;
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
	
	public SDKEngine fromEngineToSDKEngine(Engine engine) {
		logger.debug("IN");
		if (engine == null) {
			logger.warn("Engine in input is null!!");
			return null;
		}
		SDKEngine sdkEngine = null;
		try {
			sdkEngine = new SDKEngine();
			sdkEngine.setId(engine.getId());
			sdkEngine.setName(engine.getName());
			sdkEngine.setLabel(engine.getLabel());
			sdkEngine.setDescription(engine.getDescription());
			Domain engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
			sdkEngine.setEngineType(engineType.getValueCd());
			Domain documentType = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
			sdkEngine.setDocumentType(documentType.getValueCd());
			sdkEngine.setClassName(engine.getClassName());
			sdkEngine.setUrl(engine.getUrl());
			sdkEngine.setDriverClassName(engine.getDriverName());
		} catch (Exception e) {
			logger.error("Error while converting Engine into SDKEngine.", e);
			logger.debug("Returning null.");
			return null;
		} finally {
			logger.debug("OUT");
		}
        return sdkEngine;
	}
	
	public SDKDataSet fromSpagoBiDataSetToSDKDataSet(SpagoBiDataSet spagoBiDataSet) {
		logger.debug("IN");
		if (spagoBiDataSet == null) {
			logger.warn("SpagoBiDataSet in input is null!!");
			return null;
		}
		SDKDataSet toReturn = null;
		try {
			toReturn = new SDKDataSet();
			toReturn.setId(spagoBiDataSet.getDsId());
			toReturn.setLabel(spagoBiDataSet.getLabel());
			toReturn.setName(spagoBiDataSet.getName());
			toReturn.setDescription(spagoBiDataSet.getDescription());
			
			toReturn.setPivotColumnName(spagoBiDataSet.getPivotColumnName());
			toReturn.setPivotColumnValue(spagoBiDataSet.getPivotColumnValue());
			toReturn.setPivotRowName(spagoBiDataSet.getPivotRowName());
			toReturn.setNumberingRows(spagoBiDataSet.isNumRows());
			
			// file dataset
			toReturn.setFileName(spagoBiDataSet.getFileName());
			
			// jdbc dataset
			toReturn.setJdbcQuery(spagoBiDataSet.getQuery());
			if (spagoBiDataSet.getDataSource() != null) {
				toReturn.setJdbcDataSourceId(spagoBiDataSet.getDataSource().getId());
			}
			
			// web service dataset
			toReturn.setWebServiceAddress(spagoBiDataSet.getAdress());
			toReturn.setWebServiceOperation(spagoBiDataSet.getOperation());
			
			// script dataset
			toReturn.setScriptText(spagoBiDataSet.getScript());
			toReturn.setScriptLanguage(spagoBiDataSet.getLanguageScript());
			
			// java dataset
			toReturn.setJavaClassName(spagoBiDataSet.getJavaClassName());
			
			String type = null;
			
			if ( ScriptDataSet.DS_TYPE.equals( spagoBiDataSet.getType() ) ) {
				type = "SCRIPT";
			} else if (  JDBCDataSet.DS_TYPE.equals( spagoBiDataSet.getType() ) ) {
				type = "JDBC_QUERY";
			} else if ( JavaClassDataSet.DS_TYPE.equals( spagoBiDataSet.getType() ) ) {
				type = "JAVA_CLASS";
			} else if ( WebServiceDataSet.DS_TYPE.equals( spagoBiDataSet.getType() ) ) {
				type = "WEB_SERVICE";
			} else if ( FileDataSet.DS_TYPE.equals( spagoBiDataSet.getType() ) ) {
				type = "FILE";
			} else {
				logger.error("Dataset type [" + spagoBiDataSet.getType() + "] unknown.");
				type = "UNKNOWN";
			}
			
			toReturn.setType(type);
			
			List dataSetParameterItemList = DetailDataSetModule.getParametersToFill(spagoBiDataSet);
			SDKDataSetParameter[] parameters = null;
			if (dataSetParameterItemList != null) {
				parameters = this.fromDataSetParameterItemListToSDKDataSetParameterArray(dataSetParameterItemList);
			}
			
			toReturn.setParameters(parameters);
		
		} catch (Exception e) {
			logger.error("Error while converting SpagoBiDataSet into SDKDataSet.", e);
			logger.debug("Returning null.");
			return null;
		} finally {
			logger.debug("OUT");
		}
        return toReturn;
	}
	
	public SDKDataSetParameter[] fromDataSetParameterItemListToSDKDataSetParameterArray(List dataSetParameterItemList) {
		logger.debug("IN");
		if (dataSetParameterItemList == null) {
			logger.warn("DataSetParameterItem list in input is null!!");
			return null;
		}
		SDKDataSetParameter[] toReturn = new SDKDataSetParameter[dataSetParameterItemList.size()];
		for (int i = 0; i < dataSetParameterItemList.size(); i++) {
			DataSetParameterItem aDataSetParameterItem = (DataSetParameterItem) dataSetParameterItemList.get(i);
			SDKDataSetParameter aSDKDataSetParameter = this.fromDataSetParameterItemToSDKDataSetParameter(aDataSetParameterItem);
			toReturn[i] = aSDKDataSetParameter;
		}
        logger.debug("OUT");
        return toReturn;
	}
	
	public SDKDataSetParameter fromDataSetParameterItemToSDKDataSetParameter(
			DataSetParameterItem dataSetParameterItem) {
		logger.debug("IN");
		if (dataSetParameterItem == null) {
			logger.warn("DataSetParameterItem in input is null!!");
			return null;
		}
		SDKDataSetParameter toReturn = new SDKDataSetParameter();
		toReturn.setName(dataSetParameterItem.getName());
		toReturn.setType(dataSetParameterItem.getType());
        logger.debug("OUT");
        return toReturn;
	}
	
	public SDKDataStoreMetadata fromDataStoreMetadataToSDKDataStoreMetadata(DataStoreMetaData aDataStoreMetaData) {
		logger.debug("IN");
		if (aDataStoreMetaData == null) {
			logger.warn("DataStoreMetaData in input is null!!");
			return null;
		}
		SDKDataStoreMetadata toReturn = new SDKDataStoreMetadata();
		Map properties = aDataStoreMetaData.getProperties();
		toReturn.setProperties(new HashMap(properties));
		SDKDataStoreFieldMetadata[] fieldsMetadata = this.fromFieldMetadataListToSDKDataStoreFieldMetadataArray(aDataStoreMetaData.getFieldsMeta());
		toReturn.setFieldsMetadata(fieldsMetadata);
        logger.debug("OUT");
        return toReturn;
	}

	private SDKDataStoreFieldMetadata[] fromFieldMetadataListToSDKDataStoreFieldMetadataArray(
			List fieldsMeta) {
		logger.debug("IN");
		if (fieldsMeta == null) {
			logger.warn("List fieldsMeta in input is null!!");
			return null;
		}
		SDKDataStoreFieldMetadata[] toReturn = new SDKDataStoreFieldMetadata[fieldsMeta.size()];
		for (int i = 0; i < fieldsMeta.size(); i++) {
			FieldMetadata aFieldMetadata = (FieldMetadata) fieldsMeta.get(i);
			SDKDataStoreFieldMetadata aSDKDataStoreFieldMetadata = this.fromFieldMetadataToSDKDataStoreFieldMetadata(aFieldMetadata);
			toReturn[i] = aSDKDataStoreFieldMetadata;
		}
        logger.debug("OUT");
        return toReturn;
	}

	private SDKDataStoreFieldMetadata fromFieldMetadataToSDKDataStoreFieldMetadata(
			FieldMetadata fieldMetadata) {
		logger.debug("IN");
		if (fieldMetadata == null) {
			logger.warn("FieldMetadata in input is null!!");
			return null;
		}
		SDKDataStoreFieldMetadata toReturn = new SDKDataStoreFieldMetadata();
		Map properties = fieldMetadata.getProperties();
		toReturn.setProperties(new HashMap(properties));
		toReturn.setName(fieldMetadata.getName());
		toReturn.setClassName(fieldMetadata.getType().getName());
        logger.debug("OUT");
        return toReturn;
	}

	public class MemoryOnlyDataSource extends ManagedMemoryDataSource {

	    public MemoryOnlyDataSource(byte[] in, String contentType)
				throws java.io.IOException {
			super(new java.io.ByteArrayInputStream(in), Integer.MAX_VALUE - 2,
					contentType, true);
		}

	    public MemoryOnlyDataSource(String in, String contentType)
				throws java.io.IOException {
			this(in.getBytes(), contentType);
		}
		
	}
	
}
