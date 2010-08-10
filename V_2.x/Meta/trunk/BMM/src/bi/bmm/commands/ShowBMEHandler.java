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
package bi.bmm.commands;


import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;

import bi.bmm.util.HunkIO;
import bi.bmm.wizards.CreateProjectWizard;

/**
 * Shows the given perspective. If no perspective is specified in the
 * parameters, then this opens the perspective selection dialog.
 * 
 * @since 3.1
 */
public final class ShowBMEHandler extends AbstractHandler {

	/**
	 * The name of the parameter providing the perspective identifier.
	 */
	private static final String PERSPECTIVE_ID = "bi.bmm.perspectives.bme"; //$NON-NLS-1$

	
	public final Object execute(final ExecutionEvent event)
			throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		
		File f = new File(HunkIO.DEFAULT_INFO_FILE);
		if (!f.exists()) {
			if(openWizard())
			openPerspective(PERSPECTIVE_ID, window);
		}
		else{
			//MC: Aggiunta delete e openWizard
			f.delete();
			if(openWizard())
			openPerspective(PERSPECTIVE_ID, window);
		}
		
		return null;
	}

	/**
	 * Apre il wizard che raccoglie i dati sul nuovo BM
	 * 
	 */

	private boolean openWizard() {
		CreateProjectWizard wizard = new CreateProjectWizard();
		WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.open();
		if (wizard.isCreatedProject()) return true;
		else return false;
	}



	/**
	 * Opens the perspective with the given identifier.
	 * 
	 * @param perspectiveId
	 *            The perspective to open; must not be <code>null</code>
	 * @throws ExecutionException
	 *             If the perspective could not be opened.
	 */
	private final void openPerspective(final String perspectiveId,final IWorkbenchWindow activeWorkbenchWindow)
									throws ExecutionException {
		
		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		IPerspectiveDescriptor desc = activeWorkbenchWindow.getWorkbench()
				.getPerspectiveRegistry().findPerspectiveWithId(perspectiveId);
		
		if (desc == null) {
			throw new ExecutionException("Perspective " + perspectiveId //$NON-NLS-1$
					+ " cannot be found."); //$NON-NLS-1$
		}

		try {
			if (activePage != null) {
				activePage.setPerspective(desc);
			} else {
				activeWorkbenchWindow.openPage(perspectiveId, null);
			}
		} catch (WorkbenchException e) {
			throw new ExecutionException("Perspective could not be opened.", e); //$NON-NLS-1$
		}
	}
}
