package it.eng.spago.cms;

import javax.jcr.Repository;

/**
 *
 * Define a method for get a jsr 170 repository
 * 
 */
public interface IRepositoryFactory {
    
	/**
	 * return a JSR 170 Repository
	 * @return JSR 170 Repository
	 */
	public Repository getRepository();
	
	/**
	 * Makes the repository shutdown
	 */
	public void shutdown();
}
