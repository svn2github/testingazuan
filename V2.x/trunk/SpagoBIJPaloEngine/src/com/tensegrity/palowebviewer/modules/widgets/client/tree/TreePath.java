package com.tensegrity.palowebviewer.modules.widgets.client.tree;


/**
 * Represents path in the {@link ITreeModel}.
 *
 */
public class TreePath
{

    private final Object[] path;

    public TreePath() {
        this(new Object[0]);
    }

    public TreePath(Object singlePath){
        this(new Object[]{singlePath});
    }

    public TreePath(Object[] path) {
        this(path, path.length);
    }

    protected TreePath(Object[] path, int length) {
        if(path == null)
            throw new IllegalArgumentException("Path was null");
        if(length <0)
            throw new IllegalArgumentException("Length <0 ("+length+")");
        this.path = new Object[length];
        copyPathArray(path, length); 
    }

	private void copyPathArray(Object[] path, int length) {
		for( int i = 0 ; i < length ; i++ ) { 
            if(path[i]==null) {
                String message = "Path element("+i+") was null.";
                throw new IllegalArgumentException(message);
            }
            this.path[i] = path[i];
        }
	}

    public boolean equals(TreePath aTreePath) {
        if(aTreePath == null)
            return false;
        boolean result = true;
        final int size = getPathCount();
        result &= size == aTreePath.getPathCount();
        for( int i = 0 ; (i < size) && result ; i++ ) { 
            Object elemen1 = getPathComponent(i);
            Object elemen2 = aTreePath.getPathComponent(i);
            result &= elemen1.equals(elemen2);
        } 
        return result;
    }

    public boolean equals(Object o) {
        if(o instanceof TreePath)
            return equals((TreePath)o);
        else
            return false;
    }

    public Object getLastPathComponent() {
        final int size = getPathCount();
        return getPathComponent(size-1);
    }

    public TreePath getParentPath() {
        return new TreePath(path, path.length-1);
    }

    public Object[] getPath() {
        final Object[] result = new Object[getPathCount()];
        for( int i = 0 ; i < result.length ; i++ ) { 
            result[i] = getPathComponent(i);
        } 
        return result;
    }

    public Object getPathComponent(int element) {
        try {
            return path[element];
        }
        catch(IndexOutOfBoundsException ie) {
            throw new IllegalArgumentException("Index out of bounds (index="+element+", length="+path.length+")");
        }
    }

    public int getPathCount() {
        return path.length;
    }

    public int hashCode() {
        return path.length;
    }

    public boolean isDescendant(TreePath aTreePath) {
        boolean result = true;
        int size = getPathCount();
        if(aTreePath.getPathCount() < size)
            result = false;

        for( int i = 0 ; i < size && result  ; i++ ) { 
            Object element1 = getPathComponent(i);
            Object element2 = aTreePath.getPathComponent(i);
            result = element1.equals(element2);
        } 
        return result;
    }

    public TreePath pathByAddingChild(Object child) {
        int size = getPathCount();
        Object[] newPath = new Object[size + 1];
        for( int i = 0 ; i < size ; i++ ) { 
            newPath[i] = getPathComponent(i);
        } 
        newPath[size] = child;
        return new TreePath(newPath);
    }

    public String toString() {
        final int size = getPathCount();
        String result = "TreePath[";
        if(size >0)
            result += getPathComponent(0);
        for( int i = 1 ; i < size ; i++ ) { 
            result += ", " + getPathComponent(i);
        } 
        result += "]";
        return result;
    }

}
