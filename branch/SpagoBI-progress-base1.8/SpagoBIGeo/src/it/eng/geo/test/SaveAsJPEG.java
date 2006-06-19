package it.eng.geo.test;

import java.io.*;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

public class SaveAsJPEG {

    public static void main(String [] args) throws Exception {

        // create a JPEG transcoder
        JPEGTranscoder t = new JPEGTranscoder();
        // set the transcoding hints
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                             new Float(.8));
        // create the transcoder input
        String svgURI = "file:///C:/Progetti/Regione_Veneto/Georeferenziazione/g/VE_comuni/ve_comuni_rv1.svg";
        TranscoderInput input = new TranscoderInput(svgURI);
        // create the transcoder output
        OutputStream ostream = new FileOutputStream("c:/temp/out.jpg");
        TranscoderOutput output = new TranscoderOutput(ostream);
        // save the image
        t.transcode(input, output);
        // flush and close the stream then exit
        ostream.flush();
        ostream.close();
        System.exit(0);
    }
}