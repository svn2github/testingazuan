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
package it.eng.spagobi.booklets.automatictasks;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.bo.dao.audit.AuditManager;
import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.booklets.dao.IBookletsCmsDao;
import it.eng.spagobi.booklets.exceptions.OpenOfficeConnectionException;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.sun.star.awt.Point;
import com.sun.star.awt.Size;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.UnoUrlResolver;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.container.XNameContainer;
import com.sun.star.container.XNamed;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.drawing.XDrawPages;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XShape;
import com.sun.star.drawing.XShapes;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.presentation.XPresentationPage;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;

public class GenerateFinalDocumentAction implements ActionHandler {

	public void execute(ExecutionContext context) throws Exception {
		XComponent xComponent = null;
		XDesktop xdesktop =  null;
		String pathTmpFold = null;
		IBookletsCmsDao pampDao = null;
		String pathTmpFoldPamp = null;
		String pathBookConf = null;
		XBridge bridge = null;
		ContextInstance contextInstance = null;
		try{
			debug("execute", "Start execution");
			contextInstance = context.getContextInstance();
			debug("execute", "Context Instance retrived " + contextInstance);
			pathBookConf = (String)contextInstance.getVariable(BookletsConstants.PATH_BOOKLET_CONF);
			debug("execute", "Booklet path variable retrived " + pathBookConf);
			ConfigSingleton config = ConfigSingleton.getInstance();
			SourceBean pathTmpFoldSB = (SourceBean)config.getAttribute("BOOKLETS.PATH_TMP_FOLDER");
			pathTmpFold = (String)pathTmpFoldSB.getAttribute("path");
			debug("execute", "Path tmp =" + pathTmpFold);
			if (pathTmpFold.startsWith("/") || pathTmpFold.charAt(1) == ':') {
				pathTmpFoldPamp = pathTmpFold + pathBookConf;
			} else {
				String root = ConfigSingleton.getRootPath();
				pathTmpFoldPamp = root + "/" + pathTmpFold + pathBookConf;
			}
			debug("execute", "Path tmp folder pamphlet =" + pathTmpFoldPamp);
			File tempDir = new File(pathTmpFoldPamp); 
			tempDir.mkdirs();
			debug("execute", "Path tmp folder pamphlet =" + pathTmpFoldPamp + " created.");
			
			
			// gets the template file data
			pampDao = new BookletsCmsDaoImpl();
			debug("execute", "Booklets CMS Dao instantiated");
			String templateFileName = pampDao.getBookletTemplateFileName(pathBookConf);
			debug("execute", "Template file name: " + templateFileName);
			InputStream contentTempIs = pampDao.getBookletTemplateContent(pathBookConf);
			debug("execute", "InputStream opened on booklet template.");
	        byte[] contentTempBytes = GeneralUtilities.getByteArrayFromInputStream(contentTempIs);
	        debug("execute", "BookletTemplateContent stored into a byte array.");
	        contentTempIs.close();
	        // write template content into a temp file
	        File templateFile = new File(tempDir, templateFileName);
	        FileOutputStream fosTemplate = new FileOutputStream(templateFile);
	        fosTemplate.write(contentTempBytes);
	        fosTemplate.flush();
	        fosTemplate.close();
	        debug("execute", "Booklet template content written into a temp file.");
			
			
			// initialize openoffice environment
			SourceBean officeConnectSB = (SourceBean) config.getAttribute("BOOKLETS.OFFICECONNECTION");
			debug("execute", "Office connection Sourcebean retrieved: " + officeConnectSB.toXML());
			String host = (String) officeConnectSB.getAttribute("host");
			String port = (String) officeConnectSB.getAttribute("port");
			debug("execute", "Office connection host: " + host);
			debug("execute", "Office connection port: " + port);
			XComponentContext  xRemoteContext = Bootstrap.createInitialComponentContext(null);
			debug("execute", "InitialComponentContext xRemoteContext created: " + xRemoteContext);
			
//			XUnoUrlResolver urlResolver = UnoUrlResolver.create(xRemoteContext);   
//			debug("execute", "XUnoUrlResolver created: " + urlResolver);
//			Object initialObject = urlResolver.resolve("uno:socket,host="+host+",port="+port+";urp;StarOffice.ServiceManager");
//			debug("execute", "InitialObject: " + initialObject);
			
			Object x = xRemoteContext.getServiceManager().createInstanceWithContext("com.sun.star.connection.Connector", xRemoteContext);
            XConnector xConnector = (XConnector) UnoRuntime.queryInterface(XConnector.class, x);
            debug("execute", "XConnector retrieved: " + xConnector);
            XConnection connection = xConnector.connect("socket,host=" + host + ",port=" + port);
            debug("execute", "XConnection retrieved: " + connection);
            x = xRemoteContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", xRemoteContext);
            XBridgeFactory xBridgeFactory = (XBridgeFactory) UnoRuntime.queryInterface(XBridgeFactory.class, x);
            debug("execute", "XBridgeFactory retrieved: " + xBridgeFactory);
            // this is the bridge that you will dispose
            bridge = xBridgeFactory.createBridge("", "urp", connection, null);
            debug("execute", "XBridge retrieved: " + bridge);
            XComponent xComp = (XComponent) UnoRuntime.queryInterface(XComponent.class, bridge);
            // get the remote instance
            x = bridge.getInstance("StarOffice.ServiceManager");
            debug("execute", "StarOffice.ServiceManager instance retrieved: " + x);
            // Query the initial object for its main factory interface
            XMultiComponentFactory xRemoteServiceManager = (XMultiComponentFactory) UnoRuntime.queryInterface(XMultiComponentFactory.class, x);
			
			//XMultiComponentFactory xRemoteServiceManager = (XMultiComponentFactory)UnoRuntime.queryInterface(XMultiComponentFactory.class, initialObject);
			debug("execute", "xRemoteServiceManager: " + xRemoteServiceManager);
			XPropertySet xPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xRemoteServiceManager);
			debug("execute", "XPropertySet: " + xPropertySet);
			Object oDefaultContext = xPropertySet.getPropertyValue("DefaultContext");
			debug("execute", "DefaultContext: " + oDefaultContext);
			xRemoteContext = (XComponentContext)UnoRuntime.queryInterface(XComponentContext.class, oDefaultContext);
			debug("execute", "xRemoteContext: " + xRemoteContext);
			Object desktop = xRemoteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemoteContext);
			debug("execute", "Desktop object instance created: " + desktop);
			xdesktop = (XDesktop)UnoRuntime.queryInterface(XDesktop.class, desktop);
			debug("execute", "XDesktop object: " + xdesktop);
			

            XComponentLoader xComponentLoader = (XComponentLoader)UnoRuntime.queryInterface(XComponentLoader.class, xdesktop);
	        // load the template into openoffice
            debug("execute", "Path template = " + templateFile.getAbsolutePath());
            xComponent = openTemplate(xComponentLoader, "file:///" + templateFile.getAbsolutePath());	
            debug("execute", "Template opened: " + xComponent);
            XMultiServiceFactory xServiceFactory = (XMultiServiceFactory)UnoRuntime.queryInterface(XMultiServiceFactory.class, xComponent);
            debug("execute", "xServiceFactory: " + xServiceFactory);
            // get draw pages
            XDrawPagesSupplier xDrawPageSup = (XDrawPagesSupplier)UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
            XDrawPages drawPages = xDrawPageSup.getDrawPages();
            debug("execute", "Draw pages found: " + drawPages);
            int numPages = drawPages.getCount();
            debug("execute", "Draw pages number: " + numPages);
            for (int i=0; i<numPages; i++) {
            	debug("execute", "Start examining page " + i);
            	// get images corresponding to that part of the template
            	String indexPartTemplate = new Integer(i+1).toString();
            	IBookletsCmsDao pampdao = new BookletsCmsDaoImpl();
            	Map images = pampdao.getImagesOfTemplatePart(pathBookConf, indexPartTemplate);
            	debug("execute", "Images map retrieved: " + images);
            	// get draw page
            	Object pageObj = drawPages.getByIndex(i);
            	XDrawPage xDrawPage = (XDrawPage)UnoRuntime.queryInterface(XDrawPage.class, pageObj);
            	debug("execute", "Draw page: " + xDrawPage);
            	// get shapes of the page
            	XShapes xShapes = (XShapes)UnoRuntime.queryInterface( XShapes.class, xDrawPage );
            	debug("execute", "Shapes found: " + xShapes);
            	int numShapes = xShapes.getCount();
            	debug("execute", "Shapes number: " + numShapes);
            	// prepare list for shapes to remove and to add
            	List shapetoremove = new ArrayList();
            	List shapetoadd = new ArrayList();
            	Object oBitmapsObj = xServiceFactory.createInstance( "com.sun.star.drawing.BitmapTable" );
            	XNameContainer oBitmaps = (XNameContainer)UnoRuntime.queryInterface(XNameContainer.class, oBitmapsObj);
                // check each shape
            	for(int j=0; j<numShapes; j++) {
            		debug("execute", "Start examining shape " + j + " of page " + i);
            		Object shapeObj = xShapes.getByIndex(j);
            		XShape xshape = (XShape)UnoRuntime.queryInterface(XShape.class, shapeObj);
            		debug("execute", "xshape: " + xshape);
            		//XNamed xNamed = (XNamed)UnoRuntime.queryInterface(XNamed.class, shapeObj);
            		//debug("execute", "xNamed: " + xNamed);
            		//String name = xNamed.getName();
            		//debug("execute", "Name of the shape: " + name);
            		XText xShapeText = (XText)UnoRuntime.queryInterface(XText.class, shapeObj);
            		debug("execute", "XShapeText retrived " + xShapeText);
            		if(xShapeText==null) {
            			continue;
            		}
            		String shapeText = xShapeText.getString();
            		debug("execute", "shape text retrived " + shapeText);
            		shapeText = shapeText.trim();
            		// sobstitute the placeholder with the correspondent image
            		if(shapeText.startsWith("spagobi_placeholder_")) {
            			String nameImg = shapeText.substring(20);
            			debug("execute", "Name of the image corresponding to the placeholder: " + nameImg);
            			Size size = xshape.getSize();
            			Point position = xshape.getPosition();
            			debug("execute", "Stored shape size and position on local variables");
            			shapetoremove.add(xshape);
            			debug("execute", "Shape loaded on shapes to be removed");
            			Object newShapeObj = xServiceFactory.createInstance("com.sun.star.drawing.GraphicObjectShape");
            			debug("execute", "New shape object instantiated: " + newShapeObj);
            			XShape xShapeNew = (XShape)UnoRuntime.queryInterface(XShape.class, newShapeObj);
            			debug("execute", "New XShape instantiated from the new shape object: " + xShapeNew);
            			xShapeNew.setPosition(position);
            			xShapeNew.setSize(size);
            			debug("execute", "Stored size and position set for the new XShape");
                    	// write the corresponding image into file system
            			String pathTmpImgFolder = pathTmpFoldPamp + "/tmpImgs/";
            			debug("execute", "Path tmp images: " + pathTmpImgFolder);
            			File fileTmpImgFolder = new File(pathTmpImgFolder);
            			fileTmpImgFolder.mkdirs();
            			debug("execute", "Folder tmp images: " + pathTmpImgFolder + " created.");
            			String pathTmpImg = pathTmpImgFolder + nameImg + ".jpg";
            			debug("execute", "Path tmp image file: " + pathTmpImg);
            			File fileTmpImg = new File(pathTmpImg); 
            	        FileOutputStream fos = new FileOutputStream(fileTmpImg);
            	        byte[] content = (byte[])images.get(nameImg);
            	        if (content == null) 
            	        	debug("execute", "Image with name \"" + nameImg + "\" was NOT found!!!");
            	        else debug("execute", "Image with name \"" + nameImg + "\" was found");
            	        fos.write(content);
            	        fos.flush();
            	        fos.close();
            	        debug("execute", "Tmp image file written");
            			// load the image into document
            			XPropertySet xSPS = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, xShapeNew);
            			try {  
            				 String fileoopath = transformPathForOpenOffice(fileTmpImg);
            				 debug("execute", "Path image loaded into openoffice = " + fileoopath);
            				 String externalGraphicUrl = "file:///" + fileoopath;
            				 if (!oBitmaps.hasByName(nameImg)) {
            					 debug("execute", "Bitmap table does not contain an element with name '" + nameImg + "'.");
            					 oBitmaps.insertByName(nameImg, externalGraphicUrl);
            				 } else {
            					 debug("execute", "Bitmap table already contains an element with name '" + nameImg + "'.");
            				 }
            				 Object internalGraphicUrl = oBitmaps.getByName(nameImg);
            				 debug("execute", "Retrieved internal url for image '" + nameImg + "': " + internalGraphicUrl);
            				 xSPS.setPropertyValue("GraphicURL", internalGraphicUrl);            
            			} catch (Exception e) {                 
            				major("execute", "error while adding graphic shape", e);
            			}
            			shapetoadd.add(xShapeNew);
            			debug("execute", "New shape loaded on shapes to be added");
            		}
            	}
            	// add and remove shape
            	Iterator iter = shapetoremove.iterator();
            	while(iter.hasNext()){
            		XShape shape = (XShape)iter.next();
            		xShapes.remove(shape);
            	}
            	debug("execute", "Removed shapes to be removed from document");
            	iter = shapetoadd.iterator();
            	while(iter.hasNext()){
            		XShape shape = (XShape)iter.next();
            		xShapes.add(shape);
            	}
            	debug("execute", "Added shapes to be added to the document");
            	// add notes
            	XPresentationPage xPresPage = (XPresentationPage)UnoRuntime.queryInterface(XPresentationPage.class, xDrawPage );
        		XDrawPage notesPage = xPresPage.getNotesPage();
        		debug("execute", "Notes page retrieved: " + notesPage);
        		XShapes xShapesNotes = (XShapes)UnoRuntime.queryInterface( XShapes.class, notesPage );
        		debug("execute", "Shape notes retrieved: " + xShapesNotes);
        		int numNoteShapes = xShapesNotes.getCount();
        		debug("execute", "Number of shape notes: " + numNoteShapes);
            	for (int indShap=0; indShap<numNoteShapes; indShap++) {
            		debug("execute", "Start examining shape note number " + indShap + " of page " + i);
            		Object shapeNoteObj = xShapesNotes.getByIndex(indShap);
            		XShape xshapeNote = (XShape)UnoRuntime.queryInterface(XShape.class, shapeNoteObj);
            		debug("execute", "xshapeNote: " + xshapeNote);
            		String type = xshapeNote.getShapeType();
            		debug("execute", "Shape type: " + type);
            		if(type.endsWith("NotesShape")) {
            			XText textNote = (XText)UnoRuntime.queryInterface(XText.class, shapeNoteObj);
            			debug("execute", "XText: " + textNote);
            			byte[] notesByte = pampdao.getNotesTemplatePart(pathBookConf, indexPartTemplate);
            			if (notesByte == null) debug("execute", "Notes bytes array is null!!!!");
            			else debug("execute", "Notes bytes array retrieved");
            			String notes = new String(notesByte);
            			textNote.setString(notes);
            			debug("execute", "Notes applied to the XText");
            		}
            	}
            }  
            
            // save final document
            String namePamp = pampDao.getBookletTemplateFileName(pathBookConf);
            String pathFinalDoc = pathTmpFoldPamp + "/" + namePamp + ".ppt";
            debug("execute", "Path final document = " + pathFinalDoc);
            File fileFinalDoc = new File(pathFinalDoc);
            String fileoopath = transformPathForOpenOffice(fileFinalDoc);
            debug("execute", "Open Office path: " + fileoopath);
            if(fileoopath.equals(""))
            	return;
            XStorable xStorable = (XStorable)UnoRuntime.queryInterface(XStorable.class, xComponent);
            debug("execute", "XStorable: " + xStorable);
            PropertyValue[] documentProperties = new PropertyValue[2];
            documentProperties[0] = new PropertyValue();
            documentProperties[0].Name = "Overwrite";
            documentProperties[0].Value = new Boolean(true);
            documentProperties[1] = new PropertyValue();
            documentProperties[1].Name = "FilterName";
            documentProperties[1].Value = "MS PowerPoint 97";
            try {
            	debug("execute", "Try to store document with path = " + "file:///" + fileoopath);
                xStorable.storeAsURL("file:///" + fileoopath , documentProperties);
                debug("execute", "Document stored with path = " + "file:///" + fileoopath);
            } catch (IOException e) {
            	major("execute", "Error while storing the final document", e);
            }
            FileInputStream fis = new FileInputStream(pathFinalDoc);
            byte[] docCont = GeneralUtilities.getByteArrayFromInputStream(fis);
            pampDao.storeCurrentPresentationContent(pathBookConf, docCont);
            debug("execute", "Document stored in CMS");
            fis.close();
            
		} catch(Exception e) {
			major("execute", "Error during the generation of the final document", e);
			// AUDIT UPDATE
			if (contextInstance != null) {
				Integer auditId = (Integer) contextInstance.getVariable(AuditManager.AUDIT_ID);
				AuditManager auditManager = AuditManager.getInstance();
				auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", e.getMessage(), null);
			}
			// store as final document the template  

		} finally {
			
			// close open document and environment
			if(xComponent!=null)  {
				XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent);
				XCloseable xCloseable = (XCloseable)UnoRuntime.queryInterface(XCloseable.class, xModel);
				try{
					xCloseable.close(true);
				} catch (Exception e){
					major("execute", "Cannot close openoffice template document", e);
				}
			}
			
			// the following code make the OO server go down!!
//			if(xdesktop!=null){
//				xdesktop.terminate();
//			}
		}
		
		 // delete all file inside tht patmphlet temp directory
		File fileTmpFold = new File(pathTmpFold);
	    GeneralUtilities.deleteContentDir(fileTmpFold);
	    debug("execute", "Deleted all files inside folder " + fileTmpFold);

	}
	
	private static XComponent openTemplate(XComponentLoader xComponentLoader, String pathTempFile){
		XComponent xComponent = null;
		try {
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
		    major("openTemplate", "Cannot open template document", e);
		}
		return xComponent;
	}
	
	private static String transformPathForOpenOffice(File file){
		String path = "";
	    try {
	    	path = file.getCanonicalPath();
	        path = path.replace('\\', '/');
	        String prefix = path.substring(0, 2);
	        String afterPrefix = path.substring(2);
	        String secondChar = path.substring(1,2);
	        if(secondChar.equals(":")){
	        	path = prefix.toLowerCase() + afterPrefix;
	        }
	    } catch (Exception e) {
		    major("transformPathForOpenOffice", "Error while transforming file path", e);
	    }
	    return path;
	}

	private static void debug(String method, String message){
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, GenerateFinalDocumentAction.class.getName(), 
	            			method, message);
	}
	
	private static void major(String method, String message, Exception e){
		SpagoBITracer.major(BookletsConstants.NAME_MODULE, GenerateFinalDocumentAction.class.getName(), 
	            			method, message, e);
	}

}
