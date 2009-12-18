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

public class SdkSelectFolderTreeGenerator {

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


		tree = new Tree(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL
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
				TreeItem currItem=new TreeItem(parent,SWT.CHECK);
				currItem.setText(functionality.getName());
				currItem.setData(functionality);

				currItem.setImage(folderDescriptor.createImage());
				createItemsList(currItem, functionality);

			}
		}


	}



	
	
	
	
	
}
