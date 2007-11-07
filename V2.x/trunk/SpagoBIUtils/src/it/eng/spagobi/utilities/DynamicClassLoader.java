package it.eng.spagobi.utilities;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
 
public class DynamicClassLoader extends URLClassLoader {
	
	private ClassLoader parentCL = null;
	private File jar;
 
	public DynamicClassLoader(String jarFileName, ClassLoader cl) {		
		this (new File(jarFileName), cl);
	}
 
	public DynamicClassLoader(File jarFile, ClassLoader cl) {
		super(new URL[0], cl);
		jar = jarFile;
		parentCL = cl;
	}
 
	public Class loadClass(String className) throws ClassNotFoundException {
		return (loadClass(className, true));
	}
 
	public synchronized Class loadClass(String className, boolean resolve) throws ClassNotFoundException {
		
		Class classToReturn = null;
		try {
			classToReturn = super.loadClass(className, resolve);
		} catch (Exception e) {
			//System.out.println(e);
		}
		if(classToReturn == null) {
			ZipFile zipFile = null;
			BufferedInputStream bis = null;
			byte[] res = null;
			try {
				zipFile = new ZipFile(jar);
				ZipEntry zipEntry = zipFile.getEntry(className.replace('.', '/')+".class");
				res = new byte[(int)zipEntry.getSize()];
				bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
				bis.read(res, 0, res.length);
			} catch (Exception ex) {
				System.out.println("className: " + className + " Exception: "+ ex);
			} finally {
				if (bis!=null) {
					try {
						bis.close();
					} catch (IOException ioex) {}
				}
				if (zipFile!=null) {
					try {
						zipFile.close();
					} catch (IOException ioex) {}
				}
			}
			if (res == null) 
				return super.findSystemClass(className);
	 
			classToReturn = defineClass(className, res, 0, res.length);
			if (classToReturn == null) 
				throw new ClassFormatError();
	 
			if (resolve) 
				resolveClass(classToReturn);
		}
		return classToReturn;
	}
	
}
