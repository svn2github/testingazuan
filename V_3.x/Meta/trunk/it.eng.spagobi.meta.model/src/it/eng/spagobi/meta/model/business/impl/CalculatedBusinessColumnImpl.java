/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.CalculatedBusinessColumn;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Calculated Business Column</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class CalculatedBusinessColumnImpl extends BusinessColumnImpl implements CalculatedBusinessColumn {
	
	public static final String CALCULATED_COLUMN_EXPRESSION = "structural.expression";
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CalculatedBusinessColumnImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BusinessModelPackage.Literals.CALCULATED_BUSINESS_COLUMN;
	}
	
	public List<SimpleBusinessColumn> getReferencedColumns(){
		List<SimpleBusinessColumn> columnsReferenced = new ArrayList<SimpleBusinessColumn>();
		BusinessColumnSet businessColumnSet = this.getTable();
		
		//get Expression String
		String id = this.getPropertyType(CALCULATED_COLUMN_EXPRESSION).getId();
		String expression = this.getProperties().get(id).getValue();
		
		//retrieve columns objects from string
		StringTokenizer stk = new StringTokenizer(expression, "+-|*/()");
		while(stk.hasMoreTokens()){
			String operand = stk.nextToken().trim();
			SimpleBusinessColumn simpleBusinessColumn = businessColumnSet.getSimpleBusinessColumn(operand);
			if (simpleBusinessColumn != null){
				columnsReferenced.add(simpleBusinessColumn);
			}
		}
		return columnsReferenced;	
	}

} //CalculatedBusinessColumnImpl
