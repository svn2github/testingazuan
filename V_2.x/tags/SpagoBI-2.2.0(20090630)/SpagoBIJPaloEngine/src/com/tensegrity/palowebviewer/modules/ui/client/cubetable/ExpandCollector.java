/**
 * 
 */
package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.ui.client.cubetable.CubeHeaderModel.HeaderTreeNode;

class ExpandCollector {

    private final Map[] expanInfoLists;
    private final XElement expanded[][];
    private final int[][][] repetitions;
    private final CubeHeaderModel headerModel;

    public XElement[][] getExpanded() {
        return expanded;
    }

    public int[][][] getRepetitions() {
        return repetitions;
    }

    private static class ExpandInfo {

        public final XElement element;
        public final List repetitions = new ArrayList();
        public int occurrence;

        public ExpandInfo(XElement element) {

            this.element = element;
            occurrence = 0;
        }

    }

    public ExpandCollector.ExpandInfo getExpandInfo(int i, XElement element) {
        ExpandCollector.ExpandInfo result = (ExpandCollector.ExpandInfo)expanInfoLists[i].get(element);
        if(result == null) {
            result = new ExpandInfo(element);
            expanInfoLists[i].put(element, result);
        }
        return result;
    }

    public ExpandCollector(CubeHeaderModel model) {
    	this.headerModel = model;
        expanInfoLists = new Map[headerModel.getLayerCount()];
        expanded = new XElement[expanInfoLists.length][];
        repetitions = new int[expanInfoLists.length][][];
        for( int i = 0 ; i < expanInfoLists.length ; i++ ) { 
            expanInfoLists[i] = new HashMap();
        } 
    }

    public void collect() {
        for( Iterator it = headerModel.getExpandedNodes().iterator () ; it.hasNext (); ) { 
            HeaderTreeNode node = (HeaderTreeNode)it.next();
            collectNode(node);
        } 
        prepare();
    }

    protected void collectNode(HeaderTreeNode node) {
        int i = node.getLayerNr();
        XElement element = node.getElement();
        ExpandCollector.ExpandInfo info = getExpandInfo(i, element);
        info.repetitions.add(new Integer(node.getOccurence()));
    }

    private void prepare () {
        for( int i = 0 ; i < expanInfoLists.length ; i++ ) { 
            List list = getExpandedInfoList(i); 
            int j = 0;
            expanded[i] = new XElement[list.size()];
            repetitions[i] = new int[list.size()][];
            for( Iterator it = list.iterator () ; it.hasNext (); ) { 
                ExpandCollector.ExpandInfo info = (ExpandCollector.ExpandInfo)it.next();
                prepareElementRepetitions(i, j, info);
                j++;
            } 

        } 
    }

	private void prepareElementRepetitions(int i, int j, ExpandCollector.ExpandInfo info) {
		expanded[i][j] = info.element;
		int size = info.repetitions.size();
		repetitions[i][j] = new int[size];
		for( int k = 0 ; k < size  ; k++ ) { 
		    Number number = (Number)info.repetitions.get(k);
		    repetitions[i][j][k] = number.intValue();
		} 
	}

	private List getExpandedInfoList(int i) {
        Map infoMap = expanInfoLists[i];
		List result = new ArrayList();
		for( Iterator it = infoMap.keySet().iterator () ; it.hasNext (); ) { 
		    XElement key = (XElement)it.next();
		    ExpandCollector.ExpandInfo info = getExpandInfo(i, key);
		    if(!info.repetitions.isEmpty()) {
		        result.add(info);
		    }
		}
		return result;
	}


}