package it.eng.spagobi.scheduler.to;

public class SaveInfo {

	private boolean saveAsSnapshot = false;
	private boolean saveAsDocument = false;
	private boolean sendMail = false;
	private String snapshotName = "";
	private String snapshotDescription = "";
	private String snapshotHistoryLength = "";
	private String documentName = "";
	private String documentDescription = "";
	private String documentHistoryLength = "";
	private String mailTos = "";
	private String documentPaths = "";
	
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
	public String getDocumentPaths() {
		return documentPaths;
	}
	public void setDocumentPaths(String documentPaths) {
		this.documentPaths = documentPaths;
	}
	
}
