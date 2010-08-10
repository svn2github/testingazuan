package it.eng.spagobi.engines.exporters;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author giachino (antonella.giachino@eng.it)
 *
 * This class is intended to take the result of a Chart Execution and giveBack an export in other formats
 *
 *
 */

public class ChartExporter
{

    private static transient Logger logger = Logger.getLogger("ChartExporter");
    private static int MAX_NUM_IMG = 5;
    private static String HORIZONTAL_ORIENTATION = "horizontal";


    public File getChartPDF(String uuid, boolean multichart, String orientation)
        throws Exception
    {
        logger.debug("IN");
  
        File tmpFile;

        try{
	        tmpFile = null;
	        String dir = System.getProperty("java.io.tmpdir");
	        String path = (new StringBuilder(String.valueOf(dir))).append("/").append(uuid).append(".png").toString();
	        File dirF = new File(dir);
	        tmpFile = File.createTempFile("tempPDFExport", ".pdf", dirF);
	        Document pdfDocument = new Document();
	        PdfWriter docWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream(tmpFile));
	        pdfDocument.open();
	        if(multichart)
	        {
	            List images = new ArrayList();
	            for(int i = 0; i < MAX_NUM_IMG; i++)
	            {
	                String imgName = (new StringBuilder(String.valueOf(path.substring(0, path.indexOf(".png"))))).append(i).append(".png").toString();
	                Image png = Image.getInstance(imgName);
	                if(png == null)
	                {
	                    break;
	                }
	                images.add(png);
	            }
	
	            Table table = new Table(images.size());
	            for(int i = 0; i < images.size(); i++)
	            {
	                Image png = (Image)images.get(i);
	                if(HORIZONTAL_ORIENTATION.equalsIgnoreCase(orientation))
	                {
	                    Cell pngCell = new Cell(png);
	                    pngCell.setBorder(0);
	                    table.setBorder(0);
	                    table.addCell(pngCell);
	                } else
	                {
	                    png.setAlignment(5);
	                    pdfDocument.add(png);
	                }
	            }
	
	            pdfDocument.add(table);
	        } else
	        {
	            Image jpg = Image.getInstance(path);
	            pdfDocument.add(jpg);
	        }
	        pdfDocument.close();
	        docWriter.close();
	        
	        logger.debug("OUT");
	        
	        return tmpFile;
	        
        } catch(Throwable e) {
			logger.error("An exception has occured", e);
			throw new Exception(e);
		} finally {

			//tmpFile.delete();

		}
    }
}
