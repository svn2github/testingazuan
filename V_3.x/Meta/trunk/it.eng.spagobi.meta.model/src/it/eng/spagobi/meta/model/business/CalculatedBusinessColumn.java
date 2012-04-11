/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;

import java.util.List;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Calculated Business Column</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getCalculatedBusinessColumn()
 * @model
 * @generated
 */
public interface CalculatedBusinessColumn extends BusinessColumn {
	
	
	List<SimpleBusinessColumn> getReferencedColumns() throws SpagoBIPluginException;
} // CalculatedBusinessColumn
