package it.eng.spagobi.studio.dashboard.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

public class LogListenerFile implements ILogListener {

	FileOutputStream  file=null;



	public LogListenerFile() {
		super();
		try {
			file=new FileOutputStream("C:/zzzprovaLogStudio");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void logging(IStatus status, String plugin) {

		PrintStream Output = new PrintStream(file);

		Output.println(plugin+" : "+status.getMessage());


	}

}
