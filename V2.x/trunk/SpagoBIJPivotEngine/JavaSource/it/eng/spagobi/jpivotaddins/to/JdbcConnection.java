/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.to;

public class JdbcConnection {

	private String driver = "";
	private String url = "";
	private String usr = "";
	private String pwd = "";
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsr() {
		return usr;
	}
	public void setUsr(String usr) {
		this.usr = usr;
	}
	
	
}
