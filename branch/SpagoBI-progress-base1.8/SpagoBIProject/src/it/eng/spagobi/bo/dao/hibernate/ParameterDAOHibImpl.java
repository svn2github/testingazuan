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
/*
 * Created on 22-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.bo.dao.hibernate;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IParameterDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseDet;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

/**
 * Defines the Hibernate implementations for all DAO methods,
 * for a parameter
 * 
 * @author zoppello
 */
public class ParameterDAOHibImpl extends AbstractHibernateDAO implements
		IParameterDAO {

	/** 
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#loadForDetailByParameterID(java.lang.Integer)
	 */
	public Parameter loadForDetailByParameterID(Integer parameterID)throws EMFUserError{
		Parameter toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
	
			SbiParameters hibParameters = (SbiParameters)aSession.load(SbiParameters.class,  parameterID);
		
			toReturn = toParameter(hibParameters);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return toReturn;
	}

	
	
	
	
	/** 
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#loadForExecutionByParameterIDandRoleName(java.lang.Integer, java.lang.String)
	 */
	public Parameter loadForExecutionByParameterIDandRoleName(
			Integer parameterID, String roleName) throws EMFUserError {
		/*
		Parameter parameter = null;
		SQLCommand cmdSelect = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;
		ArrayList elements = new ArrayList(1); 
		DataConnection dataConnection = null;
//		 LOAD THE LOV FOR THE MODALITY
		String useIdStr = null;
		String idLovStr = null;
		
		try {
			parameter = loadForDetailByParameterID(parameterID);
			
			Role role = DAOFactory.getRoleDAO().loadByName(roleName);
			 
			dataConnection = DataConnectionManager.getInstance().getConnection("spagobi");
			
			// FIND THE MODALITY FOR PAR_ID AND ROLE NAME
			String strSql = SQLStatements.getStatement("SELECT_PARAMETER_USE_FROM_IDPAR_IDROLE");
			DataField dataField = dataConnection.createDataField("PAR_ID", Types.DECIMAL, parameter.getId());
			elements.add(dataField);
			dataField = dataConnection.createDataField("EXT_ROLE_ID", Types.DECIMAL, role.getId());
			elements.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			dr = cmdSelect.execute(elements);
			sdr = (ScrollableDataResult) dr.getDataObject();
			
			
			
			if(sdr.hasRows()) {
				DataRow row = sdr.getDataRow(1);
				useIdStr = row.getColumn("USE_ID").getObjectValue().toString();
				idLovStr = row.getColumn("LOV_ID").getObjectValue().toString();
					
				
				
			}
			
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "ParameterDAOImpl", "load", "Cannot recover detail information", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		}
		
		ModalitiesValue modVal  = DAOFactory.getModalitiesValueDAO().loadModalitiesValueByID(Integer.valueOf(idLovStr));
		parameter.setModalityValue(modVal);
		
		//LOAD THE CHECKS FOR THE MODALITY
		ParameterUse aParameterUse = DAOFactory.getParameterUseDAO().loadByUseID(new Integer(useIdStr));
		
		// Carico i Checks 
		parameter.setChecks(aParameterUse.getAssociatedChecks());
		return parameter;  
		*/
		Query hqlQuery = null;
		String hql = null;
		Session aSession = null;
		Transaction tx = null;
		Parameter parameter = null;
		
		try{
		
			parameter = loadForDetailByParameterID(parameterID);
			Role role = DAOFactory.getRoleDAO().loadByName(roleName);
			
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion domainCdCriterrion = null;
			Criteria criteria = null;
			
			// load all the paruse with the given parmaeter id
			domainCdCriterrion = Expression.eq("sbiParameters.parId", parameter.getId());
			criteria = aSession.createCriteria(SbiParuse.class);
			criteria.add(domainCdCriterrion);
			List paruses = criteria.list();
			
			/*
			SELECT 
			PARUSE.USE_ID AS USE_ID,
			PARUSE.LOV_ID AS LOV_ID
			FROM 
				SBI_PARUSE PARUSE,
				SBI_PARUSE_DET PARUSEDET
				WHERE 
			PARUSE.PAR_ID = ? 
			AND PARUSEDET.EXT_ROLE_ID = ?
			AND PARUSE.USE_ID = PARUSEDET.USE_ID"/>  
*/
			List parUseAssociated = new ArrayList();
			Iterator parusesIter = paruses.iterator();
			while(parusesIter.hasNext()) {
				SbiParuse hibParuse = (SbiParuse)parusesIter.next();
				Iterator paruseDetsIter = hibParuse.getSbiParuseDets().iterator();
				
				
				while(paruseDetsIter.hasNext()){
					SbiParuseDet hibParuseDet = (SbiParuseDet)paruseDetsIter.next();
					if (hibParuseDet.getId().getSbiExtRoles().getExtRoleId().equals(role.getId())){
						parUseAssociated.add(hibParuse);
					}
				}
			}
		
			
			if(parUseAssociated.size() == 1) {
				SbiParuse hibParuse = (SbiParuse)parUseAssociated.get(0);
				SbiLov sbiLov = hibParuse.getSbiLov();
				
				//if modval is null, then the parameter always has a man_in modality
				//force the man_in modality to the parameter
				Integer man_in = hibParuse.getManualInput();
				//Integer sbiLovId = sbiLov.getLovId();
				if(man_in.intValue() == 1){
					ModalitiesValue manInModVal = new ModalitiesValue();
					manInModVal.setITypeCd("MAN_IN");
					manInModVal.setITypeId("37");
					parameter.setModalityValue(manInModVal);
					
				}else{
				ModalitiesValue modVal  = DAOFactory.getModalitiesValueDAO().loadModalitiesValueByID(hibParuse.getSbiLov().getLovId());
				parameter.setModalityValue(modVal);
				}
				ParameterUse aParameterUse = DAOFactory.getParameterUseDAO().loadByUseID(hibParuse.getUseId());
				parameter.setChecks(aParameterUse.getAssociatedChecks());  
			} else {
				// this part of code wouldn't never be executed because one role can have only one parameteruse
				// for each parameter. The control is executed before the load of the object so 
				// the list would have to contain only one element but if the list contains more than one
				// object it's an error
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
								    "ParameterDAOHibImpl", 
								    "loadForExecutionByParameterIDandRoleName", 
								    "the parameter with id "+parameterID+" has more than one parameteruse for the role "+roleName);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			tx.commit();
			return parameter;
			
			
			
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (EMFUserError emfue) {
			if (tx != null)
				tx.rollback();
			throw emfue;
		} finally {
			if(aSession!=null) {
				if (aSession.isOpen()) aSession.close();
			}
		}
		
		
		
		
		
		
		
	}

	
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#loadAllParameters()
	 */
	public List loadAllParameters() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiParameters");
			List hibList = hibQuery.list();

			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toParameter((SbiParameters) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return realResult;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#modifyParameter(it.eng.spagobi.bo.Parameter)
	 */
	public void modifyParameter(Parameter aParameter) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String info = aParameter.getModality();
			StringTokenizer st; 
			String token = null;
			st = new StringTokenizer (info, ",", false);
			String input_type_cd = st.nextToken();
			String input_type_id = st.nextToken();
			
			Integer typeId = Integer.valueOf(input_type_id);
			SbiDomains parameterType = (SbiDomains)aSession.load(SbiDomains.class, typeId); 
			
			SbiParameters hibParameters = (SbiParameters)aSession.load(SbiParameters.class,  aParameter.getId());
			hibParameters.setDescr(aParameter.getDescription());
			hibParameters.setLength(new Short(aParameter.getLength().shortValue()));
			hibParameters.setLabel(aParameter.getLabel());
			
			hibParameters.setName(aParameter.getName());
			
			hibParameters.setParameterTypeCode(input_type_cd);
			hibParameters.setMask(aParameter.getMask());
			hibParameters.setParameterType(parameterType);
			
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	
	}
		
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#insertParameter(it.eng.spagobi.bo.Parameter)
	 */
	public void insertParameter(Parameter aParameter) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String info = aParameter.getModality();
			StringTokenizer st;
			String token = null;
			st = new StringTokenizer(info, ",", false);
			String input_type_cd = st.nextToken();
			String input_type_id = st.nextToken();

			Integer typeId = Integer.valueOf(input_type_id);
			SbiDomains parameterType = (SbiDomains) aSession.load(
					SbiDomains.class, typeId);

			SbiParameters hibParameters = new SbiParameters();
			hibParameters.setDescr(aParameter.getDescription());
			hibParameters.setLength(new Short(aParameter.getLength()
					.shortValue()));
			hibParameters.setLabel(aParameter.getLabel());
			hibParameters.setName(aParameter.getName());
			hibParameters.setParameterTypeCode(input_type_cd);
			hibParameters.setMask(aParameter.getMask());
			hibParameters.setParameterType(parameterType);
			aSession.save(hibParameters);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterDAO#eraseParameter(it.eng.spagobi.bo.Parameter)
	 */
	public void eraseParameter(Parameter aParameter) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
				
				SbiParameters hibParameters = (SbiParameters)aSession.load(SbiParameters.class,  aParameter.getId());
				aSession.delete(hibParameters);
				
				tx.commit();

			} catch (HibernateException he) {
				logException(he);

				if (tx != null)
					tx.rollback();

				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

			} finally {
				if (aSession!=null){
					if (aSession.isOpen()) aSession.close();
				}
			}
	}
	
	/**
	 * From the hibernate parametes at input, gives
	 * the corrispondent <code>Parameter</code> object.
	 * 
	 * @param hibParameters The hybernate parameter
	 * @return The corrispondent <code>Parameter</code> object
	 */
	public Parameter toParameter(SbiParameters hibParameters){
		Parameter aParameter = new Parameter();
		
		
		aParameter.setDescription(hibParameters.getDescr());
		aParameter.setId(hibParameters.getParId());
		aParameter.setLabel(hibParameters.getLabel());
		aParameter.setName(hibParameters.getName());
		aParameter.setLength(new Integer(hibParameters.getLength().intValue()));
		aParameter.setMask(hibParameters.getMask());
		aParameter.setType(hibParameters.getParameterTypeCode());
		aParameter.setTypeId(hibParameters.getParameterType().getValueId());
		
		return aParameter;
	}
}
