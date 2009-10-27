package it.eng.spagobi.studio.documentcomposition.editors;


import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocumentComposition;
import it.eng.spagobi.studio.documentcomposition.util.DocCompUtilities;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class DocumentContained {

	Group group;	
	MetadataDocument metadataDocument;	

	public static final String IMG_JASPER_REPORT="it/eng/spagobi/studio/documentcomposition/resources/images/jasper.png";
	public static final String IMG_BIRT_REPORT="it/eng/spagobi/studio/documentcomposition/resources/images/birt.png";
	public static final String IMG_DASHBOARD="it/eng/spagobi/studio/documentcomposition/resources/images/chart.png";
	public static final String IMG_CHART="it/eng/spagobi/studio/documentcomposition/resources/images/chart.png";
	public static final String IMG_DOCUMENT_COMPOSITION="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_OLAP="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_ETL="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_OFFICE_DOC="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_MAP="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DATAMART="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DOSSIER="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DATA_MINING="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";

	public static final String TYPE_REPORT="REPORT";
	public static final String TYPE_DOSSIER="DOSSIER";
	public static final String TYPE_OLAP="OLAP";
	public static final String TYPE_DATA_MINING="DATA_MINING";
	public static final String TYPE_DASH="DASH";
	public static final String TYPE_DATAMART="DATAMART";
	public static final String TYPE_MAP="MAP";
	public static final String TYPE_OFFICE_DOC="OFFICE_DOC";
	public static final String TYPE_ETL="ETL";
	public static final String TYPE_DOCUMENT_COMPOSITIOn="DOCUMENT_COMPOSITION";


	public DocumentContained(Composite parent, int style) throws Exception{
		group=new Group(parent, style);
	}


	/**
	 *  Get the metadata of the document inside the container
	 * @param composite
	 */
	public boolean recoverDocumentMetadata(IFile file){
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

				// Must get the right MetadataDocument
				MetadataDocumentComposition metadataDocumentComposition=it.eng.spagobi.studio.documentcomposition.Activator.getDefault().getMetadataDocumentComposition();
				Vector<MetadataDocument> metaDataDocumentVector=metadataDocumentComposition.getMetadataDocuments();

				metadataDocument=new MetadataDocument(file);				

				metadataDocument.setLocalFileName(localFileName);
				if(metaDataDocumentVector==null){
					metaDataDocumentVector=new Vector<MetadataDocument>();
					metadataDocumentComposition.setMetadataDocuments(metaDataDocumentVector);
				}
				metaDataDocumentVector.add(metadataDocument);
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
			String imagePath=null;
			if(metadataDocument.getType().equalsIgnoreCase(TYPE_REPORT)){
				imagePath=IMG_JASPER_REPORT;
			}
			else if(metadataDocument.getType().equalsIgnoreCase(TYPE_DASH)){
				imagePath=IMG_DASHBOARD;
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


			if(imagePath!=null){
				final String imagePathFinal=imagePath;
				group.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						Image image = null;
						try {
							InputStream is=DocCompUtilities.getInputStreamFromResource(imagePathFinal);
							image = new Image(group.getDisplay(), is);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						catch (Exception e2) {

							e2.printStackTrace();
						}
						e.gc.drawImage(image, 20, 30);
						image.dispose();
					}
				});
				group.redraw();
			}
		}

		group.setText(metadataDocument.getLocalFileName());
		Label nameLabelName=new Label(group,SWT.NULL);
		nameLabelName.setText("Name: ");			
		Label nameLabelValue=new Label(group,SWT.NULL);
		nameLabelValue.setText(metadataDocument.getName() != null ? metadataDocument.getName() : "" );
		Label localFileNameLabel=new Label(group,SWT.NULL);
		localFileNameLabel.setText(metadataDocument.getLocalFileName());				

		return true;
	}



	public MetadataDocument getMetadataDocument() {
		return metadataDocument;
	}


	public void setMetadataDocument(MetadataDocument metadataDocument) {
		this.metadataDocument = metadataDocument;
	}


	public Group getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}



}
