package it.eng.spagobi.importexport.version18;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ImportUtilities {

	public static void decompressArchive(String pathImpTmpFolder, String pathArchiveFile) throws EMFUserError {
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
	            File entryFile = new File(pathImpTmpFolder+ "/" + entry.getName());
	            File entryFileFolder = entryFile.getParentFile();
	            entryFileFolder.mkdirs();
	            FileOutputStream fos = new FileOutputStream(pathImpTmpFolder+ "/" + entry.getName());
	            dest = new BufferedOutputStream(fos, BUFFER);
	            while ((count = zis.read(data, 0, BUFFER)) != -1) {
	               dest.write(data, 0, count);
	            }
	            dest.flush();
	            dest.close();
	         }
	         zis.close();
	      } catch(Exception e) {
	    	  SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "ImportUtilities" , "decompressArchive",
	        			          "Error during the decompression of the exported file " + e);
	        	throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
	      }
	}
	
	
	
	
	public static SessionFactory getHibSessionExportDB(String pathDBFolder) throws EMFUserError {
		Configuration conf = new Configuration();
		String resource = "it/eng/spagobi/importexport/version18/hibernate.cfg.hsql.export.xml";
		conf = conf.configure(resource);
		String hsqlJdbcString = "jdbc:hsqldb:file:" + pathDBFolder + "/metadata;shutdown=true";
		conf.setProperty("hibernate.connection.url",hsqlJdbcString);
		SessionFactory sessionFactory = conf.buildSessionFactory();
		return sessionFactory;
	}
	
	
	public static SbiExtRoles makeNewSbiExtRole(SbiExtRoles role){
		SbiExtRoles newRole = new SbiExtRoles();
		newRole.setCode(role.getCode());
		newRole.setDescr(role.getDescr());
		newRole.setName(role.getName());
		newRole.setRoleType(role.getRoleType());
		newRole.setRoleTypeCode(role.getRoleTypeCode());
		newRole.setSbiParuseDets(new HashSet());
		newRole.setSbiFuncRoles(new HashSet());
		return newRole;
	}
	
	public static SbiExtRoles makeNewSbiExtRole(SbiExtRoles role, Integer id){
		SbiExtRoles newRole = makeNewSbiExtRole(role);
		newRole.setExtRoleId(id);
		return newRole;
	}
	
	
	public static SbiEngines makeNewSbiEngine(SbiEngines engine){
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
		return newEng;
	}
	
	public static SbiEngines makeNewSbiEngine(SbiEngines engine, Integer id){
		SbiEngines newEng = makeNewSbiEngine(engine);
		newEng.setEngineId(id);
		return newEng;
	}
	
	
	public static SbiFunctions makeNewSbiFunction(SbiFunctions funct){
		SbiFunctions newFunct = new SbiFunctions();
		newFunct.setCode(funct.getCode());
		newFunct.setDescr(funct.getDescr());
		newFunct.setFunctType(funct.getFunctType());
		newFunct.setFunctTypeCd(funct.getFunctTypeCd());
		newFunct.setName(funct.getName());
		newFunct.setParentFunct(funct.getParentFunct());
		newFunct.setPath(funct.getPath());
		newFunct.setSbiFuncRoles(new HashSet());
		newFunct.setSbiObjFuncs(new HashSet());
		return newFunct;
	}
	
	public static SbiFunctions makeNewSbiFunction(SbiFunctions funct, Integer id){
		SbiFunctions newFunct = makeNewSbiFunction(funct);
		newFunct.setFunctId(id);
		return newFunct;
	}
	
	
	public static SbiLov makeNewSbiLov(SbiLov lov){
		SbiLov newlov = new SbiLov();
		newlov.setDefaultVal(lov.getDefaultVal());
		newlov.setDescr(lov.getDescr());
		newlov.setInputType(lov.getInputType());
		newlov.setInputTypeCd(lov.getInputTypeCd());
		newlov.setLabel(lov.getLabel());
		newlov.setLovProvider(lov.getLovProvider());
		newlov.setName(lov.getName());
		newlov.setProfileAttr(lov.getProfileAttr());
		return newlov;
	}
	
	public static SbiLov makeNewSbiLov(SbiLov lov, Integer id){
		SbiLov newlov = makeNewSbiLov(lov);
		newlov.setLovId(id);
		return newlov;
	}
	
	
	
	public static SbiChecks makeNewSbiCheck(SbiChecks check){
		SbiChecks newck = new SbiChecks();
		newck.setCheckType(check.getCheckType());
		newck.setDescr(check.getDescr());
		newck.setLabel(check.getLabel());
		newck.setName(check.getName());
		newck.setValue1(check.getValue1());
		newck.setValue2(check.getValue2());
		newck.setValueTypeCd(check.getValueTypeCd());	
		return newck;
	}
	
	public static SbiChecks makeNewSbiCheck(SbiChecks check, Integer id){
		SbiChecks newCk = makeNewSbiCheck(check);
		newCk.setCheckId(id);
		return newCk;
	}
	
	
	public static SbiParameters makeNewSbiParameter(SbiParameters param){
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
		return newPar;
	}
	
	public static SbiParameters makeNewSbiParameter(SbiParameters param, Integer id){
		SbiParameters newPar = makeNewSbiParameter(param);
		newPar.setParId(id);
		return newPar;
	}
	
	
	public static SbiParuse makeNewSbiParuse(SbiParuse paruse){
		SbiParuse newParuse = new SbiParuse();
		newParuse.setDescr(paruse.getDescr());
		newParuse.setLabel(paruse.getLabel());
		newParuse.setName(paruse.getName());
		newParuse.setSbiLov(paruse.getSbiLov());
		newParuse.setSbiParameters(paruse.getSbiParameters());
		newParuse.setSbiParuseCks(new HashSet());
		newParuse.setSbiParuseDets(new HashSet());
		return newParuse;
	}
	
	
	public static SbiParuse makeNewSbiParuse(SbiParuse paruse, Integer id){
		SbiParuse newParuse = makeNewSbiParuse(paruse);
		newParuse.setUseId(id);
		return newParuse;
	}
	
	
	public static SbiObjects makeNewSbiObject(SbiObjects obj){
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
		return newObj;
	}
	
	
	public static SbiObjects makeNewSbiObject(SbiObjects obj, Integer id){
		SbiObjects newObj = makeNewSbiObject(obj);
		newObj.setBiobjId(id);
		return newObj;
	}
	
}
