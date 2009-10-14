package com.tensegrity.palowebviewer.modules.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.tensegrity.palowebviewer.modules.engine.client.IClientProperties;
import com.tensegrity.palowebviewer.modules.engine.client.IEngine;
import com.tensegrity.palowebviewer.modules.engine.client.IPaloServerModel;
import com.tensegrity.palowebviewer.modules.engine.client.l10n.PaloLocalizedStrings;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeTableView;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.ICubeTableModel;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.IDimensionList;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.IFormatter;
import com.tensegrity.palowebviewer.modules.ui.client.loaders.ILoaderCallback;
import com.tensegrity.palowebviewer.modules.ui.client.tree.PaloTreeModel;
import com.tensegrity.palowebviewer.modules.util.client.JavaScript;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.ITask;
import com.tensegrity.palowebviewer.modules.util.client.taskqueue.TaskQueue;
import com.tensegrity.palowebviewer.modules.widgets.client.IWidgetFactory;
import com.tensegrity.palowebviewer.modules.widgets.client.LabeledImage;
import com.tensegrity.palowebviewer.modules.widgets.client.LoadingLabel;
import com.tensegrity.palowebviewer.modules.widgets.client.dnd.DnDManager;
import com.tensegrity.palowebviewer.modules.widgets.client.section.HorizontalSection;
import com.tensegrity.palowebviewer.modules.widgets.client.section.VerticalSection;

/**
 * Widget that is a tab to display {@link XCubeEditor}. 
 * CSS: cube_title
 *
 */
public class CubeEditorView extends Composite {

    private final XCubeEditor xCubeEditor;
    private final UIManager uiManager;
    private final IFormatter formatter;

    private final SimplePanel panel;
    private FlexTable tabBody;
    private LabeledImage reloadDataButton;
    private DnDManager dndManager;
    private VerticalDimensionsPanel rowsPanel;
    private DimensionsPanel columnsPanel;
    private DimensionsPanel selectedPanel;
    private HorizontalSection topSection;
    private HorizontalSection columnSection;
    private VerticalSection rowSection;
    private CubeTableView cubeTableView;
    private Label hyperLink3;
    private final ILoaderCallback loaderCallback = new ILoaderCallback() {

		public void loaded() {
			TaskQueue.getInstance().add(new InitWidgetTask());
		}
    	
    };

    private Widget widget = null;

    private final PerformanceTimer cubeLoadTimer = new PerformanceTimer("CubeEditorView load time");

    private PaloLocalizedStrings localizedStrings;

    public CubeEditorView(XCubeEditor xCubeEditor, UIManager uiManager, IFormatter formatter) {
        if(xCubeEditor == null)
            throw new IllegalArgumentException("Editor can not be null");
        if(uiManager == null)
            throw new IllegalArgumentException("UIManager can not be null");
        this.uiManager = uiManager;
        this.xCubeEditor = xCubeEditor;
        this.formatter = formatter;
        cubeLoadTimer.start();
        localizedStrings = (PaloLocalizedStrings)GWT.create(PaloLocalizedStrings.class);
        xCubeEditor.addEditorListener(editorListener);
        
        panel = new SimplePanel();

        xCubeEditor.setLoaderCallback(loaderCallback);
        xCubeEditor.setShowLevels(uiManager.getClientProperties().getTDShowLevels());
        initWidget();
        if(uiManager.isEditorOnly()) {
        	RootPanel.get().add(panel);
        }
        else {
        	super.initWidget(panel);
        }
        xCubeEditor.load();
    }

    private void initWidget() {
        if (isLoaded()) {
            widget = createCubeWidget();
            cubeLoadTimer.report();
        } 
        else {
            if(widget == null)
                widget = createLoadingWidget();
        }
        widget.setWidth("100%");
        widget.setHeight("100%");

        panel.setWidth("100%");
        panel.setHeight("100%");

        panel.clear();
        panel.add(widget);
    }

	private boolean isLoaded() {
		return xCubeEditor.isLoaded();
	}

    private Widget createLoadingWidget() {
        return new LoadingLabel("Cube Loading...");
    }

    protected IWidgetFactory getWidgetFactory () {
        return getUIManager().getTreeWidgetFactory();
    }

    protected PaloTreeModel getPaloTreeModel() {
        return getUIManager().getPaloTreeModel();
    }

    protected DnDManager getDnDManager() {
        return dndManager;
    }

    protected void initDnDManager() {
        dndManager = new DnDManager();
    }

    public String getTitle() {
        XView view = xCubeEditor.getView();
        XCube cube = xCubeEditor.getCube();
        String result =  "Cube '" + cube.getName() + "'";
        if(view != null){
            result = "View '" + view.getName() + "' on " + result;
        }
        return result;
    }

    /**
     * xCube must bee initialized
     * 
     */
    protected Widget createCubeWidget() {
        initDnDManager();
        initDimensionPanels();
        initSections();
        initReloadButton();
        initTableView();
        createBody();
        return tabBody;
    }
    
    protected void initTableView() {
        cubeTableView = new CubeTableView(formatter);
        cubeTableView.setModel(xCubeEditor.getCubeTableModel());
        IClientProperties props = uiManager.getClientProperties();
        cubeTableView.setColumnMaxVisibleString(props.getColumnMaxVisibleString());
        cubeTableView.setColumnMinVisibleString(props.getColumnMinVisibleString());
        cubeTableView.setHintTime(props.getHintTime());
    }
    
    protected void createBody () {
        Label titleLable = createTitleLabel();
        initCornerLink();
        tabBody = new FlexTable();
        tabBody.setHeight("100%");
        tabBody.setWidget(0, 0, titleLable);
        tabBody.setWidget(1, 0, topSection);
        tabBody.setWidget(2, 0, hyperLink3);
        tabBody.setWidget(2, 1, columnSection);
        tabBody.setWidget(3, 0, rowSection);
        if(uiManager.isTableOnly()) {
        	RootPanel.get().add(cubeTableView.getWidget());
        }
        else {
        	tabBody.setWidget(3, 1, cubeTableView.getWidget() );
        }
        tabBody.setWidget(4, 0, reloadDataButton);

        FlexCellFormatter cellFormatter = tabBody.getFlexCellFormatter();
        cellFormatter.setColSpan(0, 0, 2);
        cellFormatter.setColSpan(1, 0, 2);
        cellFormatter.setWidth(3, 1, "100%");
        cellFormatter.setHeight(3, 1, "100%");
        JavaScript.disableSelection(tabBody);
    }

    protected void initReloadButton() {
        //TODO make text international
    	if(reloadDataButton == null) {
    		reloadDataButton = new LabeledImage("reload-table-button","Refresh");
    		reloadDataButton.addStyleName("tensegrity-gwt-clickable");
    		reloadDataButton.addStyleName("refresh_button");
    		reloadDataButton.addClickListener(reloadButtonListener);
    		JavaScript.disableSelection(reloadDataButton);
    	}
    }

    protected void initSections() {
        initTopSection();
        columnSection = new HorizontalSection("", true, columnsPanel);
        rowSection = new VerticalSection("", true, rowsPanel);
        rowSection.setHeight("100%");
        JavaScript.disableSelection(columnSection);
        JavaScript.disableSelection(rowSection);
    }

    protected void initCornerLink() {
        hyperLink3 = new Label("");
        hyperLink3.setStyleName("cube_corner");
    }

    protected void initDimensionPanels () {
        ICubeTableModel tableModel = xCubeEditor.getCubeTableModel();
        columnsPanel = creatDimensionPanel(tableModel.getXDimensions());
        rowsPanel = creatVerticalDimensionPanel(tableModel.getYDimensions());
        selectedPanel = creatDimensionPanel(tableModel.getSliceDimensions());
        columnsPanel.setDimensionSelectorVisible(false);
        rowsPanel.setDimensionSelectorVisible(false);
    }

    protected Label createTitleLabel() {
        String title = getTitle();
        Label result = new Label(title);
        result.setStyleName("cube_title");
        return result;
    }

    protected IPaloServerModel getPaloServerModel() {
		return getEngine().getPaloServerModel();
	}

	protected IEngine getEngine() {
		return getUIManager().getEngine();
	}

	protected void initTopSection() {
        VerticalPanel dimPanel = new VerticalPanel();
        Label text = new Label(localizedStrings.labelDragDimensions());
        dimPanel.add(text);
        dimPanel.setSpacing(5); //TODO: aren't those things have to be moved to CSS?
        dimPanel.add(selectedPanel);
        dimPanel.setWidth("100%");
        String title = localizedStrings.labelDimensions();
        topSection = new HorizontalSection( title , true, dimPanel);
        topSection.removeStyleName("tensegrity-gwt-section");
        topSection.addHeaderStyleName("dimensionsSectionHead");
        JavaScript.disableSelection(text);
        JavaScript.disableSelection(topSection);
    }

    protected DimensionsPanel creatDimensionPanel(IDimensionList dimensions) {
        DimensionsPanel result = new DimensionsPanel(getUIManager(), dimensions, getDnDManager(), getWidgetFactory());
        result.setWidth("100%");
        return result;
    }

    protected VerticalDimensionsPanel creatVerticalDimensionPanel(IDimensionList dimensions) {
        VerticalDimensionsPanel result = new VerticalDimensionsPanel(getUIManager(), dimensions, getDnDManager(), getWidgetFactory());
        result.setSpacing(5);//TODO: aren't those things have to be moved to CSS?
        result.setHeight("100%");
        return result;
    }

	private UIManager getUIManager() {
		return uiManager;
	}

    // -- Internal classes section --

    private IEditorListener editorListener = new IEditorListener() {

        public void onModified(IXObjectEditor editor) {} // ignore

        public void onUnmodified(IXObjectEditor editor) {} // ignore

        public void onSourceChanged(IXObjectEditor editor) {
        	xCubeEditor.load();
        }

		public void onObjectRenamed(IXObjectEditor editor) {
		}

    };

    private ClickListener reloadButtonListener = new ClickListener() {

        public void onClick(Widget sender) {
            if(xCubeEditor.getCubeTableModel() != null)
                xCubeEditor.getCubeTableModel().reloadData();
        }

    };
    
    private class InitWidgetTask implements ITask {

		public void execute() {
			initWidget();
		}

		public String getName() {
			return "InitWidgetTask";
		}
    	
    }

}
