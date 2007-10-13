package it.eng.spagobi.security.util.csvreader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements ICSVReader {

	public List readElement(String filename) {
		InputStream is = this.getClass().getResourceAsStream("/"+filename);
		List risultato=new ArrayList();
		try {
			
			InputStreamReader fileReader=new InputStreamReader(is);
			LineNumberReader lineReader=new LineNumberReader(fileReader);
			boolean fineFile=false;
			while (!fineFile){
				String riga=lineReader.readLine();
				if (riga==null) fineFile=true;
				else risultato.add(new CSVRow(riga));
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return risultato;
	}

}
