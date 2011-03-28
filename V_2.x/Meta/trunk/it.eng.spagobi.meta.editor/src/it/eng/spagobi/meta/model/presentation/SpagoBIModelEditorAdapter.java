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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiEditor;

/**
 * This class is never istantiated. We use it in order to define a launcher for file with extesion ".sbimodel"
 * The launcher then instatiate programmatically the real SpagoBI Model Multi Editor that is SpagoBIModelEditor
 * 
 * @see http://www.eclipse.org/forums/index.php?t=tree&th=97070&#page_top
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelEditorAdapter extends MultiEditor {

	@Override
	protected void drawGradient(IEditorPart arg0, Gradient arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPartControl(Composite arg0) {
		// TODO Auto-generated method stub
	}	
}
