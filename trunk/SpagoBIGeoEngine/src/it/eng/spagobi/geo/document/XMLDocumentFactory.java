/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.document;


/**
 * <p>
 * </p>
 * 
 * @stereotype factory
 */
public class XMLDocumentFactory {

    /**
     * <p>
     * Does ...
     * </p>
     * 
     * 
     * 
     * @param serviceId
     */
    public static XMLDocumentIFace getXMLDocument(String className) throws Exception {

        return (XMLDocumentIFace) Class.forName(className).newInstance();
    }
}