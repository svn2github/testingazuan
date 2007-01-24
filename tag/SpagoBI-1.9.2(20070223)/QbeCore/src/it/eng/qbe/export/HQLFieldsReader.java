/**
 * 
 */
package it.eng.qbe.export;

import java.util.Iterator;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * Class that can get fields (i.e name and type) from a hql query string
 * 
 * @author Gioia
 */
public class HQLFieldsReader implements IFieldsReader {

	private String query;
	private SessionFactory sessionFactory;

	public HQLFieldsReader(String query, SessionFactory sessionFactory) {
		this.query = query;
		this.sessionFactory = sessionFactory;
	}
	
	public String prepareQuery() {
		return null; 
	}

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
