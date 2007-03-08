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
package it.eng.spagobi.bo.dao.hibernate;


import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Check;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IParameterUseDAO;
import it.eng.spagobi.metadata.SbiChecks;
import it.eng.spagobi.metadata.SbiExtRoles;
import it.eng.spagobi.metadata.SbiLov;
import it.eng.spagobi.metadata.SbiParameters;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.metadata.SbiParuseCk;
import it.eng.spagobi.metadata.SbiParuseCkId;
import it.eng.spagobi.metadata.SbiParuseDet;
import it.eng.spagobi.metadata.SbiParuseDetId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Defines the Hibernate implementations for all DAO methods,
 * for a parameter use mode.
 * 
 * @author zoppello
 */
public class ParameterUseDAOHibImpl extends AbstractHibernateDAO implements
		IParameterUseDAO {

	
	/** 
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#loadById(java.lang.Integer)
	 */
	public SbiParuse loadById(Integer id) throws EMFUserError {
		SbiParuse toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try{
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = (SbiParuse)aSession.load(SbiParuse.class, id);
			tx.commit();
		}catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return toReturn;
	}
	
	
	/** 
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#loadByUseID(java.lang.Integer)
	 */
	public ParameterUse loadByUseID(Integer useID) throws EMFUserError {
		ParameterUse toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try{
			aSession = getSession();
			tx = aSession.beginTransaction();
		
		
			SbiParuse hibParuse = (SbiParuse)aSession.load(SbiParuse.class,  useID);
			
			toReturn = toParameterUse(hibParuse);
			tx.commit();
			
		}catch(HibernateException he){
			logException(he);
			
			if (tx != null) tx.rollback();	

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		
		return toReturn;
		
		
	}

	

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#fillAssociatedChecksForParUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void fillAssociatedChecksForParUse(ParameterUse aParameterUse)
			throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		Integer useID = aParameterUse.getUseID();
		
		try{
			aSession = getSession();
			tx = aSession.beginTransaction();
		
		
			SbiParuse hibParuse = (SbiParuse)aSession.load(SbiParuse.class,  useID);
			
			fillParameterUse(aParameterUse, hibParuse);
			
			tx.commit();
			
		}catch(HibernateException he){
			logException(he);
			
			if (tx != null) tx.rollback();	

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#fillRolesForParUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void fillRolesForParUse(ParameterUse aParameterUse)
			throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		Integer useID = aParameterUse.getUseID();
		
		try{
			aSession = getSession();
			tx = aSession.beginTransaction();
		
		
			SbiParuse hibParuse = (SbiParuse)aSession.load(SbiParuse.class,  useID);
			
			fillParameterUse(aParameterUse, hibParuse);
			
			tx.commit();
			
		}catch(HibernateException he){
			logException(he);
			
			if (tx != null) tx.rollback();	

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}

	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#modifyParameterUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void modifyParameterUse(ParameterUse aParameterUse)
			throws EMFUserError {

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			SbiParuse hibParuse = (SbiParuse) aSession.load(SbiParuse.class,
					aParameterUse.getUseID());

			hibParuse.setLabel(aParameterUse.getLabel());
			hibParuse.setName(aParameterUse.getName());
			hibParuse.setDescr(aParameterUse.getDescription());
			hibParuse.setSelectionType(aParameterUse.getSelectionType());
			hibParuse.setMultivalue(aParameterUse.isMultivalue()? new Integer(1): new Integer(0));
			hibParuse.setManualInput(aParameterUse.getManualInput());
			
			SbiLov hibSbiLov = (SbiLov)aSession.load(SbiLov.class, aParameterUse.getIdLov());
            //if the lov id is 0 (-1) then the modality is manual input
			//insert into the DB a null lov_id
			//if the user selected modality is manual input,and before it was a 
			//lov, we don't need a lov_id and so we can delete it
			if(hibSbiLov.getLovId().intValue()== -1 || aParameterUse.getManualInput().intValue() == 1){
			hibParuse.setSbiLov(null);
			}
			else{
			hibParuse.setSbiLov(hibSbiLov);}
			
			//hibParuse.setSbiLov(hibSbiLov);

			Set parUseDets = hibParuse.getSbiParuseDets();
			for (Iterator it = parUseDets.iterator(); it.hasNext();) {
				aSession.delete((SbiParuseDet) it.next());
			}

			Set parUseCks = hibParuse.getSbiParuseCks();
			for (Iterator it = parUseCks.iterator(); it.hasNext();) {
				aSession.delete((SbiParuseCk) it.next());
			}

			// Recreate Relations with sbi_paruse_det
			List newRoles = aParameterUse.getAssociatedRoles();
			SbiParuseDet hibParUseDet = null;
			SbiParuseDetId hibParUseDetId = null;

			SbiExtRoles tmpExtRole = null;
			Set parUseDetsToSave = new HashSet();
			for (int i = 0; i < newRoles.size(); i++) {
				hibParUseDetId = new SbiParuseDetId();
				hibParUseDetId.setSbiParuse(hibParuse);
				tmpExtRole = (SbiExtRoles) aSession.load(SbiExtRoles.class,
						((Role) newRoles.get(i)).getId());
				hibParUseDetId.setSbiExtRoles(tmpExtRole);
				hibParUseDet = new SbiParuseDet(hibParUseDetId);
				aSession.save(hibParUseDet);
				parUseDetsToSave.add(hibParUseDet);
			}

			hibParuse.getSbiParuseDets();
			hibParuse.setSbiParuseDets(parUseDetsToSave);

			// Recreate Relations with sbi_paruse_ck
			List newChecks = aParameterUse.getAssociatedChecks();
			SbiParuseCk hibParUseCk = null;
			SbiParuseCkId hibParUseCkId = null;

			SbiChecks tmpCheck = null;
			Set parUseCkToSave = new HashSet();
			for (int i = 0; i < newChecks.size(); i++) {
				hibParUseCkId = new SbiParuseCkId();
				hibParUseCkId.setSbiParuse(hibParuse);
				tmpCheck = (SbiChecks) aSession.load(SbiChecks.class,
						((Check) newChecks.get(i)).getCheckId());
				hibParUseCkId.setSbiChecks(tmpCheck);
				hibParUseCk = new SbiParuseCk(hibParUseCkId);
				aSession.save(hibParUseCk);
				parUseCkToSave.add(hibParUseCk);
			}
			
			hibParuse.getSbiParuseCks();
			hibParuse.setSbiParuseCks(parUseCkToSave);
			tx.commit();
			
		}catch(HibernateException he){
			logException(he);
			
			if (tx != null) tx.rollback();	

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#insertParameterUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void insertParameterUse(ParameterUse aParameterUse)
			throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try{
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiParuse hibParuse = new SbiParuse();
			// Set the relation with parameter
			SbiParameters hibParameters = (SbiParameters)aSession.load(SbiParameters.class, aParameterUse.getId());
			hibParuse.setSbiParameters(hibParameters);
			
			// Set the relation with idLov 
			SbiLov hibLov = (SbiLov)aSession.load(SbiLov.class, aParameterUse.getIdLov());
			//if the lov id is 0 (-1) then the modality is manual input
			//insert into the DB a null lov_id
			if(hibLov.getLovId().intValue()== -1)
			{hibParuse.setSbiLov(null);
			}
			else{
			hibParuse.setSbiLov(hibLov);}
			
			hibParuse.setLabel(aParameterUse.getLabel());
			hibParuse.setName(aParameterUse.getName());
			hibParuse.setDescr(aParameterUse.getDescription());
			hibParuse.setSelectionType(aParameterUse.getSelectionType());
			hibParuse.setMultivalue(aParameterUse.isMultivalue()? new Integer(1): new Integer(0));
			hibParuse.setManualInput(aParameterUse.getManualInput());
			Integer useId = (Integer)aSession.save(hibParuse);
			
			
			hibParuse = (SbiParuse)aSession.load(SbiParuse.class, useId);
			// Recreate Relations with sbi_paruse_det
			List newRoles = aParameterUse.getAssociatedRoles();
			SbiParuseDet hibParUseDet = null;
			SbiParuseDetId hibParUseDetId = null;
			
			SbiExtRoles tmpExtRole = null;
			Set parUseDetsToSave = new HashSet();
			for (int i=0; i < newRoles.size(); i++){
					hibParUseDetId = new SbiParuseDetId();
					hibParUseDetId.setSbiParuse(hibParuse);
					tmpExtRole = (SbiExtRoles)aSession.load(SbiExtRoles.class, ((Role)newRoles.get(i)).getId());
					hibParUseDetId.setSbiExtRoles(tmpExtRole);
					hibParUseDet = new SbiParuseDet(hibParUseDetId);
					aSession.save(hibParUseDet);
					parUseDetsToSave.add(hibParUseDet);
			}
			
			hibParuse.setSbiParuseDets(parUseDetsToSave);
			
			
//			 Recreate Relations with sbi_paruse_ck
			List newChecks = aParameterUse.getAssociatedChecks();
			SbiParuseCk hibParUseCk = null;
			SbiParuseCkId hibParUseCkId = null;
			
			SbiChecks tmpCheck = null;
			Set parUseCkToSave = new HashSet();
			for (int i=0; i < newChecks.size(); i++){
					hibParUseCkId = new SbiParuseCkId();
					hibParUseCkId.setSbiParuse(hibParuse);
					tmpCheck = (SbiChecks)aSession.load(SbiChecks.class, ((Check)newChecks.get(i)).getCheckId());
					hibParUseCkId.setSbiChecks(tmpCheck);
					hibParUseCk = new SbiParuseCk(hibParUseCkId);
					aSession.save(hibParUseCk);
					parUseCkToSave.add(hibParUseCk);
			}
			hibParuse.setSbiParuseCks(parUseCkToSave);
			
			tx.commit();
		
		}catch(HibernateException he){
			logException(he);
			
			if (tx != null) tx.rollback();	

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#eraseParameterUse(it.eng.spagobi.bo.ParameterUse)
	 */
	public void eraseParameterUse(ParameterUse aParameterUse)
			throws EMFUserError {

		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiParuse hibParuse = (SbiParuse)aSession.load(SbiParuse.class, aParameterUse.getUseID());
			
			
			Set parUseDets = hibParuse.getSbiParuseDets();
			for (Iterator it = parUseDets.iterator(); it.hasNext();){
				aSession.delete((SbiParuseDet)it.next());	
			}
			
			Set parUseCks = hibParuse.getSbiParuseCks();
			for (Iterator it = parUseCks.iterator(); it.hasNext();){
				aSession.delete((SbiParuseCk)it.next());	
			}
			
			aSession.delete(hibParuse);
			
			tx.commit();
		}catch(HibernateException he){
			
			logException(he);
			
			if (tx != null) tx.rollback();	

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#hasParUseModes(java.lang.String)
	 */
	public boolean hasParUseModes(String parId) throws EMFUserError {
		List parameterUseForParameter = loadParametersUseByParId(Integer.valueOf(parId));
		
		if (parameterUseForParameter.size() > 0) 
			return true;
		else
			return false;
	}

	
	/**
	 * @see it.eng.spagobi.bo.dao.IParameterUseDAO#loadParametersUseByParId(java.lang.Integer)
	 */
	public List loadParametersUseByParId(Integer parId) throws EMFUserError {
		List realResult = new ArrayList();

		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = "from SbiParuse s where s.sbiParameters.parId="+parId;
			Query query = aSession.createQuery(hql);
			List result = query.list();
			
			Iterator it = result.iterator();
			while (it.hasNext()){
				realResult.add(toParameterUse((SbiParuse)it.next()));
			}
			
			tx.commit();
			
		}catch(HibernateException he){
			
			logException(he);
			
			if (tx != null) tx.rollback();	
			
			System.out.println(he.toString());

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		
		}finally{
			
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		
		}
		
		return realResult;
	}

	
	/**
	 * From the hibernate Parameter Use mode  at input, gives
	 * the corrispondent <code>ParameterUse</code> object.
	 * 
	 * @param hibParUse The hybernate parameter use mode
	 * @return The corrispondent <code>ParameterUse</code> object
	 */
	public ParameterUse toParameterUse(SbiParuse hibParUse){
			ParameterUse aParameterUse = new ParameterUse();
			fillParameterUse(aParameterUse, hibParUse);
			return aParameterUse;
	}
	
	public void fillParameterUse(ParameterUse aParameterUse, SbiParuse hibParUse){
			aParameterUse.setUseID(hibParUse.getUseId());
			aParameterUse.setDescription(hibParUse.getDescr());
			aParameterUse.setId(hibParUse.getSbiParameters().getParId());
			aParameterUse.setLabel(hibParUse.getLabel());
			aParameterUse.setName(hibParUse.getName());
			
			aParameterUse.setSelectionType(hibParUse.getSelectionType());
			aParameterUse.setMultivalue(hibParUse.getMultivalue() != null && hibParUse.getMultivalue().intValue() > 0);
			
			
			//if the sbi_lov is null, then we have a man in modality
			if(hibParUse.getSbiLov()==null){
				aParameterUse.setIdLov(null);
			}
			else{
			aParameterUse.setIdLov(hibParUse.getSbiLov().getLovId());
			}
			aParameterUse.setManualInput(hibParUse.getManualInput());
			
			Set hibParUseCheks = hibParUse.getSbiParuseCks();
			SbiParuseCk aSbiParuseCk = null; 
			CheckDAOHibImpl checkDAOHibImpl = new CheckDAOHibImpl();
			Check tmpCheck = null;
			
			List checkList = new ArrayList();
			for (Iterator itParUseCk = hibParUseCheks.iterator(); itParUseCk.hasNext();){
				aSbiParuseCk = (SbiParuseCk)itParUseCk.next();
				tmpCheck = checkDAOHibImpl.toCheck(aSbiParuseCk.getId().getSbiChecks());
				checkList.add(tmpCheck);
			}
			
			
			Set hibParUseDets = hibParUse.getSbiParuseDets();
			SbiParuseDet aSbiParuseDet = null; 
			RoleDAOHibImpl roleDAOHibImpl = new RoleDAOHibImpl();
			Role tmpRole = null;
			
			List roleList = new ArrayList();
			for (Iterator itParUseDet = hibParUseDets.iterator(); itParUseDet.hasNext();){
				aSbiParuseDet = (SbiParuseDet)itParUseDet.next();
				tmpRole = roleDAOHibImpl.toRole(aSbiParuseDet.getId().getSbiExtRoles());
				roleList.add(tmpRole);
			}
			
			aParameterUse.setAssociatedChecks(checkList);
			aParameterUse.setAssociatedRoles(roleList);
			
			}
	public void eraseParameterUseByParId(Integer parId) throws EMFUserError {
		List parUseList = null;
		IParameterUseDAO parUseDAO = DAOFactory.getParameterUseDAO();
		parUseList = parUseDAO.loadParametersUseByParId(parId);
		Iterator i = parUseList.iterator();
		while (i.hasNext()){
			ParameterUse parUse = (ParameterUse) i.next();
			parUseDAO.eraseParameterUse(parUse);
			
			
		}
}



	public List getParameterUsesAssociatedToLov(Integer lovId) throws EMFUserError {
		List realResult = new ArrayList();

		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = "from SbiParuse s where s.sbiLov.lovId="+lovId;
			Query query = aSession.createQuery(hql);
			List result = query.list();
			
			Iterator it = result.iterator();
			while (it.hasNext()){
				realResult.add(toParameterUse((SbiParuse)it.next()));
			}
			
			tx.commit();
			
		} catch(HibernateException he) {
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		
		return realResult;
	}


}