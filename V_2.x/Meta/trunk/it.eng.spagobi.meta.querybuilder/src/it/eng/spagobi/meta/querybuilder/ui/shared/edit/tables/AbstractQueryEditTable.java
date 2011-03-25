package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tables;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public abstract class AbstractQueryEditTable extends Composite {
	
	AbstractQueryEditTable(Composite container, int style) {
		super(container, style);
	}

	protected TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber, TableViewer viewer) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;

	}	
}
