package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;

public class GEOEditor extends EditorPart{

	protected boolean isDirty = false;
	int height=0;
	int width=0;
	private Designer designer;
	


	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(PROP_DIRTY);
	}

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
		// TODO Auto-generated method stub
		
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

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 20;
		layout.verticalSpacing = 10;
		layout.topMargin = 20;
		layout.leftMargin = 20;
		//		FillLayout layout = new FillLayout();

		form.getBody().setLayout(layout);

		final Section section = toolkit.createSection(form.getBody(), 
				Section.TITLE_BAR | SWT.NO_REDRAW_RESIZE);
		//section.setLayoutData()
		section.setSize(1000, 1000);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				//parent.setSize(width, height);
				form.reflow(true);
			}
		});
		section.setText("GEO designer: Screen");
		Composite sectionClient = toolkit.createComposite(section, SWT.NO_REDRAW_RESIZE);
		//sectionClient.setSize(1000, 1000);
		Composite composite=new Group(sectionClient,SWT.BORDER_DOT);
		//		composite.setText("Video");
		composite.setBackground(new Color(composite.getDisplay(), new RGB(240,240,240)));
		composite.setLocation(0, 0);
		composite.setSize(800, 600);

		//////////////inserire form!!!
		section.setClient(sectionClient);

		// Add the control resize, shell resize all the document Containers
		width=sectionClient.getBounds().width;
		height=sectionClient.getBounds().height;

		GEODocument geoDocument = Activator.getDefault().getGeoDocument();
		
		designer=new Designer(composite, this);
		designer.setEditor(this);
		initializeEditor(geoDocument);

		SpagoBILogger.infoLog("END "+GEOEditor.class.toString()+": create Part Control function");

	}
	public void initializeEditor(GEODocument geoDocument){
		SpagoBILogger.infoLog("START: "+GEOEditor.class.toString()+" initialize Editor");	
		// clean the properties View
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		// Document properties
		IWorkbenchPage aa=a.getActivePage();
		
		// Initialize Designer
		designer.initializeDesigner(geoDocument);
		
		SpagoBILogger.infoLog("END: "+GEOEditor.class.toString()+" initialize Editor");	
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	public Designer getDesigner() {
		return designer;
	}

	public void setDesigner(Designer designer) {
		this.designer = designer;
	}
}
