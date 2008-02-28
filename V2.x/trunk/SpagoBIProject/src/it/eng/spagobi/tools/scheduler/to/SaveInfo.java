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
package it.eng.spagobi.tools.scheduler.to;

import java.util.ArrayList;
import java.util.List;

public class SaveInfo {

	private boolean saveAsSnapshot = false;
	private boolean saveAsDocument = false;
	private boolean sendMail = false;
	private boolean sendToDl = false;
	private boolean addToDl = false;
	private String snapshotName = "";
	private String snapshotDescription = "";
	private String snapshotHistoryLength = "";
	private String documentName = "";
	private String documentDescription = "";
	private String documentHistoryLength = "";
	private String mailTos = "";
	private String functionalityIds = "";
	private String mailSubj = "";
	private String mailTxt = "";
	private int biobjId = 0;
	private List dlIds = new ArrayList();
	
	public void removeDlId(Integer dlId) {
		this.dlIds.remove(dlId);
	}
	
	public void addDlId(Integer dlId) {
		this.dlIds.add(dlId);
	}
	public String getDocumentDescription() {
		return documentDescription;
	}
	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}
	public String getDocumentHistoryLength() {
		return documentHistoryLength;
	}
	public void setDocumentHistoryLength(String documentHistoryLength) {
		this.documentHistoryLength = documentHistoryLength;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getMailTos() {
		return mailTos;
	}
	public void setMailTos(String mailTos) {
		this.mailTos = mailTos;
	}
	public String getMailSubj() {
		return mailSubj;
	}
	public void setMailSubj(String mailSubj) {
		this.mailSubj = mailSubj;
	}
	public String getMailTxt() {
		return mailTxt;
	}
	public void setMailTxt(String mailTxt) {
		this.mailTxt = mailTxt;
	}
	public boolean isSaveAsDocument() {
		return saveAsDocument;
	}
	public void setSaveAsDocument(boolean saveAsDocument) {
		this.saveAsDocument = saveAsDocument;
	}
	public boolean isSaveAsSnapshot() {
		return saveAsSnapshot;
	}
	public void setSaveAsSnapshot(boolean saveAsSnapshot) {
		this.saveAsSnapshot = saveAsSnapshot;
	}
	public boolean isSendMail() {
		return sendMail;
	}
	public void setSendMail(boolean sendMail) {
		this.sendMail = sendMail;
	}
	public String getSnapshotDescription() {
		return snapshotDescription;
	}
	public void setSnapshotDescription(String snapshotDescription) {
		this.snapshotDescription = snapshotDescription;
	}
	public String getSnapshotHistoryLength() {
		return snapshotHistoryLength;
	}
	public void setSnapshotHistoryLength(String snapshotHistoryLength) {
		this.snapshotHistoryLength = snapshotHistoryLength;
	}
	public String getSnapshotName() {
		return snapshotName;
	}
	public void setSnapshotName(String snapshotName) {
		this.snapshotName = snapshotName;
	}
	public String getFunctionalityIds() {
		return functionalityIds;
	}
	public void setFunctionalityIds(String functionalityIds) {
		this.functionalityIds = functionalityIds;
	}
	public List getDlIds() {
		return dlIds;
	}
	public void setDlIds(List dlIds) {
		this.dlIds = dlIds;
	}

	public boolean isSendToDl() {
		return sendToDl;
	}

	public void setSendToDl(boolean sendToDl) {
		this.sendToDl = sendToDl;
	}

	public boolean isAddToDl() {
		return addToDl;
	}

	public void setAddToDl(boolean addToDl) {
		this.addToDl = addToDl;
	}

	public int getBiobjId() {
		return biobjId;
	}

	public void setBiobjId(int biobjId) {
		this.biobjId = biobjId;
	}
	
	
}
