package com.tensegrity.palowebviewer.server.paloaccessor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Element;

import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.IXConsts;
import com.tensegrity.palowebviewer.modules.paloclient.client.XCube;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;

public final class XResultFetcher implements IXConsts {

	private static final Logger log = Logger.getLogger(XResultFetcher.class);

	private final XQueryPath query;

	private final Connection connection;

	private Element[][] elementMatrix;

	private final List dimensionPaths;

	private final XCube xCube;

	private int[] order;

	private double[] doubleData;

	private String[] stringData;

	private int[] nullData;

	private final List nullArray = new ArrayList();

	private final PaloAccessor accessor;

	public XResultFetcher(PaloAccessor accessor, XQueryPath query,
			Connection connection) throws InvalidObjectPathException {
		this.accessor = accessor;
		this.query = query;
		this.connection = connection;
		dimensionPaths = query.getDimensions();
		XPath cubePath = query.getCubePath();
		xCube = (XCube) accessor.getLastObject(cubePath, connection);
		xCube.setDimensions((XDimension[]) accessor.loadChildren(cubePath,
				TYPE_DIMENSION, connection));
	}

	public XResult fetch() throws InvalidObjectPathException {
		checkConditions();
		constructElementMatrix();
		if (log.isDebugEnabled()) {
			logQuery();
		}
		Cube cube = (Cube) xCube.getNativeObject();
		Object[] data = cube.getDataArray(elementMatrix);
		// Can't serialize Object[]. Wrapping to double[]
		convertData(data);
		XResult result = new XResult(doubleData, stringData, nullData, order);
		return result;
	}

	private void logQuery() {
		String msg = "Query matrix : [ \n";
		int size = 1;
		for (int i = 0; i < elementMatrix.length; i++) {
			int rowSize = elementMatrix[i].length;
			size *= rowSize;
			msg += " " + elementMatrix[i][0].getDimension().getName() + " = ";
			for (int j = 0; j < rowSize; j++) {
				msg += elementMatrix[i][j].getName();
				if (j < rowSize) {
					msg += ",";
				}
			}
			if (i < elementMatrix.length) {
				msg += "\n";
			}
		}
		msg += " ] : " + size + " cell requested";
		log.debug(msg);
	}

	protected void checkConditions() {
		if (dimensionPaths.size() < xCube.getDimensions().length) {// TODO:
																	// what if
																	// greater?
			String message = "query dimension count (" + dimensionPaths.size();
			message += ") not equals cube dimension count (";
			message += xCube.getDimensions().length + ")";
			throw new RuntimeException(message);
		}
	}

	private void convertData(Object[] data) {
		doubleData = new double[data.length];
		stringData = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			Object object = data[i];
			setData(object, i);
		}
		// do not transmit empty array
		if (isEmpty(stringData))
			stringData = null;
		nullData = toIntArray(nullArray);
	}

	private void setData(Object data, int i) {
		if (data instanceof String) {
			String value = (String) data;
			if ("".equals(value)) {
				nullArray.add(new Integer(i));
			} else {
				stringData[i] = value;
			}
		} else {
			Number value = (Number) data;
			doubleData[i] = value.doubleValue();
		}
	}

	private int[] toIntArray(List array) {
		int[] result = new int[array.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((Number) nullArray.get(i)).intValue();
		}
		return result;
	}

	private boolean isEmpty(String[] array) {
		boolean result = true;
		for (int i = 0; i < array.length && result; i++) {
			result = array[i] == null;
		}
		return result;
	}

	private void constructElementMatrix() throws InvalidObjectPathException {
		elementMatrix = new Element[dimensionPaths.size()][];
		order = PaloHelper.getDimensionOrder(xCube, dimensionPaths);
		for (int i = 0; i < dimensionPaths.size(); ++i) {
			XPath dimensionPath = (XPath) dimensionPaths.get(i);
			List elementsPath = query.getPoinstPath(dimensionPath);
			Element[] elementRow = new Element[elementsPath.size()];
			for (int j = 0; j < elementsPath.size(); j++) {
				XPath elementPath = (XPath) elementsPath.get(j);
				XElement node = (XElement) accessor.getLastObject(elementPath,
						connection);
				Element element = (Element) node.getNativeObject();
				elementRow[j] = element;
			}
			int idx = order[i];
			elementMatrix[idx] = elementRow;
		}
	}

}