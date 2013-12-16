/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.properties;

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
