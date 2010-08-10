/*
 * IzPack - Copyright 2001-2007 Julien Ponge, All Rights Reserved.
 * 
 * http://www.izforge.com/izpack/
 * http://developer.berlios.de/projects/izpack/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.panels;

import java.io.File;

import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.installer.IzPanel;
import com.izforge.izpack.util.OsVersion;
import com.izforge.izpack.util.VariableSubstitutor;

/**
 * The simple finish panel class.
 * 
 * @author Davide Zerbetto
 */
public class SpagoBIFinishPanel extends IzPanel
{

    /**
     * 
     */
    private static final long serialVersionUID = 3689911781942572085L;

    /** The variables substitutor. */
    private VariableSubstitutor vs;

    /**
     * The constructor.
     * 
     * @param parent The parent.
     * @param idata The installation data.
     */
    public SpagoBIFinishPanel(InstallerFrame parent, InstallData idata)
    {
        super(parent, idata, new IzPanelLayout());
        vs = new VariableSubstitutor(idata.getVariables());
    }

    /**
     * Indicates wether the panel has been validated or not.
     * 
     * @return true if the panel has been validated.
     */
    public boolean isValidated()
    {
        return true;
    }

    /** Called when the panel becomes active. */
    public void panelActivate()
    {
        parent.lockNextButton();
        parent.lockPrevButton();
        parent.setQuitButtonText(parent.langpack.getString("FinishPanel.done"));
        parent.setQuitButtonIcon("done");
        if (idata.installSuccess)
        {
            
            // We set the information
            add(LabelFactory.create(parent.icons.getImageIcon("check")));
            add(IzPanelLayout.createParagraphGap());
            add(LabelFactory.create(parent.langpack.getString("FinishPanel.success"),
                    parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            add(IzPanelLayout.createParagraphGap());
            add(IzPanelLayout.createParagraphGap());
            
            String path = translatePath("$INSTALL_PATH");
            
            if (idata.uninstallOutJar != null)
            {
                // We prepare a message for the uninstaller feature
            	String unistallerPath = path + File.separator + "Uninstaller";
            	
                add(LabelFactory.create(parent.langpack
                        .getString("FinishPanel.uninst.info"), parent.icons
                        .getImageIcon("information"), LEADING), NEXT_LINE);
                add(LabelFactory.create(unistallerPath, parent.icons.getImageIcon("empty"),
                        LEADING), NEXT_LINE);
                add(IzPanelLayout.createParagraphGap());
                add(IzPanelLayout.createParagraphGap());
            }
            
            String serverType = idata.getVariable("SERVER_TYPE");
            String port = "8080";
            if ("jonas".equalsIgnoreCase(serverType)) port = "9000";
            
            boolean installBirt = idata.getVariable("BIRT").equalsIgnoreCase("yes");
            boolean installGeo = idata.getVariable("GEO").equalsIgnoreCase("yes");
            boolean installJasper = idata.getVariable("JASPER").equalsIgnoreCase("yes");
            boolean installJPivot = idata.getVariable("JPIVOT").equalsIgnoreCase("yes");
            boolean installQbe = idata.getVariable("QBE").equalsIgnoreCase("yes");
            boolean installWeka = idata.getVariable("WEKA").equalsIgnoreCase("yes");
            boolean installTalend = idata.getVariable("TALEND").equalsIgnoreCase("yes");
            boolean installExoProfileAttrManager = idata.getVariable("EXOPROFILEATTRMANAGER").equalsIgnoreCase("yes");
            boolean installBooklets = idata.getVariable("BOOKLETS").equalsIgnoreCase("yes");
            boolean installAuditingAndMonitoring = idata.getVariable("SPAGOBI_AM").equalsIgnoreCase("yes");
            boolean installExamples = idata.getVariable("SPAGOBI_EXAMPLES").equalsIgnoreCase("yes");
            
            if (installBirt || installGeo || installJasper || installJPivot || installQbe || installWeka) {
            	add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.installedEngines"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            	if (installBirt) 
            		add(LabelFactory.create("SpagoBIBirtReportEngine",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            	if (installGeo) 
            		add(LabelFactory.create("SpagoBIGeoEngine",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            	if (installJasper) 
            		add(LabelFactory.create("SpagoBIJasperReportEngine",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            	if (installJPivot) 
            		add(LabelFactory.create("SpagoBIJPivotEngine",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            	if (installQbe) 
            		add(LabelFactory.create("SpagoBIQbeEngine",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            	if (installWeka) 
            		add(LabelFactory.create("SpagoBIWekaEngine",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            	if (installTalend) 
            		add(LabelFactory.create("SpagoBITalendEngine",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            } else {
            	add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.noEnginesInstalled"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            }
            
            add(IzPanelLayout.createParagraphGap());
            add(IzPanelLayout.createParagraphGap());

            if (installExoProfileAttrManager || installBooklets) {
            	add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.modulesInstalled"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            	if (installExoProfileAttrManager) 
            		add(LabelFactory.create("Exo Profile Attribute Manager module",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            	if (installBooklets) 
            		add(LabelFactory.create("Booklets module",
                    		parent.icons.getImageIcon("done"), LEADING), NEXT_LINE);
            } else {
            	add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.noModulesInstalled"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            }
            
            add(IzPanelLayout.createParagraphGap());
            add(IzPanelLayout.createParagraphGap());
            
            if (installExamples || installAuditingAndMonitoring) {
            	if (installExamples && installAuditingAndMonitoring) add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.examplesAndAM.installed"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            	else if (installExamples) add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.examples.installed"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            	else add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.AM.installed"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
                add(IzPanelLayout.createParagraphGap());
                add(IzPanelLayout.createParagraphGap());
            	add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.startSpagoBIInfo"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            	if (OsVersion.IS_WINDOWS) add(LabelFactory.create(path + File.separator + "StartSpagoBI.bat", parent.icons.getImageIcon("empty"), LEADING), NEXT_LINE);
            	if (OsVersion.IS_UNIX) add(LabelFactory.create(path + File.separator + "StartSpagoBI.sh", parent.icons.getImageIcon("empty"), LEADING), NEXT_LINE);
            	add(IzPanelLayout.createParagraphGap());
                add(IzPanelLayout.createParagraphGap());
            	String connectionUrl = null;
            	if (installExamples && installAuditingAndMonitoring) connectionUrl = parent.langpack.getString("SpagoBIFinishPanel.examplesWithAM.connectionUrl");
            	else if (installExamples) connectionUrl = parent.langpack.getString("SpagoBIFinishPanel.examples.connectionUrl");
            	else connectionUrl = parent.langpack.getString("SpagoBIFinishPanel.AM.connectionUrl");
            	connectionUrl = connectionUrl.replace("$SERVER_PORT", port);
            	add(LabelFactory.create(connectionUrl,
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            } else {
            	add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.examplesAndAM.notinstalled"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
                add(IzPanelLayout.createParagraphGap());
                add(IzPanelLayout.createParagraphGap());
            	add(LabelFactory.create(parent.langpack.getString("SpagoBIFinishPanel.startSpagoBIInfo"),
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            	if (OsVersion.IS_WINDOWS) add(LabelFactory.create(path + File.separator + "StartSpagoBI.bat", parent.icons.getImageIcon("empty"), LEADING), NEXT_LINE);
            	if (OsVersion.IS_UNIX) add(LabelFactory.create(path + File.separator + "StartSpagoBI.sh", parent.icons.getImageIcon("empty"), LEADING), NEXT_LINE);
            	add(IzPanelLayout.createParagraphGap());
                add(IzPanelLayout.createParagraphGap());
            	String connectionUrl = parent.langpack.getString("SpagoBIFinishPanel.noExamples.connectionUrl");
            	connectionUrl = connectionUrl.replace("$SERVER_PORT", port);
            	add(LabelFactory.create(connectionUrl,
                		parent.icons.getImageIcon("information"), LEADING), NEXT_LINE);
            }

        }
        else
            add(LabelFactory.create(parent.langpack.getString("FinishPanel.fail"),
                    parent.icons.getImageIcon("information"),  LEADING));
        
        getLayoutHelper().completeLayout(); // Call, or call not?

    }

    /**
     * Translates a relative path to a local system path.
     * 
     * @param destination The path to translate.
     * @return The translated path.
     */
    private String translatePath(String destination)
    {
        // Parse for variables
        destination = vs.substitute(destination, null);

        // Convert the file separator characters
        return destination.replace('/', File.separatorChar);
    }
}

