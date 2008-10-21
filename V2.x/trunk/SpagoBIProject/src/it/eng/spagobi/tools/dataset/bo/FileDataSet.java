/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.dataset.bo;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.engines.chart.bo.charttypes.dialcharts.DialCharts;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 *  Giulio Gavardi
 *         giulio.gavardi@eng.it
 
*         
 */
public class FileDataSet extends DataSet {
    private String fileName=null;
	private static transient Logger logger=Logger.getLogger(FileDataSet.class);
    
    /**
     * Instantiates a new file data set.
     */
    public FileDataSet(){
    	super();
    	this.fileName="";
    }
    
    /**
     * Instantiates a new file data set.
     * 
     * @param a the a
     */
    public FileDataSet(DataSet a) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
    	this.fileName = "";
	}
    
    public FileDataSet(DataSet a, String fileName) {
    	setDsId(a.getDsId());
    	setLabel(a.getLabel());
    	setName(a.getName());
    	setDescription(a.getDescription());
    	this.fileName = fileName;
	}


	/**
	 * Gets the file name.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     * 
     * @param fileName the new file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	/* (non-Javadoc)
	 * @see it.eng.spagobi.tools.dataset.bo.DataSet#getDataSetResult(it.eng.spago.security.IEngUserProfile)
	 */
	public String getDataSetResult(IEngUserProfile profile) throws Exception {
		String pathFile=getFileName();
		String result="";
		FileInputStream fis=new FileInputStream(pathFile);

		InputSource inputSource=new InputSource(fis);
		SourceBean rowsSourceBean=null;
		try {
			rowsSourceBean=SourceBean.fromXMLStream(inputSource);
			result=rowsSourceBean.toXML();
		}

		catch (Exception e) {
				logger.error("Error in retreaving data");
		}
		finally{
			if(fis!=null)fis.close();
		}
		return result;	
		
		
		
		
	}

    


}
