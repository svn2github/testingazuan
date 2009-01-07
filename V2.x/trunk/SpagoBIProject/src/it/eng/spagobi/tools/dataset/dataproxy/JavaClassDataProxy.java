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
package it.eng.spagobi.tools.dataset.common.dataproxy;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.lov.bo.JavaClassDetail;
import it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JavaClassDataProxy implements IDataProxy {
	
	String className;
	IEngUserProfile userProfile;
	
	private static transient Logger logger = Logger.getLogger(JavaClassDataProxy.class);
	
	
	public JavaClassDataProxy() {
		super();
	}
	
	public JavaClassDataProxy(String className) {
		setClassName( className );
	}
	
	public Object load(String statement) throws EMFUserError {
		throw new UnsupportedOperationException("metothd load not yet implemented");
	}
	
	public Object load() throws EMFUserError {
		String result = null;				
		IJavaClassDataSet javaClass;
		JavaClassDetail javaClassDetail =new JavaClassDetail();
		try {
			javaClass = (IJavaClassDataSet) Class.forName( className ).newInstance();
			result = javaClass.getValues( getUserProfile() );
			result = result.trim();
    		boolean toconvert = javaClassDetail.checkSintax(result);
    		// check if the result must be converted into the right xml sintax
			if(toconvert) { 
				result = javaClassDetail.convertResult(result);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	    	
    	return result;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public IEngUserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(IEngUserProfile userProfile) {
		this.userProfile = userProfile;
	}

}
