/*
 * ContentProvider for the TreeViewer inside the DBStructureView
 */
package eng.it.spagobimeta.util;

import org.eclipse.datatools.modelbase.sql.schema.Catalog;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DSEContentProvider implements ITreeContentProvider {
	private static Object[] EMPTY_ARRAY = new Object[0];

	public DSEContentProvider(){
		super();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
	    if(parentElement instanceof Database) {
	    	//with Oracle we have to access the catalog first to get the schemas
	    	if (((Database)parentElement).getVendor().equals("Oracle"))
	    	{
	    		EList<Catalog> catalogs = ((Database)parentElement).getCatalogs();
	    		Catalog cat = catalogs.get(0);
	    		return cat.getSchemas().toArray();
	    	}
	    	
	    	//other DBMS
	    	Database db = (Database)parentElement;
	    	return db.getSchemas().toArray();
	    }
	    
	    if(parentElement instanceof Schema) {
		   Schema sch = (Schema)parentElement;
		   return sch.getTables().toArray();
		}
	    
	    if(parentElement instanceof Table) {
			   Table tab = (Table)parentElement;
			   return tab.getColumns().toArray();
	    } 
	    if(parentElement instanceof Column) {
			   Column col = (Column)parentElement;
			   return new Object[]{ col.getContainedType() };
	    }
	    return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
	    if(element instanceof Schema) {
	        return ((Schema)element).getDatabase();
	    }
	    if(element instanceof Table) {
	        return ((Table)element).getSchema();
	    }	
	    if(element instanceof Column) {
	        return ((Column)element).getTable();
	    }	
	    return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof Database) {
			return getChildren(inputElement);
		}
		return null;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}
}
