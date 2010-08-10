package com.tensegrity.palowebviewer.modules.ui.client.dialog;

import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.widgets.client.util.GuiHelper;

public class ErrorDialog extends DialogBox {

	private Label message;
	private Button okButton;
    private String messageText;
    private final IErrorDialogCallback callback;
    
    private final ClickListener okButtonListener = new ClickListener () {

		public void onClick(Widget sender) {
			hide();
			if(callback != null) callback.onClose();
		}
    	
    };
    
    private static void show(ErrorDialog dialog){
    	dialog.show();
    	GuiHelper.centerShowDialog(dialog);
    }
    
    public static void showError(Throwable e) {
    	e.printStackTrace();
    	String text = e.getMessage();
    	if(text == null || text.matches("\\s*")) {
    		//TODO: move it to a proper place 
    		if (e instanceof InvocationException) {
    			text = "Problem occured while trying to communicate with server\n";
    			text += "Maybe server is unreachable.";
			}
    		else {
    			text = "" + e;
    		}
    	}
    	show(new ErrorDialog(text, null));
    }
    
    public static void showError(String msg) {
    	show(new ErrorDialog(msg, null));
    }
    
    public static void showError(String msg, IErrorDialogCallback callback) {
    	show(new ErrorDialog(msg, callback));
    }

	private ErrorDialog (String msg, IErrorDialogCallback callback) {
        this.messageText = msg;
        this.callback = callback;
        buildWidgets();
	}
	
	
	protected void buildWidgets() {
		setStyleName("err_form");
		
        Label laTitle = new Label();
        laTitle.setText("Error");
        laTitle.setStyleName("error-title");
        
		Label imgError = new Label();
		imgError.setStyleName("error-icon");
        
        message = new Label(messageText ,true);
        message.setStyleName("error-text");
        
        okButton = createOkButton();
        
        FlexTable table = createLayoutTable(laTitle, imgError);
        
        setWidget(table);
	}

	private FlexTable createLayoutTable(Label laTitle, Label imgError) {
		FlexTable table = new FlexTable();
        table.setHeight("100%");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setWidget(0, 0, laTitle);
        table.setWidget(1, 0, imgError);
        table.setWidget(1, 1, message);
        table.setWidget(2, 0, okButton);
        
        table.getFlexCellFormatter().setHorizontalAlignment(2, 0, HorizontalPanel.ALIGN_CENTER);
        table.getFlexCellFormatter().setColSpan(0,0,2);
        table.getFlexCellFormatter().setColSpan(2,0,2);
		return table;
	}

	private Button createOkButton() {
		Button result = new Button("Ok");
		result.setStyleName("button");
		result.addClickListener(okButtonListener);
		return result;
	}

}
