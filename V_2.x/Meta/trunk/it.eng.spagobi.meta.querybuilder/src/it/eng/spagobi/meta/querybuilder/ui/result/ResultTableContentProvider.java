package it.eng.spagobi.meta.querybuilder.ui.result;

import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ResultTableContentProvider implements IStructuredContentProvider {

	public Object[][] getElements(Object inputElement) {
		return DataStoreReader.getResultList((IDataStore) inputElement);
	}

	public void dispose() {	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	}

}