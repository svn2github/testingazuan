package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XResult implements IsSerializable{
	private double[] doubleValues;
	private String[] stringValues;
	private int[] nullsIndices;
	private int[] indexMapping;
	private int count;
	
	public XResult(){
		
	}
	
	public XResult(double[] doubleValues, String[] stringValues, int[] nullIndices, int[] indexMapping) {
		if(doubleValues != null){
			count = doubleValues.length;
		}else if(stringValues != null){
			count = stringValues.length;
		}else{
			throw new IllegalArgumentException("doubles and strings can't be null");
		}
		this.doubleValues = doubleValues;
		this.stringValues = stringValues;
		this.nullsIndices = nullIndices;
		this.indexMapping = indexMapping;
	}

	/**
	 * indexMapping is needed to map queryed dimension order to response dimension order.
	 * e.g. if queryed dimension order was 1, then order that dimension in result will 
	 * be indexMapping[1].
	 */
	public int[] getIndexMapping() {
		return indexMapping;
	}
	
	public int getResultCount(){
		return count;
	}

	public IResultElement get(int index) {
		IResultElement result;
		String sResult = (stringValues != null) ? stringValues[index] : null;
		if(sResult != null){
			result = new ResultString(sResult);
		}
		else if(hasNull(index)){
			result = new ResultString(""); //as palo API
		}
		else{
			result = new ResultDouble(doubleValues[index]);
		}
		return result;
	}

	private boolean hasNull(int index) {
		boolean hasNull = false;
		for (int i = 0; (i < nullsIndices.length) && (index >= nullsIndices[i]) && !hasNull; i++) {
			hasNull = nullsIndices[i] == index;
		}
		return hasNull;
	}

	
	
	
}
