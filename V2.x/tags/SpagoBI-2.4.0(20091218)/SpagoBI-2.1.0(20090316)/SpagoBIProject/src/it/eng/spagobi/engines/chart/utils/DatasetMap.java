package it.eng.spagobi.engines.chart.utils;


import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.BarCharts;
import it.eng.spagobi.engines.chart.bo.charttypes.barcharts.StackedBarGroup;
import it.eng.spagobi.engines.chart.bo.charttypes.clusterchart.ClusterCharts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultXYZDataset;

public class DatasetMap {

	HashMap datasets;

	TreeSet series;
	Integer seriesNumber;
	HashMap categories;
	HashMap subCategories;
	Integer catsnum;
	Integer subcatsnum;
	Integer numberCatVisualization;
	Integer numberSerVisualization;
	String catTitle="category";
	String subcatTitle="subcategory";
	String serTitle="serie";
	int categoryCurrent=0;
	String valueSlider="1";
	String categoryCurrentName;
	Vector selectedSeries;
	Vector selectedCatGroups;
	boolean makeSlider=false;

	public DatasetMap() {
		this.datasets = new HashMap();
	}

	public void addDataset(String key, Dataset dataset){
		datasets.put(key, dataset);
	}



	public DatasetMap filteringSimpleBarChart(HttpServletRequest request, BarCharts sbi, String sbiMode, boolean docComposition){

		DefaultCategoryDataset dataset=(DefaultCategoryDataset)datasets.get("1");
		Dataset copyDataset=null;
		try {
			copyDataset = (DefaultCategoryDataset)dataset.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		series=new TreeSet(((DefaultCategoryDataset)dataset).getRowKeys());

		//fill the serieNumber MAP by mapping each serie name to its position in the dataset, needed to recover right colors when redrawing
		/*	for(int i=0;i<series.size();i++){
			String s=(String)series.get(i);
			sbi.putSeriesNumber(s,(i+1));
		}*/

		categories=(HashMap)((BarCharts)sbi).getCategories();
		catsnum=new Integer(sbi.getCategoriesNumber());
		numberCatVisualization=sbi.getNumberCatVisualization();
		numberSerVisualization=sbi.getNumberSerVisualization();
		
		catTitle=sbi.getCategoryLabel();
		serTitle=sbi.getValueLabel();

		// if slider specifies a category than set view from that point
		if(request.getParameter("category")!=null){
			String catS=(String)request.getParameter("category");
			Double catD=Double.valueOf(catS);
			categoryCurrent=catD.intValue();
		}
		else{ //else set view from first category
			categoryCurrent=1;
		}
		valueSlider=(new Integer(categoryCurrent)).toString();
		HashMap cats=(HashMap)((BarCharts)sbi).getCategories();


		if(categoryCurrent!=0){
			categoryCurrentName=(String)cats.get(new Integer(categoryCurrent));
			copyDataset=(DefaultCategoryDataset)sbi.filterDataset(copyDataset,categories,categoryCurrent,numberCatVisualization.intValue());				
		}
		else{
			categoryCurrentName="All";
		}



		// CHECK IF THERE IS TO FILTER CAT_GROUPS
		selectedCatGroups=new Vector();
		if(sbi.isFilterCatGroups()==true){
			
			if(request.getParameter("cat_group")!=null){
				// Check if particular cat_groups has been chosen

				String[] cio=request.getParameterValues("cat_group");
				//Convert array in vector
				for(int i=0;i<cio.length;i++){
					selectedCatGroups.add(cio[i]);
				}
			}
			else{
				selectedCatGroups.add("allgroups");
			}

			// if selectedSerie contains allseries 
			if(selectedCatGroups.contains("allgroups")){
				((BarCharts)sbi).setCurrentCatGroups(null);
			}
			else{	
				copyDataset=sbi.filterDatasetCatGroups(copyDataset,selectedCatGroups);	

			}
		}
		else selectedCatGroups.add("allgroups");


		// CHECK IF THERE IS TO FILTER SERIES
		selectedSeries=new Vector();
		if(sbi.isFilterSeries()==true){
			// Check if particular series has been chosen
			
			if(request.getParameter("serie")!=null){
				String[] cio=request.getParameterValues("serie");
				//Convert array in vector
				for(int i=0;i<cio.length;i++){
					selectedSeries.add(cio[i]);
				}
			}
			else{
				//if(!sbiMode.equalsIgnoreCase("WEB") && !docComposition)
				//if(!sbiMode.equalsIgnoreCase("WEB") || docComposition)

				selectedSeries.add("allseries");
			}


			// if selectedSerie contains allseries 
			if(selectedSeries.contains("allseries")){
				((BarCharts)sbi).setCurrentSeries(null);
			}
			else{	
				copyDataset=sbi.filterDatasetSeries(copyDataset,selectedSeries);	

			}
		}
		else selectedSeries.add("allseries");

		// consider if drawing the slider
		if(sbi.isFilterCategories()==true && (catsnum.intValue())>numberCatVisualization.intValue()){
			makeSlider=true;	    	
		}

		if(copyDataset==null){copyDataset=dataset;}

		DatasetMap newDatasetMap=this.copyDatasetMap(copyDataset);



		return newDatasetMap;

	}




	public DatasetMap copyDatasetMap(Dataset dataset){
		DatasetMap copy=new DatasetMap();

		copy.setSeries(this.series);
		copy.setSeriesNumber(this.seriesNumber);
		copy.setCategories(this.getCategories());
		copy.setCatsnum(this.getCatsnum());
		copy.setNumberCatVisualization(this.getNumberCatVisualization());
		copy.setNumberSerVisualization(this.getNumberSerVisualization());
		copy.setCatTitle(this.getCatTitle());
		copy.setSerTitle(this.getSerTitle());
		copy.setCategoryCurrent(this.getCategoryCurrent());
		copy.setValueSlider(this.getValueSlider());
		copy.setCategoryCurrentName(this.getCategoryCurrentName());		
		copy.setSelectedSeries(this.getSelectedSeries());
		copy.setMakeSlider(this.isMakeSlider());
		copy.selectedCatGroups=this.getSelectedCatGroups();
		copy.addDataset("1", dataset);

		return copy;

	}



	public DatasetMap filteringMultiDatasetBarChart(HttpServletRequest request, BarCharts sbi, String sbiMode, boolean docComposition){

		DatasetMap newDatasetMap=new DatasetMap();

		series=new TreeSet();


		for (Iterator iterator = datasets.keySet().iterator(); iterator.hasNext();) {
			String  key = (String ) iterator.next();

			DefaultCategoryDataset dataset=(DefaultCategoryDataset) datasets.get(key);  // this is the old dataset to filter


			Dataset copyDataset=null;
			try {
				copyDataset = (DefaultCategoryDataset)dataset.clone();	// clone dataset
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// all series present in the dataset
			// add found series if the number is less then the max number of visualization
			int contSer = 0;		
			for (Iterator iterator2 = (((DefaultCategoryDataset)dataset).getRowKeys()).iterator(); iterator2.hasNext();) {
				if (this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization()){
					String serie = (String) iterator2.next();
					if(!series.contains(serie)){
						series.add(serie);
						contSer++;
					}
				}
				else if (this.getNumberSerVisualization() == 0){
					String serie = (String) iterator2.next();
					if(!series.contains(serie)){
						series.add(serie);
					}
				}
			}


			categories=(HashMap)((BarCharts)sbi).getCategories();
			catsnum=new Integer(sbi.getCategoriesNumber());
			numberCatVisualization=sbi.getNumberCatVisualization();
			numberSerVisualization=sbi.getNumberSerVisualization();
			
			catTitle=sbi.getCategoryLabel();
			serTitle=sbi.getValueLabel();

			// if slider specifies a category than set view from that point
			if(request.getParameter("category")!=null){
				String catS=(String)request.getParameter("category");
				Double catD=Double.valueOf(catS);
				categoryCurrent=catD.intValue();
			}
			else{ //else set view from first category
				categoryCurrent=1;
			}
			valueSlider=(new Integer(categoryCurrent)).toString();
			HashMap cats=(HashMap)((BarCharts)sbi).getCategories();


			if(categoryCurrent!=0){
				categoryCurrentName=(String)cats.get(new Integer(categoryCurrent));
				copyDataset=(DefaultCategoryDataset)sbi.filterDataset(copyDataset,categories,categoryCurrent,numberCatVisualization.intValue());				
			}
			else{
				categoryCurrentName="All";
			}

			// Check if particular series has been chosen
			selectedSeries=new Vector();
			if(request.getParameter("serie")!=null){
				String[] cio=request.getParameterValues("serie");
				//Convert array in vector
				for(int i=0;i<cio.length;i++){
					selectedSeries.add(cio[i]);
				}
			}
			else{
				//if(!sbiMode.equalsIgnoreCase("WEB") && !docComposition)
				selectedSeries.add("allseries");
			}


			// if selectedSerie contains allseries 
			if(selectedSeries.contains("allseries")){
				((BarCharts)sbi).setCurrentSeries(null);
			}
			else{	
				copyDataset=sbi.filterDatasetSeries(copyDataset,selectedSeries);	

			}
			// consider if drawing the slider
			if((catsnum.intValue())>numberCatVisualization.intValue()){
				makeSlider=true;	    	
			}


			newDatasetMap.getDatasets().put(key, copyDataset);



		}
		return newDatasetMap;

	}





	public DatasetMap filteringClusterChart(HttpServletRequest request, ClusterCharts sbi, String sbiMode, boolean docComposition){

		DefaultXYZDataset dataset=(DefaultXYZDataset)datasets.get("1");
		DefaultXYZDataset copyDataset=null;
		try {
			copyDataset = (DefaultXYZDataset)dataset.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// get the selected series from request
		selectedSeries=new Vector();
		if(request.getParameter("serie")!=null){
			String[] cio=request.getParameterValues("serie");
			//Convert array in vector
			for(int i=0;i<cio.length;i++){
				selectedSeries.add(cio[i]);
			}
		}
		else{
			//if(!sbiMode.equalsIgnoreCase("WEB") && !docComposition)
			selectedSeries.add("allseries");
		}

		int numSeries=dataset.getSeriesCount();

		// fill the vector containing current series
		series=new TreeSet();
		for(int i=0;i<numSeries;i++){
			if (this.getNumberSerVisualization() > 0 && i < this.getNumberSerVisualization()){
				String nome=(String)dataset.getSeriesKey(i);
				series.add(nome);	
			}
			else if (this.getNumberSerVisualization() == 0){ //tutte le serie
				String nome=(String)dataset.getSeriesKey(i);
				series.add(nome);	
			}
			
		}

		// if all series selected return the copy of dataset
		if(selectedSeries.contains("allseries")){
			DatasetMap newDatasetMap=this.copyDatasetMap(copyDataset);
			return newDatasetMap;
		}


		// if not all series limits to selected ones	
		for(int i=0;i<numSeries;i++){
			String nome=(String)dataset.getSeriesKey(i);

			if(!selectedSeries.contains(nome)){
				copyDataset.removeSeries(nome);
				//series.remove(nome);
			}

		}


		DatasetMap newDatasetMap=this.copyDatasetMap(copyDataset);

		return newDatasetMap;

	}


	public DatasetMap filteringGroupedBarChart(HttpServletRequest request, StackedBarGroup sbi, String sbiMode, boolean docComposition){

		DefaultCategoryDataset dataset=(DefaultCategoryDataset)datasets.get("1");
		Dataset copyDataset=null;
		try {
			copyDataset = (DefaultCategoryDataset)dataset.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		series=new TreeSet();
	//	series=new TreeSet(((DefaultCategoryDataset)dataset).getRowKeys());
		
		int contSer = 0;
		for (Iterator iterator2 = (((DefaultCategoryDataset)dataset).getRowKeys()).iterator(); iterator2.hasNext();) {
			if (this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization()){
				String serie = (String) iterator2.next();
				if(!series.contains(serie)){
					series.add(serie);
					contSer++;
				}
			}
			else if (this.getNumberSerVisualization() == 0 ){
				String serie = (String) iterator2.next();
				if(!series.contains(serie))
					series.add(serie);
			}
			
		}


		//fill the serieNumber MAP by mapping each serie name to its position in the dataset, needed to recover right colors when redrawing
		/*	for(int i=0;i<series.size();i++){
			String s=(String)series.get(i);
			sbi.putSeriesNumber(s,(i+1));
		}*/

		categories=(HashMap)((BarCharts)sbi).getCategories();
		catsnum=new Integer(sbi.getRealCatNumber());
		numberCatVisualization=sbi.getNumberCatVisualization();
		numberSerVisualization=sbi.getNumberSerVisualization(); 
		
		subCategories=(HashMap)((StackedBarGroup)sbi).getSubCategories();


		catTitle=sbi.getCategoryLabel();
		subcatTitle = sbi.getSubCategoryLabel();
		serTitle=sbi.getValueLabel();

		// if slider specifies a category than set view from that point
		if(request.getParameter("category")!=null){
			String catS=(String)request.getParameter("category");
			Double catD=Double.valueOf(catS);
			categoryCurrent=catD.intValue();
		}
		else{ //else set view from first category
			categoryCurrent=1;
		}
		valueSlider=(new Integer(categoryCurrent)).toString();
		HashMap cats=(HashMap)((BarCharts)sbi).getCategories();


		if(categoryCurrent!=0){
			categoryCurrentName=(String)cats.get(new Integer(categoryCurrent));
			copyDataset=(DefaultCategoryDataset)sbi.filterDataset(copyDataset,categories,categoryCurrent,numberCatVisualization.intValue());				
		}
		else{
			categoryCurrentName="All";
		}

		// Check if particular series has been chosen
		selectedSeries=new Vector();
		if(request.getParameter("serie")!=null){
			String[] cio=request.getParameterValues("serie");
			//Convert array in vector
			for(int i=0;i<cio.length;i++){
				selectedSeries.add(cio[i]);
			}
		}
		else{
			//if(!sbiMode.equalsIgnoreCase("WEB") || docComposition)
				selectedSeries.add("allseries");
		}


		// if selectedSerie contains allseries 
		if(selectedSeries.contains("allseries")){
			((BarCharts)sbi).setCurrentSeries(null);
		}
		else{	
			copyDataset=sbi.filterDatasetSeries(copyDataset,selectedSeries);	

		}
		// consider if drawing the slider
		if(sbi.isFilterCategories()==true && (catsnum.intValue())>numberCatVisualization.intValue()){
			makeSlider=true;	    	
		}

		if(copyDataset==null){copyDataset=dataset;}

		DatasetMap newDatasetMap=this.copyDatasetMap(copyDataset);



		return newDatasetMap;

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

	public Vector getSelectedCatGroups() {
		return selectedCatGroups;
	}

	public void setSelectedCatGroups(Vector selectedCatGroups) {
		this.selectedCatGroups = selectedCatGroups;
	}

	/**
	 * @return the numberSerVisualization
	 */
	public Integer getNumberSerVisualization() {
		if (numberSerVisualization == null) numberSerVisualization = new Integer(0);
		return numberSerVisualization;
	}

	/**
	 * @param numberSerVisualization the numberSerVisualization to set
	 */
	public void setNumberSerVisualization(Integer numberSerVisualization) {
		this.numberSerVisualization = numberSerVisualization;
	}







}
