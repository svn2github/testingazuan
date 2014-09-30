package it.eng.spagobi.twitter.analysis.cache;

import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DaoService {

	Session session;

	public DaoService() {

	}

	/**
	 * Persist a new object
	 *
	 * @param entity
	 * @return
	 */
	public Object create(Object entity) {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to create a new entity without a valid Session");

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			session.persist(entity);
			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService create(): Impossible to create a new [ " + entity.getClass() + " ] - " + he.getMessage());
		} finally {
			session.close();
		}

		return entity;

	}

	public void update(Object entity) {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to update this entity without a valid Session");

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			session.update(entity);
			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService create(): Impossible to update entity [ " + entity.getClass() + " ] - " + he.getMessage());
		} finally {
			session.close();
		}

	}

	public void delete(Object entity) {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to delete this entity without a valid Session");

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			session.delete(entity);
			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService create(): Impossible to delete entity [ " + entity.getClass() + " ] - " + he.getMessage());
		} finally {
			session.close();
		}

	}

	public <T> Object find(Class<T> entity, Serializable key) {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to find an entity without a valid Session");

		Object result = null;

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			result = session.get(entity, key);
			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService create(): Impossible to get entity [ " + entity + " ] - " + he.getMessage());
		} finally {
			session.close();
		}

		return result;
	}

	public <T> T refresh(T entity) {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to refresh an entity without a valid Session");

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			session.refresh(entity);
			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService create(): Impossible to create a new [ " + entity.getClass() + " ] - " + he.getMessage());
		} finally {
			session.close();
		}

		return entity;

	}

	public <T> List<T> listFromQuery(String query, Object... args) throws HibernateException {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to execute the query without a valid Session");

		Transaction tx = null;

		List<T> result = null;

		try {

			tx = session.beginTransaction();
			Query q = session.createQuery(query);

			for (int i = 0; i < args.length; i++) {
				q.setParameter(i, args[i]);
			}

			result = q.list();
			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService listFromQuery(): Impossible to execute the query [ " + query + " ] - " + he);

		}

		finally {
			session.close();
		}

		return result;

	}

	public <T> List<T> listFromLimitedQuery(String query, int max, Object... args) throws HibernateException {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to execute the query without a valid Session");

		Transaction tx = null;

		List<T> result = null;

		try {

			tx = session.beginTransaction();
			Query q = session.createQuery(query);

			for (int i = 0; i < args.length; i++) {
				q.setParameter(i, args[i]);
			}

			q.setMaxResults(max);

			result = q.list();
			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService listFromLimitedQuery(): Impossible to execute the query [ " + query + " ] - " + he);

		}

		finally {
			session.close();
		}

		return result;

	}

	public int countQuery(String queryHQL, Object... args) {
		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to execute the query without a valid Session");

		Transaction tx = null;

		int countResult = 0;

		try {

			tx = session.beginTransaction();
			Query query = session.createQuery(queryHQL);

			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}

			countResult = query.list().size();

			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService listFromQuery(): Impossible to execute the query [ " + queryHQL + " ] - " + he);

		}

		finally {
			session.close();
		}

		return countResult;
	}

	public <T> T singleResultQuery(String query, Object... args) {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to execute the query without a valid Session");

		T result;

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			Query q = session.createQuery(query);

			for (int i = 0; i < args.length; i++) {
				q.setParameter(i, args[i]);
			}

			result = (T) q.uniqueResult();

			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService listFromQuery(): Impossible to execute the query [ " + query + " ] - " + he);

		}

		finally {
			session.close();
		}

		return result;
	}

	public void updateFromQuery(String query, Object... args) {

		this.session = TwitterHibernateUtil.getSessionFactory().openSession();

		Assert.assertNotNull(session, "DaoService: Impossible to execute the query without a valid Session");

		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			Query q = session.createQuery(query);

			for (int i = 0; i < args.length; i++) {
				q.setParameter(i, args[i]);
			}

			q.executeUpdate();

			tx.commit();

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new SpagoBIRuntimeException("DaoService updateFromQuery(): Impossible to execute the query [ " + query + " ] - " + he);

		}

		finally {
			session.close();
		}

	}

}
