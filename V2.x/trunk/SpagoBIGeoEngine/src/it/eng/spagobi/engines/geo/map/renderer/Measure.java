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
package it.eng.spagobi.engines.geo.map.renderer;

import java.util.Properties;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class Measure {
	String columnId;
	String description;
	String aggFunc;
	String colour;
	String tresholdLb;
	String tresholdUb;
	String tresholdCalculatorType;
	Properties tresholdCalculatorParameters;
	String pattern;
	String unit;
	
	String colurOutboundCol;
	String colurNullCol;
	String colurCalculatorType;
	Properties colurCalculatorParameters;
	
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public Properties getColurCalculatorParameters() {
		return colurCalculatorParameters;
	}
	public void setColurCalculatorParameters(Properties colurCalculatorParameters) {
		this.colurCalculatorParameters = colurCalculatorParameters;
	}
	public String getColurCalculatorType() {
		return colurCalculatorType;
	}
	public void setColurCalculatorType(String colurCalculatorType) {
		this.colurCalculatorType = colurCalculatorType;
	}
	public String getColurNullCol() {
		return colurNullCol;
	}
	public void setColurNullCol(String colurNullCol) {
		this.colurNullCol = colurNullCol;
	}
	public String getColurOutboundCol() {
		return colurOutboundCol;
	}
	public void setColurOutboundCol(String colurOutboundCol) {
		this.colurOutboundCol = colurOutboundCol;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Properties getTresholdCalculatorParameters() {
		return tresholdCalculatorParameters;
	}
	public void setTresholdCalculatorParameters(
			Properties tresholdCalculatorParameters) {
		this.tresholdCalculatorParameters = tresholdCalculatorParameters;
	}
	public String getTresholdCalculatorType() {
		return tresholdCalculatorType;
	}
	public void setTresholdCalculatorType(String tresholdCalculatorType) {
		this.tresholdCalculatorType = tresholdCalculatorType;
	}
	public String getTresholdLb() {
		return tresholdLb;
	}
	public void setTresholdLb(String tresholdLb) {
		this.tresholdLb = tresholdLb;
	}
	public String getTresholdUb() {
		return tresholdUb;
	}
	public void setTresholdUb(String tresholdUb) {
		this.tresholdUb = tresholdUb;
	}
	public String getAggFunc() {
		return aggFunc;
	}
	public void setAggFunc(String aggFunc) {
		this.aggFunc = aggFunc;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	} 
}
