package it.eng.spagobi.importexport;

public class JdbcConnection extends DBConnection {
	
	private String driverClassName = "";
	private String connectionString = "";
	
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	
	
}
