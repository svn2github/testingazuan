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
package it.eng.spagobi.hotlink.rememberme.metadata;

import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubObjects;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class SbiRememberMe implements java.io.Serializable  {

	//	Fields
	private Integer id;
	private String name;
	private String description;
	private String userName;
	private SbiObjects sbiObject;
	private SbiSubObjects sbiSubObject;
	private String parameters;
	
    // Constructors

	/** default constructor */
    public SbiRememberMe() {
    }
    
    /** constructor with id */
    public SbiRememberMe(Integer id) {
        this.id = id;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SbiObjects getSbiObject() {
		return sbiObject;
	}

	public void setSbiObject(SbiObjects sbiObject) {
		this.sbiObject = sbiObject;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public SbiSubObjects getSbiSubObject() {
		return sbiSubObject;
	}

	public void setSbiSubObject(SbiSubObjects sbiSubObject) {
		this.sbiSubObject = sbiSubObject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
