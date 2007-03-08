/**
 * LICENSE: see COPYING file
 */
package it.eng.spagobi.bo.lov;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.security.IEngUserProfile;

import java.io.Serializable;
import java.util.List;

/**
 * Defines methods to manage a lov
 */
public interface ILovDetail extends Serializable {
	/**
	 * serialize the lov to an xml string
	 * @return the serialized xml string
	 */
	public String toXML ();
	/** loads the lov from an xml string 
	 * @param dataDefinition the xml definition of the lov
	 * @throws SourceBeanException 
	 */
	public void loadFromXML (String dataDefinition) throws SourceBeanException;
	/**
	 * Returns the result of the lov using a user profile to fill the lov profile attribute
	 * @param profile the profile of the user
	 * @return the string result of the lov
	 * @throws Exception
	 */
	public String getLovResult(IEngUserProfile profile) throws Exception;
	/**
	 * Checks if the lov requires one or more profile attributes
	 * @return true if the lov require one or more profile attributes, false otherwise
	 * @throws Exception
	 */
	public boolean requireProfileAttributes() throws Exception;
	/**
	 * Gets the list of names of the profile attributes required
	 * @return list of profile attribute names
	 * @throws Exception
	 */
	public List getProfileAttributeNames() throws Exception;
	
	
	
	public List getVisibleColumnNames() throws Exception;
	
	public List getInvisibleColumnNames() throws Exception;
	
	public String getValueColumnName() throws Exception;
	
	public String getDescriptionColumnName() throws Exception;
	
	public void setVisibleColumnNames(List visCols) throws Exception;
	
	public void setInvisibleColumnNames(List invisCols) throws Exception;
	
	public void setValueColumnName(String name) throws Exception;
	
	public void setDescriptionColumnName(String name) throws Exception;
	
}
