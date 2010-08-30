/*
 * This class implements the action for the select connection button in  the
 * DBStructureView. It shows the currently defined Connection Profiles in the
 * DSE View
 */

package eng.it.spagobimeta.views.actions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import eng.it.spagobimeta.util.DSEBridge;
import eng.it.spagobimeta.util.CPLabelProvider;
import eng.it.spagobimeta.views.DBStructureView;



public class SelectConnectionAction implements IViewActionDelegate {

	private IViewPart dbStructView;
	private IConnectionProfile cp;
	@Override
	public void init(IViewPart arg0) {

	}

	@Override
	public void run(IAction arg0) {
		
		//Create a new DSEBridge Object
		DSEBridge dse = new DSEBridge();
		IConnectionProfile[] profiles = dse.get_CP();
		
		//Create ListDialog UI
		ListDialog ld = new ListDialog(new Shell());
		ld.setAddCancelButton(true);
		ld.setContentProvider(new ArrayContentProvider());
		ld.setLabelProvider(new CPLabelProvider());
		ld.setInput(profiles);
		ld.setInitialSelections(profiles);
		ld.setTitle("Select a connection");
		ld.setHelpAvailable(false);
		ld.setMessage("Select a connection previously defined in the DSE");
		
		//wait for selection
		if (ld.open() == IStatus.OK){
			//obtaining selection
			Object[] res = ld.getResult();
			cp = (IConnectionProfile)res[0];
			//get View reference
			dbStructView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("eng.it.spagobimeta.DBStructureView");
			//invoke tree creation on DBStructureView
			((DBStructureView)dbStructView).createTree(cp);
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {

	}

}
