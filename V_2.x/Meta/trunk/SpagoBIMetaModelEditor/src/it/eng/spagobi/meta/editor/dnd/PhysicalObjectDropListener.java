/*
 * DropSourceListener for the TreeViewer inside the GraphicEditorView
 */
package it.eng.spagobi.meta.editor.dnd;

import java.util.StringTokenizer;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
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
		this.setScrollExpandEnabled(true);
	}
	
	// This method performs the actual drop
	@Override
	public boolean performDrop(Object data) {
		
		//checking if the target of the drop is correct
		Object target = getCurrentTarget();
		int loc = getCurrentLocation();
       	if ( (target instanceof BusinessRootItemProvider) || 
       	   ( (target instanceof BusinessTable) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ) {
       		StringTokenizer stringTokenizer = new StringTokenizer(data.toString(), "##");
       		//obtaining table(s) name(s) from the passed string
       		while (stringTokenizer.hasMoreTokens()){
           		String tableName = stringTokenizer.nextToken();
       			//getting physical table
    			PhysicalTable physicalTable = coreSingleton.getPhysicalModel().getTable(tableName);
    			if (physicalTable != null){
    				//Get Active Window
    				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    				//Launch AddBCWizard
    				AddBusinessTableWizard wizard = new AddBusinessTableWizard(physicalTable);
    		    	WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
    				dialog.create();
    		    	dialog.open();
    			}		
       		}
       		return true;
		}
		else {
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}
		
	}

	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		int loc = getCurrentLocation();
		if ( (target instanceof BusinessRootItemProvider) || 
		       	   ( (target instanceof BusinessTable) && (loc == LOCATION_BEFORE || loc == LOCATION_AFTER)) ) {
			return TextTransfer.getInstance().isSupportedType(transferType);
		}
		else {
			this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}

	}

}
