package it.eng.spagobi.studio.documentcomposition.editors;


import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataBO;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.util.DocCompUtilities;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class DocumentContained {

	Composite group;	
	MetadataDocument metadataDocument;	
	Image image=null;
	String imagePath=null;

	public static final String IMG_JASPER_REPORT="it/eng/spagobi/studio/documentcomposition/resources/images/IconJasperReport.PNG";
	public static final String IMG_BIRT_REPORT="it/eng/spagobi/studio/documentcomposition/resources/images/birt.png";
	public static final String IMG_DASHBOARD="it/eng/spagobi/studio/documentcomposition/resources/images/chart.png";
	public static final String IMG_CHART="it/eng/spagobi/studio/documentcomposition/resources/images/IconChartEditor.PNG";
	public static final String IMG_DOCUMENT_COMPOSITION="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_OLAP="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_ETL="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_OFFICE_DOC="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_MAP="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DATAMART="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DOSSIER="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DATA_MINING="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String CONTAINER_MOVE="it/eng/spagobi/studio/documentcomposition/resources/images/drag.png";
	public static final String CONTAINER_20X20_MOVE="it/eng/spagobi/studio/documentcomposition/resources/images/drag20x20.png";
	public static final String CONTAINER_WHITE="it/eng/spagobi/studio/documentcomposition/resources/images/white.png";

	public static final String TYPE_REPORT=SpagoBIConstants.REPORT_TYPE_CODE;
	public static final String TYPE_DOSSIER="DOSSIER";
	public static final String TYPE_OLAP="OLAP";
	public static final String TYPE_DATA_MINING="DATA_MINING";
	public static final String TYPE_DASH=SpagoBIConstants.DASH_TYPE_CODE;
	public static final String TYPE_DATAMART="DATAMART";
	public static final String TYPE_MAP="MAP";
	public static final String TYPE_OFFICE_DOC="OFFICE_DOC";
	public static final String TYPE_ETL="ETL";
	public static final String TYPE_DOCUMENT_COMPOSITIOn=SpagoBIConstants.DOCUMENT_COMPOSITE_TYPE;

	Button buttonDrag=null;

	public DocumentContained(Composite parent, int style) throws Exception{
		group=new Composite(parent, style);
		group.setSize(DocContainer.DEFAULT_WIDTH, DocContainer.DEFAULT_HEIGHT);
		//		final String imagePathFinal1=CONTAINER_20X20_MOVE;
		//		InputStream is1=DocCompUtilities.getInputStreamFromResource(imagePathFinal1);
		//		final Image image1 = new Image(group.getDisplay(), is1);
		//		group.setBackgroundImage(image1);
		//		final String imagePathFinal=CONTAINER_20X20_MOVE;
		//		InputStream is=DocCompUtilities.getInputStreamFromResource(imagePathFinal);
		//		final Image imageSize = new Image(group.getDisplay(), is);
		//		group.addPaintListener(new PaintListener() {
		//			public void paintControl(PaintEvent e) {
		//				e.gc.drawImage(imageSize,20,20);
		//				imageSize.dispose();
		//				//				e.gc.drawImage(image, 20, 30);
		//				//				image.dispose();
		//			}
		//		});
		final String imagePathFinal=CONTAINER_20X20_MOVE;
		InputStream is=null;
		try {
			is = DocCompUtilities.getInputStreamFromResource(imagePathFinal);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final Image imageSize = new Image(group.getDisplay(), is);
		buttonDrag=new Button(group, SWT.NULL);
		buttonDrag.setLocation(0, 0);
		buttonDrag.setSize(10,10);
		buttonDrag.setBackgroundImage(imageSize);

		group.redraw();
	}




	/**
	 *  Get the metadata of the document inside the container and add the document
	 * @param composite
	 */
	public boolean recoverDocumentMetadata(Integer idContainer,IFile file){
		try{
			int i=0;
			String id=file.getPersistentProperty(PropertyPage.DOCUMENT_ID);
			if(metadataDocument!=null){
				MessageDialog.openWarning(group.getShell(), 
						"Warning", "Container has already Document in!");
				return false;
			}
			else if(id==null){
				MessageDialog.openWarning(group.getShell(), 
						"Warning", "Chosen file has no SpagoBI document metadata");
				return false;
			}
			else{			
				i++;

				IPath ia=file.getFullPath();
				String localFileName=ia.toString();

				metadataDocument=new MetadataDocument(file);				
				metadataDocument.setIdMetadataDocument(idContainer+"_"+metadataDocument.getLabel());
				metadataDocument.setLocalFileName(localFileName);
				(new MetadataBO()).getMetadataDocumentComposition().addMetadataDocument(metadataDocument);
				return viewDocumentMetadata(metadataDocument);
			}
		}
		catch (Exception e) {	
			e.printStackTrace();
			SpagoBILogger.errorLog("Exception while retrieving metadata",e);
			return false;
		}

	}


	/**
	 *  VIsualize the metadata of the document inside the container
	 * @param composite
	 */
	public boolean viewDocumentMetadata(MetadataDocument metadataDocument){

		// get the image Path

		if(metadataDocument.getType()!=null){
			imagePath=null;
			if(metadataDocument.getType().equalsIgnoreCase(TYPE_REPORT)){
				imagePath=IMG_JASPER_REPORT;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_DASH)){
				imagePath=IMG_CHART;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_DATA_MINING)){
				imagePath=IMG_DATA_MINING;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_DATAMART)){
				imagePath=IMG_DATAMART;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_DOSSIER)){
				imagePath=IMG_DOSSIER;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_DOCUMENT_COMPOSITIOn)){
				imagePath=IMG_DOCUMENT_COMPOSITION;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_MAP)){
				imagePath=IMG_MAP;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_ETL)){
				imagePath=IMG_ETL;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_OFFICE_DOC)){
				imagePath=IMG_OFFICE_DOC;
			}

			drawImage();

			String titleGroup="Name: "+metadataDocument.getName() != null ? metadataDocument.getName() : metadataDocument.getLabel();
			//group.setText(titleGroup);
			group.setData(titleGroup);
			group.setToolTipText(metadataDocument.getLocalFileName());
			Label nameLabelName=new Label(group,SWT.NULL);
			nameLabelName.setText(titleGroup);			
			//		Label nameLabelValue=new Label(group,SWT.NULL);
			//nameLabelValue.setText(metadataDocument.getName() != null ? metadataDocument.getName() : "" );
			//		Label localFileNameLabel=new Label(group,SWT.NULL);
			//		localFileNameLabel.setText(metadataDocument.getLocalFileName());				
			//		(new Label(group, SWT.NULL)).setText("");
			nameLabelName.redraw();
			group.redraw();
		}
		return true;

	}

	public void drawImage(){
		try{
			if(imagePath!=null){
				final String imagePathFinal=imagePath;
				InputStream is=DocCompUtilities.getInputStreamFromResource(imagePathFinal);
				image = new Image(group.getDisplay(), is);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		//int newWidth=


		//		final int originalWidth = image.getBounds().width;
		//		final int originalHeight = image.getBounds().height; 				
		//		int containerHeight=group.getBounds().height;
		//		int containerWidth=group.getBounds().width;

		int newWidth=DesignerUtilities.getScaledImageWidth(image.getBounds().width, group.getBounds().width);
		int newHeight=DesignerUtilities.getScaledImageHeight(image.getBounds().height, group.getBounds().height);

		final Image scaled200 = new Image(group.getDisplay(),
				image.getImageData().scaledTo(newWidth,newHeight));


		//		double rapportoHeight=(double)containerHeight / (double)originalHeight;
		//		double rapportoWidth=(double)containerWidth / (double)originalWidth;
		//		final Image scaled200 = new Image(group.getDisplay(),
		//				image.getImageData().scaledTo((int)(originalWidth*rapportoWidth-20),(int)(originalHeight*rapportoHeight-20)));


		//		group.addPaintListener(new PaintListener() {
		//			public void paintControl(PaintEvent e) {
		//				e.gc.drawImage(scaled200,20,20);
		//				image.dispose();
		//				//				e.gc.drawImage(image, 20, 30);
		//				//				image.dispose();
		//			}
		//		});
		group.setBackgroundImage(scaled200);

		group.redraw();
	}




	public MetadataDocument getMetadataDocument() {
		return metadataDocument;
	}


	public void setMetadataDocument(MetadataDocument metadataDocument) {
		this.metadataDocument = metadataDocument;
	}


	public Composite getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}


	public Image getImage() {
		return image;
	}


	public void setImage(Image image) {
		this.image = image;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}




	public Button getButtonDrag() {
		return buttonDrag;
	}




	public void setButtonDrag(Button buttonDrag) {
		this.buttonDrag = buttonDrag;
	}



}
