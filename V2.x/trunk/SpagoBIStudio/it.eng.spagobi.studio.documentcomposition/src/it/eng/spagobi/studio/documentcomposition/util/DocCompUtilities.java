package it.eng.spagobi.studio.documentcomposition.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

	
}
