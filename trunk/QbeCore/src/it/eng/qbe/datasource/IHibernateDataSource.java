package it.eng.qbe.datasource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public interface IHibernateDataSource extends IDataSource {

	
	public abstract SessionFactory getSessionFactory();	

	public abstract Configuration getConfiguration();
	

	public abstract String getDialect();

	public abstract void setDialect(String dialect);

	public abstract String getJndiDataSourceName();

	public abstract void setJndiDataSourceName(String jndiDataSourceName);
	
	public abstract Properties getLabelProperties();
	
	public abstract Properties getLabelProperties(Locale locale);	
	
	public abstract File getFormulaFile();
	
	public abstract Properties getQbeProperties();
	
	public abstract void refreshDatamartViews();
	
	
	public abstract void refreshSharedViews();
	
	public void refreshSharedView(String sharedViewName) ;
	
	public abstract void refresh();

	public abstract String getName();

	public abstract void setName(String name);
	
	public abstract List getDatamartNames();
	
	public abstract String getCompositeDatamartName();
	
	public abstract String getCompositeDatamartDescription();

}