package it.eng.spagobi.meta.intro.services;


import java.util.Properties;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;


public class PerspectiveOpener implements IIntroAction {




	public void run(IIntroSite site, Properties params) {
		try{
			PlatformUI.getWorkbench().showPerspective("org.eclipse.jst.j2ee.J2EEPerspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow()); 
		}catch(Exception e){
		}
		


	}


} 