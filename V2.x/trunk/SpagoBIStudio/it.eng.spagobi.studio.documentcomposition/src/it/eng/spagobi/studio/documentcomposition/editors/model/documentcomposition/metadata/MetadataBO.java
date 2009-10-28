package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata;

import it.eng.spagobi.studio.documentcomposition.Activator;

import org.eclipse.core.runtime.CoreException;

public class MetadataBO {

	public MetadataDocumentComposition createMetadataDocumentComposition() throws CoreException{
		return new MetadataDocumentComposition();
	}

	public void saveModel(MetadataDocumentComposition metadataDocumentComposition){
		Activator.getDefault().setMetadataDocumentComposition(metadataDocumentComposition);
	}

	public MetadataDocumentComposition getMetadataDocumentComposition(){
		return Activator.getDefault().getMetadataDocumentComposition();
	}

	
	
	
}
