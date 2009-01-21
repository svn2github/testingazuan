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

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjFunc;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjFuncId;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjPar;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjTemplates;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSnapshots;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubreports;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubreportsId;
import it.eng.spagobi.analiticalmodel.functionalitytree.bo.LowFunctionality;
import it.eng.spagobi.analiticalmodel.functionalitytree.dao.ILowFunctionalityDAO;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFuncRole;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFuncRoleId;
import it.eng.spagobi.analiticalmodel.functionalitytree.metadata.SbiFunctions;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ObjParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ParameterUse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IObjParuseDAO;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiObjParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiObjParuseId;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParameters;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuse;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseCk;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseCkId;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseDet;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParuseDetId;
import it.eng.spagobi.behaviouralmodel.check.bo.Check;
import it.eng.spagobi.behaviouralmodel.check.metadata.SbiChecks;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.metadata.SbiLov;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.bo.Subreport;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.mapcatalogue.bo.GeoFeature;
import it.eng.spagobi.mapcatalogue.bo.GeoMap;
import it.eng.spagobi.mapcatalogue.bo.GeoMapFeature;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoFeaturesDAO;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoMapFeaturesDAO;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoMapsDAO;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoFeatures;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMapFeatures;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMapFeaturesId;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMaps;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.bo.JDBCDataSet;
import it.eng.spagobi.tools.dataset.bo.JavaClassDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WebServiceDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.dataset.metadata.SbiFileDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiJClassDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiQueryDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiScriptDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiWSDataSet;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Implements methods to insert exported metadata into the exported database 
 */
public class ExporterMetadata {

    static private Logger logger = Logger.getLogger(ExporterMetadata.class);
    
	/**
	 * Insert a domain into the exported database.
	 * 
	 * @param domain Domain object to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
    public void insertDomain(Domain domain, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiDomains where valueId = " + domain.getValueId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDomains hibDomain = new SbiDomains(domain.getValueId());
			hibDomain.setDomainCd(domain.getDomainCode());
			hibDomain.setDomainNm(domain.getDomainName());
			hibDomain.setValueCd(domain.getValueCd());
			hibDomain.setValueDs(domain.getValueDescription());
			hibDomain.setValueNm(domain.getValueName());
			session.save(hibDomain);
			tx.commit();
		} catch (Exception e) {
		    logger.error("Error while inserting domain into export database ",e);
        	    throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	/**
	 * Insert data source.
	 * 
	 * @param ds the ds
	 * @param session the session
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertDataSource(IDataSource ds, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiDataSource where dsId = " + ds.getDsId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDomains dialect=(SbiDomains)session.load(SbiDomains.class, ds.getDialectId());
			
			SbiDataSource hibDS = new SbiDataSource(ds.getDsId());
			hibDS.setDescr(ds.getDescr());
			hibDS.setDriver(ds.getDriver());
			hibDS.setJndi(ds.getJndi());
			hibDS.setLabel(ds.getLabel());
			hibDS.setPwd(ds.getPwd());
			hibDS.setUrl_connection(ds.getUrlConnection());
			hibDS.setUser(ds.getUser());
			hibDS.setDialect(dialect);
			
		// va aggiunto il legame con gli engine e il doc ????
			
			session.save(hibDS);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting dataSource into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	/**
	 * Insert data set.
	 * 
	 * @param dataset the dataset
	 * @param session the session
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertDataSet(IDataSet dataset, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			// if it is a query data set, insert datasource first, before opening a new transaction
			if (dataset instanceof JDBCDataSet) {
				IDataSource ds = ((JDBCDataSet) dataset).getDataSource();
				if (ds != null) insertDataSource(ds, session);
			}
			
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiDataSetConfig where dsId = " + dataset.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDataSetConfig hibDataset = null;
			if (dataset instanceof FileDataSet) {
				hibDataset = new SbiFileDataSet();
				((SbiFileDataSet) hibDataset).setFileName(((FileDataSet) dataset).getFileName()); 
			}
			if (dataset instanceof JDBCDataSet) {
				hibDataset = new SbiQueryDataSet();
				((SbiQueryDataSet) hibDataset).setQuery(((JDBCDataSet) dataset).getQuery().toString());
				// insert the association between the dataset and the datasource
				IDataSource ds = ((JDBCDataSet) dataset).getDataSource();
				if (ds != null) {
					SbiDataSource dsHib = (SbiDataSource) session.load(SbiDataSource.class, new Integer(ds.getDsId()));
					((SbiQueryDataSet) hibDataset).setDataSource(dsHib);
				}
			}
			if (dataset instanceof WebServiceDataSet) {
				hibDataset = new SbiWSDataSet();
				((SbiWSDataSet) hibDataset).setAdress(((WebServiceDataSet) dataset).getAddress());
				((SbiWSDataSet) hibDataset).setExecutorClass(((WebServiceDataSet) dataset).getExecutorClass());
				((SbiWSDataSet) hibDataset).setOperation(((WebServiceDataSet) dataset).getOperation());
			}
			if (dataset instanceof JavaClassDataSet) {
				hibDataset = new SbiJClassDataSet();
				((SbiJClassDataSet) hibDataset).setJavaClassName(((JavaClassDataSet) dataset).getClassName());
			}
			if (dataset instanceof ScriptDataSet) {
				hibDataset = new SbiScriptDataSet();
				((SbiScriptDataSet) hibDataset).setScript(((ScriptDataSet) dataset).getScript());
			}
			hibDataset.setDsId(dataset.getId());
			hibDataset.setLabel(dataset.getLabel());
			hibDataset.setName(dataset.getName());
			hibDataset.setDescription(dataset.getDescription());
			hibDataset.setParameters(dataset.getParameters());
			hibDataset.setPivotColumnName(dataset.getPivotColumnName());
			hibDataset.setPivotColumnValue(dataset.getPivotColumnValue());
			hibDataset.setPivotRowName(dataset.getPivotRowName());
			
			if (dataset.getTransformerId() != null) {
				SbiDomains transformerType = (SbiDomains) session.load(SbiEngines.class, dataset.getTransformerId());
				hibDataset.setTransformer(transformerType);
			} else {
				hibDataset.setTransformer(null);
			}

			session.save(hibDataset);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting dataSet into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	/**
	 * Insert an engine into the exported database.
	 * 
	 * @param engine Engine Object to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertEngine(Engine engine, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiEngines where engineId = " + engine.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiEngines hibEngine = new SbiEngines(engine.getId());
			hibEngine.setName(engine.getName());
			hibEngine.setLabel(engine.getLabel());
			hibEngine.setDescr(engine.getDescription());
			hibEngine.setDriverNm(engine.getDriverName());
			hibEngine.setEncrypt(new Short((short)engine.getCriptable().intValue()));
			hibEngine.setMainUrl(engine.getUrl());
			hibEngine.setObjUplDir(engine.getDirUpload());
			hibEngine.setObjUseDir(engine.getDirUsable());
			hibEngine.setSecnUrl(engine.getSecondaryUrl());
			SbiDomains objTypeDom = (SbiDomains)session.load(SbiDomains.class, engine.getBiobjTypeId());
			hibEngine.setBiobjType(objTypeDom);
			hibEngine.setClassNm(engine.getClassName());
			SbiDomains engineTypeDom = (SbiDomains)session.load(SbiDomains.class, engine.getEngineTypeId());
			hibEngine.setEngineType(engineTypeDom);
			hibEngine.setUseDataSource(new Boolean(engine.getUseDataSource()));
			if (engine.getUseDataSource() && engine.getDataSourceId() != null) {
				SbiDataSource ds = (SbiDataSource) session.load(SbiDataSource.class, engine.getDataSourceId());
				hibEngine.setDataSource(ds);
			}
			hibEngine.setUseDataSet(new Boolean(engine.getUseDataSet()));
			session.save(hibEngine);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting engine into export database " ,e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}

	/**
	 * Insert all Snapshot and their binary content.
	 * 
	 * @param biobj the biobj
	 * @param snapshotLis the snapshot lis
	 * @param session the session
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertAllSnapshot(BIObject biobj, List snapshotLis, Session session) throws EMFUserError {
	    logger.debug("IN");
	    Iterator iter=snapshotLis.iterator();
	    while(iter.hasNext()){
		insertSnapshot(biobj,(Snapshot)iter.next(),session);
	    }
	    logger.debug("OUT");
	}
	
	/**
	 * Insert a single sub object and their binary content
	 * @param biobj
	 * @param subObject
	 * @param session
	 * @throws EMFUserError
	 */
	private void insertSnapshot(BIObject biobj, Snapshot snapshot, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiSnapshots where snapId = " + snapshot.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
			    	logger.warn("Exist another SbiSnapshot");
				return;
			}
			
			SbiObjects hibBIObj = new SbiObjects(biobj.getId());

			byte[] template = snapshot.getContent();
			
			SbiBinContents hibBinContent = new SbiBinContents();
			hibBinContent.setId(snapshot.getBinId());
			hibBinContent.setContent(template);
			
			SbiSnapshots sub=new SbiSnapshots();
			sub.setCreationDate(snapshot.getDateCreation());
			sub.setDescription(snapshot.getDescription());
			sub.setName(snapshot.getName());
			sub.setSbiBinContents(hibBinContent);
			sub.setSbiObject(hibBIObj);
			sub.setSnapId(snapshot.getId());
			
			
			session.save(sub);
			session.save(hibBinContent);
			tx.commit();

		} catch (Exception e) {
			logger.error("Error while inserting biobject into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}	
	
	/**
	 * Insert all SubObject and their binary content.
	 * 
	 * @param biobj the biobj
	 * @param subObjectLis the sub object lis
	 * @param session the session
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertAllSubObject(BIObject biobj, List subObjectLis, Session session) throws EMFUserError {
	    logger.debug("IN");
	    Iterator iter=subObjectLis.iterator();
	    while(iter.hasNext()){
		insertSubObject(biobj,(SubObject)iter.next(),session);
	    }
	    logger.debug("OUT");
	}
	/**
	 * Insert a single sub object and their binary content
	 * @param biobj
	 * @param subObject
	 * @param session
	 * @throws EMFUserError
	 */
	private void insertSubObject(BIObject biobj, SubObject subObject, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiSubObjects where subObjId = " + subObject.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
			    	logger.warn("Exist another SbiSubObjects");
				return;
			}
			
			SbiObjects hibBIObj = new SbiObjects(biobj.getId());
			
			SbiBinContents hibBinContent = new SbiBinContents();
			hibBinContent.setId(subObject.getBinaryContentId());
			hibBinContent.setContent(subObject.getContent());
			
			SbiSubObjects sub=new SbiSubObjects();
			sub.setCreationDate(subObject.getCreationDate());
			sub.setDescription(subObject.getDescription());
			sub.setIsPublic(subObject.getIsPublic());
			sub.setName(subObject.getName());
			sub.setOwner(subObject.getOwner());
			sub.setLastChangeDate(subObject.getLastChangeDate());
			sub.setSbiBinContents(hibBinContent);
			sub.setSbiObject(hibBIObj);
			sub.setSubObjId(subObject.getId());
			
			session.save(sub);
			session.save(hibBinContent);
			tx.commit();

		} catch (Exception e) {
			logger.error("Error while inserting biobject into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}	
	
	/**
	 * Insert a biobject into the exported database.
	 * 
	 * @param biobj BIObject to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertBIObject(BIObject biobj, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiObjects where biobjId = " + biobj.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			Engine engine = biobj.getEngine();	
			SbiEngines hibEngine = (SbiEngines)session.load(SbiEngines.class, engine.getId());
			SbiDomains hibState = (SbiDomains)session.load(SbiDomains.class, biobj.getStateID());
			SbiDomains hibObjectType = (SbiDomains)session.load(SbiDomains.class, biobj.getBiObjectTypeID());
			SbiObjects hibBIObj = new SbiObjects(biobj.getId());
			hibBIObj.setSbiEngines(hibEngine); 
			hibBIObj.setDescr(biobj.getDescription());
			hibBIObj.setLabel(biobj.getLabel());
			hibBIObj.setName(biobj.getName());
			hibBIObj.setEncrypt(new Short(biobj.getEncrypt().shortValue()));
			hibBIObj.setRelName(biobj.getRelName());
			hibBIObj.setState(hibState);
			hibBIObj.setStateCode(biobj.getStateCode());
			hibBIObj.setObjectType(hibObjectType);
			hibBIObj.setObjectTypeCode(biobj.getBiObjectTypeCode());
			hibBIObj.setPath(biobj.getPath());
			hibBIObj.setUuid(biobj.getUuid());
			Integer visFlagIn = biobj.getVisible();
			Short visFlagSh = new Short(visFlagIn.toString());
			hibBIObj.setVisible(visFlagSh);
			Integer dataSourceId = biobj.getDataSourceId();
			if (dataSourceId != null) {
				SbiDataSource ds = (SbiDataSource) session.load(SbiDataSource.class, dataSourceId);
				hibBIObj.setDataSource(ds);
			}
			Integer dataSetId = biobj.getDataSetId();
			if (dataSetId != null) {
				SbiDataSetConfig dataset = (SbiDataSetConfig) session.load(SbiDataSetConfig.class, dataSetId);
				hibBIObj.setDataSet(dataset);
			}
			
			hibBIObj.setCreationDate(biobj.getCreationDate());
			hibBIObj.setCreationUser(biobj.getCreationUser());
			hibBIObj.setExtendedDescription(biobj.getExtendedDescription());
			hibBIObj.setKeywords(biobj.getKeywords());
			hibBIObj.setLanguage(biobj.getLanguage());
			hibBIObj.setObjectve(biobj.getObjectve());
			hibBIObj.setRefreshSeconds(biobj.getRefreshSeconds());
			hibBIObj.setProfiledVisibility(biobj.getProfiledVisibility());
			
			session.save(hibBIObj);
			tx.commit();
			ObjTemplate template = biobj.getActiveTemplate();
			if (template == null) {
				logger.warn("Biobject with id = " + biobj.getId() + ", label = " + biobj.getLabel() + " and name = " + biobj.getName() + 
						" has not active template!!");
			} else {
				insertBIObjectTemplate(hibBIObj, template, session);
			}
			
		} catch (Exception e) {
			logger.error("Error while inserting biobject into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	/**
	 * Insert Object Template and Binary Content
	 * @param hibBIObj
	 * @param biobjTempl
	 * @param session
	 * @throws EMFUserError
	 */
	private void insertBIObjectTemplate(SbiObjects hibBIObj,ObjTemplate biobjTempl, Session session) throws EMFUserError {
	    logger.debug("IN");
	    
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiObjTemplates where objTempId = " + biobjTempl.getBiobjId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			
			byte[] template = biobjTempl.getContent();
			
			SbiBinContents hibBinContent = new SbiBinContents();
			SbiObjTemplates hibObjTemplate = new SbiObjTemplates();
			hibObjTemplate.setObjTempId(biobjTempl.getBiobjId());
			hibBinContent.setId(biobjTempl.getBinId());
			hibBinContent.setContent(template);

			
			hibObjTemplate.setActive(new Boolean(true));
			hibObjTemplate.setCreationDate(biobjTempl.getCreationDate());
			hibObjTemplate.setCreationUser(biobjTempl.getCreationUser());
			hibObjTemplate.setDimension(biobjTempl.getDimension());
			hibObjTemplate.setName(biobjTempl.getName());
			hibObjTemplate.setProg(biobjTempl.getProg());
			hibObjTemplate.setSbiBinContents(hibBinContent);
			hibObjTemplate.setSbiObject(hibBIObj);
			
			session.save(hibBinContent);
			session.save(hibObjTemplate);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting biobject into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}	
	
	/**
	 * Insert a BIObject Parameter into the exported database.
	 * 
	 * @param biobjpar BIObject parameter to insert
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertBIObjectParameter(BIObjectParameter biobjpar,  Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiObjPar where objParId = " + biobjpar.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			/*
			Integer parid = biobjpar.getParameter().getId();
			Integer objid = biobj.getId();
			String query = " from SbiObjPar where id.sbiParameters.parId = " + parid +
						   " and id.sbiObjects.biobjId = " + objid;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiObjParId hibBIObjParId = new SbiObjParId();
			SbiParameters hibParameter = (SbiParameters)session.load(SbiParameters.class, parid);
			SbiObjects hibBIObject = (SbiObjects)session.load(SbiObjects.class, objid);
			hibBIObjParId.setSbiObjects(hibBIObject);
			hibBIObjParId.setSbiParameters(hibParameter);
			hibBIObjParId.setProg(new Integer(0));
			*/
			
			// build BI Object Parameter
			SbiObjPar hibBIObjPar = new SbiObjPar(biobjpar.getId());
			hibBIObjPar.setLabel(biobjpar.getLabel());
			hibBIObjPar.setReqFl(new Short(biobjpar.getRequired().shortValue()));
			hibBIObjPar.setModFl(new Short(biobjpar.getModifiable().shortValue()));
			hibBIObjPar.setViewFl(new Short(biobjpar.getVisible().shortValue()));
			hibBIObjPar.setMultFl(new Short(biobjpar.getMultivalue().shortValue()));
			hibBIObjPar.setParurlNm(biobjpar.getParameterUrlName());
			hibBIObjPar.setPriority(biobjpar.getPriority());
			hibBIObjPar.setProg(biobjpar.getProg());
			Integer biobjid = biobjpar.getBiObjectID();
			SbiObjects sbiob = (SbiObjects)session.load(SbiObjects.class, biobjid);
			Integer parid = biobjpar.getParID();			
			SbiParameters sbipar = (SbiParameters)session.load(SbiParameters.class, parid);
			hibBIObjPar.setSbiObject(sbiob);
			hibBIObjPar.setSbiParameter(sbipar);
			// save the BI Object Parameter
			session.save(hibBIObjPar);
			tx.commit();
		
		} catch (Exception e) {
			logger.error("Error while inserting BIObjectParameter into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	
	
	/**
	 * Insert a parameter into the exported database.
	 * 
	 * @param param The param object to insert
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertParameter(Parameter param, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiParameters where parId = " + param.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDomains hibParamType = (SbiDomains)session.load(SbiDomains.class, param.getTypeId());
			SbiParameters hibParam = new SbiParameters(param.getId());
			hibParam.setDescr(param.getDescription());
			hibParam.setLength(new Short(param.getLength().shortValue()));
			hibParam.setLabel(param.getLabel());
			hibParam.setName(param.getName());
			hibParam.setParameterTypeCode(param.getType());
			hibParam.setMask(param.getMask());
			hibParam.setParameterType(hibParamType);
			hibParam.setFunctionalFlag(param.isFunctional() ? new Short((short) 1) : new Short((short) 0));
			hibParam.setTemporalFlag(param.isTemporal() ? new Short((short) 1) : new Short((short) 0));
			session.save(hibParam);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting parameter into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	/**
	 * Insert a parameter use into the exported database.
	 * 
	 * @param parUse The Parameter use object to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertParUse(ParameterUse parUse, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiParuse where useId = " + parUse.getUseID());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiParuse hibParuse = new SbiParuse(parUse.getUseID());
			// Set the relation with parameter
			SbiParameters hibParameters = (SbiParameters)session.load(SbiParameters.class, parUse.getId());
			hibParuse.setSbiParameters(hibParameters);
			// Set the relation with idLov (if the parameter ha a lov related)
			Integer lovId = parUse.getIdLov();
			if(lovId!=null){ 
				SbiLov hibLov = (SbiLov)session.load(SbiLov.class, parUse.getIdLov());
				hibParuse.setSbiLov(hibLov);
			}
			hibParuse.setLabel(parUse.getLabel());
			hibParuse.setName(parUse.getName());
			hibParuse.setDescr(parUse.getDescription());
			hibParuse.setManualInput(parUse.getManualInput());
			hibParuse.setSelectionType(parUse.getSelectionType());
			hibParuse.setMultivalue(parUse.isMultivalue()? new Integer(1): new Integer(0));
			session.save(hibParuse);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting parameter use into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	
	
	/**
	 * Insert Dependencies between parameters.
	 * 
	 * @param biparams the biparams
	 * @param session the session
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertBiParamDepend(List biparams, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Iterator iterBIParams = biparams.iterator();
			while(iterBIParams.hasNext()) {
				BIObjectParameter biparam = (BIObjectParameter)iterBIParams.next();			    
			    IObjParuseDAO objparuseDao = DAOFactory.getObjParuseDAO();
				List objparlist = objparuseDao.loadObjParuses(biparam.getId());
				Iterator iterObjParuse = objparlist.iterator();
				while(iterObjParuse.hasNext()) {
					ObjParuse objparuse = (ObjParuse)iterObjParuse.next();
					Transaction tx = session.beginTransaction();
					Query hibQuery = session.createQuery(" from SbiObjParuse where id.sbiObjPar.objParId = " + objparuse.getObjParId() + 
							                             " and id.sbiParuse.useId = " + objparuse.getParuseId());
					List hibList = hibQuery.list();
					if(!hibList.isEmpty()) {
						continue;
					}
					// built key
					SbiObjParuseId hibObjParuseId = new SbiObjParuseId();
					SbiObjPar hibObjPar = (SbiObjPar)session.load(SbiObjPar.class, objparuse.getObjParId());
					SbiParuse hibParuse = (SbiParuse)session.load(SbiParuse.class, objparuse.getParuseId());
					SbiObjPar objparfather = (SbiObjPar)session.load(SbiObjPar.class, objparuse.getObjParFatherId());
					hibObjParuseId.setSbiObjPar(hibObjPar);
					hibObjParuseId.setSbiParuse(hibParuse);
					hibObjParuseId.setFilterOperation(objparuse.getFilterOperation());
					hibObjParuseId.setSbiObjParFather(objparfather);
					SbiObjParuse hibObjParuse = new SbiObjParuse(hibObjParuseId);
					hibObjParuse.setFilterColumn(objparuse.getFilterColumn());
					hibObjParuse.setProg(objparuse.getProg());
					hibObjParuse.setPreCondition(objparuse.getPreCondition());
					hibObjParuse.setPostCondition(objparuse.getPostCondition());
					hibObjParuse.setLogicOperator(objparuse.getLogicOperator());
					session.save(hibObjParuse);
					tx.commit();	
				}
			}
			    
		} catch (Exception e) {
			logger.error("Error while inserting parameter dependencied into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	
	
	
	
	/**
	 * Insert a list of value into the exported database.
	 * 
	 * @param lov The list of values object to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertLov(ModalitiesValue lov, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiLov where lovId = " + lov.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiLov hibLov = new SbiLov(lov.getId());
			hibLov.setName(lov.getName());
			hibLov.setLabel(lov.getLabel());
			hibLov.setDescr(lov.getDescription());
			SbiDomains inpType = (SbiDomains)session.load(SbiDomains.class, new Integer(lov.getITypeId()));
			hibLov.setInputType(inpType);
			hibLov.setInputTypeCd(lov.getITypeCd());
			hibLov.setLovProvider(lov.getLovProvider());
			session.save(hibLov);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting lov into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	/**
	 * Insert a check into the exported database.
	 * 
	 * @param check The check object to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertCheck(Check check, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiChecks where checkId = " + check.getCheckId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiDomains checkType = (SbiDomains)session.load(SbiDomains.class, check.getValueTypeId());
			SbiChecks hibCheck = new SbiChecks(check.getCheckId());
			hibCheck.setCheckType(checkType);
			hibCheck.setDescr(check.getDescription());
			hibCheck.setName(check.getName());
			hibCheck.setLabel(check.getLabel());
			hibCheck.setValue1(check.getFirstValue());
			hibCheck.setValue2(check.getSecondValue());
			hibCheck.setValueTypeCd(check.getValueTypeCd());
			session.save(hibCheck);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting check into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	/**
	 * Insert an association between a parameter use and a check into the exported database.
	 * 
	 * @param parUse The paruse object which is an element of the association
	 * @param check The check object which is an element of the association
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertParuseCheck(ParameterUse parUse, Check check, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Integer paruseId = parUse.getUseID();
			Integer checkId = check.getCheckId();
			String query = " from SbiParuseCk where id.sbiParuse.useId = " + paruseId +
						   " and id.sbiChecks.checkId = " + checkId;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiParuseCkId hibParuseCkId = new SbiParuseCkId();
			SbiChecks hibChecks = (SbiChecks)session.load(SbiChecks.class, check.getCheckId());
			SbiParuse hibParuse = (SbiParuse)session.load(SbiParuse.class, parUse.getUseID());
			hibParuseCkId.setSbiChecks(hibChecks);
			hibParuseCkId.setSbiParuse(hibParuse);
			SbiParuseCk hibParuseCheck = new SbiParuseCk(hibParuseCkId);
			hibParuseCheck.setProg(new Integer(0));
			session.save(hibParuseCheck);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting paruse and check association into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	/**
	 * Insert an association between a parameter use and a role into the exported database.
	 * 
	 * @param parUse The paruse object which is an element of the association
	 * @param role The role object which is an element of the association
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertParuseRole(ParameterUse parUse, Role role, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Integer paruseId = parUse.getUseID();
			Integer roleId = role.getId();
			String query = " from SbiParuseDet where id.sbiParuse.useId = " + paruseId +
						   " and id.sbiExtRoles.extRoleId = " + roleId;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiParuseDetId hibParuseDetId = new SbiParuseDetId();
			SbiParuse hibParuse = (SbiParuse)session.load(SbiParuse.class, parUse.getUseID());
			SbiExtRoles hibExtRole = (SbiExtRoles)session.load(SbiExtRoles.class, role.getId());
			hibParuseDetId.setSbiExtRoles(hibExtRole);
			hibParuseDetId.setSbiParuse(hibParuse);
			SbiParuseDet hibParuseDet = new SbiParuseDet(hibParuseDetId);
			session.save(hibParuseDet);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting paruse and role association into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	/**
	 * Insert an association between a master report and a subreport.
	 * 
	 * @param sub The subreport
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertSubReportAssociation(Subreport sub, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Integer masterId = sub.getMaster_rpt_id();
			Integer subId = sub.getSub_rpt_id();
			String query = " from SbiSubreports as subreport where " +
					"subreport.id.masterReport.biobjId = " + masterId + " and " +
					"subreport.id.subReport.biobjId = " + subId;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			
			SbiSubreportsId hibSubreportid = new SbiSubreportsId();
			SbiObjects masterReport = (SbiObjects) session.load(SbiObjects.class, sub.getMaster_rpt_id());
			SbiObjects subReport = (SbiObjects) session.load(SbiObjects.class, sub.getSub_rpt_id());
			hibSubreportid.setMasterReport(masterReport);
			hibSubreportid.setSubReport(subReport);
			SbiSubreports hibSubreport = new SbiSubreports(hibSubreportid);
			session.save(hibSubreport);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting subreport " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	
	
	/**
	 * Insert a functionality into the exported database.
	 * 
	 * @param funct Functionality Object to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertFunctionality(LowFunctionality funct, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {			
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiFunctions where funct_id = " + funct.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			IDomainDAO domDAO = DAOFactory.getDomainDAO();
			Domain functTypeDom = domDAO.loadDomainByCodeAndValue("FUNCT_TYPE", funct.getCodType());
			SbiDomains hibFunctType = (SbiDomains)session.load(SbiDomains.class, functTypeDom.getValueId());
			SbiFunctions hibFunct = new SbiFunctions(funct.getId());
			hibFunct.setCode(funct.getCode());
			hibFunct.setDescr(funct.getDescription());
			hibFunct.setFunctTypeCd(funct.getCodType());
			hibFunct.setFunctType(hibFunctType);
			hibFunct.setName(funct.getName());
			hibFunct.setPath(funct.getPath());
			hibFunct.setProg(funct.getProg());
			session.save(hibFunct);
			tx.commit();
			
			Role[] devRoles = funct.getDevRoles();
			Domain devDom = domDAO.loadDomainByCodeAndValue("STATE", "DEV");
			for(int i=0; i<devRoles.length; i++) {
				Role devRole = devRoles[i];
				insertRole(devRole, session);
				insertFunctRole(devRole, funct, devDom.getValueId(), devDom.getValueCd(), session);
			}
			Role[] testRoles = funct.getTestRoles();
			Domain testDom = domDAO.loadDomainByCodeAndValue("STATE", "TEST");
			for(int i=0; i<testRoles.length; i++) {
				Role testRole = testRoles[i];
				insertRole(testRole, session);
				insertFunctRole(testRole, funct, testDom.getValueId(), testDom.getValueCd(), session);
			}
			Role[] execRoles = funct.getExecRoles();
			Domain execDom = domDAO.loadDomainByCodeAndValue("STATE", "REL");
			for(int i=0; i<execRoles.length; i++) {
				Role execRole = execRoles[i];
				insertRole(execRole, session);
				insertFunctRole(execRole, funct, execDom.getValueId(), execDom.getValueCd(), session);
			}
			
		} catch (Exception e) {
			logger.error("Error while inserting Functionality into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}
		
		// recursively insert parent functionalities
		Integer parentId = funct.getParentId();
		if(parentId!=null){
			ILowFunctionalityDAO lowFunctDAO = DAOFactory.getLowFunctionalityDAO();
			LowFunctionality functPar = lowFunctDAO.loadLowFunctionalityByID(parentId, false);
			insertFunctionality(functPar, session);
		}
		logger.debug("OUT");
		
	}
	
	
	
	
	
	/**
	 * Insert a role into the exported database.
	 * 
	 * @param role The role object to export
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertRole(Role role, Session session) throws EMFUserError {
	    logger.debug("IN");
		try {
			Transaction tx = session.beginTransaction();
			Query hibQuery = session.createQuery(" from SbiExtRoles where extRoleId = " + role.getId());
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			SbiExtRoles hibRole = new SbiExtRoles(role.getId());
			hibRole.setCode(role.getCode());
			hibRole.setDescr(role.getDescription());
			hibRole.setName(role.getName());
			SbiDomains roleType = (SbiDomains)session.load(SbiDomains.class, role.getRoleTypeID());
			hibRole.setRoleType(roleType);
			hibRole.setRoleTypeCode(role.getRoleTypeCD());
			hibRole.setIsAbleToSaveIntoPersonalFolder(new Boolean(role.isAbleToSaveIntoPersonalFolder()));
			hibRole.setIsAbleToSaveRememberMe(new Boolean(role.isAbleToSaveRememberMe()));
			hibRole.setIsAbleToSeeMetadata(new Boolean(role.isAbleToSeeMetadata()));
			hibRole.setIsAbleToSeeNotes(new Boolean(role.isAbleToSeeNotes()));;
			hibRole.setIsAbleToSeeSnapshots(new Boolean(role.isAbleToSeeSnapshots()));
			hibRole.setIsAbleToSeeSubobjects(new Boolean(role.isAbleToSeeSubobjects()));
			hibRole.setIsAbleToSeeViewpoints(new Boolean(role.isAbleToSeeViewpoints()));
			hibRole.setIsAbleToSendMail(new Boolean(role.isAbleToSendMail()));
			session.save(hibRole);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting role into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	/**
	 * Insert an association between a functionality and a role into the exported database.
	 * 
	 * @param role The role object which is an element of the association
	 * @param funct The functionality object which is an element of the association
	 * @param stateId The id of the State associated to the couple role / functionality
	 * @param stateCD The code of the State associated to the couple role / functionality
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertFunctRole(Role role, LowFunctionality funct, Integer stateId, String stateCD, Session session) throws EMFUserError {
	    logger.debug("IN");
	    try {
			Transaction tx = session.beginTransaction();
			Integer roleid = role.getId();
			Integer functid = funct.getId();
			String query = " from SbiFuncRole where id.function = " + functid +
						   " and id.role = " + roleid + " and id.state = " + stateId ;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiFuncRoleId hibFuncRoleId = new SbiFuncRoleId();
			SbiFunctions hibFunct = (SbiFunctions)session.load(SbiFunctions.class, funct.getId());
			SbiExtRoles hibRole = (SbiExtRoles)session.load(SbiExtRoles.class, role.getId());
			SbiDomains hibState = (SbiDomains)session.load(SbiDomains.class, stateId);
			hibFuncRoleId.setFunction(hibFunct);
			hibFuncRoleId.setRole(hibRole);
			hibFuncRoleId.setState(hibState);
			SbiFuncRole hibFunctRole = new SbiFuncRole(hibFuncRoleId);
			hibFunctRole.setStateCd(stateCD);
			session.save(hibFunctRole);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting function and role association into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}
	
	
	
	/**
	 * Insert an association between a functionality and a biobject into the exported database.
	 * 
	 * @param biobj The BIObject which is an element of the association
	 * @param funct The functionality object which is an element of the association
	 * @param session Hibernate session for the exported database
	 * 
	 * @throws EMFUserError the EMF user error
	 */
	public void insertObjFunct(BIObject biobj, LowFunctionality funct, Session session) throws EMFUserError {
	    logger.debug("IN");
	    try {
			Transaction tx = session.beginTransaction();
			Integer biobjid = biobj.getId();
			Integer functid = funct.getId();
			String query = " from SbiObjFunc where id.sbiFunctions = " + functid +
						   " and id.sbiObjects = " + biobjid;
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				return;
			}
			// built key
			SbiObjFuncId hibObjFunctId = new SbiObjFuncId();
			SbiFunctions hibFunct = (SbiFunctions)session.load(SbiFunctions.class, funct.getId());
			SbiObjects hibObj = (SbiObjects)session.load(SbiObjects.class, biobj.getId());
			hibObjFunctId.setSbiObjects(hibObj);
			hibObjFunctId.setSbiFunctions(hibFunct);
			SbiObjFunc hibObjFunct = new SbiObjFunc(hibObjFunctId);
			hibObjFunct.setProg(new Integer(0));
			session.save(hibObjFunct);
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting function and object association into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		}finally{
		    logger.debug("OUT");
		}
	}

	/**
	 * Exports the map catalogue (maps and features)
	 * 
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	public void insertMapCatalogue(Session session) throws EMFUserError {
	    logger.debug("IN");
	    try {
	    	// controls if the maps are already inserted into export db
			Transaction tx = session.beginTransaction();
			String query = " from SbiGeoMaps";
			Query hibQuery = session.createQuery(query);
			List hibList = hibQuery.list();
			if(!hibList.isEmpty()) {
				// maps are already exported
				return;
			}
			tx.commit();
			
			insertMaps(session);
			insertFeatures(session);
			insertMapFeaturesAssociations(session);
			
		} catch (Exception e) {
			logger.error("Error while inserting map catalogue into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		} finally{
		    logger.debug("OUT");
		}
		
	}
	
	/**
	 * Insert the maps of the maps catalogue
	 * 
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	private void insertMaps(Session session) throws EMFUserError {
	    logger.debug("IN");
	    try {
			Transaction tx = session.beginTransaction();
			ISbiGeoMapsDAO mapsDAO = DAOFactory.getSbiGeoMapsDAO();
			List allMaps = mapsDAO.loadAllMaps();
			Iterator mapsIt = allMaps.iterator();
			while (mapsIt.hasNext()) {
				GeoMap map = (GeoMap) mapsIt.next();
				SbiGeoMaps hibMap = new SbiGeoMaps(map.getMapId());
				hibMap.setDescr(map.getDescr());
				hibMap.setFormat(map.getFormat());
				hibMap.setName(map.getName());
				hibMap.setUrl(map.getUrl());
				
				if (map.getBinId() == 0) {
					logger.warn("Map with id = " + map.getMapId() + " and name = " + map.getName() + 
							" has not binary content!!");
					hibMap.setBinContents(null);
				} else {
					SbiBinContents hibBinContent = new SbiBinContents();
					hibBinContent.setId(map.getBinId());
					byte[] content = DAOFactory.getBinContentDAO().getBinContent(map.getBinId());
					hibBinContent.setContent(content);
					hibMap.setBinContents(hibBinContent);
					
					session.save(hibBinContent);
				}
				
				session.save(hibMap);

			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting maps into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		} finally{
		    logger.debug("OUT");
		}
	}
	
	/**
	 * Insert the features of the maps catalogue
	 * 
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	private void insertFeatures(Session session) throws EMFUserError {
	    logger.debug("IN");
	    try {
			Transaction tx = session.beginTransaction();
			ISbiGeoFeaturesDAO featuresDAO = DAOFactory.getSbiGeoFeaturesDAO();
			List allFeatures = featuresDAO.loadAllFeatures();
			Iterator featureIt = allFeatures.iterator();
			while (featureIt.hasNext()) {
				GeoFeature feature = (GeoFeature) featureIt.next();
				SbiGeoFeatures hibFeature = new SbiGeoFeatures(feature.getFeatureId());
				hibFeature.setDescr(feature.getDescr());
				hibFeature.setName(feature.getName());
				hibFeature.setType(feature.getType());
				session.save(hibFeature);
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting features into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		} finally{
		    logger.debug("OUT");
		}
	}
	
	/**
	 * Insert the association between maps and features of the maps catalogue
	 * 
	 * @param session Hibernate session for the exported database
	 * @throws EMFUserError
	 */
	private void insertMapFeaturesAssociations(Session session) throws EMFUserError {
	    logger.debug("IN");
	    try {
			Transaction tx = session.beginTransaction();
			ISbiGeoMapsDAO mapsDAO = DAOFactory.getSbiGeoMapsDAO();
			List allMaps = mapsDAO.loadAllMaps();
			ISbiGeoMapFeaturesDAO mapFeaturesDAO = DAOFactory.getSbiGeoMapFeaturesDAO();
			Iterator mapsIt = allMaps.iterator();
			while (mapsIt.hasNext()) {
				GeoMap map = (GeoMap) mapsIt.next();
				List mapFeatures = mapFeaturesDAO.loadFeaturesByMapId(new Integer(map.getMapId()));
				Iterator mapFeaturesIt = mapFeatures.iterator();
				while (mapFeaturesIt.hasNext()) {
					GeoFeature feature = (GeoFeature) mapFeaturesIt.next();
					GeoMapFeature mapFeature = mapFeaturesDAO.loadMapFeatures(new Integer(map.getMapId()), new Integer(feature.getFeatureId()));
					SbiGeoMapFeatures hibMapFeature = new SbiGeoMapFeatures();	
					SbiGeoMapFeaturesId hibMapFeatureId = new SbiGeoMapFeaturesId();			
					hibMapFeatureId.setMapId(mapFeature.getMapId());
					hibMapFeatureId.setFeatureId(mapFeature.getFeatureId());
					hibMapFeature.setId(hibMapFeatureId);
					hibMapFeature.setSvgGroup(mapFeature.getSvgGroup());
					hibMapFeature.setVisibleFlag(mapFeature.getVisibleFlag());
					session.save(hibMapFeature);
				}
			}
			tx.commit();
		} catch (Exception e) {
			logger.error("Error while inserting association between maps and features into export database " , e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "8005", "component_impexp_messages");
		} finally{
		    logger.debug("OUT");
		}
	}

}
