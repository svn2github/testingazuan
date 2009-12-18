/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

@Author Marco Cortella

**/
package bi.bmm.wizards;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.util.ClassInfo;
import bi.bmm.util.ComplexClassInfo;
import bi.bmm.util.WSInfoValue;


public class SetComplexBCFieldsWizard extends Wizard {

	
	private SetComplexBCFields_page01 one;
	private String cBCName;
	private ArrayList<String> inhBCList;
	private BMUniverseView bmUniverse;
	private String pathCCi;
	public SetComplexBCFieldsWizard(String cBCName, ArrayList<String> inhBCList) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Set the field for your Complex Business Class");
		this.cBCName = cBCName;
		this.inhBCList = inhBCList;
	}

	@Override
	public void addPages() {
		one = new SetComplexBCFields_page01(cBCName,inhBCList);
		addPage(one);
		
	}

	@Override
	public boolean performFinish() {
		
		//recupero la vista
		bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
		
		if (one.isPageComplete()){
			
			//SE L'ENTITA' HA UN NOME CHE ESISTE GIA' LO SEGNALO
			if (!bmUniverse.bcList.isEmpty()){
				for (int i = 0 ; i < bmUniverse.bcList.size() ; i++){
					if (bmUniverse.bcList.get(i).getClassName().equals(one.getComplexClassName())){
						MessageDialog.openError(new Shell(), "Error!",""+
						"The name of your new BC already exist, try to change it.");
						return false;
					}
				}
			}
			//compongo il file con l'entità
			createComplexClass(one.getFieldList());
			//disegno nella view la tabella
			createTableInBMU();
			/*
				bmu.configuringPersistenceProvider(conn);
				//controllo se c'è una relazione
				if( (two.getCiOut() != null) &&
						(two.getRelType() != -1)){
					//creo una nuova relazione
					
					/**
					 * RIPRENDO QUELLO CHE HO FATTO IN NEW REL
					 * */
					/*	
						ClassInfo cOut = two.getCiOut();
						ClassInfo cIn = null;
						BMScheme bms = bmUniverse.scheme;
						//recupero la cIn cioè la classe appena creata
						for(int i = 0; i< bmUniverse.bcList.size(); i++){
							//ottengo la classInfo e vedo se è entità con 
							//table = a tab e con arg = a arg
							ClassInfo ci = bmUniverse.bcList.get(i);
							if (ci.isTarget(two.getTabArgIn()[0],two.getTabArgIn()[1])){
								cIn = ci;
								break;
							}
						}
						
						if (cIn == null || bms == null){
							System.err.println("SETBCDETAILFAST performfinish() BUG in CLASSINFO CIN");
							return false;
						}
						
						String mappedBy = two.getTabArgOut()[1];
						switch(two.getRelType()){
						
						case BMScheme.ONE_TO_MANY:
							bms.addLinkFigure(cIn.getFigure(), cOut.getFigure(),BMScheme.ONE_TO_MANY);
							bmUniverse.relList.add(new String[] {cIn.getClassPathInfo(), cOut.getClassPathInfo(),"ONE_TO_MANY"});
							cIn.addOneToManyRelation(mappedBy, cOut.getClassName());
							cOut.addManyToOneRelation(mappedBy,cIn.getClassName());
							break;
						
						case BMScheme.ONE_TO_ONE:
							bms.addLinkFigure(cIn.getFigure(), cOut.getFigure(),BMScheme.ONE_TO_ONE);
							bmUniverse.relList.add(new String[] {cOut.getClassPathInfo(), cIn.getClassPathInfo(),"ONE_TO_ONE"});
							//TODO: IMPLEMENT MAPPING TYPE
							break;
							
						default:
							MessageDialog.openInformation(new Shell(), "Error!",""+
							" An Error was occurred during the type choosing.");
						;
						}
						
						
						String result;
						try {
							result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
							String[] sResult =result.split("#");
							BusinessModelUtil bmu = new BusinessModelUtil(sResult[0], sResult[1]);
							bmu.createClassInfoByCI(cIn);
							bmu.createClassInfoByCI(cOut);
							
							
					    	return true;		
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.err.println(e.getMessage());
							return false;
						}
				*/
				return true;
			}
	return false;
	}

	
	private void createComplexClass(ArrayList<String> fieldList) {
		
		
			//ottengo una class_info per il path
			ClassInfo ci = bmUniverse.bcList.get(0);
			
			//preparo l'hash map per i field ereditati dalle BC
			HashMap<String,ArrayList<String[]>> inhBC = new HashMap<String, ArrayList<String[]>>();
			for (int i = 0 ; i < inhBCList.size() ; i++){
				ArrayList<String[]> fList = new ArrayList<String[]>();
				System.err.println(fieldList);
				
				for (int k = 0; k < fieldList.size() ; k++){
					if (fieldList.get(k).split(" - ")[2].equals(inhBCList.get(i))){
						fList.add(new String[]{fieldList.get(k).split(" - ")[0],fieldList.get(k).split(" - ")[1]});
					}
				}
				inhBC.put(inhBCList.get(i), fList);
			}
			//preparo l'hash map per i field ereditati dai WS
			HashMap<String[],WSInfoValue> inhWS = new HashMap<String[], WSInfoValue>();
			
			ComplexClassInfo cci = new ComplexClassInfo(ci.getPath(), cBCName, inhBC, inhWS);
			//questo comando crea il file info.xml
			cci.buildInfo();
			//salvo il path
			pathCCi = cci.getClassPathInfo();
			
		
		
	}

	private void createTableInBMU() {
		
		//recupero la classe e la compilo
		ComplexClassInfo cci = new ComplexClassInfo(null, null, null, null);
		//questo comando ripristina la classe dal file info.xml specificato nel path
		cci.buildClass(pathCCi);
		bmUniverse.cbcList.add(cci);
		bmUniverse.drawCBC(cci);
		
	}
	

	

	
}