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

package it.eng.spagobi.qbe.commons.exception;

import it.eng.spagobi.qbe.QbeEngineInstance;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class QbeEngineException.
 */
public class QbeEngineException extends SpagoBIEngineException {
    
	/** The hints. 
	List hints;
	*/
	
	QbeEngineInstance engineInstance;
	
	
	/**
	 * Builds a <code>GeoEngineException</code>.
	 * 
	 * @param message Text of the exception
	 */
    public QbeEngineException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>GeoEngineException</code>.
     * 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public QbeEngineException(String message, Throwable ex) {
    	super(message, ex);
    	//this.hints = new ArrayList();
    }
    
    public QbeEngineInstance getEngineInstance() {
		return engineInstance;
	}

	public void setEngineInstance(QbeEngineInstance engineInstance) {
		this.engineInstance = engineInstance;
	}
    
    
    /**
     * Instantiates a new qbe engine exception.
     * 
     * @param message the message
     * @param description the description
     * @param hints the hints
     * @param ex the ex
     
    public QbeEngineException(String message, List hints, Throwable ex ) {
    	super(message, ex);
    	this.hints = hints;
    }
	*/
    
	/**
	 * Instantiates a new qbe engine exception.
	 * 
	 * @param message the message
	 * @param description the description
	 * @param hints the hints
	 
	public QbeEngineException(String message, List hints) {
		super(message);
		this.hints = hints;
	}
	*/

    	
    /*
	public String getRootCause() {
		String rootCause;		
		Throwable rootException;
		
		rootException = this;
		while(rootException.getCause() != null) {
			rootException = rootException.getCause();
		}
		
		rootCause = rootException.getMessage()!=null
			? rootException.getClass().getName() + ": " + rootException.getMessage()
			: rootException.getClass().getName();
		
		return rootCause;
	}
	*/
	
	
	/**
	 * Gets the hints.
	 * 
	 * @return the hints
	 
	public List getHints() {
		return hints;
	}
	*/

	/**
	 * Sets the hints.
	 * 
	 * @param hints the new hints
	 
	public void setHints(List hints) {
		this.hints = hints;
	}
	*/

	


}

