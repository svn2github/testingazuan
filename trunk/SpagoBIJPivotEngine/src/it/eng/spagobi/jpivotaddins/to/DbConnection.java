/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.to;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class DbConnection {

	private transient Logger logger = Logger.getLogger(this.getClass());
	
	private boolean jndi = false;
	private boolean jdbc = true;
	private JdbcConnection jdbcConnection = null;
	private JndiConnection jndiConnection = null;
	
	
	public DbConnection() {
		try{
			SAXReader readerConFile = new SAXReader();
			Document documentConFile = readerConFile.read(getClass().getResourceAsStream("/engine-config.xml"));
			Node connectionDef = documentConFile.selectSingleNode("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION[@isDefault='true'");
			recoverConnectionInfo(connectionDef);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+"::constructor():Error while recovering connection info \n" + e);
		}
	}
	
	public DbConnection(String nameConnection) {
		try{
			SAXReader readerConFile = new SAXReader();
			Document documentConFile = readerConFile.read(getClass().getResourceAsStream("/engine-config.xml"));
			Node connectionDef = documentConFile.selectSingleNode("//ENGINE-CONFIGURATION/CONNECTIONS-CONFIGURATION/CONNECTION[@name='"+nameConnection+"'");
			recoverConnectionInfo(connectionDef);
		} catch (Exception e) {
			logger.error(this.getClass().getName()+"::constructor(String):Error while recovering connection info \n" + e);
		}
	}	
		
	private void recoverConnectionInfo(Node connectionDef) {
		String jndi = connectionDef.valueOf("@isJNDI");
		if((jndi!=null) && jndi.equalsIgnoreCase("true")) { 
		    String iniCont = connectionDef.valueOf("@initialContext");
		    String resName = connectionDef.valueOf("@resourceName");
		    this.setJndi(true);
		    this.setJdbc(false);
		    JndiConnection jndiCon = new JndiConnection();
		    jndiCon.setIniCont(iniCont);
		    jndiCon.setResName(resName);
		    this.setJndiConnection(jndiCon);
		} else {
			String driver = connectionDef.valueOf("@driver");
			String url = connectionDef.valueOf("@jdbcUrl");
			String usr = connectionDef.valueOf("@user");
			String pwd = connectionDef.valueOf("@password");
			this.setJndi(false);
		    this.setJdbc(true);
		    JdbcConnection jdbcCon = new JdbcConnection();
		    jdbcCon.setDriver(driver);
		    jdbcCon.setPwd(pwd);
		    jdbcCon.setUrl(url);
		    jdbcCon.setUsr(usr);
		    this.setJdbcConnection(jdbcCon);
		}
	}
		
	
	public boolean isJdbc() {
		return jdbc;
	}
	public void setJdbc(boolean jdbc) {
		this.jdbc = jdbc;
	}
	public boolean isJndi() {
		return jndi;
	}
	public void setJndi(boolean jndi) {
		this.jndi = jndi;
	}
	public JdbcConnection getJdbcConnection() {
		return jdbcConnection;
	}
	public void setJdbcConnection(JdbcConnection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}
	public JndiConnection getJndiConnection() {
		return jndiConnection;
	}
	public void setJndiConnection(JndiConnection jndiConnection) {
		this.jndiConnection = jndiConnection;
	}
	
}
