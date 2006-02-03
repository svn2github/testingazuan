package it.eng.spagobi.util;

import javax.servlet.http.HttpSession;

public class SessionObjectRemoval {

	public static void removeSessionObjects(HttpSession session) {

		// remove following objects from session.
		if (session.getAttribute("query01") != null) {
			session.removeAttribute("query01");
		}

		if (session.getAttribute("table01") != null) {
			session.removeAttribute("table01");
		}

		if (session.getAttribute("minichartform01") != null) {
			session.removeAttribute("minichartform01");
		}

		if (session.getAttribute("mdxedit01") != null) {
			session.removeAttribute("mdxedit01");
		}

		if (session.getAttribute("navi01") != null) {
			session.removeAttribute("navi01");
		}

		if (session.getAttribute("print01") != null) {
			session.removeAttribute("print01");
		}

		if (session.getAttribute("sortform01") != null) {
			session.removeAttribute("sortform01");
		}

		if (session.getAttribute("printform01") != null) {
			session.removeAttribute("printform01");
		}

		if (session.getAttribute("chartform01") != null) {
			session.removeAttribute("chartform01");
		}

		if (session.getAttribute("query01.drillthroughtable") != null) {
			session.removeAttribute("query01.drillthroughtable");
		}
		
		if (session.getAttribute("chart01") != null) {
			session.removeAttribute("chart01");
		}
		
		if (session.getAttribute("analysisBean") != null) {
			session.removeAttribute("analysisBean");
		}
		
		if (session.getAttribute("save01") != null) {
			session.removeAttribute("save01");
		}

	}
	
}
