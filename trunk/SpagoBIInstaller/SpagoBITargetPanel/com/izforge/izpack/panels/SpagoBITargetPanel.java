package com.izforge.izpack.panels;

import java.io.File;

import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.util.AbstractUIHandler;
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
			if (OsVersion.IS_WINDOWS) setExistFiles(new String [] {"bin" + File.separator + "run.bat", 
					"server" + File.separator + "default" + File.separator + "deploy" + File.separator + "exoplatform.sar"});
			else setExistFiles(new String [] {"bin" + File.separator + "run.sh", 
					"server" + File.separator + "default" + File.separator + "deploy" + File.separator + "exoplatform.sar"});
		} else if ("jonas".equalsIgnoreCase(serverType)) {
			if (OsVersion.IS_WINDOWS) setExistFiles(new String [] {"bin" + File.separator + "nt" + File.separator + "jonas.bat", 
					"apps" + File.separator + "autoload" + File.separator + "exoplatform.ear"});
			else setExistFiles(new String [] {"bin" + File.separator + "unix" + File.separator + "jonas", 
					"apps" + File.separator + "autoload" + File.separator + "exoplatform.ear"});
		}
    }
    
    public boolean isValidated()
    {
        // Standard behavior of PathInputPanel.
        if (!super.isValidated()) return (false);
        String serverType = idata.getVariable("SERVER_TYPE");
        String installPath = idata.getInstallPath();
        if (!installPath.endsWith(File.separator)) {
        	installPath += File.separator;
        }
        String spagobiPath = null;
        if ("tomcat".equalsIgnoreCase(serverType)) {
        	spagobiPath = installPath + "webapps" + File.separator + "spagobi";
        } else if ("jboss".equalsIgnoreCase(serverType)) {
        	spagobiPath = installPath + "server" + File.separator + "default" + File.separator + 
        		"deploy" + File.separator + "exoplatform.sar" + File.separator + "spagobi.war";
        } else if ("jonas".equalsIgnoreCase(serverType)) {
        	spagobiPath = installPath + "apps" + File.separator + "autoload" + File.separator + 
    			"exoplatform.ear" + File.separator + "spagobi.war";
        }
        boolean ok = true;
        File path = new File(spagobiPath);
        if (path.exists())
        {
            int res = askQuestion(parent.langpack.getString("installer.warning"), 
            		getI18nStringForClass("spagobi.existing", "SpagoBITargetPanel"),
                    AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_YES);
            ok = res == AbstractUIHandler.ANSWER_YES;
        }
        return ok;
    }

}
