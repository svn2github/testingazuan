/**
 * 
 */
package it.eng.spagobi.wapp.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.wapp.bo.Menu;
import it.eng.spagobi.wapp.bo.MenuRoles;
import it.eng.spagobi.wapp.metadata.SbiMenuRole;
import it.eng.spagobi.wapp.metadata.SbiMenuRoleId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */
public class MenuRolesDAOImpl extends AbstractHibernateDAO implements IMenuRolesDAO {
	
	/** 
	 * @see it.eng.spagobi.wapp.dao.IMenuRolesDAO#loadMenuByRoleId(java.lang.Integer)
	 */
	public List loadMenuByRoleId(Integer roleId) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

				hql = " select mf.id.menuId, mf.id.extRoleId from SbiMenuRole as mf, SbiMenu m " +
					  " where mf.id.menuId = m.menuId " + 
					  " and mf.id.extRoleId = " + roleId.toString() +
					  " order by m.parentId, m.menuId";
			
			hqlQuery = aSession.createQuery(hql);
			List hibList = hqlQuery.list();
			
			Iterator it = hibList.iterator();
			IMenuDAO menuDAO = DAOFactory.getMenuDAO();
			SbiMenuRole tmpMenuRole = null;
			Menu tmpMenu = null;
			while (it.hasNext()) {	
				Object[] tmpLst = (Object[])it.next();
				Integer menuId = (Integer)tmpLst[0];
				tmpMenu = menuDAO.loadMenuByID(menuId);
				if (tmpMenu != null)
					realResult.add(tmpMenu);
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
	 * @see it.eng.spagobi.wapp.dao.IMenuRolesDAO#loadMenuRoles(java.lang.Integer, java.lang.Integer)
	 */
	public MenuRoles loadMenuRoles(Integer menuId, Integer roleId) throws EMFUserError{
		MenuRoles toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = "from SbiMenuRole s where s.id.menuId=" + menuId.toString() + 
			             " and s.id.roleId=" +  roleId.toString();
			Query query = aSession.createQuery(hql);
			//toReturn =(MenuRoles) query.uniqueResult();			
			SbiMenuRole hibMenuRole = (SbiMenuRole)query.uniqueResult();
			if (hibMenuRole == null) return null;
			toReturn = toMenuRoles(hibMenuRole);
			
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
	 * @see it.eng.spagobi.wapp.dao.IMenuRolesDAOO#modifyMenuRole(it.eng.spagobi.wapp.bo.MenuRoles)
	 */
	public void modifyMenuRole(MenuRoles aMenuRole) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiMenuRoleId hibMenuRoleId = new SbiMenuRoleId();
			hibMenuRoleId.setMenuId(aMenuRole.getMenuId());
			hibMenuRoleId.setExtRoleId(aMenuRole.getExtRoleId());

			SbiMenuRole hibFeature = (SbiMenuRole) aSession.load(SbiMenuRole.class, hibMenuRoleId);
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
	 * @see it.eng.spagobi.wapp.dao.IMenuRolesDAO#insertMenuRole(it.eng.spagobi.wapp.bo.MenuRoles)
	 */
	public void insertMenuRole(MenuRoles aMenuRole) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiMenuRole hibMenuRole = new SbiMenuRole();	
			
			SbiMenuRoleId hibMenuRoleId = new SbiMenuRoleId();			
			hibMenuRoleId.setMenuId(aMenuRole.getMenuId());
			hibMenuRoleId.setExtRoleId(aMenuRole.getExtRoleId());
			hibMenuRole.setId(hibMenuRoleId);
			aSession.save(hibMenuRole);
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
	 * @see it.eng.spagobi.wapp.dao.IMenuRolesDAO#eraseMenuRole(it.eng.spagobi.wapp.bo.MenuRoles)
	 */
	public void eraseMenuRole(MenuRoles aMenuRole) throws EMFUserError{
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiMenuRoleId hibMenuRoleId = new SbiMenuRoleId();			
			hibMenuRoleId.setMenuId(aMenuRole.getMenuId());
			hibMenuRoleId.setExtRoleId(aMenuRole.getExtRoleId());
			
			SbiMenuRole hibMenuRole = (SbiMenuRole) aSession.load(SbiMenuRole.class, hibMenuRoleId);

			aSession.delete(hibMenuRole);
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
	 * From the Hibernate menuRoles relation at input, gives the corrispondent 
	 * <code>MenuRoles</code> object.
	 * 
	 * @param hibMenuRole	The Hibernate MenuRole object 
	 * @return the corrispondent output <code>MenuRoles</code>
	 */
	public MenuRoles toMenuRoles(SbiMenuRole hibMenuRole){
		
		MenuRoles menuRoles = new MenuRoles();					
		
		menuRoles.setMenuId(hibMenuRole.getId().getMenuId());
		menuRoles.setExtRoleId(hibMenuRole.getId().getExtRoleId());
		
		return menuRoles;
	}

}
