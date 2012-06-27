/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
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