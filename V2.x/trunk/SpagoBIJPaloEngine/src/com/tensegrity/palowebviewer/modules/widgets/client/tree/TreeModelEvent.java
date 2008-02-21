package com.tensegrity.palowebviewer.modules.widgets.client.tree;



/**
 * Represents event of {@link ITreeModel}.
 *
 */
public class TreeModelEvent
{

    private final ITreeModel source;
    private final int[] childIndices;
    private final TreePath path;
    private final Object[] children;

    public TreeModelEvent(ITreeModel source, Object[] path) {
        this(source, new TreePath(path));
    }

    public TreeModelEvent(ITreeModel source, Object[] path, int[] childIndices, Object[] children) {
        this(source, new TreePath(path), childIndices, children);
    }

    public TreeModelEvent(ITreeModel source, TreePath path) {
        this(source, path, new int[0], new Object[0]);
    }

    public TreeModelEvent(ITreeModel source, TreePath path, int[] childIndices, Object[] children) {
        if(source == null)
            throw new IllegalArgumentException("Source was null");
        this.source = source;
        this.path = path;
        this.childIndices = childIndices;
        this.children = children;
    }

    public int[] getChildIndices() {
        return childIndices;
    }

    public Object[] getChildren() {
        return children;
    }

    public Object[] getPath() {
    	Object[] result = null;
    	if(path != null)
    		result = path.getPath();
    	return result;
    }
    
    
    //TODO: move it in a more general place
    private static boolean equals(int[] array1, int[] array2){
    	boolean result = false;
        if(array1 == null){
        	result = array2 == null;
        }
        else if ( array2 != null){
        	 result = array1.length == array2.length; 
        	for (int i = 0; (i < array1.length) && result; i++) {
        		result = array1[i] == array2[i];
			}
        }
        return result;
    	
    }

    //TODO: move it in a more general place
    private static boolean equals(Object[] array1, Object[] array2){
    	boolean result = false;
        if(array1 == null){
        	result = array2 == null;
        }
        else if ( array2 != null){
        	 result = array1.length == array2.length; 
        	for (int i = 0; (i < array1.length) && result; i++) {
        		Object o1 = array1[i];
        		Object o2 = array2[i];
        		result = equals(o1, o2);
			}
        }
        return result;
    }
    
    private static boolean equals(Object o1, Object o2) {
    	return o1!= null ? o1.equals(o2) : o2 == null;
    }

    
    public boolean equals(TreeModelEvent event) {
        boolean result = event != null;
        if(result) {
            result = source.equals(event.source);
            result &= path!= null ? path.equals(event.path) : event.path == null;
        	result &= equals(childIndices, event.childIndices);
        	result &= equals(children, event.children);
        }
        return result;
    }

    public boolean equals(Object o) {
        if(o instanceof TreeModelEvent)
            return equals((TreeModelEvent)o);
        else
            return false;
    }

    public ITreeModel getSource() {
        return source;
    }

    public TreePath getTreePath() {
        return path;
    }

    public String toString() {
        String result = "TreeModelEvent[";
        result += "source = " + source;
        result += ", ";
        result += "path = " + path;
        result += ", ";
        result += childIndices;
        result += "childIndices = " + childIndices;
        result += ", ";
        result += "childen = " + children;
        result += "]";
        return result;
    }

}
