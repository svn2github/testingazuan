/**

 SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/

package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelNode;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	
	List<IModelEntity> roots = null;
	
	public ViewContentProvider(List<IModelEntity> roots){
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
			children.addAll(((IModelEntity) parent).getAllFields());
			children.addAll(((IModelEntity) parent).getCalculatedFields());
			children.addAll(((IModelEntity) parent).getSubEntities());
		}else if(parent instanceof List){
			return roots.toArray(new IModelEntity[0]);
		}
		return children.toArray(new IModelNode[0]);
	}
	public boolean hasChildren(Object parent) {
		return getChildren(parent).length>0;
	}
}