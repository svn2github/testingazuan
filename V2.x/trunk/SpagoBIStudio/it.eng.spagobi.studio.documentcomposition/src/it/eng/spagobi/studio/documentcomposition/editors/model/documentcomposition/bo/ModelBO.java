package it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.model.Model;
import it.eng.spagobi.studio.documentcomposition.util.XmlTemplateGenerator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class ModelBO {

	public Model createModel(IFile file) throws CoreException{
		Model model = XmlTemplateGenerator.readXml(file);
		return model;
	}

	public void saveModel(Model model){
		Activator.getDefault().setModel(model);
	}

	public Model getModel(){
		return Activator.getDefault().getModel();
	}
}
