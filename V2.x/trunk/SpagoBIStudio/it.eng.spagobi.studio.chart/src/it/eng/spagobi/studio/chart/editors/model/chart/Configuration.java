package it.eng.spagobi.studio.chart.editors.model.chart;

import it.eng.spagobi.studio.chart.editors.ChartEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.eclipse.osgi.framework.internal.core.Tokenizer;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class Configuration {

	protected Parameter[] parameters;

	public Configuration(String type, Document configurationDocument) {
		try {
			parameters = getConfigurationParametersForType(type, configurationDocument);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Configuration() {
		// TODO Auto-generated constructor stub
	}
	public Parameter[] getParameters() {
		return parameters;
	}
	public String getParameterValue(String parameterName) {
		if (parameters == null || parameters.length == 0) return null;
		for (int i = 0; i < parameters.length; i++) {
			Parameter aParameter = parameters[i];
			String name = aParameter.getName();
			if (name.equals(parameterName)) return aParameter.getValue();
		}
		return null;
	}
	public void setParameterValue(String parameterName, String parameterValue) throws Exception {
		if (parameters == null || parameters.length == 0) return;
		for (int i = 0; i < parameters.length; i++) {
			Parameter aParameter = parameters[i];
			String name = aParameter.getName();
			if (name.equals(parameterName)) {
				int type = aParameter.getType();
				switch (type) {
				case Parameter.NUMBER_TYPE:
					try {
						Integer.parseInt(parameterValue);
					} catch (NumberFormatException nfe) {
						throw new Exception("Parameter '" + parameterName + "' is not a valid integer");
					}
					break;
				case Parameter.COLOR_TYPE:
					// TODO check color syntax
					break;
				default:
					break;
				}
				aParameter.setValue(parameterValue);
			}
		}
	}
	
	
//	public void setAllParametersValueFromModel(ChartModel model) throws Exception{
//		HashMap<String, Object> parametersInModel=model.getParameters();
//		for (int i = 0; i < parameters.length; i++) {
//			Parameter aParameter = parameters[i];
//			String name = aParameter.getName();
//			if (parametersInModel.containsKey(name)) {
//				Object recorderValue=parametersInModel.get(name);
//				int type = aParameter.getType();
//				switch (type) {
//				case Parameter.NUMBER_TYPE:
//					try {
//						Integer.parseInt(recorderValue.toString());
//					} catch (NumberFormatException nfe) {
//						throw new Exception("Parameter '" + name + "' is not a valid integer");
//					}
//					break;
//				case Parameter.COLOR_TYPE:
//					break;
//				default:
//					break;
//				}
//				aParameter.setValue(recorderValue.toString());
//			}
//		}
//		
//	}
	

	public Parameter[] getConfigurationParametersForType(String chartType, Document document) throws Exception {
		Parameter[] toReturn = null;
		// check the type and search for the root
		String upperCaseNameSl=chartType.toUpperCase();
		String upperCaseNamePl=upperCaseNameSl+"S";

		Node commonConfig = document.selectSingleNode("//"+upperCaseNamePl+"/"+upperCaseNameSl+"[@name='commons']");
		if (commonConfig == null ) throw new Exception("No common configuration set");

		List configuredParameters = commonConfig.selectNodes("CONF/PARAMETER");
		toReturn = new Parameter[configuredParameters.size()];
		for (int j = 0; j < configuredParameters.size(); j++) {
			Node aConfiguredParameter = (Node) configuredParameters.get(j);
			String name = aConfiguredParameter.valueOf("@name");
			String description = aConfiguredParameter.valueOf("@description");
			String typeStr = aConfiguredParameter.valueOf("@type");
			ArrayList<String> predefinedValues=null;
			int type;
			if (typeStr.equals("NUMBER")) type = Parameter.NUMBER_TYPE;
			else if (typeStr.equals("STRING")) type = Parameter.STRING_TYPE;
			else if (typeStr.equals("COLOR")) type = Parameter.COLOR_TYPE;
			else if (typeStr.equals("BOOLEAN")) type = Parameter.BOOLEAN_TYPE;
			else if (typeStr.equals("COMBO")) {
				type = Parameter.COMBO_TYPE;
				predefinedValues=new ArrayList<String>();
				String valueString = aConfiguredParameter.valueOf("@values");
				// TODO : Parse values with ,
				List values=new ArrayList<String>();
				values.add("a");
				values.add("b");				
			}
			else throw new Exception("Parameter type for parameter " + name + " not supported");
			toReturn[j] = new Parameter(name, "", description, type);
		}
		return toReturn;
	}
	

	
	

	public void createForm(final ChartEditor editor, Composite section, FormToolkit toolkit) {
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		section.setLayout(gl);
		Parameter[] parameters = this.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			final Parameter aParameter = parameters[i];
			Label parameterDescriptionLabel = new Label(section, SWT.NULL);
			parameterDescriptionLabel.setText(aParameter.getDescription() + ":");
			int parameterType = aParameter.getType();
			switch (parameterType) {
			case Parameter.COLOR_TYPE:
				Composite innerSection = toolkit.createComposite(section);
				GridLayout colorGd = new GridLayout();
				colorGd.numColumns = 2;
				colorGd.marginHeight = 0;
				colorGd.marginBottom = 0;
				innerSection.setLayout(colorGd);
				final Label colorLabel = new Label(innerSection, SWT.BORDER);
				colorLabel.setText("          ");
				String hexadecimal = aParameter.getValue();
				RGB rgb = ChartEditor.convertHexadecimalToRGB(hexadecimal);
				final Color color = new Color(section.getDisplay(), rgb);
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
							editor.setIsDirty(true);
							String newHexadecimal = ChartEditor.convertRGBToHexadecimal(rgb);
							aParameter.setValue(newHexadecimal);
						}
						centerShell.dispose();
					}
				});
				break;
			case Parameter.BOOLEAN_TYPE:
				final Button check = toolkit.createButton(section, "", SWT.CHECK);
				check.setSelection(Boolean.parseBoolean(aParameter.getValue()));
				check.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						editor.setIsDirty(true);
						aParameter.setValue(Boolean.toString(check.getSelection()));
					}
				});
				break;
			default:
				final Text parameterValueText = new Text(section, SWT.BORDER);
			parameterValueText.setText(aParameter.getValue());
			parameterValueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			parameterValueText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					editor.setIsDirty(true);
					String parameterValueStr = parameterValueText.getText();
					aParameter.setValue(parameterValueStr);
				}
			});
			}
		}
	}

	public String toXML() {
		String toReturn = 
			"    <CONF>\n";
		for (int i = 0; i < parameters.length; i++) {
			Parameter aParameter = parameters[i];
			toReturn +=
				"		<PARAMETER name='" + aParameter.getName() + "' value='" + aParameter.getValue() + "' />\n";
		}
		toReturn +=
			"    </CONF>\n";
		return toReturn;
	}
}
