package com.tensegrity.palowebviewer.modules.engine.server;

import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.tensegrity.palowebviewer.server.IConnectionPool;
import com.tensegrity.palowebviewer.server.IUser;
import com.tensegrity.palowebviewer.server.PaloConfiguration;
import com.tensegrity.palowebviewer.server.security.IAuthenificator;

public interface IEngineContext {

	public IAuthenificator getAuthenificator();
	
	public void setAuthenificator(IAuthenificator authenificator);
  
	public PaloConfiguration  getConfiguration();
	
	public IConnectionPool getUserConnectionPool();

	/**
	 * if user == null, the attribute will be removed from contex
	 */
	public void setUser(IUser user);
	
	public IUser getUser();
	
	public void init(ServletConfig scfg) throws ServletException;
	
	public Locale getLocale();
	
}
