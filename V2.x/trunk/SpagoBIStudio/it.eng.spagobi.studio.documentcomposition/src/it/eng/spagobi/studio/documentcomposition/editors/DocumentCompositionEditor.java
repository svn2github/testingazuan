package it.eng.spagobi.studio.documentcomposition.editors;

import it.eng.spagobi.studio.core.log.SpagoBILogger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;


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
		SpagoBILogger.infoLog("Creating the editor for charts");
		try{
			final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
			final ScrolledForm form = toolkit.createScrolledForm(parent);
			//			TableWrapLayout layout = new TableWrapLayout();
			//			layout.numColumns = 2;
			//			layout.horizontalSpacing = 20;
			//			layout.verticalSpacing = 10;
			//			layout.topMargin = 20;
			//			layout.leftMargin = 20;
			//			form.getBody().setLayout(layout);

			RowLayout layoutAll = new RowLayout(SWT.HORIZONTAL);
			layoutAll.marginTop=20;
			layoutAll.marginLeft = 20;
			layoutAll.spacing=10;

			//			layoutAll.wrap = false;
			//			layoutAll.fill = false;
			//			layoutAll.justify = false;
			//			layoutAll.pack=false;
			form.getBody().setLayout(layoutAll);

			
			Color lightGray=new Color(parent.getDisplay(), new RGB(245,245,245));

			Composite compLeft=new Composite(form.getBody(), SWT.BORDER);
			//TableWrapData td = new TableWrapData(TableWrapData.FILL);
			//compLeft.setLayoutData(td);
			//Composite compLeft=new Composite(form,SWT.BORDER);
			compLeft.setLayoutData(new RowData(300, 700));

			RowLayout layoutLeft = new RowLayout();
			compLeft.setLayout(layoutLeft);





			// ********* LEFT HIGH **************+++


			Section compLeftHigh = toolkit.createSection(compLeft, 
					Section.DESCRIPTION|Section.TITLE_BAR);
			compLeftHigh.setLayoutData(new RowData(300,100));
			compLeftHigh.setBackground(lightGray);
			compLeftHigh.setText("Documents information");
			Composite clientLeftHigh = toolkit.createComposite(compLeftHigh);
			clientLeftHigh.setBackground(lightGray);
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			clientLeftHigh.setLayout(gl);

			Label l=new Label(clientLeftHigh, SWT.NULL);
			l.setText("Title of the document composed");
			l.setForeground(new Color(compLeftHigh.getDisplay(), new RGB(0,0,0)));
			l.setBackground(lightGray);
			compLeftHigh.setClient(clientLeftHigh);










			Section compLeftLow = toolkit.createSection(compLeft, 
					Section.DESCRIPTION|Section.TITLE_BAR);
			compLeftLow.setLayoutData(new RowData(300,600));
			compLeftLow.setBackground(new Color(compLeft.getDisplay(), new RGB(15,255,150)));
			compLeftLow.setText("Sinistra basso");


			Composite compRight=new Composite(form.getBody(), SWT.BORDER);
			compRight.setLayoutData(new RowData(600, 700));
			RowLayout layoutRight = new RowLayout();
			compRight.setLayout(layoutRight);
			Section compRightHigh = toolkit.createSection(compRight, 
					Section.DESCRIPTION|Section.TITLE_BAR);
			compRightHigh.setText("destra alto");
			compRightHigh.setLayoutData(new RowData(700,500));
			compRightHigh.setBackground(new Color(compRight.getDisplay(), new RGB(200,155,200)));
			Section compRightLow = toolkit.createSection(compRight, 
					Section.DESCRIPTION|Section.TITLE_BAR);
			compRightLow.setLayoutData(new RowData(700,200));
			compRightLow.setBackground(new Color(compRight.getDisplay(), new RGB(150,255,15)));
			compRightLow.setText("destra basso");


		}
		catch (Exception e) {
			e.printStackTrace();
		}



	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}


}
