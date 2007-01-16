/**
 * 
 */
package it.eng.qbe.javascript;

/**
 * Interface of classes that can build a java script dTree object. By contract class
 * of this type have only to create the tree and build its structure. The importation
 * of the dTree.js script and all of css files used to render it should be handled 
 * separatly by the caller. 
 * 
 * @author Gioia
 */
public interface IJsTreeBuilder {
	public String build();
}
