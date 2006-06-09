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
package it.eng.spagobi.services.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.List;

/**
 * @author Gioia
 *
 */
public class CheckLinksModule extends AbstractBasicCheckListModule{
	public CheckLinksModule() {
		super();
	}
	
	public void save() throws Exception {
		super.save();
		
		String subjectIdName = (String)((SourceBean) config.getAttribute("KEYS.SUBJECT")).getAttribute("key");
		String masterReportIdStr = (String)getAttribute(subjectIdName, _request);
		Integer masterReportId = new Integer(masterReportIdStr);		
		SourceBean checkedObjects = getCheckedObjects();
		List checkedObjectsList = checkedObjects.getAttributeAsList(OBJECT);
				
		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			subrptdao.eraseSubreportByMasterRptId(masterReportId);
			for(int i = 0; i < checkedObjectsList.size(); i++) {
				SourceBean subreport = (SourceBean)checkedObjectsList.get(i);
				String key = getObjectKey(subreport);
				Integer subReportId = new Integer(key);
				subrptdao.insertSubreport(new Subreport(masterReportId, subReportId));
			}
			
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","saveSubreportsConfiguration","Cannot erase/insert subreports from/into db", e);
		}
	}
}
