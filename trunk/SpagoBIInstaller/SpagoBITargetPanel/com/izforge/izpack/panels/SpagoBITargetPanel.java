package com.izforge.izpack.panels;

import java.io.File;

import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.util.OsVersion;

public class SpagoBITargetPanel extends TargetPanel {

	private static final long serialVersionUID = 7719003609915305165L;

	public SpagoBITargetPanel(InstallerFrame parent, InstallData idata) {
        super(parent, idata);
		setMustExist(true);
	}
	
    public void panelActivate() {
        super.panelActivate();
		String serverType = idata.getVariable("SERVER_TYPE");
		if ("tomcat".equalsIgnoreCase(serverType)) {
			if (OsVersion.IS_WINDOWS) setExistFiles(new String [] {"bin" + File.separator + "exo-run.bat"});
			else setExistFiles(new String [] {"bin" + File.separator + "exo-run.sh"});
		} else if ("jboss".equalsIgnoreCase(serverType)) {
			if (OsVersion.IS_WINDOWS) setExistFiles(new String [] {"bin" + File.separator + "run.bat"});
			else setExistFiles(new String [] {"bin" + File.separator + "run.sh"});
		} else if ("jonas".equalsIgnoreCase(serverType)) {
			if (OsVersion.IS_WINDOWS) setExistFiles(new String [] {"bin" + File.separator + "nt" + File.separator + "jonas.bat"});
			else setExistFiles(new String [] {"bin" + File.separator + "unix" + File.separator + "jonas"});
		}
    }

}
