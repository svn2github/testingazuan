package it.eng.spagobi.studio.chart.editors;

import it.eng.spagobi.studio.chart.editors.model.chart.ChartModel;
import it.eng.spagobi.studio.chart.utils.DrillParameters;
import it.eng.spagobi.studio.chart.utils.SeriePersonalization;
import it.eng.spagobi.studio.core.log.SpagoBILogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
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

public class SeriesPersonalizationEditor {

	Section sectionSeries=null;
	Composite sectionClientSeries=null;

	final Group newSeriesGroup;
	final Group personalGroup;

	final Label newSerLabel;
	final Text newSerLabelText;
	final Label newColorLabel;	
	Composite innerSection; 
	final Label colorLabel;
	final Button colorButton;	
	final Label drawLabel;
	final Combo comboDraw;
	final Label scaleLabel;
	final Combo comboScale;
	final List parsList; 
	final Text newSerName; 
	// Field for personalization

	public SeriesPersonalizationEditor(FormToolkit toolkit, final ScrolledForm form) {

		sectionSeries = toolkit.createSection(form.getBody(), 
				Section.DESCRIPTION|Section.TITLE_BAR|Section.TWISTIE);
		sectionClientSeries=toolkit.createComposite(sectionSeries);

		newSeriesGroup=new Group(sectionClientSeries, SWT.NULL);
		newSeriesGroup.setText("---------- ADD SERIE ----------");
		GridLayout gridLayout=new GridLayout();
		gridLayout.numColumns=2;
		newSeriesGroup.setLayout(gridLayout);

		Label newNameLabel = new Label(newSeriesGroup, SWT.NULL); 
		newNameLabel.setText("Serie Name: ");
		newNameLabel.setToolTipText("New serie's name");
		newNameLabel.pack();		
		newSerName = new Text(newSeriesGroup, SWT.BORDER);
		newSerName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		newSerName.pack();


		// group where appear the list and the list for serie personalization
		personalGroup=new Group(sectionClientSeries, SWT.NULL);
		personalGroup.setText("-------------- SERIE PERSONALIZATION --------------");
		GridLayout gridLayoutView = new GridLayout();
		gridLayoutView.numColumns = 3;
		personalGroup.setLayout(gridLayoutView);
		GridData gridDataView = new GridData(GridData.FILL_BOTH);
		personalGroup.setLayoutData(gridDataView);

		parsList = new List (personalGroup, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		parsList.setToolTipText("Inserted series");

		newSerLabel=new Label(personalGroup, SWT.NULL); ;

		newSerLabelText=new Text(personalGroup, SWT.BORDER);;
		newSerLabelText.setToolTipText("Serie Label");

		newColorLabel=new Label(personalGroup, SWT.NULL);

		innerSection = toolkit.createComposite(personalGroup);
		colorLabel = new Label(innerSection, SWT.BORDER);
		colorButton = new Button(innerSection, SWT.PUSH);
		colorButton.setToolTipText("Color of the serie");

		drawLabel=new Label(personalGroup, SWT.NULL);
		//		Label sl1=new Label(personalGroup, SWT.NULL);
		//		sl1.setText("");
		//		Label sl2=new Label(personalGroup, SWT.NULL);
		//		sl2.setText("");
		comboDraw=new Combo(personalGroup,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		newColorLabel.setToolTipText("Set the drawing shape; can be bar, line or line without shape on the category point");
		scaleLabel=new Label(personalGroup, SWT.NULL);
		comboScale=new Combo(personalGroup,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		comboScale.setToolTipText("Map the serie to the first or to the second scale");

	}









	/** Draw the section for drill parameters
	 * 
	 * @param editor
	 * @param toolkit
	 * @param form
	 */


	public void createSeriesPersonalizationForm(final ChartModel model, final ChartEditor editor, FormToolkit toolkit, final ScrolledForm form){

		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		sectionSeries.setLayoutData(td);
		sectionSeries.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		sectionSeries.setText("Series Labels parameters");
		sectionSeries.setDescription("Set all the drill parameteres");

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		sectionClientSeries.setLayout(gl);



		newSerName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		newSerName.pack();

		//Image image = sectionClientSeries.getDisplay().getSystemImage(SWT.ICON_QUESTION);
		Image imageAdd = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_OBJ_ELEMENT);
		Button buttonAdd = new Button(newSeriesGroup, SWT.PUSH);
		buttonAdd.setImage(imageAdd);
		buttonAdd.setToolTipText("Add serie");
		buttonAdd.pack();

		//		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		//		gridData.verticalSpan = 4;
		//		gridData.verticalAlignment=SWT.CENTER;
		//		parsList.setSize(500, 1000);
		//		parsList.setLayoutData(gridData);

		GridData g=new GridData(GridData.FILL_BOTH);
		g.verticalSpan=4;
		parsList.setLayoutData(g);

		if(model.getSeriesPersonalizationHashMap()!=null){
			for (Iterator iterator = model.getSeriesPersonalizationHashMap().keySet().iterator(); iterator.hasNext();) {
				String parName = (String) iterator.next();
				parsList.add(parName);
			}			
		} //close if map is not null
		parsList.redraw();

		// Add Button Listener
		Listener addListener = new Listener() {
			public void handleEvent(Event event) {
				String nameToAdd=newSerName.getText();
				//parsMap=model.getSeriesPersonalizationHashMap();
				if(nameToAdd==null || nameToAdd.equalsIgnoreCase("")){
					SpagoBILogger.warningLog("Specify a name for serie");
					MessageDialog.openWarning(newSeriesGroup.getShell(), "Warning", "Specify a name for serie");
				}
				else if(model.getSeriesPersonalizationHashMap().keySet().contains(nameToAdd)){
					SpagoBILogger.warningLog("Name already present for Serie");
					MessageDialog.openWarning(newSeriesGroup.getShell(), "Warning", "Name already present");					
				}
				else {					
					parsList.add(nameToAdd);
					model.getSeriesPersonalizationHashMap().put(nameToAdd, new SeriePersonalization(nameToAdd));
				}

			}
		};
		buttonAdd.addListener(SWT.Selection, addListener);
		buttonAdd.pack();

		// Under the list there are two field, one to put label one for color, a button to modify and one to remove

		newSerLabel.setText("    Label Serie: ");		
		newSerLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		newSerLabel.pack();
		newSerLabelText.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		newSerLabelText.pack();
		newSerLabelText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				String newLabel = newSerLabelText.getText();
				int selection = parsList.getSelectionIndex();
				//final Map<String, SeriePersonalization> parsMap=model.getSeriesPersonalizationHashMap();				

				String parNameSelected=null;
				if(selection!=-1){
					parNameSelected=parsList.getItem(selection);
					SeriePersonalization seriePers=model.getSeriesPersonalizationHashMap().get(parNameSelected);
					if(seriePers!=null){seriePers.setLabel(newLabel);
					}
				}
			}
		});
		newSerLabel.setEnabled(false);
		newSerLabelText.setEnabled(false);

		newColorLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		newColorLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		newColorLabel.setText("    Color serie: ");
		newColorLabel.setEnabled(false);
		newColorLabel.pack();

		final Color color = new org.eclipse.swt.graphics.Color(personalGroup.getDisplay(), new RGB(255,255,255));
		GridLayout colorGd = new GridLayout();
		colorGd.numColumns = 2;
		colorGd.marginHeight = 0;
		colorGd.marginBottom = 0;
		innerSection.setLayout(colorGd);
		colorLabel.setText("          ");
		colorLabel.setBackground(color);
		colorButton.setText("Color...");
		final Shell parentShell = personalGroup.getShell();
		colorButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				final Shell centerShell = new Shell(parentShell, SWT.NO_TRIM);
				centerShell.setLocation(
						(parentShell.getSize().x - ChartEditor.COLORDIALOG_WIDTH) / 2,
						(parentShell.getSize().y - ChartEditor.COLORDIALOG_HEIGHT) / 2);
				ColorDialog colorDg = new ColorDialog(centerShell,
						SWT.APPLICATION_MODAL);
				colorDg.setRGB(colorLabel.getBackground().getRGB());
				colorDg.setText("Choose a color");
				RGB rgb = colorDg.open();
				//final Map<String, SeriePersonalization> parsMap=model.getSeriesPersonalizationHashMap();					
				if (rgb != null) {
					// Dispose the old color, create the
					// new one, and set into the label
					color.dispose();
					Color newColor = new Color(parentShell.getDisplay(), rgb);
					colorLabel.setBackground(newColor);
					if(editor!=null) editor.setIsDirty(true);
					String newHexadecimal = ChartEditor.convertRGBToHexadecimal(rgb);
					int selection = parsList.getSelectionIndex();
					//get ParSelected
					String parNameSelected=parsList.getItem(selection);
					SeriePersonalization seriePers=model.getSeriesPersonalizationHashMap().get(parNameSelected);
					if(seriePers!=null){
						seriePers.setColor(ChartEditor.convertHexadecimalToRGB(newHexadecimal));
					}
					//centerShell.pack();
					centerShell.dispose();
				}
			}
		});			
		colorLabel.setEnabled(false);
		colorButton.setEnabled(false);

		drawLabel.setText("    Draw style: ");		
		drawLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		drawLabel.pack();

		comboDraw.add("bar");
		comboDraw.add("line");
		comboDraw.add("line_no_shape");
		comboDraw.select(0);
		comboDraw.setEnabled(false);
		comboDraw.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		comboDraw.pack();
		comboDraw.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				String comboText = comboDraw.getText();
				int selection = parsList.getSelectionIndex();
				if(selection!=-1){
					//final Map<String, SeriePersonalization> parsMap=model.getSeriesPersonalizationHashMap();				
					String parNameSelected=parsList.getItem(selection);
					SeriePersonalization seriePers=model.getSeriesPersonalizationHashMap().get(parNameSelected);
					if(parNameSelected!=null){
						seriePers.setDraw(comboText);
					}
				}
			}
		});

		comboDraw.pack();

		scaleLabel.setText("  Map to scale: ");		
		scaleLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		scaleLabel.pack();

		comboScale.add("1");
		comboScale.add("2");
		comboScale.select(0);
		comboScale.setEnabled(false);
		comboScale.pack();
		comboScale.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		comboScale.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				String comboText = comboScale.getText();
				int selection = parsList.getSelectionIndex();
				if(selection!=-1){
					String parNameSelected=parsList.getItem(selection);
					SeriePersonalization seriePers=model.getSeriesPersonalizationHashMap().get(parNameSelected);
					if(parNameSelected!=null){
						seriePers.setScale(Integer.valueOf(comboText).intValue());
					}
				}
			}
		});

		comboScale.pack();


		// Add listener that show details of parameter selected
		parsList.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				// get par selected
				int selection = parsList.getSelectionIndex();
				String parNameSelected=parsList.getItem(selection);
				// put the default value

				SeriePersonalization seriePers=model.getSeriesPersonalizationHashMap().get(parNameSelected);
				newSerLabelText.setText(seriePers.getLabel()!=null ? seriePers.getLabel() : "");
				String draw=seriePers.getDraw();
				if(draw!=null && !draw.equalsIgnoreCase("")){
					int index=comboDraw.indexOf(draw);
					comboDraw.select(index);
				}
				else{
					comboDraw.select(0);
				}

				int scale=seriePers.getScale();
				int index=comboScale.indexOf(Integer.valueOf(scale).toString());
				comboScale.select(index);


				if(seriePers.getColor()!=null){
					Color newColor = new Color(parentShell.getDisplay(), seriePers.getColor());
					colorLabel.setBackground(newColor);
				}
				else
				{
					colorLabel.setBackground(null);
				}

				newSerLabel.setEnabled(true);
				newSerLabelText.setEnabled(true);
				colorLabel.setEnabled(true);
				colorButton.setEnabled(true);
				drawLabel.setEnabled(true);
				comboDraw.setEnabled(true);
				scaleLabel.setEnabled(true);
				comboScale.setEnabled(true);
				newColorLabel.setEnabled(true);
			}	
		});



		Button buttonRem = new Button(personalGroup, SWT.PUSH);
		buttonRem.setToolTipText("Remove");
		Image imageRem = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_TOOL_DELETE);
		buttonRem.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		buttonRem.setToolTipText("Remove serie");
		buttonRem.setImage(imageRem);
		buttonRem.pack();



		// Add Button Listener
		Listener cancelListener = new Listener() {
			public void handleEvent(Event event) {
				int index=parsList.getSelectionIndex();
				String namePar=parsList.getItem(index);
				//remove from java list 
				newSerLabelText.setText("");
				colorLabel.setBackground(null);

				if(model.getSeriesPersonalizationHashMap().containsKey(namePar)){
					model.getSeriesPersonalizationHashMap().remove(namePar);
				}
				colorLabel.setEnabled(false);
				colorButton.setEnabled(false);
				newSerLabel.setEnabled(false);
				newSerLabelText.setEnabled(false);
				drawLabel.setEnabled(false);
				comboDraw.setEnabled(false);
				comboScale.setEnabled(false);
				newColorLabel.setEnabled(false);
				// remove from SWT list
				parsList.remove(namePar);
				//				parsList.pack();			
			}
		};
		buttonRem.addListener(SWT.Selection, cancelListener);

		newSeriesGroup.pack();		
		personalGroup.pack();
		sectionSeries.setClient(sectionClientSeries);
	}




	public void setVisible(boolean visible){
		sectionSeries.setVisible(visible);

	}

	public boolean isVisible(){
		if(sectionSeries.isVisible())return true;
		else return false;
	}


	/** Among labels, color, draws personalizations allow only those recorded in config file
	 * 
	 * @param labels
	 * @param colors
	 * @param draws
	 */
	public void enablePersonalizations(boolean labels, boolean colors, boolean draws, boolean scales){
		newSerLabel.setVisible(labels);
		newSerLabelText.setVisible(labels);
		colorLabel.setVisible(colors);
		colorButton.setVisible(colors);
		drawLabel.setVisible(draws);
		comboDraw.setVisible(draws);
		scaleLabel.setVisible(scales);
		comboScale.setVisible(scales);
		newColorLabel.setVisible(colors);
	}

	public void eraseComposite(){
		parsList.removeAll();
		newSerName.setText("");
		newSerLabelText.setText("");
		colorLabel.setBackground(null);
		comboDraw.select(0);
		comboScale.select(0);
	}

	public void refillFieldsSeriesPersonalization(final ChartModel model, final ChartEditor editor, FormToolkit toolkit, final ScrolledForm form){
		if(model.getSeriesPersonalizationHashMap()!=null){
			for (Iterator iterator = model.getSeriesPersonalizationHashMap().keySet().iterator(); iterator.hasNext();) {
				String serName = (String) iterator.next();
				parsList.add(serName);
			}
			parsList.redraw();
		}
	}

}
