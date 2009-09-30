package it.eng.spagobi.studio.chart.editors.model.chart;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.studio.chart.editors.ChartEditorUtils;
import it.eng.spagobi.studio.chart.editors.model.chart.ChartModel.Dimension;
import it.eng.spagobi.studio.core.log.SpagoBILogger;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.xml.sax.InputSource;

public class ChartModelFactory {

	/** This method create the mdoel of the chart starting from actually opened file and from template file
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */

	public static final String BARCHART="BARCHART";
	public static final String PIECHART="PIECHART";
	public static final String DIALCHART="DIALCHART";
	public static final String CLUSTERCHART="CLUSTERCHART";
	public static final String BOXCHART="BOXCHART";
	public static final String XYCHART="XYCHART";
	public static final String SCATTERCHART="SCATTERCHART";

	public static ChartModel createChartModel(IFile file) throws Exception  {
		SpagoBILogger.infoLog("Start Creating Chart Model");
		ChartModel model = null;
		InputStream thisIs = null;
		InputStream configurationIs = null;
		InputStream templateIs = null;

		try {
			// reads the template file
			SpagoBILogger.infoLog("Getting present file content");
			thisIs = file.getContents();
			SAXReader reader = new SAXReader();
			Document thisDocument = reader.read(thisIs);

			Document configurationDocument = null;

			Element root = thisDocument.getRootElement();
			String type=root.getName();
			SpagoBILogger.infoLog("User selected a chart of type "+type);						
			// here check if a subtype is defined (should be), then get the right template path!
			String subType=root.valueOf("@type");

			// if subtype is null I get the default!!
			if(subType==null || subType.equalsIgnoreCase("")){
				SpagoBILogger.infoLog("Get default subtype");			
				subType=ChartEditorUtils.getDefaultSubtype(type);
			}

			SpagoBILogger.infoLog("Actual subtype is "+subType);			

			SpagoBILogger.infoLog("Getting config file content");						
			String configPath="";			
			// get Configuration File
			try {
				configPath=ChartEditorUtils.getChartConfigPath(type);
				configurationIs = ChartEditorUtils.getInputStreamFromResource(configPath);
				configurationDocument = reader.read(configurationIs);
			} catch (Exception e) {
				SpagoBILogger.errorLog("Error while getting config file content",e);
				throw new Exception("Error while reading " + ChartModel.CHART_INFO_FILE + " file: " + e.getMessage());
			}


			// get the general template Path
			SpagoBILogger.infoLog("Getting template file content");									
			String templatePath="";
//			Document templateDocument = null;
//			try {
//				templatePath=ChartEditorUtils.getChartTemplatePath(type, subType);
//				templateIs = ChartEditorUtils.getInputStreamFromResource(templatePath);
//				templateDocument = reader.read(templateIs);
//			} catch (Exception e) {
//				SpagoBILogger.errorLog("Error while getting template file content",e);
//				throw new Exception("Error while reading Template file: " + e.getMessage());
//			}

			// **** CREATE THE MODEL	****

			if(type.equalsIgnoreCase(ChartModelFactory.BARCHART))
			{
				model=new BarChartModel(type,subType,file,configurationDocument);
			}
			else if(type.equalsIgnoreCase(ChartModelFactory.PIECHART))
			{
				model=new PieChartModel(type,subType, file,configurationDocument);
			}
			else if(type.equalsIgnoreCase(ChartModelFactory.CLUSTERCHART))
			{
				model=new ClusterChartModel(type,subType, file,configurationDocument);
			}
			else if(type.equalsIgnoreCase(ChartModelFactory.BOXCHART))
			{
				model=new BoxChartModel(type,subType, file,configurationDocument);
			}
			else if(type.equalsIgnoreCase(ChartModelFactory.DIALCHART))
			{
				model=new DialChartModel(type, subType,file,configurationDocument);
			}
			else if(type.equalsIgnoreCase(ChartModelFactory.XYCHART))
			{
				model=new XYChartModel(type, subType,file,configurationDocument);
			}
			else if(type.equalsIgnoreCase(ChartModelFactory.SCATTERCHART))
			{
				model=new ScatterChartModel(type, subType,file,configurationDocument);
			}



		} finally {
			if (thisIs != null) thisIs.close();
			if (configurationIs != null) configurationIs.close();
			if (templateIs != null) templateIs.close();
		}

		return model;
	}

	static public String parseISToString(java.io.InputStream is){
		java.io.DataInputStream din = new java.io.DataInputStream(is);
		StringBuffer sb = new StringBuffer();
		try{
			String line = null;
			while((line=din.readLine()) != null){
				sb.append(line+"\n");
			}
		}catch(Exception ex){
			ex.getMessage();
		}finally{
			try{
				is.close();
			}catch(Exception ex){}
		}
		return sb.toString();
	}

}
