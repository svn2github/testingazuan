/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.physical;

import it.eng.spagobi.meta.editor.commons.AbstractSpagoBIModelPartInput;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalModelEditorInput extends AbstractSpagoBIModelPartInput{
	
	public PhysicalModelEditorInput(File resourceFile, PhysicalModel rootObject) {
		super(resourceFile, rootObject);
	}
	
	@Override
	public String getName() {
		return "Physical model: " + super.getName();
	}
}
