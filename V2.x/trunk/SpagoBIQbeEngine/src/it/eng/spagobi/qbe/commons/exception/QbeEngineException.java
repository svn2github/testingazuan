/**
 *
 *	LICENSE: see COPYING file
 *
**/

package it.eng.spagobi.qbe.commons.exception;

import it.eng.spagobi.utilities.engines.EngineException;

import java.util.ArrayList;
import java.util.List;

public class QbeEngineException extends EngineException {
    
	List hints;
	String description;
	
	
	/**
     * Builds a <code>GeoEngineException</code> 
     * @param message Text of the exception
     */
    public QbeEngineException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>GeoEngineException</code> 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public QbeEngineException(String message, Throwable ex) {
    	super(message, ex);
    }
    
    public QbeEngineException(String message, String description, List hints, Throwable ex ) {
    	super(message, ex);
    	this.hints = hints;
    	this.description = description;
    }

	public QbeEngineException(String message, String description) {
		super(message);
		this.hints = new ArrayList();
		this.hints.add("Sorry, there are no hints available right now on how to fix this problem");
    	this.description = description;
	}
	
	public QbeEngineException(String message, String description, List hints) {
		super(message);
		this.hints = hints;
    	this.description = description;
	}

	public QbeEngineException(String message, String description, Throwable ex ) {
    	super(message, ex);
    	this.hints = new ArrayList();
		this.hints.add("Sorry, there are no hints available right now on how to fix this problem");
    	this.description = description;
    }

	public List getHints() {
		return hints;
	}

	public void setHints(List hints) {
		this.hints = hints;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

