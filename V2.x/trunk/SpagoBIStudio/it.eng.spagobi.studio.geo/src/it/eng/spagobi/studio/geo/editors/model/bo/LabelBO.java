package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiParam;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiSettings;
import it.eng.spagobi.studio.geo.editors.model.geo.Label;
import it.eng.spagobi.studio.geo.editors.model.geo.Labels;
import it.eng.spagobi.studio.geo.editors.model.geo.Window;
import it.eng.spagobi.studio.geo.editors.model.geo.Windows;

import java.util.Vector;

public class LabelBO {
	public static Label setNewLabel(GEODocument geoDocument){
		GuiSettings guiSettings = GuiSettingsBO.getGuiSettings(geoDocument);
		if(guiSettings == null){
			guiSettings = new GuiSettings();
			geoDocument.getMapRenderer().setGuiSettings(guiSettings);
		}		
		Labels labels = guiSettings.getLabels();
		if(labels == null){
			labels = new Labels();
			guiSettings.setLabels(labels);			
		}
		Vector <Label> labelVect = labels.getLabel();
		if(labelVect == null){
			labelVect = new Vector<Label>();
			labels.setLabel(labelVect);
		}
		Label label = new Label();
		labelVect.add(label);
		return label;
	}
	public static GuiParam getParamByName(Label label, String paramName){
		
		Vector<GuiParam> params = label.getParams();
		if(params != null){
			for(int i=0; i<params.size(); i++){
				if(params.elementAt(i).getName()!= null && params.elementAt(i).getName().equalsIgnoreCase(paramName)){
					return params.elementAt(i);
				}
			}			
		}
		return null;
	}
	public static Label getLabelByPosition(GEODocument geoDocument, String position){
		Label label = null;
		try{
			GuiSettings guiSettings = GuiSettingsBO.getGuiSettings(geoDocument);
			Labels labels = guiSettings.getLabels();
			Vector <Label> labelVect = labels.getLabel();
			for(int i=0; i<labelVect.size(); i++){
				if(labelVect.elementAt(i).getPosition().equalsIgnoreCase(position)){
					label = labelVect.elementAt(i);
				}
			}
		}finally{
			return label;
		}		
	}
	public static void deleteParamByName(Label label, String paramName){
		
		Vector<GuiParam> params = label.getParams();
		if(params != null){
			for(int i=0; i<params.size(); i++){
				if(params.elementAt(i).getName()!= null && params.elementAt(i).getName().equalsIgnoreCase(paramName)){
					params.remove(i);
				}
			}			
		}

	}

}
