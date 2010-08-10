package com.tensegrity.palowebviewer.modules.engine.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class contains various properties related to client (web browser).
 * 
 * @author dmol
 * 
 */

public interface IClientProperties extends IsSerializable {

	public boolean reloadOnLogin();

	public void setReloadOnLogin(boolean value);

	public boolean showDatabaseDimensions();

	public void setShowDatabaseDimensions(boolean value);

	public boolean showCubeDimensions();

	public void setShowCubeDimensions(boolean value);

	public char getFloatSeparator();

	public String getDesimalSeparator();
	
	public int getFractionNumber();

	public void setFloatSeparator(char value);

	public void setDecimalSeparator(String value);
	
	public void setFractionNumber(int value);
	
	public int getPOVShowLevels();
	
	public void setPOVShowLevels(int value);
	
	public int getTDShowLevels();
	
	public void setTDShowLevels(int value);
	
	public void setPOVLoadSelectedPath(boolean value);
	
	public boolean hasPOVLoadSelectedPath();
	
	public void setColumnMinVisibleString(String value);
	
	public String getColumnMinVisibleString();
	
	public void setColumnMaxVisibleString(String value);
	
	public String getColumnMaxVisibleString();
	
	public int getHintTime();

	public void setHintTime(int value);
	
	public boolean getNotificationMissingExpandedElement();

	public void setNotificationMissingExpandedElement(boolean value);

	public boolean isShowDbExplorer();

	public boolean isShowFavoriteViews();

	public void setShowDbExplorer(boolean value);

	public void setShowFavoriteViews(boolean value);

	public void setNavigationPanelWidth(int width);

	public void setShowNavigationPanel(boolean show);
	
	public int getNavigationPanelWidth();

	public boolean isShowNavigationPanel();


}
