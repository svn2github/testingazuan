package it.eng.qbe.datasource;

import it.eng.qbe.bo.Formula;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public interface IHibernateDataSource extends IDataSource {

	String getDatamartName();
	List getDatamartNames();
	
	DBConnection getConnection();
	
	Configuration getConfiguration();	
	SessionFactory getSessionFactory();
	SessionFactory getSessionFactory(String dmName);

	Formula getFormula();
	void setFormula(Formula formula);
		
	
	void refreshDatamartViews();	
	void refreshSharedViews();	
	void refreshSharedView(String sharedViewName);
	void refresh();		
}