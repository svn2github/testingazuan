/*
 * Created on 4-mag-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.geo.map;

import it.eng.geo.document.XMLDocumentIFace;
import it.eng.spago.base.SourceBean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.ext.swing.Resources;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.svg.SVGDocument;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TESTBatikMapProvider extends AbstractMapProvider {

    private static final String URI = "URI";
    private static final String HTTP = "http://";

    /**
     *  
     */
    public TESTBatikMapProvider() {
        super();
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.eng.geo.mapproviders.SVGMapProviderIFace#getSVGMap()
     */
    public XMLDocumentIFace getSVGMapDocument(SourceBean mapProviderConfiguration) {

        String uRI = (String) mapProviderConfiguration.getAttribute(URI);
        InputStream inputStream = null;
//        
//        UserAgentAdapter userAgent = new UserAgentAdapter();
//        DocumentLoader loader   = new DocumentLoader(userAgent);
//        SVGDocument docnew = null;
//        try {
//            docnew = (SVGDocument)loader.loadDocument("http://localhost:8080/SVGWeb/img/maps/ve_comuni.svg");
//        } catch (IOException e6) {
//            // TODO Auto-generated catch block
//            e6.printStackTrace();
//        }
//        
//        ByteArrayOutputStream b = new ByteArrayOutputStream();
//        OutputStreamWriter out = new OutputStreamWriter(b);
//        try {
//            DOMUtilities.writeDocument(docnew, out);
//        } catch (IOException e2) {
//            // TODO Auto-generated catch block
//            e2.printStackTrace();
//        }
//        System.out.println("SchedulerInitializer::init: FINE " + new String(b.toByteArray()));
//
//        
//        
//        
        if (uRI.toLowerCase().indexOf(HTTP) == 0) {
            
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
            
        } else {
            
            File file = new File(uRI);
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
        try {
            handle(URI,docString);
        } catch (Exception e5) {
            // TODO Auto-generated catch block
            e5.printStackTrace();
        }
        
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
//        Document docSVG = f.createDocument(uRIString);
        SVGDocument docSVG = null;
        try {
            docSVG = (SVGDocument)f.createSVGDocument(null,new StringReader(docString));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
        try {
            DOMUtilities.writeDocument(docSVG, outputStreamWriter);
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        System.out.println("SchedulerInitializer::init: FINE " + new String(byteArrayOutputStream.toByteArray()));

        return null;
    }
///////////////


     public static final String[] XVG_MIME_TYPES = 
     { "image/xml+xsl+svg" };

     public static final String[] XVG_FILE_EXTENSIONS =
     { ".xml", ".xsl" };

     public static final String ERROR_NO_XML_STYLESHEET_PROCESSING_INSTRUCTION
         = "XMLInputHandler.error.no.xml.stylesheet.processing.instruction";

     public static final String ERROR_TRANSFORM_OUTPUT_NOT_SVG
         = "XMLInputHandler.error.transform.output.not.svg";

     public static final String ERROR_TRANSFORM_PRODUCED_NO_CONTENT
         = "XMLInputHandler.error.transform.produced.no.content";

     public static final String ERROR_TRANSFORM_OUTPUT_WRONG_NS
         = "XMLInputHandler.error.transform.output.wrong.ns";

     public static final String ERROR_RESULT_GENERATED_EXCEPTION 
         = "XMLInputHandler.error.result.generated.exception";

     public static final String XSL_PROCESSING_INSTRUCTION_TYPE
         = "text/xsl";

     public static final String PSEUDO_ATTRIBUTE_TYPE
         = "type";

     public static final String PSEUDO_ATTRIBUTE_HREF
         = "href";

     /**
      * Returns the list of mime types handled by this handler.
      */
     public String[] getHandledMimeTypes() {
         return XVG_MIME_TYPES;
     }
     
     /**
      * Returns the list of file extensions handled by this handler
      */
     public String[] getHandledExtensions() {
         return XVG_FILE_EXTENSIONS;
     }

     /**
      * Returns a description for this handler
      */
     public String getDescription() {
         return "";
     }

     /**
      * Returns true if the input file can be handled by the handler
      */
     public boolean accept(File f) {
         return f.isFile() && accept(f.getPath());
     }

     /**
      * Returns true if the input URI can be handled by the handler
      */
     public boolean accept(ParsedURL purl) {
         if (purl == null) {
             return false;
         }

         // <!> Note: this should be improved to rely on Mime Type 
         //     when the http protocol is used. This will use the 
         //     ParsedURL.getContentType method.

         String path = purl.getPath();        
         return accept(path);
     }

     /**
      * Return true if the resource with the given path can 
      * be handled.
      */
     public boolean accept(String path) {
         if (path == null) {
             return false;
         }

         for (int i=0; i<XVG_FILE_EXTENSIONS.length; i++) {
             if (path.endsWith(XVG_FILE_EXTENSIONS[i])) {
                 return true;
             }
         }

         return false;
     }

     /**
      * Handles the given input for the given JSVGViewerFrame
      */
     public void handle(String purl,String doc) throws Exception {
         String uri = purl.toString();

         TransformerFactory tFactory 
             = TransformerFactory.newInstance();
         
         // First, load the input XML document into a generic DOM tree
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setValidating(false);
         dbf.setNamespaceAware(true);

         DocumentBuilder db = dbf.newDocumentBuilder();

         Document inDoc = db.parse(uri);
        
//         // Now, look for <?xml-stylesheet ...?> processing instructions
//         String xslStyleSheetURI 
//             = extractXSLProcessingInstruction(inDoc);
//         
//         if (xslStyleSheetURI == null) {
//             // Assume that the input file is a literal result template
//             xslStyleSheetURI = uri;
//         }
//
//         ParsedURL parsedXSLStyleSheetURI 
//             = new ParsedURL(uri, xslStyleSheetURI);
//
//         Transformer transformer
//             = tFactory.newTransformer
//             (new StreamSource(parsedXSLStyleSheetURI.toString()));
//
//         // Set the URIResolver to properly handle document() and xsl:include
//         transformer.setURIResolver
//             (new DocumentURIResolver(parsedXSLStyleSheetURI.toString()));
//
//         // Now, apply the transformation to the input document.
//         //
//         // <!> Due to issues with namespaces, the transform creates the 
//         //     result in a stream which is parsed. This is sub-optimal
//         //     but this was the only solution found to be able to 
//         //     generate content in the proper namespaces.
//         //
//         // SVGOMDocument outDoc = 
//         //   (SVGOMDocument)impl.createDocument(svgNS, "svg", null);
//         // outDoc.setURLObject(new URL(uri));
//         // transformer.transform
//         //     (new DOMSource(inDoc),
//         //     new DOMResult(outDoc.getDocumentElement()));
//         //
         StringWriter sw = new StringWriter();
         StreamResult result = new StreamResult(sw);
//         transformer.transform(new DOMSource(inDoc),
//                               result);
//         sw.flush();
//         sw.close();

         String parser = XMLResourceDescriptor.getXMLParserClassName();
         SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
         SVGDocument outDoc = null;

         try {
             outDoc = f.createSVGDocument
                 (uri, new StringReader(doc));
         } catch (Exception e) {
             System.err.println("======================================");
             System.err.println(sw.toString());
             System.err.println("======================================");
             
             throw new IllegalArgumentException
                 (Resources.getString(ERROR_RESULT_GENERATED_EXCEPTION));
         }

         // Patch the result tree to go under the root node
         // checkAndPatch(outDoc);
         
//         svgViewerFrame.getJSVGCanvas().setSVGDocument(outDoc);
//         svgViewerFrame.setSVGDocument(outDoc,
//                                       uri,
//                                       outDoc.getTitle());
     }

     /**
      * This method checks that the generated content is SVG.
      *
      * This method accounts for the fact that the root svg's first child
      * is the result of the transform. It moves all its children under the root
      * and sets the attributes
      */
     protected void checkAndPatch(Document doc) {
         Element root = doc.getDocumentElement();
         Node realRoot = root.getFirstChild();
         String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

         if (realRoot == null) {
             throw new IllegalArgumentException
                 (Resources.getString(ERROR_TRANSFORM_PRODUCED_NO_CONTENT));
         }

         if (realRoot.getNodeType() != Node.ELEMENT_NODE
             || 
             !SVGConstants.SVG_SVG_TAG.equals(realRoot.getLocalName())) {
             throw new IllegalArgumentException
                 (Resources.getString(ERROR_TRANSFORM_OUTPUT_NOT_SVG));
         }

         if (!svgNS.equals(realRoot.getNamespaceURI())) {
             throw new IllegalArgumentException
                 (Resources.getString(ERROR_TRANSFORM_OUTPUT_WRONG_NS));
         }

         Node child = realRoot.getFirstChild();
         while ( child != null ) {
             root.appendChild(child);
             child = realRoot.getFirstChild();
         }

         NamedNodeMap attrs = realRoot.getAttributes();
         int n = attrs.getLength();
         for (int i=0; i<n; i++) {
             root.setAttributeNode((Attr)attrs.item(i));
         }

         root.removeChild(realRoot);
     }

     /**
      * Extracts the first XSL processing instruction from the input 
      * XML document. 
      */
     protected String extractXSLProcessingInstruction(Document doc) {
         Node child = doc.getFirstChild();
         while (child != null) {
             if (child.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                 ProcessingInstruction pi 
                     = (ProcessingInstruction)child;
                 
                 HashTable table = new HashTable();
                 DOMUtilities.parseStyleSheetPIData(pi.getData(),
                                                    table);

                 Object type = table.get(PSEUDO_ATTRIBUTE_TYPE);
                 if (XSL_PROCESSING_INSTRUCTION_TYPE.equals(type)) {
                     Object href = table.get(PSEUDO_ATTRIBUTE_HREF);
                     if (href != null) {
                         return href.toString();
                     } else {
                         return null;
                     }
                 }
             }
             child = child.getNextSibling();
         }

         return null;
     }

     /**
      * Implements the URIResolver interface so that relative urls used in 
      * transformations are resolved properly.
      */
     public class DocumentURIResolver implements URIResolver {
         String documentURI;

         public DocumentURIResolver(String documentURI) {
             this.documentURI = documentURI;
         }

         public Source resolve(String href, String base) {
             if (base == null || "".equals(base)) {
                 base = documentURI;
             }

             ParsedURL purl = new ParsedURL(base, href);

             return new StreamSource(purl.toString());
         }
     }


}