package it.eng.geo.test;

import it.eng.geo.document.SVGMapDocument;
import it.eng.geo.document.XMLDocumentException;
import it.eng.geo.map.DefaultMapProvider;

import java.io.IOException;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestDefaultMapProvider{


    public static void main(String[] args) throws IOException {
        DefaultMapProvider sVGMapProvider = new DefaultMapProvider();
        //SVGMapDocument sVGMapDocument = sVGMapProvider.getSVGMapDocument(null);
        /*
        try {
            System.out.println(sVGMapDocument.getDocumentAsXMLString());
        } catch (XMLDocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
    }
}