/**
 * 
 */
package it.eng.qbe.export;

import java.util.Vector;

/**
 * Interface of class that can get fields (i.e name and type) 
 * from a query string
 * 
 * @author Gioia
 */
public interface IFieldsReader {
	 public Vector readFields() throws Exception;
}
