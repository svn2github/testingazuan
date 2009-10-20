package it.eng.spagobi.studio.documentcomposition.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import test.Designer;


public class DocumentCompositionEditor extends EditorPart {

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
	throws PartInitException {
		this.setPartName(input.getName());
		FileEditorInput fei = (FileEditorInput) input;
		setInput(input);
		setSite(site);

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);
		//		TableWrapLayout layout = new TableWrapLayout();
		//		layout.numColumns = 1;
		//		layout.horizontalSpacing = 20;
		//		layout.verticalSpacing = 10;
		//		layout.topMargin = 20;
		//		layout.leftMargin = 20;
		FillLayout fill = new FillLayout();

		form.getBody().setLayout(fill);

		// Dashboard general information section
		Section section = toolkit.createSection(form.getBody(), 
				Section.TITLE_BAR);
//		TableWrapData td = new TableWrapData(TableWrapData.FILL);
//		section.setLayoutData(td);

		//section.setLayoutData()
		section.setSize(1000, 1000);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Dashboard information");
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setSize(1000, 1000);

		Designer de=new Designer(sectionClient);

		section.setClient(sectionClient);



	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}


}
