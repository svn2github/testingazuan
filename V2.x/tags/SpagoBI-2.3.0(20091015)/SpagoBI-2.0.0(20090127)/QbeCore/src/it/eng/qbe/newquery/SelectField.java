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
package it.eng.qbe.newquery;

import java.util.HashMap;
import java.util.Map;

import it.eng.qbe.model.structure.DataMartField;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SelectField {
	private String uniqueName;
	private String alias;
	private IAggregationFunction function;
	private boolean groupByField;
	private String orderType;
	private boolean visible;
	
	public static Map aggregationFunctions;
	public static String NONE = "NONE";
	public static String SUM = "SUM";
	public static String AVG = "AVG";
	public static String MAX = "MAX";
	public static String MIN = "MIN";	
	static {
		aggregationFunctions = new HashMap();
		aggregationFunctions.put(NONE, new IAggregationFunction() {
			public String getName() {return NONE;}
			public String apply(String fieldName) {
				return fieldName;
			}
		});
		aggregationFunctions.put(SUM, new IAggregationFunction() {
			public String getName() {return SUM;}
			public String apply(String fieldName) {
				return "SUM(" + fieldName + ")";
			}
		});
		aggregationFunctions.put(AVG, new IAggregationFunction() {
			public String getName() {return AVG;}
			public String apply(String fieldName) {
				return "AVG(" + fieldName + ")";
			}
		});
		aggregationFunctions.put(MAX, new IAggregationFunction() {
			public String getName() {return MAX;}
			public String apply(String fieldName) {
				return "MAX(" + fieldName + ")";
			}
		});
		aggregationFunctions.put(MIN, new IAggregationFunction() {
			public String getName() {return MIN;}
			public String apply(String fieldName) {
				return "MIN(" + fieldName + ")";
			}
		});
	}
	

	
	public SelectField(String uniqueName, String function, String alias, boolean visible,
			boolean groupByField, String orderType ) {
		this.uniqueName = uniqueName;
		this.alias = alias;
		if(function == null || !aggregationFunctions.containsKey( function )) {
			function = NONE;
		}
		this.function = (IAggregationFunction)aggregationFunctions.get(function);
		setVisible( visible );
		this.setGroupByField(groupByField);
		this.setOrderType(orderType);
	}

	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}	

	public IAggregationFunction getFunction() {
		return function;
	}

	public void setFunction(IAggregationFunction function) {
		this.function = function;
	}	
	
	public static interface IAggregationFunction {	
		String getName();
		String apply(String fieldName);
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}


	public boolean isVisible() {
		return visible;
	}


	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	public boolean isGroupByField() {
		return groupByField;
	}


	public void setGroupByField(boolean groupByField) {
		this.groupByField = groupByField;
	}


	public boolean isOrderByField() {
		return "ASC".equalsIgnoreCase( getOrderType() )
			|| "DESC".equalsIgnoreCase( getOrderType() );
	}

	public boolean isAscendingOrder() {
		return "ASC".equalsIgnoreCase( getOrderType() );
	}
	


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
}
