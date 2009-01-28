package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

public class ResultString implements IResultElement {
	private String value;

	public ResultString(){
		
	}
	
	public ResultString(String value) {
		this.value = value;
	}

	public final String getValue() {
		return value;
	}

	public String toString() {
		return value;
	}
}
