package com.tensegrity.palowebviewer.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.ClientProperties;
import com.tensegrity.palowebviewer.modules.engine.client.IClientProperties;
import com.tensegrity.palowebviewer.modules.paloclient.client.XServer;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;

public class PaloConfiguration {

    public static final int DEFAULT_POOL_MAX_CONNECTIONS = 10;

    private List servers = new ArrayList();
    private IRightManager manager;
    private int poolMaxConnections = DEFAULT_POOL_MAX_CONNECTIONS;
    private IClientProperties clientProperties = new ClientProperties();
    private String user = "guest";
    private String password = "pass";
    
    public IClientProperties getClientProperties() {
    	return clientProperties;
    }
    
    public void setClientProperties (IClientProperties value) {
    	clientProperties = value;
    }

    public final List getServers() {
        return servers;
    }

    public final void setServers(List servers) {
        this.servers = servers;
    }

    public IRightManager getRightManager(){
        return this.manager;
    }

    public void setRightManager(IRightManager manager){
        this.manager = manager;
    }

    public int getPoolMaxConnections() {
        return poolMaxConnections;
    }

    public void setPoolMaxConnections(int value) {
        poolMaxConnections = value;
    }
    
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

    public void addServer(PaloServer server){
        servers.add(server);
        Collections.sort(servers, new ServerOrderComparator());
    }

    /**
     * @return PaloServer. If it can't be found, null is returned.
     */
    public PaloServer getServer(int order){
        PaloConfiguration.PaloServer r = null;
        for (Iterator it = servers.iterator(); it.hasNext();) {
            PaloServer server = (PaloServer) it.next();
            if(server.getOrder() == order){
                r = server;
                break;
            }
        }
        return r;
    }
    
    public PaloServer getServer(String host, String service){
    	return getServer(XServer.createId(host, service));
    }
    
    public PaloServer getServer(String serverId){
        PaloConfiguration.PaloServer r = null;
        for (Iterator it = servers.iterator(); it.hasNext();) {
            PaloServer server = (PaloServer) it.next();
            if(serverId.equals(server.getId())){
                r = server;
                break;
            }
        }
        return r;
    }

    public String toString() {
        String result = "PaloConfiguration[";
        int size = servers.size();
        for( int i = 0 ; i < size ; i++ ) { 
            result += servers.get(i)+"\n";
        } 
        result += "connection.pool.max = "+getPoolMaxConnections();
        result += "]";
        return result;
    }

    // internal class;
    public static final class PaloServer{

    	private final static String DEFAULT_PROVIDER = "palo";
        private String host;
        private int order;
        private String service;
        private String login;
        private String password;
        private String provider = DEFAULT_PROVIDER;
		private String dispName;

        public final String getHost() {
            return host;
        }
        public Object getId() {
			return XServer.createId(getHost(),getService());
		}
		public final void setHost(String host) {
            this.host = host;
        }
        public final String getLogin() {
            return login;
        }
        public final void setLogin(String login) {
            this.login = login;
        }
        public final String getPassword() {
            return password;
        }
        public final void setPassword(String password) {
            this.password = password;
        }
        public final String getService() {
            return service;
        }
        public final void setService(String service) {
            this.service = service;
        }
		public String getProvider() {
			return provider;
		}
		public void setProvider(String provider) {
			this.provider = provider;
		}
		
		public String toString() {
			String result = "PaloServer[";
			result += provider + ";";
			result += host+":"+service+";";
			result += login+":" + password;
			result += "]";
			return result;
		}
		public int getOrder() {
			return order;
		}
		public void setOrder(int order) {
			this.order = order;
		}
		public void setDispName(String value) {
			dispName = value;
		}
		
		public String getDispName() {
			return dispName;
		}
    }
    
    private static class ServerOrderComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			PaloServer server1 = (PaloServer)o1;
			PaloServer server2 = (PaloServer)o2;
			return server1.getOrder() - server2.getOrder();
		}
    	
    }

}
