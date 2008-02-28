/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/


package it.eng.spagobi.engines.chart;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.engines.chart.charttypes.Dashboard;
import it.eng.spagobi.engines.chart.charttypes.SBISpeedometer;
import it.eng.spagobi.engines.chart.charttypes.SimpleDial;
import it.eng.spagobi.engines.chart.charttypes.Thermometer;
import it.eng.spagobi.engines.chart.charttypes.utils.LovAccessFunctions;
import it.eng.spagobi.services.common.IProxyService;
import it.eng.spagobi.services.common.IProxyServiceFactory;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.security.Principal;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultValueDataset;



/**
 * Action that returns a PNG of the chart as specified in template
 * @author Giulio Gavardi 
 *   giulio.gavardi@eng.it
 */



public class GetJFreeChartAction extends AbstractHttpAction {

	private static transient Logger logger=Logger.getLogger(GetJFreeChartAction.class);

	private EMFErrorHandler errorHandler = null;
	private String title="";
	private String type="";
	private IEngUserProfile profile = null;
	private ChartImpl sbi=null;




	public void service(SourceBean request, SourceBean responseSb) throws Exception {
		logger.debug("IN");
		freezeHttpResponse();

		HttpServletResponse response = getHttpResponse();
		ServletOutputStream out = response.getOutputStream();

		HttpServletRequest req = getHttpRequest();
		String userId=null;
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean validateSB =(SourceBean) config.getAttribute("SPAGOBI_SSO.ACTIVE");
		String active = (String) validateSB.getCharacters();
		errorHandler = getErrorHandler();

		if (active != null && active.equals("true")){
			Principal principal= req.getUserPrincipal();
			if (principal!=null) {
				userId=principal.getName();
				logger.debug("got userId from Principal="+userId);
			}
			IProxyService proxy=IProxyServiceFactory.createProxyService();
			userId=proxy.readUserId(req.getSession());
			logger.debug("got userId from IProxyService="+userId);
		}else {
			userId = (String)request.getAttribute("userid");
			logger.debug("got userId from Request="+userId);
		}


		ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
		try {
			SpagoBIUserProfile user= supplier.createUserProfile(userId);
			profile=new UserProfile(user);
		} catch (Exception e) {
			logger.error("Reading user information... ERROR",e);
			throw new SecurityException();
		}


		String documentId = (String)request.getAttribute("documentid");

		logger.debug("got parameters userId="+userId+" and documentId="+documentId.toString());

		//get the template
		logger.debug("getting template");


		try{
			SourceBean content = null;

			byte[] contentBytes = null;
			try{
				ObjTemplate template = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(Integer.valueOf(documentId));
				if(template==null) throw new Exception("Active Template null");
				contentBytes = template.getContent();
				if(contentBytes==null) throw new Exception("Content of the Active template null"); 

			// get bytes of template and transform them into a SourceBean
				
				String contentStr = new String(contentBytes);
				content = SourceBean.fromXMLString(contentStr);
			} catch (Exception e) {
				logger.error("Error while converting the Template bytes into a SourceBean object");
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2003);
				throw userError;
			}

			String nameSB=content.getName();
			type = (String)content.getAttribute("type");
			
			sbi=ChartImpl.createChart(nameSB, type);
			

		/*	if(type.equalsIgnoreCase("speedometer")){
				sbi=new SBISpeedometer();
			}
			else if(type.equalsIgnoreCase("simpledial")){
				sbi= new SimpleDial();
			}
			else if(type.equalsIgnoreCase("thermomether")){
				sbi= new Thermometer();
			}
			else if(type.equalsIgnoreCase("dashboard")){
				sbi= new Dashboard();
			}
			else {
				logger.error("type in template not known");
			}
*/
			sbi.setProfile(profile);
			sbi.configureChart(content);



			// Get the value from the LOV
/*
			String res=LovAccessFunctions.getLovResult(profile, sbi.getDataName());

			SourceBean sbRows=SourceBean.fromXMLString(res);
			SourceBean sbRow=(SourceBean)sbRows.getAttribute("ROW");
			String result=(String)sbRow.getAttribute("value");

			DefaultValueDataset dataset = new DefaultValueDataset(Double.valueOf(result));

*/
			
			Dataset dataset=sbi.calculateValue();

			try{
				JFreeChart chart = sbi.createChart(title,dataset);

				logger.debug("successfull chart creation");
				response.setContentType("image/png");
				ChartUtilities.writeChartAsPNG(out, chart, sbi.getHeight(), sbi.getWidth()); 

			}
			catch (Exception eex) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 2004);
				logger.debug("The message parameter is null");
				throw userError;
			}

			logger.debug("OUT");


		}
		catch (EMFUserError e) {
			errorHandler.addError(e);
		}
		catch (Exception e) {
			EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
			logger.error("Generic Error");
			errorHandler.addError(userError);
		}
	}






}


