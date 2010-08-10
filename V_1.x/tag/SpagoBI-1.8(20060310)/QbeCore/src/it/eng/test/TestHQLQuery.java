/*
 * Created on 2-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.test;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Logger;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.HSQLDialect;


/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestHQLQuery {

	public static void main(String[] args) {
		try{
			Configuration cfg = new Configuration();
			cfg.setProperty("hibernate.connection.url","jdbc:hsqldb:hsql://localhost/foodmart");
			cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
			cfg.setProperty("hibernate.connection.username", "sa");
			cfg.setProperty("hibernate.connection.password", "");
			cfg.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
			cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");
			cfg.setProperty("hibernate.show_sql", "true");
			
			
			File jarFile = new File("C:/tmp/foodmartReduced/datamart.jar");
			try{
				ClassLoader previous = Thread.currentThread().getContextClassLoader();
				ClassLoader current = URLClassLoader.newInstance(new URL[]{jarFile.toURL()}, previous);
				Thread.currentThread().setContextClassLoader(current);
			}catch (Exception e) {
				Logger.error(DataMartModel.class, e);
			}
			cfg.addJar(jarFile);
			SessionFactory sf = cfg.buildSessionFactory();
			Session aSession = sf.openSession();
			//String query = "from it.eng.model.foodmart.SalesFact1997 aliasSalesFact1997 where aliasSalesFact1997.customer.customerId  = 6280";
			//String query = "select aliasSalesFact1997.customer.lname, aliasSalesFact1997.customer.birthdate, aliasSalesFact1997.customer.fname, aliasSalesFact1997.customer.customerRegion.salesRegion from it.eng.model.foodmart.SalesFact1997 aliasSalesFact1997 where aliasSalesFact1997.customer.customerRegion.regionId = 79"; 
			
			String query = "select " +
						   "sum(aSalesFact1997.storeCost), " +
						   "avg(aSalesFact1997.storeSales), " +
						   "avg(aSalesFact1997.unitSales), " +
						   "aSalesFact1997.product.productName, " +
						   "aSalesFact1997.product.brandName, " +
						   "aSalesFact1997.product.productClass.productCategory, " +
						   "aSalesFact1997.product.productClass.productSubcategory " +
						   "from it.foodmart.SalesFact1997 as aSalesFact1997 " +
						   "group by aSalesFact1997.product.productName , " +
						   "aSalesFact1997.product.brandName , " +
						   "aSalesFact1997.product.productClass.productCategory , " +
						   "aSalesFact1997.product.productClass.productSubcategory"; 
			Query aQuery = aSession.createQuery(query);
			aQuery.list();
			
			aSession.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
