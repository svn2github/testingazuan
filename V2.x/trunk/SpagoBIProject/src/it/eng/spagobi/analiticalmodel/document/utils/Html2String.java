package it.eng.spagobi.analiticalmodel.document.utils;

import it.eng.spagobi.analiticalmodel.document.x.PrintNotesAction;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.log4j.Logger;


public class Html2String extends HTMLEditorKit.ParserCallback {
	StringBuffer s;
	String toConvert=null;

	private static Logger logger = Logger.getLogger(Html2String.class);

	public Html2String(String toConvert) {
		super();
		this.toConvert = toConvert;
	}

	public void parse() throws IOException {
		logger.debug("IN");
		StringReader stringReader=new StringReader(toConvert);
		s = new StringBuffer();
		ParserDelegator delegator = new ParserDelegator();
		// the third parameter is TRUE to ignore charset directive
		delegator.parse(stringReader, this, Boolean.TRUE);
		stringReader.close(); 
		logger.debug("OUT");
	}

	public void handleText(char[] text, int pos) {
		s.append(text);
	}

	public String getText() {
		return s.toString();
	}

	public static synchronized String convertHtml2String(String toConvert){
		logger.debug("IN");
		try{
			Html2String parser=new Html2String(toConvert);
			parser.parse();
			toConvert=parser.getText();
		}
		catch (Exception e) {
			logger.error("parsing failed",e);
			return toConvert;
			}
		logger.debug("OUT");
		return toConvert;
	}

//	public static void main (String[] args) {
//	try {
//	// the HTML to convert
//	FileReader in = new FileReader("java-new.html");
//	Html2String parser = new Html2String();
//	parser.parse(in);
//	in.close();
//	System.out.println(parser.getText());
//	}
//	catch (Exception e) {
//	e.printStackTrace();
//	}
//	}
}


