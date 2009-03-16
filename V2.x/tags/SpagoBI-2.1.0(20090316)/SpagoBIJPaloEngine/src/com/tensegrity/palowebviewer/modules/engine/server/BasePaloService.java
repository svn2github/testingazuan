package com.tensegrity.palowebviewer.modules.engine.server;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tensegrity.palowebviewer.server.GlobalConnectionPool;
import com.tensegrity.palowebviewer.server.IConnectionFactory;
import com.tensegrity.palowebviewer.server.IConnectionPool;
import com.tensegrity.palowebviewer.server.IUser;
import com.tensegrity.palowebviewer.server.PaloConfiguration;
import com.tensegrity.palowebviewer.server.PaloConfigurator;
import com.tensegrity.palowebviewer.server.PaloConnectionFactory;
import com.tensegrity.palowebviewer.server.security.AuthenificatorFactory;
import com.tensegrity.palowebviewer.server.security.IAuthenificator;

public abstract class BasePaloService extends RemoteServiceServlet {

	private static final String USER_CREDENTIALS = "com.tensegrity.user.credentials";
	private static final String CONFIGURATION = "com.tensegrity.confiruration";
	private static final String POOL = "com.tensegrity.pool";

	private static final long serialVersionUID = -6926892580699437581L;
	
	protected EngineLogic logic = new EngineLogic();
	private IEngineContext context;
	
	public BasePaloService(){
		setContext(new EngineContext(this));
	}
	
	public void init(ServletConfig scfg) throws ServletException {
		super.init(scfg);
		getContext().init(scfg);
	}
	
	protected void setContext(IEngineContext context) {
		this.context = context;
	}

	protected IEngineContext getContext() {
		return context;
	}

	private class EngineContext implements IEngineContext {

		private IAuthenificator authenificator;
		private BasePaloService servlet;

		public EngineContext(BasePaloService servlet) {
			this.servlet = servlet;
		}
		
		private ServletContext getServletContext(){
			return servlet.getServletContext();
		}
		
		public PaloConfiguration getConfiguration() {
			return (PaloConfiguration) getServletContext().getAttribute(CONFIGURATION);
		}

		public IConnectionPool getUserConnectionPool() {
			IUser user = getUser();
			PaloConfiguration cfg = getConfiguration();
			IConnectionPool pool = (IConnectionPool) getServletContext().getAttribute(POOL);
			return cfg.getRightManager().getPool(pool, user);
		}

		public IAuthenificator getAuthenificator() {
			return authenificator;
		}

		public void setAuthenificator(IAuthenificator authenificator) {
			this.authenificator = authenificator;

		}

		public void init(ServletConfig scfg) throws ServletException {
			try {
				String AUTH_CLASS_NAME = "com.tensegrity.palowebviewer.server.security.TestEnviromentAuthenificator";
				ServletContext ctx = scfg.getServletContext();
				InputStream in = getClass().getResourceAsStream("/palo.properties");
				Properties props = new Properties();
				props.load(in);
				PaloConfiguration cfg = PaloConfigurator.getConfiguration(props);
				authenificator = AuthenificatorFactory.getAuthenificator(AUTH_CLASS_NAME, cfg);
				ctx.setAttribute(CONFIGURATION, cfg);
				String useMock = System.getProperty("CONNECTION_FACTORY_CLASS"); 
				IConnectionFactory factory;
				if(useMock != null){
					factory = (IConnectionFactory) Class.forName(useMock).newInstance();
				}else{
					factory = new PaloConnectionFactory();
				}
				factory.initialize(cfg);
				IConnectionPool pool = new GlobalConnectionPool(cfg, factory);
				ctx.setAttribute(POOL, pool);
			} catch (Exception ex) {
				throw new ServletException(ex);
			}
		}

		public void setUser(IUser user) {
			HttpServletRequest request = getThreadLocalRequest();
			HttpSession session = request.getSession();
			if(user != null){
				session.setAttribute(USER_CREDENTIALS, user);
				session.invalidate();
			}else{
				session.removeAttribute(USER_CREDENTIALS);
			}
		}

		public IUser getUser() {
			HttpServletRequest request = getThreadLocalRequest();
			HttpSession session = request.getSession();
			IUser user = (IUser) session.getAttribute(USER_CREDENTIALS);
			return user;
		}

		public Locale getLocale() {
			return getThreadLocalRequest().getLocale();
		}
	}

}
