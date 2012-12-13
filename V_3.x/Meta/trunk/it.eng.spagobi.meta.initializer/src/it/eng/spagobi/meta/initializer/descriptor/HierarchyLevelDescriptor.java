/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.initializer.descriptor;

import it.eng.spagobi.meta.model.business.BusinessColumn;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CCombo;


/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class HierarchyLevelDescriptor {
	
	private BusinessColumn businessColumn;
	private String name;
	private String description;
	private String nameColumn;
	private String captionColumn;
	private boolean uniqueMembers;
	
	private TableItem ui_tableItem;
	private Text ui_textLevelName;
	private Button ui_buttonRemove;
	private Text ui_textDescription;
	private Text ui_textNameColumn;
	private Text ui_textCaptionColumn;
	private CCombo ui_comboUniqueMembers;


	
	
	/**
	 * @return the businessColumn
	 */
	public BusinessColumn getBusinessColumn() {
		return businessColumn;
	}
	/**
	 * @param businessColumn the businessColumn to set
	 */
	public void setBusinessColumn(BusinessColumn businessColumn) {
		this.businessColumn = businessColumn;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the nameColumn
	 */
	public String getNameColumn() {
		return nameColumn;
	}
	/**
	 * @param nameColumn the nameColumn to set
	 */
	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}
	/**
	 * @return the captionColumn
	 */
	public String getCaptionColumn() {
		return captionColumn;
	}
	/**
	 * @param captionColumn the captionColumn to set
	 */
	public void setCaptionColumn(String captionColumn) {
		this.captionColumn = captionColumn;
	}
	/**
	 * @return the uniqueMembers
	 */
	public boolean isUniqueMembers() {
		return uniqueMembers;
	}
	/**
	 * @param uniqueMembers the uniqueMembers to set
	 */
	public void setUniqueMembers(boolean uniqueMembers) {
		this.uniqueMembers = uniqueMembers;
	}
	/**
	 * @return the ui_textLevelName
	 */
	public Text getUi_textLevelName() {
		return ui_textLevelName;
	}
	/**
	 * @param ui_textLevelName the ui_textLevelName to set
	 */
	public void setUi_textLevelName(Text ui_textLevelName) {
		this.ui_textLevelName = ui_textLevelName;
	}
	/**
	 * @return the ui_buttonRemove
	 */
	public Button getUi_buttonRemove() {
		return ui_buttonRemove;
	}
	/**
	 * @param ui_buttonRemove the ui_buttonRemove to set
	 */
	public void setUi_buttonRemove(Button ui_buttonRemove) {
		this.ui_buttonRemove = ui_buttonRemove;
	}
	
	/**
	 * @return the ui_tableItem
	 */
	public TableItem getUi_tableItem() {
		return ui_tableItem;
	}
	/**
	 * @param ui_tableItem the ui_tableItem to set
	 */
	public void setUi_tableItem(TableItem ui_tableItem) {
		this.ui_tableItem = ui_tableItem;
	}
	/**
	 * @return the ui_textDescription
	 */
	public Text getUi_textDescription() {
		return ui_textDescription;
	}
	/**
	 * @param ui_textDescription the ui_textDescription to set
	 */
	public void setUi_textDescription(Text ui_textDescription) {
		this.ui_textDescription = ui_textDescription;
	}
	/**
	 * @return the ui_textNameColumn
	 */
	public Text getUi_textNameColumn() {
		return ui_textNameColumn;
	}
	/**
	 * @param ui_textNameColumn the ui_textNameColumn to set
	 */
	public void setUi_textNameColumn(Text ui_textNameColumn) {
		this.ui_textNameColumn = ui_textNameColumn;
	}
	/**
	 * @return the ui_textCaptionColumn
	 */
	public Text getUi_textCaptionColumn() {
		return ui_textCaptionColumn;
	}
	/**
	 * @param ui_textCaptionColumn the ui_textCaptionColumn to set
	 */
	public void setUi_textCaptionColumn(Text ui_textCaptionColumn) {
		this.ui_textCaptionColumn = ui_textCaptionColumn;
	}
	/**
	 * @return the ui_comboUniqueMembers
	 */
	public CCombo getUi_comboUniqueMembers() {
		return ui_comboUniqueMembers;
	}
	/**
	 * @param ui_comboUniqueMembers the ui_comboUniqueMembers to set
	 */
	public void setUi_comboUniqueMembers(CCombo ui_comboUniqueMembers) {
		this.ui_comboUniqueMembers = ui_comboUniqueMembers;
	}
	public void disposeUiElements() {
		ui_textLevelName.dispose();
		ui_buttonRemove.dispose();
		ui_textDescription.dispose();
		ui_textNameColumn.dispose();
		ui_textCaptionColumn.dispose();
		ui_comboUniqueMembers.dispose();
		ui_tableItem.dispose();
		
	}
	
	public String toString(){
		return "Name: "+name+" Column: "+businessColumn.getUniqueName()+" Description: "+description+" NameColumn: "+nameColumn+" CaptionColumn: "+captionColumn+" UniqueMembers: "+uniqueMembers;
	}
	

}
