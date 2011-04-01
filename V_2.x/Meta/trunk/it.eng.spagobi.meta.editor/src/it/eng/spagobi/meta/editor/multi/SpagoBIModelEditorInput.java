/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.editor.multi;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;
import it.eng.spagobi.meta.editor.business.BusinessModelEditorInput;
import it.eng.spagobi.meta.editor.physical.PhysicalModelEditor;
import it.eng.spagobi.meta.editor.physical.PhysicalModelEditorInput;
import it.eng.spagobi.meta.model.Model;

import java.io.File;

import org.eclipse.core.runtime.IPath;
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
