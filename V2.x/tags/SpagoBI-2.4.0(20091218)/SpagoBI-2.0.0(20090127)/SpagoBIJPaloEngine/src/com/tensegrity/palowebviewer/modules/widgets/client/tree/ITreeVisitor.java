package com.tensegrity.palowebviewer.modules.widgets.client.tree;


/**
 * Visitor for {@link ITreeModel}. It is used to walk through a tree model.
 *
 */
public interface ITreeVisitor
{
	
	/**
	 * @return true if it does not need to walk trhough the tree anymore, false otherwise.
	 */
	public boolean hasFinished();

    public void visitNode(Object parent, Object node);
    
    public void leaveNode(Object parent, Object node);


}
