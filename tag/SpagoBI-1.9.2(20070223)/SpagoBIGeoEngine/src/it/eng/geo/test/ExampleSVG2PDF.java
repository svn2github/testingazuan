/**
 *
 *	LICENSE: see COPYING file
 *
**/
 
package it.eng.geo.test;

//Java
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//Batik
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

//FOP
import org.apache.fop.svg.PDFTranscoder;

/**
 * This class demonstrates the conversion of an SVG file to PDF using FOP.
 */
public class ExampleSVG2PDF {

    /**
     * Converts an FO file to a PDF file using FOP
     * @param svg the SVG file
     * @param pdf the target PDF file
     * @throws IOException In case of an I/O problem
     * @throws TranscoderException In case of a transcoding problem
     */
    public void convertSVG2PDF(File svg, File pdf) throws IOException, TranscoderException {
        
        //Create transcoder
        Transcoder transcoder = new PDFTranscoder();
        //Transcoder transcoder = new org.apache.fop.render.ps.PSTranscoder();
        
        //Setup input
        InputStream in = new java.io.FileInputStream(svg);
        try {
            TranscoderInput input = new TranscoderInput(in);
            
            //Setup output
            OutputStream out = new java.io.FileOutputStream(pdf);
            out = new java.io.BufferedOutputStream(out);
            try {
                TranscoderOutput output = new TranscoderOutput(out);
                
                //Do the transformation
                transcoder.transcode(input, output);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }


    /**
     * Main method.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try {
            //Setup directories
            File baseDir = new File("C:/Progetti/Regione_Veneto/Georeferenziazione/g/VE_comuni/");
            File outDir = new File(baseDir, "out");
            outDir.mkdirs();

            //Setup input and output files            
            File svgfile = new File(baseDir, "ve_comuni_rv1.svg");
            File pdffile = new File(outDir, "ve_comuni_rv1.pdf");

            
            ExampleSVG2PDF app = new ExampleSVG2PDF();
            app.convertSVG2PDF(svgfile, pdffile);
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}