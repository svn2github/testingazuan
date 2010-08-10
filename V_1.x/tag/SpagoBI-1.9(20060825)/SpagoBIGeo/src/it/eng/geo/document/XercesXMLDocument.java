/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.document;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.xml.sax.SAXException;

public class XercesXMLDocument extends AbstractXMLDocument{

	/**
	 * @throws XMLDocumentException
	 * 
	 */

	public void createDocument(byte[] document) throws XMLDocumentException {
		try {

			// Create a XML Document
			DocumentBuilderFactory dbFactory = DocumentBuilderFactoryImpl
					.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
			setDocument(docBuilder.parse(new ByteArrayInputStream(document)));
		} catch (ParserConfigurationException e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		} catch (SAXException e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		} catch (IOException e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		} catch (Exception e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		}
	}
}