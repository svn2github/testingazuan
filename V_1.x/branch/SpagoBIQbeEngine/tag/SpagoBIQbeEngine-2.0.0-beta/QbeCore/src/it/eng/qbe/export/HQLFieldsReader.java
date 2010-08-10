/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.export;

import java.util.Iterator;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

// TODO: Auto-generated Javadoc
/**
 * Class that can get fields (i.e name and type) from a hql query string
 * 
 * @author Gioia
 */
public class HQLFieldsReader implements IFieldsReader {

	/** The query. */
	private String query;
	
	/** The session factory. */
	private SessionFactory sessionFactory;

	/**
	 * Instantiates a new hQL fields reader.
	 * 
	 * @param query the query
	 * @param sessionFactory the session factory
	 */
	public HQLFieldsReader(String query, SessionFactory sessionFactory) {
		this.query = query;
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Prepare query.
	 * 
	 * @return the string
	 */
	public String prepareQuery() {
		return null; 
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.export.IFieldsReader#readFields()
	 */
	public Vector readFields() throws Exception {

		Session session = null;
		Transaction transaction = null;

		try {

			session = sessionFactory.openSession();

			if (session == null) {
				throw new Exception(
						"Problem creating the Session object for Hibernate");
			}

			transaction = session.beginTransaction();
			Query q = session.createQuery(query);

			q.setFetchSize(1);
			Iterator iterator = q.iterate();

			String[] aliases = q.getReturnAliases();
			Type[] types = q.getReturnTypes();

			Vector fields = new Vector();

			for (int i = 0; i < types.length; ++i) {
											
				if (types[i].isComponentType() || types[i].isEntityType()) {

					// look for alias...
					String aliasName = null;
					if (aliases != null && aliases.length > i
							&& !aliases[i].equals(i + "")) {
						aliasName = aliases[i];
						Field field = new Field(aliases[i], types[i]
						          .getReturnedClass().getName(), -1);
						field.setDescription(aliases[i]);
						fields.add(field);
					}					
				} else {
					String fieldName = aliases[i];
					if (aliases != null && aliases.length > i
							&& !aliases[i].equals("" + i))
						fieldName = aliases[i];
					Field field = new Field(fieldName, types[i]
					    .getReturnedClass().getName(), -1);
					field.setDescription("field"+ (i+1));
					fields.add(field);
				}
			}

			return fields;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {

			if (transaction != null)
				try {
					transaction.rollback();
				} catch (Exception ex) {
				}
			if (session != null)
				try {
					session.close();
				} catch (Exception ex) {
				}
		}
	}
	
}
