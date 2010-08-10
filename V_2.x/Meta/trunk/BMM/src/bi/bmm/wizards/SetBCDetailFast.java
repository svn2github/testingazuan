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

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.elements.BusinessModelUtil;
import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;
import bi.bmm.util.DBConnection;
import bi.bmm.util.HunkIO;


public class SetBCDetailFast extends Wizard {

	
	private SetBCDetails_page01 one;
	private DBConnection conn;
	private String table;
	private BusinessModelUtil bmu ;
	private SetBCDetails_page02 two;
	private SetBCDetails_page03 three;

	public SetBCDetailFast(DBConnection conn, String table) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Business Class");
		this.conn = conn;
		this.table = table;
	}

	@Override
	public void addPages() {
		one = new SetBCDetails_page01(conn,table);
		two = new SetBCDetails_page02(table);
		three = new SetBCDetails_page03(table);
		//two = new NewConn_page02();
		addPage(one);
		addPage(two);
		addPage(three);
	}

	@Override
	public boolean performFinish() {
		
		//recupero la vista
		BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
		
		if (three.isPageComplete()){
			
			//SE L'ENTITA' HA UN NOME CHE ESISTE GIA' LO SEGNALO
			if (!bmUniverse.bcList.isEmpty()){
				for (int i = 0 ; i < bmUniverse.bcList.size() ; i++){
					if (bmUniverse.bcList.get(i).getClassName().equals(one.getClassName())){
						MessageDialog.openError(new Shell(), "Error!",""+
						"The name of your new BC already exist, try to change it.");
						return false;
					}
				}
			}
				//compongo il file con l'entità
				createEntity(one.getKeys(),one.getAttributes(),one.getClassName());
				//disegno nella view la tabella
				createTableInBMU();
				bmu.configuringPersistenceProvider(conn);
				//controllo se c'è una relazione diretta
				if( (two.getCiOut() != null) &&
						(two.getRelType() != -1)){
				//creo una nuova relazione
					/**
					 * RIPRENDO QUELLO CHE HO FATTO IN NEW REL
					 * */
						
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
									
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.err.println(e.getMessage());
							
						}
						
				}
				//controllo se ci sono relazione indiretta
				if( !three.getCiOut().isEmpty())
				{
				for(int i =0; i< three.getCiOut().size(); i++){
					if(!three.getRelType().get(i).equals("NONE")){
								
							//creo una nuova relazione
							/**
							* RIPRENDO QUELLO CHE HO FATTO IN NEW REL
							* */
									
							ClassInfo cOut = three.getCiOut().get(i);
							ClassInfo cIn = null;
							BMScheme bms = bmUniverse.scheme;
							//recupero la cIn cioè la classe appena creata
							for(int i1 = 0; i1< bmUniverse.bcList.size(); i1++){
								//ottengo la classInfo e vedo se è entità con 
								//table = a tab e con arg = a arg
								ClassInfo ci = bmUniverse.bcList.get(i1);
								if (ci.isTarget(three.getTabArgIn().get(i)[0],three.getTabArgIn().get(i)[1])){
									cIn = ci;
									break;
									}	
							}
									
							if (cIn == null || bms == null){
								System.err.println("SETBCDETAILFAST performfinish() BUG in CLASSINFO CIN");
								return false;
							}
									
							String mappedBy = three.getTabArgIn().get(i)[1];
									
									if(three.getRelType().get(i).equals("MANY_TO_MANY")){
										bms.addLinkFigure(cIn.getFigure(), cOut.getFigure(),BMScheme.MANY_TO_MANY);
										bmUniverse.relList.add(new String[] {cIn.getClassPathInfo(), cOut.getClassPathInfo(),"MANY_TO_MANY"});
										
									}
									
									if(three.getRelType().get(i).equals("MANY_TO_ONE")){
										bms.addLinkFigure(cIn.getFigure(), cOut.getFigure(),BMScheme.MANY_TO_ONE);
										bmUniverse.relList.add(new String[] {cOut.getClassPathInfo(), cIn.getClassPathInfo(),"MANY_TO_ONE"});
										cIn.addManyToOneRelation(mappedBy, cOut.getClassName());
										cOut.addOneToManyRelation(mappedBy,cIn.getClassName());
									}
									
									String result;
									try {
										result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
										String[] sResult =result.split("#");
										BusinessModelUtil bmu = new BusinessModelUtil(sResult[0], sResult[1]);
										bmu.createClassInfoByCI(cIn);
										bmu.createClassInfoByCI(cOut);
												
									} catch (IOException e) {
										// TODO Auto-generated catch block
										System.err.println(e.getMessage());
										
									}
						}
					}		
				}
				return true;
			}
	return false;
	}

	
	private void createEntity(Table keys, Table attributes, String name) {
		
		try {
			//Setto il file di persistenza
			String result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
			String[] sResult =result.split("#");
			
			bmu = new BusinessModelUtil(sResult[0], sResult[1]);
			bmu.creaDirPersistence(conn.getDBName());
			bmu.configuringPersistenceProvider(conn);
			bmu.createClassInfo(conn,keys,attributes,name,new ArrayList<String[]>(),new ArrayList<String[]>(),table);
			
			
		
		} catch (IOException e) {
			MessageDialog.openError(new Shell(), "Error reading info file.","An error encourring when trying to read the BM info file");
		}
		
		
	}

	private void createTableInBMU() {
		if(bmu == null){
			return;
		}
		//recupero la view
		BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
		//recupero il gruppo principale
		//recupero la classe e la compilo
		ClassInfo ci = new ClassInfo(bmu.getClassPathInfo(),null);
		ci.buildClassInfo();
		bmUniverse.bcList.add(ci);
		bmUniverse.drawBC(ci);
		
	}

	

	

	
}