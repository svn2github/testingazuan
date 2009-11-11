package it.eng.spagobi.studio.geo.editors.model.bo;

import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.KPI;
import it.eng.spagobi.studio.geo.editors.model.geo.MapRenderer;
import it.eng.spagobi.studio.geo.editors.model.geo.Measures;

import java.util.Vector;

public class KpiBO {
	
	public static void setNewMeasure(GEODocument geoDocument, KPI kpiToAdd){
		MapRenderer mapRenderer = geoDocument.getMapRenderer();
		Measures measures = mapRenderer.getMeasures();
		if(measures == null){
			measures = new Measures();
			mapRenderer.setMeasures(measures);
		}
		Vector<KPI> kpis= measures.getKpi();
		if(kpis == null){
			kpis = new Vector<KPI>();
			measures.setKpi(kpis);
		}
		boolean isKpiModified = false;
		for(int i=0; i<kpis.size(); i++){
			KPI kpi = kpis.elementAt(i);
			String columnId = kpi.getColumnId();
			if(kpiToAdd.getColumnId().equals(columnId)){
				//va in modifica
				kpi = kpiToAdd;
				isKpiModified = true;
			}
		}
		if(!isKpiModified){
			//aggiunge kpi
			kpis.add(kpiToAdd);
		}
	}
	public static void deleteMeasure(GEODocument geoDocument, KPI kpiToDelete){
		MapRenderer mapRenderer = geoDocument.getMapRenderer();
		Measures measures = mapRenderer.getMeasures();
		if(measures != null){
			Vector<KPI> kpis= measures.getKpi();
			if(kpis != null){
				int kpisSize =kpis.size();
				for(int i=0; i< kpisSize; i++){
					KPI kpi = kpis.elementAt(i);
					String columnId = kpi.getColumnId();
					if(kpiToDelete.getColumnId().equals(columnId)){
						//delete
						kpis.remove(i);
					}
				}

			}
		}
	}
	
	public static KPI getMeasureByColumnId(GEODocument geoDocument, String columnIdToSearch){
		KPI kpi = null;
		MapRenderer mapRenderer = geoDocument.getMapRenderer();
		Measures measures = mapRenderer.getMeasures();
		if(measures != null){
			Vector<KPI> kpis= measures.getKpi();
			if(kpis != null){
				int kpisSize =kpis.size();
				for(int i=0; i< kpisSize; i++){
					kpi = kpis.elementAt(i);
					String columnId = kpi.getColumnId();
					if(columnIdToSearch.equals(columnId)){
						return kpi;
					}
				}

			}
		}
		return kpi;
	}
}
