/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.document;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractXMLDocument implements XMLDocumentIFace {

	private Document document;

	private String documentAsXMLString = null;

    public abstract void createDocument(byte[] document) throws XMLDocumentException;

	public Element getElemetByID(String id) throws XMLDocumentException {
		Element element = null;
		element = this.document.getElementById(id);
		return element;
	}

	public String getValue(String key) throws XMLDocumentException {
		String value = null;
		String lst[] = getValues(key);
		if (lst != null) {
			if (lst.length > 1) {
				// Log.logWarning("Specificare meglio la chiave di ricerca o
				// usare il metodo getValues(String key); key=" + key);
				return value;
			}
			value = lst[0];
		}
		return value;
	}

	public Element getDocumentElement() throws XMLDocumentException {
		Element element = null;
		element = this.document.getDocumentElement();
		return element;
	}

	public String[] getValues(String key) throws XMLDocumentException {
		String lst[] = (String[]) null;
		try {
			NodeList nl = XPathAPI.selectNodeList(this.document, key);
			if (nl.getLength() > 0) {
				lst = new String[nl.getLength()];
				for (int i = 0; i < nl.getLength(); i++) {
					Node n = nl.item(i);
					Node nf = n.getFirstChild();
					if (nf != null) {
						lst[i] = nf.getNodeValue();
					} else {
						lst[i] = null;
					}
				}
			}
		} catch (TransformerException e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		}
		return lst;
	}

	public int setValue(String key, String value) throws XMLDocumentException {
		try {
			NodeList nl = XPathAPI.selectNodeList(this.document, key);
			if (nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Node n = nl.item(i);
					Node tn = this.document.createTextNode(value);
					Node nf = n.getFirstChild();
					if (nf != null) {
						n.removeChild(nf);
					}
					n.appendChild(tn);
				}
				return 0;
			} else {
				return -1;
			}
		} catch (TransformerException e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		}
	}

	public void removeKey(String key) throws XMLDocumentException {
		try {
			NodeList nl = XPathAPI.selectNodeList(this.document, key);
			if (nl.getLength() > 0) {
				Node p = nl.item(0).getParentNode();
				for (int i = 0; i < nl.getLength(); i++) {
					Node cn = nl.item(i);
					p.removeChild(cn);
				}
			}
		} catch (TransformerException e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		}
	}

	public void addValue(String path, String key, String value)
			throws XMLDocumentException {
		addValue(path, key, value, null, null);
	}

	public void addValue(String path, String key, String value,
			String[] attributeNames, String[] attributeValues)
			throws XMLDocumentException {
		// NodeList nodeList = XPathAPI.selectNodeList(this.document, path);
		NodeList nodeList = this.document.getElementsByTagName(path);

		Element parent = (Element) nodeList.item(0);
		Element item = this.document.createElement(key);
		item.appendChild(this.document.createTextNode(value));
		parent.appendChild(item);
		// Node newNode = new ElementNode(key);
		// Node newTextNode = new TextNode(value);
		// Node attibuteNode = null;
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
	}

	public void appendChild(Node newNode, String path)
			throws XMLDocumentException {
		try {
			NodeList nl = XPathAPI.selectNodeList(this.document, path);
			Node p = nl.item(0);
			p = p.getFirstChild();
			p.appendChild(newNode);
		} catch (TransformerException e) {
			throw new XMLDocumentException(e.getClass().getName() + " "
					+ e.getMessage());
		}
	}

	public String getDocumentAsXMLString() throws XMLDocumentException {
		documentAsXMLString = "";
		getNodeAsXMLString(this.document.getDocumentElement());
		return documentAsXMLString;
	}

	public void getNodeAsXMLString(Node currentNode) {
		switch (currentNode.getNodeType()) {

		// Document node (root node of tree)
		case Node.DOCUMENT_NODE: {
			Document doc = (Document) currentNode;
			documentAsXMLString = documentAsXMLString + "<"
					+ doc.getDocumentElement().getNodeName() + ">";
			NodeList children = doc.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {
				getNodeAsXMLString(children.item(i));
			}

			documentAsXMLString = documentAsXMLString + "</"
					+ doc.getDocumentElement().getNodeName() + ">";

		}
			break;

		// Element node
		case Node.ELEMENT_NODE: {

			documentAsXMLString = documentAsXMLString + "<"
					+ currentNode.getNodeName();

			NamedNodeMap attributeNodes = currentNode.getAttributes();

			for (int i = 0; i < attributeNodes.getLength(); i++) {
				Attr attribute = (Attr) attributeNodes.item(i);
				documentAsXMLString = documentAsXMLString + " "
						+ attribute.getNodeName() + " = \""
						+ attribute.getNodeValue() + "\"";
			}

			documentAsXMLString = documentAsXMLString + ">";

			NodeList children = currentNode.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {
				getNodeAsXMLString(children.item(i));
			}

			documentAsXMLString = documentAsXMLString + "</"
					+ currentNode.getNodeName() + ">";
		}
			break;

		// CDATA section
		case Node.CDATA_SECTION_NODE:

			documentAsXMLString = documentAsXMLString + "<![CDATA["
					+ currentNode.getNodeValue() + "]]>";
			break;

		// text node
		case Node.TEXT_NODE:

			documentAsXMLString = documentAsXMLString
					+ currentNode.getNodeValue();
			break;
		}
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
