package it.eng.spagobi.studio.core.util;

import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import it.eng.spagobi.sdk.documents.bo.SDKFunctionality;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class SdkFunctionalityTreeGenerator {

	Tree tree=null;
	Display display=null;
	Composite container=null;
	ImageDescriptor folderDescriptor=null;
	ImageDescriptor treeBaseDescriptor=null;

	HashMap<String, ImageDescriptor> imageDescriptors=null;


	public Tree generateTree(Composite parent,SDKFunctionality func){
		container=parent;
		folderDescriptor=ImageDescriptorGatherer.getImageDesc("folder.gif");
		treeBaseDescriptor=ImageDescriptorGatherer.getImageDesc("treebase.gif");
		initializeField();


		tree = new Tree(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		TreeItem root=new TreeItem(tree,SWT.SINGLE);
		root.setText("Functionalities");
		root.setImage(treeBaseDescriptor.createImage());
		createItemsList(root, func);

		return tree;
	}

	// Private recursive method that adds documents in tree
	// builds the tree with TreeItem named parent as root
	private void createItemsList(TreeItem parent,SDKFunctionality func){

		SDKFunctionality[] sdkFuncArray=func.getContainedFunctionalities();


		if(sdkFuncArray!=null){
			for (SDKFunctionality functionality : sdkFuncArray) {
				TreeItem currItem=new TreeItem(parent,SWT.SINGLE);
				currItem.setText(functionality.getName());
				currItem.setData(functionality);

				currItem.setImage(folderDescriptor.createImage());
				createItemsList(currItem, functionality);

				SDKDocument[] sdkDocuments=functionality.getContainedDocuments(); 
				if(sdkDocuments!=null){
					for (SDKDocument document : sdkDocuments) {
						createDocumentsList(currItem, document);
					}
				}
			}
		}


	}

	private void createDocumentsList(TreeItem parent,SDKDocument doc){
		TreeItem currDoc=new TreeItem(parent,SWT.CHECK);
		currDoc.setText(doc.getName());
		currDoc.setData(doc);
		if(doc.getType()!=null){
			ImageDescriptor descriptor=imageDescriptors.get(doc.getType().toUpperCase());
			if(descriptor!=null){
				currDoc.setImage(descriptor.createImage());
			}
		}
	}

	private void initializeField(){
		imageDescriptors=new HashMap<String, ImageDescriptor>();		
		ImageDescriptor reportDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_REPORT.png");
		ImageDescriptor olapDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_OLAP.png");
		ImageDescriptor dashDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_DASH.png");
		ImageDescriptor geoDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_MAP.png");
		ImageDescriptor officeDocumentDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_OFFICE_DOC.png");
		ImageDescriptor etlDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_ETL.png");
		ImageDescriptor qbeDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_DATAMART.png");
		ImageDescriptor dossierDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_DOSSIER.png");
		ImageDescriptor compositeDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_DOCUMENT_COMPOSITE.png");
		ImageDescriptor kpiDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_KPI.png");
		ImageDescriptor dataMiningDescriptor=ImageDescriptorGatherer.getImageDesc("objecticon_DATA_MINING.png");

		imageDescriptors.put("REPORT", reportDescriptor);
		imageDescriptors.put("OLAP", olapDescriptor);
		imageDescriptors.put("DASH", dashDescriptor);
		imageDescriptors.put("MAP", geoDescriptor);
		imageDescriptors.put("OFFICE_DOC", officeDocumentDescriptor);
		imageDescriptors.put("ETL", etlDescriptor);
		imageDescriptors.put("DOSSIER", dossierDescriptor);
		imageDescriptors.put("COMPOSITE", compositeDescriptor);
		imageDescriptors.put("DATAMART", qbeDescriptor);
		imageDescriptors.put("KPI", kpiDescriptor);
		imageDescriptors.put("DATA_MINING", kpiDescriptor);



	}


}
