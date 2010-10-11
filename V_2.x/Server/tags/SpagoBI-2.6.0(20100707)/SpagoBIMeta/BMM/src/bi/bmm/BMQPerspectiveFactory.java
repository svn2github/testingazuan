/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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

@Author Marco Cortella

**/
package bi.bmm;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class BMQPerspectiveFactory implements IPerspectiveFactory {
	
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		 
		 defineActions(layout);
		 defineLayout(layout);
	}
	
	public void defineActions(IPageLayout layout) {
        // Add "new wizards".
		//TODO: AGGIUNGERE WIZARDS
        
}
	public void defineLayout(IPageLayout layout) {
		// Editors are placed for free.
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        // Place navigator and outline to left of
        // editor area.

        IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, (float) 0.75, editorArea);
        right.addView("bi.bmm.views.bmq.bmqresource");    
       
        IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75, editorArea);
        bottom.addView("bi.bmm.views.bmq.bmpools");   
        
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, editorArea);
        left.addView("bi.bmm.views.bmq.bmqeditor");
        left.addView("bi.bmm.views.bmq.cbcqeditor");
        
}
}
