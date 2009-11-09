package it.eng.spagobi.studio.core.bo;

import it.eng.spagobi.sdk.maps.bo.SDKFeature;

public class GeoFeature {

	private String descr;

	private Integer featureId;

	private String name;

	private String svgGroup;

	private String type;

	private Boolean visibleFlag;


	public GeoFeature(SDKFeature sdkFeature) {
		featureId=sdkFeature.getFeatureId();
		name=sdkFeature.getName();
		descr=sdkFeature.getDescr();
		svgGroup=sdkFeature.getSvgGroup();
		type=sdkFeature.getSvgGroup();
		visibleFlag=sdkFeature.getVisibleFlag();
	}

	

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSvgGroup() {
		return svgGroup;
	}

	public void setSvgGroup(String svgGroup) {
		this.svgGroup = svgGroup;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getVisibleFlag() {
		return visibleFlag;
	}

	public void setVisibleFlag(Boolean visibleFlag) {
		this.visibleFlag = visibleFlag;
	}

}	    



