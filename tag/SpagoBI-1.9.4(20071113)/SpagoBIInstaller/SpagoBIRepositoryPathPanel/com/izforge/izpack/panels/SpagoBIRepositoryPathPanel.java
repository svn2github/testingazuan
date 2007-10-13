package com.izforge.izpack.panels;

import java.io.File;
import java.util.List;

import com.izforge.izpack.Pack;
import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.util.AbstractUIHandler;

public class SpagoBIRepositoryPathPanel extends PathInputPanel {

	private static final long serialVersionUID = -5415385450445709172L;
	
	private String variableName;
	
	public SpagoBIRepositoryPathPanel(InstallerFrame parent, InstallData idata) {
		super(parent, idata);
		setVariableName("SPAGOBI_REPOSITORY_PATH");
	}

	public boolean isValidated() {
		
        String chosenPath = pathSelectionPanel.getPath();
        boolean ok = true;

        // We put a warning if the specified target is nameless
        if (chosenPath.length() == 0)
        {
            emitError(parent.langpack.getString("installer.error"), parent.langpack
                    .getString("PathInputPanel.required"));
            return false;
        }

        // Normalize the path
        File path = new File(chosenPath).getAbsoluteFile();
        chosenPath = path.toString();
        pathSelectionPanel.setPath(chosenPath);

        // count is the number of files found in the local repository
        int count = 0;
        // selectedPacks is the number of selected packs
        int selectedPacksNum = 0;
        
        if (path.exists()) {
		    List selectedPacks = idata.selectedPacks;
		    for (int i = 0; i < selectedPacks.size(); i++) {
		    	final Pack pack = ((Pack) selectedPacks.get(i));
		    	String remoteFile = pack.remoteFile;
            	if (remoteFile != null && !remoteFile.trim().equals("")) {
            		selectedPacksNum++;
            		int index = remoteFile.lastIndexOf('/') + 1;
                	String filename = remoteFile.substring(index, remoteFile.length());
                	String filepath = chosenPath + File.separator + filename;
                	File existingfile = new File(filepath);
                	if (existingfile.exists()) count++;
            	}
		    }
		    if (count == 0) {
		    	// if the folder is not writable emits an error
	            if (!isWriteable())
	            {
	            	int res = askQuestion(parent.langpack.getString("installer.warning"), 
			    			getI18nStringForClass("notwritable.spagobirepo.warning", 
			    					"SpagoBIRepositoryPathPanel"), 
			    			AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_YES);
	            	ok = res == AbstractUIHandler.ANSWER_YES;
	            	if (ok) {
	            		pathSelectionPanel.setPath(idata.getInstallPath());
	            	}
	                //emitError(parent.langpack.getString("installer.error"), getI18nStringForClass(
	                //        "notwritable.spagobirepo.error", "SpagoBIRepositoryPathPanel"));
	                //return false;
	            } else {
			    	int res = askQuestion(parent.langpack.getString("installer.warning"), 
			    			getI18nStringForClass("empty.spagobirepo.warning", "SpagoBIRepositoryPathPanel"), 
			    			AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_YES);
		            ok = res == AbstractUIHandler.ANSWER_YES;
	            }
		    } else if (count < selectedPacksNum) {
		    	// if the folder is not writable emits an error
	            if (!isWriteable())
	            {
	            	int res = askQuestion(parent.langpack.getString("installer.warning"), 
			    			getI18nStringForClass("notwritable.spagobirepo.warning", 
			    					"SpagoBIRepositoryPathPanel"), 
			    			AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_YES);
	            	ok = res == AbstractUIHandler.ANSWER_YES;
	            	if (ok) {
	            		pathSelectionPanel.setPath(idata.getInstallPath());
	            	}
	                //emitError(parent.langpack.getString("installer.error"), getI18nStringForClass(
	                //        "notwritable.spagobirepo.warning", "SpagoBIRepositoryPathPanel"));
	                //return false;
	            } else {
			    	String message = parent.langpack.getString("SpagoBIRepositoryPathPanel." +
			    			"missingpacks.spagobirepo.warning");
			    	message = message.replaceAll("%0", new Integer(count).toString());
			    	message = message.replaceAll("%1", new Integer(selectedPacksNum).toString());
			    	int res = askQuestion(parent.langpack.getString("installer.warning"), message,
		                    AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_YES);
		            ok = res == AbstractUIHandler.ANSWER_YES;
	            }
		    }
		    
		    if (ok) {
		    	idata.setVariable(getVariableName(), pathSelectionPanel.getPath());
		    	return true;
		    } else return false;
		    
        } else {
        	
            // We assume, that we would install something into this dir
            if (!isWriteable())
            {
            	int res = askQuestion(parent.langpack.getString("installer.warning"), 
		    			getI18nStringForClass("notwritable.spagobirepo.warning", 
		    					"SpagoBIRepositoryPathPanel"), 
		    			AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_YES);
            	ok = res == AbstractUIHandler.ANSWER_YES;
            	if (ok) {
            		pathSelectionPanel.setPath(idata.getInstallPath());
            	}
                //emitError(parent.langpack.getString("installer.error"), getI18nStringForClass(
                //        "notwritable.spagobirepo.warning", "SpagoBIRepositoryPathPanel"));
                //return false;
            } else {
	            int res = askQuestion(parent.langpack.getString("installer.warning"), getI18nStringForClass(
	            		"new.spagobirepo.warning", "SpagoBIRepositoryPathPanel"), 
	            		AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_YES);
	   		    ok = res == AbstractUIHandler.ANSWER_YES;
            }
            
		    if (ok) {
		    	idata.setVariable(getVariableName(), pathSelectionPanel.getPath());
		    	return true;
		    } else return false;
        }

	}
	
    /** Called when the panel becomes active. */
    public void panelActivate()
    {
        // Resolve the default for chosenPath
        super.panelActivate();
        String chosenPath;
        // The variable will be exist if we enter this panel
        // second time. We would maintain the previos
        // selected path.
        if (idata.getVariable(getVariableName()) != null)
            chosenPath = idata.getVariable(getVariableName());
        else
            // Try the USER_HOME as child dir of the jdk path
            chosenPath = (new File(idata.getVariable("USER_HOME") + File.separator + 
            		"spagobi" + File.separator + "repository")).getAbsolutePath();
        // Set the path for method pathIsValid ...
        pathSelectionPanel.setPath(chosenPath);

        if (!pathIsValid()) chosenPath = "";
        
        idata.setVariable(getVariableName(), chosenPath);
        
//        // Set the default to the path selection panel.
//        pathSelectionPanel.setPath(chosenPath);
//        String var = idata.getVariable("JDKPathPanel.skipIfValid");
//        // Should we skip this panel?
//        if (chosenPath.length() > 0 && var != null && "yes".equalsIgnoreCase(var))
//        {
//            idata.setVariable(getVariableName(), chosenPath);
//            parent.skipPanel();
//        }

    }
	
    
    /**
     * Returns the name of the variable which should be used for the path.
     * 
     * @return the name of the variable which should be used for the path
     */
    public String getVariableName()
    {
        return variableName;
    }

    /**
     * Sets the name for the variable which should be set with the path.
     * 
     * @param string variable name to be used
     */
    public void setVariableName(String string)
    {
        variableName = string;
    }
 
    public String getSummaryBody()
    {
        return (idata.getVariable(getVariableName()));
    }
    
}
