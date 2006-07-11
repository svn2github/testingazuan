/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.test;

import it.eng.geo.document.XMLDocumentException;
import it.eng.geo.document.XercesXMLDocument;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestMapRenderer {

	public static void main(String[] args) throws IOException, XMLDocumentException {
		InputStream inputStream = null;
		String uRI = "file:///C:/Progetti/Regione_Veneto/Georeferenziazione/g/VE_comuni/ve_comuni.svg";
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
		String template = "<legend>"
				+ "<defs>"
				+ "	<rect id=\"legendBox\" x=\"1690000\" y=\"-5080000\" width=\"10000\" height=\"5000\" stroke=\"black\" stroke-width=\"100\" />"
				+ "</defs>"
				+ "<g>"
				+ "	<use xlink:href=\"#legendBox\" transform=\"translate(0,0)\"      fill=\"red\"    fill-opacity=\"1\"/>"
				+ "	<use xlink:href=\"#legendBox\" transform=\"translate(0,5000)\"   fill=\"blue\"   fill-opacity=\"1\"/>"
				+ "	<use xlink:href=\"#legendBox\" transform=\"translate(0,10000)\"  fill=\"yellow\" fill-opacity=\"1\"/>"
				+ "</g>" 
				+ "</legend>";
		XercesXMLDocument legend = new XercesXMLDocument();
		try {
			legend.createDocument(template.getBytes());
		} catch (XMLDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		XercesXMLDocument xMLDocument = new XercesXMLDocument();
		xMLDocument.createDocument(baos.toByteArray());

		Document legendDoc = legend.getDocument();
		Document svgDoc = xMLDocument.getDocument();
		NodeList nodeList = legendDoc.getFirstChild().getChildNodes();
		int i = 0;
		Node node = nodeList.item(0);
		String nodeName = node.getNodeName();
		svgDoc.adoptNode(node);
		svgDoc.getLastChild().appendChild(node);
		i = 1;
		Node node1 = nodeList.item(2);
		String nodeName1 = node1.getNodeName();
		svgDoc.adoptNode(node1);
		svgDoc.getLastChild().appendChild(node1);
		




	}
}