package test;


import it.eng.spagobi.sdk.documents.bo.SDKDocumentParameter;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.core.util.SDKDocumentParameters;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Style;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView;
import it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
import org.osgi.framework.Bundle;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;


public class DocContainer {


	//	public static QualifiedName DOCUMENT_ID = new QualifiedName("it.eng.spagobi.sdk.document.id", "Identifier");
	//	public static QualifiedName DOCUMENT_LABEL = new QualifiedName("it.eng.spagobi.sdk.document.label", "Label");
	//	public static QualifiedName DOCUMENT_NAME = new QualifiedName("it.eng.spagobi.sdk.document.name", "Name");
	//	public static QualifiedName DOCUMENT_DESCRIPTION = new QualifiedName("it.eng.spagobi.sdk.document.description", "Description");
	//	public static QualifiedName DOCUMENT_TYPE = new QualifiedName("it.eng.spagobi.sdk.document.type", "Type");
	//	public static QualifiedName DOCUMENT_STATE = new QualifiedName("it.eng.spagobi.sdk.document.description", "State");
	//
	//	public static QualifiedName DATASET_ID = new QualifiedName("it.eng.spagobi.sdk.dataset.id", "Identifier");
	//	public static QualifiedName DATASET_LABEL = new QualifiedName("it.eng.spagobi.sdk.dataset.label", "Label");
	//	public static QualifiedName DATASET_NAME = new QualifiedName("it.eng.spagobi.sdk.dataset.name", "Name");
	//	public static QualifiedName DATASET_DESCRIPTION = new QualifiedName("it.eng.spagobi.sdk.dataset.description", "Description");
	//
	//	public static QualifiedName DATA_SOURCE_ID = new QualifiedName("it.eng.spagobi.sdk.datasource.id", "Identifier");
	//
	//	public static QualifiedName ENGINE_ID = new QualifiedName("it.eng.spagobi.sdk.engine.id", "Identifier");
	//	public static QualifiedName ENGINE_LABEL = new QualifiedName("it.eng.spagobi.sdk.engine.label", "Label");
	//	public static QualifiedName ENGINE_NAME = new QualifiedName("it.eng.spagobi.sdk.engine.name", "Name");
	//	public static QualifiedName ENGINE_DESCRIPTION = new QualifiedName("it.eng.spagobi.sdk.engine.description", "Description");
	//	public static QualifiedName DOCUMENT_PARAMETERS_XML = new QualifiedName("it.eng.spagobi.sdk.document.parametersxml", "Parameters");



	final Integer id;
	Designer designer;
	Group container;
	MetadataDocument metadataDocument;	

	String title="";

	public static final String TYPE_REPORT="REPORT";
	public static final String TYPE_DOSSIER="DOSSIER";
	public static final String TYPE_OLAP="OLAP";
	public static final String TYPE_DATA_MINING="DATA_MINING";
	public static final String TYPE_DASH="DASH";
	public static final String TYPE_DATAMART="DATAMART";
	public static final String TYPE_MAP="MAP";
	public static final String TYPE_OFFICE_DOC="OFFICE_DOC";
	public static final String TYPE_ETL="ETL";
	public static final String TYPE_DOCUMENT_COMPOSITIOn="DOCUMENT_COMPOSITION";


	public static final String IMG_JASPER_REPORT="it/eng/spagobi/studio/documentcomposition/resources/images/jasper.png";
	public static final String IMG_BIRT_REPORT="it/eng/spagobi/studio/documentcomposition/resources/images/birt.png";
	public static final String IMG_DASHBOARD="it/eng/spagobi/studio/documentcomposition/resources/images/chart.png";
	public static final String IMG_CHART="it/eng/spagobi/studio/documentcomposition/resources/images/chart.png";
	public static final String IMG_DOCUMENT_COMPOSITION="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_OLAP="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_ETL="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_OFFICE_DOC="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_MAP="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DATAMART="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DOSSIER="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";
	public static final String IMG_DATA_MINING="it/eng/spagobi/studio/documentcomposition/resources/images/olap.png";


	public static final int DEFAULT_WIDTH=200;
	public static final int DEFAULT_HEIGHT=200;
	public static final int MIN_MARGIN_BOUNDS=0;
	public static final int ALIGNMENT_MARGIN=30;

	public DocContainer(Designer _designer,Composite mainComposite, int x, int y, int tempWidth, int tempHeight) {
		super();
		designer=_designer;
		id=Integer.valueOf(designer.getCounter());

		container=new Group(mainComposite, SWT.NULL);

		title="NUMERO "+(new Integer(designer.getCounter()).toString());
		container.setText(title);
		GridLayout layout=new GridLayout();
		layout.numColumns=2;
		container.setLayout(layout);
		designer.incrementCounter();

		//		FormData formData = new FormData();
		//		formData.top = new FormAttachment(1,y);
		//		formData.left = new FormAttachment(1,x);
		//		formData.width=200;
		//		formData.height=200;
		//		container.setLayoutData(formData);


		container.setSize(tempWidth, tempHeight);
		container.setLocation(x, y);

		//		this.xPos = x;
		//		this.yPos= y;
		//		this.height = DEFAULT_HEIGHT;
		//		this.width = DEFAULT_WIDTH;	
		designer.setState(Designer.NORMAL);

		addContainerMouseControls(mainComposite, container);
		addContextMenu(mainComposite.getShell(), container);
		addDragAndDropDocument(container);

		container.layout();
		container.redraw();
		container.getParent().redraw();
		container.getParent().layout();

	}


	public void addContainerMouseControls(final Composite mainComposite, final Composite composite){
		final Point[] offset = new Point[1];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				;				switch (event.type) {
				case SWT.MouseDown:
					// Reload the DocumentPropertiesView
					if(metadataDocument!=null)
						reloadDocumentPropertiesView(id.toString());

					/**  IF in resizing state mouse button on Container causes end resizing**/	
					calculateTemplateStyle();
					if(designer.getState().equals(Designer.RESIZE)){
						// only if click on the current selected!
						if(id.equals(designer.getCurrentSelection())){
							designer.setState(Designer.NORMAL);
							offset[0] = null;							
							designer.setCurrentSelection(Integer.valueOf(-1));
							int tempWidth=container.getBounds().width;
							tempWidth=tempWidth/ALIGNMENT_MARGIN;
							tempWidth=tempWidth*ALIGNMENT_MARGIN;
							int tempHeight=container.getBounds().height;
							tempHeight=tempHeight/ALIGNMENT_MARGIN;
							tempHeight=tempHeight*ALIGNMENT_MARGIN;
							container.setSize(tempWidth, tempHeight);
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
						composite.setBackground(new Color(composite.getDisplay(),new RGB(0,255,0)));
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
								Composite toDeselect=designer.getContainers().get(idPreviousSel).getContainer();
								toDeselect.setBackground(new Color(toDeselect.getDisplay(),new RGB(200,200,200)));
							}
						}

						composite.setBackground(new Color(composite.getDisplay(),new RGB(0,255,0)));							
						designer.setState(Designer.DRAG);							
						designer.setCurrentSelection(id);
						Rectangle rect = composite.getBounds();
						Point pt1 = composite.toDisplay(0, 0);
						Point pt2 = mainComposite.toDisplay(event.x, event.y);
						offset[0] = new Point(pt2.x - pt1.x, pt2.y - pt1.y);
						//System.out.println("QUESTO E' UN TEST DA DENTRO CHE "+composite.getBounds().x+" = "+xPos+" e "+composite.getBounds().y+" = "+ yPos+" e poi altezza "+composite.getBounds().height+" = "+height+ " e larghezza  "+composite.getBounds().width+ " = "+width);

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
							if(x<rect.x+rect.width && x>(rect.x+DEFAULT_WIDTH)){
								nuova_larghezza=rect.width+(x-rect.x-rect.width);
								//composite.setSize(nuova_larghezza, rect.height);
							}
							if(y<rect.y+rect.height && y>(rect.y+DEFAULT_HEIGHT)){
								nuova_altezza=rect.height+(y-rect.y-rect.height);
							}
							//check if intersect!
							boolean doesIntersect=DocContainer.doesIntersect(id,designer,container.getLocation().x, container.getLocation().y, nuova_larghezza, nuova_altezza,false);
							boolean doesExceed=DocContainer.doesExceed(id,designer,container.getLocation().x, container.getLocation().y, nuova_larghezza, nuova_altezza,false);

							if(doesIntersect==false && doesExceed==false){
								System.out.println("STIRAMI!");
								composite.setSize(nuova_larghezza, nuova_altezza);
								//								width=nuova_larghezza;
								//								height=nuova_altezza;
								System.out.println("Resizing da dentro: x="+container.getBounds().x+" e y="+container.getBounds().y+" altezza nuova="+container.getBounds().height+" e larghezza nuova="+container.getBounds().width);														
							}	
							else{
								System.out.println("Resizing BLOCCATO da dentro: x="+container.getBounds().x+" e y="+container.getBounds().y+" altezza rimane="+container.getBounds().height+" e larghezza rimane="+container.getBounds().width);														
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
								//------------- Try Alignment Margin ------------
								//								int temp=newX/ALIGNMENT_MARGIN;
								//								newX=temp*ALIGNMENT_MARGIN;
								//								temp=newY/ALIGNMENT_MARGIN;
								//								newY=temp*ALIGNMENT_MARGIN;								
								//-------------								
								boolean doesIntersect=doesIntersect(id, designer,newX, newY, container.getBounds().width,container.getBounds().height,false);
								boolean doesExceed=doesExceed(id, designer,newX, newY, container.getBounds().width,container.getBounds().height,false);

								if(doesIntersect==false && doesExceed==false){
									System.out.println("MUOVIMI");
									composite.setLocation(newX, newY);
									System.out.println("Drag da dentro: x="+container.getBounds().x+" e y="+container.getBounds().y+" altezza nuova="+container.getBounds().height+" e larghezza nuova="+container.getBounds().width);														
								}
								else{
									System.out.println("Drag BLOCCATO da dentro: x="+container.getBounds().x+" e y="+container.getBounds().y+" altezza rimane="+container.getBounds().height+" e larghezza rimane="+container.getBounds().width);														
								}
								//								System.out.println("QUESTO E' UN TEST DA DENTRO CHE "+composite.getBounds().x+" = "+xPos+" e "+composite.getBounds().y+" = "+ yPos+" e poi altezza "+composite.getBounds().height+" = "+height+ " e larghezza  "+composite.getBounds().width+ " = "+width);

							}
						}			
					}
					break;
				case SWT.MouseUp:

					/**  IF in SELECTION state mouse up on container causes selection started from DRAG**/
					if(designer.getState().equals(Designer.DRAG)){
						//offset[0] = null;
						// ---------- Try alignment MArgin-----------

						int tempX=container.getLocation().x;
						tempX=tempX/ALIGNMENT_MARGIN;
						tempX=tempX*ALIGNMENT_MARGIN;
						int tempY=container.getLocation().y;
						tempY=tempY/ALIGNMENT_MARGIN;
						tempY=tempY*ALIGNMENT_MARGIN;
						container.setLocation(tempX, tempY);

						if(id.equals(designer.getCurrentSelection())){
							composite.setBackground(new Color(composite.getDisplay(),new RGB(0,0,255)));
							designer.setCurrentSelection(id);
							designer.setState(Designer.SELECTION);
							designer.setCurrentSelection(id);
						}
					}		
					else if(designer.getState().equals(Designer.SELECTION)){
						if(designer.getCurrentSelection().equals(id)){
							designer.setState(Designer.NORMAL);
							designer.setCurrentSelection(Integer.valueOf(-1));
							offset[0] = null;							
						}
					}					
					break;
				}
			}
		};
		composite.addListener(SWT.MouseDown, listener);
		composite.addListener(SWT.MouseUp, listener);
		composite.addListener(SWT.MouseMove, listener);
	}









	public void addContextMenu(final Composite mainComposite, final Composite composite){
		composite.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				Menu menu = new Menu(mainComposite.getShell(), SWT.POP_UP);
				MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText("Resize");
				item.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						composite.setBackground(new Color(composite.getDisplay(),new RGB(255,0,0)));
						designer.setState(Designer.RESIZE);
					}
				});
				MenuItem delItem = new MenuItem(menu, SWT.PUSH);
				delItem.setText("Delete Doc");
				delItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						Integer idSel=designer.getCurrentSelection();
						String title=designer.getContainers().get(idSel).getContainer().getText();

						designer.getContainers().remove(idSel);
						composite.dispose();
						designer.getMainComposite().redraw();
						designer.getMainComposite().layout();

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




	public Group getContainer() {
		return container;
	}


	public void setContainer(Group container) {
		this.container = container;
	}


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
				//				if (textTransfer.isSupportedType(event.currentDataType)) {
				//					String text = (String)event.data;
				//					Label label=new Label(composite, SWT.NULL);
				//					label.setText("CIAOOOOO");
				//				}
				boolean doTransfer=false;
				if (localTransfer.isSupportedType(event.currentDataType)){
					Object selectedObject = event.data;
					if(selectedObject instanceof TreeSelection)
					{
						TreeSelection selectedTreeSelection=(TreeSelection)selectedObject;
						IFile file=(IFile)selectedTreeSelection.getFirstElement();
						doTransfer=viewDocumentMetadata(file);

						//						IElementComparer el=selectedTreeSelection.getElementComparer();
						//						org.eclipse.jface.viewers.TreePath[] selectedTreePathes=selectedTreeSelection.getPaths();
						//						TreePath selectedTreePath=selectedTreePathes[0];
					}

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


	public boolean viewDocumentMetadata(IFile file){

		try{
			int i=0;
			String id=file.getPersistentProperty(PropertyPage.DOCUMENT_ID);
			if(metadataDocument!=null){
				MessageDialog.openWarning(container.getShell(), 
						"Warning", "Container has already Document in!");
				return false;
			}
			else if(id==null){
				MessageDialog.openWarning(container.getShell(), 
						"Warning", "Chosen file has no SpagoBI document metadata");
				return false;
			}
			else{			
				i++;

				String documentName=file.getPersistentProperty(PropertyPage.DOCUMENT_NAME);
				String documentType=file.getPersistentProperty(PropertyPage.DOCUMENT_TYPE);
				String documentEngineId=file.getPersistentProperty(PropertyPage.ENGINE_ID);
				String documentLabel=file.getPersistentProperty(PropertyPage.DOCUMENT_LABEL);
				String documentState=file.getPersistentProperty(PropertyPage.DOCUMENT_STATE);
				String documentDescription=file.getPersistentProperty(PropertyPage.DOCUMENT_DESCRIPTION);
				String documentDatasetId=file.getPersistentProperty(PropertyPage.DATASET_ID);
				String documentDatasourceId=file.getPersistentProperty(PropertyPage.DATA_SOURCE_ID);
				String xmlParameters=file.getPersistentProperty(PropertyPage.DOCUMENT_PARAMETERS_XML);

				//			Label idLabelName=new Label(container,SWT.NULL);
				//			idLabelName.setText("id: ");
				//			Label idLabelValue=new Label(container,SWT.NULL);
				//			idLabelValue.setText(id != null ? id : "" );

				// get the image Path
				if(documentType!=null){
					String imagePath=null;
					if(documentType.equalsIgnoreCase(TYPE_REPORT)){
						imagePath=IMG_JASPER_REPORT;
					}
					else if(documentType.equalsIgnoreCase(TYPE_DASH)){
						imagePath=IMG_DASHBOARD;
					}
					else if(documentType.equalsIgnoreCase(TYPE_DATA_MINING)){
						imagePath=IMG_DATA_MINING;
					}
					else if(documentType.equalsIgnoreCase(TYPE_DATAMART)){
						imagePath=IMG_DATAMART;
					}
					else if(documentType.equalsIgnoreCase(TYPE_DOSSIER)){
						imagePath=IMG_DOSSIER;
					}
					else if(documentType.equalsIgnoreCase(TYPE_DOCUMENT_COMPOSITIOn)){
						imagePath=IMG_DOCUMENT_COMPOSITION;
					}
					else if(documentType.equalsIgnoreCase(TYPE_MAP)){
						imagePath=IMG_MAP;
					}
					else if(documentType.equalsIgnoreCase(TYPE_ETL)){
						imagePath=IMG_ETL;
					}
					else if(documentType.equalsIgnoreCase(TYPE_OFFICE_DOC)){
						imagePath=IMG_OFFICE_DOC;
					}


					if(imagePath!=null){
						final String imagePathFinal=imagePath;
						container.addPaintListener(new PaintListener() {
							public void paintControl(PaintEvent e) {
								Image image = null;
								try {
									InputStream is=getInputStreamFromResource(imagePathFinal);
									image = new Image(container.getDisplay(), is);
								} catch (FileNotFoundException e1) {
									e1.printStackTrace();
								}
								catch (Exception e2) {

									e2.printStackTrace();
								}
								e.gc.drawImage(image, 20, 30);
								image.dispose();
							}
						});
						container.redraw();
					}
				}

				container.setText(file.getName());
				Label nameLabelName=new Label(container,SWT.NULL);
				nameLabelName.setText("Name: ");			
				Label nameLabelValue=new Label(container,SWT.NULL);
				nameLabelValue.setText(documentName != null ? documentName : "" );

				metadataDocument=new MetadataDocument();
				metadataDocument.setId(Integer.valueOf(id));
				metadataDocument.setLabel(documentLabel);
				metadataDocument.setDescription(documentDescription);
				metadataDocument.setType(documentType);
				metadataDocument.setName(documentName);
				metadataDocument.setEngine(documentEngineId);
				metadataDocument.setDataSet(documentDatasetId);
				metadataDocument.setDataSource(documentDatasourceId);
				metadataDocument.setState(documentState);


				if(xmlParameters!=null && !xmlParameters.equalsIgnoreCase(""))
				{
					XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("grfthscv", "_");
					XStream xstream = new XStream(new DomDriver("UTF-8", replacer)); 
					xstream.alias("SDK_DOCUMENT_PARAMETERS", SDKDocumentParameters.class);
					xstream.alias("PARAMETER", SDKDocumentParameter.class);
					xstream.useAttributeFor(SDKDocumentParameter.class, "id");
					xstream.useAttributeFor(SDKDocumentParameter.class, "label");
					xstream.useAttributeFor(SDKDocumentParameter.class, "type");
					xstream.useAttributeFor(SDKDocumentParameter.class, "urlName");
					xstream.omitField(SDKDocumentParameter.class, "values");		
					xstream.omitField(SDKDocumentParameter.class, "constraints");
					xstream.omitField(SDKDocumentParameter.class, "__hashCodeCalc");
					SDKDocumentParameters parametersMetaDataObject= (SDKDocumentParameters)xstream.fromXML(xmlParameters);
					metadataDocument.buildMetadataParameters(parametersMetaDataObject);
				}
			}
		}
		catch (Exception e) {	
			e.printStackTrace();
			SpagoBILogger.errorLog("Exception while retrieving metadata",e);
			return false;
		}
		catch (Throwable e) {
			e.printStackTrace();
			SpagoBILogger.errorLog("Exception while retrieving metadata",e);
			return false;
		}

		return true;
	}

	/** Get input stream from a resource
	 * 
	 * @param resourcePath
	 * @return
	 * @throws IOException
	 */

	public static InputStream getInputStreamFromResource(String resourcePath) throws IOException {
		Bundle b = org.eclipse.core.runtime.Platform.getBundle(it.eng.spagobi.studio.documentcomposition.Activator.PLUGIN_ID);
		URL res = b.getResource(resourcePath);
		InputStream is = res.openStream();
		return is;
	}

	public Style calculateTemplateStyle(){
		Style style=new Style();	
		String toAdd="float:left;margin:0px;";

		// get the bounds
		Point location=container.getLocation();
		int x=location.x;
		int y=location.y;

		Rectangle rect=container.getBounds();
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
				Group otherGroup=otherContainer.getContainer();
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


	/** Reload the view with document property and with document parameters
	 * 
	 * @param id
	 */

	public void reloadDocumentPropertiesView(String id){
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		try{
			// Document properties
			IWorkbenchPage aa=a.getActivePage();
			IViewReference w=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentProperties");
			Object p=w.getPart(false);
			DocumentPropertiesView view=(DocumentPropertiesView)p;
			view.reloadProperties(metadataDocument);

			// Document parameters
			IViewReference wPars=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentParameters");
			Object p2=wPars.getPart(false);
			DocumentParametersView docParameters=(DocumentParametersView)p2;
			docParameters.reloadProperties(metadataDocument.getMetadataParameters());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		int i=0;

	}


	//	protected boolean doesIntersect (int newX, int newY, int newWidth, int newHeight){
	//		boolean doesIntersect=false;
	//		Vector<Point> points=new Vector<Point>();
	//		Point leftTopPoint=new Point(newX,newY);
	//		Point rightTopPoint=new Point(newX+newWidth,newY);
	//		Point leftBottomPoint=new Point(newX,newY+newHeight);
	//		Point rightBottomPoint=new Point(newX+newWidth,newY+newHeight);
	//		points.add(leftTopPoint);
	//		points.add(rightTopPoint);
	//		points.add(leftBottomPoint);
	//		points.add(rightBottomPoint);
	//
	//		for (Iterator iterator = designer.getContainers().keySet().iterator(); iterator.hasNext() && doesIntersect==false;) {
	//			Integer idOther = (Integer) iterator.next();
	//			if(!idOther.equals(id)){
	//				DocContainer otherContainer = designer.getContainers().get(idOther);
	//				Group otherGroup=otherContainer.getContainer();
	//				Rectangle otherRectangle=otherGroup.getBounds();
	//				for (Iterator iterator2 = points.iterator(); iterator2.hasNext() && doesIntersect==false;) {
	//					Point point = (Point) iterator2.next();
	//					doesIntersect=otherRectangle.contains(point);	
	//				}
	//			}
	//		}
	//		return doesIntersect;
	//	}





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
