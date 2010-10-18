/*
 * This action serialize the current Model to a XMI file
 */
package it.eng.spagobi.meta.editor.views.actions;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.serializer.IModelSerializer;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class SerializeBusinessModel implements IViewActionDelegate {

	@Override
	public void run(IAction action) {
		CoreSingleton cs = CoreSingleton.getInstance();
		Model rootModel = cs.getRootModel();
		
		DirectoryDialog dialog = new DirectoryDialog(new Shell(), SWT.NULL);

        // Change the title bar text
        dialog.setText("Business Model save directory selection");

        // Customizable message displayed in the dialog
        dialog.setMessage("Select a directory for saving file");
        String path = dialog.open();
        if (path != null) {
    		String fileName = path+"\\"+cs.getBmName()+".xmi";
    		IModelSerializer serializer = new EmfXmiSerializer();
            serializer.serialize(rootModel, new File(fileName));
        	showMessage("Model saved to XMI File");
        }
        else {
        	showMessage("Error on directory selection");
        }

        
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

	//Show a Message in the Dialog
	private void showMessage(String message)
	{
		MessageDialog.openInformation(new Shell(), "Business Model Editor", message);
	}
}
