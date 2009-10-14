package edu.yale.its.tp.cas.client.filter;

import edu.yale.its.tp.cas.client.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;



public class CASFilter
    implements Filter
{

    private static Logger log = Logger.getLogger(CASFilter.class);
    
    public CASFilter()
    {
        casGateway = false;
        authorizedProxies = new ArrayList();
    }

    public void init(FilterConfig config)
        throws ServletException
    {
        casLogin = config.getInitParameter("edu.yale.its.tp.cas.client.filter.loginUrl");
        casValidate = config.getInitParameter("edu.yale.its.tp.cas.client.filter.validateUrl");
        casServiceUrl = config.getInitParameter("edu.yale.its.tp.cas.client.filter.serviceUrl");
        String casAuthorizedProxy = config.getInitParameter("edu.yale.its.tp.cas.client.filter.authorizedProxy");
        casRenew = Boolean.valueOf(config.getInitParameter("edu.yale.its.tp.cas.client.filter.renew")).booleanValue();
        casServerName = config.getInitParameter("edu.yale.its.tp.cas.client.filter.serverName");
        casProxyCallbackUrl = config.getInitParameter("edu.yale.its.tp.cas.client.filter.proxyCallbackUrl");
        wrapRequest = Boolean.valueOf(config.getInitParameter("edu.yale.its.tp.cas.client.filter.wrapRequest")).booleanValue();
        casGateway = Boolean.valueOf(config.getInitParameter("edu.yale.its.tp.cas.client.filter.gateway")).booleanValue();
        if(casGateway && Boolean.valueOf(casRenew).booleanValue())
            throw new ServletException("gateway and renew cannot both be true in filter configuration");
        if(casServerName != null && casServiceUrl != null)
            throw new ServletException("serverName and serviceUrl cannot both be set: choose one.");
        if(casServerName == null && casServiceUrl == null)
            throw new ServletException("one of serverName or serviceUrl must be set.");
        if(casServiceUrl != null && !casServiceUrl.startsWith("https://") && !casServiceUrl.startsWith("http://"))
            throw new ServletException("service URL must start with http:// or https://; its current value is [" + casServiceUrl + "]");
        if(casValidate == null)
            throw new ServletException("validateUrl parameter must be set.");
        if(!casValidate.startsWith("https://"))
            throw new ServletException("validateUrl must start with https://, its current value is [" + casValidate + "]");
        if(casAuthorizedProxy != null)
        {
            String anAuthorizedProxy;
            for(StringTokenizer casProxies = new StringTokenizer(casAuthorizedProxy); casProxies.hasMoreTokens(); authorizedProxies.add(anAuthorizedProxy))
            {
                anAuthorizedProxy = casProxies.nextToken();
                if(!anAuthorizedProxy.startsWith("https://"))
                    throw new ServletException("CASFilter initialization parameter for authorized proxies must be a whitespace delimited list of authorized proxies.  Authorized proxies must be secure (https) addresses.  This one wasn't: [" + anAuthorizedProxy + "]");
            }

        }
        if(log.isDebugEnabled())
            log.debug("CASFilter initialized as: [" + toString() + "]");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
        throws ServletException, IOException
    {
        log.debug("entering doFilter()");
        if(!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse))
        {
            log.error("doFilter() called on a request or response that was not an HttpServletRequest or response.");
            throw new ServletException("CASFilter protects only HTTP resources");
        }
        if(casProxyCallbackUrl != null && casProxyCallbackUrl.endsWith(((HttpServletRequest)request).getRequestURI()) && request.getParameter("pgtId") != null && request.getParameter("pgtIou") != null)
        {
            log.debug("passing through what we hope is CAS's request for proxy ticket receptor.");
            fc.doFilter(request, response);
            return;
        }
        if(wrapRequest)
        {
            log.debug("Wrapping request with CASFilterRequestWrapper.");
            request = new CASFilterRequestWrapper((HttpServletRequest)request);
        }
        HttpSession session = ((HttpServletRequest)request).getSession();
        CASReceipt receipt = (CASReceipt)session.getAttribute("edu.yale.its.tp.cas.client.filter.receipt");
        if(receipt != null && isReceiptAcceptable(receipt))
        {
            log.debug("CAS_FILTER_RECEIPT attribute was present and acceptable - passing  request through filter..");
            fc.doFilter(request, response);
            return;
        }
        String ticket = request.getParameter("ticket");
        if(ticket == null || ticket.equals(""))
        {
            log.debug("CAS ticket was not present on request.");
            boolean didGateway = Boolean.valueOf((String)session.getAttribute("edu.yale.its.tp.cas.client.filter.didGateway")).booleanValue();
            if(casLogin == null)
            {
                log.fatal("casLogin was not set, so filter cannot redirect request for authentication.");
                throw new ServletException("When CASFilter protects pages that do not receive a 'ticket' parameter, it needs a edu.yale.its.tp.cas.client.filter.loginUrl filter parameter");
            }
            if(!didGateway)
            {
                log.debug("Did not previously gateway.  Setting session attribute to true.");
                session.setAttribute("edu.yale.its.tp.cas.client.filter.didGateway", "true");
                redirectToCAS((HttpServletRequest)request, (HttpServletResponse)response);
                return;
            }
            log.debug("Previously gatewayed.");
            if(casGateway || session.getAttribute("edu.yale.its.tp.cas.client.filter.user") != null)
            {
                log.debug("casGateway was true and CAS_FILTER_USER set: passing request along filter chain.");
                fc.doFilter(request, response);
                return;
            } else
            {
                session.setAttribute("edu.yale.its.tp.cas.client.filter.didGateway", "true");
                redirectToCAS((HttpServletRequest)request, (HttpServletResponse)response);
                return;
            }
        }
        try
        {
            receipt = getAuthenticatedUser((HttpServletRequest)request);
        }
        catch(CASAuthenticationException e)
        {
            log.error(e);
            throw new ServletException(e);
        }
        if(!isReceiptAcceptable(receipt))
            throw new ServletException("Authentication was technically successful but rejected as a matter of policy. [" + receipt + "]");
        if(session != null)
        {
            session.setAttribute("edu.yale.its.tp.cas.client.filter.user", receipt.getUserName());
            session.setAttribute("edu.yale.its.tp.cas.client.filter.receipt", receipt);
            session.removeAttribute("edu.yale.its.tp.cas.client.filter.didGateway");
        }
        log.debug("validated ticket to get authenticated receipt [" + receipt + "], now passing request along filter chain.");
        fc.doFilter(request, response);
        log.debug("returning from doFilter()");
    }

    private boolean isReceiptAcceptable(CASReceipt receipt)
    {
        if(receipt == null)
            throw new IllegalArgumentException("Cannot evaluate a null receipt.");
        if(casRenew && !receipt.isPrimaryAuthentication())
            return false;
        return !receipt.isProxied() || authorizedProxies.contains(receipt.getProxyingService());
    }

    private CASReceipt getAuthenticatedUser(HttpServletRequest request)
        throws ServletException, CASAuthenticationException
    {
        log.debug("entering getAuthenticatedUser()");
        ProxyTicketValidator pv = null;
        pv = new ProxyTicketValidator();
        pv.setCasValidateUrl(casValidate);
        pv.setServiceTicket(request.getParameter("ticket"));
        
        HttpSession session=request.getSession();
        String servizio=(String)session.getAttribute("servizioTest");
        pv.setService(servizio);
        //pv.setService(getService(request));
        pv.setRenew(Boolean.valueOf(casRenew).booleanValue());
        if(casProxyCallbackUrl != null)
            pv.setProxyCallbackUrl(casProxyCallbackUrl);
        if(log.isDebugEnabled())
            log.debug("about to validate ProxyTicketValidator: [" + pv + "]");
        return CASReceipt.getReceipt(pv);
    }

    private String getService(HttpServletRequest request)
        throws ServletException
    {
        log.debug("entering getService()");
        if(casServerName == null && casServiceUrl == null)
            throw new ServletException("need one of the following configuration parameters: edu.yale.its.tp.cas.client.filter.serviceUrl or edu.yale.its.tp.cas.client.filter.serverName");
        String serviceString;
        if(casServiceUrl != null){
            serviceString = URLEncoder.encode(casServiceUrl);
        }
        else
            serviceString = Util.getService(request, casServerName);
        log.debug("returning from getService() with service [" + serviceString + "]");
        return serviceString;
    }
    
    private String getParameters(HttpServletRequest request){
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
    

    private void redirectToCAS(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        log.debug("entering redirectToCAS()");

        HttpSession session=request.getSession();
        session.setAttribute("servizioTest",getService(request));
        String  casLoginString = casLogin + "?service=" + getService(request)+ (casRenew ? "&renew=true" : "") + (casGateway ? "&gateway=true" : "");
        log.debug("Redirecting browser to [" + casLoginString + ")");
        response.sendRedirect(casLoginString);
        log.debug("returning from redirectToCAS()");
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[CASFilter:");
        sb.append(" casGateway=");
        sb.append(casGateway);
        sb.append(" wrapRequest=");
        sb.append(wrapRequest);
        sb.append(" casAuthorizedProxies=[");
        sb.append(authorizedProxies);
        sb.append("]");
        if(casLogin != null)
        {
            sb.append(" casLogin=[");
            sb.append(casLogin);
            sb.append("]");
        } else
        {
            sb.append(" casLogin=NULL!!!!!");
        }
        if(casProxyCallbackUrl != null)
        {
            sb.append(" casProxyCallbackUrl=[");
            sb.append(casProxyCallbackUrl);
            sb.append("]");
        }
        if(casRenew)
            sb.append(" casRenew=true");
        if(casServerName != null)
        {
            sb.append(" casServerName=[");
            sb.append(casServerName);
            sb.append("]");
        }
        if(casServiceUrl != null)
        {
            sb.append(" casServiceUrl=[");
            sb.append(casServiceUrl);
            sb.append("]");
        }
        if(casValidate != null)
        {
            sb.append(" casValidate=[");
            sb.append(casValidate);
            sb.append("]");
        } else
        {
            sb.append(" casValidate=NULL!!!");
        }
        return sb.toString();
    }

    public void destroy()
    {
    }

    public static final String LOGIN_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.loginUrl";
    public static final String VALIDATE_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.validateUrl";
    public static final String SERVICE_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.serviceUrl";
    public static final String SERVERNAME_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.serverName";
    public static final String RENEW_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.renew";
    public static final String AUTHORIZED_PROXY_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.authorizedProxy";
    public static final String PROXY_CALLBACK_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.proxyCallbackUrl";
    public static final String WRAP_REQUESTS_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.wrapRequest";
    public static final String GATEWAY_INIT_PARAM = "edu.yale.its.tp.cas.client.filter.gateway";
    public static final String CAS_FILTER_USER = "edu.yale.its.tp.cas.client.filter.user";
    public static final String CAS_FILTER_RECEIPT = "edu.yale.its.tp.cas.client.filter.receipt";
    private static final String CAS_FILTER_GATEWAYED = "edu.yale.its.tp.cas.client.filter.didGateway";
    private String casLogin;
    private String casValidate;
    private String casServiceUrl;
    private String casServerName;
    private String casProxyCallbackUrl;
    private boolean casRenew;
    private boolean wrapRequest;
    private boolean casGateway;
    private List authorizedProxies;

}
