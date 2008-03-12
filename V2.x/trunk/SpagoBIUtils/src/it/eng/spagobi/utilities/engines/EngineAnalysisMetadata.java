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
package it.eng.spagobi.utilities.engines;

import java.util.Date;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class EngineAnalysisMetadata {
	private Integer id;
	private String name;	
	private String description;
	private String owner;	
	private String scope;
	private Date firstSavingDate;
	private Date lastSavingDate;
	
	public static final String PUBLIC_SCOPE = "public";
	public static final String PRIVATE_SCOPE = "public";

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Date getFirstSavingDate() {
		return firstSavingDate;
	}
	public void setFirstSavingDate(Date firstSavingDate) {
		this.firstSavingDate = firstSavingDate;
	}
	public Date getLastSavingDate() {
		return lastSavingDate;
	}
	public void setLastSavingDate(Date lastSavingDate) {
		this.lastSavingDate = lastSavingDate;
	}
	
	public boolean isPublic() {
		return scope!=null && scope.equalsIgnoreCase( PUBLIC_SCOPE );
	}
	
	public boolean alreadySaved() {
		return firstSavingDate != null;
	}
}
