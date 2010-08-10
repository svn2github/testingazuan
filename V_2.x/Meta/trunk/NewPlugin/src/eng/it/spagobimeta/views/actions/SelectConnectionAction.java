package eng.it.spagobimeta.views.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import eng.it.spagobimeta.Activator;
import eng.it.spagobimeta.util.DSE_Bridge;
import eng.it.spagobimeta.util.DSE_LabelProvider;
import eng.it.spagobimeta.views.DBStructureView;



public class SelectConnectionAction implements IViewActionDelegate {

	@Override
	public void init(IViewPart arg0) {

	}

	@Override
	public void run(IAction arg0) {
		
		DSE_Bridge dse = new DSE_Bridge();
		IConnectionProfile[] profiles = dse.get_CP();
		
		ListDialog ld = new ListDialog(new Shell());
		ld.setAddCancelButton(true);
		ld.setContentProvider(new ArrayContentProvider());
		ld.setLabelProvider(new DSE_LabelProvider());
		ld.setInput(profiles);
		ld.setInitialSelections(profiles);
		ld.setTitle("Select a connection");
		ld.setHelpAvailable(false);
		ld.setMessage("Select a connection previously defined in the DSE");
		
		//wait for selection
		if (ld.open() == IStatus.OK){
			//obtaining selection
			Object[] res = ld.getResult();
			IConnectionProfile cp = (IConnectionProfile)res[0];
			IViewPart dbStructView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("eng.it.spagobimeta.DBStructureView");
			//invoke tree creation on DBStructureView
			((DBStructureView)dbStructView).createTree(cp);
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {

	}

}
