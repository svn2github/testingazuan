package it.eng.spagobi.studio.documentcomposition.wizards.pages.util;

import org.eclipse.swt.widgets.Text;

public class DestinationInfo{
	private String docDestName;
	private Text paramDestName;
	
	public String getDocDestName() {
		return docDestName;
	}
	public void setDocDestName(String docDestName) {
		this.docDestName = docDestName;
	}
	public Text getParamDestName() {
		return paramDestName;
	}
	public void setParamDestName(Text paramDestName) {
	
		this.paramDestName = paramDestName;
	}

}
