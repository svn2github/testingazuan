package it.eng.spagobi.studio.geo.wizards;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class SpagoBIGEOWizard extends Wizard implements INewWizard{
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	// the workbench instance
	protected IWorkbench workbench;
	
	public static final String GEO_INFO_FILE = "it/eng/spagobi/studio/geo/resources/new_template.sbigeo";
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New GEO document creation");
		this.workbench = workbench;
		this.selection = selection;
		
	}
	public static void flushFromInputStreamToOutputStream(InputStream is, OutputStream os, 
			boolean closeStreams) throws Exception  {
		try{	
			int c = 0;
			byte[] b = new byte[1024];
			while ((c = is.read(b)) != -1) {
				if (c == 1024)
					os.write(b);
				else
					os.write(b, 0, c);
			}
			os.flush();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			if (closeStreams) {
				try {
					if (os != null) os.close();
					if (is != null) is.close();
				} catch (IOException e) {
					throw e;
				}

			}
		}
	}
}
