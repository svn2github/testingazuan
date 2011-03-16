package it.eng.spagobi.meta.datamarttree.tree;


import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelNode;
import it.eng.qbe.model.structure.ViewModelEntity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	
	List<ViewModelEntity> roots = null;
	
	public ViewContentProvider(List<ViewModelEntity> roots){
		this.roots = roots;
	}
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	public void dispose() {
	}
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}
	public IModelNode getParent(Object child) {
		return ((IModelNode)child).getParent();
	}
	public IModelNode[] getChildren(Object parent) {
		List<IModelNode> children = new ArrayList<IModelNode>();
		if(parent instanceof IModelEntity){
			children.addAll(((ViewModelEntity) parent).getAllFields());
			children.addAll(((ViewModelEntity) parent).getCalculatedFields());
			children.addAll(((ViewModelEntity) parent).getSubEntities());
		}else if(parent instanceof List){
			return roots.toArray(new IModelEntity[0]);
		}
		return children.toArray(new IModelNode[0]);
	}
	public boolean hasChildren(Object parent) {
		return getChildren(parent).length>0;
	}
}