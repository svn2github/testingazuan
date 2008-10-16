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

package it.eng.spagobi.engines.geo.commons.excpetion;

import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class GeoEngineException.
 */
public class GeoEngineException extends SpagoBIEngineException {
    
	/** The hints. */
	List hints;
	
	/** The description. */
	String description;
	
	
	/**
	 * Builds a <code>GeoEngineException</code>.
	 * 
	 * @param message Text of the exception
	 */
    public GeoEngineException(String message) {
    	super(message);
    }
	
    /**
     * Builds a <code>GeoEngineException</code>.
     * 
     * @param message Text of the exception
     * @param ex previous Throwable object
     */
    public GeoEngineException(String message, Throwable ex) {
    	super(message, ex);
    }
    
    /**
     * Instantiates a new geo engine exception.
     * 
     * @param message the message
     * @param description the description
     * @param hints the hints
     * @param ex the ex
     */
    public GeoEngineException(String message, String description, List hints, Throwable ex ) {
    	super(message, ex);
    	this.hints = hints;
    	this.description = description;
    }

	/**
	 * Instantiates a new geo engine exception.
	 * 
	 * @param message the message
	 * @param description the description
	 */
	public GeoEngineException(String message, String description) {
		super(message);
		this.hints = new ArrayList();
		this.hints.add("Sorry, there are no hints available right now on how to fix this problem");
    	this.description = description;
	}
	
	/**
	 * Instantiates a new geo engine exception.
	 * 
	 * @param message the message
	 * @param description the description
	 * @param hints the hints
	 */
	public GeoEngineException(String message, String description, List hints) {
		super(message);
		this.hints = hints;
    	this.description = description;
	}

	/**
	 * Instantiates a new geo engine exception.
	 * 
	 * @param message the message
	 * @param description the description
	 * @param ex the ex
	 */
	public GeoEngineException(String message, String description, Throwable ex ) {
    	super(message, ex);
    	this.hints = new ArrayList();
		this.hints.add("Sorry, there are no hints available right now on how to fix this problem");
    	this.description = description;
    }

	/**
	 * Gets the hints.
	 * 
	 * @return the hints
	 */
	public List getHints() {
		return hints;
	}

	/**
	 * Sets the hints.
	 * 
	 * @param hints the new hints
	 */
	public void setHints(List hints) {
		this.hints = hints;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}

