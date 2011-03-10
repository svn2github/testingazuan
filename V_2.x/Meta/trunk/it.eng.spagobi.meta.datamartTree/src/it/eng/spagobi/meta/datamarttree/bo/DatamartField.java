package it.eng.spagobi.meta.datamarttree.bo;

import java.util.ArrayList;

public class DatamartField {
	   private ArrayList children;
	   private String name;
	   private DatamartField parent;
	   
	   public DatamartField(String name) {
		      this.name = name;
		      this.children = new ArrayList();
		      new DatamartField(parent, name);
		   }

	   public DatamartField(DatamartField parent, String name) {
	      this.name = name;
	      this.children = new ArrayList();
	      this.parent = parent;
	      if (parent != null)
	         parent.addChild(this);
	   }
	   public void addChild(DatamartField child) {
	      if (child == null)
	         throw new NullPointerException();
	      children.add(child);
	   }
	   private void doFlatten(DatamartField datamartField, ArrayList allDatamartFields) {
	      //add the datamartField and its children to the list
	      allDatamartFields.add(datamartField);
	      DatamartField[] children = datamartField.getChildren();
	      for (int i = 0; i < children.length; i++) {
	         doFlatten(children[i], allDatamartFields);
	      }
	   }
	   public boolean equals(Object object) {
	      if (!(object instanceof DatamartField))
	         return false;
	      if (this == object)
	         return true;
	      DatamartField datamartField = ((DatamartField)object);
	      return name.equals(datamartField.name) && children.equals(datamartField.children);
	   }
	   /**
	    * Returns a flat list of all datamartFields in this datamartField tree.
	    */
	   public DatamartField[] flatten() {
	      ArrayList result = new ArrayList();
	      doFlatten(this, result);
	      return (DatamartField[])result.toArray(new DatamartField[result.size()]);
	   }
	   public DatamartField[] getChildren() {
	      return (DatamartField[])children.toArray(new DatamartField[children.size()]);
	   }
	   public String getName() {
	      return name;
	   }
	   public DatamartField getParent() {
	      return parent;
	   }
	   public int hashCode() {
	      return name.hashCode() + children.hashCode();
	   }
	   /**
	    * Returns true if this datamartField has a parent equal to
	    * the given datamartField, and false otherwise.
	    */
	   public boolean hasParent(DatamartField datamartField) {
	      if (parent == null)
	         return false;
	      return parent.equals(datamartField) || parent.hasParent(datamartField);
	   }
	   private void removeChild(DatamartField datamartField) {
	      children.remove(datamartField);
	   }
	   public void setParent(DatamartField newParent) {
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