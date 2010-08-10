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

package it.eng.spago.error;

import it.eng.spago.base.CloneableObject;

import java.util.Vector;

/**
* Un'istanza di <code>EMFValidationError</code> rappresenta un errore codificato 
* di validazione di un dato inserito dall'utente.
* @version 1.0, 06/03/2002
* @author Davide Zerbetto
* @see EMFUserError
*/

public class EMFValidationError extends EMFUserError {

	public EMFValidationError(String severity, int code) {
		super(severity, code);
		// TODO Auto-generated constructor stub
	}

	public EMFValidationError(String severity, int code, Vector params) {
		super(severity, code, params);
		// TODO Auto-generated constructor stub
	}

	public EMFValidationError(String severity, int code, Vector params,
			Object additionalInfo) {
		super(severity, code, params, additionalInfo);
		// TODO Auto-generated constructor stub
	}

	public EMFValidationError(EMFValidationError userError) {
		super(userError);
		// TODO Auto-generated constructor stub
	}

	public EMFValidationError(EMFUserError userError) {
		super(userError);
		// TODO Auto-generated constructor stub
	}
	
	public CloneableObject cloneObject() {
	    return new EMFValidationError(this);
	} // public CloneableObject cloneObject()
}
