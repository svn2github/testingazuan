package it.eng.spagobi.meta.editor.views.actions;

import it.eng.spagobi.meta.editor.views.BusinessModelView;
import it.eng.spagobi.meta.editor.views.PhysicalModelView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

public class CreateBusinessModelFromPhysicalModel implements
		IViewActionDelegate {

	private IViewPart businessModelView;
	
	@Override
	public void run(IAction action) {
		//get View reference
		businessModelView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("it.eng.spagobi.meta.editor.BusinessModel");
		//invoke tree creation on BusinessModelView
		((BusinessModelView)businessModelView).createTree();

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

}
