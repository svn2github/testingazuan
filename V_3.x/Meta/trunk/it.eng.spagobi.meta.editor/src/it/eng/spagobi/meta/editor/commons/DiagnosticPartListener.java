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
package it.eng.spagobi.meta.editor.commons;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DiagnosticPartListener implements IPartListener {
	
	private final Logger logger;
	
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
