package com.tensegrity.palowebviewer.modules.paloclient.client;

import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;


public class XServer extends XObject {

    private String service;
    private String login;
    private XDatabase[] databases;
	private String dispName;

    public XServer(){

    }

    public XServer(String host, String service, String login, XDatabase[] dbs) {
        super(host, host); //id equals to host for XServer 
        this.service = service;
        this.login = login;
        this.databases = dbs;
    }

	public void setName(String name) {
    }

    public String getName() {
        return getId();
    }
    
    public String getId() {
        return createId(getHost(), getService());
    }

    public void setHost(String host){
        super.setName(host);
    }

    public String getHost(){
        return super.getName();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void set(XObject object) {
        if(!(object instanceof XServer))
            throw new IllegalArgumentException("XObject has to be of type XServer");
        XServer server = (XServer) object;
        super.set(object);
        setDatabases(((XServer)object).getDatabases());
        setHost(server.getHost());
        setService(server.getService());
    }

    public XDatabase[] getDatabases() {
        return databases;
    }

    public void setDatabases(XDatabase[] databases) {
        this.databases = databases;
        XHelper.setParent(databases, this);
    }

    public boolean equals(Object o) {
        if(o instanceof XServer)
            return equals((XServer)o);
        else
            return false;
    }

    public boolean equals(XServer server) {
        if(server == null)
            return false;
        return super.equals(server);
    }

    public int getTypeID() {
        return TYPE_SERVER;
    }

	public static String createId(String host, String service) {
		return host + ":" + service;
	}

	public void setDispName(String value) {
		this.dispName = value;
	}
	
	public String getDispName() {
		return dispName;
	}

}
