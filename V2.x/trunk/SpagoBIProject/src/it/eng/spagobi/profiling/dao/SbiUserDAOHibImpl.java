package it.eng.spagobi.profiling.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.profiling.bean.SbiAttribute;
import it.eng.spagobi.profiling.bean.SbiExtUserRoles;
import it.eng.spagobi.profiling.bean.SbiExtUserRolesId;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bean.SbiUserAttributes;
import it.eng.spagobi.profiling.bean.SbiUserAttributesId;
import it.eng.spagobi.profiling.bo.UserBO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SbiUserDAOHibImpl extends AbstractHibernateDAO implements ISbiUserDAO{
	static private Logger logger = Logger.getLogger(SbiUserDAOHibImpl.class);
	/**
	 * Load SbiUser by userId.
	 * 
	 * @param userId the user id	/**
	 * Load SbiUser by id.
	 * 
	 * @param id the bi object id
	 * 
	 * @return the BI object
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 */
	public Integer loadByUserId(String userId) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String q = "from SbiUser us where us.userId = :userId";
			Query query = aSession.createQuery(q);
			query.setString("userId", userId);
			
			SbiUser user = (SbiUser)query.uniqueResult();			
			
			if(user != null)
				return Integer.valueOf(user.getId());
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return null;
	} 
	
	/**
	 * Load SbiUser by id.
	 * 
	 * @param id the identifier	/**
	 * Load SbiUser by id.
	 * 
	 * @param id the bi object id
	 * 
	 * @return the BI object
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 */
	public SbiUser loadSbiUserById(Integer id) throws EMFUserError {
		logger.debug("IN");
		SbiUser toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String q = "from SbiUser us where us.id = :id";
			Query query = aSession.createQuery(q);
			query.setInteger("id", id);
			toReturn = (SbiUser)query.uniqueResult();
			Hibernate.initialize(toReturn);
			Hibernate.initialize(toReturn.getSbiExtUserRoleses());
			Hibernate.initialize(toReturn.getSbiUserAttributeses());
			for(SbiUserAttributes current : toReturn.getSbiUserAttributeses() ){
				Hibernate.initialize(current.getSbiAttribute());
			}
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		logger.debug("OUT");
		return toReturn;
	} 

	/**Insert SbiUser
	 * @param user
	 * @throws EMFUserError
	 */
	public Integer saveSbiUser(SbiUser user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer id = (Integer)aSession.save(user);
			
			tx.commit();
			return id;
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}
	
	/**Update SbiUser
	 * @param user
	 * @throws EMFUserError
	 */
	public void updateSbiUser(SbiUser user, Integer userID) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			aSession.update(user, userID);
			aSession.flush();
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	public void updateSbiUserAttributes(SbiUserAttributes attribute)
			throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			aSession.saveOrUpdate(attribute);
			aSession.flush();
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	public void updateSbiUserRoles(SbiExtUserRoles role) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			aSession.saveOrUpdate(role);
			aSession.flush();
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}


	public SbiUser loadSbiUserByUserId(String userId) throws EMFUserError {
		logger.debug("IN");

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String q = "from SbiUser us where us.userId = :userId";
			Query query = aSession.createQuery(q);
			query.setString("userId", userId);
			
			SbiUser user = (SbiUser)query.uniqueResult();
			tx.commit();
			return user;
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		

	}

	public ArrayList<SbiUserAttributes> loadSbiUserAttributesById(Integer id) throws EMFUserError {
		logger.debug("IN");

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String q = "select us.sbiUserAttributeses from SbiUser us where us.id = :id";

			Query query = aSession.createQuery(q);
			query.setInteger("id", id);
			
			ArrayList<SbiUserAttributes> result = (ArrayList<SbiUserAttributes>)query.list();
			
			Hibernate.initialize(result);
			for(SbiUserAttributes current : result ){
				Hibernate.initialize(current.getSbiAttribute());
			}
			return result;


		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<SbiExtRoles> loadSbiUserRolesById(Integer id) throws EMFUserError {
		logger.debug("IN");

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String q = "select us.sbiExtUserRoleses from SbiUser us where us.id = :id";
			Query query = aSession.createQuery(q);
			query.setInteger("id", id);
			
			ArrayList<SbiExtRoles> result = (ArrayList<SbiExtRoles>)query.list();
			return result;
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}


	public ArrayList<SbiUser> loadSbiUsers() throws EMFUserError {
		logger.debug("IN");

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String q = "from SbiUser ";
			Query query = aSession.createQuery(q);
			
			ArrayList<SbiUser> result = (ArrayList<SbiUser>)query.list();
			return result;
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}
	public void deleteSbiUserById(Integer id) throws EMFUserError {
		logger.debug("IN");

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiUser userToDelete =(SbiUser)aSession.load(SbiUser.class, id);
			aSession.delete(userToDelete);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}

	public void fullUpdateSbiUser(Integer id, String password, String fullName, ArrayList<String> roles, HashMap<Integer, String> attributes)
			throws EMFUserError {
		logger.debug("IN");

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiUser userToUpdate =(SbiUser)aSession.load(SbiUser.class, id);
			userToUpdate.setPassword(password);
			userToUpdate.setFullName(fullName);
			
			//sets roles
			Set<SbiExtUserRoles> extUserRoles = new HashSet<SbiExtUserRoles>();
			if(roles != null){
				Iterator rolesIt = roles.iterator();
				while(rolesIt.hasNext()){
					String roleID = (String)rolesIt.next();
					SbiExtUserRoles sbiExtUserRole = new SbiExtUserRoles();
					SbiExtUserRolesId extUserRoleId = new SbiExtUserRolesId();
			    	
			    	Integer extRoleId = Integer.valueOf(roleID);
			    	extUserRoleId.setExtRoleId(extRoleId);//role Id
			    	extUserRoleId.setId(id);//user ID
			    	
			    	sbiExtUserRole.setId(extUserRoleId);
			    	sbiExtUserRole.setSbiUser(userToUpdate);
			    	extUserRoles.add(sbiExtUserRole);
				}
				userToUpdate.setSbiExtUserRoleses(extUserRoles);
			}
			//sets attributes
			if(attributes != null){
				Set<SbiUserAttributes> userAttrList = new HashSet<SbiUserAttributes>();
				Iterator attrsIt = attributes.keySet().iterator();
				while(attrsIt.hasNext()){
					Integer attrID = (Integer)attrsIt.next();
					SbiUserAttributes userAttr = new SbiUserAttributes();
					SbiUserAttributesId userAttrID = new SbiUserAttributesId();
			    	
			    	userAttrID.setAttributeId(attrID);//attributeid
			    	userAttrID.setId(id);//user ID
			    	userAttr.setAttributeValue(attributes.get(attrID));
			    	
			    	userAttr.setId(userAttrID);
			    	userAttr.setSbiUser(userToUpdate);
			    	userAttrList.add(userAttr);
				}
				userToUpdate.setSbiUserAttributeses(userAttrList);
			}
			//save
			aSession.saveOrUpdate(userToUpdate);
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		
	}
	public UserBO loadUserById(Integer id) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}
	public ArrayList<UserBO> loadUsers() throws EMFUserError {
		logger.debug("IN");
		ArrayList<UserBO> users = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
/*			String q = "from SbiUser ";
			Query query = aSession.createQuery(q);*/
			Criteria crit = aSession.createCriteria(SbiUser.class);
			//ArrayList<SbiUser> result = (ArrayList<SbiUser>)query.list();
			
			ArrayList<SbiUser> result = (ArrayList<SbiUser>)crit.list();
			if(result != null && !result.isEmpty()){
				users = new ArrayList<UserBO> ();
				for(int i=0; i<result.size(); i++){
					users.add(toUserBO(result.get(i)));
				}
			}
			
			return users;
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}	
	/**
	 * From the Hibernate SbiUser at input, gives the corrispondent BI
	 * object (UserBO).
	 * 
	 * @param sbiUser The Hibernate SbiUser
	 * 
	 * @return the corrispondent output <code>UserBO</code>
	 * @throws EMFUserError 
	 */
	public UserBO toUserBO(SbiUser sbiUser) throws EMFUserError{
		logger.debug("IN");
		    // create empty UserBO
			UserBO userBO = new UserBO();
			userBO.setId(sbiUser.getId());
			userBO.setDtLastAccess(sbiUser.getDtLastAccess());
			userBO.setDtPwdBegin(sbiUser.getDtPwdBegin());
			userBO.setDtPwdEnd(sbiUser.getDtPwdEnd());
			userBO.setFlgPwdBlocked(sbiUser.getFlgPwdBlocked());
			userBO.setFullName(sbiUser.getFullName());
			userBO.setPassword(sbiUser.getPassword());
			userBO.setUserId(sbiUser.getUserId());
			
			List userRoles = new ArrayList();
			Set roles = sbiUser.getSbiExtUserRoleses();
			for (Iterator it = roles.iterator(); it.hasNext(); ) {
				SbiExtRoles role = (SbiExtRoles) it.next();
				Integer roleId = role.getExtRoleId();
				userRoles.add(roleId);
			}
			userBO.setSbiExtUserRoleses(userRoles);

			HashMap<Integer, HashMap<String, String>> userAttributes = new HashMap<Integer, HashMap<String, String>>(); 
			Set<SbiUserAttributes> attributes = sbiUser.getSbiUserAttributeses();

			for (Iterator<SbiUserAttributes> it = attributes.iterator(); it.hasNext(); ) {
				SbiUserAttributes attr = it.next();
				Integer attrId = attr.getSbiAttribute().getAttributeId();	
				HashMap<String, String> nameValueAttr = new HashMap<String, String>();

				nameValueAttr.put(attr.getSbiAttribute().getAttributeName(), attr.getAttributeValue());
				userAttributes.put(attrId, nameValueAttr);
			}
			userBO.setSbiUserAttributeses(userAttributes);
			
			logger.debug("OUT");
			return userBO;
	}


}
