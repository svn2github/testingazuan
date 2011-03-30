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
