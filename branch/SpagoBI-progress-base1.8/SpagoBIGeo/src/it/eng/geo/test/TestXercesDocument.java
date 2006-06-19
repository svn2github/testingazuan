package it.eng.geo.test;

import it.eng.geo.document.XMLDocumentException;
import it.eng.geo.document.XercesXMLDocument;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.batik.dom.util.DOMUtilities;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestXercesDocument {


	public static void main(String[] args) throws IOException {

		String uRIString = "file:///C:/Progetti/Regione_Veneto/Georeferenziazione/g/VE_comuni/ve_comuni_rv1.svg";

		InputStream inputStream = null;

		URL uRL = new URL(uRIString);
		URLConnection urlConn;
		urlConn = uRL.openConnection();
		urlConn.setDoInput(true);
		urlConn.setUseCaches(false);

		inputStream = new DataInputStream(urlConn.getInputStream());

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
		XercesXMLDocument xercesXMLDocument = null;
		/*
		try {
			xercesXMLDocument = new XercesXMLDocument(baos.toByteArray());
		} catch (XMLDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// try {
		// xercesXMLDocument.addValue("svg","NEW_TAG","NEW_VALUE");
		// } catch (XMLDocumentException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
				byteArrayOutputStream);
		try {
			DOMUtilities.writeDocument(xercesXMLDocument.getDocument(),
					outputStreamWriter);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("====>"
				+ new String(byteArrayOutputStream.toByteArray()) + "<====");
		try {
			System.out.println("====>"
					+ xercesXMLDocument.getDocumentAsXMLString() + "<====");
		} catch (XMLDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}