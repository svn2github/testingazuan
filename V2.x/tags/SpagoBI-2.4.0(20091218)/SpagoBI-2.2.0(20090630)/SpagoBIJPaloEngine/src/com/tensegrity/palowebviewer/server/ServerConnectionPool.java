package com.tensegrity.palowebviewer.server;

import org.apache.log4j.Logger;
import org.palo.api.Connection;
import org.palo.api.ConnectionContext;
import org.palo.api.ConnectionListener;
import org.palo.api.Database;
import org.palo.api.Property2;
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;

import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;

public class ServerConnectionPool {


    private final static Logger log = Logger.getLogger(ServerConnectionPool.class);

    private final ConnectionWrapper[] connections;

    private final IConnectionFactory factory;
    private final String host;
    private final String service;
    private final String login;
    private final String password;
	private final String provider;
    private final Object mutex = new Object();



    public ServerConnectionPool(IConnectionFactory factory, String host, String service, String login, String password, int capasity, String provider){
    	this.factory = factory;
        this.host = host;
        this.service = service;
        this.login = login;
        this.password = password;
        this.provider = provider;
        connections = new ConnectionWrapper[capasity];
        getConnection().disconnect();//load one connection for fast access;
    }
    
    public void markNeedReload() {
    	for (int i = 0; i < connections.length; i++) {
    		if(connections[i] != null)
    			connections[i].setNeedReload();
		}
    }

    private ConnectionWrapper openConnection(int index){
        synchronized (mutex) {
            log.debug("openConnection("+index+")");
            Connection nativeConnection = factory.createConnection(host, service, login, password, provider);
            ConnectionWrapper conn = new ConnectionWrapper(nativeConnection, index);
            return conn;
        }
    }

    public Connection getConnection(){
        ConnectionWrapper result = null;
        log.debug(" getConnection");
        synchronized (mutex) {
            for(int i = 0; (i < connections.length) && (result == null); ++i){
            	result = tryGetConnection(i);
            }
        }
        if(result == null) 
        	throw new RuntimeException("no more connections avalable");
        return result;
    }

	private ConnectionWrapper tryGetConnection(int i) {
		ConnectionWrapper result = null;
		if(connections[i] == null)
			connections[i] = openConnection(i);
		if(!connections[i].isBusy()){
			result = connections[i];
			result.setBusy();
			result.reloadOnDemand();
		}
		return result;
	}

    public void shutdown(){
        synchronized (mutex) {
            for (int i = 0; i < connections.length; i++) {
            	if(connections[i] != null)
            		connections[i].forceDisconnect();
            }
        }
    }

    private class ConnectionWrapper implements Connection{

        private final Logger log = Logger.getLogger(ConnectionWrapper.class);
        private final Connection conn;
        private final int index;
        private boolean busy = false;
        private boolean needReload = false;
        private final Object mutex = new Object();


        public void setBusy() {
        	synchronized (mutex) {
        		if(!isBusy()){
        			busy = true;
        			log.debug(this + " -> busy");
        		}
        		else {
        			log.warn("Someone tried to grab a busy " + this + "!");
        		}
			}
        }

        public boolean isBusy() {
            return busy;
        }

        public void setNeedReload() {
        	synchronized (mutex) {
        		if(!isNeedReload()) {
                	needReload = true;
                	log.debug(this + " -> need reload");
            	}
			}

        }

        public boolean isNeedReload() {
            return needReload;
        }

        public ConnectionWrapper(final Connection conn, final int index){
            this.conn = conn;
            this.index = index;
            /*conn.addConnectionListener(new ConnectionListener(){
                public void connectionChanged(ConnectionEvent arg0) {
                    log.debug(this + ": changed!");
                    setNeedReload();
                }

            });
            */
        }

        public void reloadOnDemand(){
            if(isNeedReload()) {
                reload();
            }
        }

        public void forceDisconnect(){
            log.debug("conn");
            conn.disconnect();
        }

        public void addConnectionListener(ConnectionListener listener) {
            conn.addConnectionListener(listener);
        }

        public Database addDatabase(String db) {
            return conn.addDatabase(db);
        }

        public void disconnect() {
            log.debug(this.mutex + ".disconnect()");
            synchronized (mutex) {
                if(isBusy()){
                    log.debug("connection["+index+"] -> available");
                    busy = false;
                }
                else {
                    log.warn("Someone tried to disconnect free connection["+index+"]");
                }
            }
        }

        public Database getDatabaseAt(int index) {
            return conn.getDatabaseAt(index);
        }

        public Database getDatabaseByName(String name) {
            return conn.getDatabaseByName(name);
        }

        public int getDatabaseCount() {
            return conn.getDatabaseCount();
        }

        public Database[] getDatabases() {
            return conn.getDatabases();
        }

        public String getPassword() {
            return conn.getPassword();
        }

        public String getServer() {
            return conn.getServer();
        }

        public String getService() {
            return conn.getService();
        }

        public Database[] getSystemDatabases() {
            return conn.getSystemDatabases();
        }

        public String getUsername() {
            return conn.getUsername();
        }

        public void ping() {
            conn.ping();
        }

        public void reload() {
        	synchronized (mutex) {
        		log.debug(this+".reloading");
        		PerformanceTimer timer = new PerformanceTimer("ConnectionWraper.realod");
        		timer.start();
            	conn.reload();
            	timer.report();
            	needReload = false;
			}
        }

        public void removeConnectionListener(ConnectionListener listner) {
            conn.removeConnectionListener(listner);
        }

        public void removeDatabase(Database db) {
            conn.removeDatabase(db);
        }

        public void save() {
            conn.save();
        }

        public boolean isLegacy() {
            return conn.isLegacy();
        }

        public String toString() {
            return "connection["+index+"]";
        }

		public String getFunctions() {
			return conn.getFunctions();
		}

		public boolean isConnected() {
			return conn.isConnected();
		}

		public boolean login(String arg0, String arg1) {
			return conn.login(arg0, arg1);
		}

		public FavoriteViewTreeNode loadFavoriteViews() {
			return conn.loadFavoriteViews();
		}

		public void storeFavoriteViews(FavoriteViewTreeNode node) {
			conn.storeFavoriteViews(node);
		}

		public Database getDatabaseById(String id) {
			return conn.getDatabaseById(id);
		}

		public int getType() {
			return conn.getType();
		}

		public void addProperty(Property2 arg0) {
			conn.addProperty(arg0);
		}

		public String[] getAllPropertyIds() {
			return conn.getAllPropertyIds();
		}

		public ConnectionContext getContext() {
			return conn.getContext();
		}

		public Property2 getProperty(String arg0) {
			return conn.getProperty(arg0);
		}

		public void removeProperty(String arg0) {
			conn.removeProperty(arg0);
		}

		public boolean canBeModified() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean canCreateChildren() {
			// TODO Auto-generated method stub
			return false;
		}

    }

}
