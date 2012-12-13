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
package it.eng.spagobi.meta.editor.olap.editor.hierarchies;

import java.util.List;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.HierarchyDescriptor;
import it.eng.spagobi.meta.initializer.descriptor.HierarchyLevelDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.Hierarchy;
import it.eng.spagobi.meta.model.olap.OlapModel;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class HierarchyEditor extends Dialog {
	private Table tableAttributes;
	private Table tableHierarchy;
	private Hierarchy hierarchy;
	private Dimension dimension; 
	private BusinessColumnSet businessColumnSet;
	//private Combo comboPrimarykey ;
	private BusinessColumn selectedAttribute;
	
	
	private List<BusinessColumn> dimensionAttributes; 
	private Text textHierarchyName;
	HierarchyDescriptor hyerarchyDescriptor;
	private int levelCounter;
	
	
	public static final int COLUMN_REMOVE = 0;
	public static final int COLUMN_LEVEL_ORDER = 1;
	public static final int COLUMN_LEVEL_NAME = 2;
	public static final int COLUMN_COLUMN = 3;
	public static final int COLUMN_DESCRIPTION = 4;
	public static final int COLUMN_NAMECOLUMN = 5;
	public static final int COLUMN_CAPTIONCOLUMN = 6;
	public static final int COLUMN_UNIQUEMEMBERS = 7;



	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	//New Hierarchy
	/**
	 * @wbp.parser.constructor
	 */
	public HierarchyEditor(Shell parentShell, OlapModel olapModel, BusinessColumnSet businessColumnSet, OlapModelInitializer olapModelInitializer) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.PRIMARY_MODAL);
		hierarchy = null;
		dimension = olapModelInitializer.getDimension(businessColumnSet);
		this.businessColumnSet = businessColumnSet;
		hyerarchyDescriptor = new HierarchyDescriptor();
		hyerarchyDescriptor.setName(businessColumnSet.getName());
	}
	
	//Edit Existing Hierarchy
	public HierarchyEditor(Shell parentShell, OlapModel olapModel, BusinessColumnSet businessColumnSet, OlapModelInitializer olapModelInitializer, Hierarchy hierarchy) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.hierarchy = hierarchy;
		dimension = olapModelInitializer.getDimension(businessColumnSet);
		this.businessColumnSet = businessColumnSet;
		
		//TODO: convert from Hierarchy object to Hierarchy descriptor

	}
	
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Hierarchy Editor");
	}	

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(container, SWT.NONE);
		compositeDescription.setLayout(new GridLayout(1, false));
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblWindowDescription = new Label(compositeDescription, SWT.NONE);
		GridData gd_lblWindowDescription = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblWindowDescription.widthHint = 787;
		lblWindowDescription.setLayoutData(gd_lblWindowDescription);
		lblWindowDescription.setText("Drag and Drop from the Attributes Table to the Hierarchy table to add a new level to your Hierarchy.");
		
		Label lblTheLevelNumbers = new Label(compositeDescription, SWT.NONE);
		lblTheLevelNumbers.setText("The level numbers are 1 (Top Level) to N (Bottom level).");
		
		Group grpGeneralHierarchyProperties = new Group(container, SWT.NONE);
		grpGeneralHierarchyProperties.setLayout(new GridLayout(4, false));
		grpGeneralHierarchyProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpGeneralHierarchyProperties.setText("General Hierarchy Properties");
		
		Label lblName = new Label(grpGeneralHierarchyProperties, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		textHierarchyName = new Text(grpGeneralHierarchyProperties, SWT.BORDER);
		GridData gd_textHierarchyName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textHierarchyName.widthHint = 167;
		textHierarchyName.setLayoutData(gd_textHierarchyName);
		//set initial value
		textHierarchyName.setText(hyerarchyDescriptor.getName());
		
		Label lblHasAll = new Label(grpGeneralHierarchyProperties, SWT.NONE);
		lblHasAll.setText("Has All");
		
		Button buttonHasAll = new Button(grpGeneralHierarchyProperties, SWT.CHECK);
		buttonHasAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		/*
		Label lblPrimaryKey = new Label(grpGeneralHierarchyProperties, SWT.NONE);
		lblPrimaryKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPrimaryKey.setText("Primary Key");
		
		comboPrimarykey = new Combo(grpGeneralHierarchyProperties, SWT.NONE);
		GridData gd_comboPrimarykey = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboPrimarykey.widthHint = 126;
		comboPrimarykey.setLayoutData(gd_comboPrimarykey);
		*/
		
		Group groupMain = new Group(container, SWT.NONE);
		groupMain.setLayout(new GridLayout(2, false));
		groupMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpAttributes = new Group(groupMain, SWT.NONE);
		grpAttributes.setText("Attributes");
		grpAttributes.setLayout(new GridLayout(1, false));
		grpAttributes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		tableAttributes = new Table(grpAttributes, SWT.BORDER | SWT.FULL_SELECTION);
		tableAttributes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableAttributes.setHeaderVisible(true);
		tableAttributes.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(tableAttributes, SWT.NONE);
		tblclmnName.setWidth(113);
		tblclmnName.setText("Name");
		
		Group grpHierarchy = new Group(groupMain, SWT.NONE);
		grpHierarchy.setText("Hierarchy");
		grpHierarchy.setLayout(new GridLayout(1, false));
		grpHierarchy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableHierarchy = new Table(grpHierarchy, SWT.BORDER | SWT.FULL_SELECTION);
		tableHierarchy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableHierarchy.setHeaderVisible(true);
		tableHierarchy.setLinesVisible(true);
		
		TableColumn tblclmnRemove = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnRemove.setWidth(71);
		tblclmnRemove.setText("Remove");
		
		TableColumn tblclmnLevelOrder = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnLevelOrder.setWidth(72);
		tblclmnLevelOrder.setText("Level Order");
		
		TableColumn tblclmnLevelName = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnLevelName.setWidth(86);
		tblclmnLevelName.setText("Level Name");
		
		TableColumn tblclmnColumn = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnColumn.setWidth(81);
		tblclmnColumn.setText("Column");
		
		TableColumn tblclmnDescription = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnDescription.setWidth(84);
		tblclmnDescription.setText("Description");
		
		TableColumn tblclmnNamecolumn = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnNamecolumn.setWidth(100);
		tblclmnNamecolumn.setText("NameColumn");
		
		TableColumn tblclmnCaptioncolumn = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnCaptioncolumn.setWidth(100);
		tblclmnCaptioncolumn.setText("CaptionColumn");
		
		TableColumn tblclmnUniquemembers = new TableColumn(tableHierarchy, SWT.NONE);
		tblclmnUniquemembers.setWidth(100);
		tblclmnUniquemembers.setText("UniqueMembers");
		
		//******************
		//Populate the primary key combo
		//populatePrimaryKeyCombo();
		//Populate the attributes Table
		populateAttributesTable();
		//Set the drag&drop functionatily from Attribute table to Levels Tabel
		setDragAndDrop();
		

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(825, 550);
	}
	
	
	private void populateAttributesTable(){
		dimensionAttributes = businessColumnSet.getColumns();
		for (BusinessColumn attribute:dimensionAttributes){
			TableItem item = new TableItem(tableAttributes, SWT.NONE);
			item.setText(attribute.getName());
			item.setData(attribute);
		}
	}
	/*
	private void populatePrimaryKeyCombo(){
		List<BusinessColumn> columns = businessColumnSet.getColumns();
		for (BusinessColumn attribute:columns){
			comboPrimarykey.add(attribute.getName());			
		}
	}
	*/
	
	private void setDragAndDrop(){
		//DRAG
		Transfer[] types = new Transfer[] { TextTransfer.getInstance()};
		DragSource source = new DragSource(tableAttributes, DND.DROP_MOVE | DND.DROP_COPY);
		source.setTransfer(types);
		source.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				// Get the selected items in the drag source
				DragSource ds = (DragSource) event.widget;
				Table table = (Table) ds.getControl();
				TableItem[] selection = table.getSelection();

				selectedAttribute = (BusinessColumn) selection[0].getData();
				StringBuffer buff = new StringBuffer(selectedAttribute.getName());

				event.data = buff.toString();
			}
		});
		
		//DROP
		DropTarget target = new DropTarget(tableHierarchy, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
		target.setTransfer(types);

		target.addDropListener(new DropTargetAdapter() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY : DND.DROP_NONE;
				}

				// Allow dropping text only
				for (int i = 0, n = event.dataTypes.length; i < n; i++) {
					if (TextTransfer.getInstance().isSupportedType(event.dataTypes[i])) {
						event.currentDataType = event.dataTypes[i];
					}
				}
			}

			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
			}
			public void drop(DropTargetEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
					// Get the dropped data
					DropTarget target = (DropTarget) event.widget;
					Table table = (Table) target.getControl();
					String data = (String) event.data;
					
					//Create a new item and set columns values
					HierarchyLevelDescriptor hierarchyLevelDescriptor = new HierarchyLevelDescriptor();
					createTableHierarchyItem(tableHierarchy,selectedAttribute,data,hierarchyLevelDescriptor);

					//Remove item from tableAttributes
					removeTableItem(tableAttributes,selectedAttribute);
					//Remove attribute from internal model
					dimensionAttributes.remove(selectedAttribute);

					table.redraw();
				}
			}
		});

	}
	//End Drag&Drop management
	
	public void createTableHierarchyItem(final Table tableHierarchy, BusinessColumn selectedAttribute, String attributeName,final HierarchyLevelDescriptor hierarchyLevelDescriptor){
		//Create internal object corresponding to a level
		hierarchyLevelDescriptor.setBusinessColumn(selectedAttribute);
		hierarchyLevelDescriptor.setName(attributeName);
		hyerarchyDescriptor.getLevels().add(hierarchyLevelDescriptor);
		

		
		//Create a new item and set columns values
		final TableItem item = new TableItem(tableHierarchy, SWT.NONE);
		
		if (selectedAttribute != null){
			item.setData(hierarchyLevelDescriptor);
		}
		
		//*******************
		//create Cell Editor Button Remove 
		TableEditor editor_button_remove = new TableEditor(tableHierarchy);
		final Button buttonRemove = new Button(tableHierarchy, SWT.NONE);
		buttonRemove.setText("Remove");
		editor_button_remove.grabHorizontal = true;
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//find item in summaryPanelPageTableRows
				int index = findItemInLevelsDescriptors(hyerarchyDescriptor.getLevels(),item);
				if (index >= 0){
					
					BusinessColumn businessColumn = hyerarchyDescriptor.getLevels().get(index).getBusinessColumn();
					//Re-add attribute to attributes Table
					TableItem itemAttribute = new TableItem(tableAttributes, SWT.NONE);
					itemAttribute.setText(businessColumn.getName());
					itemAttribute.setData(businessColumn);
					//Re-add attribute to internal model
					dimensionAttributes.add(businessColumn);
					
					
					
					//remove level item from UI
					hyerarchyDescriptor.getLevels().get(index).disposeUiElements();
					//remove level from internal model
					hyerarchyDescriptor.getLevels().remove(index);

				}

				tableHierarchy.redraw();
				
			}
		});
		editor_button_remove.setEditor(buttonRemove,item, COLUMN_REMOVE);
		//*******************

		
		
		item.setText(COLUMN_LEVEL_ORDER,String.valueOf(levelCounter));
		levelCounter++;
		//item.setText(COLUMN_LEVEL_NAME, attributeName);
		
		//*******************
		//create Cell Editor Text Level Name
		TableEditor editor_level_name = new TableEditor(tableHierarchy);
		final Text textLevelName = new Text(tableHierarchy, SWT.NONE);
		textLevelName.setText(attributeName);

		editor_level_name.grabHorizontal = true;
		
		textLevelName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (item != null){
					HierarchyLevelDescriptor hierarchyLevelDescriptor = (HierarchyLevelDescriptor)item.getData();
					hierarchyLevelDescriptor.setName(textLevelName.getText());
				}
			}
		});
		
		editor_level_name.setEditor(textLevelName,item, COLUMN_LEVEL_NAME);
		//**************
		
		
		item.setText(COLUMN_COLUMN,selectedAttribute.getUniqueName());
		
		//*******************
		//create Cell Editor Text Description
		TableEditor editor_description = new TableEditor(tableHierarchy);
		final Text textDescription = new Text(tableHierarchy, SWT.NONE);
		textDescription.setText("");

		editor_description.grabHorizontal = true;
		
		textDescription.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (item != null){
					HierarchyLevelDescriptor hierarchyLevelDescriptor = (HierarchyLevelDescriptor)item.getData();
					hierarchyLevelDescriptor.setDescription(textDescription.getText());				
				}
			}
		});
		
		editor_description.setEditor(textDescription,item, COLUMN_DESCRIPTION);
		//**************		
		
		//*******************
		//create Cell Editor Text NameColumn
		TableEditor editor_nameColumn = new TableEditor(tableHierarchy);
		final Text textNameColumn = new Text(tableHierarchy, SWT.NONE);
		textNameColumn.setText("");

		editor_nameColumn.grabHorizontal = true;
		
		textNameColumn.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (item != null){
					HierarchyLevelDescriptor hierarchyLevelDescriptor = (HierarchyLevelDescriptor)item.getData();
					hierarchyLevelDescriptor.setNameColumn(textNameColumn.getText());				
				}
			}
		});
		
		editor_nameColumn.setEditor(textNameColumn,item, COLUMN_NAMECOLUMN);
		//**************		
		
		//*******************
		//create Cell Editor Text CaptionColumn
		TableEditor editor_captionColumn = new TableEditor(tableHierarchy);
		final Text textCaptionColumn = new Text(tableHierarchy, SWT.NONE);
		textCaptionColumn.setText("");

		editor_captionColumn.grabHorizontal = true;
		
		textCaptionColumn.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (item != null){
					HierarchyLevelDescriptor hierarchyLevelDescriptor = (HierarchyLevelDescriptor)item.getData();
					hierarchyLevelDescriptor.setCaptionColumn(textCaptionColumn.getText());				
				}
			}
		});
		
		editor_captionColumn.setEditor(textCaptionColumn,item, COLUMN_CAPTIONCOLUMN);
		//**************				
		
		//*******************
		//create Cell Editor Combo UniqueMembers
		TableEditor editor_UniqueMembers = new TableEditor(tableHierarchy);
		final CCombo comboUniqueMembers = new CCombo(tableHierarchy, SWT.READ_ONLY);
		comboUniqueMembers.add("false");
		comboUniqueMembers.add("true");

		editor_UniqueMembers.grabHorizontal = true;
		comboUniqueMembers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (item != null){
					HierarchyLevelDescriptor hierarchyLevelDescriptor = (HierarchyLevelDescriptor)item.getData();
					hierarchyLevelDescriptor.setUniqueMembers(Boolean.valueOf(comboUniqueMembers.getText()));				
				}	
				
			}
		});
		editor_UniqueMembers.setEditor(comboUniqueMembers,item, COLUMN_UNIQUEMEMBERS);
		//**************				

		
		
		
		//Save referencens to UI element of this Item inside internal object 
		hierarchyLevelDescriptor.setUi_tableItem(item);
		hierarchyLevelDescriptor.setUi_textLevelName(textLevelName);
		hierarchyLevelDescriptor.setUi_buttonRemove(buttonRemove);
		hierarchyLevelDescriptor.setUi_textDescription(textDescription);
		hierarchyLevelDescriptor.setUi_textNameColumn(textNameColumn);
		hierarchyLevelDescriptor.setUi_textCaptionColumn(textCaptionColumn);
		hierarchyLevelDescriptor.setUi_comboUniqueMembers(comboUniqueMembers);

		tableHierarchy.redraw();

	}
	
	//search a TableItem inside the collection of Levels and if found return the index position of the element
	private int findItemInLevelsDescriptors(List<HierarchyLevelDescriptor> levelsDescriptors, TableItem item){
		int index = -1;

		if (levelsDescriptors != null){
			index = 0;
			boolean found = false;
			for(HierarchyLevelDescriptor element:levelsDescriptors){
				if (element.getUi_tableItem().equals(item)){
					return index;
				} else {
					index++;
				}
			}
			if (!found){
				index= -1;
			}
		}
		return index;

	}

	public void removeTableItem(Table table, BusinessColumn businessColumn){
		TableItem[] tableItems = table.getItems();
		int toRemoveIndex=0;
		boolean itemFound = false;
		
		for (TableItem item:tableItems){
			if (item.getData().equals(businessColumn)){
				itemFound = true;
				break;
			}
			toRemoveIndex++;
		}
		if (itemFound == true){
			table.remove(toRemoveIndex);
		}
	}
}
