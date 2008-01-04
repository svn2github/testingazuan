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
package it.eng.spagobi.tools.importexport;


import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjPar;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFunctions;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParameters;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuse;
import it.eng.spagobi.behaviouralmodel.check.metadata.SbiChecks;
import it.eng.spagobi.behaviouralmodel.lov.metadata.SbiLov;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.engines.config.metadata.SbiEngines;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ImportUtilities {

    static private Logger logger = Logger.getLogger(ImportUtilities.class);
	/**
	 * Decompress the export compress file
	 * @param pathImpTmpFolder The path of the import directory 
	 * @param pathArchiveFile The path of the exported archive
	 * @throws EMFUserError
	 */
	public static void decompressArchive(String pathImpTmpFolder, String pathArchiveFile) throws EMFUserError {
	    logger.debug("IN");
	    File tmpFolder = new File(pathImpTmpFolder);
		tmpFolder.mkdirs();
		int BUFFER = 2048;
		try {
	         BufferedOutputStream dest = null;
	         FileInputStream fis = new FileInputStream(pathArchiveFile);
	         ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
	         ZipEntry entry;
	         while((entry = zis.getNextEntry()) != null) {
	            int count;
	            byte data[] = new byte[BUFFER];
	            // START: to solve problems of windows path
	            String entryName = entry.getName();
	            int indexofdp = entryName.indexOf(":\\");
	            if(indexofdp!=-1) {
	            	int indexlastslash = entryName.lastIndexOf("\\");
	            	entryName = entryName.substring(0, indexofdp - 2) + entryName.substring(indexlastslash);
	            }
	            // END: to solve problems of windows path
	            File entryFile = new File(pathImpTmpFolder+ "/" + entryName);
	            File entryFileFolder = entryFile.getParentFile();
	            entryFileFolder.mkdirs();
	            FileOutputStream fos = new FileOutputStream(pathImpTmpFolder+ "/" + entryName);
	            dest = new BufferedOutputStream(fos, BUFFER);
	            while ((count = zis.read(data, 0, BUFFER)) != -1) {
	               dest.write(data, 0, count);
	            }
	            dest.flush();
	            dest.close();
	         }
	         zis.close();
	      } catch (EOFException eofe) {
	    	  logger.warn("Error during the decompression of the exported file " , eofe);
	      } catch(Exception e) {
		  logger.warn("Error during the decompression of the exported file " , e);
	    	  throw new EMFUserError(EMFErrorSeverity.ERROR, "100", "component_impexp_messages");
	      }finally{
		  logger.debug("OUT");
	      }
	}
	
	
	
	/**
	 * Creates an Hibernate session factory for the exported database
	 * @param pathDBFolder The path of the folder which contains the exported database
	 * @return The Hibernate session factory
	 * @throws EMFUserError
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
	 * Creates a new hibernate role object
	 * @param role old hibernate role object
	 * @return the new hibernate role object
	 */
	public static SbiExtRoles makeNewSbiExtRole(SbiExtRoles role){
	    	logger.debug("IN");
		SbiExtRoles newRole = new SbiExtRoles();
		newRole.setCode(role.getCode());
		newRole.setDescr(role.getDescr());
		newRole.setName(role.getName());
		newRole.setRoleType(role.getRoleType());
		newRole.setRoleTypeCode(role.getRoleTypeCode());
		newRole.setSbiParuseDets(new HashSet());
		newRole.setSbiFuncRoles(new HashSet());
		logger.debug("OUT");
		return newRole;
	}
	
	/**
	 * Creates a new hibernate role object
	 * @param role old hibernate role object
	 * @param the id to assign to the new created role
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
	 * Creates a new hibernate engine object
	 * @param engine old hibernate engine object
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
		newEng.setEngineType(engine.getEngineType());
		newEng.setBiobjType(engine.getBiobjType());
		newEng.setClassNm(engine.getClassNm());
		logger.debug("OUT");
		return newEng;
	}
	
	/**
	 * Creates a new hibernate engine object
	 * @param engine old hibernate engine object
	 * @param the id to assign to the new created engine
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
	 * Creates a new hibernate functionality object
	 * @param functionality old hibernate functionality object
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
	 * Creates a new hibernate functionality object
	 * @param functionality old hibernate functionality object
	 * @param the id to assign to the new created functionality
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
	 * Creates a new hibernate lov object
	 * @param lov old hibernate lov object
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
	 * Creates a new hibernate lov object
	 * @param lov old hibernate lov object
	 * @param the id to assign to the new created lov
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
	 * Creates a new hibernate check object
	 * @param check old hibernate check object
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
	 * Creates a new hibernate check object
	 * @param check old hibernate check object
	 * @param the id to assign to the new created check
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
	 * Creates a new hibernate parameter object
	 * @param parameter old hibernate parameter object
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
		logger.debug("OUT");
		return newPar;
	}
	
	/**
	 * Creates a new hibernate parameter object
	 * @param parameter old hibernate parameter object
	 * @param the id to assign to the new created parameter
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
	 * Creates a new hibernate parameter use object
	 * @param parameter use old hibernate parameter use object
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
	 * Creates a new hibernate parameter use object
	 * @param parameter use old hibernate parameter use object
	 * @param the id to assign to the new created parameter use
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
	 * Creates a new hibernate biobject
	 * @param obj old hibernate biobject
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
		newObj.setObjectTypeCode(obj.getObjectTypeCode());
		newObj.setPath(obj.getPath());
		newObj.setRelName(obj.getRelName());
		newObj.setSbiEngines(obj.getSbiEngines());
		newObj.setSbiObjPars(new HashSet());
		newObj.setSbiObjFuncs(new HashSet());
		newObj.setSbiObjStates(new HashSet());
		newObj.setSchedFl(obj.getSchedFl());
		newObj.setState(obj.getState());
		newObj.setStateCode(obj.getStateCode());
		newObj.setStateConsideration(obj.getStateConsideration());
		newObj.setStateConsiderationCode(obj.getStateConsiderationCode());
		newObj.setVisible(obj.getVisible());
		newObj.setUuid(obj.getUuid());
		logger.debug("OUT");
		return newObj;
	}
	

	/**
	 * Creates a new hibernate biobject
	 * @param obj old hibernate biobject
	 * @param the id to assign to the new created biobject
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
	 * Creates a new hibernate biobject parameter object
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
	 * Creates a new hibernate biobject parameter object
	 */
	public static SbiObjPar makeNewSbiObjpar(SbiObjPar objpar, Integer id){
	    	logger.debug("IN");
		SbiObjPar newObjPar = makeNewSbiObjpar(objpar);
		newObjPar.setObjParId(id);
		logger.debug("OUT");
		return newObjPar;
	}
	
}
