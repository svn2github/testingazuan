package it.eng.spagobi.importexport;

public class JndiConnection extends DBConnection {
	
	private String jndiContextName = "";
	private String jndiName = "";
	
	public String getJndiContextName() {
		return jndiContextName;
	}
	public void setJndiContextName(String jndiContextName) {
		this.jndiContextName = jndiContextName;
	}
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

}
