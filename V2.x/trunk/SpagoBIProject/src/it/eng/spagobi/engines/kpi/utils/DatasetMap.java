/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.kpi.utils;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

import org.jfree.data.general.Dataset;

/**
 * 
 * @author Chiara Chiarelli
 * 
 */

public class DatasetMap {

		HashMap datasets;

		TreeSet series;
		Integer seriesNumber;
		HashMap categories;
		HashMap subCategories;
		Integer catsnum;
		Integer subcatsnum;
		Integer numberCatVisualization;
		String catTitle="category";
		String subcatTitle="subcategory";
		String serTitle="serie";
		int categoryCurrent=0;
		String valueSlider="1";
		String categoryCurrentName;
		Vector selectedSeries;
		boolean makeSlider=false;

		public DatasetMap() {
			this.datasets = new HashMap();
		}

		public void addDataset(String key, Dataset dataset){
			datasets.put(key, dataset);
		}



		public DatasetMap copyDatasetMap(Dataset dataset){
			DatasetMap copy=new DatasetMap();

			copy.setSeries(this.series);
			copy.setSeriesNumber(this.seriesNumber);
			copy.setCategories(this.getCategories());
			copy.setCatsnum(this.getCatsnum());
			copy.setNumberCatVisualization(this.getNumberCatVisualization());
			copy.setCatTitle(this.getCatTitle());
			copy.setSerTitle(this.getSerTitle());
			copy.setCategoryCurrent(this.getCategoryCurrent());
			copy.setValueSlider(this.getValueSlider());
			copy.setCategoryCurrentName(this.getCategoryCurrentName());		
			copy.setSelectedSeries(this.getSelectedSeries());
			copy.setMakeSlider(this.isMakeSlider());

			copy.addDataset("1", dataset);

			return copy;

		}


		public HashMap getDatasets() {
			return datasets;
		}

		public void setDatasets(HashMap datasets) {
			this.datasets = datasets;
		}


		public TreeSet getSeries() {
			return series;
		}

		public void setSeries(TreeSet series) {
			this.series = series;
		}

		public Integer getSeriesNumber() {
			return seriesNumber;
		}

		public void setSeriesNumber(Integer seriesNumber) {
			this.seriesNumber = seriesNumber;
		}

		public HashMap getCategories() {
			return categories;
		}

		public void setCategories(HashMap categories) {
			this.categories = categories;
		}

		public Integer getCatsnum() {
			return catsnum;
		}

		public void setCatsnum(Integer catsnum) {
			this.catsnum = catsnum;
		}

		public Integer getNumberCatVisualization() {
			return numberCatVisualization;
		}

		public void setNumberCatVisualization(Integer numberCatVisualization) {
			this.numberCatVisualization = numberCatVisualization;
		}

		public String getCatTitle() {
			return catTitle;
		}

		public void setCatTitle(String catTitle) {
			this.catTitle = catTitle;
		}

		public String getSerTitle() {
			return serTitle;
		}

		public void setSerTitle(String serTitle) {
			this.serTitle = serTitle;
		}

		public int getCategoryCurrent() {
			return categoryCurrent;
		}

		public void setCategoryCurrent(int categoryCurrent) {
			this.categoryCurrent = categoryCurrent;
		}

		public String getValueSlider() {
			return valueSlider;
		}

		public void setValueSlider(String valueSlider) {
			this.valueSlider = valueSlider;
		}

		public String getCategoryCurrentName() {
			return categoryCurrentName;
		}

		public void setCategoryCurrentName(String categoryCurrentName) {
			this.categoryCurrentName = categoryCurrentName;
		}

		public Vector getSelectedSeries() {
			return selectedSeries;
		}

		public void setSelectedSeries(Vector selectedSeries) {
			this.selectedSeries = selectedSeries;
		}

		public boolean isMakeSlider() {
			return makeSlider;
		}

		public void setMakeSlider(boolean makeSlider) {
			this.makeSlider = makeSlider;
		}

}
