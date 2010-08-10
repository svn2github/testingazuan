package it.eng.spago.cms.util;

import it.eng.spago.cms.exceptions.PathNotValidException;

import java.util.StringTokenizer;
import java.util.Vector;



/**
 *
 * Class utility for manipulate the path of the elements.  
 * The methods of the class allow to control the format of the path, 
 * to obtain the Ancestor Path and to perform controls.
 *  
 */
public class Path {

	
	/**
	 * Private Vector of path elements. 
	 * The first element is the root of the branch, the last element is the leaf.
	 */
	private Vector elements;
	
	
	
	/**
	 * Create the pathObject relative to a path string. 
	 * An Example of correct path is: /usr/local/program.
	 * A path must always start with the char '/' but can not end
	 * with it. If path is not in the correct format a  PathNotValidException
	 * will be throw.
	 *  
	 * @param pathStr the string of the path
	 * @return A Path Object represent the string path
	 * @throws PathNotValidException
	 */
	public static Path create(String pathStr) throws PathNotValidException {
		
		pathStr = pathStr.trim();
		
		if(!pathStr.startsWith("/")) {
			throw new PathNotValidException("Path must start with char '/'");
		}
		if(pathStr.endsWith("/") && (pathStr.length() > 1) ) {
			throw new PathNotValidException("Path can't end with char '/'");
		}
		if(pathStr.indexOf("//") != -1) {
			throw new PathNotValidException("Path can't contain string '//'");
		}
		
		Path path = new Path();
		
		StringTokenizer stk = new StringTokenizer(pathStr, "/");
		Vector elems = new Vector();
        
		while(stk.hasMoreTokens()) {
			elems.add(stk.nextToken());
		}
		
		path.setElements(elems);
		return path;
	}
	
	
	/**
	 * Controls if a path it's in the right format.
	 * 
	 * @param pathStr path string to control
	 * @return boolean, true if path havent't a right format, false otherwise
	 */
	public static boolean isMalformedPathString(String pathStr) {
		if(!pathStr.startsWith("/")) {
			return true;
		}
		if(pathStr.endsWith("/") && (pathStr.length() > 1)) {
			return true;
		}
		if(pathStr.indexOf("//") != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Controls the equality of the path with the other in input.  
	 * Two path are equal if they possess the same number of elements, 
	 * and if all the elements are equal.
	 * @param path Path object to compare
	 * @return boolean, true if paths are equals and false otherwise
	 */
	public boolean equals(Path path) {
		
		if(this.elements.size() != path.getElements().size()) {
			return false;
		}
		int size = this.elements.size();
		for(int i=0; i<size; i++) {
			String thisElem = (String)this.elements.get(i);
			String pathElem = (String)path.getElements().get(i);
			if(!thisElem.equals(pathElem)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Control if the path is an ancestor of another.
	 * A path is ancestor of another if all its elements 
	 * are equal and in the same order to those at the begin of the other path.
	 * Example:
	 * "/usr/local/program" is an ancestor of 
	 * "/usr/local/program/myprogram"
	 * 
	 * @param path Path to control 
	 * @return boolean, true if path is ancestor false otherwise
	 */
	public boolean isAncestorOf(Path path) {
		if(this.elements.size() >= path.getElements().size()) {
			return false;
		}
		int size = this.elements.size();
		for(int i=0; i<size; i++) {
			String thisElem = (String)this.elements.get(i);
			String pathElem = (String)path.getElements().get(i);
			if(!thisElem.equals(pathElem)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Control if the path corresponds to the root node of
	 * the content management tree. If the path is relative to
	 * the root path his vector elements is empty.
	 * 
	 * @return boolean, true if path is relative to root node, 
	 * false otherwise
	 */
	public boolean isRootNode() {
		if(this.elements.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Return the path in absolute string format. 
	 * The string path starts with "/" and it's 
	 * the entire path of the node represented from the path.
	 * Example: "/usr/local/program"
	 * 
	 * @return String, the entire absolute path of the node 
	 * represented from the path
	 */
    public String getAbsPathStr() {
    	int size = this.elements.size();
    	String path = "/";
    	for(int i=0; i<size; i++) {
    		path += (String)this.elements.get(i);
    		if(i != (size -1)) {
    			path += "/";
    		}
    	}
    	return path;
    }
	
    /**
     * Returns the path in string format relative to root node.
     * The path string return doesn't starts with "/" and 
     * it's the entire path of the node represented from the path,
     * relative to root node. This method is very useful for retrival
     * nodes, using paths, from root node.
     * Example: "usr/local/program"
     * 
     * @return String, the entire string path of the node represents,
     * relative to root Node.
     */
    public String getRootRelativePathStr() {
    	int size = this.elements.size();
    	String path = "";
    	for(int i=0; i<size; i++) {
    		path += (String)this.elements.get(i);
    		if(i != (size -1)) {
    			path += "/";
    		}
    	}
    	return path;
    }
    
    
    /**
     * Return the name of the last element of the path 
     * that is the name of the node.
     * Example: if the path is "/usr/local/program" the method 
     * will return "program".
     *  
     * @return String, name of the last element of the path 
     * that is the name of the node.
     */
    public String getNameLastElement() {
    	String name = null;
    	int size = this.elements.size();
    	if(size > 0) {
    		name = (String)this.elements.get(size-1);
    	}
    	return name;
    }
    
    /**
     * 
     * Return the object path of an ancestor of the path.
     * The search depth of the ancestors is given 
     * from the parameter in input:
     * 
     * <ul>
     * 	 <li>0 = the same path</li>
     *   <li>1 = the path of the father</li>
     *   <li>2 = the path of the GrandFather</li>
     *   <li>.. and so on ..</li> 
     * </ul>
     * 
     * If the deep search is greater than the number of 
     * elements of the path the method returns null.
     * 
     * @param deep, the number of back steps for find the ancestor
     * @return Path, the object path of the ancestor find or null if
     * non ancestors have been found
     */
    public Path getPathAncestor(int deep) {
    	Path pathAnc = null;
    	int size = this.elements.size();
    	try {
	    	if(deep == size) {
	    		pathAnc = Path.create("/");
	    	} 
	    	else if(deep < size) {
	    		int newSize = size - deep;
	    		String pathAncStr = "";
	    		for(int i=0; i<newSize; i++) {
	    			pathAncStr += "/";
	    			pathAncStr += (String)this.elements.get(i);
				}
	    		pathAnc = Path.create(pathAncStr);
	    	}
    	} 
    	// Exception will not be throw becase the new path string is always right formatted
    	catch (Exception e) {}
    	return pathAnc;
    }
    
    /**
     * Return the Vector of element that compose the path
     * 
     * @return Vector of elements that compose  the path.
     */
	public Vector getElements() {
		return elements;
	}
	
	public void setElements(Vector elements) {
		this.elements = elements;
	}
}
