package it.eng.spago.cms.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class CMSRecovery {

	private static boolean cmsModified = false;

	private static CMSRepository cmsRepositoryPaths = null;

	static {
		Properties props = new Properties();
		cmsRepositoryPaths = new CMSRepository();
		try {
			props.load(cmsRepositoryPaths.getClass().getResourceAsStream(
					"/jackrabbitSessionFactory.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cmsRepositoryPaths.setRepositorPath(props
				.getProperty("repository_path"));
		cmsRepositoryPaths.setZipRepositoryFileForRecovery(props
				.getProperty("zip_repository_file_for_recovery"));
	}

	public synchronized static void setupCms() throws IOException {
		if (cmsModified) {
			try {
				File directoryToDelete = new File(cmsRepositoryPaths.getRepositorPath());
				if (directoryToDelete.exists()) {
					if (!directoryToDelete.isDirectory())
						throw new IOException("Directory not found");
					deleteDirectory(directoryToDelete);
				}
				File repository_zip = new File(cmsRepositoryPaths.getZipRepositoryFileForRecovery());
				if (!repository_zip.exists() || !repository_zip.isFile())
					throw new IOException("Zip file for recovery not found");
				File newDirectory = new File(cmsRepositoryPaths.getRepositorPath());
				if (!newDirectory.mkdir())
					throw new IOException("Error while making directory");
				unzip(repository_zip, newDirectory);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// recoveryDirectory(repository_zip, newDirectory);
		}
	}

	private static void unzip(File repository_zip, File newDirectory)
			throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(repository_zip);
		Enumeration entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (entry.isDirectory()) {
				(new File(newDirectory.getParent() + File.separator
						+ entry.getName())).mkdir();
				continue;
			}
			copyInputStream(zipFile.getInputStream(entry),
					new BufferedOutputStream(new FileOutputStream(newDirectory
							.getParent()
							+ File.separator + entry.getName())));
		}
		zipFile.close();
	}

	private static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
	}

	private static void deleteDirectory(File directory) throws IOException {
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					boolean deletion = file.delete();
					if (!deletion)
						throw new IOException("Error deleting file");
				} else
					deleteDirectory(file);
			}
		}
		boolean deletion = directory.delete();
		if (!deletion)
			throw new IOException("Error deleting file");
	}

	// private static void recoveryDirectory(File directoryToWrite,
	// File newDirectory) throws IOException {
	// if (directoryToWrite.isDirectory()) {
	// boolean newdirCreation = newDirectory.mkdir();
	// if (!newdirCreation)
	// throw new IOException("Error creating directory");
	// File[] files = directoryToWrite.listFiles();
	// for (int i = 0; i < files.length; i++) {
	// File file = files[i];
	// File newFile = new File(newDirectory.getAbsolutePath()
	// + File.separator + file.getName());
	// recoveryDirectory(file, newFile);
	// }
	// } else {
	// FileInputStream inStream = new FileInputStream(directoryToWrite);
	// FileOutputStream outStream = new FileOutputStream(newDirectory);
	// int i;
	// while ((i = inStream.read()) != -1) {
	// outStream.write(i);
	// }
	// inStream.close();
	// outStream.close();
	// }
	// }

	public static boolean isCmsModified() {
		return cmsModified;
	}

	public static void setCmsModified(boolean cmsModified) {
		CMSRecovery.cmsModified = cmsModified;
	}

}
