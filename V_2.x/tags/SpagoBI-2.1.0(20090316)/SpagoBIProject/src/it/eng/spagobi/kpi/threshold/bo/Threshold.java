package it.eng.spagobi.kpi.threshold.bo;

import java.awt.Color;

public class Threshold {
	
	Integer Id = null;
	String type = null;//type could be interval/min/max
	Double minValue = null;// null if type = max
	Double maxValue = null;// null if type = min
	String label = null;
	Color color = null;
	String severity = null;
	Integer position = null;
	
	String thresholdName = null;//all thresholds referred to the same kpi will have this Name equal
	String thresholdDescription = null;//all thresholds referred to the same kpi will have this description equal
	String thresholdCode = null;
	Integer thresholdTypeId = null;
	
	
	
	public Integer getThresholdTypeId() {
		return thresholdTypeId;
	}

	public void setThresholdTypeId(Integer thresholdTypeId) {
		this.thresholdTypeId = thresholdTypeId;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getThresholdName() {
		return thresholdName;
	}

	public void setThresholdName(String thresholdName) {
		this.thresholdName = thresholdName;
	}

	public String getThresholdDescription() {
		return thresholdDescription;
	}

	public void setThresholdDescription(String thresholdDescription) {
		this.thresholdDescription = thresholdDescription;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Threshold() {
		super();
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getThresholdCode() {
		return thresholdCode;
	}

	public void setThresholdCode(String thresholdCode) {
		this.thresholdCode = thresholdCode;
	}
	
	

}
