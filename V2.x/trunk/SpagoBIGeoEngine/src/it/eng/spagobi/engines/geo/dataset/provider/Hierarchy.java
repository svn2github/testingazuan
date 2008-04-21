/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.engines.geo.dataset.provider;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class Hierarchy {
	private String name;
	private String type;
	private String table;
	private List levelList;
	private Map levelMap;
	
	public Hierarchy(String name) {
		this.name = name;
		this.type = "custom";
		this.table = null;
		this.levelList = new ArrayList();
		this.levelMap = new HashMap();
	}
	
	public Hierarchy(String name , String table) {
		this.name = name;
		this.type = "defualt";
		this.table = table;
		this.levelList = new ArrayList();
		this.levelMap = new HashMap();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void addLevel(Level level) {
		levelList.add(level);
		levelMap.put(level.getName(), level);
	}
	
	public Level getLevel(String levelName) {
		return (Level)levelMap.get(levelName);
	}
	
	public List getLevels() {
		return levelList;
	}
	
	public List getSublevels(String levelName) {
		List levels = new ArrayList();
		boolean isSubLevel = false;
		for(int i = 0; i < levelList.size(); i++) {
			Level level = (Level)levelList.get(i);
			if(isSubLevel) {
				levels.add(level);
			} else {
				if(level.getName().equalsIgnoreCase(levelName)) isSubLevel = true;
			}
		}
		
		return levels;
	}
	
	
	public static class Level {
		private String name;
		private String columnId;
		private String columnDesc;
		private String featureName;
		private Link link;
		
		public Level() {
			link = new Link();
		}
		
		public String getColumnDesc() {
			return columnDesc;
		}
		public void setColumnDesc(String columnDesc) {
			this.columnDesc = columnDesc;
		}
		public String getColumnId() {
			return columnId;
		}
		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}
		public String getFeatureName() {
			return featureName;
		}
		public void setFeatureName(String featureName) {
			this.featureName = featureName;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Link getLink() {
			return link;
		}
		public void setLink(Link link) {
			this.link = link;
		}
	}


	public Set getLevelNames() {
		return levelMap.keySet();
	}
}
