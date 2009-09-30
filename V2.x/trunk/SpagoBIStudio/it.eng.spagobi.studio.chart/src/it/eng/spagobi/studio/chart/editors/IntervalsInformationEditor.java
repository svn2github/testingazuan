package it.eng.spagobi.studio.chart.editors;

import it.eng.spagobi.studio.chart.editors.model.chart.ChartModel;
import it.eng.spagobi.studio.chart.editors.model.chart.DialChartModel;
import it.eng.spagobi.studio.chart.utils.DrillParameters;
import it.eng.spagobi.studio.chart.utils.Interval;
import it.eng.spagobi.studio.chart.utils.SeriePersonalization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.dom4j.Document;
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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class IntervalsInformationEditor {

	Section sectionIntervals=null;
	Composite sectionClientIntervals=null;


	final Button newIntervalButton;
	final List intervalsList; 
	final Label intervalLabelLabel;
	final Text intervalLabelText;
	final Label intervalMinLabel;
	final Spinner intervalMinText;
	final Label intervalMaxLabel;
	final Spinner intervalMaxText;
	Composite innerSection; 
	final Label intervalColorLabel;
	final Button intervalColorButton;	
	final Button buttonRem;	

	public IntervalsInformationEditor(final DialChartModel dialModel, FormToolkit toolkit, final ScrolledForm form, final ChartEditor editor) {

		sectionIntervals= toolkit.createSection(form.getBody(), 
				Section.DESCRIPTION|Section.TITLE_BAR|Section.TWISTIE);
		sectionClientIntervals=toolkit.createComposite(sectionIntervals);

		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		sectionIntervals.setLayoutData(td);
		sectionIntervals.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		sectionIntervals.setText("Series Intervals");
		sectionIntervals.setDescription("Set all the intervals ");

		GridLayout gridLayout=new GridLayout();
		gridLayout.numColumns=3;
		sectionClientIntervals.setLayout(gridLayout);


		Image imageAdd = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_OBJ_ELEMENT);
		newIntervalButton=new Button(sectionClientIntervals, SWT.PUSH);
		newIntervalButton.setText("Add");
		newIntervalButton.setToolTipText("Add new Interval");
		newIntervalButton.setImage(imageAdd);

		Label spaceLabel=new Label(sectionClientIntervals, SWT.NULL);
		spaceLabel.setText("");
		Label spaceLabel2=new Label(sectionClientIntervals, SWT.NULL);
		spaceLabel2.setText("");


		intervalsList = new List (sectionClientIntervals, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		intervalsList.setToolTipText("intervals added");
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.verticalSpan = 4;
		gridData.verticalAlignment=SWT.CENTER;
		intervalsList.setSize(500, 1000);
		intervalsList.setLayoutData(gridData);
		int i=0;
		if(dialModel.getIntervals()!=null){
			for (Iterator iterator = dialModel.getIntervals().iterator(); iterator.hasNext();) {
				Interval interval = (Interval) iterator.next();
				intervalsList.add(Integer.valueOf(i).toString());
				i++;
			}			
		} //close if map is not null
		intervalsList.redraw();

		// Add Button Listener
		Listener addListener = new Listener() {
			public void handleEvent(Event event) {
				// Add a new Interval: put it in kiew
				int size=dialModel.getIntervals().size();
				intervalsList.add((new Integer(size)).toString(),size);
				Interval interval=new Interval();
				dialModel.getIntervals().add(size,interval);
			}
		};
		newIntervalButton.addListener(SWT.Selection, addListener);

		intervalLabelLabel=new Label(sectionClientIntervals,SWT.NULL);
		intervalLabelLabel.setText("label");
		intervalLabelText=new Text(sectionClientIntervals, SWT.BORDER);;
		intervalLabelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		intervalLabelText.setToolTipText("Label of the interval to add");

		intervalLabelText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				String newLabel = intervalLabelText.getText();
				int selection = intervalsList.getSelectionIndex();
				if(selection!=-1){
					Interval intervalSelected=dialModel.getIntervals().get(selection);

					if(intervalSelected!=null && newLabel!=null)
					{intervalSelected.setLabel(newLabel);
					}
				}
			}
		});

		intervalLabelLabel.setEnabled(false);
		intervalLabelText.setEnabled(false);


		intervalMinLabel=new Label(sectionClientIntervals,SWT.NULL);
		intervalMinLabel.setText("min");
		intervalMinText=new Spinner(sectionClientIntervals, SWT.BORDER);
		intervalMinText.setToolTipText("Minimum value of the interval");
		intervalMinText.setMaximum(1000000);
		intervalMinText.setMinimum(-1000000);
		intervalMinText.setDigits(1);
		intervalMinText.setSelection(00);
		
		//intervalMinText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		intervalMinText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				if(editor!=null) editor.setIsDirty(true);
				double newMin = intervalMinText.getSelection()/ Math.pow(10, intervalMinText.getDigits());
				Double newMinD=null;
				try{
					newMinD=Double.valueOf(newMin);
				}
				catch (Exception e) {
					newMinD=new Double(0.0);
				}
				int selection = intervalsList.getSelectionIndex();
				if(selection!=-1){
					Interval intervalSelected=dialModel.getIntervals().get(selection);
					if(intervalSelected!=null)
					{
						intervalSelected.setMin(Double.valueOf(newMin));
					}
				}
			}
		});

		intervalMinLabel.setEnabled(false);
		intervalMinText.setEnabled(false);


		intervalMaxLabel=new Label(sectionClientIntervals,SWT.NULL);
		intervalMaxLabel.setText("max");
		intervalMaxText=new Spinner(sectionClientIntervals, SWT.BORDER);;
		//intervalMaxText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		intervalMaxText.setToolTipText("Maximum value of the interval");
		intervalMaxText.setMaximum(1000000);
		intervalMaxText.setMinimum(-1000000);
		intervalMaxText.setDigits(1);
		intervalMaxText.setSelection(00);
		
		intervalMaxText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				if(editor!=null) editor.setIsDirty(true);
				double newMax = intervalMaxText.getSelection()/ Math.pow(10, intervalMaxText.getDigits());
				Double newMaxD=null;
				try{
					newMaxD=Double.valueOf(newMax);
				}
				catch (Exception e) {
					newMaxD=new Double(0.0);
				}
				int selection = intervalsList.getSelectionIndex();
				if(selection!=-1){
					Interval intervalSelected=dialModel.getIntervals().get(selection);
					if(intervalSelected!=null)
					{
						intervalSelected.setMax(Double.valueOf(newMax));
					}
				}
			}
		});


		intervalMaxLabel.setEnabled(false);
		intervalMaxText.setEnabled(false);

		innerSection = toolkit.createComposite(sectionClientIntervals);

		intervalColorLabel=new Label(sectionClientIntervals,SWT.BORDER);
		intervalColorLabel.setText("color");
		intervalColorLabel.setToolTipText("Color of the interval");
		intervalColorButton= new Button(innerSection, SWT.PUSH);

		final Color color = new org.eclipse.swt.graphics.Color(sectionClientIntervals.getDisplay(), new RGB(255,255,255));
		GridLayout colorGd = new GridLayout();
		colorGd.numColumns = 2;
		colorGd.marginHeight = 0;
		colorGd.marginBottom = 0;
		innerSection.setLayout(colorGd);
		intervalColorLabel.setText("          ");
		intervalColorLabel.setBackground(color);
		intervalColorButton.setText("Color...");
		final Shell parentShell = sectionClientIntervals.getShell();
		intervalColorButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				final Shell centerShell = new Shell(parentShell, SWT.NO_TRIM);
				centerShell.setLocation(
						(parentShell.getSize().x - ChartEditor.COLORDIALOG_WIDTH) / 2,
						(parentShell.getSize().y - ChartEditor.COLORDIALOG_HEIGHT) / 2);
				ColorDialog colorDg = new ColorDialog(centerShell,
						SWT.APPLICATION_MODAL);
				colorDg.setRGB(intervalColorLabel.getBackground().getRGB());
				colorDg.setText("Choose a color");
				RGB rgb = colorDg.open();
				//final Map<String, SeriePersonalization> parsMap=model.getSeriesPersonalizationHashMap();					
				if (rgb != null) {
					// Dispose the old color, create the
					// new one, and set into the label
					color.dispose();
					Color newColor = new Color(parentShell.getDisplay(), rgb);
					intervalColorLabel.setBackground(newColor);
					if(editor!=null) editor.setIsDirty(true);
					String newHexadecimal = ChartEditor.convertRGBToHexadecimal(rgb);
					int selection = intervalsList.getSelectionIndex();
					//get ParSelected
					if(selection!=-1){
						Interval interval=dialModel.getIntervals().get(selection);
						interval.setColor(ChartEditor.convertHexadecimalToRGB(newHexadecimal));						}
				}
				//centerShell.pack();
				centerShell.dispose();
			}

		});			
		intervalColorLabel.setEnabled(false);
		intervalColorButton.setEnabled(false);

		Image imageRem = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_TOOL_DELETE);
		buttonRem = new Button(sectionClientIntervals, SWT.PUSH);
		buttonRem.setToolTipText("Remove (can remove only the last inserted)");
		buttonRem.setImage(imageRem);
		buttonRem.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		buttonRem.setEnabled(false);
		buttonRem.pack();

		// Add listener that show details of parameter selected
		intervalsList.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				// get par selected
				int selection = intervalsList.getSelectionIndex();
				if(selection!=-1){
					Interval intervalSelected=dialModel.getIntervals().get(selection);
					// put the default value

					String label=intervalSelected.getLabel()!=null ? intervalSelected.getLabel() : "";
					intervalLabelText.setText(label);
					
					String min;
					if(intervalSelected.getMin()!=null){
						min=intervalSelected.getMin().toString();						
					}
					else{
						min="";
					}
					int indexPoint=min.indexOf('.');
					if(indexPoint!=-1){
						min=ChartEditorUtils.removeChar(min, '.');
					}
					Integer minI=null;
					try{
						minI=Integer.valueOf(min);	
					}
					catch (Exception e2) {
						minI=Integer.valueOf(00);
					}

					intervalMinText.setSelection(minI);
					
					
					String max;
					if(intervalSelected.getMax()!=null){
						max=intervalSelected.getMax().toString();						
					}
					else{
						max="";
					}
					int indexP=max.indexOf('.');
					if(indexP!=-1){
						max=ChartEditorUtils.removeChar(max, '.');
					}
					Integer maxI=null;
					try{
						maxI=Integer.valueOf(max);	
					}
					catch (Exception e2) {
						maxI=Integer.valueOf(00);
					}

					intervalMaxText.setSelection(maxI);

					
					if(intervalSelected.getColor()!=null){
						Color newColor = new Color(parentShell.getDisplay(), intervalSelected.getColor());
						intervalColorLabel.setBackground(newColor);
					}
					else
					{
						intervalColorLabel.setBackground(null);
					}


					intervalColorButton.setEnabled(true);
					intervalColorLabel.setEnabled(true);
					intervalLabelLabel.setEnabled(true);
					intervalLabelText.setEnabled(true);
					intervalMaxLabel.setEnabled(true);
					intervalMaxText.setEnabled(true);
					intervalMinText.setEnabled(true);
					intervalMinLabel.setEnabled(true);
				}
				if(selection==dialModel.getIntervals().size()-1){
					buttonRem.setEnabled(true);
				}
				else{
					buttonRem.setEnabled(false);

				}
			}
		});


		// Add Button Listener
		Listener cancelListener = new Listener() {
			public void handleEvent(Event event) {
				int index=intervalsList.getSelectionIndex();
				if(index!=-1 && index==dialModel.getIntervals().size()-1){
					//can erase only the last
					dialModel.getIntervals().remove(index);
					intervalsList.remove(index);
					intervalLabelText.setText("");
					intervalMinText.setSelection(00);
					intervalMaxText.setSelection(00);
					intervalColorLabel.setBackground(null);
					intervalsList.remove(index);
					intervalColorButton.setEnabled(false);
					intervalColorLabel.setEnabled(false);
					intervalLabelLabel.setEnabled(false);
					intervalLabelText.setEnabled(false);
					intervalMaxLabel.setEnabled(false);
					intervalMaxText.setEnabled(false);
					intervalMinText.setEnabled(false);
					intervalMinLabel.setEnabled(false);
					buttonRem.setEnabled(false);
				}
			}
		};
		buttonRem.addListener(SWT.Selection, cancelListener);

		sectionIntervals.setClient(sectionClientIntervals);

	}











	public void setVisible(boolean visible){
		sectionIntervals.setVisible(visible);

	}

	public boolean isVisible(){
		if(sectionIntervals.isVisible())return true;
		else return false;
	}



	public void eraseComposite(){
		intervalsList.removeAll();
		intervalColorLabel.setBackground(null);
		intervalLabelText.setText("");
		intervalMaxText.setSelection(00);
		intervalMinText.setSelection(00);
		buttonRem.setEnabled(false);
	}

	public void refillFieldsSeriesPersonalization(final DialChartModel dialmodel, final ChartEditor editor, FormToolkit toolkit, final ScrolledForm form){
		if(dialmodel.getIntervals()!=null){
			for (int j = 0; j < dialmodel.getIntervals().size(); j++) {
				Interval inter= (Interval) dialmodel.getIntervals().get(j);
				intervalsList.add(new Integer(j).toString());				
			}
			
			intervalsList.redraw();
		}
	}

}
