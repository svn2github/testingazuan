/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

@Author Marco Cortella

**/
package bi.bmm.util;

import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer ;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//TODO: CANCELLARE LE INFO TRACCIANTI

public class myClassLoader extends ClassLoader {
    private Hashtable classes = new Hashtable();
    protected String path ;
    protected String extension ;

    public myClassLoader( String path, String extension ) {
      this.path = path ;
      this.extension = extension ;
    }

    /* Carica il contenuto di un file .class */
    private byte getClassBytes(String className)[] {
        //System.out.println("Lettura dati da file per la classe "+className);
        byte result[];
        try {
            result = loadClassBytes( className ) ;
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    protected byte[] loadClassBytes( String className )
      throws IOException {
        String fileSeparator = System.getProperty( "file.separator" ) ;
        className = className.replace( '.', fileSeparator.charAt(0) ) +
          "." + extension ;
       // System.out.println( "Nome file : " + className ) ;
        String classpath = System.getProperty( "java.class.path" ) ;
        StringTokenizer st = new StringTokenizer( 
          classpath, System.getProperty("path.separator") ) ;
        File classFile = null ;
        File dir = null ;
        String[] pathTokens = path.split(";");
        
        for (int i=0;i<pathTokens.length;i++)
        {
          String pathToken=pathTokens[i];
          //MC: controlla se si tratta di un jar
          if (pathToken.endsWith(".jar")) {
        	  ZipFile zipFile = new ZipFile(pathToken);
        	  ZipEntry zipEntry = zipFile.getEntry(className.replace('\\', '/'));
        	  //cerca la classe all'interno del jar
        	  if (zipEntry!=null) {
        		  byte[] res = new byte[(int)zipEntry.getSize()];
        		  BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
        		  bis.read(res, 0, res.length);
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
        		  return res;
        	  }
          }
          //cerca il class NON dentro jar
          else {
              dir = new File( pathToken ) ;
              //System.out.println( "directory : " + pathToken ) ;
              classFile = new File( dir, className ) ;
              if ( classFile.exists() )
                break ; 
          }
        }       
        
        FileInputStream fi = new FileInputStream( classFile ) ;
        byte result[] = new byte[ fi.available() ] ;
        fi.read( result ) ;
        return result ;
    }

    // Questa e' la versione chiamata */
    public Class loadClass(String className) throws ClassNotFoundException {
        return (loadClass(className, false));
    }

    // Questa e' la versione completa */
    public synchronized Class loadClass(String className, boolean resolveIt)
        throws ClassNotFoundException {
        Class result=null;
        byte  classData[];

        //System.out.println("Caricamento della classe : "+className);

        // Prima si controlla la cache */
        result = (Class)classes.get(className);
        if (result != null) {
            //System.out.println("Utilizzata la copia nella cache.");
            return result;
        }
        // Prima si prova col primordial class loader */
        try {
            result = super.findSystemClass(className);
            //System.out.println("Classe di sistema (CLASSPATH)");
            return result;
        } catch (ClassNotFoundException e) {
            //System.out.println("Non e' una classe di sistema.");
            if ( className.startsWith( "java.") ) {
              //System.out.println("Classe pericolosa : " + className ) ;
              //System.out.println( "Caricamento abortito" ) ;
              throw new ClassNotFoundException();
            }
        }

        // poi si prova a caricare la classe dal path specificato */
        classData = getClassBytes(className);
        if (classData == null) {
            throw new ClassNotFoundException();
        }

        // viene eseguito il parsing, e costruito l'oggetto class  */
        result = defineClass(className,classData, 0, classData.length);
        if (result == null) {
            throw new ClassFormatError();
        }

        if (resolveIt) {
            resolveClass(result);
        }

        // Si aggiorna la cache 
       
        classes.put(className, result);
       
       //System.out.println("Classe caricata : " +className);
        return result;
    }
}
