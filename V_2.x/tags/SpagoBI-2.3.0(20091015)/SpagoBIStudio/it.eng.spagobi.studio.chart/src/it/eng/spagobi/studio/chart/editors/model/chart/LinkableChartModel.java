package it.eng.spagobi.studio.chart.editors.model.chart;

import it.eng.spagobi.studio.chart.utils.DrillConfiguration;

import org.dom4j.Document;
import org.eclipse.core.resources.IFile;

public class LinkableChartModel extends ChartModel {

	// ********* subtype drill Parameters ***********
	protected DrillConfiguration drillConfiguration=null;


	public DrillConfiguration getDrillConfiguration() {
		return drillConfiguration;
	}

	public void setDrillConfiguration(DrillConfiguration drillConfiguration) {
		this.drillConfiguration = drillConfiguration;
	}

	
	public LinkableChartModel(String type_, String subType_, IFile thisFile,
			Document configDocument_)
			throws Exception {
		super(type_, subType_, thisFile, configDocument_);
		// TODO Auto-generated constructor stub
	}

	
}
