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
package it.eng.spagobi.pamphlets.toolagents;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.pamphlets.constants.PamphletsConstants;
import it.eng.spagobi.pamphlets.dao.IPamphletsCmsDao;
import it.eng.spagobi.pamphlets.dao.PamphletsCmsDaoImpl;
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

import org.enhydra.shark.api.internal.toolagent.AppParameter;

import com.sun.star.awt.Point;
import com.sun.star.awt.Size;
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

public class PamphletGeneratorFinalTA {

	
	public static void execute(AppParameter param1) {
		XComponent xComponent = null;
		XDesktop xdesktop =  null;
		String pathTmpFold = null;
		try{
			String pathPamphlet = param1.the_value.toString();
			
			debug("execute", "Path Pamphlet =" + pathPamphlet);
			
			ConfigSingleton config = ConfigSingleton.getInstance();
			SourceBean pathTmpFoldSB = (SourceBean)config.getAttribute("PAMPHLETS.PATH_TMP_FOLDER");
			pathTmpFold = (String)pathTmpFoldSB.getAttribute("path");
			
			debug("execute", "Path tmp =" + pathTmpFold);
			
			String pathTmpFoldPamp = pathTmpFold + pathPamphlet;
			
			debug("execute", "Path tmp folder pamphlet =" + pathTmpFoldPamp);
			
			File tempDir = new File(pathTmpFoldPamp); 
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
			
			
			//	gets the template file data
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
            debug("execute", "Path template = " + templateFile.getAbsolutePath());
            xComponent = openTemplate(xComponentLoader, "file:///" + templateFile.getAbsolutePath());	
            XMultiServiceFactory xServiceFactory = (XMultiServiceFactory)UnoRuntime.queryInterface(XMultiServiceFactory.class, xComponent);
            // get draw pages
            XDrawPagesSupplier xDrawPageSup = (XDrawPagesSupplier)UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
            XDrawPages drawPages = xDrawPageSup.getDrawPages();
            int numPages = drawPages.getCount();         
            for(int i=0; i<numPages; i++) {
            	// get images corresponding to that part of the template
            	String indexPartTemplate = new Integer(i+1).toString();
            	IPamphletsCmsDao pampdao = new PamphletsCmsDaoImpl();
            	Map images = pampdao.getImagesOfTemplatePart(pathPamphlet, indexPartTemplate);
            	// get draw page
            	Object pageObj = drawPages.getByIndex(i);
            	XDrawPage xDrawPage = (XDrawPage)UnoRuntime.queryInterface(XDrawPage.class, pageObj);
            	// get shapes of the page
            	XShapes xShapes = (XShapes)UnoRuntime.queryInterface( XShapes.class, xDrawPage );
            	int numShapes = xShapes.getCount();
            	// prepare list for shapes to remove and to add
            	List shapetoremove = new ArrayList();
            	List shapetoadd = new ArrayList();
                // check each shape
            	for(int j=0; j<numShapes; j++) {
            		Object shapeObj = xShapes.getByIndex(j);
            		XShape xshape = (XShape)UnoRuntime.queryInterface(XShape.class, shapeObj);
            		XNamed xNamed = (XNamed)UnoRuntime.queryInterface(XNamed.class, shapeObj );
            		String name = xNamed.getName();
            		// sobstitute the placeholder with the correspondent image
            		if(name.startsWith("spagobi_placeholder_")) {
            			String nameImg = name.substring(20);
            			Size size = xshape.getSize();
            			Point position = xshape.getPosition();     			
            			shapetoremove.add(xshape);
            			Object newShapeObj = xServiceFactory.createInstance("com.sun.star.drawing.GraphicObjectShape");
            			XShape xShapeNew = (XShape)UnoRuntime.queryInterface(XShape.class, newShapeObj);
            			xShapeNew.setPosition(position);
            			xShapeNew.setSize(size);
                    	// write the corresponding image into file system
            			String pathTmpImgFolder = pathTmpFoldPamp + "/tmpImgs/";
            			
            			debug("execute", "Path tmp images = " + pathTmpImgFolder);
            			
            			File fileTmpImgFolder = new File(pathTmpImgFolder);
            			fileTmpImgFolder.mkdirs();
            			String pathTmpImg = pathTmpImgFolder + nameImg + ".jpg";
            			File fileTmpImg = new File(pathTmpImg); 
            	        FileOutputStream fos = new FileOutputStream(fileTmpImg);
            	        byte[] content = (byte[])images.get(nameImg);
            	        fos.write(content);
            	        fos.flush();
            	        fos.close();
            			// load the image into document
            			XPropertySet xSPS = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, xShapeNew);
            			try {  
            				 String fileoopath = transformPathForOpenOffice(fileTmpImg);
            				 debug("execute", "Path image loaded into openoffice = " + fileoopath);
            				 xSPS.setPropertyValue("GraphicURL", "file:///" + fileoopath);            
            			} catch (Exception e) {                 
            				SpagoBITracer.major(PamphletsConstants.NAME_MODULE, PamphletGeneratorFinalTA.class.getName(),
    								            "execute", "error while adding graphic shape", e);
            			}
            			shapetoadd.add(xShapeNew);
            		}
            	}
            	// add and remove shape
            	Iterator iter = shapetoremove.iterator();
            	while(iter.hasNext()){
            		XShape shape = (XShape)iter.next();
            		xShapes.remove(shape);
            	}
            	iter = shapetoadd.iterator();
            	while(iter.hasNext()){
            		XShape shape = (XShape)iter.next();
            		xShapes.add(shape);
            	}
            	
            	// add notes
            	XPresentationPage xPresPage = (XPresentationPage)UnoRuntime.queryInterface(XPresentationPage.class, xDrawPage );
        		XDrawPage notesPage = xPresPage.getNotesPage();
        		XShapes xShapesNotes = (XShapes)UnoRuntime.queryInterface( XShapes.class, notesPage );
            	int numNoteShapes = xShapesNotes.getCount();
            	for(int indShap=0; indShap<numNoteShapes; indShap++) {
            		Object shapeNoteObj = xShapesNotes.getByIndex(indShap);
            		XShape xshapeNote = (XShape)UnoRuntime.queryInterface(XShape.class, shapeNoteObj);
            		String type = xshapeNote.getShapeType();
            		if(type.endsWith("NotesShape")) {
            			XText textNote = (XText)UnoRuntime.queryInterface(XText.class, shapeNoteObj);
            			byte[] notesByte = 	pampdao.getNotesTemplatePart(pathPamphlet, indexPartTemplate);
            			String notes = new String(notesByte);
            			textNote.setString(notes);
            		}
            	}
            	
            }  
            
            
            // save final document
            String namePamp = pampDao.getPamphletName(pathPamphlet);
            String pathFinalDoc = pathTmpFoldPamp + "/" + namePamp + ".ppt";
            
            debug("execute", "Path final document = " + pathFinalDoc);
            
            File fileFinalDoc = new File(pathFinalDoc);
            String fileoopath = transformPathForOpenOffice(fileFinalDoc);
            if(fileoopath.equals(""))
            	return;
            
            XStorable xStorable = (XStorable)UnoRuntime.queryInterface(XStorable.class, xComponent);
            PropertyValue[] documentProperties = new PropertyValue[2];
            documentProperties[0] = new PropertyValue();
            documentProperties[0].Name = "Overwrite";
            documentProperties[0].Value = new Boolean(true);
            try {
            	 debug("execute", "Path document stored = " + "file:///" + fileoopath);
                xStorable.storeAsURL("file:///" + fileoopath , documentProperties);
            } catch (IOException e) {
            	SpagoBITracer.major(PamphletsConstants.NAME_MODULE, PamphletGeneratorFinalTA.class.getName(),
						            "execute", "Error while storing the final document", e);
            }
            
            FileInputStream fis = new FileInputStream(pathFinalDoc);
            byte[] docCont = GeneralUtilities.getByteArrayFromInputStream(fis);
            pampDao.storeFinalDocument(pathPamphlet, docCont);
            fis.close();

		} catch(Exception e) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, PamphletGeneratorFinalTA.class.getName(),
								"execute", "Error during the generation of the final document", e);
		} finally {
			 // close open document and environment
			if(xComponent!=null)  {
				XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent);
				XCloseable xCloseable = (XCloseable)UnoRuntime.queryInterface(XCloseable.class, xModel);
				try{
					xCloseable.close(true);
				} catch (Exception e){
					SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, PamphletGeneratorFinalTA.class.getName(),
							            "execute", "Cannot close openoffice template document", e);
				}
			} 
			if(xdesktop!=null){
				xdesktop.terminate();
			}
		}
		
		 // delete all file inside tht patmphlet temp directory
		File fileTmpFold = new File(pathTmpFold);
	    GeneralUtilities.deleteContentDir(fileTmpFold);
		
	}
	
	
	
	
	private static XComponent openTemplate(XComponentLoader xComponentLoader, String pathTempFile){
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
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, PamphletGeneratorFinalTA.class.getName(),
								"openTemplate", "Cannot open template document", e);
		}
		return xComponent;
	}
	
	
	
	private static String transformPathForOpenOffice(File file){
		String path = "";
		try{
			path = file.getCanonicalPath();
			path = path.replace('\\', '/');
			String prefix = path.substring(0, 2);
			String afterPrefix = path.substring(2);
			String secondChar = path.substring(1,2);
			if(secondChar.equals(":")){
				path = prefix.toLowerCase() + afterPrefix;
			}
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, PamphletGeneratorFinalTA.class.getName(),
					            "transformPathForOpenOffice", "Error while transforming file path");
		}
		return path;
	}
	
	
	private static void debug(String method, String message) {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, PamphletGeneratorFinalTA.class.getName(), method, message);
	}

	
	
}
