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
package it.eng.qbe.locale;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.message.MessageBundle;


// TODO: Auto-generated Javadoc
/**
 * The Class QbeWebMessageHelper.
 * 
 * @author Andrea Zoppello
 * 
 * The implementation of IQbeMessageHelper used when QBE is used as a STANDALONE Web Application
 */
public class QbeWebMessageHelper implements
		IQbeMessageHelper {

	/* (non-Javadoc)
	 * @see it.eng.qbe.locale.IQbeMessageHelper#getMessage(it.eng.spago.base.RequestContainer, java.lang.String)
	 */
	public String getMessage(RequestContainer aRequestContainer, String code){
		return MessageBundle.getMessage(code);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.locale.IQbeMessageHelper#getMessage(it.eng.spago.base.RequestContainer, java.lang.String, java.lang.String)
	 */
	public String getMessage(RequestContainer aRequestContainer, String code, String bundle){
		SessionContainer session = aRequestContainer.getSessionContainer();
		/*
		SpagoBIInfo spagoBIInfo = (SpagoBIInfo)session.getAttribute("spagobi");
		if(spagoBIInfo != null && spagoBIInfo.getLoacale() != null) {
			return MessageBundle.getMessage(code, bundle, spagoBIInfo.getLoacale());
		}
		*/
		return MessageBundle.getMessage(code, bundle);
	}

}
