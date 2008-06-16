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
public class WhereField {
	private String uniqueName;
	private IConditionalOperator operator;
	private Object rightHandValue;
	
	public static Map conditionalOperator;
	
	public static final String EQUALS_TO = "EQUALS TO";
	public static final String NOT_EQUALS_TO = "NOT EQUALS TO";
	public static final String GREATER_THAN = "GREATER THAN";
	public static final String EQUALS_OR_GREATER_THAN = "EQUALS OR GREATER THAN";
	public static final String LESS_THAN = "LESS THAN";
	public static final String EQUALS_OR_LESS_THAN = "EQUALS OR LESS THAN";
	public static final String STARTS_WITH = "STARTS WITH";
	public static final String NOT_STARTS_WITH = "NOT STARTS WITH";
	public static final String ENDS_WITH = "ENDS WITH";
	
	/** The Constant NOT_ENDS_WITH. */
	public static final String NOT_ENDS_WITH = "NOT ENDS WITH";
	
	/** The Constant NOT_NULL. */
	public static final String NOT_NULL = "NOT NULL";
	
	/** The Constant IS_NULL. */
	public static final String IS_NULL = "IS NULL";
	
	/** The Constant CONTAINS. */
	public static final String CONTAINS = "CONTAINS";
	
	/** The Constant NOT_CONTAINS. */
	public static final String NOT_CONTAINS = "NOT CONTAINS";
	
	
	
	static {
		conditionalOperator = new HashMap();
		conditionalOperator.put(EQUALS_TO, new IConditionalOperator() {
			public String getName() {return EQUALS_TO;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + "=" + rightHandValue;
			}
		});
		conditionalOperator.put(NOT_EQUALS_TO, new IConditionalOperator() {
			public String getName() {return NOT_EQUALS_TO;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + "!=" + rightHandValue;
			}
		});
		conditionalOperator.put(GREATER_THAN, new IConditionalOperator() {
			public String getName() {return GREATER_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + ">" + rightHandValue;
			}
		});
		conditionalOperator.put(EQUALS_OR_GREATER_THAN, new IConditionalOperator() {
			public String getName() {return EQUALS_OR_GREATER_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + ">=" + rightHandValue;
			}
		});
		conditionalOperator.put(LESS_THAN, new IConditionalOperator() {
			public String getName() {return LESS_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + "<" + rightHandValue;
			}
		});
		conditionalOperator.put(EQUALS_OR_LESS_THAN, new IConditionalOperator() {
			public String getName() {return EQUALS_OR_LESS_THAN;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + "<=" + rightHandValue;
			}
		});
		conditionalOperator.put(STARTS_WITH, new IConditionalOperator() {
			public String getName() {return STARTS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {				
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = rightHandValue + "%";
				return leftHandValue + " like '" + rightHandValue + "'";
			}
		});
		conditionalOperator.put(NOT_STARTS_WITH, new IConditionalOperator() {
			public String getName() {return NOT_STARTS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = rightHandValue + "%";
				return leftHandValue + " not like '" + rightHandValue + "'";
			}
		});
		conditionalOperator.put(ENDS_WITH, new IConditionalOperator() {
			public String getName() {return ENDS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = "%" + rightHandValue;
				return leftHandValue + " like '" + rightHandValue + "'";
			}
		});
		conditionalOperator.put(NOT_ENDS_WITH, new IConditionalOperator() {
			public String getName() {return NOT_ENDS_WITH;}
			public String apply(String leftHandValue, String rightHandValue) {
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = "%" + rightHandValue;
				return leftHandValue + " not like '" + rightHandValue + "'";
			}
		});		
		conditionalOperator.put(CONTAINS, new IConditionalOperator() {
			public String getName() {return CONTAINS;}
			public String apply(String leftHandValue, String rightHandValue) {
				rightHandValue = rightHandValue.trim();
				rightHandValue = rightHandValue.substring(1, rightHandValue.length()-1);
				rightHandValue = "%" + rightHandValue + "%";
				return leftHandValue + " like '" + rightHandValue + "'";
			}
		});
		conditionalOperator.put(IS_NULL, new IConditionalOperator() {
			public String getName() {return IS_NULL;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + " IS NULL";
			}
		});
		conditionalOperator.put(NOT_NULL, new IConditionalOperator() {
			public String getName() {return NOT_NULL;}
			public String apply(String leftHandValue, String rightHandValue) {
				return leftHandValue + " IS NOT NULL";
			}
		});
	}
	
	public WhereField(String uniqueName, String operator, Object rightHandValue) {
		setUniqueName(uniqueName);
		setOperator( operator );
		setRightHandValue( rightHandValue );
	}
	
	public WhereField(String uniqueName, String operator) {
		setUniqueName(uniqueName);
		setOperator( operator );
		setRightHandValue( null );
	}
	
	public static interface IConditionalOperator {	
		String getName();
		String apply(String leftHandValue, String rightHandValue);
	}

	public IConditionalOperator getOperator() {
		return operator;
	}

	public void setOperator(IConditionalOperator operator) {
		this.operator = operator;
	}
	
	public void setOperator(String operatorName) {
		this.operator = (IConditionalOperator)conditionalOperator.get( operatorName );
	}

	public Object getRightHandValue() {
		return rightHandValue;
	}

	public void setRightHandValue(Object rightHandValue) {
		this.rightHandValue = rightHandValue;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
}
