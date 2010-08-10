package it.eng.spagobi.security.util.csvreader;

import java.util.List;

public interface ICSVReader {
	
	final static String SEPARATOR=";";

	/**
	 * Legge gli elementi di un file CSV e li ritorna come Lista di Mappe 
	 * @param filename
	 * @return
	 */
	List readElement(String filename);
}
