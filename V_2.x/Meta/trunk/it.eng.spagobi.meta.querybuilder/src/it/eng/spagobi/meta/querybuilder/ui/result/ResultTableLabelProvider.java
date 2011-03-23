package it.eng.spagobi.meta.querybuilder.ui.result;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ResultTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	public String getColumnText(Object element, int columnIndex) {
		String[] record = (String[])element;
		return record[(columnIndex)];
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}