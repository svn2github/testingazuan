package it.eng.spagobi.kpi.service;

import it.businesslogic.ireport.IReportTemplate;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.chart.service.GetPngAction;
import it.eng.spagobi.engines.kpi.bo.KpiResourceBlock;
import it.eng.spagobi.kpi.utils.BasicTemplateBuilder;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

public class KpiExporter extends AbstractHttpAction {

	private static transient Logger logger=Logger.getLogger(KpiExporter.class);

	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {


		logger.debug("IN");
		HttpServletRequest httpRequest = getHttpRequest();
		HttpSession session=httpRequest.getSession();
		List<KpiResourceBlock> listKpiBlocks=(List<KpiResourceBlock>)session.getAttribute("KPI_BLOCK");
		KpiResourceBlock resBlock=new KpiResourceBlock();

		// recover Object Id
		Object idObject=serviceRequest.getAttribute(SpagoBIConstants.OBJECT_ID);
		
		if(idObject==null){
		logger.error("Document id not found");
		return;
		}
		
		Integer id=Integer.valueOf(idObject.toString());
		
		BIObject document=DAOFactory.getBIObjectDAO().loadBIObjectById(id);
		String docName=document.getName();
		
		BasicTemplateBuilder basic=new BasicTemplateBuilder(docName);

		String template2=basic.buildTemplate(listKpiBlocks);

		this.freezeHttpResponse();

		HashedMap parameters=new HashedMap();
		String userId=null;

		Object userIdO=serviceRequest.getAttribute("user_id");	
		if(userIdO!=null)userId=userIdO.toString();

		JREmptyDataSource conn=new JREmptyDataSource(1);
		
		String outputType = "PDF";
		parameters.put("PARAM_OUTPUT_FORMAT", outputType);
		//parameters.put("SBI_USERID", userUniqueIdentifier);
		parameters.put("SBI_HTTP_SESSION", session);
		//parameters.put("SBI_RESOURCE_PATH", resourcePath);

		SpagoBIAccessUtils util = new SpagoBIAccessUtils();
		// identity string for object execution
		UUIDGenerator uuidGen  = UUIDGenerator.getInstance();
		UUID uuid_local = uuidGen.generateTimeBasedUUID();
		String executionId = uuid_local.toString();
		executionId = executionId.replaceAll("-", "");


		//Creta etemp file
		String tmpdir=System.getProperty("java.io.tmpdir");
		tmpdir=tmpdir + System.getProperty("file.separator") + "reports";
		File dir = new File(tmpdir);
		dir.mkdirs();

		File tmpFile = File.createTempFile("report", "." + outputType, dir);
		OutputStream out = new FileOutputStream(tmpFile);

		//ContentServiceProxy contentProxy=new ContentServiceProxy(userId,session);

		try {								
//			IReportTemplate template=new IReportTemplate();
//			String rootPath=ConfigSingleton.getRootPath();
//			String templateDirPath=rootPath+"/WEB-INF/classes/it/eng/spagobi/kpi/exporterKpi/";
//			templateDirPath+="robaKPI.jrxml";

			
//			String tmpdirTTT=tmpdir + System.getProperty("file.separator") + "templateKpiTemp.jrxml";	
//			File file=new File(tmpdirTTT);

			StringBufferInputStream sbis=new StringBufferInputStream(template2);
			
			//FileInputStream fis=new FileInputStream(file);

			JasperReport report  = JasperCompileManager.compileReport(sbis);

			//parameters = adaptReportParams(parameters, report);


			logger.debug("Filling report ...");
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, conn);
			logger.debug("Report filled succesfully");

			logger.debug("Exporting report: Output format is [" + outputType + "]");
			JRExporter exporter=null;
			//JRExporter exporter = ExporterFactory.getExporter(outputType);	
			exporter = (JRExporter)Class.forName("net.sf.jasperreports.engine.export.JRPdfExporter").newInstance();
			String mimeType = "application/pdf";

			HttpServletResponse response = getHttpResponse();
			//ServletOutputStream outSOS = response.getOutputStream();
			response.setContentType(mimeType);				
			if(exporter == null) exporter = new JRPdfExporter(); 	

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			exporter.exportReport();
			logger.debug("Report exported succesfully");



			response.setHeader("Content-Disposition", "filename=\"report." + outputType + "\";");
			// response.setContentType((String)extensions.get(outputType));
			response.setContentLength((int) tmpFile.length());

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(tmpFile));
			int b = -1;
			while ((b = in.read()) != -1) {
				response.getOutputStream().write(b);
			}
			response.getOutputStream().flush();
			in.close();
			// instant cleaning
			tmpFile.delete();






			// instant cleaning




		} catch(Throwable e) {
			logger.error("An exception has occured", e);
			throw new Exception(e);
		} finally {

			out.flush();
			out.close();

		}























	}




}
