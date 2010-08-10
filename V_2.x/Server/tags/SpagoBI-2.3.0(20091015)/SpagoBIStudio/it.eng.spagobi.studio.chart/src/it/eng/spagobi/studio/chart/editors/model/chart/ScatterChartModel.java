package it.eng.spagobi.studio.chart.editors.model.chart;

import it.eng.spagobi.studio.chart.editors.ChartEditor;
import it.eng.spagobi.studio.chart.editors.ChartEditorComponents;
import it.eng.spagobi.studio.chart.utils.DrillConfiguration;
import it.eng.spagobi.studio.chart.utils.ScatterRangeMarker;
import it.eng.spagobi.studio.core.log.SpagoBILogger;

import org.dom4j.Document;
import org.dom4j.Node;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ScatterChartModel extends ChartModel {

	ScatterRangeMarker scatterRangeMarker;


	public void eraseSpecificParameters() {
		scatterRangeMarker=new ScatterRangeMarker();
		super.eraseSpecificParameters();
	}


	public ScatterChartModel(String type, String subType_, IFile thisFile, Document configDocument_) throws Exception {
		super(type, subType_, thisFile, configDocument_);
		scatterRangeMarker=new ScatterRangeMarker();
		scatterRangeMarker.fillScatterRangeRankConfigurations(type, thisDocument);
	}


	@Override
	public void initializeEditor(ChartEditor editor,
			ChartEditorComponents components, FormToolkit toolkit,
			ScrolledForm form) throws Exception {
		// TODO Auto-generated method stub
		super.initializeEditor(editor, components, toolkit, form);
		components.createScatterRangeMarkerSection(this, toolkit, form);
		components.getScatterRangeMarkerEditor().setVisible(true);

	}



	@Override
	public void refreshEditor(ChartEditor editor,
			ChartEditorComponents components, FormToolkit toolkit,
			ScrolledForm form) throws Exception {
		SpagoBILogger.infoLog("Erase fields of editor");
		eraseSpecificParameters();
		super.refreshEditor(editor, components, toolkit, form);
		components.getScatterRangeMarkerEditor().eraseComposite();
		getScatterRangeMarker().fillScatterRangeRankConfigurations(type, thisDocument);
		components.getScatterRangeMarkerEditor().refillFieldsScatterRangeMarker(this,editor, toolkit, form);							
		components.getScatterRangeMarkerEditor().setVisible(true);

	}


	@Override
	public String toXML() {
		String toReturn="";
		SpagoBILogger.infoLog("Write XML for Model");
		toReturn = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
		if(subType==null) {
			SpagoBILogger.errorLog("Sub Type not defined",null);
			return "";
		}

		SpagoBILogger.infoLog("General settings");

		//intestazione
		toReturn+="<SCATTERCHART type=\""+this.subType+"\" name=\""+this.title+"\">\n";

		toReturn+=super.toXML();
		
		toReturn+=scatterRangeMarker.toXML();

		toReturn+="</SCATTERCHART>\n";

		SpagoBILogger.infoLog("Final Template is\n:" + toReturn);
		return toReturn;
	}


	public ScatterRangeMarker getScatterRangeMarker() {
		return scatterRangeMarker;
	}


	public void setScatterRangeMarker(ScatterRangeMarker scatterRangeMarker) {
		this.scatterRangeMarker = scatterRangeMarker;
	}



}
