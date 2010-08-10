package it.eng.test;

import it.eng.qbe.utility.Logger;

public class MainAAA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.debug(MainAAA.class,"Current Thread Classloader " + Thread.currentThread().getContextClassLoader());
		
		try{
			JarClassLoader jarClassLoader = new JarClassLoader("C://tmp//andrea.jar");
			
			Logger.debug(MainAAA.class,"Parent of jar Classloader " + jarClassLoader.getParent());
		
			Object o = jarClassLoader.loadClass("it.eng.andrea.Zoppello").newInstance();
			
			Logger.debug(MainAAA.class,o.getClass().getName());
		}catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
