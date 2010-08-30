/*
 * This View shows properties of the Business Model Tree
 */
package eng.it.spagobimeta.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import eng.it.spagobimeta.model.BusinessClass;
import eng.it.spagobimeta.model.BusinessModel;

public class PropertiesView extends ViewPart implements ISelectionListener {


    private FormToolkit toolkit;
    private ScrolledForm form;
    private Composite parentRef;

    public PropertiesView() {
    	super();
    }
	
    @Override
    public void setFocus() {

    }
	
	@Override
	public void createPartControl(Composite parent) {	
		parentRef = parent;
		toolkit = new FormToolkit(parent.getDisplay());
		
		//Add this view to SelectionListener (for the BM Tree in GraphicEditorView)
		getViewSite().getPage().addSelectionListener(this);
	}
	
	@Override
	//Do something when there is a selection event (from the BM Tree)
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object first = ((IStructuredSelection)selection).getFirstElement();
			//BC node selected
			if (first instanceof BusinessClass) {
				createBCProperties((BusinessClass)first);
			}
			//BM node selected
			if (first instanceof BusinessModel) {
				createBMProperties((BusinessModel)first);
			}
		}		
	}

	//Create Properties UI for a BusinessModel object
	private void createBMProperties(BusinessModel bm){
		if (form!=null)
			form.dispose();
		
		parentRef.layout(true);
		form = toolkit.createScrolledForm(parentRef);
		form.setText("Properties of Business Model");
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
		layout.numColumns = 2;
		
		//Section
		Section section = toolkit.createSection(form.getBody(), 
				Section.DESCRIPTION|Section.TITLE_BAR|
				Section.TWISTIE|Section.EXPANDED);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		section.setLayoutData(td);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("General");
		section.setDescription("General Business Model properties");
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		sectionClient.setLayout(gl);
		Label l= toolkit.createLabel(sectionClient,"Business Model Name");
		Text t = toolkit.createText(sectionClient, bm.getName());
		t.setLayoutData(gd);
		section.setClient(sectionClient);	

		parentRef.layout(true);
	}
	
	//Create Properties UI for a BusinessClass object
	private void createBCProperties(BusinessClass bc){
		if (form!=null)
			form.dispose();
		
		parentRef.layout(true);
		form = toolkit.createScrolledForm(parentRef);
		form.setText("Properties of Business Class");
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
		layout.numColumns = 2;
		
		//Section
		Section section = toolkit.createSection(form.getBody(), 
				Section.DESCRIPTION|Section.TITLE_BAR|
				Section.TWISTIE|Section.EXPANDED);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		section.setLayoutData(td);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("General");
		section.setDescription("General Business Class properties");
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		sectionClient.setLayout(gl);
		Label l= toolkit.createLabel(sectionClient,"Business Class Name:");
		Text t = toolkit.createText(sectionClient, bc.getName());
		t.setLayoutData(gd);
		Label l2= toolkit.createLabel(sectionClient,"Class Type:");
		Text t2 = toolkit.createText(sectionClient,"");
		t2.setLayoutData(gd);
		section.setClient(sectionClient);
		
		//Section 2
		Section section2 = toolkit.createSection(form.getBody(), 
				Section.DESCRIPTION|Section.TITLE_BAR|
				Section.TWISTIE|Section.EXPANDED);
		TableWrapData td2 = new TableWrapData(TableWrapData.FILL_GRAB);
		td2.colspan = 2;
		section2.setLayoutData(td2);
		section2.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section2.setText("Custom");
		section2.setDescription("Custom Business Class properties");
		Composite sectionClient2 = toolkit.createComposite(section2);
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		sectionClient2.setLayout(gl2);
		Label l3= toolkit.createLabel(sectionClient2,"Custom Property:");
		Text t3 = toolkit.createText(sectionClient2, " ");
		t3.setLayoutData(gd2);
		section2.setClient(sectionClient2);

		parentRef.layout(true);
	}
	
	public void dispose() {
		 toolkit.dispose();
		 super.dispose();
	}

}
