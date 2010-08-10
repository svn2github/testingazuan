package it.eng.spagobi.studio.chart.utils;

import it.eng.spagobi.studio.chart.editors.ChartEditor;
import it.eng.spagobi.studio.chart.editors.ChartEditorUtils;
import it.eng.spagobi.studio.core.log.SpagoBILogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.eclipse.swt.graphics.RGB;

public class Interval {

	RGB color=null;
	Double min=null;
	Double max=null;
	String label=null;



	public RGB getColor() {
		return color;
	}




	public void setColor(RGB color) {
		this.color = color;
	}




	public Double getMin() {
		return min;
	}




	public void setMin(Double min) {
		this.min = min;
	}




	public Double getMax() {
		return max;
	}




	public void setMax(Double max) {
		this.max = max;
	}




	public String getLabel() {
		return label;
	}




	public void setLabel(String label) {
		this.label = label;
	}

	public String toXML(){
		String toReturn="";
		toReturn+="<INTERVAL ";
		if(label!=null){
			toReturn+="label='"+label+"' ";	
		}
		if(min!=null){
			toReturn+="min='"+min.toString()+"' ";	
		}
		if(max!=null){
			toReturn+="max='"+max.toString()+"' ";	
		}
		if(color!=null){
			toReturn+="color='"+ChartEditor.convertRGBToHexadecimal(color)+"' ";	
		}
		toReturn+=" />";
		return toReturn;
	}


}
