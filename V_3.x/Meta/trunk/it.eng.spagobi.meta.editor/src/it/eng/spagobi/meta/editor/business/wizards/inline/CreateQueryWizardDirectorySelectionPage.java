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
package it.eng.spagobi.meta.editor.business.wizards.inline;

import java.net.URL;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.SpagoBIMetaModelEditorPlugin;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author cortella
 *
 */
public class CreateQueryWizardDirectorySelectionPage extends WizardPage {

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	Text textDirectory;
	/**
	 * @param pageName
	 */
	protected CreateQueryWizardDirectorySelectionPage(String pageName) {
		super(pageName);
		setTitle("Create Query");
		setDescription("Please select the directory where to put SpagoBI Query file.");
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
	    if (image!=null) setImageDescriptor(image);	
	    
	}


	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(6, false));
		
		Label lblDirectory = new Label(container, SWT.NONE);
		lblDirectory.setText("Directory:");
		
	    // Input text box
	    textDirectory = new Text(container, SWT.BORDER);
	    GridData gd_textDirectory = new GridData(GridData.FILL_HORIZONTAL);
	    gd_textDirectory.horizontalSpan = 4;
	    textDirectory.setLayoutData(gd_textDirectory);

	    // Browse button to select directory
	    Button buttonBrowse = new Button(container, SWT.PUSH);
	    buttonBrowse.setText("Browse...");
	    buttonBrowse.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        DirectoryDialog dlg = new DirectoryDialog(new Shell());

	        // Set the initial filter path according
	        // to anything they've selected or typed in
	        dlg.setFilterPath(textDirectory.getText());

	        dlg.setText("Select Directory");
	        dlg.setMessage("Select a directory");

	        // Calling open() will open and run the dialog.
	        // It will return the selected directory, or
	        // null if user cancels
	        String dir = dlg.open();
	        if (dir != null) {
	          // Set the text box to the new selection
	          textDirectory.setText(dir);
	        }
	        
	        checkPageComplete();
	      }
	    });

	}
	
	private void checkPageComplete(){
		if (textDirectory.getText().length() > 0){
			this.setPageComplete(true);
		} else {
			this.setPageComplete(false);
		}	
	}
	
	public String getSelectedDirectory(){
		return textDirectory.getText();
	}

}
