package it.eng.spagobi.security.util.csvreader;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CSVRow {
	List row=null;
	
	public CSVRow(){
		row=new ArrayList();
	}
	public CSVRow(String p){
		row=tokenize(p);
	}	
	public String getElement(int i){
		return (String)row.get(i);
	}
	
	private List tokenize(String row){
		List result=new ArrayList();
		StringTokenizer tokenizer=new StringTokenizer(row,ICSVReader.SEPARATOR);
		while(tokenizer.hasMoreElements()){
			String token=tokenizer.nextToken();
			result.add(token);
		}
		return result;
	}

}
