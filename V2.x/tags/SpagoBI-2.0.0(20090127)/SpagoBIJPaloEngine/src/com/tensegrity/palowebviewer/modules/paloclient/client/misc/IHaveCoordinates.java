package com.tensegrity.palowebviewer.modules.paloclient.client.misc;

import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;


public interface IHaveCoordinates
{

	public void addCoordinate(XDimension dimension, XElement point);

}
