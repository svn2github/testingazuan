/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.svg2svg.PrettyPrinter;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestBatik {


    public static void main(String[] args) throws IOException {

        String uRIString = "file:///C:/Progetti/Regione_Veneto/Georeferenziazione/g/VE_comuni/ve_comuni_rv1.svg";
//        uRIString = "C:/Temp/test.xml";
//          String uRIString ="http://localhost:8080/SVGWeb/img/maps/ve_comuni.svg";
//        
        InputStream inputStream = null;
//        if (uRIString.toLowerCase().indexOf(HTTP) == 0) {
            
            URL uRL = new URL(uRIString);
            URLConnection urlConn;
            urlConn = uRL.openConnection();
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);

            inputStream = new DataInputStream(urlConn.getInputStream());
            
//        } else {
//            
//            File file = new File(uRIString);
//            try {
//                inputStream = new FileInputStream(file);
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            
//        }

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
        String docString = new String (baos.toByteArray());
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
//        SVGDocument docSVG = f.createSVGDocument(uRIString);
        SVGDocument docSVG = null;
        try {
            docSVG = (SVGDocument)f.createSVGDocument(null,new StringReader(docString));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        PrettyPrinter pp = new PrettyPrinter();
        OutputStream ostream = new FileOutputStream("c:/temp/svg.svg");

        try {
			pp.print(new StringReader(docString),new OutputStreamWriter(ostream));
		} catch (TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//        Document doc = null;
//        try {
//            doc = (Document)f.createDocument(null,new StringReader(docString));
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
//        FileOutputStream fos = new FileOutputStream("C:/Progetti/Regione_Veneto/Georeferenziazione/g/VE_comuni/ve_comuni_rv1_bis.svg");
//        Writer outWriter = new OutputStreamWriter(fos,"UTF-8");
//        svgGenerator.stream(outWriter, false);
               
        
        
        
        
        
        
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
        try {
            DOMUtilities.writeDocument(docSVG, outputStreamWriter);
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
    }

}