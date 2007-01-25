/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.document;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface XMLDocumentIFace {

	public void createDocument(byte[] document) throws XMLDocumentException;

	public Document getDocument();

	public Element getElemetByID(String id) throws XMLDocumentException;

	public String getDocumentAsXMLString() throws XMLDocumentException;

	public String getValue(String key) throws XMLDocumentException;

	public Element getDocumentElement() throws XMLDocumentException;

	public String[] getValues(String key) throws XMLDocumentException;

	public int setValue(String key, String value) throws XMLDocumentException;

	public void removeKey(String key) throws XMLDocumentException;

	public void addValue(String path, String key, String value)
			throws XMLDocumentException;

	public void addValue(String path, String key, String value,
			String[] attributeNames, String[] attributeValues)
			throws XMLDocumentException;

	public void appendChild(Node newNode, String path)
			throws XMLDocumentException;

}
