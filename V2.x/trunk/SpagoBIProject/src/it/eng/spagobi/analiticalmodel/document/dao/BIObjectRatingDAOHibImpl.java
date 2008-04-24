package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjectsRating;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjectsRatingId;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BIObjectRatingDAOHibImpl extends AbstractHibernateDAO implements
		IBIObjectRating {
    
    static private Logger logger = Logger.getLogger(BIObjectRatingDAOHibImpl.class);
    
    public void voteBIObject(BIObject obj,String userid, String rating) throws EMFUserError{
    	logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
				
			SbiObjects hibBIObject = (SbiObjects)aSession.load(SbiObjects.class,  obj.getId());
			SbiObjectsRating hibBIObjectsRating = new SbiObjectsRating();
			
			hibBIObjectsRating = loadBIObjectRatingById(obj, userid);
			
			if (hibBIObjectsRating != null){
				
				hibBIObjectsRating.setRating(new Integer(rating));
				aSession.update(hibBIObjectsRating);

			}else {
				SbiObjectsRating hibBIObjectsRating1 = new SbiObjectsRating();
				SbiObjectsRatingId hibBIObjectsRatingId1 = new SbiObjectsRatingId();
				hibBIObjectsRatingId1.setObjId(obj.getId());
				hibBIObjectsRatingId1.setUserId(userid);
				hibBIObjectsRating1.setId(hibBIObjectsRatingId1);
				hibBIObjectsRating1.setRating(new Integer(rating));
				hibBIObjectsRating1.setSbiObjects(hibBIObject);
				hibBIObjectsRating = hibBIObjectsRating1 ;
				aSession.save(hibBIObjectsRating);
				
				
			}		
					
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while inserting the Distribution List with name " , he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
    	
    }
    
    public Double calculateBIObjectRating(BIObject obj) throws EMFUserError{
    	Double rating = new Double(0);
    
    	
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiObjectsRating s where s.id.objId = " + obj.getId();
			Query query = aSession.createQuery(hql);
				
			List l = query.list();
			double totalVotes = 0 ;
			double sumVotes = 0 ;
			
		    Iterator it = l.iterator();
		    while(it.hasNext()){
		    	SbiObjectsRating temp = (SbiObjectsRating)it.next();
		    	Integer rat = temp.getRating();
		    	sumVotes = sumVotes + rat.doubleValue();
		    	totalVotes ++ ;
		    }
		    if (totalVotes != 0){
		    	rating = new Double (sumVotes / totalVotes);
		    }
			tx.commit();
			return rating ;
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
    
    private SbiObjectsRating loadBIObjectRatingById(BIObject obj, String userid) throws EMFUserError {

    	SbiObjectsRating hibBIObjectsRating = new SbiObjectsRating();
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = " from SbiObjectsRating s where " +
			             " s.id.objId = "+  obj.getId()+ " and s.id.userId = '"+ userid +"'";
			Query hqlQuery = aSession.createQuery(hql);
			hibBIObjectsRating = (SbiObjectsRating)hqlQuery.uniqueResult();
			tx.commit();
			return hibBIObjectsRating ;
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


}
