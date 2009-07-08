/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.utility;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.httpchannel.upload.IUploadHandler;
import it.eng.spago.dispatching.service.DefaultRequestContext;
import it.eng.spago.init.InitializerIFace;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

// TODO: Auto-generated Javadoc
/**
 * The Class DataMartUploadManager.
 */
public class DataMartUploadManager extends DefaultRequestContext implements IUploadHandler, InitializerIFace {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
    
    /** The file path. */
    private String filePath = null;

 
    
	/* (non-Javadoc)
	 * @see it.eng.spago.init.InitializerIFace#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
		
		String qbeDataMartDir = FileUtils.getQbeDataMartDir(new File(it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath()));
		//String qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		
		File f = new File(qbeDataMartDir);
		
		if (!(f.exists())){
			f.mkdirs();
		}
		filePath = qbeDataMartDir;
	}

	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.httpchannel.upload.IUploadHandler#upload(org.apache.commons.fileupload.FileItem)
	 */
	public void upload(FileItem fileItem) throws Exception {
		//String fieldName = fileItem.getFieldName();
		//String fileName = filterName(fileItem.getName());

		String dmPath =(String) getServiceRequest().getAttribute("dmPath");
        
		
		String datamartDir = filePath + System.getProperty("file.separator") + dmPath + System.getProperty("file.separator");
		
		File dmDir = new File(datamartDir);
		
		if (!dmDir.exists()){
			dmDir.mkdirs();
		}
		String completeFileName = datamartDir  + "datamart.jar";
		
		
		File diskFile = new File(completeFileName);
		
		fileItem.write(diskFile);
	}

    /* (non-Javadoc)
     * @see it.eng.spago.init.InitializerIFace#getConfig()
     */
    public SourceBean getConfig() {
        return null;
    }
    
    /**
     * Filter name.
     * 
     * @param fileName the file name
     * 
     * @return the string
     */
    private String filterName(String fileName) {
        String result = fileName;
        int pos = fileName.lastIndexOf("/");
        if (pos != -1) {
            result = fileName.substring(pos + 1);
        } else {
            pos = fileName.lastIndexOf("\\");
            if (pos != -1) {
                result = fileName.substring(pos + 1);
            }
        }
        return result;
    }

}
