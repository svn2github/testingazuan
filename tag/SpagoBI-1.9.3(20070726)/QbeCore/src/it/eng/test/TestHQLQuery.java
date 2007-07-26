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
