package it.eng.spagobi.meta.editor.util;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

public class DBTreeAdapterFactoryContentProvider extends AdapterFactoryContentProvider{
	private static Object[] EMPTY_ARRAY = new Object[0];
	
	public DBTreeAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	@Override
	public Object [] getElements(Object inputElement) {
		if(inputElement instanceof PhysicalModel) {
			return getChildren(inputElement);
		}
		return null;
	}
	@Override
	public Object [] getChildren(Object parentElement) {
	    if(parentElement instanceof PhysicalModel) {
	    	PhysicalModel model = (PhysicalModel)parentElement;
	    	return concat(model.getTables().toArray(), 
	    			model.getPrimaryKeys().toArray(),
	    			model.getForeignKeys().toArray());
	    }
	    
	    if(parentElement instanceof PhysicalTable) {
	        PhysicalTable table = (PhysicalTable)parentElement;
	        if (table.getPrimaryKey() != null)
	        	return concat(table.getColumns().toArray(), new Object[]{table.getPrimaryKey()} ,table.getForeignKeys().toArray());
	        else
	        	return concat(table.getColumns().toArray(), table.getForeignKeys().toArray()); 
		}
	    
	    if(parentElement instanceof PhysicalColumn) {
	    	PhysicalColumn col = (PhysicalColumn)parentElement;
			return new Object[]{ "Type: "+col.getTypeName(), "Size: "+col.getSize()};
	    }
	    if(parentElement instanceof PhysicalPrimaryKey) {
	    	PhysicalPrimaryKey pk = (PhysicalPrimaryKey)parentElement;
			return pk.getColumns().toArray();
	    }
	    if(parentElement instanceof PhysicalForeignKey) {
	    	PhysicalForeignKey fk = (PhysicalForeignKey)parentElement;
			return concat(fk.getSourceColumns().toArray(),fk.getDestinationColumns().toArray());
	    }
	    return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {

	    if(element instanceof PhysicalTable) {
	        return ((PhysicalTable)element).getModel();
	    }	
	    if(element instanceof PhysicalColumn) {
	        return ((PhysicalColumn)element).getTable();
	    }	
	    return null;
	}	
	
	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}
	
	protected Object[] concat(Object[] object, Object[] more, Object[] more2) {
		Object[] both = new Object[object.length + more.length + more2.length];
		System.arraycopy(object, 0, both, 0, object.length);
		System.arraycopy(more, 0, both, object.length, more.length);
		System.arraycopy(more2, 0, both, object.length + more.length, more2.length);		
		return both;
	}
	protected Object[] concat(Object[] object, Object[] more) {
		Object[] both = new Object[object.length + more.length];
		System.arraycopy(object, 0, both, 0, object.length);
		System.arraycopy(more, 0, both, object.length, more.length);	
		return both;
	}	
	
	
}
