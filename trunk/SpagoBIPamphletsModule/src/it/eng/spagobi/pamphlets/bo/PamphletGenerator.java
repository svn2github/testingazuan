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
package it.eng.spagobi.pamphlets.bo;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.pamphlets.constants.PamphletsConstants;
import it.eng.spagobi.pamphlets.dao.IPamphletsCmsDao;
import it.eng.spagobi.pamphlets.dao.PamphletsCmsDaoImpl;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import sun.misc.BASE64Decoder;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.UnoUrlResolver;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.container.XNamed;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPages;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.drawing.XShapes;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;

public class PamphletGenerator {

	private String pathPamphlet = "";
	private File tempDir = null;
	private XDesktop xdesktop = null;
	
	public PamphletGenerator(String pathPamp) throws Exception {
		pathPamphlet = pathPamp;
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean pathTmpFoldSB = (SourceBean)config.getAttribute("PAMPHLETS.PATH_TMP_FOLDER");
		String pathTmpFold = (String)pathTmpFoldSB.getAttribute("path");
		String pathTmpFoldPamp = pathTmpFold + pathPamphlet;
		tempDir = new File(pathTmpFoldPamp); 
		tempDir.mkdirs();
		// initialize openoffice environment
		SourceBean officeConnectSB = (SourceBean)config.getAttribute("PAMPHLETS.OFFICECONNECTION");
		String host = (String)officeConnectSB.getAttribute("host");
		String port = (String)officeConnectSB.getAttribute("port");
		XComponentContext  xRemoteContext = Bootstrap.createInitialComponentContext(null);
		XUnoUrlResolver urlResolver = UnoUrlResolver.create(xRemoteContext);    
		Object initialObject = urlResolver.resolve("uno:socket,host="+host+",port="+port+";urp;StarOffice.ServiceManager");
		XMultiComponentFactory xRemoteServiceManager = (XMultiComponentFactory)UnoRuntime.queryInterface(XMultiComponentFactory.class, initialObject);
		XPropertySet xProperySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xRemoteServiceManager);
		Object oDefaultContext = xProperySet.getPropertyValue("DefaultContext");
		xRemoteContext = (XComponentContext)UnoRuntime.queryInterface(XComponentContext.class, oDefaultContext);
		Object desktop = xRemoteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemoteContext);
		xdesktop = (XDesktop)UnoRuntime.queryInterface(XDesktop.class, desktop);
	}
	
	
	
	
	public void generateTemplateParts() {
		try{
			// gets the template file data
			IPamphletsCmsDao pampDao = new PamphletsCmsDaoImpl();
			String templateFileName = pampDao.getPamphletTemplateFileName(pathPamphlet);
			InputStream contentTempIs = pampDao.getPamphletTemplateContent(pathPamphlet);
	        byte[] contentTempBytes = GeneralUtilities.getByteArrayFromInputStream(contentTempIs);
	        contentTempIs.close();
	        // write template content into a temp file
	        File templateFile = new File(tempDir, templateFileName);
	        FileOutputStream fosTemplate = new FileOutputStream(templateFile);
	        fosTemplate.write(contentTempBytes);
	        fosTemplate.flush();
	        fosTemplate.close();
            XComponentLoader xComponentLoader = (XComponentLoader)UnoRuntime.queryInterface(XComponentLoader.class, xdesktop);
	        // load the template into openoffice
            XComponent xComponent = openTemplate(xComponentLoader, "file:///" + templateFile.getAbsolutePath());	        
	        // gets the number of the parts of the documents
            XMultiServiceFactory xServiceFactory = (XMultiServiceFactory)UnoRuntime.queryInterface(XMultiServiceFactory.class, xComponent);
            XDrawPagesSupplier xDrawPageSup = (XDrawPagesSupplier)UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
            XDrawPages drawPages = xDrawPageSup.getDrawPages();
            int numPages = drawPages.getCount();
            // built the structure into the cms 
            IPamphletsCmsDao pampdao = new PamphletsCmsDaoImpl();
            pampdao.createStructureForTemplate(pathPamphlet, numPages);
            // for each part of the document gets the image images and stores them into cms
            for(int i=0; i<numPages; i++) {
            	int numPage = i + 1;
            	Object pageObj = drawPages.getByIndex(i);
            	XDrawPage xDrawPage = (XDrawPage)UnoRuntime.queryInterface(XDrawPage.class, pageObj);
            	XShapes xShapes = (XShapes)UnoRuntime.queryInterface( XShapes.class, xDrawPage );
            	int numShapes = xShapes.getCount();
            	for(int j=0; j<numShapes; j++) {
            		Object shapeObj = xShapes.getByIndex(j);
            		XShape xshape = (XShape)UnoRuntime.queryInterface(XShape.class, shapeObj);
            		XNamed xNamed = (XNamed)UnoRuntime.queryInterface(XNamed.class, shapeObj );
            		String name = xNamed.getName();
            		if(name.startsWith("spagobi_placeholder_")) {
            			String logicalObjectName = name.substring(20);
            			ConfiguredBIDocument confDoc = pampDao.getConfigureDocument(pathPamphlet, logicalObjectName);
            			storeDocImages(confDoc, numPage);
            		}
            	}
            }
            XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent);
            XCloseable xCloseable = (XCloseable)UnoRuntime.queryInterface(XCloseable.class, xModel);
            xCloseable.close(true);
            // delete the template temp document
            templateFile.delete();
		} catch (Exception e) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass().getName(),
					            "generateTemplateParts", "Error while generating images of the document", e);
		}
        
	}
	
	
	
	private void storeDocImages(ConfiguredBIDocument confDoc, int numPage) {
		try{
			// get id of the biobject
			Integer idObj = confDoc.getId();
			// get the map of configured parameter
			Map confPars = confDoc.getParameters();
			// load the biobject
			IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			BIObject biobj = biobjdao.loadBIObjectForDetail(idObj);
			// load the list of parameter of the biobject
			IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
			List params = biobjpardao.loadBIObjectParametersById(idObj);
			// for each parameter set the configured value
			Iterator iterParams = params.iterator();
			while(iterParams.hasNext()) {
				BIObjectParameter par = (BIObjectParameter)iterParams.next();
				String parUrlName = par.getParameterUrlName();
				String value = (String)confPars.get(parUrlName);
				if(value!=null) {
					List values = new ArrayList();
					values.add(value);
					par.setParameterValues(values);	
				}
			}
			// set the parameters into the biobject
			biobj.setBiObjectParameters(params);
			// get the engine of the bionject
			Engine eng = biobj.getEngine();
			// get driver class 
			String driverClassName = eng.getDriverName();
			// get the url of the engine
			String urlEngine = eng.getUrl();
			// build an instance of the driver
			IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
			// get the map of parameter to send to the engine
			Map mapPars = aEngineDriver.getParameterMap(biobj);
			// built the request to sent to the engine
			Iterator iterMapPar = mapPars.keySet().iterator();
			HttpClient client = new HttpClient();
		    PostMethod httppost = new PostMethod(urlEngine);
		    while(iterMapPar.hasNext()){
		    	String parurlname = (String)iterMapPar.next();
		    	String parvalue = (String)mapPars.get(parurlname);
		    	httppost.addParameter(parurlname, parvalue);
		    }
		    // sent request to the engine
		    int statusCode = client.executeMethod(httppost);
		    byte[] responseBody = httppost.getResponseBody();
		    httppost.releaseConnection();
		    // extract image from the response
		    String xmlRespStr = new String(responseBody);
		    SourceBean xmlRespSB = SourceBean.fromXMLString(xmlRespStr);
		    List images = xmlRespSB.getAttributeAsList("IMAGE");
		    SourceBean firstImageSB = (SourceBean)images.get(0);
		    String firstImgBase64 = firstImageSB.getCharacters();
		    BASE64Decoder decoder = new BASE64Decoder();
		    byte[] firstImg = decoder.decodeBuffer(firstImgBase64);
		    // store image into cms
		    IPamphletsCmsDao pampdao = new PamphletsCmsDaoImpl();
		    pampdao.storeTemplateImage(pathPamphlet, firstImg, confDoc.getLogicalName(), numPage);
		} catch (Exception e) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass().getName(),
		                        "storeDocImages", "Error while generating and storing " +
		                        "images of the document" + confDoc.getLogicalName(), e);
		}
	}
	

	
	
	private XComponent openTemplate(XComponentLoader xComponentLoader, String pathTempFile){
		XComponent xComponent = null;
		try{
			PropertyValue[] pPropValues = new PropertyValue[ 2 ];
			pPropValues[ 0 ] = new PropertyValue();
			pPropValues[ 0 ].Name = "Hidden";
			pPropValues[ 0 ].Value = new Boolean( true );
			pPropValues[ 1 ] = new PropertyValue();
			pPropValues[ 1 ].Name = "OpenNewView";
			pPropValues[ 1 ].Value = new Boolean( true );
	        String loadUrl = "private:factory/simpress";
	        xComponent = xComponentLoader.loadComponentFromURL(pathTempFile, "_blank", 0, pPropValues);
		} catch (Exception e) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass().getName(),
								"openTemplate", "Cannot open template document", e);
		}
		return xComponent;
	}
	
	
	
	public void generateFinalTemplate(){
		
	}
	
}
