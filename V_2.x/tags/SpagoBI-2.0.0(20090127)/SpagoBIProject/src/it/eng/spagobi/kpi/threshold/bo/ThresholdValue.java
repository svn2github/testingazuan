package it.eng.spagobi.kpi.threshold.bo;

public class ThresholdValue extends Threshold {
	private Integer thresholdId = null;
	private Integer severityId = null;
	private String colourString = null;
	
	public Integer getSeverityId() {
		return severityId;
	}

	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}

	public Integer getThresholdId() {
		return thresholdId;
	}

	public void setThresholdId(Integer thresholdId) {
		this.thresholdId = thresholdId;
	}

	public String getColourString() {
		return colourString;
	}

	public void setColourString(String colourString) {
		this.colourString = colourString;
	}

}
