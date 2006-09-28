package it.eng.spagobi.booklets.automatictasks;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.booklets.bo.ConfiguredBIDocument;
import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.booklets.dao.IBookletsCmsDao;
import it.eng.spagobi.booklets.exceptions.OpenOfficeConnectionException;
import it.eng.spagobi.drivers.IEngineDriver;
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
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import sun.misc.BASE64Decoder;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.UnoUrlResolver;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPages;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XShapes;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;

public class ProcessOOTemplateAction implements ActionHandler {

	XDesktop xdesktop = null;
	XComponent xComponent = null;
	File templateOOFile = null;
	String pathTmpFold = null;
	
	public void execute(ExecutionContext context) throws Exception {
		
		try {
			// RECOVER CONFIGURATION PARAMETER
			debug("execute", "Start execution");
			ContextInstance contextInstance = context.getContextInstance();
			debug("execute", "Context Instance retrived " + contextInstance);
			String pathBookConf = (String)contextInstance.getVariable(BookletsConstants.PATH_BOOKLET_CONF);
			debug("execute", "Booklet path variable retrived " + pathBookConf);
			ConfigSingleton config = ConfigSingleton.getInstance();
			debug("execute", "Spago configuration retrived " + config);
			SourceBean pathTmpFoldSB = (SourceBean)config.getAttribute("BOOKLETS.PATH_TMP_FOLDER");
			if(pathTmpFoldSB==null) {
				major("execute", "Cannot found Booklet tmp folder sourcebean BOOKLETS.PATH_TMP_FOLDER");
				throw new Exception("Cannot found Booklet tmp folder sourcebean BOOKLETS.PATH_TMP_FOLDER");
			} else {
				debug("execute", "Booklet tmp folder sourcebean retrived " + pathTmpFoldSB);
			}
			pathTmpFold = (String)pathTmpFoldSB.getAttribute("path");
			if(pathTmpFold==null) {
				major("execute", "Cannot found path attribute into sourcebean BOOKLETS.PATH_TMP_FOLDER");
				throw new Exception("Cannot found path attribute into sourcebean BOOKLETS.PATH_TMP_FOLDER");
			} else {
				debug("execute", "Booklet base tmp folder retrived " + pathTmpFold);
			}
			String pathTmpFoldBook = pathTmpFold + pathBookConf;
			debug("execute", "Using tmp folder path " + pathTmpFoldBook);
			File tempDir = new File(pathTmpFoldBook); 
			tempDir.mkdirs();
			debug("execute", "Create tmp folders " + pathTmpFoldBook);
			
			
			
			// GETS OO TEMPLATE AND WRITE IT INTO THE TMP DIRECTORY
			IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
			String templateFileName = bookDao.getBookletTemplateFileName(pathBookConf);
			debug("execute", "booklet oo template name retrived " + templateFileName);
			InputStream contentTempIs = bookDao.getBookletTemplateContent(pathBookConf);
			debug("execute", "booklet oo template input stream retrived " + contentTempIs);
	        byte[] contentTempBytes = GeneralUtilities.getByteArrayFromInputStream(contentTempIs);
	        debug("execute", "booklet oo template bytes retrived ");
	        contentTempIs.close();
	        // write template content into a temp file
	        templateOOFile = new File(tempDir, templateFileName);
	        FileOutputStream fosTemplate = new FileOutputStream(templateOOFile);
	        fosTemplate.write(contentTempBytes);
	        debug("execute", "oo template bytes written into a tmp file ");
	        fosTemplate.flush();
	        fosTemplate.close();
			
			
			
			// INITIALIZE OFFICE ENVIRONMENT
			SourceBean officeConnectSB = (SourceBean)config.getAttribute("BOOKLETS.OFFICECONNECTION");
			if(officeConnectSB==null) {
				major("execute", "Cannot found sourcebean BOOKLETS.PATH_TMP_FOLDER into configuration");
				throw new Exception("Cannot found sourcebean BOOKLETS.PATH_TMP_FOLDER into configuration");
			} else {
				debug("execute", "Booklet office connection sourcebean retrived " + officeConnectSB);
			}
			String host = (String)officeConnectSB.getAttribute("host");
			String port = (String)officeConnectSB.getAttribute("port");
			debug("execute", "office connection, using host " + host + " and port " + port);
			XComponentContext xRemoteContext = Bootstrap.createInitialComponentContext(null);
			debug("execute", "initial XComponentContext created " + xRemoteContext);
			XUnoUrlResolver urlResolver = UnoUrlResolver.create(xRemoteContext);
			debug("execute", "XUnoUrlResolver retrived " + urlResolver);
			Object initialObject = null;
			try{
				initialObject = urlResolver.resolve("uno:socket,host="+host+",port="+port+";urp;StarOffice.ServiceManager");
			} catch (Exception e) {
				major("execute", "Cannot connect to open office using host " + host + " and port " + port);
				throw new OpenOfficeConnectionException("Cannot connect to open office using host " + host + " and port " + port);
			}
			debug("execute", "Office url resolved");
			XMultiComponentFactory xRemoteServiceManager = (XMultiComponentFactory)UnoRuntime.queryInterface(XMultiComponentFactory.class, initialObject);
			debug("execute", "XMultiComponentFactory retrived " + xRemoteServiceManager);
			XPropertySet xProperySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xRemoteServiceManager);
			debug("execute", "xProperySet retrived " + xProperySet);
			Object oDefaultContext = xProperySet.getPropertyValue("DefaultContext");
			debug("execute", "DefaultContext Propery  value retrived " + oDefaultContext);
			xRemoteContext = (XComponentContext)UnoRuntime.queryInterface(XComponentContext.class, oDefaultContext);
			debug("execute", "remote XComponentContext retrived " + xRemoteContext);
			Object desktop = xRemoteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemoteContext);
			debug("execute", "Desktop object retrived " + desktop);
			xdesktop = (XDesktop)UnoRuntime.queryInterface(XDesktop.class, desktop);
			debug("execute", "XDesktop object retrived " + desktop);
			
			
			
			// LOAD OO TEMPLATE INTO OPEN OFFICE
	        XComponentLoader xComponentLoader = (XComponentLoader)UnoRuntime.queryInterface(XComponentLoader.class, xdesktop);
	        debug("execute", "XComponentLoader object retrived " + xComponentLoader);
	        xComponent = openTemplate(xComponentLoader, "file:///" + templateOOFile.getAbsolutePath());
	        debug("execute", "Template loaded into openffice ");
		
	        
	        
	        
	        // ANALYZE OO TEMPLATE, EXTRACT NAME OF PLACEHOLDERS, CALL ENGINE AND STORE RESULT IMAGES
	        // gets the number of the parts of the documents
            XMultiServiceFactory xServiceFactory = (XMultiServiceFactory)UnoRuntime.queryInterface(XMultiServiceFactory.class, xComponent);
            debug("execute", "XMultiServiceFactory of the Template retrived " + xServiceFactory);
            XDrawPagesSupplier xDrawPageSup = (XDrawPagesSupplier)UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
            debug("execute", "XDrawPagesSupplier retrived " + xDrawPageSup);
            XDrawPages drawPages = xDrawPageSup.getDrawPages();
            debug("execute", "XDrawPages retrived " + drawPages);
            int numPages = drawPages.getCount();
            debug("execute", "Template has " + numPages + " pages");
            // built the structure into the cms 
            IBookletsCmsDao pampdao = new BookletsCmsDaoImpl();
            pampdao.createStructureForTemplate(pathBookConf, numPages);
            // store as current version the template (in case of flow exception there will be a file to download)
    		InputStream tempContIS = bookDao.getBookletTemplateContent(pathBookConf);
    		bookDao.storeCurrentPresentationContent(pathBookConf, tempContIS);
            debug("execute", "Template cms structure created");
            // for each part of the document gets the image images and stores them into cms
            for(int i=0; i<numPages; i++) {
            	int numPage = i + 1;
            	debug("execute", "processing page with index " + i);
            	Object pageObj = drawPages.getByIndex(i);
            	debug("execute", "page object retrived " + pageObj);
            	XDrawPage xDrawPage = (XDrawPage)UnoRuntime.queryInterface(XDrawPage.class, pageObj);
            	debug("execute", "XDrawPage retrived " + xDrawPage);
            	XShapes xShapes = (XShapes)UnoRuntime.queryInterface( XShapes.class, xDrawPage );
            	debug("execute", "xShapes of pages retrived " + xShapes);
            	int numShapes = xShapes.getCount();
            	debug("execute", "Page has " + numShapes + " shapes");
            	for(int j=0; j<numShapes; j++) {
            		debug("execute", "processing shape with index " + j);
            		Object shapeObj = xShapes.getByIndex(j);
            		debug("execute", "shape object retrived " + shapeObj);
            		//XShape xshape = (XShape)UnoRuntime.queryInterface(XShape.class, shapeObj);
            		//debug("execute", "XShape retrived " + xshape);
            		//XNamed xNamed = (XNamed)UnoRuntime.queryInterface(XNamed.class, shapeObj );
            		//debug("execute", "xNamed retrived " + xNamed);
            		//String name = xNamed.getName();
            		//debug("execute", "Name of the shape " + name);
            		XText xShapeText = (XText)UnoRuntime.queryInterface(XText.class, shapeObj);
            		debug("execute", "XShapeText retrived " + xShapeText);
            		if(xShapeText!=null) {
            			String shapeText = xShapeText.getString();
            			debug("execute", "shape text retrived " + shapeText);
            			shapeText = shapeText.trim();
            			if(shapeText.startsWith("spagobi_placeholder_")) {
                			String logicalObjectName = shapeText.substring(20);
                			debug("execute", "Logical Name of the shape " + logicalObjectName);
                			ConfiguredBIDocument confDoc = bookDao.getConfigureDocument(pathBookConf, logicalObjectName);
                			debug("execute", "Configured document with Logical Name " + logicalObjectName + " retrived " + confDoc);
                			storeDocImages(confDoc, numPage, pathBookConf); 
                		} 
            		}
            	}
            }
	        
	        
	        
	        
		} catch (Exception e) {
			major("execute", "Exception during execution : \n" + e);
			throw e;
		} finally {
			if(xComponent!=null){
				debug("execute", "Start close xComponent (template document)");
				XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent);
				debug("execute", "XModel interface retrived " + xModel);
	            XCloseable xCloseable = (XCloseable)UnoRuntime.queryInterface(XCloseable.class, xModel);
	            debug("execute", "XCloseable interface retrived " + xCloseable);
	            try{
					xCloseable.close(true);
					debug("execute", "xComponent (template document) closed");
				} catch (Exception e){
					major("execute", "Cannot close openoffice template document \n " + e);
				}
			}
			if(xdesktop!=null) {
				xdesktop.terminate();
				debug("execute", "xDesktop terminated");
			}
            // delete the template temp document
            if(templateOOFile!=null) {
            	templateOOFile.delete();
            	debug("execute", "oo template tmp file deleted");
            }
		}
		
		
		// DELETE ALL FILES INTO THE BOOKLET TMP DIRECTORY
		File fileTmpFold = new File(pathTmpFold);
	    GeneralUtilities.deleteContentDir(fileTmpFold);
	    debug("execute", "Files contained into the booklet tmp directory deleted");
		
	}

	
	
	
	
	private static void storeDocImages(ConfiguredBIDocument confDoc, int numPage, String pathConfBook) throws Exception {
		debug("storeDocImages", "start storeDocImages method");
		try{
			// get id of the biobject
			Integer idObj = confDoc.getId();
			debug("storeDocImages", "using configured document / biobject id " + idObj);
			// get the map of configured parameter
			Map confPars = confDoc.getParameters();
			debug("storeDocImages", "map conf doc parameters " + confPars);
			// load the biobject
			IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			BIObject biobj = biobjdao.loadBIObjectForDetail(idObj);
			debug("storeDocImages", "biobject loaded using id "+ idObj);
			// load the list of parameter of the biobject
			IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
			List params = biobjpardao.loadBIObjectParametersById(idObj);
			debug("storeDocImages", "biobject parameter list "+ params);
			// for each parameter set the configured value
			Iterator iterParams = params.iterator();
			while(iterParams.hasNext()) {
				BIObjectParameter par = (BIObjectParameter)iterParams.next();
				String parUrlName = par.getParameterUrlName();
				debug("storeDocImages", "processing biparameter with url name "+ parUrlName);
				String value = (String)confPars.get(parUrlName);
				debug("storeDocImages", "usign "+ value+" as value for the parameter");
				if(value!=null) {
					List values = new ArrayList();
					values.add(value);
					par.setParameterValues(values);	
					debug("storeDocImages", "parameter value set");
				}
			}
			// set the parameters into the biobject
			biobj.setBiObjectParameters(params);
			// get the engine of the bionject
			Engine eng = biobj.getEngine();
			debug("storeDocImages", "biobj engine retrived " + eng);
			// get driver class 
			String driverClassName = eng.getDriverName();
			debug("storeDocImages", "engine driver class " + driverClassName);
			// get the url of the engine
			String urlEngine = eng.getUrl();
			debug("storeDocImages", "url engine  " + urlEngine);
			// build an instance of the driver
			IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
			debug("storeDocImages", "engine driver instance created " + aEngineDriver);
			// get the map of parameter to send to the engine
			Map mapPars = aEngineDriver.getParameterMap(biobj, null, "");
			debug("storeDocImages", "parameter map returned by engine driver " + mapPars);
			// built the request to sent to the engine
			Iterator iterMapPar = mapPars.keySet().iterator();
			HttpClient client = new HttpClient();
		    PostMethod httppost = new PostMethod(urlEngine);
		    while(iterMapPar.hasNext()){
		    	String parurlname = (String)iterMapPar.next();
		    	String parvalue = (String)mapPars.get(parurlname);
		    	httppost.addParameter(parurlname, parvalue);
		    }
		    debug("storeDocImages", "post object created " + httppost);
		    // sent request to the engine
		    int statusCode = client.executeMethod(httppost);
		    debug("storeDocImages", "post request sent");
		    byte[] responseBody = httppost.getResponseBody();
		    debug("storeDocImages", "engine response received " + responseBody);
		    httppost.releaseConnection();
		    debug("storeDocImages", "http connection released");
		    // extract image from the response
		    String xmlRespStr = new String(responseBody);
		    SourceBean xmlRespSB = SourceBean.fromXMLString(xmlRespStr);
		    debug("storeDocImages", "response parsed into a sourcebean");
		    List images = xmlRespSB.getAttributeAsList("IMAGE");
		    debug("storeDocImages", "list of images sourcebean extracted from response" + images);
		    SourceBean firstImageSB = (SourceBean)images.get(0);
		    debug("storeDocImages", "first images sourcebean recovered");
		    String firstImgBase64 = firstImageSB.getCharacters();
		    debug("storeDocImages", "base 64 encoded image bytes retrived " + firstImgBase64);
		    BASE64Decoder decoder = new BASE64Decoder();
		    byte[] firstImg = decoder.decodeBuffer(firstImgBase64);
		    debug("storeDocImages", "image bytes decoded " + firstImg);
		    // store image into cms
		    IBookletsCmsDao bookdao = new BookletsCmsDaoImpl();
		    bookdao.storeTemplateImage(pathConfBook, firstImg, confDoc.getLogicalName(), numPage);
		    debug("storeDocImages", "image stored into cms");
		} catch (Exception e) {
			major("StoreDocImages", "Error while generating and storing " +
				  "images of the document" + confDoc.getLogicalName() + " \n " + e);
			throw e;
		}
	}
	
	
	
	
	
	
	private static XComponent openTemplate(XComponentLoader xComponentLoader, String pathTempFile){
		debug("openTemplate", "start open template method");
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
	        debug("openTemplate", "template document loaded " + xComponent);
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, ProcessOOTemplateAction.class.getName(),
								"openTemplate", "Cannot open oo template document", e);
		}
		debug("openTemplate", "end open template method");
		return xComponent;
	}
	
	
	
	
	private static void debug(String method, String message){
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, ProcessOOTemplateAction.class.getName(), 
	            			method, message);
	}
	
	private static void major(String method, String message){
		SpagoBITracer.major(BookletsConstants.NAME_MODULE, ProcessOOTemplateAction.class.getName(), 
	            			method, message);
	}
	
}
