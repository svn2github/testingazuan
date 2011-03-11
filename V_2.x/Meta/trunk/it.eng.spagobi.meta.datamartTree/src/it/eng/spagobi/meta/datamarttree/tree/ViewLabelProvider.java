package it.eng.spagobi.meta.datamarttree.tree;

import it.eng.qbe.model.structure.AbstractDataMartItem;
import it.eng.qbe.model.structure.DataMartCalculatedField;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

public 	class ViewLabelProvider extends LabelProvider {

	private TreeViewer viewer;
	
	public ViewLabelProvider (TreeViewer viewer){
		this.viewer = viewer;
	}
	
	public String getText(Object obj) {
		return ((AbstractDataMartItem)obj).getName();
	}
	public Image getImage(Object obj) {   
		
		String path = "D:/sviluppo/SpagoBIMeta/workspaceSpagoBIMeta/it.eng.spagobi.meta.datamartTree/img/datamartstructure/";
		
		if(obj instanceof DataMartEntity){
			path=path+"dimension.gif";
		} else if(obj instanceof DataMartField){
			DataMartField dm = (DataMartField)obj;
			path=path+"attribute.gif";
		} else if(obj instanceof DataMartCalculatedField){
			path=path+"calculation.gif";
		}else{
			path=path+"dimension.gif";
		}
		return new Image(viewer.getControl().getDisplay(), path); 
	}
	
}