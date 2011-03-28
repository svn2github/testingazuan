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
package it.eng.spagobi.meta.model.presentation;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.presentation.BusinessModelEditor;
import it.eng.spagobi.meta.model.physical.presentation.PhysicalModelEditor;

import java.io.File;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.MultiEditorInput;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelInput extends MultiEditorInput {
	
	String modelName;
	String modelDescription;

	public SpagoBIModelInput(File modelFile, Model spagobiModel) {
		
		super(
				new String[]{
						BusinessModelEditor.PLUGIN_ID,
						PhysicalModelEditor.PLUGIN_ID
				},
				new IEditorInput[]{
						new BusinessModelInput(modelFile, spagobiModel.getBusinessModels().get(0)),
						new PhysicalModelInput(modelFile, spagobiModel.getPhysicalModels().get(0)), 
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
