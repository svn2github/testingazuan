package it.eng.spagobi.plugins.ireport;


import java.util.Properties;

public class MyNode {
		
		private Properties attributes;
		private String tag;
		
		public MyNode(String tag, Properties attributes){
			
			this.tag=tag;
			this.attributes=attributes;
			
		}
		
		public String toString() {
			String name = (String)attributes.get("name");
			if (name==null) name = "";
			return name;
					
		}

		public Properties getAttributes() {
			return attributes;
		}

		public String getTag() {
			return tag;
		}
		
		public String getName() {
			String name = (String)attributes.get("name");
			if (name==null)
				return "no name";
			else 
				return name;
			
		}
		
		public void addAttribute(String name, String value){
			
			attributes.put(name,value);
			
		}
		
		
	}


