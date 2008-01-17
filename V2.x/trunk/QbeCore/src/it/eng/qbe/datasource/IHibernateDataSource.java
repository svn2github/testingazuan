package it.eng.qbe.datasource;

import it.eng.qbe.bo.Formula;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public interface IHibernateDataSource extends IDataSource {

	String getDatamartName();
	
	DBConnection getConnection();
	/*
	String getJndiDataSourceName();
	String getDialect();
	*/
	
	Configuration getConfiguration();	
	SessionFactory getSessionFactory();	

	Formula getFormula();
	void setFormula(Formula formula);
		
	
	void refreshDatamartViews();	
	void refreshSharedViews();	
	void refreshSharedView(String sharedViewName);
	void refresh();	
	
	
	List getDatamartNames();
}