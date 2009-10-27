package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition;


public class DocumentComposition {
	
	private DocumentsConfiguration documentsConfiguration;
	private String templateValue;

	public DocumentsConfiguration getDocumentsConfiguration() {
		return documentsConfiguration;
	}
	public void setDocumentsConfiguration(
			DocumentsConfiguration documentsConfiguration) {
		this.documentsConfiguration = documentsConfiguration;
	}
	public String getTemplateValue() {
		return templateValue;
	}
	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}
	public DocumentComposition() {
		documentsConfiguration=new DocumentsConfiguration();
	}
	
}
