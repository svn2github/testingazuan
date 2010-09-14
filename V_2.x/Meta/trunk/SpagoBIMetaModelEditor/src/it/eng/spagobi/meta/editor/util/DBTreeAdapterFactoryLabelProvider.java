package it.eng.spagobi.meta.editor.util;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.swt.graphics.Image;

public class DBTreeAdapterFactoryLabelProvider extends
		AdapterFactoryLabelProvider {

	public DBTreeAdapterFactoryLabelProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the label text 
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof PhysicalTable){
			PhysicalTable table = (PhysicalTable)element;
	        if( table.getName() == null )
	                return "Unnamed table";
	        return table.getName();
		}
		else if (element instanceof PhysicalColumn) {
			PhysicalColumn col = (PhysicalColumn)element;
	        if( col.getName() == null )
	                return "Unnamed column";
	        return col.getTable().getName()+"."+col.getName();
		}		
		else if (element instanceof PhysicalPrimaryKey) {
			PhysicalPrimaryKey pk = (PhysicalPrimaryKey)element;
	        if( pk.getName() == null )
	                return "Unnamed pk";
	        return pk.getName()+" "+pk.getTable().getName();
		}
		else if (element instanceof PhysicalForeignKey) {
			PhysicalForeignKey fk = (PhysicalForeignKey)element;
	        if (fk.getSourceName() != null)
	        	return "Foreign Key "+fk.getSourceName();
	        else return "unamed fk";
		}	
		else if (element instanceof PrimaryKeysFolder) {
			return "Primary Keys";
		}	
		else if (element instanceof ForeignKeysFolder) {
			return "Foreign Keys";
		}		
		else if (element instanceof String) {
			return (String)element;
		}	
		return "";
	}

	/**
	 * This returns the associated image
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof PhysicalTable){
			return Activator.getImageDescriptor("table.gif").createImage();
		}
		else if (element instanceof PhysicalColumn){
			return Activator.getImageDescriptor("column.png").createImage();
		}
		else if (element instanceof PhysicalPrimaryKey){
			return Activator.getImageDescriptor("key.png").createImage();
		}
		else if (element instanceof PhysicalForeignKey){
			return Activator.getImageDescriptor("foreignkey.png").createImage();
		}		
		else if (element instanceof PrimaryKeysFolder) {
			return Activator.getImageDescriptor("folderkey.png").createImage();
		}	
		else if (element instanceof ForeignKeysFolder) {
			return Activator.getImageDescriptor("folderforeignkey.png").createImage();
		}
		else if (element instanceof String) {
			return Activator.getImageDescriptor("field.png").createImage();
		}			
		return null;
	}	
	
}
