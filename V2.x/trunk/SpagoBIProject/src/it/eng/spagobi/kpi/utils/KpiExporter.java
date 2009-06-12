package it.eng.spagobi.kpi.utils;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.engines.kpi.bo.KpiResourceBlock;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;


/**
 * 
 * @author gavardi
 *
 * This class is intended to take the result of a Kpi Execution and giveBack an export in other formats
 *
 *
 */

public class KpiExporter {

	private static transient Logger logger=Logger.getLogger(KpiExporter.class);


	public File getKpiReportPDF(List<KpiResourceBlock> kpiBlocks, BIObject obj, String userId) throws Exception{
		logger.debug("IN");


		//Build report template
		String docName=(obj!=null) ? obj.getName() : "";
		BasicTemplateBuilder basic=new BasicTemplateBuilder(docName);
		String template2=basic.buildTemplate(kpiBlocks);

		//System.out.println(template2);

		String outputType = "PDF";
		HashedMap parameters=new HashedMap();
		parameters.put("PARAM_OUTPUT_FORMAT", outputType);
		//parameters.put("SBI_HTTP_SESSION", session);   ???

		JREmptyDataSource conn=new JREmptyDataSource(1);

		// identity string for object execution
		UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuid_local = uuidGen.generateTimeBasedUUID();
		String executionId = uuid_local.toString();
		executionId = executionId.replaceAll("-", "");

		//Creta etemp file
		String dirS=System.getProperty("java.io.tmpdir");
		File dir = new File(dirS);
		dir.mkdirs();

		logger.debug("Create Temp File");
		String fileName="report"+executionId;
		File tmpFile = File.createTempFile(fileName, "." + outputType, dir);
		OutputStream out = new FileOutputStream(tmpFile);
		try {								

			StringBufferInputStream sbis=new StringBufferInputStream(template2);
			JasperReport report  = JasperCompileManager.compileReport(sbis);

			logger.debug("Filling report ...");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
			logger.debug("Report filled succesfully");

			logger.debug("Exporting report: Output format is [" + outputType + "]");
			JRExporter exporter=null;
			//JRExporter exporter = ExporterFactory.getExporter(outputType);	
			// Set the PDF exporter
			exporter = (JRExporter)Class.forName("net.sf.jasperreports.engine.export.JRPdfExporter").newInstance();
			String mimeType = "application/pdf";

			if(exporter == null) exporter = new JRPdfExporter(); 	

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			exporter.exportReport();
			logger.debug("Report exported succesfully");
			//in = new BufferedInputStream(new FileInputStream(tmpFile));
			logger.debug("OUT");
			return tmpFile;


		} catch(Throwable e) {
			logger.error("An exception has occured", e);
			throw new Exception(e);
		} finally {
			out.flush();
			out.close();
			//tmpFile.delete();

		}

	}
}




