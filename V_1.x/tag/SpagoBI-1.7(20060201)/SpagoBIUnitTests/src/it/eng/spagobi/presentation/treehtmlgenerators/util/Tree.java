package it.eng.spagobi.presentation.treehtmlgenerators.util;

import java.util.ArrayList;
import java.util.List;

public class Tree{
	
	private List tree = new ArrayList(); 
	
	public void addRecord(IRecord record){
		tree.add(record);
	}
	
	public void updRecord(IRecord record) throws Exception{
		int index = -1;
		for  (int i = 0; i < tree.size(); i++){
			IRecord rec = (IRecord) tree.get(i);
			if (rec.getName().equals(record.getName())) {
				rec.updFields(record);
				index = i;
				break;
			}
		}
		if (index == -1) throw new Exception ("String not found: "+record.getName());
	}
	
	public IRecord get(int index){
		return (IRecord) tree.get(index);
	}
	
	public int size(){
		return tree.size();
	}
	
	public int indexOf(String name) throws Exception{
		int index = -1;
		for  (int i = 0; i < tree.size(); i++){
			IRecord rec = (IRecord) tree.get(i);
			if (rec.getName().equals(name)) {
				index = i;
				break;
			}
		}
		if (index == -1) throw new Exception ("String not found: "+name);
		return index;
	}
	
	public String toString(){
		String toReturn = ((IRecord) tree.get(0)).getHead();
		for (int i = 0; i < tree.size(); i++) {
			IRecord rec = (IRecord) tree.get(i);
			toReturn = toReturn + "\n" + rec.toString();
		}
		return toReturn;
	}
	
	public boolean equals(Object object){
		Tree aTree = (Tree) object;
		if (tree.size() != aTree.size()) return false;
		for (int i = 0; i < tree.size(); i++){
			IRecord rec1 = (IRecord) tree.get(i);
			IRecord rec2 = (IRecord) aTree.get(i);
			if (!rec1.equals(rec2)) return false;
		}
		return true;
	}
	
}
