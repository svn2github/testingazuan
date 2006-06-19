package it.eng.geo.render;

import it.eng.geo.configuration.Constants;
import it.eng.geo.configuration.MapConfiguration;
import it.eng.geo.datamart.DatamartObject;
import it.eng.geo.datamart.DatamartProviderFactory;
import it.eng.geo.datamart.DatamartProviderIFace;
import it.eng.geo.document.XMLDocumentIFace;
import it.eng.geo.map.MapProviderFactory;
import it.eng.geo.map.MapProviderIFace;
import it.eng.spago.base.SourceBean;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 * Classe <code>SessionExpiredAction</code> Gestione dell'avvenuta scadenza
 * della sessione
 * 
 * @author
 * @version 1.0
 */
public class MapRenderer {

	/**
	 * <code>service</code> Metodo contenente la logica per l'esecuzione del
	 * comando
	 * 
	 * @param arg0
	 *            di tipo <code>String</code>
	 * @exception Exception
	 *                Se si sono verificati problemi, in realta' usato per
	 *                forzare l'uso del blocco try-catch
	 */
	public byte[] renderMap(byte[] template) throws Exception {

		MapConfiguration mapConfiguration = new MapConfiguration(template);

		SourceBean mapProviderConfiguration = mapConfiguration
				.getMapProviderConfiguration();
		MapProviderIFace mapProvider = MapProviderFactory
				.getMapProvider((String) mapProviderConfiguration
						.getAttribute(Constants.CLASS_NAME));
		XMLDocumentIFace xMLDocument = (XMLDocumentIFace) mapProvider
				.getSVGMapDocument(mapConfiguration
						.getMapProviderConfiguration());

		SourceBean datamartProviderConfiguration = mapConfiguration
				.getDatamartProviderConfiguration();
		DatamartProviderIFace datamartProvider = DatamartProviderFactory
				.getDatamartProvider((String) datamartProviderConfiguration
						.getAttribute(Constants.CLASS_NAME));
		DatamartObject datamartObject = (DatamartObject) datamartProvider
				.getDatamartObject(datamartProviderConfiguration);

		ArrayList elementIdList = datamartObject.getElementIdList();
		ArrayList valueList = datamartObject.getValueList();
		for (int i = 0; i < elementIdList.size(); i++) {
			String elementId = (String) elementIdList.get(i);
			String value = (String) valueList.get(i);
			Element element = xMLDocument.getElemetByID(elementId);
			String style = mapConfiguration.getStyle(Integer.parseInt(value));
			element.setAttribute(Constants.STYLE, style);
		}

		// // mapConfiguration.addLegend(xMLDocument);
		// XercesXMLDocument legend = mapConfiguration.getLegend();
		// Node legendNode = (Node)legend.getDocumentElement();
		// // Node svgNode = xMLDocument.getDocumentElement();
		// Document svgDoc = xMLDocument.getDocument();
		// System.out.println(legendNode.getNodeName());
		// NodeList nodeList = legendNode.getChildNodes();
		// for (int i = 0; i < nodeList.getLength(); i++) {
		// Node node = nodeList.item(i);
		// System.out.println(node.getFirstChild().getNodeName());
		// svgDoc.adoptNode(node);
		// xMLDocument.appendChild(node.getFirstChild(),"svg");
		//
		// // svgNode.appendChild(node);
		// }
		// soluzione di backup
		String mapString = xMLDocument.getDocumentAsXMLString();
		String legendString = mapConfiguration.getLegend();
		mapString = mapString.substring(0, mapString.indexOf("</svg>"))
				+ legendString + "</svg>";
		return mapString.getBytes();

	}
}