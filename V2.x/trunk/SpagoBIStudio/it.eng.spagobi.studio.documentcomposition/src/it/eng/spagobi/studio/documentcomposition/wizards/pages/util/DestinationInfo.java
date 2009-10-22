package it.eng.spagobi.studio.documentcomposition.wizards.pages.util;

import org.eclipse.swt.widgets.Text;

public class DestinationInfo{
	private String docDestName;
	private String paramDestName;
	private Text paramDefaultValue;
	
	public Text getParamDefaultValue() {
		return paramDefaultValue;
	}
	public void setParamDefaultValue(Text paramDefaultValue) {
		this.paramDefaultValue = paramDefaultValue;
	}
	public String getDocDestName() {
		return docDestName;
	}
	public void setDocDestName(String docDestName) {
		this.docDestName = docDestName;
	}
	public String getParamDestName() {
		return paramDestName;
	}
	public void setParamDestName(String paramDestName) {
	
		this.paramDestName = paramDestName;
	}

}
