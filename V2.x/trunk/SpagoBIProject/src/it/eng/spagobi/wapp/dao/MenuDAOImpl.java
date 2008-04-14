/**
 * 
 */
package it.eng.spagobi.wapp.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.wapp.bo.Menu;
import it.eng.spagobi.wapp.metadata.SbiMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
 
/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */
public class MenuDAOImpl extends AbstractHibernateDAO implements IMenuDAO{
	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#loadMenuByID(integer)
	 */
	public Menu loadMenuByID(Integer menuID) throws EMFUserError {
		Menu toReturn = null;
		Session tmpSession = null;
		Transaction tx = null;

		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();	
			SbiMenu hibMenu = (SbiMenu)tmpSession.load(SbiMenu.class,  menuID);
			toReturn = toMenu(hibMenu);
			
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
				
			}
		}		
		return toReturn;
	}	
	

	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#loadMenuByName(string)
	 */	
	public Menu loadMenuByName(String name) throws EMFUserError {
		Menu biMenu = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("name",
					name);
			Criteria criteria = tmpSession.createCriteria(SbiMenu.class);
			criteria.add(labelCriterrion);	
			SbiMenu hibMenu = (SbiMenu) criteria.uniqueResult();
			if (hibMenu == null) return null;
			biMenu = toMenu(hibMenu);				
			
			//tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		return biMenu;		
	}

	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#modifyMenu(it.eng.spagobi.wapp.bo.Menu)
	 */
	public void modifyMenu(Menu aMenu) throws EMFUserError {
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();

			SbiMenu hibMenu = (SbiMenu) tmpSession.load(SbiMenu.class, aMenu.getMenuId());
			hibMenu.setName(aMenu.getName());
			hibMenu.setDescr(aMenu.getDescr());
			hibMenu.setParentId(aMenu.getParentId());	
			SbiObjects objId = (SbiObjects)tmpSession.load(SbiObjects.class,  aMenu.getObjId());
			hibMenu.setSbiObjects(objId);
			tx.commit();
			
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}			
		}

	}

	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#insertMenu(it.eng.spagobi.wapp.bo.Menu)
	 */
	public void insertMenu(Menu aMenu) throws EMFUserError{		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			SbiMenu hibMenu = new SbiMenu();
			hibMenu.setName(aMenu.getName());
			hibMenu.setDescr(aMenu.getDescr());
			hibMenu.setParentId(aMenu.getParentId());	
			SbiObjects objId = (SbiObjects)tmpSession.load(SbiObjects.class,  aMenu.getObjId());
			hibMenu.setSbiObjects(objId);
			tmpSession.save(hibMenu);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}
	}
	
	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#eraseMenu(it.eng.spagobi.wapp.bo.Menu)
	 */
	public void eraseMenu(Menu aMenu) throws EMFUserError {
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			
			SbiMenu hibMenu = (SbiMenu) tmpSession.load(SbiMenu.class, aMenu.getMenuId());

			tmpSession.delete(hibMenu);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}
	}
	
	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#loadAllMenues()
	 */
	public List loadAllMenues() throws EMFUserError {
		Session tmpSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();

			Query hibQuery = tmpSession.createQuery(" from SbiMenu");
			
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();			
			while (it.hasNext()) {			
				SbiMenu hibMenu = (SbiMenu) it.next();	
				if (hibMenu != null) {
					Menu biMenu = toMenu(hibMenu);	
					realResult.add(biMenu);
				}
			}
			//tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}
		return realResult;
	}
	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#hasRolesAssociated(java.lang.Integer)
	 */
	public boolean hasRolesAssociated (Integer menuId) throws EMFUserError{
		boolean bool = false; 
		
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			
			String hql = " from SbiMenuRole s where s.id.menuId = "+ menuId;
			Query aQuery = tmpSession.createQuery(hql);
			
			List biFeaturesAssocitedWithMap = aQuery.list();
			if (biFeaturesAssocitedWithMap.size() > 0)
				bool = true;
			else
				bool = false;
			//tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		return bool;
		
	}
	
	/**
	 * @see it.eng.spagobi.wapp.dao.IMenuDAO#getChildrenMenu(java.lang.Integer)
	 */
	public List getChildrenMenu (Integer menuId) throws EMFUserError{
		List lstChildren = new ArrayList();
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			
			String hql = " from SbiMenu s where s.id.parentId = "+ menuId;
			Query aQuery = tmpSession.createQuery(hql);
			
			List hibList = aQuery.list();
			Iterator it = hibList.iterator();			
			while (it.hasNext()) {			
				SbiMenu hibMenu = (SbiMenu) it.next();	
				if (hibMenu != null) {
					Menu biMenu = toMenu(hibMenu);	
					lstChildren.add(biMenu);
				}
			}
			
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		return lstChildren;
	}

	/**
	 * From the Hibernate Menu object at input, gives the corrispondent 
	 * <code>Menu</code> object.
	 * 
	 * @param hibMenu	The Hibernate Menu object
	 * @return the corrispondent output <code>Menu</code>
	 */
	private Menu toMenu(SbiMenu hibMenu) throws EMFUserError{
		
		Menu menu = new Menu();
		menu.setMenuId(hibMenu.getMenuId());
		menu.setName(hibMenu.getName());
		menu.setDescr(hibMenu.getDescr());
		menu.setParentId(hibMenu.getParentId());
		if (hibMenu.getSbiObjects() != null)
			menu.setObjId(hibMenu.getSbiObjects().getBiobjId());
		menu.setLevel(getLevel(menu.getParentId(), menu.getObjId()));
	    //set children
		try{
			List tmpLstChildren = (DAOFactory.getMenuDAO().getChildrenMenu(menu.getMenuId()));
			boolean hasCHildren = (tmpLstChildren.size()==0)?false:true;
			menu.setLstChildren(tmpLstChildren);
			menu.setHasChildren(hasCHildren);
		}catch (Exception ex){
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		
		return menu;
	}

	/**
	 * Return the level of menu element: 1 - first, 2 - second|third, 4 - last, 0 other
	 */
	private Integer getLevel(Integer parentId, Integer objId){
		if (parentId == null && objId != null)
			return new Integer("1");
		else if (parentId != null && objId == null)
			return new Integer("2");
		else if(parentId != null && objId != null)
			return new Integer("4");
		else
			return new Integer("1");
	}
}