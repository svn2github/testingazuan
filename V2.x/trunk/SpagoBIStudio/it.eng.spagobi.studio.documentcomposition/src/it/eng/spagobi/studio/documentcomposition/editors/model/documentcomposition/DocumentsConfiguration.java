package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition;

import java.util.Vector;

public class DocumentsConfiguration {

	private String videoWidth;
	private String videoHeight;
	private Vector<Document> documents=new Vector<Document>();

	private static int VIDEO_WIDTH=1200;
	private static int VIDEO_HEIGHT=1064;

	public Vector<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Vector<Document> documents) {
		this.documents = documents;
	}

	public String getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(String videoWidth) {
		this.videoWidth = videoWidth;
	}

	public String getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(String videoHeight) {
		this.videoHeight = videoHeight;
	}

	public DocumentsConfiguration() {
		documents=new Vector<Document>();
		videoHeight=Integer.valueOf(VIDEO_HEIGHT).toString();
		videoWidth=Integer.valueOf(VIDEO_WIDTH).toString();
	}




}
