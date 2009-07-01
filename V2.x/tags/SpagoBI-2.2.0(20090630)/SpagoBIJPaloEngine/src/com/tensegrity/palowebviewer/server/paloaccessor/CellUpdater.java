/**
 * 
 */
package com.tensegrity.palowebviewer.server.paloaccessor;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Element;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.ResultDouble;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.ResultString;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public final class CellUpdater implements IXConsts{ 

    private Connection connection;
    private XPath cubePath;
    private IXPoint point;
    private IResultElement value;
    private List dimensionPaths;
    private XCube xCube; 
    private Cube cube;
    private Element[] elements;
    private boolean consolidatedCell;
    private final PaloAccessor accessor;
	private boolean cellTypeString;
	private Locale locale;

    public CellUpdater(PaloAccessor accessor) {
    	this.accessor = accessor;
    }

    public void setConection (Connection value) {
        this.connection = value;
    }

    public void setCubePath(XPath path){
        cubePath = path;
    }

    public void setPoint(IXPoint point) {
        this.point = point;
    }

    public void setResultElement(IResultElement value) {
        this.value = value;
    }

    protected void initFields() throws InvalidObjectPathException {
        dimensionPaths = point.getDimensions();
        xCube = (XCube) accessor.getLastObject(cubePath, connection);
        xCube.setDimensions((XDimension[])accessor.loadChildren(cubePath, TYPE_DIMENSION, connection));
        cube = (Cube)xCube.getNativeObject();
    }

    public void update() throws InvalidObjectPathException {
        initFields();
        checkConditions();

        constructElementArray();
        Object dbValue = getDbValue();
        writeValue(dbValue);
    }

    private void writeValue(Object dbValue) {
    	NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        if (consolidatedCell && !cellTypeString){
            cube.setDataSplashed(elements, dbValue, formatter);
        }else{
            cube.setData(elements, dbValue, formatter);
        }
    }

    protected Object getDbValue() {
        Object dbValue;
        if (value instanceof ResultDouble) {
            ResultDouble rd = (ResultDouble) value;
            dbValue = new Double(rd.getDoubleValue());
        }
        else{
            ResultString rs = (ResultString) value;
            dbValue = rs.getValue();
        }
        return dbValue;
    }

    private void constructElementArray() throws InvalidObjectPathException {
        int[] order = PaloHelper.getDimensionOrder(xCube, dimensionPaths);
        elements = new Element[dimensionPaths.size()];            
        consolidatedCell = false;
        cellTypeString = false;
        for (int i = 0; i < dimensionPaths.size(); ++i) {
            XPath dimensionPath = (XPath)dimensionPaths.get(i);
            XPath elementPath = point.getElementPath(dimensionPath);
            XElement xElement = (XElement) accessor.getLastObject(elementPath, connection);
            Element  element = (Element) xElement.getNativeObject();
            cellTypeString |=  element.getType() == Element.ELEMENTTYPE_STRING;
            consolidatedCell |= (element.getConsolidationCount() > 0);
            int idx = order[i];
            elements[idx] = element;
        }
    }

    private void checkConditions() {
        if(dimensionPaths.size() < cube.getDimensionCount()) {
            String message = "dimension count less than cube dimension count "; 
            message += "(" + dimensionPaths.size() + " < ";
            message += cube.getDimensionCount() + ")";
            throw new RuntimeException(message);

        }
    }

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}