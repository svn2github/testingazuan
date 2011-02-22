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
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelEditorAdapterLauncher implements IEditorLauncher {

	@Override
	public void open(IPath path) {
		SpagoBIModelInput modelInput;
		File modelFile;
		URI modelFileURI;
		Model spagobiModel;
		
		// Register the package
		ModelPackage libraryPackage = ModelPackage.eINSTANCE;
		
		modelFile = new File(path.toOSString());
		modelFileURI = URI.createFileURI(modelFile.getAbsolutePath().toString());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(modelFileURI);
		Map<Object, Object> options = new HashMap<Object, Object>();
		
		try {
			resource.load(options);
		} catch (IOException e) {
			SpagoBIMetaModelEditorPlugin.INSTANCE.log(e);
		}
		
		TreeIterator it = resource.getAllContents();
		if(it.hasNext()) {
			spagobiModel = (Model)it.next();
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				page.openEditor( new SpagoBIModelInput(modelFile, spagobiModel) , SpagoBIModelEditor.PLUGIN_ID );
			} catch (PartInitException exception) {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
			}
		}
		
		
		
	}

}
