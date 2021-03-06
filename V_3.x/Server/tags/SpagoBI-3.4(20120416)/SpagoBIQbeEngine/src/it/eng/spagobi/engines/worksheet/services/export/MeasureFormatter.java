/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.engines.worksheet.services.export;

import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.engines.qbe.crosstable.CrossTab;
import it.eng.spagobi.engines.qbe.crosstable.CrossTab.MeasureInfo;
import it.eng.spagobi.engines.qbe.crosstable.serializer.json.CrosstabSerializationConstants;
import it.eng.spagobi.engines.worksheet.bo.MeasureScaleFactorOption;
import it.eng.spagobi.tools.dataset.common.metadata.IFieldMetaData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class MeasureFormatter {

		List<MeasureInfo> measuresInfo;
		//String[][] measureMetadata;
		boolean measureOnRow;
		DecimalFormat numberFormat;
		String pattern;
		
//		public MeasureFormatter(JSONObject crosstabDefinitionJSON, DecimalFormat numberFormat, String pattern) throws SerializationException, JSONException{
//			JSONArray measuresJSON = crosstabDefinitionJSON.optJSONArray(CrosstabSerializationConstants.MEASURES);
//			JSONObject config =  crosstabDefinitionJSON.optJSONObject(CrosstabSerializationConstants.CONFIG);
//			//Assert.assertTrue(rows != null && rows.length() > 0, "No measures specified!");
//			this.pattern = pattern;
//			this.numberFormat=numberFormat;
//			if (measuresJSON != null) {
//				measureMetadata = new String[measuresJSON.length()][3];
//				for (int i = 0; i < measuresJSON.length(); i++) {
//					JSONObject obj = (JSONObject) measuresJSON.get(i);
//					measureMetadata[i][0] = obj.getString("name");
//					measureMetadata[i][1] = obj.getString("format");
//					measureMetadata[i][2] = obj.getString("type");
//				}
//			}
//			measureOnRow = false;
//			if(config!=null){
//				measureOnRow = config.optString(CrosstabSerializationConstants.MEASURESON).equals(CrosstabSerializationConstants.ROWS);
//			}
//		}
//		
		
		public MeasureFormatter(JSONObject crosstabDefinitionJSON, DecimalFormat numberFormat, String pattern) throws SerializationException, JSONException{
			
			JSONArray measuresJSON = crosstabDefinitionJSON.optJSONArray(CrosstabSerializationConstants.MEASURES);
			JSONObject config =  crosstabDefinitionJSON.optJSONObject(CrosstabSerializationConstants.CONFIG);
			this.pattern = pattern;
			this.numberFormat=numberFormat;
			if (measuresJSON != null) {
				measuresInfo = new ArrayList<MeasureInfo>();
				for (int i = 0; i < measuresJSON.length(); i++) {
					JSONObject obj = (JSONObject) measuresJSON.get(i);
					MeasureInfo mi = new MeasureInfo(obj.getString("name"), "", obj.getString("type"), obj.getString("format"));
					measuresInfo.add(mi);
				}
			}
			measureOnRow = false;
			if(config!=null){
				measureOnRow = config.optString(CrosstabSerializationConstants.MEASURESON).equals(CrosstabSerializationConstants.ROWS);
			}
		}
		
		public MeasureFormatter(CrossTab crosstab, DecimalFormat numberFormat, String pattern) throws SerializationException, JSONException{
			this.measuresInfo = crosstab.getMeasures();
			this.measureOnRow = crosstab.isMeasureOnRow();
		}
		
		public String getFormat(Float f, int positionI, int positionJ) {
			int pos;
			String formatted="";
			if(measureOnRow){
				pos = positionI%measuresInfo.size();
			}else{
				pos = positionJ%measuresInfo.size();
			}
			try {
				String decimalPrecision =  (new JSONObject(measuresInfo.get(pos).getFormat())).optString(IFieldMetaData.DECIMALPRECISION);
				if(decimalPrecision!=null){
					DecimalFormat numberFormat = new DecimalFormat(pattern);
					numberFormat.setMinimumFractionDigits(new Integer(decimalPrecision));
					numberFormat.setMaximumFractionDigits(new Integer(decimalPrecision));
					formatted = numberFormat.format(f);
				}
			} catch (Exception e) {
				formatted = numberFormat.format(f);
			}
			return formatted;
		}
		
		public int getFormatXLS(int positionI, int positionJ) {
			int pos;

			if(measureOnRow){
				pos = positionI%measuresInfo.size();
			}else{
				pos = positionJ%measuresInfo.size();
			}
			try {
				String decimalPrecision =  (new JSONObject(measuresInfo.get(pos).getFormat())).optString(IFieldMetaData.DECIMALPRECISION);
				return new Integer(decimalPrecision);
			} catch (Exception e) {
				return 2;
			}
		}
		
		public Double applyScaleFactor(Double value, int positionI, int positionJ) {
			String scaleFactor = "";
			int pos;
			
			if(measureOnRow){
				pos = positionI%measuresInfo.size();
			}else{
				pos = positionJ%measuresInfo.size();
			}
			scaleFactor = measuresInfo.get(pos).getScaleFactor();
			
			return MeasureScaleFactorOption.applyScaleFactor(value, scaleFactor);


		}

}
