/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Test {

    private static final String HTTP = "http://";

    public static void main(String[] args) throws IOException {

        String uRIString = "C:/progetti/svg/SVGWeb/img/maps/ve_comuni.svg";
//        uRIString = "C:/Documents and Settings/Administrator/Documenti/download/SVG/MapInfo2SVG/NE_States.svg";
//          String uRIString ="http://localhost:8080/SVGWeb/img/maps/ve_comuni.svg";
        
        InputStream inputStream = null;
        if (uRIString.toLowerCase().indexOf(HTTP) == 0) {
            
            URL uRL = new URL(uRIString);
            URLConnection urlConn;
            urlConn = uRL.openConnection();
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);

            inputStream = new DataInputStream(urlConn.getInputStream());
            
        } else {
            
            File file = new File(uRIString);
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
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

        String docString  = new String(baos.toByteArray());
        
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
//        Document docSVG = f.createDocument(uRIString);
        SVGDocument docSVG = (SVGDocument)f.createSVGDocument(null,new StringReader(docString));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
        DOMUtilities.writeDocument(docSVG, outputStreamWriter);

        
//        SVGMapDocument sVGMapDocument = null;
//        try {
//            sVGMapDocument = new SVGMapDocument(baos.toByteArray());
//        } catch (XMLDocumentException e2) {
//            // TODO Auto-generated catch block
//            e2.printStackTrace();
//        }

    }

}