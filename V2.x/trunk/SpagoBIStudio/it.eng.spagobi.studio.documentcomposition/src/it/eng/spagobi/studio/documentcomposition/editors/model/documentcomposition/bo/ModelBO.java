package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Refresh;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Style;
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
}
