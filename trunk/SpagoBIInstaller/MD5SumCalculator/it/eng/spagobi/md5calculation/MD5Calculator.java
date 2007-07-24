package it.eng.spagobi.md5calculation;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class MD5Calculator {

	private static String algorithm = "MD5";
	private static int readBufferSize = 8 * 1024;
	
	public static void main(String[] args) {
		try {
			String basePath = "C:\\Progetti\\SpagoBI\\Rilasci\\2007-07-06 SpagoBI platform 1.9.3\\";
			String[] fileNames = new String[] { 
					"SpagoBI-bin-1.9.3_07062007.zip",
					"SpagoBIBirtReportEngine-bin-1.9.3_07062007.zip",
					"SpagoBIGeoEngine-bin-1.9.3_07062007.zip",
					"SpagoBIJasperReportEngine-bin-1.9.3_07062007.zip",
					"SpagoBIJPivotEngine-bin-1.9.3_07062007.zip",
					"SpagoBIQbeEngine-bin-1.9.3_07062007.zip",
					"SpagoBIWekaEngine-bin-1.9.3_07062007.zip",
					"SpagoBITalendEngine-bin-1.9.3_07062007.zip",
					"ExoProfileAttributesManagerModule-bin-1.9.3_07062007.zip",
					"SpagoBIBookletsComponent-bin-1.9.3_07062007.zip"
					};
//			System.out.println("************** MD5 sun calculation *****************");
//			for (int i = 0; i < fileNames.length; i++) {
//				String fileName = basePath + fileNames[i];
//				String checksum = calculateMd5(fileName);
//				System.out.println("MD5 value for file " + fileNames[i] + " is : [" + checksum + "]");
//			}

			System.out.println("************** File dimensions *****************");
			for (int i = 0; i < fileNames.length; i++) {
				String fileName = basePath + fileNames[i];
				File file = new File(fileName);
				System.out.println(fileNames[i] + " dimension is " + file.length());
			}
			
			basePath = "C:\\Progetti\\SpagoBI\\Rilasci\\2007-07-23 Correzioni\\";
			File folder = new File(basePath);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String fileName = file.getName();
				System.out.println(fileName + " dimension is " + file.length());
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
