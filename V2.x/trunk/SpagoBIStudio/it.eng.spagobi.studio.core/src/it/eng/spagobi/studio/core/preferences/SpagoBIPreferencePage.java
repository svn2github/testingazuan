package it.eng.spagobi.studio.core.preferences;

import java.rmi.RemoteException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import it.eng.spagobi.services.proxy.SessionServiceProxy;
import it.eng.spagobi.services.session.exceptions.AuthenticationException;
import it.eng.spagobi.studio.core.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class SpagoBIPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private StringFieldEditor serverUrl = null;
	private StringFieldEditor userName = null;
	private StringFieldEditor userPassword = null;
	
	public SpagoBIPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("SpagoBI Server connection parameters");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		serverUrl = new StringFieldEditor(PreferenceConstants.SPAGOBI_SERVER_URL, "SpagoBI Server url:", getFieldEditorParent());
		addField(serverUrl);
		userName = new StringFieldEditor(PreferenceConstants.SPABOGI_USER_NAME, "User name:", getFieldEditorParent());
		addField(userName);
		userPassword = new StringFieldEditor(PreferenceConstants.SPABOGI_USER_PASSWORD, "Password:", getFieldEditorParent());
		userPassword.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(userPassword);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
	protected void contributeButtons(Composite parent) {
		Button testButton = new Button(parent, SWT.FLAT);
		testButton.setText("Test connection");
		((GridLayout) parent.getLayout()).numColumns++;
		GridData data = new GridData();
		testButton.setLayoutData(data);
		testButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				testConnection();
			};
		});
	}
	
	private void testConnection() {
        SessionServiceProxy proxy = new SessionServiceProxy();
        proxy.setEndpoint(serverUrl.getStringValue() + "/services/WSSessionService");
    	try {
    		// opening a session into SpagoBI Server
    		proxy.openSession(userName.getStringValue(), userPassword.getStringValue());
    		MessageDialog.openInformation(this.getShell(), "", "Connection test successful!");
//    		setErrorMessage(null);
//    		setValid(true);
    	} catch (AuthenticationException e) {
    		MessageDialog.openError(this.getShell(), "", "Authentication failed!");
//    		setErrorMessage("Authentication failed!");
//    		setValid(false);
    	} catch (Exception e) {
    		MessageDialog.openError(this.getShell(), "", "Could not connect to SpagoBI Server!");
//    		setErrorMessage("Could not connect to SpagoBI Server!");
//    		setValid(false);
    	} finally {
    		if (proxy != null) {
    			try {
					proxy.closeSession();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
    		}
    	}
	}
	
}