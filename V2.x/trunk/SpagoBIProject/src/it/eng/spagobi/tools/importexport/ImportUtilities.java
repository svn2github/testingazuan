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
package it.eng.spagobi.tools.importexport;


import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjPar;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjTemplates;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSnapshots;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubObjects;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFuncRole;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFuncRoleId;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFunctions;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParameters;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseCk;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseCkId;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseDet;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseDetId;
import it.eng.spagobi.behaviouralmodel.check.metadata.SbiChecks;
import it.eng.spagobi.behaviouralmodel.lov.metadata.SbiLov;
import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.dataset.metadata.SbiFileDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiQueryDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiWSDataSet;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ImportUtilities {

    static private Logger logger = Logger.getLogger(ImportUtilities.class);
	
    public static final int MAX_DEFAULT_IMPORT_FILE_SIZE = 5242880;
    
	/**
	 * Decompress the export compress file.
	 * 
	 * @param pathImpTmpFolder The path of the import directory
	 * @param pathArchiveFile The path of the exported archive
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static void decompressArchive(String pathImpTmpFolder, String pathArchiveFile) throws EMFUserError {
	    logger.debug("IN");
	    File tmpFolder = new File(pathImpTmpFolder);
		tmpFolder.mkdirs();
		int BUFFER = 2048;
        FileInputStream fis = null;
        ZipInputStream zis = null;
		try {
	         fis = new FileInputStream(pathArchiveFile);
	         zis = new ZipInputStream(new BufferedInputStream(fis));
	         ZipEntry entry;
	         while((entry = zis.getNextEntry()) != null) {
	        	BufferedOutputStream dest = null;
	        	FileOutputStream fos = null;
	        	try {
		            int count;
		            byte data[] = new byte[BUFFER];
		            String entryName = entry.getName();
		            int indexofdp = entryName.indexOf(":\\");
		            if(indexofdp!=-1) {
		            	int indexlastslash = entryName.lastIndexOf("\\");
		            	entryName = entryName.substring(0, indexofdp - 2) + entryName.substring(indexlastslash);
		            }
		            File entryFile = new File(pathImpTmpFolder+ "/" + entryName);
		            File entryFileFolder = entryFile.getParentFile();
		            entryFileFolder.mkdirs();
		            fos = new FileOutputStream(pathImpTmpFolder+ "/" + entryName);
		            dest = new BufferedOutputStream(fos, BUFFER);
		            while ((count = zis.read(data, 0, BUFFER)) != -1) {
		               dest.write(data, 0, count);
		            }
		            dest.flush();
	        	} finally {
	        		if (dest != null) dest.close();
	        		if (fos != null) fos.close();
	        	}
	         }
	      } catch (EOFException eofe) {
	    	  logger.warn("Error during the decompression of the exported file " , eofe);
	      } catch (Exception e) {
	    	  logger.warn("Error during the decompression of the exported file " , e);
	    	  	throw new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_impexp_messages");
	      } finally {
	    	  if (zis != null || fis != null) {
	    		  try {
	    			  if (zis != null) zis.close();
	    			  if (fis != null) fis.close();
	    		  } catch (IOException e) {
					logger.error("Error closing stream", e);
	    		  }
	    	  }
	    	  logger.debug("OUT");
	      }
	}
	
	
	
	/**
	 * Creates an Hibernate session factory for the exported database.
	 * 
	 * @param pathDBFolder The path of the folder which contains the exported database
	 * 
	 * @return The Hibernate session factory
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static SessionFactory getHibSessionExportDB(String pathDBFolder) throws EMFUserError {
	    logger.debug("IN");
		Configuration conf = new Configuration();
		String resource = "it/eng/spagobi/tools/importexport/metadata/hibernate.cfg.hsql.export.xml";
		conf = conf.configure(resource);
		String hsqlJdbcString = "jdbc:hsqldb:file:" + pathDBFolder + "/metadata;shutdown=true";
		conf.setProperty("hibernate.connection.url",hsqlJdbcString);
		SessionFactory sessionFactory = conf.buildSessionFactory();
		logger.debug("IN");
		return sessionFactory;
	}
	
	
	/**
	 * Creates a new hibernate role object.
	 * 
	 * @param role old hibernate role object
	 * 
	 * @return the new hibernate role object
	 */
	public static SbiExtRoles makeNewSbiExtRole(SbiExtRoles role){
	    	logger.debug("IN");
		SbiExtRoles newRole = new SbiExtRoles();
		newRole.setCode(role.getCode());
		newRole.setDescr(role.getDescr());
		newRole.setName(role.getName());
		//newRole.setRoleType(role.getRoleType());
		//newRole.setRoleTypeCode(role.getRoleTypeCode());
		newRole.setSbiParuseDets(new HashSet());
		newRole.setSbiFuncRoles(new HashSet());
		newRole.setIsAbleToSaveIntoPersonalFolder(role.getIsAbleToSaveIntoPersonalFolder());
		newRole.setIsAbleToSaveRememberMe(role.getIsAbleToSaveRememberMe());
		newRole.setIsAbleToSeeMetadata(role.getIsAbleToSeeMetadata());
		newRole.setIsAbleToSeeNotes(role.getIsAbleToSeeNotes());
		newRole.setIsAbleToSeeSnapshots(role.getIsAbleToSeeSnapshots());
		newRole.setIsAbleToSeeSubobjects(role.getIsAbleToSeeSubobjects());
		newRole.setIsAbleToSeeViewpoints(role.getIsAbleToSeeViewpoints());
		newRole.setIsAbleToSendMail(role.getIsAbleToSendMail());
		logger.debug("OUT");
		return newRole;
	}
	
	/**
	 * Creates a new hibernate role object.
	 * 
	 * @param role old hibernate role object
	 * @param id the id
	 * 
	 * @return the new hibernate role object
	 */
	public static SbiExtRoles makeNewSbiExtRole(SbiExtRoles role, Integer id){
	    	logger.debug("IN");
		SbiExtRoles newRole = makeNewSbiExtRole(role);
		newRole.setExtRoleId(id);
		logger.debug("OUT");
		return newRole;
	}
	
	
	/**
	 * Creates a new hibernate engine object.
	 * 
	 * @param engine old hibernate engine object
	 * 
	 * @return the new hibernate engine object
	 */
	public static SbiEngines makeNewSbiEngine(SbiEngines engine){
	    	logger.debug("IN");
		SbiEngines newEng = new SbiEngines();
		newEng.setDescr(engine.getDescr());
		newEng.setDriverNm(engine.getDriverNm());
		newEng.setEncrypt(engine.getEncrypt());
		newEng.setLabel(engine.getLabel());
		newEng.setMainUrl(engine.getMainUrl());
		newEng.setName(engine.getName());
		newEng.setObjUplDir(engine.getObjUplDir());
		newEng.setObjUseDir(engine.getObjUseDir());
		newEng.setSecnUrl(engine.getSecnUrl());
		newEng.setClassNm(engine.getClassNm());
		newEng.setUseDataSet(engine.getUseDataSet());
		newEng.setUseDataSource(engine.getUseDataSource());
		logger.debug("OUT");
		return newEng;
	}
	
	/**
	 * Make new data source.
	 * 
	 * @param ds the ds
	 * 
	 * @return the sbi data source
	 */
	public static SbiDataSource makeNewSbiDataSource(SbiDataSource ds){
	    logger.debug("IN");
	    SbiDataSource newDS = new SbiDataSource();
		newDS.setDescr(ds.getDescr());
		newDS.setLabel(ds.getLabel());
		newDS.setJndi(ds.getJndi());
		newDS.setDriver(ds.getDriver());
		newDS.setPwd(ds.getPwd());
		newDS.setUrl_connection(ds.getUrl_connection());
		newDS.setUser(ds.getUser());
		logger.debug("OUT");
		return newDS;
	}	
	
	public static SbiDataSource modifyExistingSbiDataSource(
			SbiDataSource dataSource, Session sessionCurrDB,
			Integer existingDatasourceId) {
    	logger.debug("IN");
    	SbiDataSource existingDatasource = null;
    	try {
    		existingDatasource = (SbiDataSource) sessionCurrDB.load(SbiDataSource.class, existingDatasourceId);
    		existingDatasource.setDescr(dataSource.getDescr());
    		existingDatasource.setDialect(dataSource.getDialect());
    		existingDatasource.setDialectDescr(dataSource.getDialectDescr());
    		existingDatasource.setDriver(dataSource.getDriver());
    		existingDatasource.setJndi(dataSource.getJndi());
    		existingDatasource.setLabel(dataSource.getLabel());
    		existingDatasource.setPwd(dataSource.getPwd());
    		existingDatasource.setUrl_connection(dataSource.getUrl_connection());
    		existingDatasource.setUser(dataSource.getUser());
		} finally {
			logger.debug("OUT");
		}
		return existingDatasource;
	}
	
	/**
	 * Make new data set.
	 * 
	 * @param dataProxy the ds
	 * 
	 * @return the sbi data set
	 */
	public static SbiDataSetConfig makeNewSbiDataSet(SbiDataSetConfig dataset){
	    logger.debug("IN");
	    SbiDataSetConfig newDataset = null;
	    
		if (dataset instanceof SbiFileDataSet) {
			newDataset = new SbiFileDataSet();
			((SbiFileDataSet) newDataset).setFileName(((SbiFileDataSet) dataset).getFileName()); 
		}
		if (dataset instanceof SbiQueryDataSet) {
			newDataset = new SbiQueryDataSet();
			((SbiQueryDataSet) newDataset).setQuery(((SbiQueryDataSet) dataset).getQuery());
		}
		if (dataset instanceof SbiWSDataSet) {
			newDataset = new SbiWSDataSet();
			((SbiWSDataSet) newDataset).setAdress(((SbiWSDataSet) dataset).getAdress());
			((SbiWSDataSet) newDataset).setExecutorClass(((SbiWSDataSet) dataset).getExecutorClass());
			((SbiWSDataSet) newDataset).setOperation(((SbiWSDataSet) dataset).getOperation());
		}
		newDataset.setPivotColumnName(dataset.getPivotColumnName());
		newDataset.setPivotColumnValue(dataset.getPivotColumnValue());
		newDataset.setPivotRowName(dataset.getPivotRowName());
		newDataset.setLabel(dataset.getLabel());
		newDataset.setName(dataset.getName());
		newDataset.setDescription(dataset.getDescription());
		newDataset.setParameters(dataset.getParameters());;
		logger.debug("OUT");
		return newDataset;
	}	
	
	/**
	 * Creates a new hibernate engine object.
	 * 
	 * @param engine old hibernate engine object
	 * @param id the id
	 * 
	 * @return the new hibernate engine object
	 */
	public static SbiEngines makeNewSbiEngine(SbiEngines engine, Integer id){
	    	logger.debug("IN");
		SbiEngines newEng = makeNewSbiEngine(engine);
		newEng.setEngineId(id);
		logger.debug("OUT");
		return newEng;
	}
	
	
	/**
	 * Creates a new hibernate functionality object.
	 * 
	 * @param funct the funct
	 * 
	 * @return the new hibernate functionality object
	 */
	public static SbiFunctions makeNewSbiFunction(SbiFunctions funct){
	    	logger.debug("IN");
		SbiFunctions newFunct = new SbiFunctions();
		newFunct.setCode(funct.getCode());
		newFunct.setDescr(funct.getDescr());
		newFunct.setFunctType(funct.getFunctType());
		newFunct.setFunctTypeCd(funct.getFunctTypeCd());
		newFunct.setName(funct.getName());
		newFunct.setParentFunct(funct.getParentFunct());
		newFunct.setPath(funct.getPath());
		logger.debug("OUT");
		return newFunct;
	}

	/**
	 * Creates a new hibernate functionality object.
	 * 
	 * @param funct the funct
	 * @param id the id
	 * 
	 * @return the new hibernate functionality object
	 */
	public static SbiFunctions makeNewSbiFunction(SbiFunctions funct, Integer id){
	    	logger.debug("IN");
		SbiFunctions newFunct = makeNewSbiFunction(funct);
		newFunct.setFunctId(id);
		logger.debug("OUT");
		return newFunct;
	}
	
	
	/**
	 * Creates a new hibernate lov object.
	 * 
	 * @param lov old hibernate lov object
	 * 
	 * @return the new hibernate lov object
	 */
	public static SbiLov makeNewSbiLov(SbiLov lov){
	    logger.debug("IN");
		SbiLov newlov = new SbiLov();
		newlov.setDefaultVal(lov.getDefaultVal());
		newlov.setDescr(lov.getDescr());
		newlov.setInputType(lov.getInputType());
		newlov.setInputTypeCd(lov.getInputTypeCd());
		newlov.setLabel(lov.getLabel());
		newlov.setLovProvider(lov.getLovProvider());
		newlov.setName(lov.getName());
		newlov.setProfileAttr(lov.getProfileAttr());
		logger.debug("OUT");
		return newlov;
	}

	/**
	 * Creates a new hibernate lov object.
	 * 
	 * @param lov old hibernate lov object
	 * @param id the id
	 * 
	 * @return the new hibernate lov object
	 */
	public static SbiLov makeNewSbiLov(SbiLov lov, Integer id){
	    	logger.debug("IN");
		SbiLov newlov = makeNewSbiLov(lov);
		newlov.setLovId(id);
		logger.debug("OUT");
		return newlov;
	}
	
	
	/**
	 * Creates a new hibernate check object.
	 * 
	 * @param check old hibernate check object
	 * 
	 * @return the new hibernate check object
	 */
	public static SbiChecks makeNewSbiCheck(SbiChecks check){
	    	logger.debug("IN");
		SbiChecks newck = new SbiChecks();
		newck.setCheckType(check.getCheckType());
		newck.setDescr(check.getDescr());
		newck.setLabel(check.getLabel());
		newck.setName(check.getName());
		newck.setValue1(check.getValue1());
		newck.setValue2(check.getValue2());
		newck.setValueTypeCd(check.getValueTypeCd());
		logger.debug("OUT");
		return newck;
	}
	
	/**
	 * Creates a new hibernate check object.
	 * 
	 * @param check old hibernate check object
	 * @param id the id
	 * 
	 * @return the new hibernate check object
	 */
	public static SbiChecks makeNewSbiCheck(SbiChecks check, Integer id){
	    	logger.debug("IN");
		SbiChecks newCk = makeNewSbiCheck(check);
		newCk.setCheckId(id);
		logger.debug("OUT");
		return newCk;
	}
	
	
	/**
	 * Creates a new hibernate parameter object.
	 * 
	 * @param param the param
	 * 
	 * @return the new hibernate parameter object
	 */
	public static SbiParameters makeNewSbiParameter(SbiParameters param){
	    	logger.debug("IN");
		SbiParameters newPar = new SbiParameters();
		newPar.setDescr(param.getDescr());
		newPar.setLabel(param.getLabel());
		newPar.setLength(param.getLength());
		newPar.setMask(param.getMask());
		newPar.setName(param.getName());
		newPar.setParameterType(param.getParameterType());
		newPar.setParameterTypeCode(param.getParameterTypeCode());
		newPar.setSbiObjPars(new HashSet());
		newPar.setSbiParuses(new HashSet());
		newPar.setFunctionalFlag(param.getFunctionalFlag());
		newPar.setTemporalFlag(param.getTemporalFlag());
		logger.debug("OUT");
		return newPar;
	}
	
	/**
	 * Creates a new hibernate parameter object.
	 * 
	 * @param param the param
	 * @param id the id
	 * 
	 * @return the new hibernate parameter object
	 */
	public static SbiParameters makeNewSbiParameter(SbiParameters param, Integer id){
	    	logger.debug("IN");
		SbiParameters newPar = makeNewSbiParameter(param);
		newPar.setParId(id);
		logger.debug("OUT");
		return newPar;
	}
	
	/**
	 * Creates a new hibernate parameter use object.
	 * 
	 * @param paruse the paruse
	 * 
	 * @return the new hibernate parameter use object
	 */
	public static SbiParuse makeNewSbiParuse(SbiParuse paruse){
	    	logger.debug("IN");
		SbiParuse newParuse = new SbiParuse();
		newParuse.setDescr(paruse.getDescr());
		newParuse.setLabel(paruse.getLabel());
		newParuse.setName(paruse.getName());
		newParuse.setSbiLov(paruse.getSbiLov());
		newParuse.setSbiParameters(paruse.getSbiParameters());
		newParuse.setSbiParuseCks(new HashSet());
		newParuse.setSbiParuseDets(new HashSet());
		newParuse.setManualInput(paruse.getManualInput());
		newParuse.setSelectionType(paruse.getSelectionType());
		newParuse.setMultivalue(paruse.getMultivalue());
		logger.debug("OUT");
		return newParuse;
	}
	

	/**
	 * Creates a new hibernate parameter use object.
	 * 
	 * @param paruse the paruse
	 * @param id the id
	 * 
	 * @return the new hibernate parameter use object
	 */
	public static SbiParuse makeNewSbiParuse(SbiParuse paruse, Integer id){
	    	logger.debug("IN");
		SbiParuse newParuse = makeNewSbiParuse(paruse);
		newParuse.setUseId(id);
		logger.debug("OUT");
		return newParuse;
	}
	
	/**
	 * Creates a new hibernate biobject.
	 * 
	 * @param obj old hibernate biobject
	 * 
	 * @return the new hibernate biobject
	 */
	public static SbiObjects makeNewSbiObject(SbiObjects obj){
	    	logger.debug("IN");
		SbiObjects newObj = new SbiObjects();
		newObj.setDescr(obj.getDescr());
		newObj.setEncrypt(obj.getEncrypt());
		newObj.setExecMode(obj.getExecMode());
		newObj.setExecModeCode(obj.getExecModeCode());
		newObj.setLabel(obj.getLabel());
		newObj.setName(obj.getName());
		newObj.setObjectType(obj.getObjectType());
		//newObj.setObjectTypeCode(obj.getObjectTypeCode());
		newObj.setPath(obj.getPath());
		newObj.setRelName(obj.getRelName());
		//newObj.setSbiEngines(obj.getSbiEngines());
		newObj.setSbiObjPars(new HashSet());
		newObj.setSbiObjFuncs(new HashSet());
		newObj.setSbiObjStates(new HashSet());
		newObj.setSchedFl(obj.getSchedFl());
		newObj.setState(obj.getState());
		newObj.setStateCode(obj.getStateCode());
		newObj.setStateConsideration(obj.getStateConsideration());
		newObj.setStateConsiderationCode(obj.getStateConsiderationCode());
		newObj.setVisible(obj.getVisible());
		newObj.setProfiledVisibility(obj.getProfiledVisibility());
		newObj.setUuid(obj.getUuid());
		newObj.setCreationDate(obj.getCreationDate());
		newObj.setCreationUser(obj.getCreationUser());
		newObj.setExtendedDescription(obj.getExtendedDescription());
		newObj.setKeywords(obj.getKeywords());
		newObj.setLanguage(obj.getLanguage());
		newObj.setObjectve(obj.getObjectve());
		newObj.setRefreshSeconds(obj.getRefreshSeconds());
		//newObj.setDataSource(obj.getDataSource());
		logger.debug("OUT");
		return newObj;
	}
	
	/**
	 * Make new sbi snapshots.
	 * 
	 * @param obj the obj
	 * 
	 * @return the sbi snapshots
	 */
	public static SbiSnapshots makeNewSbiSnapshots(SbiSnapshots obj){
	    	logger.debug("IN");
	    	SbiSnapshots newObj = new SbiSnapshots();
	    	newObj.setCreationDate(obj.getCreationDate());
	    	newObj.setDescription(obj.getDescription());
	    	newObj.setName(obj.getName());
		logger.debug("OUT");
		return newObj;
	}
	
	/**
	 * Make new sbi sub objects.
	 * 
	 * @param obj the obj
	 * 
	 * @return the sbi sub objects
	 */
	public static SbiSubObjects makeNewSbiSubObjects(SbiSubObjects obj){
	    	logger.debug("IN");
	    	SbiSubObjects newObj = new SbiSubObjects();
	    	newObj.setCreationDate(obj.getCreationDate());
	    	newObj.setDescription(obj.getDescription());
	    	newObj.setIsPublic(obj.getIsPublic());
	    	newObj.setLastChangeDate(obj.getLastChangeDate());
	    	newObj.setName(obj.getName());
	    	newObj.setOwner(obj.getOwner());
		logger.debug("OUT");
		return newObj;
	}	
	
	/**
	 * Make new sbi obj templates.
	 * 
	 * @param obj the obj
	 * 
	 * @return the sbi obj templates
	 */
	public static SbiObjTemplates makeNewSbiObjTemplates(SbiObjTemplates obj){
    	logger.debug("IN");
    	SbiObjTemplates newObj = new SbiObjTemplates();
    	newObj.setActive(obj.getActive());
    	newObj.setCreationDate(obj.getCreationDate());
    	newObj.setCreationUser(obj.getCreationUser());
    	newObj.setName(obj.getName());
    	newObj.setProg(obj.getProg());
	    newObj.setDimension(obj.getDimension());
		logger.debug("OUT");
		return newObj;
	}
	
	/**
	 * Make new sbi bin contents.
	 * 
	 * @param obj the obj
	 * 
	 * @return the sbi bin contents
	 */
	public static SbiBinContents makeNewSbiBinContents(SbiBinContents obj){
	    	logger.debug("IN");
	    	SbiBinContents newObj = new SbiBinContents();
	    	newObj.setContent(obj.getContent());
		logger.debug("OUT");
		return newObj;
	}	

	/**
	 * Creates a new hibernate biobject.
	 * 
	 * @param obj old hibernate biobject
	 * @param id the id
	 * 
	 * @return the new hibernate biobject
	 */
	public static SbiObjects makeNewSbiObject(SbiObjects obj, Integer id){
	    	logger.debug("IN");
		SbiObjects newObj = makeNewSbiObject(obj);
		newObj.setBiobjId(id);
		logger.debug("OUT");
		return newObj;
	}
	
	/**
	 * Load an existing biobject and make modifications as per the exported biobject in input
	 * (existing associations with functionalities are maintained, while existing associations
	 * with parameters are deleted).
	 * 
	 * @param exportedObj the exported obj
	 * @param sessionCurrDB the session curr db
	 * @param existingId the existing id
	 * 
	 * @return the existing biobject modified as per the exported biobject in input
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static SbiObjects modifyExistingSbiObject(SbiObjects exportedObj, Session sessionCurrDB, 
			Integer existingId) throws EMFUserError {
		logger.debug("IN");
		SbiObjects existingObj = null;
		try {
		    // update document
		    existingObj = (SbiObjects) sessionCurrDB.load(SbiObjects.class, existingId);
		    existingObj.setName(exportedObj.getName());
		    existingObj.setDescr(exportedObj.getDescr());
		    existingObj.setLabel(exportedObj.getLabel());
		    existingObj.setExecModeCode(exportedObj.getExecModeCode());
		    existingObj.setObjectTypeCode(exportedObj.getObjectTypeCode());
		    existingObj.setPath(exportedObj.getPath());
		    existingObj.setRelName(exportedObj.getRelName());
		    existingObj.setStateConsiderationCode(exportedObj.getStateConsiderationCode());
		    existingObj.setUuid(exportedObj.getUuid());
		    existingObj.setEncrypt(exportedObj.getEncrypt());
		    existingObj.setSbiEngines(exportedObj.getSbiEngines());
		    existingObj.setSchedFl(exportedObj.getSchedFl());
		    existingObj.setSbiObjStates(new HashSet());
		    existingObj.setVisible(exportedObj.getVisible());
		    existingObj.setProfiledVisibility(exportedObj.getProfiledVisibility());
		    existingObj.setRefreshSeconds(exportedObj.getRefreshSeconds());
		    existingObj.setExtendedDescription(exportedObj.getExtendedDescription());
		    existingObj.setKeywords(exportedObj.getKeywords());
		    existingObj.setLanguage(exportedObj.getLanguage());
		    existingObj.setObjectve(exportedObj.getObjectve());
		    
		    // deletes existing associations between object and parameters 
		    Set objPars = existingObj.getSbiObjPars();
		    Iterator objParsIt = objPars.iterator();
		    while (objParsIt.hasNext()) {
		    	SbiObjPar objPar = (SbiObjPar) objParsIt.next();
		    	sessionCurrDB.delete(objPar);
		    }
		} finally {
			logger.debug("OUT");
		}
		return existingObj;
	}

	/**
	 * Load an existing parameter and make modifications as per the exported parameter in input
	 * (existing associations with biobjects are maintained, while parameter uses are deleted).
	 * 
	 * @param exportedParameter the exported parameter
	 * @param sessionCurrDB the session curr db
	 * @param existingId the existing id
	 * 
	 * @return the existing parameter modified as per the exported parameter in input
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static SbiParameters modifyExistingSbiParameter(SbiParameters exportedParameter, Session sessionCurrDB, 
			Integer existingId) throws EMFUserError {
		logger.debug("IN");
		SbiParameters existingPar = null;
		try {
		    // update parameter
		    existingPar = (SbiParameters) sessionCurrDB.load(SbiParameters.class, existingId);
			existingPar.setDescr(exportedParameter.getDescr());
			existingPar.setLabel(exportedParameter.getLabel());
			existingPar.setLength(exportedParameter.getLength());
			existingPar.setMask(exportedParameter.getMask());
			existingPar.setName(exportedParameter.getName());
			existingPar.setSbiParuses(new HashSet());
			existingPar.setFunctionalFlag(exportedParameter.getFunctionalFlag());
			existingPar.setTemporalFlag(exportedParameter.getTemporalFlag());
		    // deletes existing associations between object and parameters 
		    Set paruses = existingPar.getSbiParuses();
		    Iterator parusesIt = paruses.iterator();
		    while (parusesIt.hasNext()) {
		    	SbiParuse paruse = (SbiParuse) parusesIt.next();
		    	sessionCurrDB.delete(paruse);
		    }
		} finally {
			logger.debug("OUT");
		}
		return existingPar;
	}

	/**
	 * Creates a new hibernate biobject parameter object.
	 * 
	 * @param objpar the objpar
	 * 
	 * @return the sbi obj par
	 */
	public static SbiObjPar makeNewSbiObjpar(SbiObjPar objpar){
	    logger.debug("IN");
		SbiObjPar newObjPar = new SbiObjPar();
		newObjPar.setLabel(objpar.getLabel());
		newObjPar.setModFl(objpar.getModFl());
		newObjPar.setMultFl(objpar.getMultFl());
		newObjPar.setParurlNm(objpar.getParurlNm());
		newObjPar.setPriority(objpar.getPriority());
		newObjPar.setProg(objpar.getProg());
		newObjPar.setReqFl(objpar.getReqFl());
		newObjPar.setSbiObject(objpar.getSbiObject());
		newObjPar.setSbiParameter(objpar.getSbiParameter());
		newObjPar.setViewFl(objpar.getViewFl());
		logger.debug("OUT");
		return newObjPar;
	}
	
	/**
	 * Creates a new hibernate biobject parameter object.
	 * 
	 * @param objpar the objpar
	 * @param id the id
	 * 
	 * @return the sbi obj par
	 */
	public static SbiObjPar makeNewSbiObjpar(SbiObjPar objpar, Integer id){
	    logger.debug("IN");
		SbiObjPar newObjPar = makeNewSbiObjpar(objpar);
		newObjPar.setObjParId(id);
		logger.debug("OUT");
		return newObjPar;
	}


	/**
	 * Set into the biobject to the engine/object type/object state/datasource
	 * the entities associated with the exported biobject.
	 * 
	 * @param obj the obj
	 * @param exportedObj the exported obj
	 * @param sessionCurrDB the session curr db
	 * @param importer the importer
	 * @param metaAss the meta ass
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static void associateWithExistingEntities(SbiObjects obj,
			SbiObjects exportedObj, Session sessionCurrDB,
			ImporterMetadata importer, MetadataAssociations metaAss) throws EMFUserError {
		logger.debug("IN");
		try {
		    // reading exist engine
		    SbiEngines engine = getAssociatedSbiEngine(exportedObj, sessionCurrDB, metaAss);
		    obj.setSbiEngines(engine);
		    // reading exist object type
		    SbiDomains existDom = getAssociatedBIObjectType(exportedObj, sessionCurrDB, importer);
			if (existDom != null) {
				obj.setObjectType(existDom);
				obj.setObjectTypeCode(existDom.getValueCd());
			}
			// reading exist state
			SbiDomains existDomSt = getAssociatedBIObjectState(exportedObj, sessionCurrDB, importer);
			if (existDomSt != null) {
				obj.setState(existDomSt);
				obj.setStateCode(existDomSt.getValueCd());
			}
			// reading exist datasource
			SbiDataSource localDS = getAssociatedSbiDataSource(exportedObj, sessionCurrDB, metaAss);
			if (localDS != null) obj.setDataSource(localDS);
			// reading exist datasset
			SbiDataSetConfig localDataSet = getAssociatedSbiDataSet(exportedObj, sessionCurrDB, metaAss);
			if (localDataSet != null) obj.setDataSet(localDataSet);
		} finally {
			logger.debug("OUT");
		}
	}
	
	
	private static SbiDataSource getAssociatedSbiDataSource(
			SbiObjects exportedObj, Session sessionCurrDB,
			MetadataAssociations metaAss) {
		logger.debug("IN");
		SbiDataSource expDs = exportedObj.getDataSource();
		if (expDs != null) {
			Integer existingDsId = (Integer) metaAss.getDataSourceIDAssociation().get(new Integer(expDs.getDsId()));
//			if (label == null) {
//			    // exist a DataSource Association, read a new DataSource
//			    // from the DB
//			    label = expDs.getLabel();
//			}
//			Criterion labelCriterrion = Expression.eq("label", label);
//			Criteria criteria = sessionCurrDB.createCriteria(SbiDataSource.class);
//			criteria.add(labelCriterrion);
//			SbiDataSource localDS = (SbiDataSource) criteria.uniqueResult();
			SbiDataSource localDS = (SbiDataSource) sessionCurrDB.load(SbiDataSource.class, existingDsId);
			logger.debug("OUT");
			return localDS;
		} else {
			logger.debug("OUT");
			return null;
		}

	}

	private static SbiDataSetConfig getAssociatedSbiDataSet(
			SbiObjects exportedObj, Session sessionCurrDB,
			MetadataAssociations metaAss) {
		logger.debug("IN");
		SbiDataSetConfig expDataset = exportedObj.getDataSet();
		if (expDataset != null) {
			Integer existingDatasetId = (Integer) metaAss.getDataSetIDAssociation().get(new Integer(expDataset.getDsId()));
			SbiDataSetConfig localDS = (SbiDataSetConfig) sessionCurrDB.load(SbiDataSetConfig.class, existingDatasetId);
			logger.debug("OUT");
			return localDS;
		} else {
			logger.debug("OUT");
			return null;
		}

	}

	private static SbiDomains getAssociatedBIObjectState(
			SbiObjects exportedObj, Session sessionCurrDB,
			ImporterMetadata importer) throws EMFUserError {
		logger.debug("IN");
		String stateCd = exportedObj.getStateCode();
		Map unique = new HashMap();
		unique.put("valuecd", stateCd);
		unique.put("domaincd", "STATE");
		SbiDomains existDomSt = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
		logger.debug("OUT");
		return existDomSt;
	}


	private static SbiDomains getAssociatedBIObjectType(SbiObjects exportedObj,
			Session sessionCurrDB, ImporterMetadata importer) throws EMFUserError {
		logger.debug("IN");
	    String typeCd = exportedObj.getObjectTypeCode();
		Map unique = new HashMap();
		unique.put("valuecd", typeCd);
		unique.put("domaincd", "BIOBJ_TYPE");
		SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
		logger.debug("OUT");
		return existDom;
	}


	private static SbiEngines getAssociatedSbiEngine(SbiObjects exportedObj,
			Session sessionCurrDB, MetadataAssociations metaAss) {
		logger.debug("IN");
		SbiEngines existingEngine = null;
		SbiEngines engine = exportedObj.getSbiEngines();
		Integer expEngId = engine.getEngineId();
		Map assEngs = metaAss.getEngineIDAssociation();
		Integer existingId = (Integer) assEngs.get(expEngId);
		if (existingId != null) {
			existingEngine = (SbiEngines) sessionCurrDB.load(SbiEngines.class, existingId);
		}
		logger.debug("OUT");
		return existingEngine;
	}

	private static SbiDataSource getAssociatedSbiDataSource(SbiQueryDataSet exportedDataset,
			Session sessionCurrDB, MetadataAssociations metaAss) {
		logger.debug("IN");
		SbiDataSource existingDs = null;
		SbiDataSource exportedDs = exportedDataset.getDataSource();
		if (exportedDs != null) {
			Integer exportedDsId = new Integer(exportedDs.getDsId());
			Map assDatasources = metaAss.getDataSourceIDAssociation();
			Integer existingDsID = (Integer) assDatasources.get(exportedDsId);
			existingDs = (SbiDataSource) sessionCurrDB.load(SbiDataSource.class, existingDsID);
		}
		logger.debug("OUT");
		return existingDs;
	}
	
	/**
	 * Set into the parameter to the parameter type
	 * domain associated with the exported parameter.
	 * 
	 * @param parameter the parameter
	 * @param exportedParameter the exported parameter
	 * @param sessionCurrDB the session curr db
	 * @param importer the importer
	 * @param metaAss the meta ass
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static void associateWithExistingEntities(
			SbiParameters parameter, SbiParameters exportedParameter,
			Session sessionCurrDB, ImporterMetadata importer,
			MetadataAssociations metaAss) throws EMFUserError {
		logger.debug("IN");
		try {
			// reading existing parameter type
			SbiDomains existDom = getAssociatedParameterType(exportedParameter, sessionCurrDB, metaAss, importer);
			if (existDom != null) {
				parameter.setParameterType(existDom);
				parameter.setParameterTypeCode(existDom.getValueCd());
			}
		} finally {
			logger.debug("OUT");
		}
		
	}

	/**
	 * Load an existing dataset and make modifications as per the exported dataset in input
	 * 
	 * @param exportedDataset the exported dataset
	 * @param sessionCurrDB the session curr db
	 * @param existingId the existing id
	 * 
	 * @return the existing dataset modified as per the exported dataset in input
	 * 
	 * @throws EMFUserError 	 */
	public static SbiDataSetConfig modifyExistingSbiDataSet(SbiDataSetConfig exportedDataset,
			Session sessionCurrDB, Integer existingId) {
    	logger.debug("IN");
    	SbiDataSetConfig existingDataset = null;
    	try {
    		existingDataset = (SbiDataSetConfig) sessionCurrDB.load(SbiDataSetConfig.class, existingId);
    		// TODO sistemare il cambio di subclass
    		if ((existingDataset instanceof SbiFileDataSet && !(exportedDataset instanceof SbiFileDataSet))
    			|| (existingDataset instanceof SbiQueryDataSet && !(exportedDataset instanceof SbiQueryDataSet))
    			|| (existingDataset instanceof SbiWSDataSet && !(exportedDataset instanceof SbiWSDataSet))) {
    			logger.warn("Cannot change data set subclass");
    		}
			if (existingDataset instanceof SbiFileDataSet)
				((SbiFileDataSet)existingDataset).setFileName(((SbiFileDataSet)exportedDataset).getFileName());
			else if(existingDataset instanceof SbiQueryDataSet)
				((SbiQueryDataSet)existingDataset).setQuery(((SbiQueryDataSet)exportedDataset).getQuery());
			else if(existingDataset instanceof SbiWSDataSet) {
				((SbiWSDataSet)existingDataset).setAdress(((SbiWSDataSet)exportedDataset).getAdress());
				((SbiWSDataSet)existingDataset).setExecutorClass(((SbiWSDataSet)exportedDataset).getExecutorClass());
				((SbiWSDataSet)existingDataset).setOperation(((SbiWSDataSet)exportedDataset).getOperation());
			}
			existingDataset.setLabel(exportedDataset.getLabel());
			existingDataset.setName(exportedDataset.getName());			
			existingDataset.setDescription(exportedDataset.getDescription());
			existingDataset.setParameters(exportedDataset.getParameters());
			existingDataset.setPivotColumnName(exportedDataset.getPivotColumnName());
			existingDataset.setPivotColumnValue(exportedDataset.getPivotColumnValue());
			existingDataset.setPivotRowName(exportedDataset.getPivotRowName());
		} finally {
			logger.debug("OUT");
		}
		return existingDataset;
	}

	/**
	 * Load an existing lov and make modifications as per the exported lov in input
	 * (existing associations with parameters are maintained).
	 * 
	 * @param exportedLov the exported lov
	 * @param sessionCurrDB the session curr db
	 * @param existingId the existing id
	 * 
	 * @return the existing lov modified as per the exported lov in input
	 * 
	 * @throws EMFUserError 	 */
	public static SbiLov modifyExistingSbiLov(SbiLov exportedLov,
			Session sessionCurrDB, Integer existingId) {
    	logger.debug("IN");
    	SbiLov existingLov = null;
    	try {
	    	existingLov = (SbiLov) sessionCurrDB.load(SbiLov.class, existingId);
	    	existingLov.setDefaultVal(exportedLov.getDefaultVal());
	    	existingLov.setDescr(exportedLov.getDescr());
	    	existingLov.setLabel(exportedLov.getLabel());
	    	existingLov.setLovProvider(exportedLov.getLovProvider());
	    	existingLov.setName(exportedLov.getName());
	    	existingLov.setProfileAttr(exportedLov.getProfileAttr());
		} finally {
			logger.debug("OUT");
		}
		return existingLov;
	}


	/**
	 * Set into the lov the lov type domain
	 * associated with the exported lov.
	 * 
	 * @param lov the lov
	 * @param exportedLov the exported lov
	 * @param sessionCurrDB the session curr db
	 * @param importer the importer
	 * @param metaAss the meta ass
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static void associateWithExistingEntities(SbiLov lov,
			SbiLov exportedLov, Session sessionCurrDB,
			ImporterMetadata importer, MetadataAssociations metaAss) throws EMFUserError {
		logger.debug("IN");
		try {
			// reading existing lov type
			SbiDomains existDom = getAssociatedLovType(exportedLov, sessionCurrDB, metaAss, importer);
			if (existDom != null) {
				lov.setInputType(existDom);
				lov.setInputTypeCd(existDom.getValueCd());
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Set into the datasource the dialect type domain
	 * associated with the exported datasource.
	 * 
	 * @param datasource the datasource
	 * @param exportedDatasource the exported lov
	 * @param sessionCurrDB the session curr db
	 * @param importer the importer
	 * @param metaAss the meta ass
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static void associateWithExistingEntities(SbiDataSource datasource,
			SbiDataSource exportedDatasource, Session sessionCurrDB,
			ImporterMetadata importer, MetadataAssociations metaAss) throws EMFUserError {
		logger.debug("IN");
		try {
			// reading existing lov type
			SbiDomains dialect = getAssociatedDialect(exportedDatasource, sessionCurrDB, metaAss, importer);
			if (dialect != null) {
				datasource.setDialect(dialect);
				datasource.setDialectDescr(dialect.getValueDs());
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	/**
	 * Set into the dataset the datasource
	 * associated with the exported dataset.
	 * 
	 * @param dataset the dataset
	 * @param exportedDataset the exported dataset
	 * @param sessionCurrDB the session curr db
	 * @param importer the importer
	 * @param metaAss the meta ass
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public static void associateWithExistingEntities(SbiDataSetConfig dataset,
			SbiDataSetConfig exportedDataset, Session sessionCurrDB,
			ImporterMetadata importer, MetadataAssociations metaAss) throws EMFUserError {
		logger.debug("IN");
		try {
			// TODO togliere il controllo che sia quello esportato sia quello esistente siano dello stesso tipo
			if (exportedDataset instanceof SbiQueryDataSet && dataset instanceof SbiQueryDataSet) {
				SbiQueryDataSet queryDataSet = (SbiQueryDataSet) exportedDataset;
				SbiDataSource ds = getAssociatedSbiDataSource(queryDataSet, sessionCurrDB, metaAss);
				if (ds != null) {
					((SbiQueryDataSet) dataset).setDataSource(ds);
				}
			}
			// reading existing transfomer type
			SbiDomains transformer = getAssociatedTransfomerType(exportedDataset, sessionCurrDB, metaAss, importer);
			if (transformer != null) {
				dataset.setTransformer(transformer);
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	private static SbiDomains getAssociatedTransfomerType(SbiDataSetConfig exportedDataset,
			Session sessionCurrDB, MetadataAssociations metaAss, ImporterMetadata importer) throws EMFUserError {
		logger.debug("IN");
		SbiDomains transformer = exportedDataset.getTransformer();
		if (transformer == null) {
			logger.debug("Transformer not set for exported dataset [" + exportedDataset.getLabel() + "]");
			return null;
		}
		String typeCd = transformer.getValueCd();
		Map unique = new HashMap();
		unique.put("valuecd", typeCd);
		unique.put("domaincd", "TRANSFORMER_TYPE");
		SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
		logger.debug("OUT");
		return existDom;
	}
	
	private static SbiDomains getAssociatedParameterType(SbiParameters exportedParameter,
			Session sessionCurrDB, MetadataAssociations metaAss, ImporterMetadata importer) throws EMFUserError {
		logger.debug("IN");
		String typeCd = exportedParameter.getParameterTypeCode();
		Map unique = new HashMap();
		unique.put("valuecd", typeCd);
		unique.put("domaincd", "PAR_TYPE");
		SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
		logger.debug("OUT");
		return existDom;
	}
	
	private static SbiDomains getAssociatedDialect(SbiDataSource exportedDatasource,
			Session sessionCurrDB, MetadataAssociations metaAss, ImporterMetadata importer) throws EMFUserError {
		logger.debug("IN");
		String valueCd = exportedDatasource.getDialect().getValueCd();
		Map unique = new HashMap();
		unique.put("valuecd", valueCd);
		unique.put("domaincd", "DIALECT_HIB");
		SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
		logger.debug("OUT");
		return existDom;
	}
	
	private static SbiDomains getAssociatedLovType(SbiLov exportedLov,
			Session sessionCurrDB, MetadataAssociations metaAss, ImporterMetadata importer) throws EMFUserError {
		logger.debug("IN");
		String inpTypeCd = exportedLov.getInputTypeCd();
		Map unique = new HashMap();
		unique.put("valuecd", inpTypeCd);
		unique.put("domaincd", "INPUT_TYPE");
		SbiDomains existDom = (SbiDomains) importer.checkExistence(unique, sessionCurrDB, new SbiDomains());
		logger.debug("OUT");
		return existDom;
	}

	public static SbiParuseDet makeNewSbiParuseDet(SbiParuseDet parusedet, Integer newParuseid, Integer newRoleid) {
		logger.debug("IN");
		SbiParuseDetId parusedetid = parusedet.getId();
		SbiParuseDetId newParusedetid = new SbiParuseDetId();
		if (newParuseid != null) {
		    SbiParuse sbiparuse = parusedetid.getSbiParuse();
		    SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(sbiparuse, newParuseid);
		    newParusedetid.setSbiParuse(newParuse);
		}
		if (newRoleid != null) {
		    SbiExtRoles sbirole = parusedetid.getSbiExtRoles();
		    SbiExtRoles newRole = ImportUtilities.makeNewSbiExtRole(sbirole, newRoleid);
		    newParusedetid.setSbiExtRoles(newRole);
		}
		SbiParuseDet newParuseDet = new SbiParuseDet();
		newParuseDet.setId(newParusedetid);
		newParuseDet.setDefaultVal(parusedet.getDefaultVal());
		newParuseDet.setHiddenFl(parusedet.getHiddenFl());
		newParuseDet.setProg(parusedet.getProg());
		logger.debug("OUT");
		return newParuseDet;
	}



	public static SbiParuseCk makeNewSbiParuseCk(SbiParuseCk paruseck,
			Integer newParuseid, Integer newCheckid) {
		logger.debug("IN");
		// build a new id for the SbiParuseCheck
		SbiParuseCkId parusecheckid = paruseck.getId();
		SbiParuseCkId newParusecheckid = new SbiParuseCkId();
		if (newParuseid != null) {
		    SbiParuse sbiparuse = parusecheckid.getSbiParuse();
		    SbiParuse newParuse = ImportUtilities.makeNewSbiParuse(sbiparuse, newParuseid);
		    newParusecheckid.setSbiParuse(newParuse);
		}
		if (newCheckid != null) {
		    SbiChecks sbicheck = parusecheckid.getSbiChecks();
		    SbiChecks newCheck = ImportUtilities.makeNewSbiCheck(sbicheck, newCheckid);
		    newParusecheckid.setSbiChecks(newCheck);
		}
		SbiParuseCk newParuseck = new SbiParuseCk();
		newParuseck.setId(newParusecheckid);
		newParuseck.setProg(paruseck.getProg());
		logger.debug("OUT");
		return newParuseck;
	}



	public static SbiFuncRole makeNewSbiFunctRole(SbiFuncRole functrole,
			Integer newFunctid, Integer newRoleid) {
		logger.debug("IN");
		SbiFuncRoleId functroleid = functrole.getId();
		SbiFuncRoleId newFunctroleid = new SbiFuncRoleId();
		if (newFunctid != null) {
		    SbiFunctions sbifunct = functroleid.getFunction();
		    SbiFunctions newFunct = ImportUtilities.makeNewSbiFunction(sbifunct, newFunctid);
		    newFunctroleid.setFunction(newFunct);
		}
		if (newRoleid != null) {
		    SbiExtRoles sbirole = functroleid.getRole();
		    SbiExtRoles newRole = ImportUtilities.makeNewSbiExtRole(sbirole, newRoleid);
		    newFunctroleid.setRole(newRole);
		}
		SbiFuncRole newFunctRole = new SbiFuncRole();
		newFunctRole.setId(newFunctroleid);
		logger.debug("OUT");
		return newFunctRole;
	}

	
	public static int getImportFileMaxSize() {
		logger.debug("IN");
		int toReturn = MAX_DEFAULT_IMPORT_FILE_SIZE;
		try {
			ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	    	SourceBean maxSizeSB = (SourceBean) serverConfig.getAttribute("IMPORTEXPORT.IMPORT_FILE_MAX_SIZE");
	    	if (maxSizeSB != null) {
	    		String maxSizeStr = (String) maxSizeSB.getCharacters();
	    		logger.debug("Configuration found for max import file size: " + maxSizeStr);
	    		Integer maxSizeInt = new Integer(maxSizeStr);
	    		toReturn = maxSizeInt.intValue();
	    	} else {
	    		logger.debug("No configuration found for max import file size");
	    	}
		} catch (Exception e) {
			logger.error("Error while retrieving max import file size", e);
			logger.debug("Considering default value " + MAX_DEFAULT_IMPORT_FILE_SIZE);
			toReturn = MAX_DEFAULT_IMPORT_FILE_SIZE;
		}
		logger.debug("OUT: max size = " + toReturn);
		return toReturn;
	}
	
	public static String getImportTempFolderPath() {
		logger.debug("IN");
		String toReturn = null;
		try {
		    ConfigSingleton conf = ConfigSingleton.getInstance();
		    SourceBean importerSB = (SourceBean) conf.getAttribute("IMPORTEXPORT.IMPORTER");
		    toReturn = (String) importerSB.getAttribute("tmpFolder");
		    toReturn = GeneralUtilities.checkForSystemProperty(toReturn);
		    if (!toReturn.startsWith("/") && toReturn.charAt(1) != ':') {
		    	String root = ConfigSingleton.getRootPath();
		    	toReturn = root + "/" + toReturn;
		    }
		} catch (Exception e) {
			logger.error("Error while retrieving export temporary folder path", e);
		} finally {
			logger.debug("OUT: export temporary folder path = " + toReturn);
		}
		return toReturn;
	}
	
	public static IImportManager getImportManagerInstance() throws Exception {
		logger.debug("IN");
		IImportManager toReturn = null;
		try {
		    ConfigSingleton conf = ConfigSingleton.getInstance();
		    SourceBean importerSB = (SourceBean) conf.getAttribute("IMPORTEXPORT.IMPORTER");
		    // instance the importer class
		    String impClassName = (String) importerSB.getAttribute("class");
		    Class impClass = Class.forName(impClassName);
		    toReturn = (IImportManager) impClass.newInstance();
		} catch (Exception e) {
			logger.error("Error while instantiating import manager", e);
			throw e;
		} finally {
			logger.debug("OUT");
		}
		return toReturn;
	}
	
}
