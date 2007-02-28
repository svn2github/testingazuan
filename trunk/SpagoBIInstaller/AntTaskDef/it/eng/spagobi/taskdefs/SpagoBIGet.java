/*
 *
 * License
 * MyJavaPack is licensed according to the terms of the Apache License, Version 2.0.
 * 
 * See http://www.apache.org/licenses/LICENSE-2.0.txt for more details
 * 
 */
package it.eng.spagobi.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * 
 * <p>
 * This is the SpagoBI get ant task, derived from from MyJavaPack ant task:
 * see http://www.open-centric.com/myjavapack/ for more information
 * </p>
 * <p>
 * It combines Ant's Get, Md5Sum tasks and a download mirror scheme.
 * </p>
 * <p>
 * This class will download via a random mirror selection.
 * </p>
 * <p>
 * If the build.properties file does not contain a mirror entry, we will still
 * do a checksum, on a local file.
 * </p>
 * 
 * @author Davide Zerbetto
 *  
 */
public class SpagoBIGet extends Task {

	/**
	 * REQUIRED - list of http/ftp mirror prefixes
	 */
	private String mirrorPrefixList;

	/**
	 * REQUIRED - htp/ftp mirror suffix
	 */
	private String mirrorSuffix;

	/**
	 * REQUIRED - md5sum that the http/ftp pack must match
	 */
	private String md5sum;

	private String separator = File.separator;

	/**
	 * DERIVED - prefix element + suffix element
	 */
	private ArrayList mirrorURLList = new ArrayList();

	/**
	 * REQUIRED - the local SpagoBI repositoty directory path
	 */
	private String spagobiRepositoryDir;
	
	/**
	 * Default empty constructor.
	 */
	public SpagoBIGet() {

	}

	/**
	 * Does the work.
	 * 
	 * Fetches the pack via HTTP/FTP, md5checks it, applies 777
	 * permissions if it's a unix/linux machine.
	 * 
	 * @exception BuildException
	 *                Thrown in unrecoverable error.
	 */
	public void execute() throws BuildException {
		try {

			// controls if the local spagobi repository folder exists, if not it creates it
			controlSpagoBILocalRepository();
			
			// srcZipFile is the file in the local spagobi repository
			File srcZipFile = new File(getSourceZipFile(this.mirrorSuffix));
			// zipFile is the file in the installation folder: it is copied from the local repository or 
			// downloaded from the remote spagobi repository via http
			File zipFile = new File(getZipFileDest(this.mirrorSuffix));
			// cleans pre-existing files
			if (zipFile.exists()) zipFile.delete();
			
			boolean fetch = true;
			
			if (srcZipFile.exists()) {
				if (!srcZipFile.isDirectory()) {
					log("Info - found source file " + srcZipFile.getAbsolutePath());
					fetch = false;
					copyFile(zipFile, srcZipFile);
				} else {
					log("Info - found source file " + srcZipFile.getAbsolutePath());
					srcZipFile.delete();
				}
			} 
			
			//get the zip file from Internet via http, ftp
			//only if fetch is required
			if (fetch) {
				
				URL getURL;
				boolean done = false;
				//Set the mirror URL list
				this.setMirrorURLList(this.mirrorPrefixList, this.mirrorSuffix);

				//keep trying to get from other mirrors even if this one is
				// down.
				while (this.mirrorURLList.size() > 0 && done == false) {
					int randomIndex = this.getRandomURLIndex();
					getURL = new URL((String) this.mirrorURLList
							.get(randomIndex));

					try {
						httpGet(getURL, zipFile);
						done = true;
						copyFile(srcZipFile, zipFile);
					} catch (BuildException ex) {
						log("Warning - Could not get from this URL: "
								+ getURL.toString());
						this.mirrorURLList.remove(randomIndex);
					}
				}
				if (this.mirrorURLList.size() == 0) {
					throw new BuildException(
							"No good URLs for this pack exist, or Internet connection down: ");
				}
			}

			//do the checksum
			if (!md5SumMatch(this.md5sum, zipFile))
				throw new BuildException("MD5Sum Mismatch: " + zipFile);

			setPermissions();

		} catch (MalformedURLException urlExc) {
			urlExc.printStackTrace();
			throw new BuildException(urlExc);
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new BuildException(exc);
		}
	}

	private void controlSpagoBILocalRepository() {
		File spagobiRepositoryFolder = new File(spagobiRepositoryDir);
		if (!spagobiRepositoryFolder.exists()) {
			spagobiRepositoryFolder.mkdirs();
		} else if (!spagobiRepositoryFolder.isDirectory()) {
			log("ERROR: spagobi local repository [" + spagobiRepositoryDir + "] must be a directory");
		}
	}
	
	/**
	 * Get the http/ftp mirror suffix
	 * 
	 * @return Returns the mirrorSuffix.
	 */
	public String getMirrorSuffix() {
		return mirrorSuffix;
	}

	/**
	 * Set the http/ftp mirror suffix
	 * 
	 * @param mirrorSuffix
	 *            The mirrorSuffix to set.
	 */
	public void setMirrorSuffix(String mirrorSuffix) {
		this.mirrorSuffix = mirrorSuffix;
	}

	/**
	 * Get the md5sum.
	 * 
	 * @return Returns the md5sum.
	 */
	public String getMd5sum() {
		return md5sum;
	}

	/**
	 * Set the md5sum.
	 * 
	 * @param md5sum
	 *            The md5sum to set.
	 */
	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}

	/**
	 * Get the http/ftp mirror prefix list.
	 * 
	 * Must be comma separated list.
	 * 
	 * @return Returns the mirrorPrefixList.
	 */
	public String getMirrorPrefixList() {
		return mirrorPrefixList;
	}

	/**
	 * 
	 * Set the http/ftp mirror prefix list.
	 * 
	 * Must be comma separated list.
	 * 
	 * @param mirrorPrefixList
	 *            The mirrorPrefixList to set.
	 */
	public void setMirrorPrefixList(String mirrorPrefixList) {
		this.mirrorPrefixList = mirrorPrefixList;
	}

	/**
	 * Return a single random mirror prefix index.
	 * 
	 * @return random arraylist index
	 */
	private int getRandomURLIndex() {

		Random random = new Random();
		int randomInt = random.nextInt(this.mirrorURLList.size());

		return randomInt;
	}

	/**
	 * Set the URL mirrors from the mirror prefix list and the mirror suffix.
	 * 
	 * @param mirrorPrefixList
	 * @param mirrorSuffix
	 */
	private void setMirrorURLList(String mirrorPrefixList, String mirrorSuffix) {
		mirrorPrefixList = mirrorPrefixList.trim();
		StringTokenizer tokenizer = new StringTokenizer(mirrorPrefixList, ",");
		String url;
		while (tokenizer.hasMoreTokens()) {
			url = tokenizer.nextToken() + mirrorSuffix;
			this.mirrorURLList.add(url);
			log("Adding this URL to the URL mirror list - " + url);
		}
	}

	/**
	 * Get the destination location of the zip file or tarball. This should be
	 * in the same directory as the build script, and the name should be the
	 * same as the mirrorSuffix file name. We just chop off any mirror subdirs.
	 * 
	 * @param mirror
	 *            suffix
	 * @return
	 */
	private String getZipFileDest(String mirrorSuffix) {
		int substrIndex = mirrorSuffix.lastIndexOf("/") + 1;
		StringBuffer buf = new StringBuffer(this.getProject().getBaseDir()
				.getAbsolutePath());
		String zipFileDest;
		buf.append(separator);
		buf.append(mirrorSuffix.substring(substrIndex));
		zipFileDest = buf.toString();
		log("Getting zip file destination - " + zipFileDest);
		return zipFileDest;
	}

	private String getSourceZipFile(String mirrorSuffix) {
		int substrIndex = mirrorSuffix.lastIndexOf("/") + 1;
		StringBuffer buf = new StringBuffer(spagobiRepositoryDir);
		buf.append(separator);
		buf.append(mirrorSuffix.substring(substrIndex));
		String sourceZipFile = buf.toString();
		log("Source file path: " + sourceZipFile);
		return sourceZipFile;
	}
	
	/**
	 * Call ant get to pull the file from the Internet.
	 * 
	 * @param URL -
	 *            the URL we want to fetch from
	 * @param File -
	 *            The destination file
	 */
	private void httpGet(URL url, File destFile) throws BuildException {

		Get getTask = new Get();

		//set the OPTIONAL ANT parms
		getTask.setSrc(url);
		getTask.setDest(destFile);
		getTask.setUseTimestamp(true);

		log("Getting URL from Internet/URL - " + url);
		log("Getting URL from Internet/Dest File - " + destFile);

		//execute the get task
		getTask.execute();
	}

	/**
	 * Set the permissions of basedir to 700, recursively.
	 * 
	 * Only for unix/linux. Doesn't do anything in windows.
	 */
	private void setPermissions() {

		String os = System.getProperty("os.name").toLowerCase(Locale.US);
		try {
			//only do this if it's unix/linux.
			if (os.indexOf("unix") != -1 || os.indexOf("linux") != -1) {
				log("Setting file permissions to 777: "
						+ this.getProject().getBaseDir().getAbsolutePath());

				Runtime rt = Runtime.getRuntime();
				File bindir = new File(this.getProject().getBaseDir()
						.getAbsolutePath());
				File basedir = bindir.getParentFile();
				String cmd = "chmod -R 777 " + basedir.getAbsolutePath();
				rt.exec(cmd);
			}
		} catch (IOException ex) {
			throw new BuildException("Could not change permissions: " + ex);
		}
	}

	/**
	 * Perform an md5sum check on the zip file/tarball. Use the md5sum parm as a
	 * 
	 * @return true if checksums match.
	 */
	private boolean md5SumMatch(String md5Sum, File file) {
		Checksum md5sumTask = new Checksum();
		String verifyProp = "true";

		//set the required ANT parms
		md5sumTask.setProperty(md5Sum);
		md5sumTask.setVerifyproperty(verifyProp);
		md5sumTask.setFile(file);

		log("Checking md5sum/checksum - " + md5sum);

		md5sumTask.execute();
		verifyProp = md5sumTask.getVerifyProperty();
		log("Checking md5sum/verifyProp - " + verifyProp);

		if (verifyProp.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}

	/**
	 * Copy a file from a src dir to a dest dir. Does not unpack it!
	 * 
	 * @param dest -
	 *            destination
	 * @param src -
	 *            source
	 * @throws IOException
	 */
	private void copyFile(File dest, File src) throws IOException {
		int buffSize = 100000;
		byte[] buffer = new byte[buffSize];
		File destDir = dest.getParentFile();
		destDir.mkdirs();

		InputStream in = null;
		OutputStream out = null;
		try {

			log("copy source file - " + src);
			log("copy dest file - " + dest);
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);

			while (true) {
				synchronized (buffer) {
					int amountRead = in.read(buffer);
					if (amountRead == -1) {
						break;
					}
					out.write(buffer, 0, amountRead);
				}
			}

		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}
	
}