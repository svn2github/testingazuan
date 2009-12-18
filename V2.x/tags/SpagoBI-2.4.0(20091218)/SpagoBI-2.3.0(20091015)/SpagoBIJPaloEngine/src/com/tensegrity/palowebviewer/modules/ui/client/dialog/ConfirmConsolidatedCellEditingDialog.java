package com.tensegrity.palowebviewer.modules.ui.client.dialog;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class ConfirmConsolidatedCellEditingDialog extends DialogBox{
	
	private final TextBox tbName = new TextBox();
	private final CheckBox cbRemember = new CheckBox("Remember me");
	
	public ConfirmConsolidatedCellEditingDialog(){
		buildWidgets();
	}
	
	private void buildWidgets() {
		
		Button btOk = new Button("Ok");
		Button btCancel = new Button("Cancel");
		
		HorizontalPanel paButtons = new HorizontalPanel();
		paButtons.setSpacing(3);
		paButtons.add(btOk);
		paButtons.add(btCancel);
		
		FlexTable table = new FlexTable();
		
		table.setText(0, 0, "Name");
		table.setText(1, 0, "Description");
		
		table.setWidget(0, 1, tbName);
		table.setWidget(1, 1, cbRemember);
		
		table.setWidget(3, 1, paButtons);
		
		setWidget(table);
	}
}
