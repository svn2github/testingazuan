package com.tensegrity.palowebviewer.modules.engine.server;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.engine.client.IClientProperties;
import com.tensegrity.palowebviewer.modules.engine.client.IEngineService;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.AuthenticationException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InternalErrorException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidLoginOrPasswordException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDefaultView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.server.IUser;

public class EngineService extends BasePaloService implements
		IEngineService {

	private static final long serialVersionUID = -4183989263192186885L;

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EngineService.class);

	public EngineService() {
		Logger.setUseFirebug(false);
		Logger.setOn(true);
	}

	public Boolean isAuthenticated() {
		Boolean result = logic.isAuthenticated(getContext());
		log.debug("isAuthentificated");
		if(!result.booleanValue()){
			result = tryCookiesAuthentication();
		}
		if(!result.booleanValue()){
			//result = Boolean.TRUE;
		}
		return result;
	}
	
	public Boolean authenticate(String login, String password, boolean remember)
			throws InternalErrorException {
		HttpSession session = getThreadLocalRequest().getSession();
		if(session != null) session.invalidate();
		getThreadLocalRequest().getSession(true);
		
		IUser user = logic.authenticate(login, password, remember, getContext());
		if (user == null)
			return Boolean.FALSE;
		getContext().setUser(user);
		updateCookies(login, getContext().getAuthenificator().calculateHash(password), remember);
		return Boolean.TRUE;
	}

	public void logoff() {
		logic.logoff(getContext());
		Object user = getContext().getUser();
		log.info("logoff(" + user + ")");
		cleanCookies();
	}

	private void cleanCookies() {
		updateCookies("", "", false);
	}

	private void updateCookies(String login, String hash, boolean remember) {
		Cookie cLogin = new Cookie("login", login);
		Cookie cPwd = new Cookie("pwd", hash);
		int maxAge;
		String path = getThreadLocalRequest().getContextPath();
		if (!remember) { // mark cookies to be deleted
			maxAge = 0;
		} else {
			maxAge = 60 * 60 * 24 * 30; // 1 month
		}

		cLogin.setMaxAge(maxAge);
		cPwd.setMaxAge(maxAge);

		cLogin.setPath(path);
		cPwd.setPath(path);

		getThreadLocalResponse().addCookie(cLogin);
		getThreadLocalResponse().addCookie(cPwd);
	}

	public XResult[] query(final XQueryPath[] queries)
			throws InvalidObjectPathException {
		return logic.query(queries, getContext());
	}

	public void updateData(final XPath cube, final IXPoint point,
			final IResultElement value) throws InvalidObjectPathException {
		logic.updateData(cube, point, value, getContext());
	}

	public String saveView(final XViewPath viewPath)
			throws InvalidObjectPathException {
		return logic.saveView(viewPath, getContext().getUserConnectionPool());
	}

	public void forceReload() {
		logic.forceReload(getContext());
	};
	
	protected Boolean tryCookiesAuthentication() {
		Boolean result = Boolean.FALSE;
		try {
			AuthInfo authInfo = getAuthInfoFromCookies();
			if (authInfo.isValid()) {
				String login = authInfo.getLogin();
				String pwdHash = authInfo.getPwdHash();
				IUser user = getContext().getAuthenificator().hashAuthentificate(login, pwdHash);
				getContext().setUser(user);
				updateCookies(login, pwdHash, true);
				result = Boolean.TRUE;
			}
		} catch (InvalidLoginOrPasswordException e) {
			// result == false; do nothing.
		} catch (AuthenticationException e) {
			log.error("Can't authenticate user", e);
		}
		return result;
	}


	
	protected AuthInfo getAuthInfoFromCookies () {
		AuthInfo result = new AuthInfo();
		Cookie[] cookies = getThreadLocalRequest().getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				String name = cookie.getName();
				String value = cookie.getValue();
				result.setProperty(name, value);
			}
		}
		return result;
	}

	public IClientProperties getClientProperties() {
		return logic.getClientProperties(getContext());
	}


	public XObject[] loadChildren(XPath path, int type) throws InvalidObjectPathException {
		return logic.loadChildren(path, type, getContext());
	}
	
	public XElement[] getParentsOf(XPath contextPath, String elementName) throws InvalidObjectPathException {
		return logic.getParentsOf(contextPath, elementName, getContext());
	}

	public XDefaultView loadDefaultView(XPath path) throws InvalidObjectPathException {
		return logic.loadDefaultView(path, getContext());
	}

	public boolean checkExistance(XPath contextPath, String elementName) throws InvalidObjectPathException {
		return logic.checkExistance(contextPath, elementName, getContext());
	}

	public boolean checkExistance(XPath path) {
		return logic.checkExistance(path, getContext());
	}
	
	public XFolder loadFavoriteViews() {
		return logic.loadFavoriteViews(getContext());
	}
	

	public XObject loadChild(XPath path, String childId, int type) throws InvalidObjectPathException {
		return logic.loadChild(path, childId, type, getContext());
	}

	public XObject loadChildByName(XPath path, String name, int type) throws InvalidObjectPathException{
		return logic.loadChildByName(path, name, type, getContext());
	}



	//Support classes
	
	protected static class AuthInfo {
		private String login;
		private String pwdHash;
		
		
		public void setProperty(String name, String value) {
			if (name.equals("login"))
				setLogin(value);
			else if (name.equals("pwd"))
				setPwdHash(value);
		}
		
		public String getLogin () {
			return login;
		}
		
		public String getPwdHash() {
			return pwdHash;
		}
		
		public void setLogin(String value) {
			if("".equals(value))
				value = null;
			login = value;
		}
		
		public void setPwdHash(String value) {
			if("".equals(value))
				value = null;			
			pwdHash = value;
		}
		
		public boolean isValid() {
			return getLogin()!=null && getPwdHash() != null;
		}

	}
}
