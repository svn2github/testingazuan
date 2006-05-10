package it.eng.spagobi.importexport;

public class DBConnection {

	private String name;
	private String description;
	private boolean jdbcType = false;
	private boolean jndiType = false;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(boolean jdbcType) {
		this.jdbcType = jdbcType;
	}
	public boolean isJndiType() {
		return jndiType;
	}
	public void setJndiType(boolean jndiTypw) {
		this.jndiType = jndiTypw;
	}
	
	
}
