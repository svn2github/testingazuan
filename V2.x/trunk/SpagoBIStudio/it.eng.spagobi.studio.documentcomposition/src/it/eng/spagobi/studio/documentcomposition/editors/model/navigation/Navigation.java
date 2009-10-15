package it.eng.spagobi.studio.documentcomposition.editors.model.navigation;

public class Navigation {
	
	private String navigationNameText;
	private String masterDocNameText;
	private String destinationDocNameText;
	private String inputParameterText;
	
	
	public String getDestinationDocNameText() {
		return destinationDocNameText;
	}

	public void setDestinationDocNameText(String destinationDocNameText) {
		this.destinationDocNameText = destinationDocNameText;
	}

	public String getInputParameterText() {
		return inputParameterText;
	}

	public void setInputParameterText(String inputParameterText) {
		this.inputParameterText = inputParameterText;
	}



	public String getMasterDocNameText() {
		return masterDocNameText;
	}

	public void setMasterDocNameText(String masterDocNameText) {
		this.masterDocNameText = masterDocNameText;
	}

	public String getNavigationNameText() {
		return navigationNameText;
	}

	public void setNavigationNameText(String navigationNameText) {
		this.navigationNameText = navigationNameText;
	}
	

}
