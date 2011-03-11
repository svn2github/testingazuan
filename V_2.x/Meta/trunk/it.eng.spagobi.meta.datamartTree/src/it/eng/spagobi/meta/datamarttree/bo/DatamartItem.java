package it.eng.spagobi.meta.datamarttree.bo;

import java.util.ArrayList;

public class DatamartItem {
	   private ArrayList children;
	   private String name;
	   private DatamartItem parent;
	   private String path;		
	   private String role;	
	   private String type;	
	   
	   public DatamartItem(String name) {
	      this.name = name;
	      this.children = new ArrayList();
	      new DatamartItem(parent, name);
	   }


	   public DatamartItem(String name, String path, String role, String type) {
		super();
		this.name = name;
		this.path = path;
		this.role = role;
		this.type = type;
	}



	public DatamartItem(DatamartItem parent, String name) {
	      this.name = name;
	      this.children = new ArrayList();
	      this.parent = parent;
	      if (parent != null)
	         parent.addChild(this);
	   }
	   public void addChild(DatamartItem child) {
	      if (child == null)
	         throw new NullPointerException();
	      children.add(child);
	   }
	   private void doFlatten(DatamartItem datamartField, ArrayList allDatamartFields) {
	      //add the datamartField and its children to the list
	      allDatamartFields.add(datamartField);
	      DatamartItem[] children = datamartField.getChildren();
	      for (int i = 0; i < children.length; i++) {
	         doFlatten(children[i], allDatamartFields);
	      }
	   }
	   public boolean equals(Object object) {
	      if (!(object instanceof DatamartItem))
	         return false;
	      if (this == object)
	         return true;
	      DatamartItem datamartField = ((DatamartItem)object);
	      return name.equals(datamartField.name) && children.equals(datamartField.children);
	   }
	   /**
	    * Returns a flat list of all datamartFields in this datamartField tree.
	    */
	   public DatamartItem[] flatten() {
	      ArrayList result = new ArrayList();
	      doFlatten(this, result);
	      return (DatamartItem[])result.toArray(new DatamartItem[result.size()]);
	   }
	   public DatamartItem[] getChildren() {
	      return (DatamartItem[])children.toArray(new DatamartItem[children.size()]);
	   }
	   public String getName() {
	      return name;
	   }
	   public DatamartItem getParent() {
	      return parent;
	   }
	   public int hashCode() {
	      return name.hashCode() + children.hashCode();
	   }
	   /**
	    * Returns true if this datamartField has a parent equal to
	    * the given datamartField, and false otherwise.
	    */
	   public boolean hasParent(DatamartItem datamartField) {
	      if (parent == null)
	         return false;
	      return parent.equals(datamartField) || parent.hasParent(datamartField);
	   }
	   private void removeChild(DatamartItem datamartField) {
	      children.remove(datamartField);
	   }
	   public void setParent(DatamartItem newParent) {
	      if (parent != null)
	         parent.removeChild(this);
	      this.parent = newParent;
	      if (newParent != null)
	         newParent.addChild(this);
	   }
	   public String toString() {
	      return name;
	   }
	}