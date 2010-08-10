package com.tensegrity.palowebviewer.modules.engine.client;

/**
 * Class that is used to transfere propertyes from a server to client.
 *
 */
public class ClientProperties implements IClientProperties {

	private boolean showCubeDimentsion = false;

	private boolean showDatabaseDimentsion = false;

	private boolean reloadOnLogin = false;

	private char floatSeparator = '.';

	private String desimalSeparator = "";
	private int fractionNumber = 3;
	
	private int povShowLevels = 1;
	private int tdShowLevels = 1;
	private boolean povLoadSelectedPath = false;
	
	private String columnMinVisibleString = "9.999.999.999,99";
	private String columnMaxVisibleString = "9.999.999.999.999.999.999.999,99";
	private int hintTime = 1000;
	
	private boolean notificationMissingExpandedElement = false;

	private boolean showDbExplorer = true;

	private boolean showFavoriteViews = true;

	private int navigationPanelWidth = 200;

	private boolean showNavigationPanel = true;

	/**
	 * {@inheritDoc}
	 */
	public boolean showCubeDimensions() {
		return this.showCubeDimentsion;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowCubeDimensions(boolean value) {
		showCubeDimentsion = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowDatabaseDimensions(boolean value) {
		this.showDatabaseDimentsion = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean showDatabaseDimensions() {
		return showDatabaseDimentsion;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean reloadOnLogin() {
		return reloadOnLogin;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setReloadOnLogin(boolean value) {
		this.reloadOnLogin = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public char getFloatSeparator() {
		return floatSeparator;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFloatSeparator(char value) {
		floatSeparator = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDesimalSeparator() {
		return desimalSeparator;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDecimalSeparator(String desimalSeparator) {
		this.desimalSeparator = desimalSeparator;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getFractionNumber() {
		return fractionNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFractionNumber(int fractionNumber) {
		this.fractionNumber = fractionNumber;		
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPOVShowLevels() {
		return povShowLevels;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPOVShowLevels(int value) {
		povShowLevels = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasPOVLoadSelectedPath() {
		return povLoadSelectedPath;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPOVLoadSelectedPath(boolean value) {
		povLoadSelectedPath = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTDShowLevels() {
		return tdShowLevels;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTDShowLevels(int value) {
		this.tdShowLevels = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumnMaxVisibleString() {
		return columnMaxVisibleString;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setColumnMaxVisibleString(String columnMaxVisibleString) {
		this.columnMaxVisibleString = columnMaxVisibleString;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumnMinVisibleString() {
		return columnMinVisibleString;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setColumnMinVisibleString(String columnMinVisibleString) {
		this.columnMinVisibleString = columnMinVisibleString;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHintTime() {
		return hintTime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHintTime(int hintTime) {
		this.hintTime = hintTime;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean getNotificationMissingExpandedElement(){
		return notificationMissingExpandedElement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setNotificationMissingExpandedElement(boolean value){
		notificationMissingExpandedElement = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowDbExplorer() {
		return showDbExplorer;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowFavoriteViews() {
		return showFavoriteViews;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowDbExplorer(boolean value) {
		showDbExplorer = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowFavoriteViews(boolean value) {
		showFavoriteViews = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNavigationPanelWidth() {
		return navigationPanelWidth;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isShowNavigationPanel() {
		return showNavigationPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNavigationPanelWidth(int width) {
		navigationPanelWidth = width;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShowNavigationPanel(boolean show) {
		showNavigationPanel = show;
	}

}
