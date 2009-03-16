/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.List;

import com.tensegrity.palowebviewer.modules.engine.client.IQueryCallback;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IMatrixIterator;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XFastMatrixIterator;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;

class DataQuery implements IQueryCallback {

	private final ICubeTableModel cubeTableModel;
	final int[] x;
    final int[] y;
    private final XQueryPath[] queries;

    public DataQuery(ICubeTableModel model, XQueryPath[] queries, int[] x, int[] y) {
        cubeTableModel = model;
		this.queries = queries;
        this.x = x;
        this.y = y;
    }

    public void onSuccess(XResult[] result) {
        Logger.debug("response for data query."); 
        if(result == null)
            throw new IllegalArgumentException("XResult can not be null.");

        if(cubeTableModel.isModelValid()){ //model can be disposed at data arrival moment.
            PerformanceTimer timer = new PerformanceTimer("DataQuery#set data");
            timer.start();
            for (int i = 0; i < result.length; i++) {
                showResult(queries[i], result[i], x[i], y[i]);
            }
            timer.report();
            cubeTableModel.updateFinished();
        }
        
    }

    public void sendQuery() {
        Logger.debug("send data query");
        
        cubeTableModel.getPaloServerModel().query(getQueries(), this);
    }

    protected XQueryPath[] getQueries() {
        return queries;
    }

    protected void updatePoint(int x, int y, IResultElement element) {
    	if(Logger.isOn()){
    		Logger.debug("("+x+";"+y+") = "+ element);
    	}
        cubeTableModel.setCellValue(x, y, element);
    }

    private void showResult(XQueryPath query, XResult result, int x, int y){
        List xDimList = cubeTableModel.getXDimensions().getDimensionsXPaths();
        List yDimList = cubeTableModel.getYDimensions().getDimensionsXPaths();

        IMatrixIterator it = new XFastMatrixIterator(query, result, xDimList, yDimList);
        for(; it.hasMorePoints(); ) { 
            it.next();
            int dx = it.getX();
            int dy = it.getY();
            IResultElement element = it.getValue();
            updatePoint(x+dx, y+dy, element);
        }
    }

}