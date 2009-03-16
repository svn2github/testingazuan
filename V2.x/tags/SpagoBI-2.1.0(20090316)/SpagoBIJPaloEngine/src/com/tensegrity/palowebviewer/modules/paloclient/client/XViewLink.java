package com.tensegrity.palowebviewer.modules.paloclient.client;

public class XViewLink  extends XFavoriteNode {

	private String viewId;
	private String cubeId;
	private String databaseId;
	private String connectionId;

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getCubeId() {
		return cubeId;
	}

	public void setCubeId(String cubeId) {
		this.cubeId = cubeId;
	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	
	public String toString() {
		String result = "XViewLink[";
		result += getName() +" : ";
		result += getViewId();
		result += "]";
		return result;
	}
	
	public boolean equals(Object o) {
		boolean result = false;
		if(o instanceof XViewLink && super.equals(o) ) {
			XViewLink link = (XViewLink)o;
			result = safeCompare(link.connectionId, connectionId);
			result &= safeCompare(link.databaseId, databaseId);
			result &= safeCompare(link.cubeId, cubeId);
			result &= safeCompare(link.viewId, viewId);
		}
		return result;
	}
	
	private static boolean safeCompare(Object o1, Object o2) {
		return o1 == null ? o2==null : o1.equals(o2);
	}

}
