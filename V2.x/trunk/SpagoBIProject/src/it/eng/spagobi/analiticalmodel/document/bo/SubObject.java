package it.eng.spagobi.analiticalmodel.document.bo;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

public class SubObject implements Serializable {
	
	static private Logger logger = Logger.getLogger(SubObject.class);
	
	private Integer id = null;
	private Integer biobjId = null;
	private String name = null;
	private Boolean isPublic = new Boolean(false);
	private String owner = null;
	private String description = null;
	private Date lastChangeDate;
	private Date creationDate;
	private byte[] content;
	private Integer binaryContentId=null;
	
	public Integer getBinaryContentId() {
	    return binaryContentId;
	}
	public void setBinaryContentId(Integer binaryContentId) {
	    this.binaryContentId = binaryContentId;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getLastChangeDate() {
		return lastChangeDate;
	}
	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * Tries to load binary content from database for this SubObject instance, given its binary content identifier, 
	 * if content field is null.
	 * @return The binary content of this instance; if it is null, it tries to load it from database if binary content identifier
	 * is available
	 * @throws EMFUserError if some errors while reading from db occurs
	 * @throws EMFInternalError if some errors while reading from db occurs
	 */
	public byte[] getContent() throws EMFUserError, EMFInternalError {
		if (content == null) {
			if (binaryContentId != null) {
				// reads from database
				try {
					content = DAOFactory.getBinContentDAO().getBinContent(binaryContentId);
				} catch (EMFUserError e) {
					logger.error("Error while recovering content of subobject with id = [" + id + "], " +
							"binary content id = [" + binaryContentId + "], " +
							"name = [" + name + "] of biobject with id = [" + biobjId + "]" + e);
					throw e;
				} catch (EMFInternalError e) {
					logger.error("Error while recovering content of subobject with id = [" + id + "], " +
							"binary content id = [" + binaryContentId + "], " +
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
	
}
