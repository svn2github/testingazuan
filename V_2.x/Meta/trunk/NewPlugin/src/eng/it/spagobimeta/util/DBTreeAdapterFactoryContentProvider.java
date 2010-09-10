package eng.it.spagobimeta.util;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.datatools.modelbase.sql.schema.Catalog;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
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
	    	return model.getTables().toArray();
	    }
	    
	    if(parentElement instanceof PhysicalTable) {
	        PhysicalTable table = (PhysicalTable)parentElement;
		    return table.getColumns().toArray();
		}
	    
	    if(parentElement instanceof PhysicalColumn) {
	    	PhysicalColumn col = (PhysicalColumn)parentElement;
			return new Object[]{ "type: "+col.getTypeName() };
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

	
	
	
}
