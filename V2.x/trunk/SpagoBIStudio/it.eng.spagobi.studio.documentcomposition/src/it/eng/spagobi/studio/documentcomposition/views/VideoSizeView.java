package it.eng.spagobi.studio.documentcomposition.views;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo.ModelBO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class VideoSizeView extends ViewPart{

	Composite client;

	@Override
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		// Lets make a layout for the first section of the screen
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		// Creating the Screen
		Section section = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		section.setText("Video Size Properties"); //$NON-NLS-1$
		client = toolkit.createComposite(section, SWT.WRAP);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);

		int width=1000;
		int height=500;
		final DocumentComposition documentComposition=(new ModelBO()).getModel();
		if(documentComposition!=null && documentComposition.getDocumentsConfiguration()!=null){
			DocumentsConfiguration documentsConfiguration=documentComposition.getDocumentsConfiguration();
			String heightS=documentsConfiguration.getVideoHeight();
			String widthS=documentsConfiguration.getVideoWidth();
			if(widthS!=null){
				width=Integer.valueOf(widthS);
			}
			if(heightS!=null){
				height=Integer.valueOf(heightS);
			}
		}

		Label text2=new Label(client, SWT.NULL);
		text2.setText("Video Height: ");

		final Spinner heightSpin = new Spinner (client, SWT.BORDER);
		heightSpin.setMaximum(100000);
		heightSpin.setMinimum(0);
		heightSpin.setSelection(Integer.valueOf(height));
		//styleSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		heightSpin.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				int newSize = heightSpin.getSelection();
				Integer newSizeInt=null;
				try{
					newSizeInt=Integer.valueOf(newSize);
				}
				catch (Exception e) {
					newSizeInt=new Integer(10);
				}
				if(documentComposition!=null && documentComposition.getDocumentsConfiguration()!=null){
					documentComposition.getDocumentsConfiguration().setVideoHeight(newSizeInt.toString());
				}
			}
		});

		Label text1=new org.eclipse.swt.widgets.Label(client, SWT.NULL);
		text1.setText("Video : ");

		final Spinner widthSpin = new Spinner (client, SWT.BORDER);
		widthSpin.setMaximum(100000);
		widthSpin.setMinimum(0);
		widthSpin.setSelection(Integer.valueOf(width));
		//styleSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		widthSpin.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				int newSize = widthSpin.getSelection();
				Integer newSizeInt=null;
				try{
					newSizeInt=Integer.valueOf(newSize);
				}
				catch (Exception e) {
					newSizeInt=new Integer(10);
				}
				if(documentComposition!=null && documentComposition.getDocumentsConfiguration()!=null){
					documentComposition.getDocumentsConfiguration().setVideoWidth(newSizeInt.toString());
				}
			}
		});

		client.pack();
		section.setClient(client);



	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site);
	}



}
