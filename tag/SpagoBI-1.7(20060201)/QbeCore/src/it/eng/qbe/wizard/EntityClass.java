package it.eng.qbe.wizard;

import java.io.Serializable;


public class EntityClass implements Serializable{

	private String className = null;
	
	private String classAlias= null;

	public EntityClass(){
		
	}
	public EntityClass( String name,String alias) {
		super();
		// TODO Auto-generated constructor stub
		classAlias = alias;
		className = name;
	}

	public String getClassAlias() {
		return classAlias;
	}

	public void setClassAlias(String classAlias) {
		this.classAlias = classAlias;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	
}
