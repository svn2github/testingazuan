package it.eng.spagobi.studio.documentcomposition.views;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class NavigationView extends ViewPart {
	ListViewer viewer;
	IMemento memento;
	Table table;
	
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
	}
	public void init(IViewSite site,IMemento memento) throws PartInitException {
		init(site);
		this.memento = memento;	
	}	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		// Lets make a layout for the first section of the screen
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		// Creating the Screen
		Section section = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		section.setText("Lista navigazione documento composto"); //$NON-NLS-1$
		section.setDescription("Lista navigazioni");
		// Composite for storing the data
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		
		final boolean[] result = new boolean[1];
		/**crea dialog x conferma**/
		final Shell confirm = createConfirmDialog(parent, toolkit, client, result);
		
		
		Button newButton = toolkit.createButton(client, "New", SWT.PUSH); 
		Button deleteButton = createDeleteButton(parent, toolkit, client);
		Button updateButton = toolkit.createButton(client, "Modify", SWT.PUSH);
		
		GridData gd = new GridData(GridData.CENTER);
		gd.widthHint = 50;

		newButton.setLayoutData(gd);
		deleteButton.setLayoutData(gd);
		updateButton.setLayoutData(gd);
		
		
		// Add Button Listener
		Listener deleteListener = new Listener() {
			public void handleEvent(Event event) {
		        switch (event.type) {
		        case SWT.Selection:
		          System.out.println("Delete button pressed");

				  System.out.println("sto x aprire dialog");
				  confirm.open();
				  System.out.println("aperto dialog");

		          break;
		        }

			}
		};
	    
		deleteButton.addListener(SWT.Selection, deleteListener);
		
		/**tabella navigazioni**/
		createTable(parent, toolkit, client);

		
		toolkit.paintBordersFor(client);
		section.setClient(client);
	}

	protected Button createDeleteButton(Composite parent, FormToolkit toolkit, Composite client){
		
		Button deleteButton = toolkit.createButton(client, "Delete", SWT.PUSH | SWT.BORDER_DOT);

		deleteButton.pack();
		return deleteButton;
	}
	protected Shell createConfirmDialog(Composite parent, FormToolkit toolkit, Composite client, final boolean[] result){
		final Shell confirm = new Shell(parent.getDisplay(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		confirm.setLayout(new RowLayout());
/*		Point pt = client.getDisplay().getCursorLocation();
	    confirm.setLocation(pt.x, pt.y);*/
	    final Button ok = new Button(confirm, SWT.PUSH);
	    ok.setText("Confirm");
	    Button cancel = new Button(confirm, SWT.PUSH);
	    cancel.setText("Cancel");
	    confirm.isReparentable();

	    Listener dialogListener = new Listener() {
	        public void handleEvent(Event event) {
	          System.out.println("confirm pressed");
	          result[0] = event.widget == ok;
	          confirm.notifyListeners(event.type, event);
	          confirm.close();
	          System.out.println("risultato::"+result[0]);
			  int selection = table.getSelectionIndex();
			  TableItem tableItem=table.getItem(selection);

			  if(result[0]){
				  tableItem.dispose();
				  System.out.println("Eliminata voce di lista");
			  }
	        }
	      };
	      
	    ok.addListener(SWT.Selection, dialogListener);
	    cancel.addListener(SWT.Selection, dialogListener);
	    return confirm;
		
	}
	protected Table createTable(Composite parent, FormToolkit toolkit, Composite client){
		table = toolkit.createTable(client, SWT.SINGLE  | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 200;
		table.setLayoutData(gd);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		
	    String[] titles = { " Navigation name" };
	    for (int i = 0; i < titles.length; i++) {
	      TableColumn column = new TableColumn(table, SWT.NONE);
	      column.setText(titles[i]);
	    }

	    for (int i = 0; i < 5; i++) {
	      TableItem item = new TableItem(table, SWT.NONE);
	      //item.setText(0, "");
	      item.setText(0, "navigation "+i);
	    }

	    for (int i=0; i<titles.length; i++) {
	      table.getColumn (i).pack ();
	    }  
	    return table;
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */

	public void setFocus() {
		viewer.getControl().setFocus();
	}


}
