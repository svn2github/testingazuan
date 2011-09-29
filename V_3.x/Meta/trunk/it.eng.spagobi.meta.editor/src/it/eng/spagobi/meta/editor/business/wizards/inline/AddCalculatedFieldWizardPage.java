/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import java.net.URL;
import java.util.List;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.editor.dnd.CalculatedFieldDragSourceListener;
import it.eng.spagobi.meta.editor.dnd.CalculatedFieldDropTargetListener;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;



/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class AddCalculatedFieldWizardPage extends WizardPage {
	private Text txtName;
	private Text textCalculatedField;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	private BusinessColumnSet sourceTable;

	/**
	 * @param pageName
	 */
	protected AddCalculatedFieldWizardPage(String pageName, BusinessColumnSet sourceTable) {
		super(pageName);
		setMessage("Use this wizard to add calculated field.");
		setTitle("Add Calculated Field");
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.calculatedField") );
	    if (image!=null) setImageDescriptor(image);	
	    this.sourceTable = sourceTable;
		
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		
		createGroupHeader(composite, SWT.NONE);
		createGroupCreation(composite, SWT.NONE);
		
		checkPageComplete();
		
	}
	
	public void createGroupHeader(Composite composite, int style){
		Group groupHeader = new Group(composite, style);
		groupHeader.setLayout(new GridLayout(2, false));
		groupHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(groupHeader, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtName = new Text(groupHeader, SWT.BORDER);
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				checkPageComplete();
			}
		});


		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(groupHeader, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type:");
		
		Combo comboType = new Combo(groupHeader, SWT.READ_ONLY);
		comboType.setItems(new String[] {"String", "Number"});
		comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboType.select(0);
	}
	
	public void createGroupCreation(Composite composite, int style){
		Group groupCreation = new Group(composite, style);
		groupCreation.setLayout(new GridLayout(2, false));
		groupCreation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createTree(groupCreation, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		createCalculateFieldText(groupCreation, SWT.NONE);
		
	}
	
	public void createTree(Composite composite, int style){
		ScrolledComposite compositeTree = new ScrolledComposite(composite, style);
		GridData gd_compositeTree = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_compositeTree.widthHint = 130;
		compositeTree.setLayoutData(gd_compositeTree);
		compositeTree.setExpandHorizontal(true);
		compositeTree.setExpandVertical(true);
		
		Tree treeItems = new Tree(compositeTree, SWT.NONE);
		
		TreeItem treeRoot = new TreeItem(treeItems, SWT.NONE);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.calculatedField.tree.root") );
		treeRoot.setImage(imageDescriptor.createImage() );
		treeRoot.setText("Exp. items");
		
		createFieldsNodes(treeRoot,SWT.NONE );
		createFunctionsNodes(treeRoot, SWT.NONE);
		createAggregationFunctionsNodes(treeRoot, SWT.NONE);
		treeRoot.setExpanded(true);

		compositeTree.setContent(treeItems);
		compositeTree.setMinSize(treeItems.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		//set dragSource 
		DragSourceListener dragSourceListener = new CalculatedFieldDragSourceListener(treeItems);
	    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
	    int operations = DND.DROP_MOVE;
	    DragSource source = new DragSource(treeItems, operations);
	    source.setTransfer(types);
	    source.addDragListener(dragSourceListener);
	}
	
	public void createFieldsNodes(TreeItem parentNode, int style){
		TreeItem treeItemFields = new TreeItem(parentNode, style);
		ImageDescriptor imageDescriptorFolder = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.calculatedField.tree.folder") );
		treeItemFields.setImage(imageDescriptorFolder.createImage() );
		treeItemFields.setText("Fields");
		treeItemFields.setExpanded(true);
	
		createFieldsFromColumns(treeItemFields, SWT.NONE);
		
	}
	
	public void createFieldsFromColumns(TreeItem parentNode, int style){
		List<BusinessColumn> businessColumns = sourceTable.getColumns();
		for (BusinessColumn businessColumn : businessColumns ){
			TreeItem treeItem = new TreeItem(parentNode, style);
			ImageDescriptor imageDescriptorFolder = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.calculatedField.tree.field") );
			treeItem.setImage(imageDescriptorFolder.createImage() );
			treeItem.setText(businessColumn.getName());
		}
	}
	
	public void createFunctionsNodes(TreeItem parentNode, int style){
		TreeItem treeItemFunctions = new TreeItem(parentNode, style);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.calculatedField.tree.folder") );

		treeItemFunctions.setImage(imageDescriptor.createImage() );
		treeItemFunctions.setText("Functions");
		
		ImageDescriptor imageDescriptorOperator = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.calculatedField.tree.operator") );
		
		TreeItem treeItemPlus = new TreeItem(treeItemFunctions, SWT.NONE);
		treeItemPlus.setImage(imageDescriptorOperator.createImage() );
		treeItemPlus.setText("+");
		
		TreeItem treeItemMinus = new TreeItem(treeItemFunctions, SWT.NONE);
		treeItemMinus.setImage(imageDescriptorOperator.createImage() );
		treeItemMinus.setText("-");
		
		TreeItem treeItemMultiply = new TreeItem(treeItemFunctions, SWT.NONE);
		treeItemMultiply.setImage(imageDescriptorOperator.createImage() );
		treeItemMultiply.setText("*");
		
		TreeItem treeItemDivision = new TreeItem(treeItemFunctions, SWT.NONE);
		treeItemDivision.setImage(imageDescriptorOperator.createImage() );
		treeItemDivision.setText("/");
		
		TreeItem treeItemOr = new TreeItem(treeItemFunctions, SWT.NONE);
		treeItemOr.setImage(imageDescriptorOperator.createImage() );
		treeItemOr.setText("||");
	}
	
	public void createAggregationFunctionsNodes(TreeItem parentNode, int style){
		TreeItem treeItemAggregationFunction = new TreeItem(parentNode, style);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.calculatedField.tree.folder") );	
		
		treeItemAggregationFunction.setImage(imageDescriptor.createImage() );
		treeItemAggregationFunction.setText("Aggregation");
		
		ImageDescriptor imageDescriptorOperator = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.calculatedField.tree.operator") );
		
		TreeItem treeItemSum = new TreeItem(treeItemAggregationFunction, SWT.NONE);
		treeItemSum.setImage(imageDescriptorOperator.createImage() );
		treeItemSum.setText("SUM");
		
		TreeItem treeItemMin = new TreeItem(treeItemAggregationFunction, SWT.NONE);
		treeItemMin.setImage(imageDescriptorOperator.createImage() );
		treeItemMin.setText("MIN");
		
		TreeItem treeItemMax = new TreeItem(treeItemAggregationFunction, SWT.NONE);
		treeItemMax.setImage(imageDescriptorOperator.createImage() );
		treeItemMax.setText("MAX");
		
		TreeItem treeItemCount = new TreeItem(treeItemAggregationFunction, SWT.NONE);
		treeItemCount.setImage(imageDescriptorOperator.createImage() );
		treeItemCount.setText("COUNT");
		
		TreeItem treeItemCountDistinct = new TreeItem(treeItemAggregationFunction, SWT.NONE);
		treeItemCountDistinct.setImage(imageDescriptorOperator.createImage() );
		treeItemCountDistinct.setText("COUNT_DISTINCT");
		
		TreeItem treeItemAvg = new TreeItem(treeItemAggregationFunction, SWT.NONE);
		treeItemAvg.setImage(imageDescriptorOperator.createImage() );
		treeItemAvg.setText("AVG");

	}
	
	public void createCalculateFieldText(Composite composite, int style){
		Composite compositeCalculatedFieldText = new Composite(composite, style);
		compositeCalculatedFieldText.setLayout(new GridLayout(1, false));
		compositeCalculatedFieldText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createCalculateFieldTextButtons(compositeCalculatedFieldText, SWT.NONE);
		createCalculateFieldTextPanel(compositeCalculatedFieldText, SWT.BORDER | SWT.V_SCROLL);

	}
	
	public void createCalculateFieldTextButtons(Composite composite, int style){
		Composite compositeButtons = new Composite(composite, style);
		compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeButtons.setLayout(new GridLayout(2, false));
		
		Button btnClearAll = new Button(compositeButtons, SWT.NONE);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textCalculatedField.setText("");
			}
		});
		btnClearAll.setText("Clear All");
		
		Button btnValidate = new Button(compositeButtons, SWT.NONE);
		btnValidate.setText("Validate");
	}
	
	public void createCalculateFieldTextPanel(Composite composite, int style){
		textCalculatedField = new Text(composite,SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI );
		textCalculatedField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				checkPageComplete();
			}
		});
		textCalculatedField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		//set dropTarget
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
	    int operations = DND.DROP_MOVE ;
		DropTarget target = new DropTarget(textCalculatedField, operations);
	    DropTargetAdapter dropTargetAdapter =  new CalculatedFieldDropTargetListener(textCalculatedField);
	    target.setTransfer(types);
	    target.addDropListener(dropTargetAdapter);	
	}
	
	public String getTextCalculatedField(){
		return textCalculatedField.getText();
	}

	public String getTxtName() {
		return txtName.getText();
	}
	
	//check if the right conditions to go forward occurred
	public void checkPageComplete(){
		if( (!getTxtName().isEmpty()) && (!getTextCalculatedField().isEmpty()) ){
			setPageComplete(true);
		}
		else{			
			setPageComplete(false);
		}		
	}
	
}
