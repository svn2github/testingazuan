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
package it.eng.spagobi.analiticalmodel.document.bo;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

public class ObjTemplate implements Serializable {

	static private Logger logger = Logger.getLogger(ObjTemplate.class);
	
	Integer id = null;
	Integer biobjId = null;
	Integer binId = null;
	String name = null;
	Integer prog = null;
    Date creationDate = null;
    Boolean active = null;
    byte[] content = null;
    String dimension=null;
    String creationUser=null;    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProg() {
		return prog;
	}
	public void setProg(Integer prog) {
		this.prog = prog;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean activeP) {
		this.active = activeP;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBiobjId() {
		return biobjId;
	}
	public void setBiobjId(Integer biobjId) {
		this.biobjId = biobjId;
	}
	public Integer getBinId() {
		return binId;
	}
	public void setBinId(Integer binId) {
		this.binId = binId;
	}
	
	/**
	 * Tries to load binary content from database for this ObjTemplate instance, given its binary content identifier, 
	 * if content field is null.
	 * @return The binary content of this instance; if it is null, it tries to load it from database if binary content identifier
	 * is available
	 * @throws EMFUserError if some errors while reading from db occurs
	 * @throws EMFInternalError if some errors while reading from db occurs
	 */
	public byte[] getContent() throws EMFUserError, EMFInternalError {
		if (content == null) {
			if (binId != null) {
				// reads from database
				try {
					content = DAOFactory.getBinContentDAO().getBinContent(binId);
				} catch (EMFUserError e) {
					logger.error("Error while recovering content of template with id = [" + id + "], binary content id = [" + binId + "], " +
							"name = [" + name + "] of biobject with id = [" + biobjId + "]" + e);
					throw e;
				} catch (EMFInternalError e) {
					logger.error("Error while recovering content of template with id = [" + id + "], binary content id = [" + binId + "], " +
							"name = [" + name + "] of biobject with id = [" + biobjId + "]" + e);
					throw e;
				}
			} else {
				logger.warn("Both content field of this istance and binary identifier are null. Cannot load content from database.");
			}
		}
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getDimension() {
	    return dimension;
	}
	public void setDimension(String dimension) {
	    this.dimension = dimension;
	}
	public String getCreationUser() {
	    return creationUser;
	}
	public void setCreationUser(String creationUser) {
	    this.creationUser = creationUser;
	}


	
	
}
