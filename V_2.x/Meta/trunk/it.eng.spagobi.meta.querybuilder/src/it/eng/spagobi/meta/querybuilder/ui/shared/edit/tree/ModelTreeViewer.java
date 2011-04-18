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

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ModelTreeViewer extends TreeViewer {

	private static Logger logger = LoggerFactory.getLogger(ModelTreeViewer.class);
	private QueryBuilder queryBuilder;
	
	/**
	 * Initialize the tree
	 * @param parent
	 */
	public ModelTreeViewer(Composite parent, IDataSource dataSource, IModelStructure modelView, QueryBuilder queryBuilder){
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		this.queryBuilder = queryBuilder;
		logger.debug("IN: initializing the datamartTree");
		List<IModelEntity> roots = getModelViewRootEntities(modelView);
		setContentProvider(new ViewContentProvider(roots));
		setLabelProvider(new ViewLabelProvider(this,queryBuilder.getLabelProvider()));
		setInput(roots);
		logger.debug("OUT: datamartTree initialized");
		addDoubleClickListener(new DoubleClickListener());
	}
	
	/**
	 * Get the root entities 
	 * @param modelView
	 * @return
	 */
	private List<IModelEntity> getModelViewRootEntities(IModelStructure modelView){
		logger.debug("IN: Getting the datamart structure roots");
		Iterator<String> modelNamesIter = modelView.getModelNames().iterator();
		// TODO GENERICO PER PIU DATAMART
		String modelName = modelNamesIter.next();
		List<IModelEntity> modelViewEntities = modelView.getRootEntities(modelName);	
		List<IModelEntity> modelViewFilteredEntities = new ArrayList<IModelEntity>();
	
		modelViewFilteredEntities = modelViewEntities;

		return modelViewFilteredEntities;
	}
	
	/**
	 * This class is a listener for the double action in the tree entities
	 * 
	 * @author Alberto Ghedin (alberto.ghedin@eng.it)
	 *
	 */
	private class DoubleClickListener implements IDoubleClickListener{
		public void doubleClick(DoubleClickEvent event) {
			//if the user click 2 times over a node..
			//the node is added in the select table
			Object selectedElement = ((TreeSelection)event.getSelection()).getFirstElement();
			queryBuilder.addSelectFields(selectedElement);
			queryBuilder.refreshSelectFields();
		}
	}

		
}
