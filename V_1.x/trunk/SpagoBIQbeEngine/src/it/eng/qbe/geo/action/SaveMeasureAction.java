/**

 SpagoBI - The Business Intelligence Free Platform

 Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.qbe.geo.action;

import java.util.Properties;

import it.eng.qbe.geo.configuration.DatamartProviderConfiguration;
import it.eng.qbe.geo.configuration.MapRendererConfiguration;
import it.eng.spago.base.SourceBean;


/**
 * @author Andrea Gioia
 * 
 */
public class SaveMeasureAction extends GeoAbstractAction {
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		super.service(request, response);
		
		MapRendererConfiguration.Measure measure;
		measure = (MapRendererConfiguration.Measure)getRequestContainer().getSessionContainer().getAttribute("MEASURE");
		
		String measureName = (String)request.getAttribute("measureName");
		measure.setColumnId(measureName);
		String description = (String)request.getAttribute("description");
		measure.setDescription(description);
		String aggFunc = (String)request.getAttribute("aggFunc");
		measure.setAggFunc(aggFunc);
		String color = (String)request.getAttribute("color");
		measure.setColour(color);
		String trasholdCalculationType = (String)request.getAttribute("trasholdCalculationType");
		measure.setTresholdCalculatorType(trasholdCalculationType);
		String lb = (String)request.getAttribute("lb");
		measure.setTresholdLb(lb);
		String ub = (String)request.getAttribute("ub");
		measure.setTresholdUb(ub);
		Properties tresholdCalculatorParameters = new Properties();
		//tresholdCalculatorParameters.setProperty("range", "");	
		//tresholdCalculatorParameters.setProperty("GROUPS_NUMBER", "");	
		if(trasholdCalculationType.equalsIgnoreCase("static")) {
			String trasholdRange = (String)request.getAttribute("trasholdRange");
			tresholdCalculatorParameters.setProperty("range", trasholdRange);			
		} else if (trasholdCalculationType.equalsIgnoreCase("quantile")) {
			String trasholdQuantileNo = (String)request.getAttribute("trasholdQuantileNo");
			tresholdCalculatorParameters.setProperty("GROUPS_NUMBER", trasholdQuantileNo);	
		} else if (trasholdCalculationType.equalsIgnoreCase("perc")) {
			String trasholdPercRange = (String)request.getAttribute("trasholdPercRange");
			tresholdCalculatorParameters.setProperty("range", trasholdPercRange);		
		}  else if (trasholdCalculationType.equalsIgnoreCase("uniform")) {
			String trasholdGroupsNo = (String)request.getAttribute("trasholdGroupsNo");
			tresholdCalculatorParameters.setProperty("GROUPS_NUMBER", trasholdGroupsNo);		
		}
		measure.setTresholdCalculatorParameters(tresholdCalculatorParameters);
		
		
		
		String colorCalculationType = (String)request.getAttribute("colorCalculationType");
		measure.setColurCalculatorType(colorCalculationType);
		String obColor = (String)request.getAttribute("obColor");
		measure.setColurOutboundCol(obColor);
		String nvColor = (String)request.getAttribute("nvColor");
		measure.setColurNullCol(nvColor);
		Properties colorCalculatorParameters = new Properties();
		//colorCalculatorParameters.setProperty("range", "");	
		//colorCalculatorParameters.setProperty("BASE_COLOR", "");	
		if(colorCalculationType.equalsIgnoreCase("static")) {
			String colorRange = (String)request.getAttribute("colorRange");
			colorCalculatorParameters.setProperty("range", colorRange);			
		} else if (colorCalculationType.equalsIgnoreCase("grad")) {
			String colorBaseColor = (String)request.getAttribute("colorBaseColor");
			colorCalculatorParameters.setProperty("BASE_COLOR", colorBaseColor);		
		} 
		measure.setColurCalculatorParameters(colorCalculatorParameters);
		
		
		//mapConfiguration.getMapRendererConfiguration().addMeasure(measure);

		//getRequestContainer().getSessionContainer().delAttribute("MEASURE");		
	}
}
