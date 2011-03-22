package it.eng.spagobi.meta.datamarttree.tree;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.ViewModelEntity;
import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.IStatement;
import it.eng.spagobi.meta.datamarttree.builder.DatamartSrtuctureBuilder;
import it.eng.spagobi.meta.datamarttree.tree.i18n.ModelLabelProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatamartTree extends TreeViewer{

	private static Logger logger = LoggerFactory.getLogger(DatamartTree.class);
	
	private ViewModelStructure datamartStructure;
	
	/**
	 * Initialize the tree
	 * @param parent
	 */
	public DatamartTree(Composite parent){
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		logger.debug("IN: initializing the datamartTree");
		datamartStructure = DatamartSrtuctureBuilder.build();
		List<ViewModelEntity> roots = getDatamartStructureRoot(datamartStructure);
		setContentProvider(new ViewContentProvider(roots));
		setLabelProvider(new ViewLabelProvider(this,new ModelLabelProvider(datamartStructure.getDataSource())));
		setInput(roots);
		logger.debug("OUT: datamartTree initialized");
	}
	
	/**
	 * Get the root entities 
	 * @param datamartStructure
	 * @return
	 */
	private List<ViewModelEntity> getDatamartStructureRoot(ViewModelStructure datamartStructure){
		logger.debug("IN: Getting the datamart structure roots");
		Iterator<String> modelNamesIter = datamartStructure.getModelNames().iterator();
		// TODO GENERICO PER PIU DATAMART
		List<IModelEntity> datamartEntity = datamartStructure.getRootEntities(modelNamesIter.next());	
		List<ViewModelEntity> datamartFilterdedEntity = new ArrayList<ViewModelEntity>();
		
		for(int i=0; i<datamartEntity.size(); i++){
			datamartFilterdedEntity.add(new ViewModelEntity (datamartEntity.get(i), datamartStructure.getDataSource(), datamartStructure.getQbeTreeFilter()));
		}
		logger.debug("IN: Datamart structure roots loaded");
		return datamartFilterdedEntity;
	}

	public ViewModelStructure getDatamartStructure() {
		return datamartStructure;
	}
		
}
