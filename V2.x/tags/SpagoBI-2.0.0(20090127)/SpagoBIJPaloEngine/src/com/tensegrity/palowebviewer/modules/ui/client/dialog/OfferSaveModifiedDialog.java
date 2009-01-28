package com.tensegrity.palowebviewer.modules.ui.client.dialog;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OfferSaveModifiedDialog extends DialogBox{
	
	private Label label;
	
	private ArrayList listeners = new ArrayList();
	private String question;
	private final ClickListener yesListener = new ClickListener() {
		public void onClick(Widget sender) {
			fireOnYes();
		}

	};
	private final ClickListener noListener = new ClickListener() {
		public void onClick(Widget sender) {
			fireOnNo();
		}

	};
	private final ClickListener cancelListener = new ClickListener() {
		public void onClick(Widget sender) {
			fireOnCancel();
		}

	};

	
	public OfferSaveModifiedDialog(String question){
		this.question = question;
		buildWidgets();
		
	}
	
	public void addListener(OfferSaveModifiedDialogListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(OfferSaveModifiedDialogListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireOnYes() {
		for (Iterator i = listeners.iterator(); i.hasNext(); ) {
			OfferSaveModifiedDialogListener listener = (OfferSaveModifiedDialogListener)i.next();
			listener.onYes();
		}
	}

	private void fireOnNo() {
		for (Iterator i = listeners.iterator(); i.hasNext(); ) {
			OfferSaveModifiedDialogListener listener = (OfferSaveModifiedDialogListener)i.next();
			listener.onNo();
		}
	}
	
	private void fireOnCancel() {
		for (Iterator i = listeners.iterator(); i.hasNext(); ) {
			OfferSaveModifiedDialogListener listener = (OfferSaveModifiedDialogListener)i.next();
			listener.onCancel();
		}
	}

	private void buildWidgets() {
		label = new Label(question);
		HorizontalPanel paButtons = createButtonPanel();
		
		FlexTable table = new FlexTable();
		table.setStyleName("input_form");
		
		table.setWidget(0, 0, label);
		table.setWidget(1, 0, paButtons);
		
		setWidget(table);
	}

	private HorizontalPanel createButtonPanel() {
		Button btYes = createYesButton();
		Button btNo = createNoButton();
		Button btCancel = createCancelButton();
		

		
		HorizontalPanel paButtons = new HorizontalPanel();
		paButtons.setSpacing(3);
		paButtons.add(btYes);
		paButtons.add(btNo);
		paButtons.add(btCancel);
		return paButtons;
	}

	private Button createCancelButton() {
		Button btCancel = new Button("Cancel");
		btCancel.setStyleName("button");

		btCancel.addClickListener(cancelListener);
		return btCancel;
	}

	private Button createNoButton() {
		Button btNo = new Button("No");
		btNo.setStyleName("button");
		btNo.addClickListener(noListener);
		return btNo;
	}

	private Button createYesButton() {
		Button btYes = new Button("Yes");
		btYes.setStyleName("button");

		btYes.addClickListener(yesListener);
		return btYes;
	}
}
