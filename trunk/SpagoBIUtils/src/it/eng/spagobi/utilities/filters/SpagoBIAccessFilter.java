package it.eng.spagobi.utilities.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SpagoBIAccessFilter implements Filter {

	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String username = request.getParameter("username");
		String spagobiContextUrl = request.getParameter("spagobicontext");
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession();
			if (username != null) session.setAttribute("username", username);
			if (spagobiContextUrl != null) session.setAttribute("spagobicontext", spagobiContextUrl);
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}

}
