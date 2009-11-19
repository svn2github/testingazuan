package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.sdk.datasets.bo.SDKDataSet;
import it.eng.spagobi.sdk.exceptions.MissingParameterValue;
import it.eng.spagobi.sdk.proxy.DataSetsSDKServiceProxy;
import it.eng.spagobi.studio.core.bo.DataStoreMetadata;
import it.eng.spagobi.studio.core.bo.DataStoreMetadataField;
import it.eng.spagobi.studio.core.bo.Dataset;
import it.eng.spagobi.studio.core.bo.GeoFeature;
import it.eng.spagobi.studio.core.bo.GeoMap;
import it.eng.spagobi.studio.core.bo.SpagoBIServerObjects;
import it.eng.spagobi.studio.core.exceptions.NoServerException;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.core.sdk.SDKProxyFactory;
import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.bo.ColumnBO;
import it.eng.spagobi.studio.geo.editors.model.bo.DatasetBO;
import it.eng.spagobi.studio.geo.editors.model.bo.LayerBO;
import it.eng.spagobi.studio.geo.editors.model.bo.LayersBO;
import it.eng.spagobi.studio.geo.editors.model.bo.MetadataBO;
import it.eng.spagobi.studio.geo.editors.model.bo.ModelBO;
import it.eng.spagobi.studio.geo.editors.model.geo.Column;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Layer;
import it.eng.spagobi.studio.geo.editors.model.geo.Layers;
import it.eng.spagobi.studio.geo.editors.model.geo.Metadata;
import it.eng.spagobi.studio.geo.util.DeepCopy;
import it.eng.spagobi.studio.geo.util.DesignerUtils;
import it.eng.spagobi.studio.geo.util.XmlTemplateGenerator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class GEOEditor extends EditorPart {

	protected boolean isDirty = false;
	final ImageDescriptor measureIcon = AbstractUIPlugin
			.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/measure.gif");

	private Vector<String> dataSets;
	private Vector<String> maps;
	private HashMap<String, Dataset> datasetInfos;
	private HashMap<String, GeoMap> mapInfos;

	private HashMap<String, DataStoreMetadata> tempDsMetadataInfos;
	private HashMap<String, GeoFeature[]> tempMapMetadataInfos;

	private String selectedDataset;
	private String selectedMap;
	private Table datasetTable;
	private Combo datasetCombo;
	private Table mapTable;

	private Vector<TableEditor> datasetTableEditors = new Vector<TableEditor>();
	private Vector<TableEditor> mapTableEditors = new Vector<TableEditor>();
	private MeasuresDesigner measuresDesigner;

	private static final int DATASET_NAME = 0;
	private static final int DATASET_CLASS = 1;
	private static final int DATASET_SELECT = 2;
	private static final int DATASET_AGGREGATION = 3;

	private static final int FEATURE_NAME = 0;
	private static final int FEATURE_DESCR = 1;
	private static final int FEATURE_DEFAULT_LEVEL = 2;
	private static final int FEATURE_DEFAULT_COLORS = 3;

	private GEODocument geoDocument;

	public void init(IEditorSite site, IEditorInput input) {
		try {
			this.setPartName(input.getName());

			QualifiedName ciao = PropertyPage.MADE_WITH_STUDIO;
			FileEditorInput fei = (FileEditorInput) input;
			IFile file = fei.getFile();
			ModelBO bo = new ModelBO();
			try {
				geoDocument = bo.createModel(file);
				bo.saveModel(geoDocument);

			} catch (CoreException e) {
				e.printStackTrace();
				SpagoBILogger.errorLog(GEOEditor.class.toString()
						+ ": Error in reading template", e);
				throw (new PartInitException("Error in reading template"));
			}
			setInput(input);
			setSite(site);

			mapInfos = new HashMap<String, GeoMap>();
			datasetInfos = new HashMap<String, Dataset>();
			tempDsMetadataInfos = new HashMap<String, DataStoreMetadata>();
			tempMapMetadataInfos = new HashMap<String, GeoFeature[]>();
		} catch (Exception e) {
			SpagoBILogger.warningLog("Error occurred:" + e.getMessage());
		}
	}

	public void initializeEditor(GEODocument geoDocument) {
		SpagoBILogger.infoLog("START: " + GEOEditor.class.toString()
				+ " initialize Editor");
		// clean the properties View
		IWorkbenchWindow a = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		// Document properties
		IWorkbenchPage aa = a.getActivePage();

		SDKDataSet[] sdkDataSets = null;
		try {
			SDKProxyFactory proxyFactory = new SDKProxyFactory();
			DataSetsSDKServiceProxy dataSetsServiceProxy = proxyFactory
					.getDataSetsSDKServiceProxy();
			sdkDataSets = dataSetsServiceProxy.getDataSets();
			int i = 0;
		} catch (Exception e) {
			SpagoBILogger
					.errorLog(
							"No comunication with SpagoBI server, could not retrieve dataset informations",
							e);
		}

		SpagoBIServerObjects sbso = new SpagoBIServerObjects();
		Vector<Dataset> datasetVector;
		try {
			datasetVector = sbso.getAllDatasets();

			for (Iterator iterator = datasetVector.iterator(); iterator
					.hasNext();) {
				Dataset dataset = (Dataset) iterator.next();
				datasetInfos.put(dataset.getLabel(), dataset);
			}
			Vector<GeoMap> mapVector = sbso.getAllGeoMaps();
			for (Iterator iterator = mapVector.iterator(); iterator.hasNext();) {
				GeoMap geoMap = (GeoMap) iterator.next();
				mapInfos.put(geoMap.getName(), geoMap);
			}
		} catch (NoServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SpagoBILogger.infoLog("END: " + GEOEditor.class.toString()
				+ " initialize Editor");
	}

	@Override
	public void createPartControl(Composite parent) {

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 20;
		layout.verticalSpacing = 10;
		layout.topMargin = 20;
		layout.leftMargin = 20;

		form.getBody().setLayout(layout);

		final Section section = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR | SWT.RESIZE | SWT.TOP);

		section.setSize(1000, 1000);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				// parent.setSize(width, height);
				form.reflow(true);
			}
		});
		section.setText("GEO designer");

		Composite sectionClient = toolkit.createComposite(section, SWT.RESIZE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.makeColumnsEqualWidth = true;
		gl.marginHeight = 5;
		gl.marginRight = 5;
		gl.marginLeft = 5;
		sectionClient.setLayout(gl);

		HierarchiesDesigner designer = new HierarchiesDesigner(sectionClient,
				this);

		geoDocument = Activator.getDefault().getGeoDocument();
		designer.setGeoDocument(geoDocument);
		measuresDesigner = new MeasuresDesigner(sectionClient, this,
				geoDocument);

		initializeEditor(geoDocument);
		// creazione delle combo e tabelle

		Group datasetGroup = new Group(sectionClient, SWT.FILL);
		datasetGroup.setSize(800, 600);
		datasetGroup.setLayout(sectionClient.getLayout());
		Group mapGroup = new Group(sectionClient, SWT.FILL);
		mapGroup.setSize(800, 600);
		mapGroup.setLayout(sectionClient.getLayout());

		createDatasetCombo(sectionClient, datasetGroup);
		createMapCombo(sectionClient, mapGroup);

		createDatasetTable(sectionClient, datasetGroup);
		createMapTable(sectionClient, mapGroup);

		designer.createHierarchiesTree(sectionClient, toolkit);
		CrossNavigationDesigner crossNavigationDesigner = new CrossNavigationDesigner(sectionClient, this, geoDocument);
		crossNavigationDesigner.createCrossnavigationTable(sectionClient, toolkit);
		
		section.setClient(sectionClient);

		section.pack();
		sectionClient.pack();

		SpagoBILogger.infoLog("END " + GEOEditor.class.toString()
				+ ": create Part Control function");

	}

	private void createDatasetCombo(final Composite sectionClient,
			final Group datasetGroup) {

		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = SWT.END;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth = 120;
		gd.verticalAlignment = SWT.TOP;

		Label datasetLabel = new Label(datasetGroup, SWT.SIMPLE);
		datasetLabel.setText("Data Set");
		datasetLabel.setAlignment(SWT.RIGHT);

		Metadata metadata = MetadataBO.getMetadata(geoDocument);

		datasetCombo = new Combo(datasetGroup, SWT.SIMPLE | SWT.DROP_DOWN
				| SWT.READ_ONLY);
		int index = 0;
		Iterator<String> iterator = datasetInfos.keySet().iterator();
		while (iterator.hasNext()) {
			String name = (String) iterator.next();
			datasetCombo.add(name);
			if (metadata != null && metadata.getDataset() != null
					&& metadata.getDataset().equals(name)) {
				datasetCombo.select(index);
			}
			index++;
		}

		datasetCombo.setLayoutData(gd);

		datasetCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				// Once selected the dataset fill the table with its metadata,
				// check first if they have been already recovered!
				datasetTable.removeAll();
				datasetTable.setItemCount(0);
				datasetTable.pack();

				if (datasetTableEditors != null) {
					for (int i = 0; i < datasetTableEditors.size(); i++) {
						TableEditor editor = datasetTableEditors.elementAt(i);
						Control old = editor.getEditor();
						if (old != null)
							old.dispose();
					}
				}
				int indexSelection = datasetCombo.getSelectionIndex();
				String datasetLabel = datasetCombo.getItem(indexSelection);
				selectedDataset = datasetLabel;
				DataStoreMetadata dataStoreMetadata = null;
				// get the metadata
				if (tempDsMetadataInfos.get(datasetLabel) != null) {
					dataStoreMetadata = tempDsMetadataInfos.get(datasetLabel);
				} else {
					Dataset dataset = datasetInfos.get(datasetLabel);
					it.eng.spagobi.studio.geo.editors.model.geo.Dataset datasetGeo = DatasetBO
							.setNewDataset(geoDocument, dataset.getJdbcQuery());
					Integer datasourceId = dataset.getJdbcDataSourceId();

					// DatasourceBO.addDatasource(datasetGeo, type, driver, url,
					// user, password);
					try {
						dataStoreMetadata = new SpagoBIServerObjects()
								.getDataStoreMetadata(dataset.getId());
						if (dataStoreMetadata != null) {
							tempDsMetadataInfos.put(datasetLabel,
									dataStoreMetadata);
						} else {
							SpagoBILogger
									.warningLog("Dataset returned no metadata");
							MessageDialog.openWarning(sectionClient.getShell(),
									"Warning", "Dataset with label = "
											+ datasetLabel
											+ " returned no metadata");
						}
					} catch (MissingParameterValue e2) {
						SpagoBILogger
								.errorLog(
										"Could not execute dataset with label = "
												+ datasetLabel
												+ " metadata: probably missing parameter",
										e2);
						MessageDialog
								.openError(
										sectionClient.getShell(),
										"Error",
										"Could not execute dataset with label = "
												+ datasetLabel
												+ " metadata: probably missing parameter");
					} catch (NoServerException e1) {
						SpagoBILogger.errorLog(
								"Error No comunciation with server retrieving dataset with label = "
										+ datasetLabel + " metadata", e1);
						MessageDialog.openError(sectionClient.getShell(),
								"Error",
								"No comunciation with server retrieving dataset with label = "
										+ datasetLabel + " metadata");
					}
				}
				if (dataStoreMetadata != null) {
					fillDatasetTable(dataStoreMetadata, true);
				}
				// resize the row height using a MeasureItem listener
				datasetTable.addListener(SWT.MeasureItem, new Listener() {
					public void handleEvent(Event event) {
						// height cannot be per row so simply set
						event.height = 20;
					}
				});

				// listener per measures --> right click
				datasetTable.addListener(SWT.MouseDown, new Listener() {
					public void handleEvent(Event event) {
						if (event.button == 3) {
							TableItem[] selection = datasetTable.getSelection();
							if (selection[0].getText(2) != null
									&& selection[0].getText(2)
											.equalsIgnoreCase("measures")) {
								measuresDesigner.createMeasuresShell(
										sectionClient, selection[0].getText(0));
							} else {
								MessageDialog.openWarning(sectionClient
										.getShell(), "Warning",
										"No measure in selected column");
							}
						}
					}
				});

				sectionClient.getParent().pack();
				sectionClient.getParent().redraw();
				setIsDirty(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void createDatasetTable(final Composite sectionClient,
			Group datasetGroup) {

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;

		datasetTable = new Table(datasetGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION | SWT.CHECK);
		datasetTable.setLayoutData(gd);
		datasetTable.setLinesVisible(true);
		datasetTable.setHeaderVisible(true);

		String[] titles = { "  Column name   ",
				"               Type               ", "     Select       ",
				"   Aggregation mode   " };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(datasetTable, SWT.NONE);
			column.setText(titles[i]);
			// column.setResizable(true);

		}
		// look up for metadata stored in geodocument
		final Metadata metadata = MetadataBO.getMetadata(geoDocument);

		if (metadata != null && metadata.getDataset() != null
				&& !metadata.getDataset().equals("")) {
			selectDataset(sectionClient, metadata);

		} else {
			for (int i = 0; i < 10; i++) {
				TableItem item = new TableItem(datasetTable, SWT.TRANSPARENT);
			}
		}
		for (int i = 0; i < titles.length; i++) {
			datasetTable.getColumn(i).pack();
		}
		// resize the row height using a MeasureItem listener
		datasetTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// height cannot be per row so simply set
				event.height = 20;
			}
		});
		datasetTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TableItem item = (TableItem)e.item;
				if(item != null){
					String selType = e.detail == SWT.CHECK ? "Checked" : "Selected";
					if(selType != null && selType.equals("Checked")){
						String columnName = item.getText();
						Column col = ColumnBO.getColumnByName(geoDocument, columnName);
						col.setChoosenForTemplate(item.getChecked());
						setIsDirty(true);
					}
				}
			}
		});
		// listener per measures --> right click
		datasetTable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 3) {
					TableItem[] selection = datasetTable.getSelection();
					// find the column
					Column col = ColumnBO.getColumnByName(geoDocument,
							selection[0].getText());

					if (col != null
							&& col.getType().equalsIgnoreCase("measures")) {
						measuresDesigner.createMeasuresShell(sectionClient, col
								.getColumnId());
					} else {
						MessageDialog.openWarning(sectionClient.getShell(),
								"Warning", "No measure in selected column");
					}
				}
			}
		});
		datasetTable.redraw();

	}

	private void selectFeature(Composite sectionClient, Layers layers) {
		try {
			selectedMap = layers.getMapName();
			GeoMap geoMap = mapInfos.get(selectedMap);
			GeoFeature[] geoFeatures = new SpagoBIServerObjects()
					.getFeaturesByMapId(geoMap.getMapId());
			if (geoFeatures != null) {
				tempMapMetadataInfos.put(selectedMap, geoFeatures);
				fillMapTable(geoFeatures, sectionClient, false);
			} else {
				SpagoBILogger
						.warningLog("No features returned from map with label "
								+ selectedMap);
				MessageDialog.openWarning(sectionClient.getShell(), "Warning",
						"No features returned from map with label "
								+ selectedMap);
			}
		} catch (NoServerException e1) {
			SpagoBILogger.errorLog(
					"Could not get features associated to map with label = "
							+ selectedMap, e1);
			MessageDialog.openError(sectionClient.getShell(), "Error",
					"Could not get features associated to map with label = "
							+ selectedMap);
		}
	}

	private void selectDataset(Composite sectionClient, Metadata metadata) {

		try {
			selectedDataset = metadata.getDataset();
			Dataset dataset = datasetInfos.get(metadata.getDataset());
			DataStoreMetadata dataStoreMetadata = new SpagoBIServerObjects()
					.getDataStoreMetadata(dataset.getId());
			if (dataStoreMetadata != null) {
				tempDsMetadataInfos.put(metadata.getDataset(),
						dataStoreMetadata);
				fillDatasetTable(dataStoreMetadata, false);
			} else {
				SpagoBILogger.warningLog("Dataset returned no metadata");
				MessageDialog.openWarning(sectionClient.getShell(), "Warning",
						"Dataset with label = " + metadata.getDataset()
								+ " returned no metadata");
			}
		} catch (MissingParameterValue e2) {
			SpagoBILogger.errorLog("Could not execute dataset with label = "
					+ metadata.getDataset()
					+ " metadata: probably missing parameter", e2);
			MessageDialog.openError(sectionClient.getShell(), "Error",
					"Could not execute dataset with label = "
							+ metadata.getDataset()
							+ " metadata: probably missing parameter");
		} catch (NoServerException e1) {
			SpagoBILogger.errorLog(
					"Error No comunciation with server retrieving dataset with label = "
							+ metadata.getDataset() + " metadata", e1);
			MessageDialog.openError(sectionClient.getShell(), "Error",
					"No comunciation with server retrieving dataset with label = "
							+ metadata.getDataset() + " metadata");
		}
	}

	private void createMapCombo(final Composite sectionClient, Group mapGroup) {

		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = SWT.END;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth = 120;
		gd.verticalAlignment = SWT.TOP;

		Label mapLabel = new Label(mapGroup, SWT.SIMPLE);
		mapLabel.setText("Map");
		mapLabel.setAlignment(SWT.RIGHT);

		Layers layers = LayersBO.getLayers(geoDocument);

		final Combo mapCombo = new Combo(mapGroup, SWT.SIMPLE | SWT.DROP_DOWN
				| SWT.READ_ONLY);
		int index = 0;
		for (Iterator<String> iterator = mapInfos.keySet().iterator(); iterator
				.hasNext();) {
			String name = (String) iterator.next();
			mapCombo.add(name);
			if (layers != null && layers.getMapName() != null
					&& layers.getMapName().equals(name)) {
				mapCombo.select(index);
			}
			index++;
		}

		mapCombo.setLayoutData(gd);

		mapCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				// Once selected the dataset fill the table with its metadata,
				// check first if they have been already recovered!
				mapTable.removeAll();
				mapTable.setItemCount(0);
				mapTable.pack();

				if (mapTableEditors != null) {
					for (int i = 0; i < mapTableEditors.size(); i++) {
						TableEditor editor = mapTableEditors.elementAt(i);
						Control old = editor.getEditor();
						if (old != null)
							old.dispose();
					}
				}
				int indexSelection = mapCombo.getSelectionIndex();
				String mapLabel = mapCombo.getItem(indexSelection);
				selectedMap = mapLabel;
				GeoFeature[] geoFeatures = null;
				// get the metadata
				if (tempMapMetadataInfos.get(mapLabel) != null) {
					geoFeatures = tempMapMetadataInfos.get(mapLabel);
				} else {
					GeoMap geoMap = mapInfos.get(mapLabel);
					try {
						geoFeatures = new SpagoBIServerObjects()
								.getFeaturesByMapId(geoMap.getMapId());
						if (geoFeatures != null) {
							tempMapMetadataInfos.put(mapLabel, geoFeatures);
						} else {
							SpagoBILogger
									.warningLog("No features returned from map with label "
											+ mapLabel);
							MessageDialog.openWarning(sectionClient.getShell(),
									"Warning",
									"No features returned from map with label "
											+ mapLabel);
						}
					} catch (NoServerException e1) {
						SpagoBILogger.errorLog(
								"Could not get features associated to map with label = "
										+ mapLabel, e1);
						MessageDialog.openError(sectionClient.getShell(),
								"Error",
								"Could not get features associated to map with label = "
										+ mapLabel);
					}
				}
				if (geoFeatures != null) {

					fillMapTable(geoFeatures, sectionClient, true);
				}
				// resize the row height using a MeasureItem listener
				mapTable.addListener(SWT.MeasureItem, new Listener() {
					public void handleEvent(Event event) {
						// height cannot be per row so simply set
						event.height = 25;
					}
				});

				mapTable.pack();

				sectionClient.getParent().pack();
				sectionClient.getParent().redraw();

				setIsDirty(true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	private void createMapTable(Composite sectionClient, Group mapGroup) {

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;

		mapTable = new Table(mapGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION | SWT.CHECK);
		mapTable.setLayoutData(gd);
		mapTable.setLinesVisible(true);
		mapTable.setHeaderVisible(true);

		String[] titles = { "   Feature name      ",
				"           Description        ", "Select Default ",
				"  Default Color    " };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(mapTable, SWT.NONE);
			column.setText(titles[i]);
			// column.setResizable(true);
		}
		Layers layers = LayersBO.getLayers(geoDocument);

		if (layers != null && layers.getMapName() != null
				&& !layers.getMapName().equals("")) {
			selectFeature(sectionClient, layers);

		} else {
			for (int i = 0; i < 10; i++) {
				TableItem item = new TableItem(mapTable, SWT.TRANSPARENT);
			}
		}

		for (int i = 0; i < titles.length; i++) {
			mapTable.getColumn(i).pack();
		}
		// resize the row height using a MeasureItem listener
		mapTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// height cannot be per row so simply set
				event.height = 25;

			}
		});
		mapTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TableItem item = (TableItem)e.item;
				if(item != null){
					String selType = e.detail == SWT.CHECK ? "Checked" : "Selected";
					if(selType != null && selType.equals("Checked")){
						String featureId = item.getText();
						Layer layer = LayerBO.getLayerByName(geoDocument, featureId);
						layer.setChoosenForTemplate(item.getChecked());
						setIsDirty(true);
					}
				}
			}
		});
		mapTable.redraw();

	}

	private void fillMapTable(GeoFeature[] geoFeatures,
			Composite sectionClient, boolean replace) {
		if (replace) {
			LayersBO.setNewLayers(geoDocument, selectedMap);
		}
		for (int i = 0; i < geoFeatures.length; i++) {
			GeoFeature geoFeature = geoFeatures[i];

			Layer layer = LayerBO.getLayerByName(geoDocument, geoFeature
					.getName());
			if (layer == null) {
				// if no column exists than create it
				layer = LayerBO.setNewLayer(geoDocument, geoFeature.getName(),
						selectedMap);
			}
			final Layer selectedLayer = layer;
			TableItem item = new TableItem(mapTable, SWT.CENTER);
			item.setChecked(selectedLayer.isChoosenForTemplate());

			item.setText(FEATURE_NAME, geoFeature.getName());

			TableEditor editor = new TableEditor(mapTable);
			Text newDescr = new Text(mapTable, SWT.BORDER);
			newDescr.setBackground(new Color(sectionClient.getDisplay(),
					new RGB(245, 245, 245)));
			newDescr.setText(geoFeature.getDescr() != null ? geoFeature
					.getDescr() : "");
			if (layer != null && selectedLayer.getDescription() != null) {
				newDescr.setText(selectedLayer.getDescription());
			}

			newDescr.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent me) {
					System.out.println("Changed");
					selectedLayer.setDescription(((Text) me.widget).getText());
					setIsDirty(true);
				}
			});

			editor.minimumWidth = newDescr.getBounds().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.grabHorizontal = true;
			editor.minimumHeight = newDescr.getBounds().y;
			editor.verticalAlignment = SWT.CENTER;
			editor.grabVertical = true;
			newDescr.selectAll();
			newDescr.setFocus();
			editor.setEditor(newDescr, item, FEATURE_DESCR);
			mapTableEditors.add(editor);

			final Button selButton = new Button(mapTable, SWT.RADIO);
			selButton.setText("");
			editor = new TableEditor(mapTable);
			editor.minimumWidth = selButton.getBounds().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.grabHorizontal = true;
			editor.minimumHeight = selButton.getBounds().y;
			editor.verticalAlignment = SWT.CENTER;
			editor.grabVertical = true;
			editor.setEditor(selButton, item, FEATURE_DEFAULT_LEVEL);
			editor.layout();

			final boolean[] isSelected = new boolean[1];

			selButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					isSelected[0] = e.widget == selButton;
					selectedLayer.setSelected(String.valueOf(isSelected[0]));

					setIsDirty(true);
				}
			});

			if (selectedLayer.getSelected() != null
					&& selectedLayer.getSelected().equals("true")) {
				selButton.setSelection(true);
			}

			final String[] defaultFillColour = new String[1];
			defaultFillColour[0] = "#FF0000";
			if (selectedLayer.getDefaultFillColour() != null) {
				defaultFillColour[0] = selectedLayer.getDefaultFillColour();
			}
			final Composite colorSection = DesignerUtils
					.createColorPickerFillLayer(mapTable, defaultFillColour[0],
							selectedLayer, this);
			String col = (String) colorSection.getData();
			selectedLayer.setDefaultFillColour(defaultFillColour[0]);

			mapTableEditors.add(editor);

			editor = new TableEditor(mapTable);
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.setEditor(colorSection, item, FEATURE_DEFAULT_COLORS);
			editor.layout();
			mapTableEditors.add(editor);

		}
		mapTable.pack();
		mapTable.redraw();
	}

	private void fillDatasetTable(DataStoreMetadata dataStoreMetadata,
			boolean replace) {
		// if dataset changed than new Metadata
		if (replace) {
			MetadataBO.setNewMetadata(geoDocument, selectedDataset);
		}

		for (int i = 0; i < dataStoreMetadata.getFieldsMetadata().length; i++) {

			DataStoreMetadataField dsmf = dataStoreMetadata.getFieldsMetadata()[i];
			// find out the current column
			Column column = ColumnBO.getColumnByName(geoDocument, dsmf
					.getName());
			if (column == null) {
				// if no column exists than create it
				column = ColumnBO.setNewColumn(geoDocument, dsmf.getName(),
						selectedDataset);
			}
			final Column selectedColumn = column;

			final TableItem item = new TableItem(datasetTable, SWT.NONE);
			item.setChecked(selectedColumn.isChoosenForTemplate());

			item.setText(DATASET_NAME, dsmf.getName());
			item.setText(DATASET_CLASS, dsmf.getClassName());
			// combo per geoid, measures, geocd
			final Combo comboSel = new Combo(datasetTable, SWT.SIMPLE
					| SWT.DROP_DOWN | SWT.READ_ONLY);
			comboSel.add("geoid");
			comboSel.add("measures");
			comboSel.add("geocd");
			for (int k = 0; k < comboSel.getItemCount(); k++) {
				String typeText = comboSel.getItem(k);
				if (selectedColumn.getType() != null
						&& selectedColumn.getType().equals(typeText)) {
					comboSel.select(k);
				}
			}
			if (comboSel.getText() != null
					&& comboSel.getText().equals("measures")) {
				item.setText(2, comboSel.getText());
				if (comboSel.getText().equalsIgnoreCase("measures")) {
					item.setImage(0, measureIcon.createImage());
					
				} else {
					if (item.getImage() != null) {
						item.setImage(0, null);
					}
				}
			}

			comboSel.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					// per valorizzare table item col valore del widget
					// contenuto
					item.setText(2, comboSel.getText());
					if (comboSel.getText().equalsIgnoreCase("measures")) {
						item.setImage(0, measureIcon.createImage());
					} else {
						if (item.getImage() != null) {
							item.setImage(0, null);
						}
					}
					// add type
					selectedColumn.setType(comboSel.getText());
					setIsDirty(true);
				}
			});
			selectedColumn.setType(comboSel.getText());
			comboSel.pack();
			TableEditor editor = new TableEditor(datasetTable);
			editor.minimumWidth = comboSel.getBounds().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.grabHorizontal = true;
			editor.minimumHeight = comboSel.getBounds().y;
			editor.verticalAlignment = SWT.CENTER;
			editor.grabVertical = true;

			editor.setEditor(comboSel, item, DATASET_SELECT);

			// combo per geoid, measures, geocd
			final Combo comboAgg = new Combo(datasetTable, SWT.SIMPLE
					| SWT.DROP_DOWN | SWT.READ_ONLY);
			comboAgg.add("sum");
			comboAgg.add("media");

			for (int k = 0; k < comboAgg.getItemCount(); k++) {
				String aggText = comboAgg.getItem(k);
				if (selectedColumn.getAggFunction() != null
						&& selectedColumn.getAggFunction().equals(aggText)) {
					comboAgg.select(k);
				}
			}

			comboAgg.pack();
			// add aggregate function
			comboAgg.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					selectedColumn.setAggFunction(comboAgg.getText());
					setIsDirty(true);
				}
			});
			datasetTableEditors.add(editor);

			editor = new TableEditor(datasetTable);
			editor.minimumWidth = comboAgg.getBounds().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.grabHorizontal = true;
			editor.minimumHeight = comboAgg.getBounds().y;
			editor.verticalAlignment = SWT.CENTER;
			editor.grabVertical = true;
			editor.setEditor(comboAgg, item, DATASET_AGGREGATION);
			datasetTableEditors.add(editor);

			datasetTable.pack();
			datasetTable.redraw();

		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return isDirty;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		SpagoBILogger.infoLog("Start Saving GEO Template File");
		ByteArrayInputStream bais = null;

		try {
			FileEditorInput fei = (FileEditorInput) getEditorInput();
			IFile file = fei.getFile();
			
			ModelBO modelBO = new ModelBO();
			
			GEODocument geoDocumentToSaveOnFile = (GEODocument)DeepCopy.copy(geoDocument);
			modelBO.cleanGEODocument(geoDocumentToSaveOnFile);
			String newContent = XmlTemplateGenerator
					.transformToXml(geoDocumentToSaveOnFile);
			System.out.println("******** SAVING ***************");
			System.out.println(newContent);
			byte[] bytes = newContent.getBytes();
			bais = new ByteArrayInputStream(bytes);
			file.setContents(bais, IFile.FORCE, null);

		} catch (CoreException e) {
			SpagoBILogger.errorLog("Error while Saving GEO Template File", e);
			e.printStackTrace();
		} finally {
			if (bais != null)
				try {
					bais.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		setIsDirty(false);
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSelectedDataset() {
		return selectedDataset;
	}

	public void setSelectedDataset(String selectedDataset) {
		this.selectedDataset = selectedDataset;
	}

	public HashMap<String, DataStoreMetadata> getTempDsMetadataInfos() {
		return tempDsMetadataInfos;
	}

	public void setTempDsMetadataInfos(
			HashMap<String, DataStoreMetadata> tempDsMetadataInfos) {
		this.tempDsMetadataInfos = tempDsMetadataInfos;
	}

	public HashMap<String, GeoFeature[]> getTempMapMetadataInfos() {
		return tempMapMetadataInfos;
	}

	public void setTempMapMetadataInfos(
			HashMap<String, GeoFeature[]> tempMapMetadataInfos) {
		this.tempMapMetadataInfos = tempMapMetadataInfos;
	}

	public HashMap<String, Dataset> getDatasetInfos() {
		return datasetInfos;
	}

	public void setDatasetInfos(HashMap<String, Dataset> datasetInfos) {
		this.datasetInfos = datasetInfos;
	}

	public HashMap<String, GeoMap> getMapInfos() {
		return mapInfos;
	}

	public void setMapInfos(HashMap<String, GeoMap> mapInfos) {
		this.mapInfos = mapInfos;
	}

	public String getSelectedMap() {
		return selectedMap;
	}

	public void setSelectedMap(String selectedMap) {
		this.selectedMap = selectedMap;
	}

}
