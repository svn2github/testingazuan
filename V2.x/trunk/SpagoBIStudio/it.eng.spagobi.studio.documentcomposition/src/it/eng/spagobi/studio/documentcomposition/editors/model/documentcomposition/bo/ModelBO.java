package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import java.util.Iterator;
import java.util.Vector;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Refresh;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Style;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.util.XmlTemplateGenerator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;

import com.thoughtworks.xstream.XStream;

public class ModelBO {

	public DocumentComposition createModel(IFile file) throws CoreException{
		DocumentComposition documentComposition = XmlTemplateGenerator.readXml(file);
		return documentComposition;
	}

		public void saveModel(DocumentComposition documentComposition){
			Activator.getDefault().setDocumentComposition(documentComposition);
		}

	public DocumentComposition getModel(){
		return Activator.getDefault().getDocumentComposition();
	}

	
	/** update the model with a new document!
	 * 
	 */
	public void updateModelWithNewDocument(MetadataDocument _metadataDocument, Style style){
		ModelBO bo=new ModelBO();
		DocumentComposition documentComposition=bo.getModel();
		DocumentsConfiguration documentsConfiguration=documentComposition.getDocumentsConfiguration();
		if(documentsConfiguration==null)documentsConfiguration=new DocumentsConfiguration();
		Vector<Document> documents=documentsConfiguration.getDocuments();
		if(documents==null)documents=new Vector<Document>();
		Document newDocument=new Document();
		newDocument.setSbiObjLabel(_metadataDocument.getLabel());
		newDocument.setLocalFileName(_metadataDocument.getLocalFileName());
		newDocument.setStyle(style);
		documents.add(newDocument);
		bo.saveModel(documentComposition);
	}

	/** update the model with a new document!
	 * 
	 */
	public void updateModelModifyDocument(MetadataDocument _metadataDocument, Style style){
		ModelBO bo=new ModelBO();
		DocumentComposition documentComposition=bo.getModel();
		DocumentsConfiguration documentsConfiguration=documentComposition.getDocumentsConfiguration();
		Vector<Document> documents=documentsConfiguration.getDocuments();
		if(documents!=null){
			for (Iterator iterator = documents.iterator(); iterator.hasNext();) {
				Document document = (Document) iterator.next();
				// Modify the current document!
				if(document.getSbiObjLabel().equals(_metadataDocument.getLabel())){
					document.setStyle(style);
				}

			}
			bo.saveModel(documentComposition);
		}
	}





}
