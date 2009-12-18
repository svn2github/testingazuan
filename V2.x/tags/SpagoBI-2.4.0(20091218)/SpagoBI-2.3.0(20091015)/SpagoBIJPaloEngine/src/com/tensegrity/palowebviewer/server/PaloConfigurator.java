package com.tensegrity.palowebviewer.server;

import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.palowebviewer.server.exeptions.ConfigurationException;

public class PaloConfigurator {
	
	private static final IPropertyConfigurator[] configurators = new IPropertyConfigurator[]{
		new RightManagerConfigurator(),
		new ServerConfigurator(),
		new PoolMaxConnectionsConfigurator(),
		new ShowDbExplorerConfigurator(),
		new ShowFavoriteViewsConfigurator(),
		new ShowCubeDimensionsConfigurator(),
		new ShowDatabaseDimensionsConfigurator(),
		new ShowNavigationPanelConfigurator(),
		new NavigationPanelWidthConfigurator(),
		new ReloadOnLogincConfigurator(),
		new FractionNumberConfigurator(),
		new POVShowLevelsConfigurator(),
		new POVLoadSelectedPathConfigurator(),
		new TDShowLevelsConfigurator(),
		new UserConfigurator(),
		new PasswordConfigurator(),
		new ColumnMinVisibleStringConfigurator(),
		new ColumnMaxVisibleStringConfigurator(),
		new HintTimeConfigurator(),
		new NotificationMissingExpandedElementConfigurator(),
	};
	
	public static PaloConfiguration getConfiguration(Properties props) throws ConfigurationException{
		PaloConfiguration config = new PaloConfiguration();
		Enumeration en = props.propertyNames();
		while(en.hasMoreElements()){
			String name = (String)en.nextElement();
			String value = props.getProperty(name);
			boolean configuratorFound = false;
			for (int i = 0; !configuratorFound && i < configurators.length; i++) {
				Matcher m = configurators[i].getPattern().matcher(name);
				configuratorFound = m.matches();
				if(configuratorFound){
					configurators[i].parse(config, m, value);
				}
			}
			if(!configuratorFound){
				Logger.warn("PaloConfigurator: unknown option '"+name+"'");
			}
		}
		return config;
	}
	
	public static interface IPropertyConfigurator {
		
		public Pattern getPattern();
		
		public void parse(PaloConfiguration config, Matcher m, String value) throws ConfigurationException ;
		
	}
	
	public static abstract class BasePropertyConfigurator implements IPropertyConfigurator{
		private final Pattern pattern;
		
		public Pattern getPattern() {
			return pattern;
		}
		
		public BasePropertyConfigurator(String key){
			this.pattern = Pattern.compile("\\Q"+key+"\\E");
		}
		
		protected abstract void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException;
		
		public void parse(PaloConfiguration config, Matcher m, String value) throws ConfigurationException {
			onKeyMatch(value, config);
		}
		
	}
	public static class ServerConfigurator implements IPropertyConfigurator {
		
		private static final Pattern PATTERN = Pattern.compile("server(\\d+)\\.(\\w+)");
		
		public Pattern getPattern() {
			return PATTERN;
		}

		public void parse(PaloConfiguration config, Matcher m, String value) throws ConfigurationException {
			int  serverOrder = Integer.parseInt(m.group(1));
			String property = m.group(2);
			PaloConfiguration.PaloServer server = getServer(config, serverOrder);
			if(property.equals("login")){
			    server.setLogin(value);
			}
			else if(property.equals("password")){
			    server.setPassword(value);
			}
			else if(property.equals("provider")){
			    server.setProvider(value);
			}
			else if(property.equals("url")){
			    server.setHost(value);
			}
			else if(property.equals("service")){
			    server.setService(value);
			}
			else if(property.equals("dispname")){
			    server.setDispName(value);
			}
			else {
				String message = "Unknown option '"+property+"' for server"+serverOrder;
				throw new ConfigurationException(message);
			}
		}
		
		private PaloConfiguration.PaloServer getServer(PaloConfiguration config, int order) {
			PaloConfiguration.PaloServer server = config.getServer(order);
			if(server == null) {
			    server = new PaloConfiguration.PaloServer();
			    server.setOrder(order);
			    config.addServer(server);
			}
			return server;
		}
		
	}
	
	
	public static class RightManagerConfigurator extends BasePropertyConfigurator {
		
		public RightManagerConfigurator(){
			super("right.manager.class");
		}
		
		protected void onKeyMatch(String value, PaloConfiguration config)  throws ConfigurationException{
			try{
	        	IRightManager manager = (IRightManager) Class.forName(value).newInstance();
	        	config.setRightManager(manager);
			}catch(Exception e){
				throw new ConfigurationException(e);
			}
		}

	}
	
	public static class PoolMaxConnectionsConfigurator extends BasePropertyConfigurator {
		
		public PoolMaxConnectionsConfigurator() {
			super("connection.pool.max");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
            int max = Integer.parseInt(value);
            config.setPoolMaxConnections(max);
		}
		
	}
	
	public static class ShowCubeDimensionsConfigurator extends BasePropertyConfigurator {
		
		public ShowCubeDimensionsConfigurator() {
			super("client.ui.show.cube.dimensions");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			boolean show = Boolean.valueOf(value).booleanValue();
            config.getClientProperties().setShowCubeDimensions(show);
		}
		
	}

	public static class ShowDbExplorerConfigurator extends BasePropertyConfigurator {

		public ShowDbExplorerConfigurator() {
			super("client.ui.show.dbexplorer");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			boolean show = Boolean.valueOf(value).booleanValue();
			config.getClientProperties().setShowDbExplorer(show);
		}

	}
	
	public static class ShowFavoriteViewsConfigurator extends BasePropertyConfigurator {

		public ShowFavoriteViewsConfigurator() {
			super("client.ui.show.favoriteviews");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			boolean show = Boolean.valueOf(value).booleanValue();
			config.getClientProperties().setShowFavoriteViews(show);
		}

	}

	public static class NavigationPanelWidthConfigurator extends BasePropertyConfigurator {

		public NavigationPanelWidthConfigurator() {
			super("client.ui.navigationpanel.width");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			int width = Integer.valueOf(value).intValue();
			config.getClientProperties().setNavigationPanelWidth(width);
		}

	}
	
	public static class ShowNavigationPanelConfigurator extends BasePropertyConfigurator {

		public ShowNavigationPanelConfigurator() {
			super("client.ui.show.navigationpanel");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			boolean show = Boolean.valueOf(value).booleanValue();
			config.getClientProperties().setShowNavigationPanel(show);
		}

	}


	public static class ShowDatabaseDimensionsConfigurator extends BasePropertyConfigurator{
		
		public ShowDatabaseDimensionsConfigurator() {
			super("client.ui.show.database.dimensions");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
            boolean show = Boolean.valueOf(value).booleanValue();
            config.getClientProperties().setShowDatabaseDimensions(show);
		}
		
	}
	
	public static class ReloadOnLogincConfigurator extends BasePropertyConfigurator{
		
		public ReloadOnLogincConfigurator() {
			super("client.behavior.database.reload-on-login");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
            boolean bValue = Boolean.valueOf(value).booleanValue();
            config.getClientProperties().setReloadOnLogin(bValue);
		}
		
	}
	
	public static class FractionNumberConfigurator extends BasePropertyConfigurator {
		
		public FractionNumberConfigurator() {
			super("client.ui.format.fraction.number_of_digits");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			int number = Integer.valueOf(value).intValue();
			config.getClientProperties().setFractionNumber(number);
		}
		
	}
	
	public static class POVShowLevelsConfigurator extends BasePropertyConfigurator {
		
		public POVShowLevelsConfigurator() {
			super("client.ui.views.pov.showlevels");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			int number = Integer.valueOf(value).intValue();
			config.getClientProperties().setPOVShowLevels(number);
		}
		
	}
	
	public static class POVLoadSelectedPathConfigurator extends BasePropertyConfigurator {
		
		public POVLoadSelectedPathConfigurator() {
			super("client.ui.views.pov.loadselectedpath");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			boolean bValue = Boolean.valueOf(value).booleanValue();
            config.getClientProperties().setPOVLoadSelectedPath(bValue);
        }
		
	}

	
	public static class TDShowLevelsConfigurator extends BasePropertyConfigurator {
		
		public TDShowLevelsConfigurator() {
			super("client.ui.views.td.showlevels");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			int number = Integer.valueOf(value).intValue();
			config.getClientProperties().setTDShowLevels(number);
		}
		
	}
	
	public static class UserConfigurator extends BasePropertyConfigurator {
		
		public UserConfigurator() {
			super("user");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			config.setUser(value);
		}
		
	}
	
	public static class PasswordConfigurator extends BasePropertyConfigurator {
		
		public PasswordConfigurator() {
			super("password");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			config.setPassword(value);
		}
		
	}
	
	public static class ColumnMinVisibleStringConfigurator extends BasePropertyConfigurator {
		
		public ColumnMinVisibleStringConfigurator() {
			super("client.ui.views.table.columns.min_visible_string");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			config.getClientProperties().setColumnMinVisibleString(value);
		}
		
	}
	
	public static class ColumnMaxVisibleStringConfigurator extends BasePropertyConfigurator {
		
		public ColumnMaxVisibleStringConfigurator() {
			super("client.ui.views.table.columns.max_visible_string");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			config.getClientProperties().setColumnMaxVisibleString(value);
		}
		
	}
	
	public static class HintTimeConfigurator extends BasePropertyConfigurator {
		
		public HintTimeConfigurator() {
			super("client.ui.views.table.hint_time");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			int number = Integer.valueOf(value).intValue();
			config.getClientProperties().setHintTime(number);
		}
		
	}
	
	public static class NotificationMissingExpandedElementConfigurator extends BasePropertyConfigurator {
		
		public NotificationMissingExpandedElementConfigurator() {
			super("client.ui.views.table.notification.missing_expanded_element");
		}

		protected void onKeyMatch(String value, PaloConfiguration config) throws ConfigurationException {
			boolean bValue = Boolean.valueOf(value).booleanValue();
			config.getClientProperties().setNotificationMissingExpandedElement(bValue);
		}
		
	}
	
	
}
