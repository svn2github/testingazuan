/*
 * DropSourceListener for the TreeViewer inside the GraphicEditorView
 */
package it.eng.spagobi.meta.editor.dnd;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;



public class PhysicalObjectDropListener  extends ViewerDropAdapter {

	private final TreeViewer treeViewer;
	private CoreSingleton coreSingleton = CoreSingleton.getInstance();
	
	public PhysicalObjectDropListener(TreeViewer tree){
		super(tree);
		treeViewer = tree;
	}
	
	// This method performs the actual drop
	@Override
	public boolean performDrop(Object data) {
		//getting physical table
		PhysicalTable physicalTable = coreSingleton.getPhysicalModel().getTable(data.toString());
		if (physicalTable != null){
			//Get Active Window
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			//Launch AddBCWizard
			AddBusinessTableWizard wizard = new AddBusinessTableWizard(physicalTable);
	    	WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.create();
	    	dialog.open();
		}	
		return false;
	}

	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		return TextTransfer.getInstance().isSupportedType(transferType);

	}

}
