package it.eng.spagobi.meta.datamarttree.tree;

import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.spagobi.meta.datamarttree.builder.DatamartSrtuctureBuilder;

import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DatamartTree extends TreeViewer{

	public DatamartTree(Composite parent){
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		DataMartEntity invisibleRoot = getDatamartStructureRoot();
		setContentProvider(new ViewContentProvider());
		setLabelProvider(new ViewLabelProvider(this));
		setInput(invisibleRoot);

	}
	
	private DataMartEntity getDatamartStructureRoot(){
		DataMartModelStructure datamartStructure = DatamartSrtuctureBuilder.build();
		
		List<DataMartEntity> datamartFields = datamartStructure.getRootEntities("foodmart");
			
		DataMartEntity root = new DataMartEntity("datamart", "path", "role", "type", datamartStructure);
		for(int i=0; i<datamartFields.size(); i++){
			root.addSubEntity(datamartFields.get(i));
		}
					
		DataMartEntity invisibleRoot = new DataMartEntity("datamart", "path", "role", "type", datamartStructure);
		invisibleRoot.addSubEntity(root);
		
		return invisibleRoot;
	}
	
}
