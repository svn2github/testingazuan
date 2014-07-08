/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.multi.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * @author cortella
 *
 */
public class NewQueryFileProjectExplorerWizardPage  extends WizardNewFileCreationPage{

	/**
	 * @param pageName
	 * @param selection
	 */
	public NewQueryFileProjectExplorerWizardPage(
			IStructuredSelection selection) {
        super("NewQueryFileWizardPage", selection);
        setTitle("QueryFile");
        setDescription("Creates a new Query File");
        setFileExtension("metaquery");
	}

}
