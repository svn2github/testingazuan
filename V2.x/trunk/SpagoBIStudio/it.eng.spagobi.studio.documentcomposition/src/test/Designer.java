package test;

import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
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
	

	public void addGroup(Composite mainComposite, int x, int y){
		//System.out.println("La x: "+Integer.valueOf(x).toString()+" la y: "+Integer.valueOf(y).toString());

		// shell check if overlaids or exceeds
		int tempWidth=DocContainer.DEFAULT_WIDTH;
		tempWidth=tempWidth/DocContainer.ALIGNMENT_MARGIN;
		tempWidth=tempWidth*DocContainer.ALIGNMENT_MARGIN;
		int tempHeight=DocContainer.DEFAULT_HEIGHT;
		tempHeight=tempHeight/DocContainer.ALIGNMENT_MARGIN;
		tempHeight=tempHeight*DocContainer.ALIGNMENT_MARGIN;

		Rectangle rectangle=new Rectangle(x,y, tempWidth, tempHeight);
		boolean doesExceed=DocContainer.doesExceed(Integer.valueOf(-1), this, x, y, tempWidth, tempHeight, false);
		boolean doesIntersect=DocContainer.doesIntersect(Integer.valueOf(-1), this, x, y, tempWidth, tempHeight, false);

		if(doesExceed==true){
			MessageDialog.openWarning(mainComposite.getShell(), 
					"Warning", "COntainer you want to insert exceeds shell!");
			return;
		}
		if(doesIntersect==true){
			MessageDialog.openWarning(mainComposite.getShell(), 
					"Warning", "COntainer you want to insert intersects other composites!");
			return;
		}

		DocContainer group=new DocContainer(this, mainComposite, x, y, tempWidth, tempHeight);
		containers.put(group.getId(), group);
		Composite g=group.getContainer();
		mainComposite.layout();
		mainComposite.update();
		mainComposite.redraw();
		mainComposite.setFocus();
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



	public void addShellMouseControls(final Composite shell){

		// get Selected composite if any
		//final Composite selected=currentSelection.intValue()!=-1 ? containers.get(currentSelection).getContainer() : null ;

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
						int tempWidth=selected.getBounds().width;
						tempWidth=tempWidth/DocContainer.ALIGNMENT_MARGIN;
						tempWidth=tempWidth*DocContainer.ALIGNMENT_MARGIN;
						int tempHeight=selected.getBounds().height;
						tempHeight=tempHeight/DocContainer.ALIGNMENT_MARGIN;
						tempHeight=tempHeight*DocContainer.ALIGNMENT_MARGIN;
						selected.setSize(tempWidth, tempHeight);						

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
									System.out.println("MUOVIMI");
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
					Composite selectedDoc2=currentSelection.intValue()!=-1 ? containers.get(currentSelection).getContainer() : null ;
					//System.out.println("Stampo coordinate: x = "+event.x+" su "+p.x+" | y = "+event.y+" su "+p.y);
					if(getState().equals(Designer.DRAG)){
						int tempX=selectedDoc2.getLocation().x;
						tempX=tempX/DocContainer.ALIGNMENT_MARGIN;
						tempX=tempX*DocContainer.ALIGNMENT_MARGIN;
						int tempY=selectedDoc2.getLocation().y;
						tempY=tempY/DocContainer.ALIGNMENT_MARGIN;
						tempY=tempY*DocContainer.ALIGNMENT_MARGIN;
						selectedDoc2.setLocation(tempX, tempY);

					}					
					break;
				}
			}
		};
		shell.addListener(SWT.MouseDown, listener);
		shell.addListener(SWT.MouseUp, listener);
		shell.addListener(SWT.MouseMove, listener);
	}



	public void addContextMenu(final Composite composite){
		composite.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				Menu menu = new Menu(composite.getShell(), SWT.POP_UP);
				MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText("Add Doc");
				item.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						if(getState().equals(Designer.NORMAL)){
							addGroup(composite, currentX, currentY);
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







}
