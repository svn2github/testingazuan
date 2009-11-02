package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.EditorReference;

import it.eng.spagobi.studio.documentcomposition.editors.Designer;
import it.eng.spagobi.studio.documentcomposition.editors.DocContainer;
import it.eng.spagobi.studio.documentcomposition.editors.DocumentCompositionEditor;
import it.eng.spagobi.studio.documentcomposition.editors.DocumentContained;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo.ModelBO;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataBO;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocumentComposition;


public class DocumentComposition {

	private DocumentsConfiguration documentsConfiguration;
	private String templateValue;

	public DocumentsConfiguration getDocumentsConfiguration() {
		return documentsConfiguration;
	}
	public void setDocumentsConfiguration(
			DocumentsConfiguration documentsConfiguration) {
		this.documentsConfiguration = documentsConfiguration;
	}
	public String getTemplateValue() {
		return templateValue;
	}
	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}
	public DocumentComposition() {
		documentsConfiguration=new DocumentsConfiguration();
	}



	/** Calld before saving recalculate styles for each document (filled) contestualized to the actual video size (except for those set to manual!
	 * 	
	 */
	public void reloadAllStylesContained(){
		// take desginer and run all the containers
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		IWorkbenchPage aa=a.getActivePage();
		IEditorReference[] editors=aa.findEditors(null, "it.eng.spagobi.studio.documentcomposition.editors.DocumentCompositionEditor", IWorkbenchPage.MATCH_ID);
		if(editors!=null && editors.length>0){
			EditorReference editorReference=(EditorReference)editors[0];
			DocumentCompositionEditor editor=(DocumentCompositionEditor)editorReference.getPart(false);
			Designer designer=editor.getDesigner();
			for (Iterator iterator = designer.getContainers().keySet().iterator(); iterator.hasNext();) {
				Integer id = (Integer) iterator.next();
				DocContainer docContainer=designer.getContainers().get(id);
				if(docContainer.getDocumentContained()!=null && docContainer.getDocumentContained().getMetadataDocument()!=null) {
					Style style=docContainer.calculateTemplateStyle();
					MetadataDocument metadataDocument=docContainer.getDocumentContained().getMetadataDocument();
					new ModelBO().updateModelModifyDocument(metadataDocument, style);
				}
			}

		}
	}


}
