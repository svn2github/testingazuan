package it.eng.spagobi.jpivotaddins.cache;

import it.eng.spagobi.jpivotaddins.bean.AnalysisBean;
import it.eng.spagobi.jpivotaddins.bean.adapter.AnalysisAdapterUtil;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import mondrian.olap.CacheControl;
import mondrian.olap.Connection;
import mondrian.olap.Cube;

import com.tonbeller.jpivot.chart.ChartComponent;
import com.tonbeller.jpivot.mondrian.ScriptableMondrianDrillThrough;
import com.tonbeller.jpivot.olap.model.OlapModel;
import com.tonbeller.jpivot.table.TableComponent;

public class FlushCacheServlet extends HttpServlet {
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		
		Logger logger = Logger.getLogger(this.getClass());
		logger.debug("Entering service method");
		
		HttpSession session = request.getSession();
		OlapModel olapModel = (OlapModel) session.getAttribute("query01");
		ChartComponent chart = (ChartComponent) session.getAttribute("chart01");
		TableComponent table = (TableComponent) session.getAttribute("table01");
		AnalysisBean analysis = (AnalysisBean) session.getAttribute("analysisBean");
		analysis = AnalysisAdapterUtil.createAnalysisBean(analysis.getConnectionName(), analysis.getCatalogUri(),
			chart, table, olapModel);
		session.setAttribute("analysisBean", analysis);
		
		ScriptableMondrianDrillThrough smdt = (ScriptableMondrianDrillThrough) olapModel.getExtension("drillThrough");
		Connection mondrianConnection = smdt.getConnection();
		CacheControl cacheControl = mondrianConnection.getCacheControl(null);
		
		Cube[] cubes = mondrianConnection.getSchema().getCubes();
		for (int i = 0; i < cubes.length; i++) {
			Cube aCube = cubes[i];
			CacheControl.CellRegion measuresRegion = cacheControl.createMeasuresRegion(aCube);
			cacheControl.flush(measuresRegion);
		}
		
		
		try {
			response.sendRedirect(request.getContextPath() + "/jpivotOlap.jsp?query=refresh");
		} catch (IOException e) {
			logger.error("Error while redirecting response", e);
		}
		
		logger.debug("Exiting service method");
		
	}
	
}
