package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.thoughtworks.xstream.XStream;

public class ModelBO {
	
	public DocumentComposition createModel(IFile file){
		DocumentComposition documentComposition = new DocumentComposition();
		
		//logica di riempimento
		XStream xstream = new XStream();
		try {
			Object objFromXml = xstream.fromXML(file.getContents());
			
			
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return documentComposition;
	}
	
	public void saveModel(DocumentComposition documentComposition){
		Activator.getDefault().setDocumentComposition(documentComposition);
	}
	
	public DocumentComposition getModel(){
		return Activator.getDefault().getDocumentComposition();
	}
}
