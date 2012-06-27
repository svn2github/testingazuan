/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.multi;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.part.MultiEditor;
import org.eclipse.ui.part.MultiEditorInput;

/**
 * This class is never instantiated. We use it in order to define a launcher for file with extesion ".sbimodel"
 * The launcher then instantiate programmatically the real SpagoBI Model Multi Editor that is SpagoBIModelEditor
 * 
 * @see http://www.eclipse.org/forums/index.php?t=tree&th=97070&#page_top
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelEditorAdapter extends MultiEditor {
	
	// this class must be never instantiated and also it must be never initialized
	public void init(IEditorSite site, IEditorInput input) {
		throw new SpagoBIPluginException("The editor [" + SpagoBIModelEditorAdapter.class.getName() + "] is not designed to be instatiated");
	}
	
	public void init(IEditorSite site, MultiEditorInput  input) {
		throw new SpagoBIPluginException("The editor [" + SpagoBIModelEditorAdapter.class.getName() + "] is not designed to be instatiated");
	}

	
	// MultiEditor is an abstract class so we have to add the following dummy ovverides 
	// to make this SpagoBIModelEditorAdapter not abstract
	
	@Override
	protected void drawGradient(IEditorPart arg0, Gradient arg1) {}

	@Override
	public void createPartControl(Composite arg0) {}

}
