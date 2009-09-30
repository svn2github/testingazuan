package it.eng.spagobi.studio.chart.editors;

import it.eng.spagobi.studio.chart.editors.model.chart.ChartModel;
import it.eng.spagobi.studio.chart.editors.model.chart.DialChartModel;
import it.eng.spagobi.studio.chart.editors.model.chart.LinkableChartModel;
import it.eng.spagobi.studio.chart.editors.model.chart.XYChartModel;
import it.eng.spagobi.studio.chart.utils.Style;
import it.eng.spagobi.studio.core.log.SpagoBILogger;

import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.dom4j.Node;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ChartEditorComponents {


	// Sections
	Composite sectionClientInformation=null;
	Composite sectionClientDimension=null;
	Composite sectionClientStyle=null;

	// editors components
	ConfigurationEditor configurationEditor=null;
	DrillConfigurationEditor drillConfigurationEditor=null;
	SeriesPersonalizationEditor seriesPersonalizationEditor=null;
	IntervalsInformationEditor intervalsInformationEditor=null;
	YZRangesEditor yzRangesEditor=null;


	//HashMap<String, Group> subTypeGroup=new HashMap<String, Group>();



	public ChartEditorComponents() {
		super();
	}




	//	public boolean currentGroupSelection(String subType, Composite section){
	//
	//		boolean groupAlreadyPresent=false;
	//		Group group=null;
	//		if(!subTypeGroup.containsKey(model.getSubType())){
	//			group=new Group(section, SWT.NULL);
	//			group.setText(model.getSubType());
	//			group.setToolTipText(model.getSubType());
	//			group.setVisible(false);
	//			group.pack();
	//			subTypeGroup.put(model.getSubType(), group);
	//
	//		}
	//		else if(subTypeGroup.get(model.getSubType())!=null){
	//			group=new Group(section, SWT.NULL);
	//			group.setVisible(false);
	//			group.pack();
	//			subTypeGroup.put(model.getSubType(), group);
	//		}
	//		else{
	//			group=subTypeGroup.get(model.getSubType());		
	//			groupAlreadyPresent=true;
	//		}
	//
	//		// disable all others
	//		for (Iterator iterator = subTypeGroup.keySet().iterator(); iterator.hasNext();) {
	//			String subTypeC = (String) iterator.next();
	//			Group g=subTypeGroup.get(subTypeC);
	//			if(!subTypeC.equalsIgnoreCase(subType)){
	//				g.setVisible(false);
	//				//				Control[] children=g.getChildren();
	//				//				for (int i = 0; i < children.length; i++) {
	//				//					children[i].setVisible(false);
	//				//					children[i].dispose();					
	//				//				}
	//				g.dispose();
	//
	//
	//			}
	//			else{
	//				g.setVisible(true);
	//				g.redraw();
	//			}
	//		}
	//
	//
	//
	//		return groupAlreadyPresent;
	//	}











	/** Create Style parameters form
	 * 
	 */


	public void createStyleParametersForm(final ChartModel model, final ChartEditor editor, final Composite section, FormToolkit toolkit){
		SpagoBILogger.infoLog("Start Style parameters form creation");
		GridLayout gl = new GridLayout();
		gl.numColumns = 4;
		section.setLayout(gl);

		Set<String> stylesTrattati=model.getStyleParametersEditors().keySet();

		for (Iterator iterator = stylesTrattati.iterator(); iterator.hasNext();) {
			String styleName = (String) iterator.next();
			SpagoBILogger.infoLog("Style parameter "+styleName);			
			final Style style=model.getStyleParametersEditors().get(styleName);
			Label styleLabel = new Label(section, SWT.BORDER_DOT);
			styleLabel.setText(style.getName());
			styleLabel.setForeground(new Color(section.getDisplay(),0,0,255));
			if(style.getTooltip()!=null){
				styleLabel.setToolTipText(style.getTooltip());
			}
			Label spaceLabel1 = new Label(section, SWT.BORDER_DOT);
			spaceLabel1.setText("");
			spaceLabel1 = new Label(section, SWT.BORDER_DOT);
			spaceLabel1.setText("");
			spaceLabel1 = new Label(section, SWT.BORDER_DOT);
			spaceLabel1.setText("");

			// Draw Parameters form

			if(style.isHasFont()){
				Label fontLabel = new Label(section, SWT.NULL);
				fontLabel.setText("			Font:");
				fontLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				//fontLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				final Combo styleFontCombo = new Combo(section,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
				styleFontCombo.add("Helvetica");
				styleFontCombo.add("Times_New_Roman");
				styleFontCombo.add("Arial");
				int index2=styleFontCombo.indexOf(style.getFont()!=null ? style.getFont() : "");
				if(index2!=-1) styleFontCombo.select(index2);
				else	styleFontCombo.select(0);
				//styleFontCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				styleFontCombo.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						String newFont = styleFontCombo.getText();
						style.setFont(newFont);
						if(editor!=null)editor.setIsDirty(true);
					}
				});
			}
			else{
				Label sl=new Label(section,SWT.NULL);
				sl.setText("");
				sl=new Label(section,SWT.NULL);
				sl.setText("");
			}

			if(style.isHasSize()){
				Label sizeLabel = new Label(section, SWT.NULL);
				sizeLabel.setText("		Size:");
				sizeLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

				final Spinner styleSizeText = new Spinner (section, SWT.BORDER);
				styleSizeText.setMaximum(100000);
				styleSizeText.setMinimum(0);
				styleSizeText.setSelection(style.getSize()!=null?style.getSize() : 10);
				//styleSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				styleSizeText.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						if(editor!=null) editor.setIsDirty(true);
						int newSize = styleSizeText.getSelection();
						Integer newSizeInt=null;
						try{
							newSizeInt=Integer.valueOf(newSize);
						}
						catch (Exception e) {
							newSizeInt=new Integer(10);
						}
						style.setSize(newSizeInt);
					}


				});
			}
			else{
				Label sl=new Label(section,SWT.NULL);
				sl.setText("");
				sl=new Label(section,SWT.NULL);
				sl.setText("");
			}

			if(style.isHasOrientation()){
				Label orientationLabel = new Label(section, SWT.NULL);
				orientationLabel.setText("			Orientation:");
				orientationLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

				final Combo combo = new Combo(section,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
				boolean selected=false;
				combo.add(Style.HORIZONTAL);
				combo.add(Style.VERTICAL);

				int index=combo.indexOf(style.getOrientation()!=null ? style.getOrientation().toUpperCase() : "");
				if(index!=-1) combo.select(index);
				else index=0;
				//combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				combo.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						String newOrientation = combo.getText();
						style.setOrientation(newOrientation);
						if(editor!=null)editor.setIsDirty(true);
					}
				});
			}
			else{
				Label sl=new Label(section,SWT.NULL);
				sl.setText("");
				sl=new Label(section,SWT.NULL);
				sl.setText("");
			}

			if(style.isHasColor()){
				Label colorLabel1 = new Label(section, SWT.NULL);
				colorLabel1.setText("		Color:");
				colorLabel1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				Composite innerSection = toolkit.createComposite(section);
				GridLayout colorGd = new GridLayout();
				colorGd.numColumns = 2;
				colorGd.marginHeight = 0;
				colorGd.marginBottom = 0;
				innerSection.setLayout(colorGd);
				final Label colorLabel = new Label(innerSection, SWT.BORDER);
				colorLabel.setText("          ");
				String hexadecimal = style.getColor()!=null ? ChartEditor.convertRGBToHexadecimal(style.getColor()) : "#FFFFFF";
				RGB rgb =null;
				try{
					rgb= ChartEditor.convertHexadecimalToRGB(hexadecimal);
				}
				catch (Exception e) {
					rgb=new RGB(255,0,0);
				}
				final Color color = new org.eclipse.swt.graphics.Color(section.getDisplay(), rgb);
				colorLabel.setBackground(color);
				Button button = new Button(innerSection, SWT.PUSH);
				button.setText("Color...");
				final Shell parentShell = section.getShell();
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						final Shell centerShell = new Shell(parentShell, SWT.NO_TRIM);
						centerShell.setLocation(
								(parentShell.getSize().x - ChartEditor.COLORDIALOG_WIDTH) / 2,
								(parentShell.getSize().y - ChartEditor.COLORDIALOG_HEIGHT) / 2);
						ColorDialog colorDg = new ColorDialog(centerShell,
								SWT.APPLICATION_MODAL);
						colorDg.setRGB(colorLabel.getBackground().getRGB());
						//colorDg.setText("Choose a color");
						RGB rgb = colorDg.open();
						if (rgb != null) {
							// Dispose the old color, create the
							// new one, and set into the label
							color.dispose();
							Color newColor = new Color(parentShell.getDisplay(), rgb);
							colorLabel.setBackground(newColor);
							if(editor!=null) editor.setIsDirty(true);
							String newHexadecimal = ChartEditor.convertRGBToHexadecimal(rgb);
							style.setColor(ChartEditor.convertHexadecimalToRGB(newHexadecimal));
						}
						if(editor!=null)editor.setIsDirty(true);
						centerShell.dispose();
					}
				});
			}
			else{
				Label sl=new Label(section,SWT.NULL);
				sl.setText("");
				sl=new Label(section,SWT.NULL);
				sl.setText("");
			}

			// If enabled set Legend Position
//			if(model.isLegendPositionStyle()){
//				Label label=new Label(section, SWT.NULL);
//				label.setText("Legend Position");
//				final Combo comboLP = new Combo(section,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
//				Node node=model.getConfigDocument().selectSingleNode("//"+model.getType().toUpperCase()+"S/"+model.getType().toUpperCase()+"/PARAMETER[@name='LEGEND_POSITION']");
//				String values=node.valueOf("@values");
//				StringTokenizer st=new StringTokenizer(values,",");
//				while(st.hasMoreTokens()){
//					String val=st.nextToken();
//					comboLP.add(val);
//				}
//				String actualLP=model.getLegendPositionValue();
//				if(actualLP==null)actualLP="";
//				int index1=combo.indexOf(actualLP);
//				if(index1!=-1) combo.select(index1);
//				else combo.select(0);
//				combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				combo.addModifyListener(new ModifyListener() {
//					public void modifyText(ModifyEvent event) {
//						String newOrientation = combo.getText();
//						style.setOrientation(newOrientation);
//						if(editor!=null)editor.setIsDirty(true);
//					}
//				});


			}

		}
	





	public Composite getSectionClientInformation() {
		return sectionClientInformation;
	}

	public void setSectionClientInformation(Composite sectionClientInformation) {
		this.sectionClientInformation = sectionClientInformation;
	}

	public Composite getSectionClientDimension() {
		return sectionClientDimension;
	}

	public void setSectionClientDimension(Composite sectionClientDimension) {
		this.sectionClientDimension = sectionClientDimension;
	}


	public Composite getSectionClientStyle() {
		return sectionClientStyle;
	}

	public void setSectionClientStyle(Composite sectionClientStyle) {
		this.sectionClientStyle = sectionClientStyle;
	}


	/** Calls the creation of configuration form
	 * 
	 * @param model
	 * @param editor
	 * @param formToolkit
	 * @param scrolledForm
	 */

	public void createConfigurationSection(final ChartModel model, ChartEditor editor, FormToolkit formToolkit, final ScrolledForm scrolledForm){
		configurationEditor.createConfigurationParametersForm(model,editor,formToolkit, scrolledForm);
	}




	/** Calls the creation of specific configuration form
	 * 
	 * @param model
	 * @param editor
	 * @param formToolkit
	 * @param scrolledForm
	 */

	public void createSpecificConfigurationSection(final ChartModel model, ChartEditor editor, FormToolkit formToolkit, final ScrolledForm scrolledForm){
		configurationEditor.createSpecificConfigurationParametersForm(model,editor,formToolkit);
	}


	/** Calls the creation of drill configuration form
	 * 
	 * @param model
	 * @param editor
	 * @param formToolkit
	 * @param scrolledForm
	 */

	public void createDrillConfigurationSection(final LinkableChartModel model, ChartEditor editor, FormToolkit formToolkit, final ScrolledForm scrolledForm){
		drillConfigurationEditor=new DrillConfigurationEditor(formToolkit, scrolledForm);
		drillConfigurationEditor.createDrillInformationsForm(model,editor,formToolkit, scrolledForm);
	}


	/** Calls the creation of seres personalization form
	 * 
	 * @param model
	 * @param editor
	 * @param formToolkit
	 * @param scrolledForm
	 */

	public void createSeriesPersonalizationSection(final ChartModel model, ChartEditor editor, FormToolkit formToolkit, final ScrolledForm scrolledForm){
		seriesPersonalizationEditor=new SeriesPersonalizationEditor(formToolkit, scrolledForm);
		seriesPersonalizationEditor.createSeriesPersonalizationForm(model,editor,formToolkit, scrolledForm);
	}


	/** Calls the creation of intervals infroamtion form
	 * 
	 * @param model
	 * @param editor
	 * @param formToolkit
	 * @param scrolledForm
	 */

	public void createIntervalsInformationsSection(final ChartModel model, ChartEditor editor, FormToolkit formToolkit, final ScrolledForm scrolledForm){
		intervalsInformationEditor=new IntervalsInformationEditor((DialChartModel) model, formToolkit, scrolledForm,editor);
	}

	public void createYZRangesSection(final ChartModel model, ChartEditor editor, FormToolkit formToolkit, final ScrolledForm scrolledForm){
		yzRangesEditor=new YZRangesEditor((XYChartModel) model, formToolkit, scrolledForm,editor);
	}


	public DrillConfigurationEditor getDrillConfigurationEditor() {
		return drillConfigurationEditor;
	}

	public void setDrillConfigurationEditor(
			DrillConfigurationEditor drillConfigurationEditor) {
		this.drillConfigurationEditor = drillConfigurationEditor;
	}

	public ConfigurationEditor getConfigurationEditor() {
		return configurationEditor;
	}

	public void setConfigurationEditor(ConfigurationEditor configurationEditor) {
		this.configurationEditor = configurationEditor;
	}




	public SeriesPersonalizationEditor getSeriesPersonalizationEditor() {
		return seriesPersonalizationEditor;
	}




	public void setSeriesPersonalizationEditor(
			SeriesPersonalizationEditor seriesPersonalizationEditor) {
		this.seriesPersonalizationEditor = seriesPersonalizationEditor;
	}




	public IntervalsInformationEditor getIntervalsInformationEditor() {
		return intervalsInformationEditor;
	}




	public void setIntervalsInformationEditor(
			IntervalsInformationEditor intervalsInformationEditor) {
		this.intervalsInformationEditor = intervalsInformationEditor;
	}




	public YZRangesEditor getYzRangesEditor() {
		return yzRangesEditor;
	}




	public void setYzRangesEditor(YZRangesEditor yzRangesEditor) {
		this.yzRangesEditor = yzRangesEditor;
	}



}
