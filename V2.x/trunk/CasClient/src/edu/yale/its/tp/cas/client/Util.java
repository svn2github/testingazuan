
package edu.yale.its.tp.cas.client;

import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.filter.CASFilter;


public class Util
{

    public Util()
    {
    }

    private static  String addParameters(HttpServletRequest request){
	log.debug("IN");
	
	String param="?";
	Enumeration enumer = request.getParameterNames();
	String parName = null;
	String parValue = null;
	log.debug("Reading request parameters...");
	while (enumer.hasMoreElements()) {
	    parName = (String) enumer.nextElement();
	    parValue = request.getParameter(parName);
	    if (!parName.equalsIgnoreCase("ticket")){
		param=param+parName+"="+parValue;
		if (enumer.hasMoreElements()) param=param+"&";
	    }
	    
	}
	log.debug("OUT.param= "+param);
	return param;
    }
    
    public static String getService(HttpServletRequest request, String server)
        throws ServletException
    {

            log.debug("entering getService(" + request + ", " + server + ")");
        if(server == null)
        {
            log.error("getService() argument \"server\" was illegally null.");
            throw new IllegalArgumentException("name of server is required");
        }
        StringBuffer sb = new StringBuffer();
        if(request.isSecure())
            sb.append("https://");
        else
            sb.append("http://");
        sb.append(server);
        sb.append(request.getRequestURI());
        sb.append(addParameters(request));
        if(request.getQueryString() != null)
        {
            int ticketLoc = request.getQueryString().indexOf("ticket=");
            if(ticketLoc == -1)
                sb.append("?" + request.getQueryString());
            else
            if(ticketLoc > 0)
            {
                ticketLoc = request.getQueryString().indexOf("&ticket=");
                if(ticketLoc == -1)
                    sb.append("?" + request.getQueryString());
                else
                if(ticketLoc > 0)
                    sb.append("?" + request.getQueryString().substring(0, ticketLoc));
            }
        }
        String encodedService = URLEncoder.encode(sb.toString());
            log.debug("returning from getService() with encoded service [" + encodedService + "]");
        return encodedService;
    }

    private static Logger log = Logger.getLogger(Util.class);
}