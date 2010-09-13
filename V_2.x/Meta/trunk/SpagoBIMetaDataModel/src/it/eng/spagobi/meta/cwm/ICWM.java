/**
 * 
 */
package it.eng.spagobi.meta.cwm;



/**
 * @author agioia
 *
 */
public interface ICWM {
	public CWMImplType getImplementationType();
	public String getName();
	public void setName(String name);
	
	public void exportToXMI(String filename);	
	public void importFromXMI(String filename);
}
