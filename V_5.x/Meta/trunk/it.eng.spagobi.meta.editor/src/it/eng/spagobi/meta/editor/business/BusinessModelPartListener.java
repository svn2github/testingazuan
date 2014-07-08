/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business;

import it.eng.spagobi.meta.editor.commons.DiagnosticPartListener;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;
import org.slf4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelPartListener extends DiagnosticPartListener {
	
	BusinessModelEditor editor;
	
	BusinessModelPartListener(BusinessModelEditor editor, Logger logger) {
		super(logger);
		this.editor = editor;
	}

	public void partActivated(IWorkbenchPart p) {
		logger.trace("IN");
		
		logger.debug("Activated part [{}]", p.getClass().getName());
		if (p instanceof PropertySheet) {
			logger.debug("Activated [{}]", PropertySheet.class.getName());
			if (((PropertySheet)p).getCurrentPage() == editor.getPropertySheetPage()) {
				logger.debug("Activated the property sheet  of this editor");
				editor.getActionBarContributor().setActiveEditor(editor);

			}
		} else if (p == editor) {
			logger.debug("Activated [{}]", BusinessModelEditor.class.getName());
		}
		
		logger.trace("OUT");
	}

}
