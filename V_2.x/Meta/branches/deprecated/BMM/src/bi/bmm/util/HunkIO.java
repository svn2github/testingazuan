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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * read/write a file in one large hunk.
 * <p/>
 * Variants to read/write bytes, strings with default encoding and strings with specified encoding.
 * No main method.
 * We don't use java.nio.charset.Charset because it requires JDK 1.4+.
 * See also the more elaborate filetransfer package.
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.7 2009-05-03 - create PrintWriterPlus vs PrintWriterPortable
 * @since 2002-07-12
 */
public final class HunkIO
    {
    // ------------------------------ CONSTANTS ------------------------------

    /**
     * true to include the debugging harness code
     */
    private static final boolean DEBUGGING = false;
    public static final String DEFAULT_INFO_FILE = "bminfo.bm";

    /**
     * undisplayed copyright notice
     *
     * @noinspection UnusedDeclaration
     */
    /*
    private static final String EMBEDDED_COPYRIGHT =
            "copyright (c) 1999-2009 Roedy Green, Canadian Mind Products, http://mindprod.com";

    /**
     * @noinspection UnusedDeclaration
     */
    /*
    private static final String RELEASE_DATE = "2009-05-03";

    /**
     * embedded version string.
     *
     * @noinspection UnusedDeclaration
     */
    /*
    private static final String VERSION_STRING = "1.7";
    */
    // -------------------------- PUBLIC STATIC METHODS --------------------------

    /**
     * Create a temporary file, Slightly smarter version of File.createTempFile
     *
     * @param prefix beginning letters of filename
     * @param suffix ending letters of filename. e.g. ".temp". null means ".tmp"
     * @param near   directory where to put file, or file to place this temp file near in the same directory. null means
     *               put the temp file in the current directory.
     *
     * @return A temporary file. It will not automatically delete on program completion, however.
     * @throws java.io.IOException when cannot create file.
     */
    public static File createTempFile( String prefix,
                                       String suffix,
                                       File near ) throws IOException
        {
        if ( near != null )
            {
            if ( near.isDirectory() )
                {
                return File.createTempFile( prefix, suffix, near );
                }
            else if ( near.isFile() )
                {
                String parent = near.getParent();
                if ( parent != null )
                    {
                    File dir = new File( parent );
                    if ( dir.isDirectory() )
                        {
                        return File.createTempFile( prefix, suffix, dir );
                        }
                    }
                }
            }
        // anything else, just create in the current directory.
        return File.createTempFile( prefix, suffix );
        }

    /**
     * read file of bytes without conversion.
     *
     * @param fromFile file to read.
     *
     * @return byte array representing whole file.
     * @throws IOException when cannot access file.
     */
    public static byte[] rawReadEntireFile( String fromFile ) throws IOException
        {
        return rawReadEntireFile( new File( fromFile ) );
        }

    /**
     * read file of bytes without conversion
     *
     * @param fromFile file to read
     *
     * @return byte array representing whole file
     * @throws IOException when cannot access file.
     * @noinspection WeakerAccess
     */
    public static byte[] rawReadEntireFile( File fromFile ) throws IOException
        {
        int size = ( int ) fromFile.length();

        FileInputStream fis = new FileInputStream( fromFile );
        // R E A D
        byte[] rawContents = new byte[size];
        int bytesRead = fis.read( rawContents );
        if ( bytesRead != size )
            {
            throw new IOException( "error: problems reading file " + fromFile );
            }
        // C L O S E
        fis.close();
        return rawContents;
        }

    /**
     * Get all text in a file.
     *
     * @param fromFile file where to get the text.
     *
     * @return all the text in the File.
     * @throws IOException when cannot access file.
     * @noinspection WeakerAccess
     */
    public static String readEntireFile( File fromFile ) throws IOException
        {
        // decode with default encoding and return entire file as one big string
        return new String( rawReadEntireFile( fromFile ) );
        }// end readEntireFile

    /**
     * Get all text in a file.
     *
     * @param fromFile Name of file where to get the text.
     *
     * @return all the text in the File.
     * @throws IOException when cannot access file.
     */
    public static String readEntireFile( String fromFile ) throws IOException
        {
        return readEntireFile( new File( fromFile ) );
        }// end readEntireFile

    /**
     * Get all text in a file.
     *
     * @param fromFile file where to get the text.
     * @param encoding name of the encoding to use to translate the bytes in the file into chars e.g. "windows-1252"
     *                 "UTF-8" "UTF-16"
     *
     * @return all the text in the File.
     * @throws IOException when cannot access file.
     * @noinspection WeakerAccess
     */
    public static String readEntireFile( File fromFile,
                                         String encoding ) throws IOException
        {
        // decode with encoding and return entire file as one big string
        return new String( rawReadEntireFile( fromFile ), encoding );
        }// end readEntireFile

    /**
     * Get all text in a file.
     *
     * @param fromFile Name of file where to get the text.
     * @param encoding name of the encoding to use to translate the bytes in the file into chars e.g. "windows-1252"
     *                 "UTF-8" "UTF-16"
     *
     * @return all the text in the File.
     * @throws IOException when cannot access file.
     */
    public static String readEntireFile( String fromFile,
                                         String encoding ) throws IOException
        {
        return readEntireFile( new File( fromFile ), encoding );
        }// end readEntireFile

    /**
     * Write all the text in a file
     *
     * @param toFile file where to write the text
     * @param text   Text to write
     *
     * @throws IOException when cannot access file.
     * @noinspection WeakerAccess
     */
    public static void writeEntireFile( File toFile,
                                        String text ) throws IOException
        {
        // default encoding
        FileWriter fw = new FileWriter( toFile );
        // W R I T E
        fw.write( text );
        // C L O S E
        fw.close();
        }// end writeEntireFile

    /**
     * Write all the text in a file
     *
     * @param toFile Name of file where to write the text
     * @param text   Text to write
     *
     * @throws IOException when cannot access file.
     */
    public static void writeEntireFile( String toFile,
                                        String text ) throws IOException
        {
        writeEntireFile( new File( toFile ), text );
        }// end writeEntireFile

    /**
     * Write all the text in a file
     *
     * @param toFile   file where to write the text
     * @param text     Text to write
     * @param encoding name of the encoding to use to translate chars to bytes e.g. "windows-1252" "UTF-8" "UTF-16"
     *
     * @throws IOException when cannot access file.
     * @throws java.io.UnsupportedEncodingException
     *                     If the named encoding is not supported
     * @noinspection WeakerAccess
     */
    public static void writeEntireFile( File toFile,
                                        String text,
                                        String encoding ) throws IOException
        {
        // supplied encoding
        FileOutputStream fos = new FileOutputStream( toFile );
        OutputStreamWriter osw = new OutputStreamWriter( fos, encoding );
        // W R I T E
        osw.write( text );
        // C L O S E
        osw.close();
        }// end writeEntireFile

    /**
     * Write all the text in a file
     *
     * @param toFile   file where to write the text
     * @param text     Text to write
     * @param encoding name of the encoding to use to translate chars to bytes e.g. "windows-1252" "UTF-8" "UTF-16"
     *
     * @throws IOException when cannot access file.
     * @throws java.io.UnsupportedEncodingException
     *                     If the named encoding is not supported
     */
    public static void writeEntireFile( String toFile,
                                        String text,
                                        String encoding ) throws IOException
        {
        writeEntireFile( new File( toFile ), text, encoding );
        }// end writeEntireFile

    // --------------------------- CONSTRUCTORS ---------------------------

    /**
     * all methods are static, so hidden constructor
     */
    private HunkIO()
        {
        }

    // --------------------------- main() method ---------------------------

    /**
     * debugging harness for HunkIO.
     *
     * @param args not used.
     */
    public static void main( String[] args )
        {
        if ( DEBUGGING )
            {
            try
                {
                writeEntireFile( "C:/Users/Emylio/temp.txt",
                        "abcedefghijklmnopqrstuvwxyz",
                        "UTF-8" );
                String result = readEntireFile( "C:/Users/Emylio/temp.txt", "UTF-8" );
                System.out.println( result );
                }
            catch ( IOException e )
                {
                System.err.println( e );
                }
            }
        }
    }// end HunkIO
