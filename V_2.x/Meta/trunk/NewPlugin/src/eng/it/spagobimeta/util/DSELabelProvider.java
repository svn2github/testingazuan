package eng.it.spagobimeta.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.modelbase.sql.datatypes.SQLDataType;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import eng.it.spagobimeta.Activator;
import eng.it.spagobimeta.views.DBStructureView;

public class DSELabelProvider implements ILabelProvider {

	private Map<ImageDescriptor,Image> imageCache;
	
	public DSELabelProvider(){
		imageCache = new HashMap();
	}
	
	@Override
	public Image getImage(Object element) {	
		ImageDescriptor descriptor = null;

		if (element instanceof Database) {
			descriptor = DBStructureView.getImageDescriptor("database.png");
			// return Activator.getImageDescriptor("icons/database.png").createImage();
		} else if (element instanceof Schema) {
			descriptor = DBStructureView.getImageDescriptor("database.png");
			//return Activator.getImageDescriptor("icons/database.png").createImage();
		} else if (element instanceof Table) {
			descriptor = DBStructureView.getImageDescriptor("table.gif");
			//return Activator.getImageDescriptor("icons/table.gif").createImage();
		} else if (element instanceof Column) {
			if (((Column)element).isPartOfPrimaryKey())
				descriptor = DBStructureView.getImageDescriptor("key.png");
			else
				descriptor = DBStructureView.getImageDescriptor("column.png");
			//return Activator.getImageDescriptor("icons/column.png").createImage();
		} else if (element instanceof SQLDataType) {
			descriptor = DBStructureView.getImageDescriptor("arrow.png");
			//return Activator.getImageDescriptor("icons/arrow.png").createImage();
		} 
		else {
			return null;
		}  
		//obtain the cached image corresponding to the descriptor
		Image image = (Image)imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
		}
		return image;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Database) {
			return ((Database)element).getName();
		} else if (element instanceof Schema) {
			return ((Schema)element).getName();
		} else if (element instanceof Table) {
			return ((Table)element).getName();
		} else if (element instanceof Column) {
			return ((Column)element).getName();
		} else if (element instanceof SQLDataType) {
			return ((SQLDataType)element).getName();
		} 
		else {
			return "N/A";
		}  
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) 
		{
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

}
