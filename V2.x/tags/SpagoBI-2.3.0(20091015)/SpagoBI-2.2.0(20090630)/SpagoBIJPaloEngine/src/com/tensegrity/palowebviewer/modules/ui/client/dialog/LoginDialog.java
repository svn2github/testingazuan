package com.tensegrity.palowebviewer.modules.ui.client.dialog;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginDialog extends DialogBox {

	ArrayList listeners;

	FlexTable toolBarTable;

	Label laCause;

	private final TextBox tbLogin = new TextBox();
	private final PasswordTextBox tbPassword = new PasswordTextBox();
	private final CheckBox cbRemember = new CheckBox("Remember me");
	
	private final ClickListener okListener = new ClickListener() {
		
		public void onClick(Widget sender) {
			fireOnOK();
		}

		
	};
	
	private final ClickListener cancelListner = new ClickListener() {
		public void onClick(Widget sender) {
			fireOnCancel();
		}

	};
	
	private final KeyboardListenerAdapter keyboardListenerAdapter = new KeyboardListenerAdapter() {

		public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			// Check for Enter key
			if ((keyCode == KeyboardListener.KEY_ENTER)) {
				fireOnOK();
			}
		}
	};



	public LoginDialog() {
		listeners = new ArrayList();
		buildWidgets();
	}

	public void addListener(LoginDialogListener listener) {
		listeners.add(listener);
	}

	public void removeListener(LoginDialogListener listener) {
		listeners.remove(listener);
	}

	public void setCause(String cause) {
		if (cause == null || "".equals(cause)) {
			laCause.setVisible(false);
		} else {
			laCause.setText(cause);
			laCause.setVisible(true);
		}

	}
	
	protected void fireOnOK() {
		String login = tbLogin.getText();
		String password = tbPassword.getText();
		boolean checked = cbRemember.isChecked();
		tbLogin.setText("");
		tbPassword.setText("");
		for (Iterator it = listeners.iterator(); it.hasNext(); ) {
			LoginDialogListener listener = (LoginDialogListener)it.next();
			listener.onOk(login, password, checked);
		}
	}
	
	protected void fireOnCancel() {
		for (Iterator it = listeners.iterator(); it.hasNext();) {
			LoginDialogListener listener = (LoginDialogListener) it.next();
			listener.onCancel();
		}
	}
	

	private void buildWidgets() {
		laCause = new Label();
		laCause.setVisible(false);

		Button btOk = new Button("Ok");
		btOk.setStyleName("button");
		
		btOk.addClickListener(okListener);

		Button btCancel = new Button("Cancel");
		btCancel.setStyleName("button");

		btCancel.addClickListener(cancelListner);

		HorizontalPanel paButtons = new HorizontalPanel();
		paButtons.setSpacing(3);
		paButtons.add(btOk);
		paButtons.add(btCancel);

		Image imgLogo = new Image();
		imgLogo.setUrl("themes/default/img/log.jpg");
		imgLogo.setHeight("75");
		imgLogo.setWidth("300");

		toolBarTable = new FlexTable();
		toolBarTable.setStyleName("login_form");

		toolBarTable.setWidget(0, 0, imgLogo);

		toolBarTable.setText(1, 0, "Login");
		toolBarTable.setText(2, 0, "Password");

		toolBarTable.setWidget(1, 1, tbLogin);
		toolBarTable.setWidget(2, 1, tbPassword);

		tbLogin.addKeyboardListener(keyboardListenerAdapter);
		tbPassword.addKeyboardListener(keyboardListenerAdapter);
		toolBarTable.setWidget(3, 1, cbRemember);

		toolBarTable.setWidget(4, 0, laCause);

		toolBarTable.setWidget(5, 1, paButtons);
		toolBarTable.getCellFormatter().setStyleName(5, 1, "r_buttons");

		toolBarTable.getFlexCellFormatter().setColSpan(0, 0, 3);
		toolBarTable.getFlexCellFormatter().setColSpan(4, 0, 2);

		setWidget(toolBarTable);
	}

	public void show() {
		super.show();
		tbLogin.setFocus(true);
	}

}
