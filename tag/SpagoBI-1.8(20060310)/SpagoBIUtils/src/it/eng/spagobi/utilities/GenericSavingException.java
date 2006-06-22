package it.eng.spagobi.utilities;

public class GenericSavingException extends Exception {
	
	private String description;
	
	public GenericSavingException() {
		super();
	}
	
	public GenericSavingException(String msg) {
		super();
		this.setLocalizedMessage(msg);
	}
	
    public String getLocalizedMessage() {
        return description;
    }
    
    public void setLocalizedMessage(String msg) {
        this.description = msg;
    }
}
