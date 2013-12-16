/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.multi;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;
import it.eng.spagobi.meta.editor.business.BusinessModelEditorInput;
import it.eng.spagobi.meta.editor.physical.PhysicalModelEditor;
import it.eng.spagobi.meta.editor.physical.PhysicalModelEditorInput;
import it.eng.spagobi.meta.model.Model;

import java.io.File;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.MultiEditorInput;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelEditorInput extends MultiEditorInput {
	
	String modelName;
	String modelDescription;
	
	
	public SpagoBIModelEditorInput(File modelFile, Model spagobiModel) {
		
		super(
			new String[]{
				BusinessModelEditor.EDITOR_ID,
				PhysicalModelEditor.EDITOR_ID
			},
			new IEditorInput[]{
				new BusinessModelEditorInput(modelFile, spagobiModel.getBusinessModels().get(0)),
				new PhysicalModelEditorInput(modelFile, spagobiModel.getPhysicalModels().get(0)), 
			}
		);
		
		modelDescription = modelFile.getAbsolutePath();
		modelName = modelFile.getName();
	}
	
	@Override
	public String getName() {
		return modelName;
	}
	
	public String getToolTipText() {
		return modelDescription;
	}
}
