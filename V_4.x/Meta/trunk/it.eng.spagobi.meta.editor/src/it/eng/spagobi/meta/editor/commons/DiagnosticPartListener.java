/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.commons;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DiagnosticPartListener implements IPartListener {
	
	protected final Logger logger;
	
	public DiagnosticPartListener(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public void partActivated(IWorkbenchPart p) {
		logger.debug("Activated part [{}]", p.getClass().getName());
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart p) {
		logger.debug("Brought to top part [{}]", p.getClass().getName());
	}

	@Override
	public void partClosed(IWorkbenchPart p) {
		logger.debug("Closed part [{}]", p.getClass().getName());
	}

	@Override
	public void partDeactivated(IWorkbenchPart p) {
		logger.debug("Deactivated part [{}]", p.getClass().getName());
	}

	@Override
	public void partOpened(IWorkbenchPart p) {
		logger.debug("Opened part [{}]", p.getClass().getName());
	}
}
