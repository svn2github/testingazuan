package it.eng.spagobi.studio.dashboard.editors.model.dashboard.grid;

import it.eng.spagobi.studio.dashboard.editors.DashboardEditor;
import it.eng.spagobi.studio.dashboard.editors.model.dashboard.Configuration;
import it.eng.spagobi.studio.dashboard.editors.model.dashboard.DashboardModel;
import it.eng.spagobi.studio.dashboard.editors.model.dashboard.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class GridConfiguration extends Configuration {
	
	protected LinkColumn[] linkColumns;
	protected LightColumn[] lightColumns;
	protected NameColumn[] nameColumns;
	protected DimensionColumn[] dimensionColumns;
	
	public GridConfiguration(String movie, Document configurationDocument) {
		super(movie, configurationDocument);
	}
	
	public LinkColumn[] getLinkColumns() {
		return linkColumns;
	}
	public void setLinkColumns(LinkColumn[] linkColumns) {
		this.linkColumns = linkColumns;
	}
	public LightColumn[] getLightColumns() {
		return lightColumns;
	}
	public void setLightColumns(LightColumn[] lightColumns) {
		this.lightColumns = lightColumns;
	}
	public NameColumn[] getNameColumns() {
		return nameColumns;
	}
	public void setNameColumns(NameColumn[] nameColumns) {
		this.nameColumns = nameColumns;
	}
	public DimensionColumn[] getDimensionColumns() {
		return dimensionColumns;
	}
	public void setDimensionColumns(DimensionColumn[] dimensionColumns) {
		this.dimensionColumns = dimensionColumns;
	}

	public void createForm(final DashboardEditor editor, Composite section, FormToolkit toolkit) {
		super.createForm(editor, section, toolkit);
		DashboardModel model = editor.getModel();
		String[] columns = model.getLov().getColumns();
		for (int i = 0; i < columns.length; i++) {
			Label columnNameLabel = new Label(section, SWT.NULL);
			columnNameLabel.setText("Name for column '" + columns[i] + "':");
			final Text columnAssignedName = new Text(section, SWT.BORDER);
			String assignedName = getColumnAssignedName(i);
			columnAssignedName.setText(assignedName);
			columnAssignedName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			final int index = i;
			columnAssignedName.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					editor.setIsDirty(true);
					String newAssignedName = columnAssignedName.getText();
					setColumnAssignedName(index, newAssignedName);
				}
			});
		}
	}
	
	public String toXML() {
		String toReturn = 
			"    <CONFIGURATION>\n" +
			"    	<PARAMETERS>\n";
		for (int i = 0; i < parameters.length; i++) {
			Parameter aParameter = parameters[i];
			toReturn +=
				"		<PARAMETER name='" + aParameter.getName() + "' value='" + aParameter.getValue() + "' />\n";
		}
		toReturn +=
			"    	</PARAMETERS>\n" +
			"		<LINKCOLUMNS>\n";
		for (int i = 0; i < linkColumns.length; i++) {
			LinkColumn link = linkColumns[i];
			toReturn +=
				"		<COLUMN index='" + link.getIndex() + "' onlyheader='" + link.isOnlyheader() + "' " +
						"fixedquerystring='" + link.getFixedquerystring() + "' prefixvalue='" + link.getPrefixvalue() + "' />\n";
		}
		toReturn +=
			"		</LINKCOLUMNS>\n" +
			"		<NAMECOLUMNS>\n";
		for (int i = 0; i < nameColumns.length; i++) {
			NameColumn nameColumn = nameColumns[i];
			toReturn +=
				"		<COLUMN index='" + nameColumn.getIndex() + "' name='" + nameColumn.getAssignedName() + "' />\n";
		}
		toReturn +=
			"		</NAMECOLUMNS>\n" +
			"		<DIMENSIONCOLUMNS>\n";
		for (int i = 0; i < dimensionColumns.length; i++) {
			DimensionColumn dimensionColumn = dimensionColumns[i];
			toReturn +=
				"		<COLUMN index='" + dimensionColumn.getIndex() + "' width='" + dimensionColumn.getWidth() + "' />\n";
		}
		toReturn +=
			"		</DIMENSIONCOLUMNS>\n" +
			"		<LIGHTCOLUMNS>\n";
		for (int i = 0; i < lightColumns.length; i++) { 
			LightColumn lightColumn = lightColumns[i];
			toReturn +=
				"		<COLUMN index='" + lightColumn.getIndex() + "' defaultcolor='" + lightColumn.getDefaultcolor() + "' " +
						" defaulttooltip='" + lightColumn.getDefaulttooltip() + "' >\n";
			Condition[] conditions = lightColumn.getConditions();
			if (conditions != null && conditions.length > 0) {
				toReturn += 
					"			<CONDITIONS>\n";
				for (int j = 0; j < conditions.length; j++) {
					Condition condition = conditions[j];
					toReturn +=
						"		<CONDITION operator='" + condition.getOperator() + "' value1='" + condition.getValue1() + "' " +
								" value2='" + condition.getValue2() + "' conditioncolor='" + condition.getConditioncolor() + "' " +
								" tooltip='" + condition.getTooltip() + "' showvalueintotooltip='" + condition.isShowvalueintotooltip() + 
								"' />\n";
				}
				toReturn += 
					"			</CONDITIONS>\n";
			} else {
				toReturn += 
					"			<CONDITIONS/>\n";
			}
			toReturn += 
				"		</COLUMN>\n";
		}
		toReturn +=
			"    	</LIGHTCOLUMNS>\n" +
			"    </CONFIGURATION>\n";
		return toReturn;
	}
	
	public String getColumnAssignedName(int i) {
		for (int j = 0; j < nameColumns.length; j++) {
			NameColumn nameColumn = nameColumns[j];
			if (nameColumn.getIndex() == i) {
				return nameColumn.getAssignedName();
			}
		}
		return "";
	}
	
	public void setColumnAssignedName(int index, String assignedName) {
		boolean found = false;
		for (int j = 0; j < nameColumns.length; j++) {
			NameColumn nameColumn = nameColumns[j];
			if (nameColumn.getIndex() == index) {
				nameColumn.setAssignedName(assignedName);
				found = true;
				break;
			}
		}
		if (!found) {
			try {
				NameColumn[] newNameColumns = new NameColumn[nameColumns.length + 1];
				for (int i = 0; i < nameColumns.length; i++) {
					newNameColumns[i] = nameColumns[i];
				}
				NameColumn newNameColumn = new NameColumn();
				newNameColumn.setIndex(index);
				newNameColumn.setAssignedName(assignedName);
				newNameColumns[nameColumns.length] = newNameColumn;
				nameColumns = newNameColumns;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
