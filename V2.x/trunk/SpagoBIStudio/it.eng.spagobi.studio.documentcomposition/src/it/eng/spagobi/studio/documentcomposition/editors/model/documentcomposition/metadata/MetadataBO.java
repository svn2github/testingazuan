package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata;

import it.eng.spagobi.studio.documentcomposition.Activator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class MetadataBO {

	public void createMetadataDocumentComposition(IFile file) throws CoreException{
		MetadataDocumentComposition metadataDocumentComposition=new MetadataDocumentComposition(file);
		Activator.getDefault().setMetadataDocumentComposition(metadataDocumentComposition);

	}

	public void saveModel(MetadataDocumentComposition metadataDocumentComposition){
		Activator.getDefault().setMetadataDocumentComposition(metadataDocumentComposition);
	}

	public MetadataDocumentComposition getMetadataDocumentComposition(){
		return Activator.getDefault().getMetadataDocumentComposition();
	}

	
	
	
}
