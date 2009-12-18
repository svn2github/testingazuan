
package edu.yale.its.tp.cas.client;


import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.filter.CASFilter;


public class Util
{

    static private Logger logger= Logger.getLogger(Util.class);
    
    public Util()
    {
    }

    private static  String addParameters(HttpServletRequest request){
	logger.debug("IN");
	String param="?";
	Enumeration enumer = request.getParameterNames();
	String parName = null;
	String parValue = null;
	logger.debug("Reading request parameters...");
	while (enumer.hasMoreElements()) {
	    parName = (String) enumer.nextElement();
	    parValue = request.getParameter(parName);
	    if (!parName.equalsIgnoreCase("ticket")){
		param=param+parName+"="+parValue;
		if (enumer.hasMoreElements()) param=param+"&";
	    }
	    
	}
	logger.debug("OUT.param= "+param);
	return param;
    }
    
    public static String getService(HttpServletRequest request, String server)
        throws ServletException
    {
	String methodRequest=request.getMethod();
	System.out.println("methodRequest="+methodRequest);
	System.out.println("request.getRequestURI()="+request.getRequestURL());
            logger.debug("entering getService(" + request + ", " + server + ")");
        if(server == null)
        {
            logger.error("getService() argument \"server\" was illegally null.");
            throw new IllegalArgumentException("name of server is required");
        }
        StringBuffer sb = new StringBuffer();
        if(request.isSecure())
            sb.append("https://");
        else
            sb.append("http://");
        sb.append(server);
        sb.append(request.getRequestURI());
        if (methodRequest.equals("POST")) sb.append(addParameters(request));
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
            logger.debug("returning from getService() with encoded service [" + encodedService + "]");
        return encodedService;
    }


}