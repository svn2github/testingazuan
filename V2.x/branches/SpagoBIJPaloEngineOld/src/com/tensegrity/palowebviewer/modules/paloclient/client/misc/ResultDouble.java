package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

public class ResultDouble implements IResultElement {
	private double value;
	
	public ResultDouble(){
		
	}

	public ResultDouble(double value) {
		this.value = value;
	}

	
	public final double getDoubleValue(){
		return value;
	}

	public String toString() {
		return "" + value;
	}
	
	
}
