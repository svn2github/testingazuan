package it.eng.spagobi.booklets.bo;

public class PresentationVersion {

	String creationDate = null;
	String versionName = "";
	String presentationName = "";
	boolean approved = false;
	boolean currentVersion = true;
	
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getPresentationName() {
		return presentationName;
	}
	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public boolean isCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(boolean currentVersion) {
		this.currentVersion = currentVersion;
	}
	
}
