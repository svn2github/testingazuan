package it.eng.spagobi.studio.documentcomposition.util;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import org.osgi.framework.Bundle;

public class DocCompUtilities {


	/** Get input stream from a resource
	 * 
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 */

	public static InputStream getInputStreamFromResource(String resourcePath) throws IOException {
		Bundle b = org.eclipse.core.runtime.Platform.getBundle(it.eng.spagobi.studio.documentcomposition.Activator.PLUGIN_ID);
		URL res = b.getResource(resourcePath);
		InputStream is = res.openStream();
		return is;
	}

	public boolean isDocumentDeletable(Document doc) {
		boolean ret = true;
		
		//ricava DocumentsComposition salvato
		DocumentComposition documentComposition =Activator.getDefault().getDocumentComposition();
		if(documentComposition != null){
			Vector<Document> docs = documentComposition.getDocumentsConfiguration().getDocuments();
			if(docs.contains(doc)){
				ret = false;
			}
		}
		
		return ret;
		
	}
}
