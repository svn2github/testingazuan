package it.eng.spagobi.commons.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.commons.metadata.SbiUserFunctionality;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserFunctionalityDAO extends AbstractHibernateDAO implements IUserFunctionalityDAO {

    static private Logger logger = Logger.getLogger(UserFunctionalityDAO.class);
    
    public String[] readUserFunctionality(String[] roles) throws Exception{
	logger.debug("IN");
	if (roles==null) return null;
	String strRoles="";

	ArrayList toReturn = new ArrayList();;
	Session aSession = null;
	Transaction tx = null;
	try{
		aSession = getSession();
		tx = aSession.beginTransaction();
		
		for (int i=0;i<roles.length;i++){
		    String hql = "from SbiExtRoles ser where ser.name=?";
		    Query query = aSession.createQuery(hql);		   
		    query.setParameter(0, roles[i]);
		    SbiExtRoles spaobiRole=(SbiExtRoles)query.uniqueResult();
		    strRoles=strRoles+"'"+spaobiRole.getRoleType().getValueCd()+"',";
		}
		if (strRoles.endsWith(",")){
		    strRoles=strRoles.substring(0, strRoles.length()-1);
		}
		logger.debug("strRoles="+strRoles);
		
		//String hql = "from SbiRolesUserFunctionality suf where suf.userFunctionality.domainCd = 'USER_FUNCTIONALITY'" + 
		// " and suf.roleType.valueCd in ("+strRoles+")";
		String hql = "Select distinct suf.name from SbiUserFunctionality suf where suf.roleType.valueCd in ("+strRoles+") and suf.roleType.domainCd='ROLE_TYPE'";
		Query query = aSession.createQuery(hql);
		List userFuncList = query.list();
		Iterator iter=userFuncList.iterator();
		while (iter.hasNext()){
		    String tmp=(String)iter.next();
		    toReturn.add(tmp);
		    logger.debug("Add Functionality="+tmp);
		}
		tx.commit();
	}catch(HibernateException he){
		logger.error("HibernateException during query",he);
		
		if (tx != null) tx.rollback();	

		throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
	
	}finally{
		if (aSession!=null){
			if (aSession.isOpen()) aSession.close();
		}
		logger.debug("OUT");
	}
	String[] ris=new String[toReturn.size()];
	toReturn.toArray(ris);
	return ris;
	
    }

}
