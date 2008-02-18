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
package it.eng.spagobi.engines.dossier.bo;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class DossierPresentation {

	static private Logger logger = Logger.getLogger(DossierPresentation.class);
	
	private Integer id;
	private Long workflowProcessId;
	private Integer biobjectId;
	private Integer binId;
	private String name;
	private byte[] content;
	private Integer prog;
	private Date creationDate;
	private Boolean approved;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBiobjectId() {
		return biobjectId;
	}
	public void setBiobjectId(Integer biobjectId) {
		this.biobjectId = biobjectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getContent() throws EMFUserError, EMFInternalError {
		if (content == null) {
			if (binId != null) {
				// reads from database
				try {
					content = DAOFactory.getBinContentDAO().getBinContent(binId);
				} catch (EMFUserError e) {
					logger.error("Error while recovering content of presentation with id = [" + id + "], binary content id = [" + binId + "], " +
							"name = [" + name + "] of biobject with id = [" + biobjectId + "]" + e);
					throw e;
				} catch (EMFInternalError e) {
					logger.error("Error while recovering content of presentation with id = [" + id + "], binary content id = [" + binId + "], " +
							"name = [" + name + "] of biobject with id = [" + biobjectId + "]" + e);
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
	public Boolean getApproved() {
		return approved;
	}
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	public Integer getBinId() {
		return binId;
	}
	public void setBinId(Integer binId) {
		this.binId = binId;
	}
	public Long getWorkflowProcessId() {
		return workflowProcessId;
	}
	public void setWorkflowProcessId(Long workflowProcessId) {
		this.workflowProcessId = workflowProcessId;
	}
	
}
