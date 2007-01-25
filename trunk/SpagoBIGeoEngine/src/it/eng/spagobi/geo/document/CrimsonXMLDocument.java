/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.document;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.crimson.jaxp.DocumentBuilderFactoryImpl;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CrimsonXMLDocument extends AbstractXMLDocument {

	/**
	 * @throws XMLDocumentException
	 * 
	 */

	public void createDocument(byte[] document) throws XMLDocumentException {
		try {
			DocumentBuilderFactoryImpl dbf = new DocumentBuilderFactoryImpl();
			DocumentBuilder db = dbf.newDocumentBuilder();
			setDocument(db.parse(new InputSource(new ByteArrayInputStream(
					document))));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		}
	}
	//
	// public String getValue(String key) throws XMLDocumentException {
	// String value = null;
	// String lst[] = getValues(key);
	// if (lst != null) {
	// if (lst.length > 1) {
	// // Log.logWarning("Specificare meglio la chiave di ricerca o
	// // usare il metodo getValues(String key); key=" + key);
	// return value;
	// }
	// value = lst[0];
	// }
	// return value;
	// }
	//
	// public Element getElemetByID(String id) throws XMLDocumentException {
	// Element element = null;
	// element = this.document.getElementById(id);
	// return element;
	// }
	//
	// public Element getDocumentElement() throws XMLDocumentException {
	// Element element = null;
	// element = this.document.getDocumentElement();
	// return element;
	// }
	//
	// public String[] getValues(String key) throws XMLDocumentException {
	// String lst[] = (String[]) null;
	// try {
	// NodeList nl = XPathAPI.selectNodeList(this.document, key);
	// if (nl.getLength() > 0) {
	// lst = new String[nl.getLength()];
	// for (int i = 0; i < nl.getLength(); i++) {
	// Node n = nl.item(i);
	// Node nf = n.getFirstChild();
	// if (nf != null) {
	// lst[i] = nf.getNodeValue();
	// } else {
	// lst[i] = null;
	// }
	// }
	// }
	// } catch (TransformerException e) {
	// throw new XMLDocumentException(e.getClass().getName() + " "
	// + e.getMessage());
	// }
	// return lst;
	// }
	//
	// public int setValue(String key, String value) throws XMLDocumentException
	// {
	// try {
	// NodeList nl = XPathAPI.selectNodeList(this.document, key);
	// if (nl.getLength() > 0) {
	// for (int i = 0; i < nl.getLength(); i++) {
	// Node n = nl.item(i);
	// Node tn = this.document.createTextNode(value);
	// Node nf = n.getFirstChild();
	// if (nf != null) {
	// n.removeChild(nf);
	// }
	// n.appendChild(tn);
	// }
	// return 0;
	// } else {
	// return -1;
	// }
	// } catch (TransformerException e) {
	// throw new XMLDocumentException(e.getClass().getName() + " "
	// + e.getMessage());
	// }
	// }
	//
	// public void removeKey(String key) throws XMLDocumentException {
	// try {
	// NodeList nl = XPathAPI.selectNodeList(this.document, key);
	// if (nl.getLength() > 0) {
	// Node p = nl.item(0).getParentNode();
	// for (int i = 0; i < nl.getLength(); i++) {
	// Node cn = nl.item(i);
	// p.removeChild(cn);
	// }
	// }
	// } catch (TransformerException e) {
	// throw new XMLDocumentException(e.getClass().getName() + " "
	// + e.getMessage());
	// }
	// }
	//
	// public void addValue(String path, String key, String value)
	// throws XMLDocumentException {
	// addValue(path, key, value, null, null);
	// }
	//
	// public void addValue(String path, String key, String value,
	// String[] attributeNames, String[] attributeValues)
	// throws XMLDocumentException {
	// try {
	// NodeList nl = XPathAPI.selectNodeList(this.document, path);
	// Node p = nl.item(0);
	// Node newNode = new ElementNode(key);
	// Node newTextNode = new TextNode(value);
	// if (attributeNames != null && attributeNames.length > 0) {
	// for (int i = 0; i < attributeNames.length; i++) {
	// newNode.getAttributes().setNamedItem(
	// new AttributeNode(null, attributeNames[i],
	// attributeValues[i], false, ""));
	// }
	// }
	// newNode.appendChild(newTextNode);
	// p.appendChild(new TextNode("\t"));
	// p.appendChild(newNode);
	// p.appendChild(new TextNode("\n"));
	// } catch (TransformerException e) {
	// throw new XMLDocumentException(e.getClass().getName() + " "
	// + e.getMessage());
	// }
	// }
	//
	// public void appendChild(Node newNode, String path)
	// throws XMLDocumentException {
	// try {
	// NodeList nl = XPathAPI.selectNodeList(this.document, path);
	// Node p = nl.item(0);
	// p = p.getFirstChild();
	// p.appendChild(newNode);
	// } catch (TransformerException e) {
	// throw new XMLDocumentException(e.getClass().getName() + " "
	// + e.getMessage());
	// }
	// }
}