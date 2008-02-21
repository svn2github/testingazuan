package com.tensegrity.palowebviewer.server;

import org.palo.api.Connection;
import org.palo.api.ConnectionConfiguration;
import org.palo.api.ConnectionFactory;

import com.tensegrity.palo.xmla.XMLAClient;

public class PaloConnectionFactory implements IConnectionFactory {

	public Connection createConnection(String host, String service, String login,
			String password, String provider) {
		ConnectionConfiguration cc = new ConnectionConfiguration(host, service);
		cc.setUser(login);
		cc.setPassword(password);
		int type = provider.equalsIgnoreCase("xmla")? Connection.TYPE_XMLA : Connection.TYPE_HTTP;
		cc.setType(type);
		cc.setLoadOnDemand(true);
		
		/*
		if(provider.equalsIgnoreCase("xmla")){
			try{
				 XMLAClient c = null;
				 c = new XMLAClient("a", "b", "c", "d");
				 c.setVerbose(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}*/
		Connection r = ConnectionFactory.getInstance().newConnection(cc);
		return r;
	}

	public void initialize(PaloConfiguration cfg) {
		//ignore
	}

}
