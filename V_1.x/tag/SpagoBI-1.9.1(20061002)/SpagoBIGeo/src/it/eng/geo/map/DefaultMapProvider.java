/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.map;

import it.eng.geo.document.XMLDocumentException;
import it.eng.geo.document.XMLDocumentFactory;
import it.eng.geo.document.XMLDocumentIFace;
import it.eng.spago.base.SourceBean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DefaultMapProvider extends AbstractMapProvider {

	private static final String URI = "URI";

	private static final String XML_PARSER_IMPL = "XML_PARSER_IMPL";

	private static final String DEFAULT_PARSER = "it.eng.geo.document.XercesXMLDocument";

	/**
	 * 
	 */
	public DefaultMapProvider() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XMLDocumentIFace getSVGMapDocument(
			SourceBean mapProviderConfiguration) {

		String uRI = (String) mapProviderConfiguration.getAttribute(URI);
		String xMLParserImpl = (String) mapProviderConfiguration
				.getAttribute(XML_PARSER_IMPL);
		if (xMLParserImpl == null || xMLParserImpl.equals("")) {
			xMLParserImpl = DEFAULT_PARSER;
		}
		InputStream inputStream = null;

		URL uRL = null;
		try {
			uRL = new URL(uRI);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URLConnection urlConn = null;
		try {
			urlConn = uRL.openConnection();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		urlConn.setDoInput(true);
		urlConn.setUseCaches(false);

		try {
			inputStream = new DataInputStream(urlConn.getInputStream());
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		try {
			for (int count = 0; (count = inputStream.read(buf)) > 0;) {
				baos.write(buf, 0, count);
				baos.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		XMLDocumentIFace xMLDocument = null;
		try {
			xMLDocument = XMLDocumentFactory.getXMLDocument(xMLParserImpl);
			xMLDocument.createDocument(baos.toByteArray());
		} catch (XMLDocumentException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xMLDocument;
	}

}