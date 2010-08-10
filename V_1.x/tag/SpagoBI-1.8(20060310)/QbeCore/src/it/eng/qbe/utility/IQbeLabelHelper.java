package it.eng.qbe.utility;

/**
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements logic
 * to retrieve labels for the entities(classes) and the fields
 * of the datamart model
 * 
 */
public interface IQbeLabelHelper{

	/**
	 * @param completeClassName, the entity(class) to retrieve label
	 * @return the label for entity(class) identified by completeClassName
	 */
	public String getLabelForClass(String completeClassName);
	
	/**
	 * @param completeFieldName the fieldName to retrieve label
	 * @return the label for field identified by completeFieldName
	 */
	public String getLabelForField(String completeFieldName);

}
