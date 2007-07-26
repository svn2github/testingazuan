/**
Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
*/
package it.eng.spagobi.engines.bo;

import com.bo.wibean.WIDocuments;

public class Constants {
//	 The path to the report panel applet
	public static final String C_STR_PATH_TO_APPLET = "../boclasses";

	// The path to the DHTML report panel
	public static final String C_STR_PATH_TO_DHTML = "../querywizard";
	// The path to the servlet the report panel applet talks to
	public static final String C_STR_PATH_TO_SERVLET = "/servlet/CadenzaServlet";

	// The path to 3-tier BusinessObjects.
	// final String C_STR_PATH_TO_3TIER = "../distribution/";

	// The name of the JSP pages corresponding to the main actions
	public static final String C_STR_DOC_LIST_PAGE = "docList.jsp";
	public static final String C_STR_UNIV_LIST_PAGE = "universeList.jsp";
	public static final String C_STR_VIEW_FC = "viewFCDoc_frameset.jsp";
	public static final String C_STR_VIEW_TC = "viewTCDoc_frameset.jsp";
	public static final String C_STR_DP_TC = "viewTCDoc_DP.jsp";
	public static final String C_STR_MIGRATE_TC = "migrateTCDoc.jsp";
	public static final String C_STR_UPLOAD_FORM = "uploadForm.jsp";
	public static final String C_STR_CREATE_TC_WITH_APPLET = "createTCDoc_Applet.jsp";
	public static final String C_STR_CREATE_TC_WITH_DHTML = "createTCDoc_DHTML.jsp";
	public static final String C_STR_CREATE_FC_WITH_ZABO = "createFCDoc_ZABO.jsp";

	// The different session attributes, used for displaying user messages
	public static final String C_STR_INFORMATION_SESSION_ATTRIBUTE = "informationMessage";
	public static final String C_STR_SUCCESS_SESSION_ATTRIBUTE = "successMessage";
	public static final String C_STR_FAILURE_SESSION_ATTRIBUTE = "failureMessage";

	// The different values of the page parameter, used for viewing thin client documents
	public static final String C_STR_PAGE_FIRST = "first";
	public static final String C_STR_PAGE_PREVIOUS = "prev";
	public static final String C_STR_PAGE_NEXT = "next";
	public static final String C_STR_PAGE_LAST = "last";

	// The name of the main JSP page of this tutorial
	public static final String C_STR_MAIN_PAGE = C_STR_DOC_LIST_PAGE;

	// The filter on the document list
	public static final int C_INT_DOC_FILTER = WIDocuments.WID_FILTER;
	public static final String C_STR_DOC_FILTER = "(only *.WID)";

	// The title given to this tutorial
	public static final String C_STR_SAMPLE_TITLE = "Drilling in a WebIntelligence report";

	// The display of the particular values of a list
	public static final String C_STR_ALL = "*";
	public static final String C_STR_NOTHING = "Nothing";
	
	
// *****************************************************************************


	
	
}