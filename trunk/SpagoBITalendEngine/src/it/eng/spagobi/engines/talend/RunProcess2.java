package it.eng.spagobi.engines.talend;

import java.io.IOException;
import java.util.Properties;

public class RunProcess2 {

	    public String run(String project, String jobName, String context) {
	    	
	    	String libPath = "C:/Programmi/TOS-Win32-r1413-V1.1.1/plugins/org.talend.designer.codegen.perlmodule_1.1.1._r1413/perl";
	 	    String exeModulePath =  "C:/Programmi/TOS-Win32-r1413-V1.1.1/workspace/.Perl";
	 	    String perlInterpreter = "perl";
	 	    String perlLibPrefix = "-I";
	 	    String projectSeparator = ".process";
	 	    String wordSeparator = "_";
	 	    String perlExt = ".pl";
	    	
	    	Properties prop = new Properties();
	    	try {
	    		prop.load(getClass().getResourceAsStream("/talend2.properties"));
	    		libPath = prop.getProperty("libPath");
		 	    exeModulePath = prop.getProperty("exeModulePath");
		 	    perlInterpreter = prop.getProperty("perlInterpreter");
		 	    perlLibPrefix = prop.getProperty("perlLibPrefix");
		 	    projectSeparator = prop.getProperty("projectSeparator");
		 	    wordSeparator = prop.getProperty("wordSeparator");
		 	    perlExt = prop.getProperty("perlExt");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	
	        String perlLibOption = libPath != null && libPath.length() > 0 ? perlLibPrefix + libPath : "";
	        String contextLibOption = exeModulePath != null && exeModulePath.length() > 0 ? perlLibPrefix + exeModulePath
	                : "";
	        String perlCode = exeModulePath + project + projectSeparator + wordSeparator + jobName + perlExt;
	        String contextCode = "--context="+exeModulePath + project + projectSeparator + wordSeparator + jobName + wordSeparator
	                +context + perlExt;
	        
	        String[] cmd = new String[] { perlInterpreter, perlLibOption, contextLibOption, perlCode, contextCode };
	        try {
	            sysOutCommandLine(cmd);
	            Runtime.getRuntime().exec(cmd);
	            return "";
	        } catch (IOException ioe) {
	            return ioe.getMessage();
	        }
	    }

	    
	    private void sysOutCommandLine(String[] cmd) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("Command line:");
	        for (String s : cmd) {
	            sb.append(' ').append(s);
	        }
	        System.out.println(sb);
	    }
	    
}
