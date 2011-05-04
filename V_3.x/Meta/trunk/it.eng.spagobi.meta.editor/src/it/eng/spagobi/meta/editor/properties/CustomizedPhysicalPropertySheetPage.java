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
package it.eng.spagobi.meta.editor.properties;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;
import it.eng.spagobi.meta.editor.physical.PhysicalModelEditor;

import java.util.List;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.properties.PropertySheetSorter;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CustomizedPhysicalPropertySheetPage extends ExtendedPropertySheetPage {
	PhysicalModelEditor physicalEditor;
	
	public CustomizedPhysicalPropertySheetPage(PhysicalModelEditor businessEditor,
			PropertySheetSorter propertySheetSorter) {
		super((AdapterFactoryEditingDomain)businessEditor.getEditingDomain());
		this.physicalEditor = businessEditor;
		this.setSorter(propertySheetSorter);
	}
	
	@Override
	public void setSelectionToViewer(List<?> selection) {
		physicalEditor.setSelectionToViewer(selection);
		physicalEditor.setFocus();
	}

	@Override
	public void setActionBars(IActionBars actionBars) {
		super.setActionBars(actionBars);
		physicalEditor.getActionBarContributor().shareGlobalActions(this, actionBars);
	}
}
