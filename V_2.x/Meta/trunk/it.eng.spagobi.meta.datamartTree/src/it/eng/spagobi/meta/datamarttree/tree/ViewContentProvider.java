package it.eng.spagobi.meta.datamarttree.tree;

import it.eng.qbe.model.structure.AbstractDataMartItem;
import it.eng.qbe.model.structure.DataMartEntity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	public void dispose() {
	}
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}
	public AbstractDataMartItem getParent(Object child) {
		return ((AbstractDataMartItem)child).getParent();
	}
	public AbstractDataMartItem[] getChildren(Object parent) {
		List<AbstractDataMartItem> children = new ArrayList<AbstractDataMartItem>();
		if(parent instanceof DataMartEntity){
			children.addAll(((DataMartEntity) parent).getAllFields());
			children.addAll(((DataMartEntity) parent).getCalculatedFields());
			children.addAll(((DataMartEntity) parent).getSubEntities());
		}
		return children.toArray(new AbstractDataMartItem[0]);
	}
	public boolean hasChildren(Object parent) {
		if(parent instanceof DataMartEntity){
			if((((DataMartEntity) parent).getSubEntities()).size()>0)
				return true;
			if((((DataMartEntity) parent).getAllFields()).size()>0)
				return true;
			if((((DataMartEntity) parent).getCalculatedFields()).size()>0)
				return true;
		}
		return false;
	}
}