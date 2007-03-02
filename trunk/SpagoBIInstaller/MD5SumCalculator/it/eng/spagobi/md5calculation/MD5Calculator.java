package it.eng.spagobi.md5calculation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import com.izforge.izpack.installer.WebAccessor;

public class MD5Calculator {

	private static String algorithm = "MD5";
	private static int readBufferSize = 8 * 1024;
	
	public static void main(String[] args) {
		try {
			String basePath = "C:\\Progetti\\SpagoBI\\rilasci\\2007-01-23 SpagoBI 1.9.2\\";
			String[] fileNames = new String[] { 
					"SpagoBI-bin-1.9.2__build02062007.zip",
					"SpagoBIBirtReportEngine-bin-1.9.2.zip",
					"SpagoBIGeoEngine-bin-1.9.2-Beta.zip",
					"SpagoBIJasperReportDriver-bin-1.9.2.zip",
					"SpagoBIJPivotEngine-bin-1.9.2.zip",
					"SpagoBIQbeEngine-bin-1.9.2.zip",
					"SpagoBIWekaEngine-bin-1.9.2-Beta.zip"					
					};
			for (int i = 0; i < fileNames.length; i++) {
				String fileName = basePath + fileNames[i];
				String checksum = calculateMd5(fileName);
				System.out.println("MD5 value for file " + fileNames[i] + " is : [" + checksum + "]");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String calculateMd5(String fileName) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        File src = new File(fileName);
        FileInputStream fis = new FileInputStream(src);
        
        byte[] buf = new byte[readBufferSize];
        DigestInputStream dis = new DigestInputStream(fis, messageDigest);
        while (dis.read(buf, 0, readBufferSize) != -1) {
            ;
        }
        dis.close();
        fis.close();
        fis = null;
        byte[] fileDigest = messageDigest.digest();
        String checksum = createDigestString(fileDigest);
        return checksum;
	}
	
    private static String createDigestString(byte[] fileDigest) {
        StringBuffer checksumSb = new StringBuffer();
        for (int i = 0; i < fileDigest.length; i++) {
            String hexStr = Integer.toHexString(0x00ff & fileDigest[i]);
            if (hexStr.length() < 2) {
                checksumSb.append("0");
            }
            checksumSb.append(hexStr);
        }
        return checksumSb.toString();
    }
	
}
