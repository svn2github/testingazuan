package com.tensegrity.palowebviewer.modules.ui.client;

import com.tensegrity.palowebviewer.modules.util.client.JavaScript;
import com.tensegrity.palowebviewer.modules.util.client.Logger;

public class RequestParamParser {
	
	public static final String TABLE_ONLY = "table-only";
	public static final String EDITOR_ONLY = "editor-only";
	public static final String TABLE_PATH = "table-path";
	public static final String USER = "user";
	public static final String PASSWORD = "password";
	private static final String SERVER = "server";
	private static final String SCHEMA = "schema";
	private static final String CUBE = "cube";
	
	private boolean tableOnly;
	private boolean editorOnly;
	private String[] tablePath;
	private String user;
	private String password;
	private String serverName;
	private String schemaName;
	private String cubeName;
	
	
	
	public RequestParamParser() {
		parse();
	}
	
	public String getPassword() {
		return password;
	}

	public boolean isTableOnly() {
		return tableOnly;
	}

	public boolean isEditorOnly() {
		return editorOnly;
	}

	public String[] getTablePath() {
		return tablePath;
	}

	public String getUser() {
		return user;
	}

	public void parse() {
		tableOnly = readBoolean(TABLE_ONLY);
		editorOnly = readBoolean(EDITOR_ONLY);
		String path = JavaScript.getUrlParam(TABLE_PATH);

		if(path != null) {
			tablePath = path.split(";"); 
		}
		user = JavaScript.getUrlParam(USER);
		Logger.debug("user: " + user);
		user = user==null?"guest":user;
		
		password = JavaScript.getUrlParam(PASSWORD);
		Logger.debug("password: " + password);
		password = password==null?"pass":password;
		
		serverName = JavaScript.getUrlParam(SERVER);
		Logger.debug("[RequestParamParser] database: " + serverName);
		
		schemaName = JavaScript.getUrlParam(SCHEMA);
		Logger.debug("[RequestParamParser] schema: " + schemaName);
		
		cubeName = JavaScript.getUrlParam(CUBE);
		Logger.debug("[RequestParamParser] cube: " + cubeName);
		
		//tableOnly = true;
		//editorOnly = true;		
	}

	private boolean readBoolean(String varName) {
		String urlParam = JavaScript.getUrlParam(varName);
		Logger.debug("[RequestParamParser] " + varName + ": " + urlParam);
		
		if(urlParam != null) {
			urlParam = urlParam.toLowerCase();
		}
		return "true".equals(urlParam) || "yes".equals(urlParam);
	}

	

	public String getSchemaName() {
		return schemaName;
	}

	public String getCubeName() {
		return cubeName;
	}

	public String getServerName() {
		return serverName;
	}

}
