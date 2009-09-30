package it.eng.spagobi.studio.chart.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.eng.spagobi.studio.chart.editors.model.chart.ChartModel;
import it.eng.spagobi.studio.chart.editors.model.chart.LinkableChartModel;
import it.eng.spagobi.studio.chart.editors.model.chart.Parameter;
import it.eng.spagobi.studio.chart.utils.DrillConfiguration;
import it.eng.spagobi.studio.chart.utils.DrillParameters;
import it.eng.spagobi.studio.core.log.SpagoBILogger;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class DrillConfigurationEditor {

	Section sectionDrill = null;
	Composite sectionClientDrill = null;

	// texts of url and common pars
	final Group group;
	final Group groupView;

	final Text serValueText;
	final Text catValueText;
	final Text urlValueText;

	final Text newParName;
	final Text newParVal;
	final Combo newComboType;
	final List parsList;
	final Label parameterDetail;

	/**
	 * Constructor of the drillConfiguration Editor
	 * 
	 * @param toolkit
	 * @param form
	 */

	public DrillConfigurationEditor(FormToolkit toolkit, final ScrolledForm form) {
		SpagoBILogger.infoLog("Constructor of drill configuration editor");
		sectionDrill = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE);
		setSectionClientDrill(toolkit.createComposite(sectionDrill));
		// URL PAR
		Label urlLabel = new Label(sectionClientDrill, SWT.NULL);
		urlLabel.setText("			Document Label:");
		urlLabel.pack();
		urlValueText = new Text(sectionClientDrill, SWT.BORDER);
		urlValueText.setToolTipText("The label of the document to drill in");

		// CAT PAR
		Label catLabel = new Label(sectionClientDrill, SWT.NULL);
		catLabel.setText("			Category Url Name:");
		catLabel.pack();
		catValueText = new Text(sectionClientDrill, SWT.BORDER);
		catValueText.setToolTipText("the name with wich the category you choose will be passed to the drill document");
		// SER PAR
		Label serLabel = new Label(sectionClientDrill, SWT.NULL);
		serLabel.setText("			Serie Url Name:");
		serLabel.pack();
		serValueText = new Text(sectionClientDrill, SWT.BORDER);
		serValueText.setToolTipText("the name with wich the serie you choose will be passed to the drill document");

		group = new Group(sectionClientDrill, SWT.NULL);
		groupView = new Group(sectionClientDrill, SWT.NULL);

		Label newNameLabel = new Label(group, SWT.NULL);
		newNameLabel.setText("Parameter Name: ");
		// newNameLabel.setLocation(5,5);
		newNameLabel.pack();
		newParName = new Text(group, SWT.BORDER);
		newParName.setToolTipText("name of a parameter to pass");		

		Label newValLabel = new Label(group, SWT.NULL);
		newValLabel.setText("Parameter Value: ");
		newValLabel.pack();
		newParVal = new Text(group, SWT.BORDER);
		newParVal.setToolTipText("value of a parameter to pass");		

		Label newTypeLabel = new Label(group, SWT.NULL);
		newTypeLabel.setText("Parameter Type: ");
		newTypeLabel.pack();

		newComboType = new Combo(group, SWT.NULL);
		newComboType.setToolTipText("Type of the parameter to pass: ABSOLUTE means that take the specified value, RELATIVE means that search in request for the value");				

		parsList = new List(groupView, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL );

		GridData g=new GridData(GridData.FILL_BOTH);
		g.horizontalSpan=2;
		parsList.setLayoutData(g);

		parameterDetail=new Label(groupView, SWT.NULL);;

	}

	/**
	 * Draw the section for drill parameters
	 * 
	 * @param editor
	 * @param toolkit
	 * @param form
	 */

	// public void createDrillInformationsForm(final ChartModel model, final
	// ChartEditor editor, FormToolkit toolkit, final ScrolledForm form){
	public void createDrillInformationsForm(final LinkableChartModel model,
			final ChartEditor editor, FormToolkit toolkit,
			final ScrolledForm form) {
		SpagoBILogger.infoLog("Create the drill informations form");
		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		sectionDrill.setLayoutData(td);
		sectionDrill.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		sectionDrill.setText("Drill parameters");
		sectionDrill.setDescription("Set all the drill parameteres");

		Composite sectionClientDrill = getSectionClientDrill();
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		sectionClientDrill.setLayout(gl);

		if (model.getDrillConfiguration().getUrl() != null) {
			urlValueText.setText(model.getDrillConfiguration().getUrl());
		}
		urlValueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlValueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				if (editor != null)
					editor.setIsDirty(true);
				String parameterValueStr = urlValueText.getText();
				// model.getDrillConfiguration().setUrl(parameterValueStr);
				model.getDrillConfiguration().setUrl(parameterValueStr);
			}
		});
		urlValueText.pack();

		if (model.getDrillConfiguration().getCategoryUrlName() != null) {
			catValueText.setText(model.getDrillConfiguration()
					.getCategoryUrlName());
		}
		catValueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		catValueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				if (editor != null)
					editor.setIsDirty(true);
				String parameterValueStr = catValueText.getText();
				// model.getDrillConfiguration().setCategoryUrlName(parameterValueStr);
				model.getDrillConfiguration().setCategoryUrlName(
						parameterValueStr);
			}
		});
		catValueText.pack();

		if (model.getDrillConfiguration().getSeriesUrlName() != null) {
			serValueText.setText(model.getDrillConfiguration()
					.getSeriesUrlName());
		}
		serValueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serValueText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				if (editor != null)
					editor.setIsDirty(true);
				String parameterValueStr = serValueText.getText();
				model.getDrillConfiguration().setSeriesUrlName(
						parameterValueStr);
			}
		});
		serValueText.pack();
		createDrillParametersForm(model, editor, toolkit, form,
				sectionClientDrill);
		sectionDrill.setClient(sectionClientDrill);

	}

	public Composite getSectionClientDrill() {
		return sectionClientDrill;
	}

	public void setSectionClientDrill(Composite sectionClientDrill) {
		this.sectionClientDrill = sectionClientDrill;
	}

	public Section getSectionDrill() {
		return sectionDrill;
	}

	public void setSectionDrill(Section sectionDrill) {
		this.sectionDrill = sectionDrill;
	}

	public void createDrillParametersForm(final LinkableChartModel model,
			final ChartEditor editor, FormToolkit toolkit,
			final ScrolledForm form, Composite sectionClientDrill) {

		// First group with new parameter texts
		group.setText("---------- ADD PARAMETER ----------");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group.setLayout(gridLayout);

		//		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_END);
		//		gridData.horizontalSpan = 2;
		//		gridData.horizontalAlignment = GridData.FILL;
		//		group.setLayoutData(gridData);

		newParName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// newParName.setLocation(5,50);
		newParName.pack();

		newParVal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		newParVal.pack();

		newComboType.add("RELATIVE");
		newComboType.add("ABSOLUTE");
		newComboType.select(0);
		newComboType.pack();

		Image image = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_OBJ_ELEMENT);
		Button buttonAdd = new Button(group, SWT.PUSH);
		buttonAdd.setImage(image);
		buttonAdd.setToolTipText("Add the parameter");		
		buttonAdd.pack();
		group.pack();
		// buttonAdd.setText("Add");

		// Group to view parameters
		groupView.setText("---------- DRILL PARAMETERS LIST ----------");

		GridLayout gridLayoutView = new GridLayout();
		gridLayoutView.numColumns = 2;
		groupView.setLayout(gridLayoutView);
		GridData gridDataView = new GridData(GridData.FILL_BOTH);
		groupView.setLayoutData(gridDataView);


		// Fill parameters

		if (model.getDrillConfiguration().getDrillParameters() != null) {
			for (Iterator iterator = model.getDrillConfiguration()
					.getDrillParameters().keySet().iterator(); iterator
					.hasNext();) {
				String parName = (String) iterator.next();
				// DrillParameters par=parsMap.get(parName);
				parsList.add(parName);
			}

		}
		parsList.setToolTipText("parameters to pass");
		parsList.redraw();
		// close if map is not null


		// add the labels for detail
		parameterDetail.setText("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
		parameterDetail.setVisible(false);

		Image imageRem = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_TOOL_DELETE);
		Button buttonCancel = new Button(groupView, SWT.PUSH);
		buttonCancel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		buttonCancel.setToolTipText("Erase Parameter");
		buttonCancel.setImage(imageRem);
		//buttonCancel.pack();


		// Add listener that show details of parameter selected
		parsList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int selection = parsList.getSelectionIndex();
				// get ParSelected
				String parNameSelected = parsList.getItem(selection);
				DrillParameters drillPar = model.getDrillConfiguration().getDrillParameters().get(parNameSelected);
				String detail="Parameter "+drillPar.getName()+" ";
				detail+="is of type "+drillPar.getType()+" ";
				if(drillPar.getValue()!=null && !drillPar.getValue().equalsIgnoreCase("")){
					detail+="with value "+drillPar.getValue();
				}
				else{
					detail+="has no value specified";					
				}
				parameterDetail.setText(detail);
				parameterDetail.redraw();
				parameterDetail.setVisible(true);

				//				parNameLabel.setVisible(true);
				//				parNameVal.setText(drillPar.getName() != null ? drillPar
				//						.getName() : "");
				//				parNameVal.setVisible(true);
				//				parNameVal.pack();
				//				parValueLabel.setVisible(true);
				//				parValueVal.setText(drillPar.getValue() != null ? drillPar
				//						.getValue() : "");
				//				parValueVal.setVisible(true);
				//				parValueVal.pack();
				//				parTypeLabel.setVisible(true);
				//				parTypeVal.setText(Integer.valueOf(drillPar.getType())
				//						.toString());
				//				parTypeVal.setVisible(true);
				//				parTypeVal.pack();

				// groupView.pack();

			}
		});

		// Add Button Listener
		Listener addListener = new Listener() {
			public void handleEvent(Event event) {
				String nameToAdd = newParName.getText();
				String valueToAdd = newParVal.getText();
				Map<String, DrillParameters> mapDrillPars = model.getDrillConfiguration().getDrillParameters();
				//if not already present
				if(nameToAdd==null || nameToAdd.equalsIgnoreCase("")){
					SpagoBILogger.warningLog("Error in inserting parameter, no name specified");
					MessageDialog.openWarning(groupView.getShell(), "Warning", "Specify a parameter name");
				}
				else if(mapDrillPars.keySet().contains(nameToAdd)){
					SpagoBILogger.warningLog("Error in inserting parameter, already present or with no name");
					MessageDialog.openWarning(groupView.getShell(), "Warning", "Parameter name already present");
				}
				else {
					String typeToAdd = newComboType.getItem(newComboType
							.getSelectionIndex());
					parsList.add(nameToAdd);

					DrillParameters par = new DrillParameters(nameToAdd,
							valueToAdd, typeToAdd);
					mapDrillPars.put(nameToAdd, par);
					// erase insert fields
					newParName.setText("");
					newParVal.setText("");
				}

			}
		};
		buttonAdd.addListener(SWT.Selection, addListener);

		// Add Button Listener
		Listener cancelListener = new Listener() {
			public void handleEvent(Event event) {

				int index = parsList.getSelectionIndex();
				String namePar = parsList.getItem(index);
				// remove from java list
				if (model.getDrillConfiguration().getDrillParameters()
						.containsKey(namePar)) {
					model.getDrillConfiguration().getDrillParameters().remove(
							namePar);
				}
				// remove from SWT list
				parsList.remove(namePar);
				parameterDetail.setText("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
				parameterDetail.setVisible(false);
				// parsList.pack();
			}
		};
		buttonCancel.addListener(SWT.Selection, cancelListener);

		// groupView.pack();

	}

	public void setVisible(boolean visible) {
		sectionDrill.setVisible(visible);

	}

	public boolean isVisible() {
		if (sectionDrill.isVisible())
			return true;
		else
			return false;
	}

	public Text getSerValueText() {
		return serValueText;
	}

	public Text getCatValueText() {
		return catValueText;
	}

	public Text getUrlValueText() {
		return urlValueText;
	}

	public void eraseComposite() {
		serValueText.setText("");
		urlValueText.setText("");

		catValueText.setText("");
		newParName.setText("");
		newParVal.setText("");
		newComboType.select(0);
		parsList.removeAll();
	}

	public void refillFieldsDrillConfiguration(
			final DrillConfiguration drillConfiguration,
			final ChartEditor editor, FormToolkit toolkit,
			final ScrolledForm form) {
		if (drillConfiguration != null) {
			DrillConfiguration drill = drillConfiguration;
			if (drill != null) {
				urlValueText.setText(drill.getUrl() != null ? drill.getUrl()
						: "");
				catValueText.setText(drill.getCategoryUrlName() != null ? drill
						.getCategoryUrlName() : "");
				serValueText.setText(drill.getSeriesUrlName() != null ? drill
						.getSeriesUrlName() : "");
				if (drill.getDrillParameters() != null) {
					for (Iterator iterator = drill.getDrillParameters()
							.keySet().iterator(); iterator.hasNext();) {
						String parName = (String) iterator.next();
						parsList.add(parName);
					}
				}
				parsList.redraw();

			}
		}
	}

}
