package it.eng.spagobi.studio.documentcomposition.editors;


import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Style;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo.ModelBO;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataBO;
import it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView;
import it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView;
import it.eng.spagobi.studio.documentcomposition.views.NavigationView;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


public class DocContainer {




	final Integer id;
	Designer designer;
	DocumentContained documentContained;

	String title="";

	public static final int DEFAULT_WIDTH=200;
	public static final int DEFAULT_HEIGHT=200;
	public static final int MIN_MARGIN_BOUNDS=0;
	public static final int ALIGNMENT_MARGIN=30;

	Cursor cursor=null;

	/** Document Container Contrusctor
	 * 
	 * @param _designer
	 * @param mainComposite
	 * @param x
	 * @param y
	 * @param tempWidth
	 * @param tempHeight
	 */

	public DocContainer(Designer _designer,Composite mainComposite, int x, int y, int tempWidth, int tempHeight) {
		super();
		designer=_designer;
		id=Integer.valueOf(designer.getCounter());
		try{
			documentContained=new DocumentContained(mainComposite, SWT.NULL);
		}
		catch (Exception e) {
			e.printStackTrace();}

		title="NUMERO "+(new Integer(designer.getCounter()).toString());
		documentContained.getGroup().setText(title);
		GridLayout layout=new GridLayout();
		layout.numColumns=2;
		documentContained.getGroup().setLayout(layout);
		designer.incrementCounter();
		documentContained.getGroup().setSize(tempWidth, tempHeight);
		documentContained.getGroup().setLocation(x, y);
		designer.setState(Designer.NORMAL);

		addContainerMouseControls(mainComposite, documentContained.getGroup());
		addContextMenu(mainComposite.getShell(), documentContained.getGroup());
		addDragAndDropDocument(documentContained.getGroup());

		documentContained.getGroup().layout();
		documentContained.getGroup().redraw();
		documentContained.getGroup().getParent().redraw();
		documentContained.getGroup().getParent().layout();

	}


	/**
	 *  Add Mouse controls on document container
	 * @param mainComposite
	 * @param composite
	 */

	public void addContainerMouseControls(final Composite mainComposite, final Composite composite){
		final Point[] offset = new Point[1];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				;				switch (event.type) {
				case SWT.MouseDown:
					// Reload the DocumentPropertiesView
					if(documentContained.getMetadataDocument()!=null)
						reloadDocumentPropertiesView(id.toString());
					reloadStyleDocumentProperties();
					// Reload navigations view
					if(documentContained.getMetadataDocument()!=null){
						reloadNavigationView(id.toString());
					}

					/**  IF in resizing state mouse button on Container causes end resizing**/	
					calculateTemplateStyle();
					if(designer.getState().equals(Designer.RESIZE)){
						// only if click on the current selected!
						if(id.equals(designer.getCurrentSelection())){
							designer.setState(Designer.NORMAL);
							cursor=new Cursor(designer.getMainComposite().getDisplay(), SWT.CURSOR_ARROW);						
							designer.getMainComposite().setCursor(cursor);							
							offset[0] = null;							
							//							int tempWidth=documentContained.getGroup().getBounds().width;
							//							tempWidth=tempWidth/ALIGNMENT_MARGIN;
							//							tempWidth=tempWidth*ALIGNMENT_MARGIN;
							//							int tempHeight=documentContained.getGroup().getBounds().height;
							//							tempHeight=tempHeight/ALIGNMENT_MARGIN;
							//							tempHeight=tempHeight*ALIGNMENT_MARGIN;

							DesignerUtilities designerUtilities=new DesignerUtilities();
							int setWidth=designerUtilities.calculateWidth(documentContained.getGroup(), mainComposite.getBounds().width);
							int setHeight=designerUtilities.calculateHeight(documentContained.getGroup(), mainComposite.getBounds().height);
							documentContained.getGroup().setSize(setWidth, setHeight);
							reloadStyleDocumentProperties();
							(new ModelBO()).updateModelModifyDocument(documentContained.getMetadataDocument(), calculateTemplateStyle());
							designer.setCurrentSelection(Integer.valueOf(-1));
							// Reload style text in view (only if in mode automatic
						}
					}
					/**  IF in normal state mouse button on Container causes selection**/					
					else if(designer.getState().equals(Designer.NORMAL)){
						//****** SELECTION OF A RECTANGLE **********
						Rectangle rect = composite.getBounds();
						Point pt1 = composite.toDisplay(0, 0);
						Point pt2 = mainComposite.toDisplay(event.x, event.y);
						offset[0] = new Point(pt2.x - pt1.x, pt2.y - pt1.y);
						// while up put on DRAG situation
						designer.setState(Designer.DRAG);
						cursor=new Cursor(designer.getMainComposite().getDisplay(), SWT.CURSOR_HAND);						
						designer.getMainComposite().setCursor(cursor);
						composite.setBackground(new Color(composite.getDisplay(),new RGB(0,255,0)));
						System.out.println(id);
						designer.setCurrentSelection(id);
						// ---- modify selection view---

					}
					/**  IF in selection state mouse button on Container causes restart DRAG or another selection!**/					
					else if(designer.getState().equals(Designer.SELECTION)){
						// check if already selected or is changing selection!
						Integer idPreviousSel=designer.getCurrentSelection();
						if(Integer.valueOf(id).equals(idPreviousSel)){
							//composite.setBackground(new Color(composite.getDisplay(),new RGB(0,255,0)));
						}
						else{ 
							if(idPreviousSel.intValue()!=-1){
								Composite toDeselect=designer.getContainers().get(idPreviousSel).getDocumentContained().getGroup();
								toDeselect.setBackground(new Color(toDeselect.getDisplay(),new RGB(200,200,200)));
							}
						}

						composite.setBackground(new Color(composite.getDisplay(),new RGB(0,255,0)));							
						designer.setState(Designer.DRAG);
						cursor=new Cursor(designer.getMainComposite().getDisplay(), SWT.CURSOR_HAND);						
						designer.getMainComposite().setCursor(cursor);						
						designer.setCurrentSelection(id);
						Rectangle rect = composite.getBounds();
						Point pt1 = composite.toDisplay(0, 0);
						Point pt2 = mainComposite.toDisplay(event.x, event.y);
						offset[0] = new Point(pt2.x - pt1.x, pt2.y - pt1.y);
					}					
					break;
				case SWT.MouseMove:
					if(designer.getState().equals(Designer.RESIZE)){
						if(id.equals(designer.getCurrentSelection())){
							Rectangle rect=composite.getBounds();
							int x=event.x;
							int y=event.y;
							int nuova_larghezza=rect.width;
							int nuova_altezza=rect.height;
							if(x<rect.x+rect.width //&& x>(rect.x+DEFAULT_WIDTH)
							){
								nuova_larghezza=rect.width+(x-rect.x-rect.width);
								//composite.setSize(nuova_larghezza, rect.height);
							}
							if(y<rect.y+rect.height //&& y>(rect.y+DEFAULT_HEIGHT)
							){
								nuova_altezza=rect.height+(y-rect.y-rect.height);
							}
							if(nuova_altezza<DEFAULT_HEIGHT)nuova_altezza=DEFAULT_HEIGHT;
							if(nuova_larghezza<DEFAULT_WIDTH)nuova_larghezza=DEFAULT_WIDTH;

							//check if intersect!
							boolean doesIntersect=DocContainer.doesIntersect(id,designer,documentContained.getGroup().getLocation().x, documentContained.getGroup().getLocation().y, nuova_larghezza, nuova_altezza,false);
							boolean doesExceed=DocContainer.doesExceed(id,designer,documentContained.getGroup().getLocation().x, documentContained.getGroup().getLocation().y, nuova_larghezza, nuova_altezza,false);

							if(doesIntersect==false && doesExceed==false){
								composite.setSize(nuova_larghezza, nuova_altezza);
								(new ModelBO()).updateModelModifyDocument(documentContained.getMetadataDocument(), calculateTemplateStyle());
								//System.out.println("Resizing da dentro: x="+documentContained.getGroup().getBounds().x+" e y="+documentContained.getGroup().getBounds().y+" altezza nuova="+documentContained.getGroup().getBounds().height+" e larghezza nuova="+documentContained.getGroup().getBounds().width);														
								System.out.println("OK Slarga dentro");

							}	
							else{
								//								System.out.println("Resizing BLOCCATO da dentro: x="+documentContained.getGroup().getBounds().x+" e y="+documentContained.getGroup().getBounds().y+" altezza rimane="+documentContained.getGroup().getBounds().height+" e larghezza rimane="+documentContained.getGroup().getBounds().width);														
								System.out.println("BLocca Slarga dentro");

							}

						}
					}
					/**  IF in Selection state mouse moving on container causes drag and drop**/
					else if(designer.getState().equals(Designer.DRAG)){
						if(id.equals(designer.getCurrentSelection())){
							if (offset[0] != null) {
								Point pt = offset[0];							
								int newX=event.x - pt.x;
								int newY=event.y - pt.y;
								boolean doesIntersect=doesIntersect(id, designer,newX, newY, documentContained.getGroup().getBounds().width,documentContained.getGroup().getBounds().height,false);
								boolean doesExceed=doesExceed(id, designer,newX, newY, documentContained.getGroup().getBounds().width, documentContained.getGroup().getBounds().height,false);

								if(doesIntersect==false && doesExceed==false){
									composite.setLocation(newX, newY);
									(new ModelBO()).updateModelModifyDocument(documentContained.getMetadataDocument(), calculateTemplateStyle());
									System.out.println("Drag da dentro: x="+documentContained.getGroup().getBounds().x+" e y="+documentContained.getGroup().getBounds().y+" altezza nuova="+documentContained.getGroup().getBounds().height+" e larghezza nuova="+documentContained.getGroup().getBounds().width);														
								}
								else{
									System.out.println("Drag BLOCCATO da dentro: x="+documentContained.getGroup().getBounds().x+" e y="+documentContained.getGroup().getBounds().y+" altezza rimane="+documentContained.getGroup().getBounds().height+" e larghezza rimane="+documentContained.getGroup().getBounds().width);														
								}
								//								System.out.println("QUESTO E' UN TEST DA DENTRO CHE "+composite.getBounds().x+" = "+xPos+" e "+composite.getBounds().y+" = "+ yPos+" e poi altezza "+composite.getBounds().height+" = "+height+ " e larghezza  "+composite.getBounds().width+ " = "+width);

							}
						}			
					}
					break;
				case SWT.MouseUp:

					/**  IF in SELECTION state mouse up on container causes selection started from DRAG**/
					if(designer.getState().equals(Designer.DRAG)){
						// ---------- Try alignment MArgin-----------

						int tempX=documentContained.getGroup().getLocation().x;
						int tempY=documentContained.getGroup().getLocation().y;
						tempX=tempX/ALIGNMENT_MARGIN;
						tempX=tempX*ALIGNMENT_MARGIN;
						tempY=tempY/ALIGNMENT_MARGIN;
						tempY=tempY*ALIGNMENT_MARGIN;

						// check if space is almost filled: autofill
						int width=documentContained.getGroup().getBounds().width;
						int height=documentContained.getGroup().getBounds().height;
						int totalX=width+tempX;
						int mainWidth=mainComposite.getBounds().width;		
						if((mainWidth-totalX)<=(DocContainer.ALIGNMENT_MARGIN+10)){
							// increase the width to fill							
							int newwidth=width+((mainWidth-totalX));
							//documentContained.getGroup().getBounds().width=width;
							documentContained.getGroup().setSize(newwidth, height);
						}
						int totalY=height+tempY;
						int mainHeight=mainComposite.getBounds().height;		
						if((mainHeight-totalY)<=(DocContainer.ALIGNMENT_MARGIN+10)){
							// increase the width to fill							
							int newheight=height+((mainHeight-totalY));
							//documentContained.getGroup().getBounds().width=width;
							documentContained.getGroup().setSize(width, newheight);
						}





						documentContained.getGroup().setLocation(tempX, tempY);
						if(id.equals(designer.getCurrentSelection())){
							composite.setBackground(new Color(composite.getDisplay(),new RGB(0,0,255)));
							designer.setCurrentSelection(id);
							designer.setState(Designer.SELECTION);
							designer.setCurrentSelection(id);
							cursor=new Cursor(designer.getMainComposite().getDisplay(), SWT.CURSOR_ARROW);						
							designer.getMainComposite().setCursor(cursor);
						}
					}		
					else if(designer.getState().equals(Designer.SELECTION)){
						if(designer.getCurrentSelection().equals(id)){
							designer.setState(Designer.NORMAL);
							designer.setCurrentSelection(Integer.valueOf(-1));
							offset[0] = null;							
						}
					}
					documentContained.getGroup().redraw();
					break;
				}
			}
		};
		composite.addListener(SWT.MouseDown, listener);
		composite.addListener(SWT.MouseUp, listener);
		composite.addListener(SWT.MouseMove, listener);
	}





	/**
	 *  Add the context menu on document containers
	 * @param mainComposite
	 * @param composite
	 */

	public void addContextMenu(final Composite mainComposite, final Composite composite){
		composite.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				Menu menu = new Menu(mainComposite.getShell(), SWT.POP_UP);
				MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText("Resize");
				item.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						composite.setBackground(new Color(composite.getDisplay(),new RGB(255,0,0)));
						//cursor = new Cursor(container.getDisplay(), new Image(container.getDisplay(),"D:\\progetti\\spagobi\\workspaceSpagobiStudio\\it.eng.spagobi.studio.documentcomposition\\icons\\cursorResize.PNG").getImageData(), 0, 0);
						cursor=new Cursor(designer.getMainComposite().getDisplay(), SWT.CURSOR_CROSS);						
						designer.getMainComposite().setCursor(cursor);
						designer.setState(Designer.RESIZE);
					}
				});
				MenuItem delItem = new MenuItem(menu, SWT.PUSH);
				delItem.setText("Delete Doc");
				delItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						Integer idSel=designer.getCurrentSelection();
						String title=designer.getContainers().get(idSel).getDocumentContained().getGroup().getText();

						// delete document 
						(new ModelBO()).deleteDocumentFromModel(documentContained.getMetadataDocument());
						// delete metadata document
						(new MetadataBO()).getMetadataDocumentComposition().removeMetadataDocument(documentContained.getMetadataDocument());
						designer.setCurrentSelection(-1);
						designer.setState(Designer.NORMAL);
						composite.dispose();
						designer.getContainers().remove(idSel);
						designer.getMainComposite().layout();
						designer.getMainComposite().redraw();
						//						designer.getMainComposite().pack();

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



	}


	public Integer getId() {
		return id;
	}





	public DocumentContained getDocumentContained() {
		return documentContained;
	}


	public void setDocumentContained(DocumentContained documentContained) {
		this.documentContained = documentContained;
	}


	/**
	 *  Add Drag and drop function of a document to its document container
	 * @param composite
	 */

	protected void addDragAndDropDocument(final Composite composite){
		// Allow data to be copied or moved to the drop target
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(composite, operations);

		// Receive data in Text or File format
		//final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		final LocalSelectionTransfer localTransfer = LocalSelectionTransfer.getTransfer();

		Transfer[] types = new Transfer[] {fileTransfer, localTransfer};
		//		Transfer[] types = new Transfer[] {fileTransfer, textTransfer};

		target.setTransfer(types);

		target.addDropListener(new DropTargetListener() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
				// will accept text but prefer to have files dropped
				for (int i = 0; i < event.dataTypes.length; i++) {
					if (fileTransfer.isSupportedType(event.dataTypes[i])){
						event.currentDataType = event.dataTypes[i];
						// files should only be copied
						if (event.detail != DND.DROP_COPY) {
							event.detail = DND.DROP_NONE;
						}
						break;
					}
				}
				// Mine
				for (int i = 0; i < event.dataTypes.length; i++) {
					if (localTransfer.isSupportedType(event.dataTypes[i])){
						event.currentDataType = event.dataTypes[i];
						//						if (event.detail != DND.DROP_COPY) {
						//							event.detail = DND.DROP_NONE;
						//						}
						break;
					}
				}				
			}
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
				//				if (textTransfer.isSupportedType(event.currentDataType)) {
				//					Object o = textTransfer.nativeToJava(event.currentDataType);
				//					String t = (String)o;
				//					if (t != null) System.out.println(t);
				//				}
			}
			public void dragOperationChanged(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
				// allow text to be moved but files should only be copied
				if (fileTransfer.isSupportedType(event.currentDataType)){
					if (event.detail != DND.DROP_COPY) {
						event.detail = DND.DROP_NONE;
					}
				}
			}
			public void dragLeave(DropTargetEvent event) {
			}
			public void dropAccept(DropTargetEvent event) {
			}
			public void drop(DropTargetEvent event) {
				boolean doTransfer=false;
				if (localTransfer.isSupportedType(event.currentDataType)){
					Object selectedObject = event.data;
					if(selectedObject instanceof TreeSelection)
					{
						TreeSelection selectedTreeSelection=(TreeSelection)selectedObject;
						IFile file=(IFile)selectedTreeSelection.getFirstElement();
						doTransfer=documentContained.recoverDocumentMetadata(file);
						// add the document!!
						(new ModelBO()).addNewDocumentToModel(documentContained.getMetadataDocument(), calculateTemplateStyle());
					}

				}
				if(doTransfer==true){
					// Select the component!
					if(documentContained.getMetadataDocument()!=null)
						reloadDocumentPropertiesView(id.toString());
					reloadStyleDocumentProperties();
					// Reload navigations view
					if(documentContained.getMetadataDocument()!=null){
						reloadNavigationView(id.toString());
					}
					designer.setState(Designer.SELECTION);
					composite.setBackground(new Color(composite.getDisplay(),new RGB(0,0,255)));
					if(designer.getCurrentSelection().intValue()!=-1){
						Composite toDeselect=designer.getContainers().get(designer.getCurrentSelection()).getDocumentContained().getGroup();
						toDeselect.setBackground(new Color(toDeselect.getDisplay(),new RGB(200,200,200)));
					}
					designer.setCurrentSelection(id);
				}

				if(doTransfer==true){
					if (fileTransfer.isSupportedType(event.currentDataType)){
						String[] files = (String[])event.data;
						for (int i = 0; i < files.length; i++) {
							Label label=new Label(composite, SWT.NULL);
							label.setText(files[i]);
						}					
					}
				}
				composite.redraw();
				composite.layout();
				composite.getParent().redraw();
				composite.getParent().layout();
			}
		});

	}











	/**
	 *  Calculate the style string from the Style class
	 * @param composite
	 */
	public Style calculateTemplateStyle(){
		Style style=new Style();	
		String toAdd="float:left;margin:0px;";

		// get the bounds
		Point location=documentContained.getGroup().getLocation();
		int x=location.x;
		int y=location.y;

		Rectangle rect=documentContained.getGroup().getBounds();
		int width =rect.width;
		int height =rect.height;

		// get the left margin: arrotondo alla decina
		toAdd+="left:"+Integer.valueOf(x).toString()+";";

		// get the top margin: arrotondo alla decina
		int marginTopTemp=y/10;
		int marginTop=y*10;
		toAdd+="top:"+Integer.valueOf(y).toString()+";";

		// get the total height and width of the container
		Point point=designer.getMainComposite().getSize();
		int totalWidth=point.x;
		int totalHeight=point.y;

		// calculate width and height percentage
		int widthPerc=(width*100)/totalWidth;
		int heightPerc=(height*100)/totalHeight;
		toAdd+="width:"+Integer.valueOf(widthPerc).toString()+"%;";
		toAdd+="height:"+Integer.valueOf(heightPerc).toString()+"%;";

		style.setStyle(toAdd);
		return style;
	}

	/**
	 * check if next drag event inteferes with other containers
	 * @return
	 */

	public static boolean doesIntersect (Integer currentId,Designer designer,int newX, int newY, int newWidth, int newHeight, boolean fromDesigner){
		boolean doesIntersect=false;
		Rectangle thisRectangle=new Rectangle(newX,newY,newWidth,newHeight);
		for (Iterator iterator = designer.getContainers().keySet().iterator(); iterator.hasNext() && doesIntersect==false;) {
			Integer idOther = (Integer) iterator.next();
			if(!idOther.equals(currentId)){
				DocContainer otherContainer = designer.getContainers().get(idOther);
				Group otherGroup=otherContainer.documentContained.getGroup();
				Rectangle otherRectangle=otherGroup.getBounds();
				doesIntersect=thisRectangle.intersects(otherRectangle);	
				System.out.println("Interseca primo: "+doesIntersect+" il container "+otherContainer.getTitle());
				doesIntersect=thisRectangle.intersects(otherRectangle.x, otherRectangle.y, otherRectangle.width, otherRectangle.height);
				System.out.println("secondo: "+doesIntersect+" il container "+otherContainer.getTitle());		
			}

		}
		return doesIntersect;
	}


	/**
	 * check if next drag event exceeds bounds!
	 * @return
	 */

	public static boolean doesExceed (Integer currentId, Designer designer, int newX, int newY, int newWidth, int newHeight, boolean fromDesigner){
		boolean doesExceed=false;
		Composite mainComposite=designer.getMainComposite();
		int heightMain=mainComposite.getSize().y;
		int widthMain=mainComposite.getSize().x;

		//-------give bound of 5 -------
		// x should be nor less than 5, x+width should not be more than widthMain-5
		if(newX<MIN_MARGIN_BOUNDS || (newX+newWidth)>(widthMain-MIN_MARGIN_BOUNDS)){
			System.out.println("Eccede larghezza: x è "+newX+" la larghezza è "+newWidth+" su un totale di "+widthMain);			
			return true;
		}
		else if(newY<MIN_MARGIN_BOUNDS || (newY+newHeight)>(heightMain-MIN_MARGIN_BOUNDS)){
			System.out.println("Eccede Altezza: y è "+newY+" la altezza è "+newHeight+" su un totale di "+heightMain);			
			return true;
		}
		else return false;
	}




	/**
	 *  Call to reload the style document properties view when a container is selected
	 * @param composite
	 */
	public void reloadStyleDocumentProperties(){
		Style style=calculateTemplateStyle();
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		// Document properties
		IWorkbenchPage aa=a.getActivePage();
		IViewReference w=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView");
		Object p=w.getPart(false);
		if(p!=null){
			DocumentPropertiesView view=(DocumentPropertiesView)p;
			view.reloadStyle(id, style.getStyle(), documentContained.getMetadataDocument());
		}
		else{
			// View not present

		}
	}

	/** Reload the view with document property and with document parameters
	 * 
	 * @param id
	 */

	public void reloadDocumentPropertiesView(String id){
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		try{
			// Document properties
			IWorkbenchPage aa=a.getActivePage();
			IViewReference w=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView");
			Object p=w.getPart(false);
			if(p!=null){
				DocumentPropertiesView view=(DocumentPropertiesView)p;
				view.reloadProperties(documentContained.getMetadataDocument());
			}
			else{
				// View not present

			}
			// Document parameters
			IViewReference wPars=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView");
			Object p2=wPars.getPart(false);
			if(p2!=null){
				DocumentParametersView docParameters=(DocumentParametersView)p2;
				docParameters.reloadProperties(documentContained.getMetadataDocument().getMetadataParameters());
			}
			else{
				// View Not present
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		int i=0;

	}
	/** Reload the view with navigations
	 * 
	 * @param id
	 */

	public void reloadNavigationView(String id){
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		try{
			// Document properties
			IWorkbenchPage aa=a.getActivePage();
			IViewReference w=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.NavigationView");
			Object p=w.getPart(false);
			if(p!=null){
				NavigationView view=(NavigationView)p;
				view.reloadNavigations(documentContained.getMetadataDocument());
			}
			else{
				// View not present

			}
			// Document parameters
			IViewReference wPars=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView");
			Object p2=wPars.getPart(false);
			if(p2!=null){
				DocumentParametersView docParameters=(DocumentParametersView)p2;
				docParameters.reloadProperties(documentContained.getMetadataDocument().getMetadataParameters());
			}
			else{
				// View Not present
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		int i=0;
	}

	public Designer getDesigner() {
		return designer;
	}


	public void setDesigner(Designer designer) {
		this.designer = designer;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}








}
