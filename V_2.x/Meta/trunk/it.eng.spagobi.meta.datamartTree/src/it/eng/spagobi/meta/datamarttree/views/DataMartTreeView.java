package it.eng.spagobi.meta.datamarttree.views;

import it.eng.qbe.model.structure.AbstractDataMartItem;
import it.eng.qbe.model.structure.DataMartCalculatedField;
import it.eng.qbe.model.structure.DataMartEntity;
import it.eng.qbe.model.structure.DataMartField;
import it.eng.qbe.model.structure.DataMartModelStructure;
import it.eng.spagobi.meta.datamarttree.bo.DatamartItem;
import it.eng.spagobi.meta.datamarttree.builder.DatamartSrtuctureBuilder;
import it.eng.spagobi.meta.datamarttree.draganddrop.DDListener;
import it.eng.spagobi.meta.datamarttree.draganddrop.DatamartFieldTransfer;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.osgi.framework.Bundle;

public class DataMartTreeView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "it.eng.spagobi.meta.datamarttree.views.DataMartTreeView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;


	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
		private DataMartEntity invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public AbstractDataMartItem getParent(Object child) {
			return ((AbstractDataMartItem)child).getParent();
		}
		public AbstractDataMartItem[] getChildren(Object parent) {
			List<AbstractDataMartItem> children = new ArrayList<AbstractDataMartItem>();
			if(parent instanceof DataMartEntity){
				children.addAll(((DataMartEntity) parent).getSubEntities());
				children.addAll(((DataMartEntity) parent).getAllFields());
				children.addAll(((DataMartEntity) parent).getCalculatedFields());
			}
			return children.toArray(new AbstractDataMartItem[0]);
		}
		public boolean hasChildren(Object parent) {
			if(parent instanceof DataMartEntity){
				if((((DataMartEntity) parent).getSubEntities()).size()>0)
					return true;
				if((((DataMartEntity) parent).getAllFields()).size()>0)
					return true;
				if((((DataMartEntity) parent).getCalculatedFields()).size()>0)
					return true;
			}
			return false;
		}
		
		/*
		 * We will set up a dummy model to initialize tree heararchy.
		 * In a real code, you will connect to a real model and
		 * expose its hierarchy.
		 */
		private void initialize() {
			
			DataMartModelStructure datamartStructure = DatamartSrtuctureBuilder.build();
			
			List<DataMartEntity> datamartFields = datamartStructure.getRootEntities("foodmart");
			
			DataMartEntity root = new DataMartEntity("datamart", "path", "role", "type", datamartStructure);
			for(int i=0; i<datamartFields.size(); i++){
				root.addSubEntity(datamartFields.get(i));
			}
					
			invisibleRoot = new DataMartEntity("datamart", "path", "role", "type", datamartStructure);
			invisibleRoot.addSubEntity(root);
		}
	}
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return ((AbstractDataMartItem)obj).getName();
		}
		public Image getImage(Object obj) {   
			
			String path = "D:/sviluppo/SpagoBIMeta/workspaceSpagoBIMeta/it.eng.spagobi.meta.datamartTree/img/datamartstructure/";
			
			if(obj instanceof DataMartEntity){
				path=path+"dimension.gif";
			} else if(obj instanceof DataMartField){
				DataMartField dm = (DataMartField)obj;
				path=path+"attribute.gif";
			} else if(obj instanceof DataMartCalculatedField){
				path=path+"calculation.gif";
			}else{
				path=path+"dimension.gif";
			}
			return new Image(viewer.getControl().getDisplay(), path); 
		}
		
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public DataMartTreeView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "it.eng.spagobi.meta.datamartTree.viewer");
		makeActions();
		
		hookContextMenu();
		//hookDoubleClickAction();
		hookDragAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				DataMartTreeView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	
	
	private void hookDragAction() {
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		//Transfer[] transfers = new Transfer[] { (DatamartFieldTransfer.getInstance())};
		   Transfer[] transfers = new Transfer[] {
				      DatamartFieldTransfer.getInstance(), PluginTransfer.getInstance()};

		viewer.addDragSupport(ops, transfers, new DDListener(viewer));

			  
		//viewer.addDropSupport(ops, transfers, new DDAdapter(viewer));

	}
	
	
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Sample View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}