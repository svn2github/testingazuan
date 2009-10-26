package test;

import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Style;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataStyle;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.EditorPart;

public class Designer {

	int counter=0;
	String state=NORMAL;
	Integer currentSelection=-1;
	int currentX=0;
	int currentY=0;
	Composite mainComposite;
	HashMap<Integer, DocContainer> containers;

	EditorPart editor;

	public static final String NORMAL="normal";
	public static final String SELECTION="selection";
	public static final String RESIZE="resize";
	public static final String DRAG="drag";
	public static final int MOUSE_LEFT_KEY=1;
	public static final int MOUSE_RIGHT_KEY=3;


	/** 
	 * Adds a new Document Container in the designer
	 * 
	 * @param mainComposite
	 * @param x
	 * @param y
	 * @param _width
	 * @param _height
	 * @return
	 */

	public Integer addNewDocContainer(Composite mainComposite, int x, int y, int _width, int _height){
		SpagoBILogger.infoLog(Designer.class.toString()+": add New Container function");

		// shell check if overlaids or exceeds
		int tempWidth=_width;
		tempWidth=tempWidth/DocContainer.ALIGNMENT_MARGIN;
		tempWidth=tempWidth*DocContainer.ALIGNMENT_MARGIN;
		int tempHeight=_height;
		tempHeight=tempHeight/DocContainer.ALIGNMENT_MARGIN;
		tempHeight=tempHeight*DocContainer.ALIGNMENT_MARGIN;

		Rectangle rectangle=new Rectangle(x,y, tempWidth, tempHeight);
		boolean doesExceed=DocContainer.doesExceed(Integer.valueOf(-1), this, x, y, tempWidth, tempHeight, false);
		boolean doesIntersect=DocContainer.doesIntersect(Integer.valueOf(-1), this, x, y, tempWidth, tempHeight, false);

		if(doesExceed==true){
			MessageDialog.openWarning(mainComposite.getShell(), 
					"Warning", "Container you want to insert exceeds shell!");
			SpagoBILogger.warningLog(Designer.class.toString()+": add New Container function: Container exceed shell limits");
			return null;
		}
		if(doesIntersect==true){
			MessageDialog.openWarning(mainComposite.getShell(), 
					"Warning", "Container you want to insert intersects other composites!");
			SpagoBILogger.warningLog(Designer.class.toString()+": add New Container function: Container you want to insert intersects other composites");
			return null;
		}

		DocContainer group=new DocContainer(this, mainComposite, x, y, tempWidth, tempHeight);
		containers.put(group.getId(), group);
		Composite g=group.getContainer();
		mainComposite.layout();
		mainComposite.update();
		mainComposite.redraw();
		mainComposite.setFocus();
		SpagoBILogger.infoLog("END "+Designer.class.toString()+": add New Container function");
		return group.id;
	}

	/** Add Container reading the template
	 * 
	 * @param mainComposite
	 * @param x
	 * @param y
	 * @param _width
	 * @param _height
	 * @param metadataDocument
	 */

	public void addDocContainerFromTemplate(Composite mainComposite, int x, int y, int _width, int _height, MetadataDocument metadataDocument){
		SpagoBILogger.infoLog(Designer.class.toString()+": add Doc Container from template");
		Integer id=addNewDocContainer(mainComposite, x, y, _width, _height);
		if(id==null) return;
		else{
			DocContainer docContainer=containers.get(id);
			docContainer.setTitle(metadataDocument!=null ? metadataDocument.getLabel() : "NoDocument");
			if(metadataDocument!=null){
				docContainer.setMetadataDocument(metadataDocument);
			}
			docContainer.viewDocumentMetadata(metadataDocument);
		}
		SpagoBILogger.infoLog("END: "+Designer.class.toString()+": add Doc Container from template");

	}

	public Designer(Composite composite) {
		super();
		FormLayout layout=new FormLayout();
		//composite.setLayout(layout);
		mainComposite=composite;
		setMouseControls(composite);
		addContextMenu(composite);
		containers=new HashMap<Integer, DocContainer>();
		addShellMouseControls(composite);

	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public int getCounter() {
		return counter;
	}



	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void incrementCounter() {
		this.counter = counter+1;
	}



	public Integer getCurrentSelection() {
		return currentSelection;
	}



	public void setCurrentSelection(Integer currentSelection) {
		this.currentSelection = currentSelection;
	}


	/** Track the mous eposition when clicking
	 * 
	 * @param shell
	 */
	public void setMouseControls(final Composite shell){
		shell.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent arg0) {
			}
			public void mouseDown(MouseEvent arg0) {
				if(arg0.button==MOUSE_LEFT_KEY){
					currentX=arg0.x;
					currentY=arg0.y;
				}
				else if(arg0.button==MOUSE_RIGHT_KEY){
					// memorize th eposition of the menu 
					currentX=arg0.x;
					currentY=arg0.y;
				}
			}

			public void mouseUp(MouseEvent arg0) {

			}

		});

	}


	/** Add control on mouse events won the shell
	 * 
	 * @param shell
	 */

	public void addShellMouseControls(final Composite shell){
		SpagoBILogger.infoLog(Designer.class.toString()+": Add Shell mouse control");		
		final Point[] offset = new Point[1];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.MouseDown:
					/**  IF in resizing state mouse button on shell causes end resizing**/
					Composite selected=currentSelection.intValue()!=-1 ? containers.get(currentSelection).getContainer() : null ;
					if(getState().equals(Designer.RESIZE)){
						setState(Designer.NORMAL);
						offset[0] = null;							
						Cursor cursor=new Cursor(getMainComposite().getDisplay(), SWT.CURSOR_ARROW);						
						getMainComposite().setCursor(cursor);						
						int tempWidth=selected.getBounds().width;
						tempWidth=tempWidth/DocContainer.ALIGNMENT_MARGIN;
						tempWidth=tempWidth*DocContainer.ALIGNMENT_MARGIN;
						int tempHeight=selected.getBounds().height;
						tempHeight=tempHeight/DocContainer.ALIGNMENT_MARGIN;
						tempHeight=tempHeight*DocContainer.ALIGNMENT_MARGIN;
						selected.setSize(tempWidth, tempHeight);						
						DocContainer docContainerSelected=containers.get(currentSelection);
						docContainerSelected.reloadStyleDocumentProperties();
						setCurrentSelection(new Integer(-1));
						if(selected!=null){
							selected.setBackground(new Color(selected.getDisplay(),new RGB(200,200,200)));
						}
					}
					/**  IF in Selection state mouse button on shell causes end selection**/
					else if(getState().equals(Designer.SELECTION)){
						setState(Designer.NORMAL);
						offset[0] = null;							
						setCurrentSelection(new Integer(-1));
						if(selected!=null){
							selected.setBackground(new Color(selected.getDisplay(),new RGB(200,200,200)));
						}
					}
					break;
				case SWT.MouseMove:
					/**  IF in resizing state mouse moving on shell causes resizing**/
					DocContainer selectedDoc=currentSelection.intValue()!=-1 ? containers.get(currentSelection) : null ;
					Composite selected1=selectedDoc.getContainer();
					//System.out.println("Stampo coordinate: x = "+event.x+" su "+p.x+" | y = "+event.y+" su "+p.y);
					if(getState().equals(Designer.RESIZE)){
						if(selected1!=null){
							Rectangle rect=selected1.getBounds();
							int x=event.x;
							int y=event.y;
							int nuova_larghezza=rect.width;
							int nuova_altezza=rect.height;
							if(x>rect.x+rect.width){
								nuova_larghezza=rect.width+(x-rect.x-rect.width);
								//selected1.setSize(nuova_larghezza, rect.height);
							}
							if(y>rect.y+rect.height){
								nuova_altezza=rect.height+(y-rect.y-rect.height);
							}
							// check if intersects other components
							boolean doesIntersect=DocContainer.doesIntersect(selectedDoc.getId(), selectedDoc.getDesigner(),selectedDoc.getContainer().getLocation().x, selectedDoc.getContainer().getLocation().y, nuova_larghezza,nuova_altezza, true);
							// check if exceeds bounds
							boolean doesExceed=DocContainer.doesExceed(selectedDoc.getId(), selectedDoc.getDesigner(),selectedDoc.getContainer().getLocation().x, selectedDoc.getContainer().getLocation().y, nuova_larghezza,nuova_altezza, true);							
							if(doesIntersect==false && doesExceed==false){
								selected1.setSize(nuova_larghezza, nuova_altezza);
								//								selectedDoc.setWidth(nuova_larghezza);
								//								selectedDoc.setHeight(nuova_altezza);
								System.out.println("Resizing da fuori: x="+selectedDoc.getContainer().getBounds().x+" e y="+selectedDoc.getContainer().getBounds().y+" altezza nuova="+selectedDoc.getContainer().getBounds().height+" e larghezza nuova="+selectedDoc.getContainer().getBounds().width);														
							}
							else{
								System.out.println("BLoccato resizing da fuori: x="+selectedDoc.getContainer().getBounds().x+" e y="+selectedDoc.getContainer().getBounds().y+" altezza rimane="+selectedDoc.getContainer().getBounds().height+" e larghezza rimane="+selectedDoc.getContainer().getBounds().width);							
							}
							//shell.redraw();
						}
					}
					else if(getState().equals(Designer.DRAG)){
						if(selectedDoc.getId().equals(selectedDoc.getDesigner().getCurrentSelection())){
							if (offset[0] != null) {
								Point pt = offset[0];							
								int newX=event.x - pt.x;
								int newY=event.y - pt.y;
								boolean doesIntersect=DocContainer.doesIntersect(selectedDoc.getId(), selectedDoc.getDesigner(),newX, newY, selectedDoc.getContainer().getBounds().width,selectedDoc.getContainer().getBounds().height,true);
								boolean doesExceed=DocContainer.doesExceed(selectedDoc.getId(), selectedDoc.getDesigner(),newX, newY, selectedDoc.getContainer().getBounds().width,selectedDoc.getContainer().getBounds().height,true);

								if(doesIntersect==false && doesExceed==false){
									selectedDoc.getContainer().setLocation(newX, newY);
									//									xPos=newX;
									//									yPos=newY;
									System.out.println("Drag da fuori: x="+selectedDoc.getContainer().getBounds().x+" e y="+selectedDoc.getContainer().getBounds().y+" altezza nuova="+selectedDoc.getContainer().getBounds().height+" e larghezza nuova="+selectedDoc.getContainer().getBounds().width);														
								}
								else{
									System.out.println("Drag BLOCCATO da fuori: x="+selectedDoc.getContainer().getBounds().x+" e y="+selectedDoc.getContainer().getBounds().y+" altezza rimane="+selectedDoc.getContainer().getBounds().height+" e larghezza rimane="+selectedDoc.getContainer().getBounds().width);														
								}
							}
						}
					}
					break;
				case SWT.MouseUp:
					//					Composite selectedDoc2=currentSelection.intValue()!=-1 ? containers.get(currentSelection).getContainer() : null ;
					//					if(getState().equals(Designer.DRAG)){
					//						int tempX=selectedDoc2.getLocation().x;
					//						tempX=tempX/DocContainer.ALIGNMENT_MARGIN;
					//						tempX=tempX*DocContainer.ALIGNMENT_MARGIN;
					//						int tempY=selectedDoc2.getLocation().y;
					//						tempY=tempY/DocContainer.ALIGNMENT_MARGIN;
					//						tempY=tempY*DocContainer.ALIGNMENT_MARGIN;
					//						selectedDoc2.setLocation(tempX, tempY);
					//						
					//					}					
					break;
				}
			}
		};
		shell.addListener(SWT.MouseDown, listener);
		shell.addListener(SWT.MouseUp, listener);
		shell.addListener(SWT.MouseMove, listener);
		SpagoBILogger.infoLog("END "+Designer.class.toString()+": Add Shell mouse control");		
	}


	/** Add the context menu
	 * 
	 * @param composite
	 */

	public void addContextMenu(final Composite composite){
		SpagoBILogger.infoLog(Designer.class.toString()+": Add context menu function");		
		composite.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				Menu menu = new Menu(composite.getShell(), SWT.POP_UP);
				MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText("Add Doc");
				item.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event e) {
						if(getState().equals(Designer.NORMAL)){
							addNewDocContainer(composite, currentX, currentY, DocContainer.DEFAULT_WIDTH, DocContainer.DEFAULT_HEIGHT);
						}
					}
				});

				menu.setLocation(event.x, event.y);
				menu.setVisible(true);
				while (!menu.isDisposed() && menu.isVisible()) {
					if (!composite.getDisplay().readAndDispatch())
						composite.getDisplay().sleep();
				}
				menu.dispose();
			}
		});

		SpagoBILogger.infoLog("END "+Designer.class.toString()+": Add context menu function");		

	}



	public int getCurrentX() {
		return currentX;
	}



	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}



	public int getCurrentY() {
		return currentY;
	}



	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}



	public HashMap<Integer, DocContainer> getContainers() {
		return containers;
	}



	public void setContainers(HashMap<Integer, DocContainer> containers) {
		this.containers = containers;
	}



	public Composite getMainComposite() {
		return mainComposite;
	}



	public void setMainComposite(Composite mainComposite) {
		this.mainComposite = mainComposite;
	}



	public EditorPart getEditor() {
		return editor;
	}



	public void setEditor(EditorPart editor) {
		this.editor = editor;
	}

	/**
	 * 	Initizialize the designer by the template
	 * @param documentComposition
	 */

	public void initializeDesigner(DocumentComposition documentComposition){
		SpagoBILogger.infoLog(Designer.class.toString()+": Initialize designer function");
		if(documentComposition.getDocumentsConfiguration()==null || documentComposition.getDocumentsConfiguration().getDocuments()==null || documentComposition.getDocumentsConfiguration().getDocuments().size()==0){
			return;
		}
		// Run all the documents
		for (Iterator iterator = documentComposition.getDocumentsConfiguration().getDocuments().iterator(); iterator.hasNext();) {
			Document document = (Document) iterator.next();
			String label=document.getSbiObjLabel();
			String sbiObjectLabel =	document.getSbiObjLabel();
			MetadataStyle metadataStyle=null;
			// Recover style informations!
			try{
				Style styleS=document.getStyle();
				metadataStyle=new MetadataStyle(styleS);
			}
			catch (Exception e) {
				MessageDialog.openError(mainComposite.getShell(), 
						"Error", "Error in retrieving positioning metadata for the file with name "+sbiObjectLabel);
				SpagoBILogger.errorLog("END "+Designer.class.toString()+": Initialize designer function: " +
						"Error in retrieving positioning metadata for the file with name "+sbiObjectLabel,null);				
				continue;
			}

			try{
				// Recover documentInformations from File
				String localFileName=document.getLocalFileName();	
				MetadataDocument metadataDocument=null;
				if(localFileName!=null){
					IPath w=new Path(localFileName);					
					IFile fileToGet = ResourcesPlugin.getWorkspace().getRoot().getFile(w);
					if(fileToGet.exists()){
						String name=fileToGet.getName();
						String ciao=fileToGet.getPersistentProperty(PropertyPage.DOCUMENT_NAME);
						IPath f=fileToGet.getFullPath(); 
						metadataDocument=new MetadataDocument(fileToGet);
						int widthToPut=metadataStyle.getWidthFromPerc(mainComposite);
						int heightToPut=metadataStyle.getHeightFromPerc(mainComposite);
						addDocContainerFromTemplate(mainComposite, metadataStyle.getX(), metadataStyle.getY(), widthToPut, heightToPut, metadataDocument);
					}
					else{
						MessageDialog.openError(mainComposite.getShell(), 
								"Error", "Could not find file "+localFileName+", download idt again!");
						SpagoBILogger.errorLog("END "+Designer.class.toString()+": Initialize designer function: " +
								"Could not find file "+localFileName+", download idt again!",null);
					}
				}
			}
			catch (Exception e) {
				// Error in retrieving the document, download idt again
				MessageDialog.openError(mainComposite.getShell(), 
						"Error", "Error in retrieving metadata the file with name "+sbiObjectLabel+", download idt again!");
				SpagoBILogger.errorLog("END "+Designer.class.toString()+": Initialize designer function: Error in retrieving metadata the file with name "+sbiObjectLabel+", download idt again!",e);

			}
		}
		SpagoBILogger.infoLog("END "+Designer.class.toString()+": Initialize designer function");

	}


}
